package com.korgutlova.diplom.service.impl;

import com.korgutlova.diplom.repository.ProjectRepository;
import com.korgutlova.diplom.service.api.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
}
