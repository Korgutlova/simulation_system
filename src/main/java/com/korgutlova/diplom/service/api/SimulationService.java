package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;

public interface SimulationService {
    Simulation findActiveSimulation(User user);
}
