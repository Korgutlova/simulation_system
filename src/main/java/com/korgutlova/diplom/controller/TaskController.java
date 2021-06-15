package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.SpentTimeTaskDto;
import com.korgutlova.diplom.model.dto.UserTaskDto;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.view.TaskView;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.SpentTimeTaskService;
import com.korgutlova.diplom.service.api.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    //метод для получения всех задач проекта

    private final TaskService taskService;
    private final SpentTimeTaskService spentTimeTaskService;
    private final SimulationService simulationService;

    @GetMapping
    public String getTask(@RequestParam(name = "id", required = false) String id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TaskView taskView = taskService.findTaskInSimulation(id);

        model.addAttribute("user", currentUser);
        model.addAttribute("task", taskView);

        return "task";
    }

    @PostMapping("/init")
    public String initTasksSimBeforeStart() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskService.initTasksForSimulation(simulationService.findActiveSimulation(currentUser));
        return "redirect:/home";
    }

    @PostMapping
    public String issueTask() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        taskService.issueNewTask(simulationService.findActiveSimulation(currentUser));
        return "redirect:/chat";
    }

    @PostMapping("/edit")
    public String changeStatusTask(@RequestBody UserTaskDto userTaskDto) {
        taskService.editTask(userTaskDto);
        return "redirect:/api/task?id=" + userTaskDto.getId();
    }

    @PostMapping("/spent_time")
    public String addSpentTimeTask(@RequestBody SpentTimeTaskDto spentTimeTaskDto) {
        spentTimeTaskService.addSpentTime(spentTimeTaskDto);
        return "redirect:/api/task?id=" + spentTimeTaskDto.getTaskId();
    }
}
