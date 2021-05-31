package com.korgutlova.diplom.model.dto;

import com.korgutlova.diplom.model.entity.Group;
import com.korgutlova.diplom.model.enums.roles.RoleForm;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpForm {
    private String login;

    private String firstName;

    private String lastName;

    //необязательно
    private String thirdName;

    //@Size(min = 6, max = 20, message = "Password should be from 6 to 20 symbols")
    private String password;

    private RoleForm role;

    private Long group;

}
