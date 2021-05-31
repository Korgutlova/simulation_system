package com.korgutlova.diplom.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;
import com.korgutlova.diplom.model.enums.roles.Role;

@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String thirdName;

    private String login;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

    public String getFirstAndLastName() {
        return firstName + " " + lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName + " " + thirdName;
    }

    public String getNameWithRole() {
        return firstName + " " + lastName + " (" + role.getName() + ")";
    }

    public boolean isOrganizer() {
        return role == Role.ORGANIZER;
    }
}
