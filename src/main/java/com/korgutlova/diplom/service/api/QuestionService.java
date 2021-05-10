package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.QuestionCommandDto;

public interface QuestionService {
    String findQuestionByCommand(String command);

    void create(QuestionCommandDto questionCommandDto);
}
