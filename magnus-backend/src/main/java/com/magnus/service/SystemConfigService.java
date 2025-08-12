package com.magnus.service;

import com.magnus.service.dto.SystemConfigDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.SystemConfig}.
 */
public interface SystemConfigService {
    /**
     * Save a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    SystemConfigDTO save(SystemConfigDTO systemConfigDTO);

    /**
     * Updates a systemConfig.
     *
     * @param systemConfigDTO the entity to update.
     * @return the persisted entity.
     */
    SystemConfigDTO update(SystemConfigDTO systemConfigDTO);

    /**
     * Partially updates a systemConfig.
     *
     * @param systemConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemConfigDTO> partialUpdate(SystemConfigDTO systemConfigDTO);

    /**
     * Get all the systemConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemConfigDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemConfigDTO> findOne(Long id);

    /**
     * Delete the "id" systemConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
