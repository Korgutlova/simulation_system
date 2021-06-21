package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.CheckRepository;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckRepoRepository extends CrudRepository<CheckRepository, Long> {
    Optional<CheckRepository> findByTaskInSimulation(TaskInSimulation taskInSimulation);
}
