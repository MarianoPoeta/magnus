package com.magnus.service;

import com.magnus.service.dto.ProductRequirementDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.ProductRequirement}.
 */
public interface ProductRequirementService {
    /**
     * Save a productRequirement.
     *
     * @param productRequirementDTO the entity to save.
     * @return the persisted entity.
     */
    ProductRequirementDTO save(ProductRequirementDTO productRequirementDTO);

    /**
     * Updates a productRequirement.
     *
     * @param productRequirementDTO the entity to update.
     * @return the persisted entity.
     */
    ProductRequirementDTO update(ProductRequirementDTO productRequirementDTO);

    /**
     * Partially updates a productRequirement.
     *
     * @param productRequirementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductRequirementDTO> partialUpdate(ProductRequirementDTO productRequirementDTO);

    /**
     * Get all the productRequirements.
     *
     * @return the list of entities.
     */
    List<ProductRequirementDTO> findAll();

    /**
     * Get the "id" productRequirement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductRequirementDTO> findOne(Long id);

    /**
     * Delete the "id" productRequirement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
