package com.magnus.service;

import com.magnus.service.dto.BudgetTemplateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.BudgetTemplate}.
 */
public interface BudgetTemplateService {
    /**
     * Save a budgetTemplate.
     *
     * @param budgetTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    BudgetTemplateDTO save(BudgetTemplateDTO budgetTemplateDTO);

    /**
     * Updates a budgetTemplate.
     *
     * @param budgetTemplateDTO the entity to update.
     * @return the persisted entity.
     */
    BudgetTemplateDTO update(BudgetTemplateDTO budgetTemplateDTO);

    /**
     * Partially updates a budgetTemplate.
     *
     * @param budgetTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BudgetTemplateDTO> partialUpdate(BudgetTemplateDTO budgetTemplateDTO);

    /**
     * Get all the budgetTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BudgetTemplateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" budgetTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BudgetTemplateDTO> findOne(Long id);

    /**
     * Delete the "id" budgetTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
