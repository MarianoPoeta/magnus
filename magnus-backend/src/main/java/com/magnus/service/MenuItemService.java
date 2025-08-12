package com.magnus.service;

import com.magnus.service.dto.MenuItemDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.magnus.domain.MenuItem}.
 */
public interface MenuItemService {
    /**
     * Save a menuItem.
     *
     * @param menuItemDTO the entity to save.
     * @return the persisted entity.
     */
    MenuItemDTO save(MenuItemDTO menuItemDTO);

    /**
     * Updates a menuItem.
     *
     * @param menuItemDTO the entity to update.
     * @return the persisted entity.
     */
    MenuItemDTO update(MenuItemDTO menuItemDTO);

    /**
     * Partially updates a menuItem.
     *
     * @param menuItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO);

    /**
     * Get all the menuItems.
     *
     * @return the list of entities.
     */
    List<MenuItemDTO> findAll();

    /**
     * Get the "id" menuItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MenuItemDTO> findOne(Long id);

    /**
     * Delete the "id" menuItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
