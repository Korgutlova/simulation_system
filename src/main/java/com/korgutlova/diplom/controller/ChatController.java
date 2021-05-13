package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.service.api.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final BotService botService;

    @GetMapping("/chat")
    public String getChat(@RequestParam(name = "id", required = false) String id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //default chat - открывается по последнему сообщению отправленному с нашей стороны или со стороны бота

        //возвращаем список активных ботов (с кем была хоть какая-нибудь связь)

        //возвращаем текущую переписку

//        model.addAttribute("chat", conversationService.getConversation(Long.valueOf(id)));
        Long defaultId;
        if (id == null || id.isEmpty()) {
            defaultId = 105L;
        } else  {
            defaultId = Long.valueOf(id);
        }
        Bot bot = botService.findById(defaultId);

        // если есть хоть одно сообщение с ботом выводи его в список
        model.addAttribute("bots", null);
        // сообщения от бота и от пользака
        model.addAttribute("messages", null);
        //текущий чат

        model.addAttribute("bot", bot);
        model.addAttribute("user", currentUser);
        return "chat_page";
    }
}