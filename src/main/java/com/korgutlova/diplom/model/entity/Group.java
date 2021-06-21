package com.korgutlova.diplom.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.korgutlova.diplom.model.enums.Course;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Course course;

    private String name;

    @ManyToOne
    @JoinColumn(name = "direction_id", nullable = false)
    @JsonIgnore
    private Direction direction;
}
