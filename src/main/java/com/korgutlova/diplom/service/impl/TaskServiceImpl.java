package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.UserTaskDto;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.question.QuestionToUserSimulation;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.entity.view.TaskView;
import com.korgutlova.diplom.model.enums.DirectionMessage;
import com.korgutlova.diplom.model.mapper.TaskMapper;
import com.korgutlova.diplom.repository.TaskInSimulationRepository;
import com.korgutlova.diplom.repository.TaskRepository;
import com.korgutlova.diplom.service.api.MessageService;
import com.korgutlova.diplom.service.api.QuestionService;
import com.korgutlova.diplom.service.api.TaskService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.korgutlova.diplom.util.TemplateBotMessages.ISSUE_NEW_TASK;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskInSimulationRepository taskInSimulationRepository;

    private final QuestionService questionService;
    private final MessageService messageService;

    private final TaskMapper taskMapper;


    @Override
    public TaskInSimulation findById(Long id) {
        return taskInSimulationRepository.findById(id).orElse(null);
    }

    @Override
    public TaskView findTaskInSimulation(String id) {
        return taskMapper
                .toView(taskInSimulationRepository
                        .findById(Long.valueOf(id))
                        .orElse(null)
                );
    }

    @Override
    public List<TaskInSimulation> findTasksBySimulation(Simulation simulation) {
        return taskInSimulationRepository.findBySimulationAndIsViewed(simulation, true);
    }

    @Override
    public void issueNewTask(Simulation simulation) {
        switch (simulation.getProject().getTaskDistributionType()) {
            case ORDER:
                getNewOrderTaskAndSend(simulation);
                break;
            case STANDART:
                getNewRandomTaskAndSend(simulation);
                break;
            case VARIABLE:
                chooseTaskAndSendQuestion(simulation);
                break;
        }
    }

    private void chooseTaskAndSendQuestion(Simulation simulation) {
        List<TaskInSimulation> tasks = simulation.getTasks()
                .stream().filter(t -> !t.getIsViewed()).collect(Collectors.toList());

        Optional<TaskInSimulation> taskInSimulation = tasks.size() == 0 ? Optional.empty() :
                Optional.of(tasks.get(new Random().nextInt(tasks.size())));

        if (taskInSimulation.isPresent()) {
            QuestionToUserSimulation questionToUserSimulation =
                    questionService.findNewQuestionToUser(simulation, taskInSimulation.get().getTask());
            if (questionToUserSimulation != null) {
                messageService.saveAndSend(questionToUserSimulation.getQuestion().getQuestion(),
                        questionToUserSimulation.getQuestion().getBot(),
                        simulation,
                        DirectionMessage.BOT_TO_USER
                );
            }
        } else {
            log.info("Tasks for user " + simulation.getUser().getId() + " ended");
        }
    }

    private void getNewRandomTaskAndSend(Simulation simulation) {
        List<TaskInSimulation> tasks = simulation.getTasks()
                .stream().filter(t -> !t.getIsViewed()).collect(Collectors.toList());

        Optional<TaskInSimulation> taskInSimulation = tasks.size() == 0 ? Optional.empty() :
                Optional.of(tasks.get(new Random().nextInt(tasks.size())));

        sendMessage(taskInSimulation, simulation);
    }

    private void getNewOrderTaskAndSend(Simulation simulation) {
        Optional<TaskInSimulation> taskInSimulation = simulation.getTasks().stream()
                .filter(t -> !t.getIsViewed()).min(Comparator.comparing(t -> t.getTask().getOrder()));

        sendMessage(taskInSimulation, simulation);
    }

    private void sendMessage(Optional<TaskInSimulation> taskInSimulation, Simulation simulation) {
        if (taskInSimulation.isPresent()) {
            TaskInSimulation task = taskInSimulation.get();
            assignTask(task);
            messageService.saveAndSend(String.format(ISSUE_NEW_TASK, task.getId(), task.getTask().getViewName()),
                    null,
                    simulation,
                    DirectionMessage.BOT_TO_USER
            );
        } else {
            log.info("Tasks for user " + simulation.getUser().getId() + " ended");
        }
    }

    @Override
    public void assignTask(TaskInSimulation taskInSimulation) {
        taskInSimulation.setIsViewed(true);
        Integer duration = taskInSimulation.getTask().getDuration();
        if (duration != null) {
            int days = duration / 8;
            taskInSimulation.setDueDate(LocalDateTime.now().plus(days, ChronoUnit.DAYS));
        }
        taskInSimulationRepository.save(taskInSimulation);
    }

    //иницируется при заходе пользователя в симуляцию
    @Override
    public void initTasksForSimulation(Simulation simulation) {
        List<Task> tasks = taskRepository.findAllByProject(simulation.getProject());
        List<TaskInSimulation> taskInSimulations = tasks.stream()
                .map(t -> new TaskInSimulation(simulation, t))
                .collect(Collectors.toList());
        taskInSimulationRepository.saveAll(taskInSimulations);
    }

    @Override
    public void editTask(UserTaskDto dto) {
        TaskInSimulation task = findById(dto.getId());
        task.setStatus(dto.getStatus());
        taskInSimulationRepository.save(task);
    }
}
