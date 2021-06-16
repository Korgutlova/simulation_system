package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionToUserSimRepository extends CrudRepository<QuestionToUserSimulation, Long> {

    List<QuestionToUserSimulation> findAllByQuestion_ForTask(Task task);

    List<QuestionToUserSimulation> findAllBySimulationAndQuestion_BotOrderByDateAskedDesc(Simulation simulation, Bot bot);
}
