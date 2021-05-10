package com.korgutlova.diplom.model.entity.question;

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
@Entity(name = "custom_question")
public class CustomQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;

    private String answer;

    //вопрос разделенный на токены
    private String questionTokens;

    //ответ разделенный на токены
    private String answerTokens;

    @Enumerated(EnumType.STRING)
    private TeamRole typeQuestion;

    //если null значит это общий вопрос для всех проектов
    @Column(name = "project_id")
    @ManyToOne
    private Project project;

}
