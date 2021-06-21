package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.SpentTimeTaskDto;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.SpentTimeTask;
import java.util.List;

public interface SpentTimeTaskService {

    void addSpentTime(SpentTimeTaskDto spentTimeTaskDto);

    List<SpentTimeTask> findSpentTimeTasksForWeek(Simulation simulation);
}
