package com.magnus.service;

import com.magnus.service.dto.ShoppingItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.ShoppingItem}.
 */
public interface ShoppingItemService {
    /**
     * Save a shoppingItem.
     *
     * @param shoppingItemDTO the entity to save.
     * @return the persisted entity.
     */
    ShoppingItemDTO save(ShoppingItemDTO shoppingItemDTO);

    /**
     * Updates a shoppingItem.
     *
     * @param shoppingItemDTO the entity to update.
     * @return the persisted entity.
     */
    ShoppingItemDTO update(ShoppingItemDTO shoppingItemDTO);

    /**
     * Partially updates a shoppingItem.
     *
     * @param shoppingItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShoppingItemDTO> partialUpdate(ShoppingItemDTO shoppingItemDTO);

    /**
     * Get all the shoppingItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShoppingItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" shoppingItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShoppingItemDTO> findOne(Long id);

    /**
     * Delete the "id" shoppingItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
