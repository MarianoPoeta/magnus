package com.magnus.service;

import com.magnus.service.dto.WorkflowTriggerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.WorkflowTrigger}.
 */
public interface WorkflowTriggerService {
    /**
     * Save a workflowTrigger.
     *
     * @param workflowTriggerDTO the entity to save.
     * @return the persisted entity.
     */
    WorkflowTriggerDTO save(WorkflowTriggerDTO workflowTriggerDTO);

    /**
     * Updates a workflowTrigger.
     *
     * @param workflowTriggerDTO the entity to update.
     * @return the persisted entity.
     */
    WorkflowTriggerDTO update(WorkflowTriggerDTO workflowTriggerDTO);

    /**
     * Partially updates a workflowTrigger.
     *
     * @param workflowTriggerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkflowTriggerDTO> partialUpdate(WorkflowTriggerDTO workflowTriggerDTO);

    /**
     * Get all the workflowTriggers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkflowTriggerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workflowTrigger.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkflowTriggerDTO> findOne(Long id);

    /**
     * Delete the "id" workflowTrigger.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
