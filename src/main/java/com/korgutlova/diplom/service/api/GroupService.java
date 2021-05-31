package com.korgutlova.diplom.service.api;

import com.korgutlova.diplom.model.entity.Group;
import java.util.List;

public interface GroupService {
    List<Group> findAll();

    Group findById(Long id);
}
