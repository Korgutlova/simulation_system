package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.repository.MessageRepository;
import com.korgutlova.diplom.service.api.MessageService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Message findLastMessage(Simulation simulation) {
        return messageRepository.findFirstBySimulationOrderByMessageCreatedDesc(simulation).orElse(null);
    }

    @Override
    public List<Message> findMessages(Simulation currentSim, Bot bot) {
        return messageRepository.findAllBySimulationAndBot(currentSim, bot);
    }

    @Override
    public Set<Bot> findActiveBots(Simulation currentSim) {
        return messageRepository.findAllBySimulation(currentSim)
                .stream()
                .map(Message::getBot)
                .collect(Collectors.toSet());
    }
}
