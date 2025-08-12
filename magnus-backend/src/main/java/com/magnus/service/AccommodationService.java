package com.magnus.service;

import com.magnus.service.dto.AccommodationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.Accommodation}.
 */
public interface AccommodationService {
    /**
     * Save a accommodation.
     *
     * @param accommodationDTO the entity to save.
     * @return the persisted entity.
     */
    AccommodationDTO save(AccommodationDTO accommodationDTO);

    /**
     * Updates a accommodation.
     *
     * @param accommodationDTO the entity to update.
     * @return the persisted entity.
     */
    AccommodationDTO update(AccommodationDTO accommodationDTO);

    /**
     * Partially updates a accommodation.
     *
     * @param accommodationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccommodationDTO> partialUpdate(AccommodationDTO accommodationDTO);

    /**
     * Get all the accommodations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccommodationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accommodation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccommodationDTO> findOne(Long id);

    /**
     * Delete the "id" accommodation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
