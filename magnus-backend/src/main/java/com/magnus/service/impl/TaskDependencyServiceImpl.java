package com.magnus.service.impl;

import com.magnus.domain.TaskDependency;
import com.magnus.repository.TaskDependencyRepository;
import com.magnus.service.TaskDependencyService;
import com.magnus.service.dto.TaskDependencyDTO;
import com.magnus.service.mapper.TaskDependencyMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.TaskDependency}.
 */
@Service
@Transactional
public class TaskDependencyServiceImpl implements TaskDependencyService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskDependencyServiceImpl.class);

    private final TaskDependencyRepository taskDependencyRepository;

    private final TaskDependencyMapper taskDependencyMapper;

    public TaskDependencyServiceImpl(TaskDependencyRepository taskDependencyRepository, TaskDependencyMapper taskDependencyMapper) {
        this.taskDependencyRepository = taskDependencyRepository;
        this.taskDependencyMapper = taskDependencyMapper;
    }

    @Override
    public TaskDependencyDTO save(TaskDependencyDTO taskDependencyDTO) {
        LOG.debug("Request to save TaskDependency : {}", taskDependencyDTO);
        TaskDependency taskDependency = taskDependencyMapper.toEntity(taskDependencyDTO);
        taskDependency = taskDependencyRepository.save(taskDependency);
        return taskDependencyMapper.toDto(taskDependency);
    }

    @Override
    public TaskDependencyDTO update(TaskDependencyDTO taskDependencyDTO) {
        LOG.debug("Request to update TaskDependency : {}", taskDependencyDTO);
        TaskDependency taskDependency = taskDependencyMapper.toEntity(taskDependencyDTO);
        taskDependency = taskDependencyRepository.save(taskDependency);
        return taskDependencyMapper.toDto(taskDependency);
    }

    @Override
    public Optional<TaskDependencyDTO> partialUpdate(TaskDependencyDTO taskDependencyDTO) {
        LOG.debug("Request to partially update TaskDependency : {}", taskDependencyDTO);

        return taskDependencyRepository
            .findById(taskDependencyDTO.getId())
            .map(existingTaskDependency -> {
                taskDependencyMapper.partialUpdate(existingTaskDependency, taskDependencyDTO);

                return existingTaskDependency;
            })
            .map(taskDependencyRepository::save)
            .map(taskDependencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDependencyDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TaskDependencies");
        return taskDependencyRepository.findAll(pageable).map(taskDependencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDependencyDTO> findAll() {
        LOG.debug("Request to get all TaskDependencies");
        return taskDependencyRepository.findAll()
            .stream()
            .map(taskDependencyMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDependencyDTO> findByDependentTask(Long taskId) {
        LOG.debug("Request to get TaskDependencies for dependent task: {}", taskId);
        return taskDependencyRepository.findByDependentTaskId(taskId)
            .stream()
            .map(taskDependencyMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDependencyDTO> findByPrerequisiteTask(Long taskId) {
        LOG.debug("Request to get TaskDependencies for prerequisite task: {}", taskId);
        return taskDependencyRepository.findByPrerequisiteTaskId(taskId)
            .stream()
            .map(taskDependencyMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDependencyDTO> findOne(Long id) {
        LOG.debug("Request to get TaskDependency : {}", id);
        return taskDependencyRepository.findById(id).map(taskDependencyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TaskDependency : {}", id);
        taskDependencyRepository.deleteById(id);
    }
}
