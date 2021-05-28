package com.korgutlova.diplom.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.korgutlova.diplom.exception.QuestionNotFound;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.question.QuestionCommand;
import com.korgutlova.diplom.model.mapper.QuestionCommandMapper;
import com.korgutlova.diplom.repository.QuestionCommandRepository;
import com.korgutlova.diplom.service.api.QuestionService;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionCommandRepository questionCommandRepository;
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
}
