package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.BotDto;
import com.korgutlova.diplom.model.dto.BotsForm;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.mapper.BotMapper;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.ProjectService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bots")
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;
    private final ProjectService projectService;
    private final BotMapper botMapper;

    private final int countBots = 4;

    @GetMapping("/create")
    public String createBotsPage(@RequestParam("project_id") Long projectId, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = projectService.findById(projectId);
        if (!project.getCreator().equals(currentUser)) {
            return "404_not_found";
        }

        BotsForm botsForm = new BotsForm();
        botsForm.setProjectId(projectId);

        List<Bot> bots = botService.findBots(project);
        List<BotDto> list;
        if (bots.size() == 0) {
            list = new ArrayList<>();
            for (int i = 0; i < countBots; i++) {
                list.add(new BotDto());
            }
        } else {
            list = botMapper.toDto(bots);
            if (bots.size() < countBots) {
                for (int i = 0; i < countBots - bots.size(); i++) {
                    list.add(new BotDto());
                }
            }
        }

        botsForm.setBots(list);
        model.addAttribute("botsForm", botsForm);
        model.addAttribute("user", currentUser);
        return "bots";
    }

    @PostMapping("/create")
    public String createBots(@ModelAttribute("botsForm") @Valid BotsForm botsForm,
                             BindingResult bindingResult, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!bindingResult.hasErrors()) {
            botService.addBots(botsForm.getBots(), projectService.findById(botsForm.getProjectId()));
            return "redirect:/home";
        }
        model.addAttribute("user", currentUser);
        return "bots";
    }
}
