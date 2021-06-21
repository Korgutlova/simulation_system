package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.job.WelcomeMessageTrigger;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.enums.simulation.SimStatus;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.GitHubService;
import com.korgutlova.diplom.service.api.ProjectService;
import com.korgutlova.diplom.service.api.SimulationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;
    private final GitHubService gitHubService;
    private final ProjectService projectService;
    private final BotService botService;
    private final WelcomeMessageTrigger welcomeMessageTrigger;

    @GetMapping("/wiki")
    public String getWikiPage(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Simulation simulation = simulationService.findActiveSimulation(currentUser);

        List<Bot> botList = botService.findBots(simulation.getProject());
        model.addAttribute("bots", botList);
        model.addAttribute("user", currentUser);
        model.addAttribute("simulation", simulation);
        return "wiki";
    }

    //первый раз заходим в симуляцию
    @PostMapping("/init")
    public String initSimulation(@RequestParam Long id, @RequestParam String login) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //create Sim and init tasks
        Simulation simulation = simulationService.createSimulation(projectService.findById(id), currentUser);

        //init repo
        gitHubService.initRepository(simulation, login);

        //change active simulation
        simulation.setStatus(SimStatus.IN_PROCESS);
        simulationService.save(simulation);

        Thread thread = new Thread(() -> {
            welcomeMessageTrigger.sendMessages(simulation);
        });
        thread.start();

        return "redirect:wiki";
    }

    //повторно заходим в симуляцию
    @PostMapping("/active")
    public String activeSimulation(@RequestParam Long id) {
        Simulation simulation = simulationService.findSimulation(id);
        if (simulation == null) {
            return "redirect:/project/all";
        }
        simulation.setStatus(SimStatus.IN_PROCESS);
        simulationService.save(simulation);
        return "redirect:/chat";
    }
}
