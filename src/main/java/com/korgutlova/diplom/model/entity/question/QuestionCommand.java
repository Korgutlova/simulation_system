package com.korgutlova.diplom.model.entity.question;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.enums.roles.TeamRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "question_command")
public class QuestionCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;

    private String answer;

    private String command;

    //если null значит это общий вопрос для всех проектов
    @Column(name = "project_id")
    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private TeamRole questionTopic;
}
