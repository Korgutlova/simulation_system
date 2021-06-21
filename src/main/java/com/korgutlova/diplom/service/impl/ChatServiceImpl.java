package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.AnswerUser;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.service.api.ChatService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.QuestionService;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.TaskService;
import java.util.Objects;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.korgutlova.diplom.util.TemplateBotMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final SimulationService simulationService;
    private final MessageService messageService;
    private final QuestionService questionService;
    private final TaskService taskService;

    private final Random random = new Random();

    @Override
    public void processingIncomingMessages(MessageDto dto, Bot bot) {

        Simulation simulation = simulationService.findActiveSimulation(dto.getUser());

        // сохранение и отправка сообщения от пользователя
        messageService.saveAndSend(dto.getText(), bot, simulation, DirectionMessage.USER_TO_BOT);


        QuestionToUserSimulation question = questionService.findLastQuestion(simulation, bot);

        //пользователь не ответил на вопрос
        if (question != null && question.getAnswer() == null) {

            processAnswerUser(question, simulation, bot, dto.getText());

        } else {
            // проверка вопроса по команде
            String answer = questionService.findQuestionByCommand(dto.getText());
            if (answer == null) {
                // проверка произвольного вопроса, с помощью запроса к Django сервису
                answer = questionService.findCustomQuestion(dto.getText(), simulation.getProject());
            }

            if (answer != null) {
                delayBetweenMessagesFromBot();
                messageService.saveAndSend(answer, bot, simulation, DirectionMessage.BOT_TO_USER);
            } else {
                messageService.saveAndSend(DEFAULT_MESSAGE_DURING_ERRORS, bot, simulation, DirectionMessage.BOT_TO_USER);
            }
        }
    }

    private void processAnswerUser(QuestionToUserSimulation question, Simulation simulation,
                                   Bot bot, String text) {
        AnswerUser answerUser = questionService.checkCorrectAnswerAndSave(question, text);
        Task task = answerUser.getQuestion().getQuestion().getForTask();

        if (answerUser.getIsRight()) {

            // отправление положительной реакции бота на ответ
            messageService.saveAndSend(REPLICAS_RIGHT_ANSWERS[random.nextInt(REPLICAS_RIGHT_ANSWERS.length)],
                    bot,
                    simulation,
                    DirectionMessage.BOT_TO_USER
            );

            delayBetweenMessagesFromBot();

            if (task != null) {
                TaskInSimulation currentTask = simulation.getTasks()
                        .stream()
                        .filter(t -> t.getTask().getId().equals(task.getId()))
                        .findFirst().orElse(null);

                // подсчитываются все корректные ответы по задаче
                int sum = questionService.sumCorrectAnswers(currentTask);
                if (sum >= task.getDifficult()) {
                    // выдается новая задача
                    taskService.assignTask(currentTask);

                    messageService.saveAndSend(
                            String.format(ISSUE_NEW_TASK, Objects.requireNonNull(currentTask).getId(), currentTask.getTask().getViewName()),
                            bot,
                            simulation,
                            DirectionMessage.BOT_TO_USER
                    );

                } else {
                    getNewQuestionAndSend(simulation, task);
                }
            }
        } else {
            // отправление отрицательной реакции бота на ответ
            messageService.saveAndSend(
                    String.format(REPLICAS_WRONG_ANSWERS[random.nextInt(REPLICAS_WRONG_ANSWERS.length)],
                            answerUser.getQuestion().getQuestion().getCorrectAnswer()),
                    bot,
                    simulation,
                    DirectionMessage.BOT_TO_USER
            );

            delayBetweenMessagesFromBot();

            getNewQuestionAndSend(simulation, task);
        }
    }

    private void getNewQuestionAndSend(Simulation simulation, Task task) {
        QuestionToUserSimulation questionToUserSimulation =
                questionService.findNewQuestionToUser(simulation, task);

        if (questionToUserSimulation != null) {
            // задается новый вопрос
            messageService.saveAndSend(questionToUserSimulation.getQuestion().getQuestion(),
                    questionToUserSimulation.getQuestion().getBot(),
                    simulation,
                    DirectionMessage.BOT_TO_USER
            );
        }
    }

    private void delayBetweenMessagesFromBot() {
        try {
            Thread.sleep((random.nextInt(10) + 1) * 1000);
        } catch (InterruptedException e) {
            log.warn("Server error occurred, during generate question from bot");
        }
    }
}
