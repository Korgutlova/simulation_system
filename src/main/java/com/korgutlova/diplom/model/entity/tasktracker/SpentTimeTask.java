package com.korgutlova.diplom.model.entity.tasktracker;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "spent_time_task")
public class SpentTimeTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_in_simulation_id", nullable = false)
    private TaskInSimulation task;

    private String description;

    private LocalDate startDate;

    //продолжительность работы
    private int hours;


    //по этой сущности необходимо делать проверку что списано определенное количество часов за неделю
    //если нет PM пишет о том что необходимо списать часы на соответсвующие задачи
}
