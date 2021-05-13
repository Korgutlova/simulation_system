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

    private String number;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "bot_id", nullable = false)
    private Bot creator;

    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.MAJOR;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private TypeTask typeTask = TypeTask.TASK;

    //сколько нужно дать верных ответов, чтобы выдать данную задачу пользователю
    private int rightQuestion;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
