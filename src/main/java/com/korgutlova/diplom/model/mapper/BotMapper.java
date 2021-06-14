package com.korgutlova.diplom.model.mapper;

import com.korgutlova.diplom.model.dto.BotDto;
import com.korgutlova.diplom.model.dto.SignUpForm;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.service.api.GroupService;
import com.korgutlova.diplom.service.api.UserService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BotMapper {

    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "thirdName", source = "user.thirdName")
    @Mapping(target = "login", source = "user.login")
    @Mapping(target = "role", source = "teamRole")
    @Mapping(target = "userId", source = "user.id")
    BotDto toDto(Bot bot);

    List<BotDto> toDto(List<Bot> bots);
}
