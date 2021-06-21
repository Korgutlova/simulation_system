package com.korgutlova.diplom.job;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.QuestionService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.korgutlova.diplom.model.enums.simulation.CommunicationType.WITH_COMM_AND_REMINDERS;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final QuestionService questionService;
    private final SimulationService simulationService;
    private final MessageService messageService;
    private final BotService botService;

    //каждые 20 минут с 9 до 18 часов
    @Scheduled(cron = "0 */20 9-18 * * *")
//    @Scheduled(cron = "*/30 * * * * *")
    private void remind() {
        log.info("Remind job started ...");
        List<Simulation> simulations = simulationService.findActiveSimulations();
        for (Simulation simulation : simulations) {
            if (simulation.getProject().getCommunicationType() == WITH_COMM_AND_REMINDERS) {
                for (Bot bot : botService.findBots(simulation.getProject())) {

                    QuestionToUserSimulation question = questionService.findLastQuestion(simulation, bot);

                    LocalDateTime timeAnswer = question.getDateAsked().plus(1, ChronoUnit.HOURS);

                    //ответ не найден, и время прошло, тогда делаем отправку
                    if (question.getAnswer() == null && timeAnswer.isBefore(LocalDateTime.now())) {
                        messageService.saveAndSend(
                                "Ответь пожалуйста на вопрос",
                                bot,
                                simulation,
                                DirectionMessage.BOT_TO_USER
                        );
                    }
                }
            }
        }
        log.info("Remind job stopped ...");
    }
}
