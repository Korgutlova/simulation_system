package com.korgutlova.diplom.job;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.QuestionService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.korgutlova.diplom.model.enums.simulation.CommunicationType.WITH_COMM;
import static com.korgutlova.diplom.model.enums.simulation.CommunicationType.WITH_COMM_AND_REMINDERS;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendingQuestionScheduler {

    private final QuestionService questionService;
    private final SimulationService simulationService;
    private final MessageService messageService;
    private final BotService botService;

    private final Random random = new Random();

    //каждый час с 9 до 18 часов
    @Scheduled(cron = "0 0 9-18 * * *")
//    @Scheduled(cron = "*/30 * * * * *")
    private void sendQuestions() {
        log.info("Send questions job started ...");
        List<Simulation> simulations = simulationService.findActiveSimulations();
        for (Simulation simulation : simulations) {
            if (Arrays.asList(WITH_COMM, WITH_COMM_AND_REMINDERS)
                    .contains(simulation.getProject().getCommunicationType())) {
                if (random.nextInt(3) == 0) {
                    boolean oldQuestion = false;
                    for (Bot bot : botService.findBots(simulation.getProject())) {
                        QuestionToUserSimulation tempQuestion = questionService.findLastQuestion(simulation, bot);
                        if (tempQuestion != null && tempQuestion.getAnswer() == null) {
                            oldQuestion = true;
                            break;
                        }
                    }

                    if (!oldQuestion) {
                        QuestionToUserSimulation question = questionService.findNewQuestionToUser(simulation);
                        if (question != null) {
                            messageService.saveAndSend(
                                    question.getQuestion().getQuestion(),
                                    question.getQuestion().getBot(),
                                    simulation,
                                    DirectionMessage.BOT_TO_USER
                            );
                        }
                    }

                }
            }
        }
        log.info("Send questions job started ...");
    }
}
