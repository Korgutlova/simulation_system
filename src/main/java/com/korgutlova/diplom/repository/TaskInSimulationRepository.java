package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInSimulationRepository extends CrudRepository<TaskInSimulation, Long> {
}
