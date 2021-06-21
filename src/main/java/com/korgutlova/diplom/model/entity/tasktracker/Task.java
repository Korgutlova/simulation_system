package com.korgutlova.diplom.model.entity.tasktracker;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.enums.task.*;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String shortId;

    private String name;

    private String description;

    private Integer difficult;

    @ManyToOne
    @JoinColumn(name = "bot_id", nullable = false)
    private Bot creator;

    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.MAJOR;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private TypeTask typeTask = TypeTask.TASK;

    //продолжительность задачи в часах (нужно для сроков задачи), по умолчанию 8 часов (обычный день)
    private Integer duration;

    private Integer order;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public String getViewName() {
        return "[" + this.shortId + "] " + this.name;
    }
}
