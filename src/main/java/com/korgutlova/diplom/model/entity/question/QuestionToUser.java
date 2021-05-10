package com.korgutlova.diplom.model.entity.question;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.enums.roles.TeamRole;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "question_to_user")
public class QuestionToUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;

    private String answer;

    //вопрос задает определенный бот, если null значит определенный бот по роли
    @Column(name = "bot_id")
    @ManyToOne
    private Bot bot;

    //если тут null, значит вопрос просто так, не для задачи
    @Column(name = "task_id")
    @ManyToOne
    private Task forTask;

    @Enumerated(EnumType.STRING)
    private TeamRole questionTopic;


}
