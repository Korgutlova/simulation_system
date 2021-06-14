package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.dto.BotDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import java.util.List;

public interface BotService {
    Bot findById(Long id);

    void addBots(List<BotDto> bots, Project project);

    List<Bot> findBots(Project project);
}
