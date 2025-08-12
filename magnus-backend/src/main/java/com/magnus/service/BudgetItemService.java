package com.magnus.service;

import com.magnus.service.dto.BudgetItemDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.BudgetItem}.
 */
public interface BudgetItemService {
    /**
     * Save a budgetItem.
     *
     * @param budgetItemDTO the entity to save.
     * @return the persisted entity.
     */
    BudgetItemDTO save(BudgetItemDTO budgetItemDTO);

    /**
     * Updates a budgetItem.
     *
     * @param budgetItemDTO the entity to update.
     * @return the persisted entity.
     */
    BudgetItemDTO update(BudgetItemDTO budgetItemDTO);

    /**
     * Partially updates a budgetItem.
     *
     * @param budgetItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BudgetItemDTO> partialUpdate(BudgetItemDTO budgetItemDTO);

    /**
     * Get all the budgetItems.
     *
     * @return the list of entities.
     */
    List<BudgetItemDTO> findAll();

    /**
     * Get the "id" budgetItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BudgetItemDTO> findOne(Long id);

    /**
     * Delete the "id" budgetItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
