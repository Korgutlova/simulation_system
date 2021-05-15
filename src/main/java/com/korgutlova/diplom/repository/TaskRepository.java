package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.tasktracker.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
}
