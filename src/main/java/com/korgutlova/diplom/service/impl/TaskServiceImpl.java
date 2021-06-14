package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.entity.view.TaskView;
import com.korgutlova.diplom.model.mapper.TaskMapper;
import com.korgutlova.diplom.repository.TaskInSimulationRepository;
import com.korgutlova.diplom.repository.TaskRepository;
import com.korgutlova.diplom.service.api.TaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskInSimulationRepository taskInSimulationRepository;
    private final TaskMapper taskMapper;


    @Override
    public TaskView findTaskInSimulation(String id) {
        return taskMapper
                .toView(taskInSimulationRepository
                        .findById(Long.valueOf(id))
                        .orElse(null)
                );
    }

    @Override
    public List<TaskInSimulation> findTasksBySimulation(Simulation simulation) {
        return taskInSimulationRepository.findBySimulation(simulation);
    }

    @Override
    public void issueNewTask(Simulation simulation) {

    }
}
