package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import org.springframework.web.multipart.MultipartFile;

public interface GitHubService {

    void addFileForRepository(Long projectId, MultipartFile file);

    void initRepository(Simulation simulation, String login);

    void checkRepository(Simulation simulation, TaskInSimulation task);
}
