package com.korgutlova.diplom.model.entity.question;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.tasktracker.Task;
import com.korgutlova.diplom.model.enums.roles.TeamRole;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "question_to_user")
public class QuestionToUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String question;

    //хранится через запятую различные верные ответы
    private String answers;

    //вопрос задает определенный бот, если null значит определенный бот по роли
    @JoinColumn(name = "bot_id")
    @ManyToOne
    private Bot bot;

    //если тут null, значит вопрос просто так, не для задачи
    @JoinColumn(name = "task_id")
    @ManyToOne
    private Task forTask;

    private Boolean isWelcomeMessage = false;

    @Enumerated(EnumType.STRING)
    private TeamRole questionTopic;

    public List<String> getListAnswers() {
        return Arrays.asList(answers.split(","));
    }

    public String getCorrectAnswer() {
        return getListAnswers().get(0);
    }
}
