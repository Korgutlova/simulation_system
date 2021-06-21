package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.dto.SpentTimeTaskDto;
import com.korgutlova.diplom.model.dto.UserTaskDto;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.view.TaskView;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.SpentTimeTaskService;
import com.korgutlova.diplom.service.api.TaskService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        model.addAttribute("form", new UserTaskDto(taskView.getId(), taskView.getStatus()));
        model.addAttribute("user", currentUser);
        model.addAttribute("task", taskView);

        return "task";
    }

    @GetMapping("/all")
    public String getTasks(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TaskView> tasksView = taskService.findViewedTasksFromSimulation(
                simulationService.findActiveSimulation(currentUser)
        );

        model.addAttribute("user", currentUser);
        model.addAttribute("tasks", tasksView);

        return "tasks";
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
    public String changeStatusTask(@ModelAttribute("form") @Valid UserTaskDto userTaskDto) {
        taskService.editTask(userTaskDto);
        return "redirect:/api/task?id=" + userTaskDto.getId();
    }

    @PostMapping("/spent_time")
    public String addSpentTimeTask(@ModelAttribute("form") @Valid SpentTimeTaskDto spentTimeTaskDto) {
        spentTimeTaskService.addSpentTime(spentTimeTaskDto);
        return "redirect:/api/task?id=" + spentTimeTaskDto.getTaskId();
    }
}
