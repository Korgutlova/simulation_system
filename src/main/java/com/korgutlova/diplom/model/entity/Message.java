package com.korgutlova.diplom.model.entity;

import com.korgutlova.diplom.model.enums.DirectionMessage;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    @CreationTimestamp
    private LocalDate messageCreated;

    private Bot bot;


    private Simulation simulation;

    @Enumerated(EnumType.STRING)
    private DirectionMessage directionMessage;
}
