package com.magnus.service.impl;

import com.magnus.domain.WorkflowTrigger;
import com.magnus.repository.WorkflowTriggerRepository;
import com.magnus.service.WorkflowTriggerService;
import com.magnus.service.dto.WorkflowTriggerDTO;
import com.magnus.service.mapper.WorkflowTriggerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.WorkflowTrigger}.
 */
@Service
@Transactional
public class WorkflowTriggerServiceImpl implements WorkflowTriggerService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTriggerServiceImpl.class);

    private final WorkflowTriggerRepository workflowTriggerRepository;

    private final WorkflowTriggerMapper workflowTriggerMapper;

    public WorkflowTriggerServiceImpl(WorkflowTriggerRepository workflowTriggerRepository, WorkflowTriggerMapper workflowTriggerMapper) {
        this.workflowTriggerRepository = workflowTriggerRepository;
        this.workflowTriggerMapper = workflowTriggerMapper;
    }

    @Override
    public WorkflowTriggerDTO save(WorkflowTriggerDTO workflowTriggerDTO) {
        LOG.debug("Request to save WorkflowTrigger : {}", workflowTriggerDTO);
        WorkflowTrigger workflowTrigger = workflowTriggerMapper.toEntity(workflowTriggerDTO);
        workflowTrigger = workflowTriggerRepository.save(workflowTrigger);
        return workflowTriggerMapper.toDto(workflowTrigger);
    }

    @Override
    public WorkflowTriggerDTO update(WorkflowTriggerDTO workflowTriggerDTO) {
        LOG.debug("Request to update WorkflowTrigger : {}", workflowTriggerDTO);
        WorkflowTrigger workflowTrigger = workflowTriggerMapper.toEntity(workflowTriggerDTO);
        workflowTrigger = workflowTriggerRepository.save(workflowTrigger);
        return workflowTriggerMapper.toDto(workflowTrigger);
    }

    @Override
    public Optional<WorkflowTriggerDTO> partialUpdate(WorkflowTriggerDTO workflowTriggerDTO) {
        LOG.debug("Request to partially update WorkflowTrigger : {}", workflowTriggerDTO);

        return workflowTriggerRepository
            .findById(workflowTriggerDTO.getId())
            .map(existingWorkflowTrigger -> {
                workflowTriggerMapper.partialUpdate(existingWorkflowTrigger, workflowTriggerDTO);

                return existingWorkflowTrigger;
            })
            .map(workflowTriggerRepository::save)
            .map(workflowTriggerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkflowTriggerDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WorkflowTriggers");
        return workflowTriggerRepository.findAll(pageable).map(workflowTriggerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkflowTriggerDTO> findOne(Long id) {
        LOG.debug("Request to get WorkflowTrigger : {}", id);
        return workflowTriggerRepository.findById(id).map(workflowTriggerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WorkflowTrigger : {}", id);
        workflowTriggerRepository.deleteById(id);
    }
}
