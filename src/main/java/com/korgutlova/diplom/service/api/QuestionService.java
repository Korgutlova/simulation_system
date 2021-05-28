package com.korgutlova.diplom.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;

public interface QuestionService {
    String findQuestionByCommand(String command);

    void create(QuestionCommandDto questionCommandDto);

    String findQuestion(String question);

    String findCustomQuestion(String question) throws JsonProcessingException;
}
