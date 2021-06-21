package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.enums.simulation.SimStatus;
import com.korgutlova.diplom.repository.SimulationRepository;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.TaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {
    private final SimulationRepository simulationRepository;
    private final TaskService taskService;

    @Override
    public Simulation findActiveSimulation(User user) {
        return simulationRepository.findByStatusAndUser(SimStatus.IN_PROCESS, user).orElse(null);
    }

    @Override
    public Simulation findInitSimulation(User user) {
        return simulationRepository.findByStatusAndUser(SimStatus.INITIALIZE, user).orElse(null);
    }

    @Override
    public List<Simulation> findActiveSimulations() {
        return simulationRepository.findByStatus(SimStatus.IN_PROCESS);
    }

    @Override
    public void save(Simulation simulation) {
        simulationRepository.save(simulation);
    }

    @Override
    public List<Simulation> findSimulations(User currentUser) {
        List<Simulation> simulations = simulationRepository.findByUser(currentUser);
        simulations.forEach(sim -> {
            if (sim.getStatus() == SimStatus.IN_PROCESS) {
                sim.setStatus(SimStatus.ON_PAUSE);
                simulationRepository.save(sim);
            }
        });
        return simulations;
    }

    @Override
    public Simulation findSimulation(Long id) {
        return simulationRepository.findById(id).orElse(null);
    }

    @Override
    public Simulation createSimulation(Project project, User currentUser) {
        Simulation simulation = new Simulation();
        simulation.setProject(project);
        simulation.setUser(currentUser);
        simulationRepository.save(simulation);
        simulation.setTasks(taskService.initTasksForSimulation(simulation));
        simulationRepository.save(simulation);
        return simulation;
    }
}
