package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.question.QuestionToUser;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionToUserRepository extends CrudRepository<QuestionToUser, Long> {
    List<QuestionToUser> findAllByForTask(Task task);
}
