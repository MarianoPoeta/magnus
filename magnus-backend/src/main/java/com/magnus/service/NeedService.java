package com.magnus.service;

import com.magnus.service.dto.NeedDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.Need}.
 */
public interface NeedService {
    /**
     * Save a need.
     *
     * @param needDTO the entity to save.
     * @return the persisted entity.
     */
    NeedDTO save(NeedDTO needDTO);

    /**
     * Updates a need.
     *
     * @param needDTO the entity to update.
     * @return the persisted entity.
     */
    NeedDTO update(NeedDTO needDTO);

    /**
     * Partially updates a need.
     *
     * @param needDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NeedDTO> partialUpdate(NeedDTO needDTO);

    /**
     * Get all the needs.
     *
     * @return the list of entities.
     */
    List<NeedDTO> findAll();

    /**
     * Get the "id" need.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NeedDTO> findOne(Long id);

    /**
     * Delete the "id" need.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
