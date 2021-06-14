package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.BotDto;
import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.model.entity.Project;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.mapper.UserMapper;
import com.korgutlova.diplom.repository.BotRepository;
import com.korgutlova.diplom.service.api.BotService;
import com.korgutlova.diplom.service.api.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final BotRepository botRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public Bot findById(Long id) {
        return botRepository.findById(id).orElse(null);
    }

    @Override
    public void addBots(List<BotDto> bots, Project project) {
        bots.forEach(botDto ->
                {
                    User user = userMapper.toEntity(botDto);
                    userService.saveAsBot(user);

                    Bot bot = new Bot();
                    bot.setId(botDto.getId());
                    bot.setProject(project);
                    bot.setTeamRole(botDto.getRole());
                    bot.setUser(user);

                    botRepository.save(bot);
                }
        );
    }

    @Override
    public List<Bot> findBots(Project project) {
        return botRepository.findAllByProject(project);
    }
}
