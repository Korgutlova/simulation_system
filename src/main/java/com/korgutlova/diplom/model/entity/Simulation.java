package com.korgutlova.diplom.model.entity;

import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import com.korgutlova.diplom.model.enums.simulation.SimStatus;

@Data
@Entity
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private SimStatus status = SimStatus.INITIALIZE;

    private String nameRepo;

    private String fullNameRepo;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.EAGER)
    private List<TaskInSimulation> tasks;
}
