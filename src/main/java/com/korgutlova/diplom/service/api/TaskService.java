package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.entity.view.TaskView;

public interface TaskService {
    TaskView findTaskInSimulation(String id);
}
