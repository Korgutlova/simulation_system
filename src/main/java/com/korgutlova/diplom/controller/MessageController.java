package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.model.mapper.MessageMapper;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpleMessagingTemplate;
    private final BotService botService;
    private final MessageService messageService;
    private final SimulationService simulationService;
    private final MessageMapper messageMapper;

    @MessageMapping("/bot/send/{id}")
    public void sendMessage(@DestinationVariable("id") String id, MessageDto message) {
        Bot bot = botService.findById(Long.valueOf(id));
        if (bot == null) {
            // do nothing
        }

        //перенести это в сервис
        Message newMessage = new Message();
        newMessage.setText(message.getText());
        newMessage.setDirectionMessage(DirectionMessage.USER_TO_BOT);
        newMessage.setMessageCreated(LocalDateTime.now());
        newMessage.setBot(bot);
        newMessage.setSimulation(simulationService.findActiveSimulation(message.getUser()));
        messageService.save(newMessage);

        simpleMessagingTemplate.convertAndSend(
                "/queue/bot/" + id + "/user/" + message.getUser().getId(),
               messageMapper.toView(newMessage));
    }
}
