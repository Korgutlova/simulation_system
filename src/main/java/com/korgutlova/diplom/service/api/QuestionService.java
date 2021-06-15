package com.korgutlova.diplom.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;

public interface QuestionService {
    String findQuestionByCommand(String command);

    void create(QuestionCommandDto questionCommandDto);

    String findQuestion(String question);

    String findCustomQuestion(String question) throws JsonProcessingException;

    QuestionToUserSimulation findNewQuestionToUser(Simulation simulation, Task task);
}
