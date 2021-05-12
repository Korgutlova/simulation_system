package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

//    private final ConversationService conversationService;
//
//    public ChatController(ConversationService conversationService) {
//        this.conversationService = conversationService;
//    }

    @GetMapping("/chat")
    public String getCurrentChat(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("chat", conversationService.getConversation(Long.valueOf(id)));
        model.addAttribute("user", currentUser);
        return "chat_page";
    }
}