package com.korgutlova.diplom.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.enums.simulation.StatusAction;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "check_repository")
public class CheckRepository{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "task_id")
    @JsonIgnore
    private TaskInSimulation taskInSimulation;

    /**
     * Id of commit = sha
     */
    private String commitSha;

    private String errors;

    private String nameBranch;

    private String nameCommit;

    private String namePR;

    private StatusAction statusAction;

    @CreationTimestamp
    private LocalDateTime checkCreated;
}
