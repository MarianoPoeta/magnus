package com.magnus.service;

import com.magnus.service.dto.TransportAssignmentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.TransportAssignment}.
 */
public interface TransportAssignmentService {
    /**
     * Save a transportAssignment.
     *
     * @param transportAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    TransportAssignmentDTO save(TransportAssignmentDTO transportAssignmentDTO);

    /**
     * Updates a transportAssignment.
     *
     * @param transportAssignmentDTO the entity to update.
     * @return the persisted entity.
     */
    TransportAssignmentDTO update(TransportAssignmentDTO transportAssignmentDTO);

    /**
     * Partially updates a transportAssignment.
     *
     * @param transportAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransportAssignmentDTO> partialUpdate(TransportAssignmentDTO transportAssignmentDTO);

    /**
     * Get all the transportAssignments.
     *
     * @return the list of entities.
     */
    List<TransportAssignmentDTO> findAll();

    /**
     * Get the "id" transportAssignment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransportAssignmentDTO> findOne(Long id);

    /**
     * Delete the "id" transportAssignment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
