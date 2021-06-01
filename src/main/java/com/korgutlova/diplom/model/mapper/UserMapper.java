package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.dto.BotDto;
import com.korgutlova.diplom.model.dto.SignUpForm;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.service.api.GroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { GroupService.class })
public interface UserMapper {
    @Mapping(target = "role", source = "role")
    @Mapping(target = "group", source = "group")
    User toEntity(SignUpForm signUpForm);

    @Mapping(target = "role", source = "role", ignore = true)
    User toEntity(BotDto botDto);
}
