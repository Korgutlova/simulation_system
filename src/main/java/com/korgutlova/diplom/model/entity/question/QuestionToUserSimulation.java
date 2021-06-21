package com.korgutlova.diplom.model.entity.question;

import com.korgutlova.diplom.model.entity.Simulation;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity(name = "question_to_user_simulation")
public class QuestionToUserSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "question_id", nullable = false)
    @ManyToOne
    private QuestionToUser question;

    @JoinColumn(name = "simulation_id", nullable = false)
    @ManyToOne
    private Simulation simulation;

    @CreationTimestamp
    private LocalDateTime dateAsked;

    @OneToOne(mappedBy = "question", fetch = FetchType.EAGER)
    private AnswerUser answer;
}
