package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.SpentTimeTask;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpentTimeTaskRepository extends CrudRepository<SpentTimeTask, Long> {
    List<SpentTimeTask> findAllByTask_SimulationAndStartDateIsBetween(Simulation simulation,
                                                                      LocalDateTime from,
                                                                      LocalDateTime to);
}
