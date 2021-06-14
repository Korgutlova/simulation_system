package com.korgutlova.diplom.job;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.TaskService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckActiveTasksScheduler {

    private final SimulationService simulationService;
    private final TaskService taskService;

    // каждый день в 9 утра
    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(cron = "*/30 * * * * *")
    private void checkActiveTasks() {
        log.info("Check active task job started ...");
        List<Simulation> simulations = simulationService.findActiveSimulations();
        for (Simulation simulation : simulations) {
            List<TaskInSimulation> taskInSimulations = taskService.findTasksBySimulation(simulation);
            Optional<TaskInSimulation> task = taskInSimulations
                    .stream()
                    .filter(t -> TaskStatus.CANCELLED != t.getStatus() && TaskStatus.DONE != t.getStatus())
                    .findFirst();
            if (task.isEmpty() || taskInSimulations.size() == 0) {
                log.info("User " + simulation.getUser().getFirstAndLastName() + " don't have active task");
                taskService.issueNewTask(simulation);
            }
        }
        log.info("Check active task job stopped ...");
    }
}
