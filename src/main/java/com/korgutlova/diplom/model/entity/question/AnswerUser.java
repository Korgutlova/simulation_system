package com.korgutlova.diplom.model.entity.question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "answer_user")
public class AnswerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String answer;

    //верный ли ответ
    private Boolean isRight;

    @Column(name = "question_id", nullable = false)
    @ManyToOne
    private QuestionToUserSimulation question;

}
