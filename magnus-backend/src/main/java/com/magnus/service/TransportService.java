package com.magnus.service;

import com.magnus.service.dto.TransportDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.Transport}.
 */
public interface TransportService {
    /**
     * Save a transport.
     *
     * @param transportDTO the entity to save.
     * @return the persisted entity.
     */
    TransportDTO save(TransportDTO transportDTO);

    /**
     * Updates a transport.
     *
     * @param transportDTO the entity to update.
     * @return the persisted entity.
     */
    TransportDTO update(TransportDTO transportDTO);

    /**
     * Partially updates a transport.
     *
     * @param transportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransportDTO> partialUpdate(TransportDTO transportDTO);

    /**
     * Get all the transports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransportDTO> findOne(Long id);

    /**
     * Delete the "id" transport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
