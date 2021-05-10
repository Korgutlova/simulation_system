package com.korgutlova.diplom.model.entity;

import com.korgutlova.diplom.model.enums.simulation.CommunicationType;
import com.korgutlova.diplom.model.enums.simulation.TaskDistributionType;
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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;

    @Enumerated(EnumType.STRING)
    private TaskDistributionType taskDistributionType;

    @OneToMany
    private Set<Bot> bots;

    //необходимое время работы в неделю в часах
    private int workHoursPerWeek;

    //todo, редактировать может только создатель (также добавлять,вопросы/ботов)
//    @ManyToOne
//    @JoinColumn(nullable = false, name = "user_id")
//    private User creator;
}
