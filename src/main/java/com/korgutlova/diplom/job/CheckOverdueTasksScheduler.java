package com.korgutlova.diplom.job;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.entity.view.MessageView;
import com.korgutlova.diplom.model.enums.roles.TeamRole;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import com.korgutlova.diplom.repository.BotRepository;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.korgutlova.diplom.util.TemplateBotMessages.CHAT_DESTINATION;
import static com.korgutlova.diplom.util.TemplateBotMessages.OVERDUE_TASK_REMINDER;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckOverdueTasksScheduler {

    private final SimulationService simulationService;
    private final TaskService taskService;
    private final MessageService messageService;
    private final SimpMessagingTemplate simpleMessagingTemplate;

    private final BotRepository botRepository;

    //проверка каждые 2 часа
    @Scheduled(cron = "0 0 */2 * * *")
//    @Scheduled(cron = "*/30 * * * * *")
    private void checkActiveTasks() {
        log.info("Check overdue tasks job started ...");
        List<Simulation> simulations = simulationService.findActiveSimulations();
        for (Simulation simulation : simulations) {
            if (simulation.getProject().isCheckOverdueTasks()) {
                List<TaskInSimulation> taskInSimulations = taskService.findTasksBySimulation(simulation);
                for (TaskInSimulation task : taskInSimulations) {
                    if ((task.getStatus() == TaskStatus.TO_DO || task.getStatus() == TaskStatus.IN_PROGRESS) &&
                            task.getDueDate().isBefore(LocalDateTime.now())) {
                        Optional<Bot> projectManager = botRepository.findAllByProject(simulation.getProject())
                                .stream()
                                .filter(b -> b.getTeamRole() == TeamRole.PROJECT_MANAGER)
                                .findAny();
                        if (projectManager.isEmpty()) {
                            log.error("In simulation " + simulation.getProject().getName() + " not initialized PM");
                        } else {
                            MessageView messageView = messageService.createAnswerMessage(
                                    String.format(OVERDUE_TASK_REMINDER, task.getId(), task.getTask().getViewName()),
                                    projectManager.get(),
                                    simulation
                            );
                            simpleMessagingTemplate.convertAndSend(
                                    String.format(CHAT_DESTINATION, projectManager.get().getId(), simulation.getUser().getId()),
                                    messageView
                            );
                            log.info("Send message about overdue tasks from PM to user " + simulation.getUser().getId());
                        }
                    }
                }
            }
            log.info("Check overdue tasks job stopped ...");
        }
    }
}
