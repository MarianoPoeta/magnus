package com.magnus.web.rest;

import com.magnus.repository.AccommodationRepository;
import com.magnus.service.AccommodationService;
import com.magnus.service.dto.AccommodationDTO;
import com.magnus.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.magnus.domain.Accommodation}.
 */
@RestController
@RequestMapping("/api/accommodations")
public class AccommodationResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccommodationResource.class);

    private static final String ENTITY_NAME = "accommodation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccommodationService accommodationService;

    private final AccommodationRepository accommodationRepository;

    public AccommodationResource(AccommodationService accommodationService, AccommodationRepository accommodationRepository) {
        this.accommodationService = accommodationService;
        this.accommodationRepository = accommodationRepository;
    }

    /**
     * {@code POST  /accommodations} : Create a new accommodation.
     *
     * @param accommodationDTO the accommodationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accommodationDTO, or with status {@code 400 (Bad Request)} if the accommodation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccommodationDTO> createAccommodation(@Valid @RequestBody AccommodationDTO accommodationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Accommodation : {}", accommodationDTO);
        if (accommodationDTO.getId() != null) {
            throw new BadRequestAlertException("A new accommodation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accommodationDTO = accommodationService.save(accommodationDTO);
        return ResponseEntity.created(new URI("/api/accommodations/" + accommodationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accommodationDTO.getId().toString()))
            .body(accommodationDTO);
    }

    /**
     * {@code PUT  /accommodations/:id} : Updates an existing accommodation.
     *
     * @param id the id of the accommodationDTO to save.
     * @param accommodationDTO the accommodationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accommodationDTO,
     * or with status {@code 400 (Bad Request)} if the accommodationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accommodationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccommodationDTO> updateAccommodation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccommodationDTO accommodationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Accommodation : {}, {}", id, accommodationDTO);
        if (accommodationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accommodationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accommodationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accommodationDTO = accommodationService.update(accommodationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accommodationDTO.getId().toString()))
            .body(accommodationDTO);
    }

    /**
     * {@code PATCH  /accommodations/:id} : Partial updates given fields of an existing accommodation, field will ignore if it is null
     *
     * @param id the id of the accommodationDTO to save.
     * @param accommodationDTO the accommodationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accommodationDTO,
     * or with status {@code 400 (Bad Request)} if the accommodationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accommodationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accommodationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccommodationDTO> partialUpdateAccommodation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccommodationDTO accommodationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Accommodation partially : {}, {}", id, accommodationDTO);
        if (accommodationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accommodationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accommodationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccommodationDTO> result = accommodationService.partialUpdate(accommodationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accommodationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /accommodations} : get all the accommodations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accommodations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccommodationDTO>> getAllAccommodations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Accommodations");
        Page<AccommodationDTO> page = accommodationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accommodations/:id} : get the "id" accommodation.
     *
     * @param id the id of the accommodationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accommodationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDTO> getAccommodation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Accommodation : {}", id);
        Optional<AccommodationDTO> accommodationDTO = accommodationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accommodationDTO);
    }

    /**
     * {@code DELETE  /accommodations/:id} : delete the "id" accommodation.
     *
     * @param id the id of the accommodationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccommodation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Accommodation : {}", id);
        accommodationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
