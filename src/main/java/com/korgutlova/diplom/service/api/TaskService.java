package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.SpentTimeTaskDto;
import com.korgutlova.diplom.model.dto.UserTaskDto;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.entity.view.TaskView;
import java.util.List;

public interface TaskService {
    TaskInSimulation findById(Long id);

    TaskView findTaskInSimulation(String id);

    List<TaskInSimulation> findTasksBySimulation(Simulation simulation);

    void issueNewTask(Simulation simulation);

    void initTasksForSimulation(Simulation simulation);

    void editTask(UserTaskDto dto);
}
