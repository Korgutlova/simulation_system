package com.korgutlova.diplom.model.entity.tasktracker;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "task_in_simulation")
public class TaskInSimulation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "simulation_id", nullable = false)
    private Simulation simulation;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TO_DO;

    private Boolean isViewed = false;

    //если статус задачи в QA - назначать на тестировщика
    //если на review то на тимлида
    //когда задача в done/cancelled/closed - на PM

}
