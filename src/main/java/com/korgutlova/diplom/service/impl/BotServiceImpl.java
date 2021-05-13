package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Bot;
import com.korgutlova.diplom.repository.BotRepository;
import com.korgutlova.diplom.service.api.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;

    @Override
    public Bot findById(Long id) {
        return botRepository.findById(id).orElse(null);
    }
}
