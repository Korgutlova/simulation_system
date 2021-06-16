package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.repository.CheckRepoRepository;
import com.korgutlova.diplom.service.api.GitHubService;
import com.korgutlova.diplom.service.api.ProjectService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {

    private final CheckRepoRepository checkRepoRepository;
    private final ProjectService projectService;

    private final SimulationService simulationService;

    private final ResourceLoader resourceLoader;

    @Value("${github.oauth}")
    private String oauth;

    @Value("${github.files.folder}")
    private String folderName;

    @Value("${github.workflows.folder}")
    private String workflowsFolder;

    private final static String REQUIRED_TYPE = "application/x-zip-compressed";

    @Override
    public void addFileForRepository(Long projectId, MultipartFile file) {
        Project project = projectService.findById(projectId);
        if (project == null) {
            log.warn("Project " + projectId + " is not exist");
            return;
        }

        // проверка расширения и contentType
        log.info(file.getContentType());
        if (!Objects.equals(file.getContentType(), REQUIRED_TYPE)) {
            log.warn("Wrong type of uploaded file. Needed zip file.");
            return;
        }

        // инициализация папки если ее нет
        File folder = new File(folderName + "\\" + project.getShortName());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            // распаковка архива

            unzipFile(file, folder);

            // добавление workflow файла
            addWorkflowFile(project);
        } catch (IOException e) {

            // если возникла ошибка удаляем папку

            log.error("Произошла ошибка во время распаковки архива");
            folder.delete();
        }
    }

    private void unzipFile(MultipartFile file, File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private void addWorkflowFile(Project project) throws IOException {
        if (project.getLanguage() != null && !project.getLanguage().isEmpty()) {
            Resource resource = resourceLoader.getResource(workflowsFolder +
                    project.getLanguage().toLowerCase() + ".yaml");
            if (resource != null) {
                String directory = folderName + "\\" + project.getShortName() + "\\.github\\workflows";
                File workflow = new File(directory);
                File workflowFile = new File(directory + "\\ci.yaml");
                workflow.mkdirs();
                workflowFile.createNewFile();
                Files.copy(resource.getInputStream(), workflowFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    @Override
    public void initRepository(Simulation simulation, String login) {
        try {
            GitHub github = new GitHubBuilder()
                    .withOAuthToken(oauth)
                    .build();
            GHUser user = github.getUser(login);
            log.info("In collaborators added user " + user.getLogin());

            GHRepository repository = createRepo(github, user, simulation);

            simulation.setNameRepo(repository.getName());

            simulationService.save(simulation);

        } catch (GHFileNotFoundException e) {
            log.info("Account " + login + " is not exist");
        } catch (IOException e) {
            log.error("Errors in working with github");
        }
    }

    private GHRepository createRepo(GitHub github, GHUser user, Simulation simulation) throws IOException {
        GHRepository repository = github
                .createRepository(simulation.getProject().getShortName() + "_" + user.getLogin())
                .create();
        repository.setPrivate(true);
        repository.setDescription(simulation.getProject().getDescription());
        repository.addCollaborators(GHOrganization.Permission.PUSH, user);

        //пустой README.md
        repository.createContent()
                .branch("master")
                .content("Info about project " + simulation.getProject().getName())
                .path("README.md")
                .message("Add README.md to project " + simulation.getProject().getName())
                .commit();

        //коммит со всеми файлами
        createInitCommit(repository, simulation);

        return repository;
    }

    private void createInitCommit(GHRepository repo, Simulation simulation) throws IOException {
        //получение refs
        GHRef mainRef = repo.getRef("heads/master");

        //sha
        String mainTreeSha = repo.getTreeRecursive("master", 1).getSha();

        //само дерево с изменениями
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(mainTreeSha);

        //добавляем файлы
        File folder = new File(folderName + "\\" + simulation.getProject().getShortName());
        Files.walk(Paths.get(folder.getPath()))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        File file = path.toFile();
                        String namePath = file.getCanonicalPath()
                                .substring(folder.getCanonicalPath().length() + 1)
                                .replace("\\", "/");

                        System.out.println("name " + namePath);

                        String info = FileUtils.readFileToString(file, "UTF-8");
                        treeBuilder.add(namePath, info, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        //новое sha tree
        String treeSha = treeBuilder.create().getSha();

        //сам коммит
        GHCommit commit = repo.createCommit()
                .message("Initialize project " + simulation.getProject().getName())
                .tree(treeSha)
                .parent(mainRef.getObject().getSha())
                .create();

        String commitSha = commit.getSHA1();

        //обновление в репозитории
        mainRef.updateTo(commitSha);
    }

    @Override
    public void checkRepository(Simulation simulation, TaskInSimulation task) {

    }
}
