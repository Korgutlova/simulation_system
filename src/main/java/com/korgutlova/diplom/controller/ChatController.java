package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.mapper.MessageMapper;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.List;
import java.util.Random;
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
    private final MessageService messageService;
    private final SimulationService simulationService;
    private final MessageMapper messageMapper;

    @GetMapping("/chat")
    public String getChat(@RequestParam(name = "id", required = false) String id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Simulation currentSim = simulationService.findActiveSimulation(currentUser);
        List<Bot> activeBots = botService.findBots(currentSim.getProject());
        Bot bot = activeBots.get(new Random().nextInt(activeBots.size()));

        if (id == null || id.isEmpty()) {
            Message lastMessage = messageService.findLastMessage(currentSim);
            if (lastMessage != null) {
                bot = lastMessage.getBot();
            }
        } else {
            bot = botService.findById(Long.valueOf(id));
        }

        model.addAttribute("bots", activeBots);
        model.addAttribute("messages", messageMapper.toListViews(messageService.findMessages(currentSim, bot)));
        model.addAttribute("bot", bot);
        model.addAttribute("user", currentUser);

        return "chat_page";
    }
}