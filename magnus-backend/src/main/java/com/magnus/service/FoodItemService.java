package com.magnus.service;

import com.magnus.service.dto.FoodItemDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.FoodItem}.
 */
public interface FoodItemService {
    /**
     * Save a foodItem.
     *
     * @param foodItemDTO the entity to save.
     * @return the persisted entity.
     */
    FoodItemDTO save(FoodItemDTO foodItemDTO);

    /**
     * Updates a foodItem.
     *
     * @param foodItemDTO the entity to update.
     * @return the persisted entity.
     */
    FoodItemDTO update(FoodItemDTO foodItemDTO);

    /**
     * Partially updates a foodItem.
     *
     * @param foodItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FoodItemDTO> partialUpdate(FoodItemDTO foodItemDTO);

    /**
     * Get all the foodItems.
     *
     * @return the list of entities.
     */
    List<FoodItemDTO> findAll();

    /**
     * Get the "id" foodItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodItemDTO> findOne(Long id);

    /**
     * Delete the "id" foodItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
