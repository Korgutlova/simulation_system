package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.dto.SpentTimeTaskDto;
import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.tasktracker.SpentTimeTask;
import com.korgutlova.diplom.model.mapper.SpentTimeTaskMapper;
import com.korgutlova.diplom.repository.SpentTimeTaskRepository;
import com.korgutlova.diplom.service.api.SpentTimeTaskService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpentTimeTaskServiceImpl implements SpentTimeTaskService {
    private final SpentTimeTaskMapper spentTimeTaskMapper;
    private final SpentTimeTaskRepository spentTimeTaskRepository;

    @Override
    public void addSpentTime(SpentTimeTaskDto spentTimeTaskDto) {
        spentTimeTaskRepository.save(spentTimeTaskMapper.toEntity(spentTimeTaskDto));
    }

    @Override
    public List<SpentTimeTask> findSpentTimeTasksForWeek(Simulation simulation) {
        return spentTimeTaskRepository
                .findAllByTask_SimulationAndStartDateIsBetween(
                        simulation,
                        LocalDateTime.now().minus(7, ChronoUnit.DAYS),
                        LocalDateTime.now()
                );
    }
}
