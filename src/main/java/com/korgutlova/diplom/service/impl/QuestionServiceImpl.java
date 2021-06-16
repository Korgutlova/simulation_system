package com.korgutlova.diplom.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korgutlova.diplom.exception.QuestionNotFound;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.AnswerUser;
import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import com.korgutlova.diplom.model.entity.question.QuestionToUser;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.mapper.QuestionCommandMapper;
import com.korgutlova.diplom.repository.AnswerUserRepository;
import com.korgutlova.diplom.repository.QuestionCommandRepository;
import com.korgutlova.diplom.repository.QuestionToUserRepository;
import com.korgutlova.diplom.repository.QuestionToUserSimRepository;
import com.korgutlova.diplom.service.api.QuestionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionCommandRepository questionCommandRepository;
    private final QuestionToUserRepository questionToUserRepository;
    private final QuestionToUserSimRepository questionToUserSimRepository;
    private final AnswerUserRepository answerUserRepository;

    private final QuestionCommandMapper questionCommandMapper;

    private final RestTemplate restTemplate;

    @Value("${qa-system.url.get-answer}")
    private String answerUrl;

    @Value("${qa-system.url.create-model}")
    private String createModelUrl;

    @Override
    public String findQuestionByCommand(String command) {
        Optional<QuestionCommand> question = questionCommandRepository.findByCommand(command);
        if (question.isPresent()) {
            return question.get().getAnswer();
        }
        log.info("Вопрос по команде " + command + " не найден");
        return null;
    }

    @Override
    public void create(QuestionCommandDto questionCommandDto) {
        questionCommandRepository.save(questionCommandMapper.toEntity(questionCommandDto));
    }

    @Override
    public String findQuestion(String question) {
        Optional<QuestionCommand> resultQuestion = questionCommandRepository.findByQuestion(question);
        if (resultQuestion.isPresent()) {
            return resultQuestion.get().getAnswer();
        }
        throw new QuestionNotFound("Вопрос " + question + " не найден");
    }

    @Override
    public String findCustomQuestion(String question, Project project) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    String.format("%s?project_id=%s&question=%s", answerUrl, project.getShortName(), question),
                    String.class);

            if (response == null) {
                log.warn("Во время поиска возникла непредвиденная ошибка");
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
            JsonNode answer = root.path("answer");
            return answer.asText();
        } catch (ResourceAccessException | JsonProcessingException e) {
            log.warn("Error during JsonProcessing");
            return null;
        }
    }

    @Override
    public QuestionToUserSimulation findNewQuestionToUser(Simulation simulation, Task task) {
        List<QuestionToUser> askedQuestions = questionToUserSimRepository
                .findAllByQuestion_ForTask(task)
                .stream()
                .map(QuestionToUserSimulation::getQuestion)
                .collect(Collectors.toList());
        List<QuestionToUser> newQuestions = questionToUserRepository.findAllByForTask(task);
        newQuestions.removeAll(askedQuestions);
        if (newQuestions.size() == 0) {
            //not found question
            return null;
        } else {
            QuestionToUser question = newQuestions.get(new Random().nextInt(newQuestions.size()));
            QuestionToUserSimulation questionSimulation = new QuestionToUserSimulation();
            questionSimulation.setDateAsked(LocalDateTime.now());
            questionSimulation.setSimulation(simulation);
            questionSimulation.setQuestion(question);
            questionToUserSimRepository.save(questionSimulation);
            return questionSimulation;
        }
    }

    @Override
    public QuestionToUserSimulation findLastQuestion(Simulation simulation, Bot bot) {
        List<QuestionToUserSimulation> list = questionToUserSimRepository
                .findAllBySimulationAndQuestion_BotOrderByDateAskedDesc(simulation, bot);
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public AnswerUser checkCorrectAnswerAndSave(QuestionToUserSimulation question, String text) {
        AnswerUser answerUser = new AnswerUser();
        answerUser.setQuestion(question);
        answerUser.setAnswer(text);
        answerUser.setIsRight(question.getQuestion().getListAnswers()
                .stream().anyMatch(answer -> answer.toLowerCase().equals(text.toLowerCase())));
        answerUserRepository.save(answerUser);
        return answerUser;
    }

    @Override
    public int sumCorrectAnswers(TaskInSimulation taskInSimulation) {
        List<AnswerUser> answerUsers = answerUserRepository.findAllByQuestion_Question_ForTaskAndQuestion_Simulation
                (taskInSimulation.getTask(), taskInSimulation.getSimulation());
        return (int) answerUsers.stream().filter(AnswerUser::getIsRight).count();
    }
}
