package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import java.util.List;

public interface SimulationService {
    Simulation findActiveSimulation(User user);

    Simulation findInitSimulation(User user);

    List<Simulation> findActiveSimulations();

    void save(Simulation simulation);

    List<Simulation> findSimulations(User currentUser);

    Simulation findSimulation(Long id);

    Simulation createSimulation(Project project, User currentUser);
}
