package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.view.TaskView;
import com.korgutlova.diplom.service.api.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    //метод для получения всех задач проекта

    private final TaskService taskService;

    @GetMapping()
    public String getChat(@RequestParam(name = "id", required = false) String id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TaskView taskView = taskService.findTaskInSimulation(id);

        model.addAttribute("user", currentUser);
        model.addAttribute("task", taskView);

        return "task";
    }
}
