package com.korgutlova.diplom.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korgutlova.diplom.exception.QuestionNotFound;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import com.korgutlova.diplom.model.entity.question.QuestionToUser;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.mapper.QuestionCommandMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionCommandRepository questionCommandRepository;
    private final QuestionToUserRepository questionToUserRepository;
    private final QuestionToUserSimRepository questionToUserSimRepository;
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
        throw new QuestionNotFound("Вопрос по команде " + command + " не найден");
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
    public String findCustomQuestion(String question) throws JsonProcessingException {
        // сначала проверить на ответный вопрос (опр боту), проверить на команду
        // затем сюда
        // TODO получить текущий проект

        String projectId = "qa_java_project";

        ResponseEntity<String> response = restTemplate.getForEntity(
                String.format("%s?project_id=%s&question=%s", answerUrl, projectId, question),
                String.class);
        if (response == null) {
            throw new QuestionNotFound("Во время поиска возникла непредвиденная ошибка");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        JsonNode answer = root.path("answer");
        return answer.asText();
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
        if (newQuestions.size() == 0){
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
}
