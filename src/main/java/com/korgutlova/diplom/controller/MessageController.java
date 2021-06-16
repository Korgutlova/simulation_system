package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final BotService botService;
    private final ChatService chatService;

    @MessageMapping("/bot/send/{id}")
    public void sendMessage(@DestinationVariable("id") String id, MessageDto message) {
        Bot bot = botService.findById(Long.valueOf(id));
        if (bot == null) {
            // do nothing
            return;
        }

        chatService.processingIncomingMessages(message, bot);

    }
}
