package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.SignUpForm;
import com.korgutlova.diplom.model.entity.User;

public interface UserService {
    User findByLogin(String login);

    void register(SignUpForm signUpForm);
}
