package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.exception.QuestionNotFound;
import com.korgutlova.diplom.model.dto.MessageDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.view.MessageView;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final SimpMessagingTemplate simpleMessagingTemplate;
    private final BotService botService;
    private final MessageService messageService;
    private final QuestionService questionService;

    @MessageMapping("/bot/send/{id}")
    public void sendMessage(@DestinationVariable("id") String id, MessageDto message) throws InterruptedException {
        Bot bot = botService.findById(Long.valueOf(id));
        if (bot == null) {
            // do nothing
            return;
        }

        MessageView newMessage = messageService.createMessage(message, bot);

        simpleMessagingTemplate.convertAndSend(
                "/queue/bot/" + id + "/user/" + message.getUser().getId(),
               newMessage);


        //обработку асинхронную нужно делать после только как мы сохранили и отправили
        String answer;
        try {
            answer = questionService.findQuestion(message.getText());
        } catch (QuestionNotFound ex) {
            //do nothing message not send
            log.warn(ex.getMessage());
            return;
        }

        Thread.sleep(2000);

        newMessage = messageService.createAnswerMessage(answer, bot, message.getUser());

        simpleMessagingTemplate.convertAndSend(
                "/queue/bot/" + id + "/user/" + message.getUser().getId(),
                newMessage);

    }
}
