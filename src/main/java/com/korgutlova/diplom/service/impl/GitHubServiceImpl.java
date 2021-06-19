package com.korgutlova.diplom.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korgutlova.diplom.model.entity.CheckRepository;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.enums.simulation.StatusAction;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {

    private final CheckRepoRepository checkRepoRepository;
    private final ProjectService projectService;

    private final SimulationService simulationService;

    private final ResourceLoader resourceLoader;

    private final RestTemplate restTemplate;

    @Value("${github.oauth}")
    private String oauth;

    @Value("${github.files.folder}")
    private String folderName;

    @Value("${github.workflows.folder}")
    private String workflowsFolder;

    private final static String REQUIRED_TYPE = "application/x-zip-compressed";

    private final static String PATTERN_TASK = "[TASK_ID]";
    private final static String PATTERN_BRANCH = "feature-" + PATTERN_TASK;
    private final static String PATTERN_COMMIT = PATTERN_TASK + " ?: ?.*";

    private final static String URL_WORKFLOWS_RUN = "https://api.github.com/repos/%s/%s/actions/workflows/%s/runs?branch=%s";
    private final static String URL_WORKFLOWS = "https://api.github.com/repos/%s/%s/actions/workflows";

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
            simulation.setFullNameRepo(String.format("https://github.com/%s/%s",
                    github.getMyself().getLogin(), repository.getName()));

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
    public void checkRepository(TaskInSimulation task) throws IOException {
        GitHub github = new GitHubBuilder()
                .withOAuthToken(oauth)
                .build();
        GHRepository repository = github.getRepository(github.getMyself().getLogin() + "/" +
                task.getSimulation().getNameRepo());

        Optional<CheckRepository> checkRepositoryOpt = checkRepoRepository.findByTaskInSimulation(task);
        StringBuilder errors = new StringBuilder();
        GHPullRequest pullRequest;
        GHBranch branch = null;

        if (!(checkRepositoryOpt.isPresent() && checkRepositoryOpt.get().getNumberPR() == null)) {

            Map<String, GHBranch> branches = repository.getBranches();
            GHBranch tempBranch = branches.get(PATTERN_BRANCH.replace(PATTERN_TASK, task.getTask().getShortId()));

            //check branch
            if (tempBranch == null) {
                log.warn("Для задачи " + task.getTask().getShortId() +
                        " не было найдено ветки с соответствующим названием " +
                        PATTERN_BRANCH);
                return;
            }

            //check PR
            List<GHPullRequest> pullRequests = repository.getPullRequests(GHIssueState.OPEN);

            Optional<GHPullRequest> pullRequestOptional = pullRequests
                    .stream()
                    .filter(p -> p.getHead().getRef().equals(tempBranch.getName()))
                    .findFirst();

            if (!pullRequestOptional.isPresent()) {
                log.warn("Pull Request из ветки " + PATTERN_BRANCH.replace(PATTERN_TASK, task.getTask().getShortId()) +
                        " не было найдено");
                return;
            } else {
                pullRequest = pullRequestOptional.get();
            }

            branch = tempBranch;
        } else {
            pullRequest = repository.getPullRequest(checkRepositoryOpt.get().getNumberPR());
        }

        //check commit
        GHCommit commit = pullRequest.getHead().getCommit();

        if (!commit.getCommitShortInfo().getMessage()
                .matches(PATTERN_COMMIT.replace(PATTERN_TASK, task.getTask().getShortId()))) {
            errors.append("Ошибка в названии коммита, должно быть в формате " + PATTERN_COMMIT)
                    .append("\n");
        }

        //check status workflow
        StatusAction statusAction = checkWorkflowStatus(github, repository, branch);

        //save checkRepository
        saveCheckRepository(checkRepositoryOpt, task, pullRequest, commit,
                branch, errors.toString(), statusAction);
    }

    private StatusAction checkWorkflowStatus(GitHub github, GHRepository repository,
                                             GHBranch branch) throws IOException {

        JsonNode root = sendRequest(String.format(URL_WORKFLOWS,
                github.getMyself().getLogin(), repository.getName()));

        JsonNode nextWorkflowId = root.get("workflows").elements().next();
        JsonNode workflowId = nextWorkflowId.get("id");


        JsonNode lastRuns = sendRequest(String.format(URL_WORKFLOWS_RUN,
                github.getMyself().getLogin(), repository.getName(), workflowId,
                branch != null ? branch.getName() : ""));

        String status = lastRuns.get("workflow_runs").elements().next()
                .get("conclusion").asText();

        switch (status) {
            case "failure":
                return StatusAction.ERROR;
            case "success":
                return StatusAction.SUCCESS;
            default:
                return null;
        }
    }

    private JsonNode sendRequest(String url) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders() {{
                    set("Authorization", "token " + oauth);
                }}),
                String.class
        );
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(Objects.requireNonNull(response.getBody()));
    }

    private void saveCheckRepository(Optional<CheckRepository> checkRepositoryOpt, TaskInSimulation task,
                                     GHPullRequest pullRequest, GHCommit commit, GHBranch branch,
                                     String errors, StatusAction statusAction) throws IOException {

        CheckRepository checkRepository;
        if (checkRepositoryOpt.isPresent()) {
            checkRepository = checkRepositoryOpt.get();
        } else {
            checkRepository = new CheckRepository();
            checkRepository.setTaskInSimulation(task);
        }
        checkRepository.setNameBranch(branch != null ? branch.getName() : "");
        checkRepository.setNamePR(pullRequest != null ? pullRequest.getTitle() : "");
        checkRepository.setNumberPR(pullRequest != null ? pullRequest.getNumber() : null);
        checkRepository.setCommitSha(commit != null ? commit.getSHA1() : "");
        checkRepository.setNameCommit(commit != null ? commit.getCommitShortInfo().getMessage() : "");
        checkRepository.setErrors(errors);
        checkRepository.setStatusAction(statusAction != null ? statusAction : StatusAction.NOT_INITIALIZED);

        checkRepoRepository.save(checkRepository);
    }
}
