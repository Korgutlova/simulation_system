package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.repository.CheckRepoRepository;
import com.korgutlova.diplom.service.api.GitHubService;
import com.korgutlova.diplom.service.api.ProjectService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {
    private final CheckRepoRepository checkRepoRepository;
    private final ProjectService projectService;

    @Value("${github.oauth}")
    private String oauth;

    @Value("${github.files.folder}")
    private String folderName;

    private final static String REQIRED_TYPE = "application/x-zip-compressed";

    @Override
    public void addFileForRepository(Long projectId, MultipartFile file) {
        Project project = projectService.findById(projectId);
        if (project == null) {
            log.warn("Project " + projectId + " is not exist");
            return;
        }

        // проверка расширения и contentType
        log.info(file.getContentType());
        if (!Objects.equals(file.getContentType(), REQIRED_TYPE)) {
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

    @Override
    public void initRepository(Simulation simulation, String login) {

    }

    @Override
    public void checkRepository(Simulation simulation, TaskInSimulation task) {

    }
}
