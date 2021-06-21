package com.korgutlova.diplom.model.entity.tasktracker;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "task_in_simulation")
@NoArgsConstructor
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

    private LocalDateTime dueDate;

    private LocalDateTime closedDate;

    public TaskInSimulation(Simulation simulation, Task task) {
        this.simulation = simulation;
        this.task = task;
    }
}
