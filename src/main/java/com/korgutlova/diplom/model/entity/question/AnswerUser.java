package com.korgutlova.diplom.model.entity.question;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @JoinColumn(name = "question_id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private QuestionToUserSimulation question;

}
