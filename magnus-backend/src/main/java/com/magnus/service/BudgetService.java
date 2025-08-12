package com.magnus.service;

import com.magnus.service.dto.BudgetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.Budget}.
 */
public interface BudgetService {
    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save.
     * @return the persisted entity.
     */
    BudgetDTO save(BudgetDTO budgetDTO);

    /**
     * Updates a budget.
     *
     * @param budgetDTO the entity to update.
     * @return the persisted entity.
     */
    BudgetDTO update(BudgetDTO budgetDTO);

    /**
     * Partially updates a budget.
     *
     * @param budgetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BudgetDTO> partialUpdate(BudgetDTO budgetDTO);

    /**
     * Get all the budgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BudgetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" budget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BudgetDTO> findOne(Long id);

    /**
     * Delete the "id" budget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
