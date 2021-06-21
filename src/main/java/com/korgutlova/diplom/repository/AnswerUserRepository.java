package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.AnswerUser;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerUserRepository extends CrudRepository<AnswerUser, Long> {

    List<AnswerUser> findAllByQuestion_Question_ForTaskAndQuestion_Simulation(Task task, Simulation simulation);

}
