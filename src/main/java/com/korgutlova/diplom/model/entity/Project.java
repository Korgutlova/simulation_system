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

    //необходимое время работы в неделю в часах,
    // если null значит списывание времени никак не регламентируется
    private Integer workHoursPerWeek;

    //уведомление о просроченных заданиях
    private boolean checkOverdueTasks = false;

    @OneToMany
    private Set<Bot> bots;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    @JsonIgnore
    private User creator;

    // информация нужная во время инициализации проекта
    private Integer countBotsMax = 4;

    private Integer countCustomQuestionMax = 10;

    private Integer countBotQuestionMax = 10;
}
