package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Message;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.Set;
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

    @GetMapping("/chat")
    public String getChat(@RequestParam(name = "id", required = false) String id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Bot bot;
        Simulation currentSim = simulationService.findActiveSimulation(currentUser);
        if (id == null || id.isEmpty()) {
            Message lastMessage = messageService.findLastMessage(currentSim);
            if (lastMessage == null){
                //у пользователя нет сообщений, выводим пусто..
                model.addAttribute("bots", null);
                return "chat_page";
            }
            bot = lastMessage.getBot();
        } else  {
            bot = botService.findById(Long.valueOf(id));

            //обработка экспшена если такго бота нет, или он есть но из другой симуляции
        }

        Set<Bot> activeBots = messageService.findActiveBots(currentSim);
        activeBots.add(bot);
        model.addAttribute("bots", activeBots);

        model.addAttribute("messages", messageService.findMessages(currentSim, bot));
        model.addAttribute("bot", bot);
        model.addAttribute("user", currentUser);

        return "chat_page";
    }
}