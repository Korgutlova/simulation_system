package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import com.korgutlova.diplom.service.api.GitHubService;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.TaskService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;
    private final SimulationService simulationService;
    private final TaskService taskService;

    @PostMapping(value = "/add_file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addFile(@RequestParam Long projectId,
                                          @RequestPart MultipartFile file) {
        gitHubService.addFileForRepository(projectId, file);
        return ResponseEntity.ok("Loaded");
    }

    @PostMapping("/init_repo")
    public ResponseEntity<String> initRepository(@RequestParam String login) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Simulation simulation = simulationService.findInitSimulation(currentUser);

        if (simulation == null || login == null || login.isEmpty()) {
            return ResponseEntity.badRequest().body("Repo was not initialize");
        }

        gitHubService.initRepository(simulation, login);
        return ResponseEntity.ok("Repo initialized");
    }

    @PostMapping("/check_repository")
    public ResponseEntity<String> checkRepository(@RequestParam Long taskId) throws IOException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskInSimulation task = taskService.findById(taskId);

        if (task == null || !task.getIsViewed()
                || task.getStatus() == TaskStatus.DONE || task.getStatus() == TaskStatus.CANCELLED ) {
            return ResponseEntity.badRequest().body("Check is cancelled");
        }

        if (!task.getSimulation().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.badRequest().body("Check is cancelled, task don't belong user" + currentUser.getId());
        }

        gitHubService.checkRepository(task);
        return ResponseEntity.ok("Loaded");
    }
}
