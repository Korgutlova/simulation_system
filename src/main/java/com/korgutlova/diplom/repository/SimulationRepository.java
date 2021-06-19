package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.enums.simulation.SimStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRepository extends CrudRepository<Simulation, Long> {
    Optional<Simulation> findByStatusAndUser(SimStatus status, User user);

    List<Simulation> findByStatus(SimStatus status);

    List<Simulation> findByUser(User currentUser);
}
