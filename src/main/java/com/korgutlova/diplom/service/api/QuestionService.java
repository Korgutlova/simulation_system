package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.QuestionCommandDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.AnswerUser;
import com.korgutlova.diplom.model.entity.question.QuestionToUser;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import java.util.List;

public interface QuestionService {
    String findQuestionByCommand(String command);

    void create(QuestionCommandDto questionCommandDto);

    String findQuestion(String question);

    String findCustomQuestion(String question, Project project);

    QuestionToUserSimulation findNewQuestionToUser(Simulation simulation, Task task);

    QuestionToUserSimulation findLastQuestion(Simulation simulation, Bot bot);

    AnswerUser checkCorrectAnswerAndSave(QuestionToUserSimulation question, String text);

    int sumCorrectAnswers(TaskInSimulation taskInSimulation);

    QuestionToUserSimulation findNewQuestionToUser(Simulation simulation);

    List<QuestionToUser> findAllWelcomeMessage(Simulation simulation);

}
