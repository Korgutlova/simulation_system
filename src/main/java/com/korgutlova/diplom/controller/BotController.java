package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.BotDto;
import com.korgutlova.diplom.model.dto.BotsForm;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.ProjectService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bots")
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;
    private final ProjectService projectService;

    @RequestMapping("/create")
    public String createBotsPage(@RequestParam("project_id") Long projectId, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", currentUser);
        BotsForm botsForm = new BotsForm();
        botsForm.setProjectId(projectId);
        model.addAttribute("botsForm", botsForm);
        return "bots";
    }

    @RequestMapping(name = "/create", method = RequestMethod.POST)
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

    @RequestMapping(value="/create", params={"addBot"})
    public String addBot(final BotsForm botsForm, final BindingResult bindingResult, final User user) {
        botsForm.getBots().add(new BotDto());
        return "bots";
    }
}
