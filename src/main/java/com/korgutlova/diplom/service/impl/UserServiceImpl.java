package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.SignUpForm;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.mapper.UserMapper;
import com.korgutlova.diplom.repository.UserRepository;
import com.korgutlova.diplom.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    @Override
    public void register(SignUpForm signUpForm) {
        User user = userMapper.toEntity(signUpForm);
        user.setPassword(encoder.encode(signUpForm.getPassword()));
        userRepository.save(user);
    }

}
