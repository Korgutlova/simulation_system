package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.model.entity.Group;
import com.korgutlova.diplom.repository.GroupRepository;
import com.korgutlova.diplom.service.api.GroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;


    @Override
    public List<Group> findAll() {
        return (List<Group>) groupRepository.findAll();
    }

    @Override
    public Group findById(Long id) {
        if (id == null) {
            return null;
        }
        return groupRepository.findById(id).orElse(null);
    }
}
