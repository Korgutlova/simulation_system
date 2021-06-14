package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInSimulationRepository extends CrudRepository<TaskInSimulation, Long> {
    List<TaskInSimulation> findBySimulation(Simulation simulation);
}
