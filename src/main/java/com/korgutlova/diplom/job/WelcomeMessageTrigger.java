package com.korgutlova.diplom.job;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionToUser;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.QuestionService;
import com.korgutlova.diplom.service.api.TaskService;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WelcomeMessageTrigger {

    private final QuestionService questionService;
    private final MessageService messageService;
    private final TaskService taskService;
    private final Random random = new Random();

    public void sendMessages(Simulation simulation) {
        log.info("Send welcome messages started ...");
        List<QuestionToUser> questions = questionService.findAllWelcomeMessage(simulation);
        for (QuestionToUser question : questions) {
//            delayBetweenMessagesFromBot(100);
            delayBetweenMessagesFromBot(20);
            messageService.saveAndSend(question.getQuestion(),
                    question.getBot(),
                    simulation,
                    DirectionMessage.BOT_TO_USER
            );
        }
        log.info("Send welcome messages ended ...");

        taskService.issueNewTask(simulation);

    }

    private void delayBetweenMessagesFromBot(int i) {
        try {
            Thread.sleep((random.nextInt(i) + 1) * 1000);
        } catch (InterruptedException e) {
            log.warn("Server error occurred, during generate question from bot");
        }
    }
}
