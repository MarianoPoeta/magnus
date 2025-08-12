package com.magnus.service;

import com.magnus.service.dto.WeeklyPlanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.WeeklyPlan}.
 */
public interface WeeklyPlanService {
    /**
     * Save a weeklyPlan.
     *
     * @param weeklyPlanDTO the entity to save.
     * @return the persisted entity.
     */
    WeeklyPlanDTO save(WeeklyPlanDTO weeklyPlanDTO);

    /**
     * Updates a weeklyPlan.
     *
     * @param weeklyPlanDTO the entity to update.
     * @return the persisted entity.
     */
    WeeklyPlanDTO update(WeeklyPlanDTO weeklyPlanDTO);

    /**
     * Partially updates a weeklyPlan.
     *
     * @param weeklyPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WeeklyPlanDTO> partialUpdate(WeeklyPlanDTO weeklyPlanDTO);

    /**
     * Get all the weeklyPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WeeklyPlanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" weeklyPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WeeklyPlanDTO> findOne(Long id);

    /**
     * Delete the "id" weeklyPlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
