package com.magnus.service;

import com.magnus.service.dto.CookingScheduleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.CookingSchedule}.
 */
public interface CookingScheduleService {
    /**
     * Save a cookingSchedule.
     *
     * @param cookingScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    CookingScheduleDTO save(CookingScheduleDTO cookingScheduleDTO);

    /**
     * Updates a cookingSchedule.
     *
     * @param cookingScheduleDTO the entity to update.
     * @return the persisted entity.
     */
    CookingScheduleDTO update(CookingScheduleDTO cookingScheduleDTO);

    /**
     * Partially updates a cookingSchedule.
     *
     * @param cookingScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CookingScheduleDTO> partialUpdate(CookingScheduleDTO cookingScheduleDTO);

    /**
     * Get all the cookingSchedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CookingScheduleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cookingSchedule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CookingScheduleDTO> findOne(Long id);

    /**
     * Delete the "id" cookingSchedule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
