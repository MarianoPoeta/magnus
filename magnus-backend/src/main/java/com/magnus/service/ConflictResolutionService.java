package com.magnus.service;

import com.magnus.service.dto.ConflictResolutionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.ConflictResolution}.
 */
public interface ConflictResolutionService {
    /**
     * Save a conflictResolution.
     *
     * @param conflictResolutionDTO the entity to save.
     * @return the persisted entity.
     */
    ConflictResolutionDTO save(ConflictResolutionDTO conflictResolutionDTO);

    /**
     * Updates a conflictResolution.
     *
     * @param conflictResolutionDTO the entity to update.
     * @return the persisted entity.
     */
    ConflictResolutionDTO update(ConflictResolutionDTO conflictResolutionDTO);

    /**
     * Partially updates a conflictResolution.
     *
     * @param conflictResolutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConflictResolutionDTO> partialUpdate(ConflictResolutionDTO conflictResolutionDTO);

    /**
     * Get all the conflictResolutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConflictResolutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" conflictResolution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConflictResolutionDTO> findOne(Long id);

    /**
     * Delete the "id" conflictResolution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
