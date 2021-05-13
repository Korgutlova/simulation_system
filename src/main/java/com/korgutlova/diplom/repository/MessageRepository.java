package com.korgutlova.diplom.repository;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    Optional<Message> findFirstBySimulationOrderByMessageCreatedDesc(Simulation simulation);

    List<Message> findAllBySimulationAndBot(Simulation simulation, Bot bot);

    List<Message> findAllBySimulation(Simulation simulation);
}
