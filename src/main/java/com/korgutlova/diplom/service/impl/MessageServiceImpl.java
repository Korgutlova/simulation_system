package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.view.MessageView;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.model.mapper.MessageMapper;
import com.korgutlova.diplom.repository.MessageRepository;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final SimulationService simulationService;
    private final MessageMapper messageMapper;

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
        return messageRepository.findAllBySimulationAndBotOrderByMessageCreatedAsc(currentSim, bot);
    }

    @Override
    public Set<Bot> findActiveBots(Simulation currentSim) {
        return messageRepository.findAllBySimulation(currentSim)
                .stream()
                .map(Message::getBot)
                .collect(Collectors.toSet());
    }

    @Override
    public MessageView createMessage(MessageDto messageDto, Bot bot) {
        Message newMessage = new Message();
        newMessage.setText(messageDto.getText());
        newMessage.setDirectionMessage(DirectionMessage.USER_TO_BOT);
        newMessage.setBot(bot);
        newMessage.setSimulation(simulationService.findActiveSimulation(messageDto.getUser()));
        save(newMessage);
        return messageMapper.toView(newMessage);
    }

    @Override
    public MessageView createAnswerMessage(String answer, Bot bot, User user) {
        Message newMessage = new Message();
        newMessage.setText(answer);
        newMessage.setDirectionMessage(DirectionMessage.BOT_TO_USER);
        newMessage.setBot(bot);
        newMessage.setSimulation(simulationService.findActiveSimulation(user));
        save(newMessage);
        return messageMapper.toView(newMessage);
    }

    @Override
    public MessageView createAnswerMessage(String answer, Bot bot, Simulation simulation) {
        Message newMessage = new Message();
        newMessage.setText(answer);
        newMessage.setDirectionMessage(DirectionMessage.BOT_TO_USER);
        newMessage.setBot(bot);
        newMessage.setSimulation(simulationService.findActiveSimulation(simulation.getUser()));
        save(newMessage);
        return messageMapper.toView(newMessage);
    }
}
