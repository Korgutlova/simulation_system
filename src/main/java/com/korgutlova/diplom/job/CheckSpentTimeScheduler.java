package com.korgutlova.diplom.job;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.SpentTimeTask;
import com.korgutlova.diplom.repository.SpentTimeTaskRepository;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.korgutlova.diplom.util.TemplateBotMessages.TIME_WRITE_REMINDER;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckSpentTimeScheduler {

    private final SimulationService simulationService;
    private final MessageService messageService;

    private final SpentTimeTaskRepository spentTimeTaskRepository;

    //один раз в конце недели, в вс в 6 вечера
    @Scheduled(cron = "0 0 18 * * SAT")
//    @Scheduled(cron = "*/30 * * * * MON")
    private void checkActiveTasks() {
        log.info("Check spent time job started ...");
        List<Simulation> simulations = simulationService.findActiveSimulations();
        for (Simulation simulation : simulations) {
            Integer workHours = simulation.getProject().getWorkHoursPerWeek();
            if (workHours != null) {
                List<SpentTimeTask> spentTimeTasks = spentTimeTaskRepository
                        .findAllByTask_SimulationAndStartDateIsBetween(
                                simulation,
                                LocalDateTime.now().minus(7, ChronoUnit.DAYS),
                                LocalDateTime.now()
                        );
                int sumHours = spentTimeTasks
                        .stream()
                        .mapToInt(SpentTimeTask::getHours)
                        .sum();
                if (sumHours < workHours) {
                    messageService.saveAndSend(
                            String.format(TIME_WRITE_REMINDER, sumHours, workHours),
                            null,
                            simulation
                    );
                    log.info("Send message from PM to user " + simulation.getUser().getId());
                }
            }
        }
        log.info("Check spent time job stopped ...");
    }
}
