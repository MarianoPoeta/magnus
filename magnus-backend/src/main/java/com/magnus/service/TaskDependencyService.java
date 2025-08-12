package com.magnus.service;

import com.magnus.service.dto.TaskDependencyDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.TaskDependency}.
 */
public interface TaskDependencyService {
    /**
     * Save a taskDependency.
     *
     * @param taskDependencyDTO the entity to save.
     * @return the persisted entity.
     */
    TaskDependencyDTO save(TaskDependencyDTO taskDependencyDTO);

    /**
     * Updates a taskDependency.
     *
     * @param taskDependencyDTO the entity to update.
     * @return the persisted entity.
     */
    TaskDependencyDTO update(TaskDependencyDTO taskDependencyDTO);

    /**
     * Partially updates a taskDependency.
     *
     * @param taskDependencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskDependencyDTO> partialUpdate(TaskDependencyDTO taskDependencyDTO);

    /**
     * Get all the taskDependencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskDependencyDTO> findAll(Pageable pageable);

    /**
     * Get all the taskDependencies.
     *
     * @return the list of entities.
     */
    List<TaskDependencyDTO> findAll();

    /**
     * Get all the taskDependencies for a specific task.
     *
     * @param taskId the id of the dependent task.
     * @return the list of entities.
     */
    List<TaskDependencyDTO> findByDependentTask(Long taskId);

    /**
     * Get all the taskDependencies that are prerequisites for a specific task.
     *
     * @param taskId the id of the task.
     * @return the list of entities.
     */
    List<TaskDependencyDTO> findByPrerequisiteTask(Long taskId);

    /**
     * Get the "id" taskDependency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskDependencyDTO> findOne(Long id);

    /**
     * Delete the "id" taskDependency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
