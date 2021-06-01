package com.korgutlova.diplom.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.korgutlova.diplom.model.enums.simulation.CommunicationType;
import com.korgutlova.diplom.model.enums.simulation.StatusProject;
import com.korgutlova.diplom.model.enums.simulation.TaskDistributionType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shortName;

    private String name;

    private String description;

    private String language;

    private String version;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private StatusProject status = StatusProject.DRAFT;

    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;

    @Enumerated(EnumType.STRING)
    private TaskDistributionType taskDistributionType;

    //необходимое время работы в неделю в часах
    private int workHoursPerWeek;

    @OneToMany
    private Set<Bot> bots;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    @JsonIgnore
    private User creator;
}
