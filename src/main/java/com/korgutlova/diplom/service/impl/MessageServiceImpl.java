package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.view.MessageView;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.model.enums.roles.TeamRole;
import com.korgutlova.diplom.model.mapper.MessageMapper;
import com.korgutlova.diplom.repository.BotRepository;
import com.korgutlova.diplom.repository.MessageRepository;
import com.korgutlova.diplom.service.api.MessageService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.korgutlova.diplom.util.TemplateBotMessages.CHAT_DESTINATION;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final BotRepository botRepository;

    private final MessageMapper messageMapper;

    private final SimpMessagingTemplate simpleMessagingTemplate;

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

    private MessageView createMessage(String message, Bot bot, Simulation simulation, DirectionMessage directionMessage) {
        Message newMessage = new Message();
        newMessage.setText(message);
        newMessage.setDirectionMessage(directionMessage);
        newMessage.setBot(bot);
        newMessage.setSimulation(simulation);
        save(newMessage);
        return messageMapper.toView(newMessage);
    }

    @Override
    public void saveAndSend(String message, Bot bot, Simulation simulation, DirectionMessage directionMessage) {
        if (bot == null) {
            Optional<Bot> projectManager = botRepository.findAllByProject(simulation.getProject())
                    .stream()
                    .filter(b -> b.getTeamRole() == TeamRole.PROJECT_MANAGER)
                    .findAny();
            if (projectManager.isPresent()) {
                bot = projectManager.get();
            } else {
                return;
            }
        }
        MessageView messageView = createMessage(message, bot, simulation, directionMessage);
        simpleMessagingTemplate.convertAndSend(
                String.format(CHAT_DESTINATION, bot.getId(), simulation.getUser().getId()),
                messageView
        );
    }
}
