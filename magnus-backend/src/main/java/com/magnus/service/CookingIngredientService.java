package com.magnus.service;

import com.magnus.service.dto.CookingIngredientDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.CookingIngredient}.
 */
public interface CookingIngredientService {
    /**
     * Save a cookingIngredient.
     *
     * @param cookingIngredientDTO the entity to save.
     * @return the persisted entity.
     */
    CookingIngredientDTO save(CookingIngredientDTO cookingIngredientDTO);

    /**
     * Updates a cookingIngredient.
     *
     * @param cookingIngredientDTO the entity to update.
     * @return the persisted entity.
     */
    CookingIngredientDTO update(CookingIngredientDTO cookingIngredientDTO);

    /**
     * Partially updates a cookingIngredient.
     *
     * @param cookingIngredientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CookingIngredientDTO> partialUpdate(CookingIngredientDTO cookingIngredientDTO);

    /**
     * Get all the cookingIngredients.
     *
     * @return the list of entities.
     */
    List<CookingIngredientDTO> findAll();

    /**
     * Get the "id" cookingIngredient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CookingIngredientDTO> findOne(Long id);

    /**
     * Delete the "id" cookingIngredient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
