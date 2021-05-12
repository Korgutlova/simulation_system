package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.dto.SignUpForm;
import com.korgutlova.diplom.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(SignUpForm signUpForm);
}
