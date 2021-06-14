package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.enums.simulation.SimStatus;
import com.korgutlova.diplom.repository.SimulationRepository;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {
    private final SimulationRepository simulationRepository;

    @Override
    public Simulation findActiveSimulation(User user) {
        return simulationRepository.findByStatusAndUser(SimStatus.IN_PROCESS, user).orElse(null);
    }

    @Override
    public List<Simulation> findActiveSimulations() {
        return simulationRepository.findByStatus(SimStatus.IN_PROCESS);
    }
}
