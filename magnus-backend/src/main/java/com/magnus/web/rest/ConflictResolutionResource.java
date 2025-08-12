package com.magnus.web.rest;

import com.magnus.repository.ConflictResolutionRepository;
import com.magnus.service.ConflictResolutionService;
import com.magnus.service.dto.ConflictResolutionDTO;
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
 * REST controller for managing {@link com.magnus.domain.ConflictResolution}.
 */
@RestController
@RequestMapping("/api/conflict-resolutions")
public class ConflictResolutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConflictResolutionResource.class);

    private static final String ENTITY_NAME = "conflictResolution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConflictResolutionService conflictResolutionService;

    private final ConflictResolutionRepository conflictResolutionRepository;

    public ConflictResolutionResource(
        ConflictResolutionService conflictResolutionService,
        ConflictResolutionRepository conflictResolutionRepository
    ) {
        this.conflictResolutionService = conflictResolutionService;
        this.conflictResolutionRepository = conflictResolutionRepository;
    }

    /**
     * {@code POST  /conflict-resolutions} : Create a new conflictResolution.
     *
     * @param conflictResolutionDTO the conflictResolutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conflictResolutionDTO, or with status {@code 400 (Bad Request)} if the conflictResolution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConflictResolutionDTO> createConflictResolution(@Valid @RequestBody ConflictResolutionDTO conflictResolutionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ConflictResolution : {}", conflictResolutionDTO);
        if (conflictResolutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new conflictResolution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        conflictResolutionDTO = conflictResolutionService.save(conflictResolutionDTO);
        return ResponseEntity.created(new URI("/api/conflict-resolutions/" + conflictResolutionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, conflictResolutionDTO.getId().toString()))
            .body(conflictResolutionDTO);
    }

    /**
     * {@code PUT  /conflict-resolutions/:id} : Updates an existing conflictResolution.
     *
     * @param id the id of the conflictResolutionDTO to save.
     * @param conflictResolutionDTO the conflictResolutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conflictResolutionDTO,
     * or with status {@code 400 (Bad Request)} if the conflictResolutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conflictResolutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConflictResolutionDTO> updateConflictResolution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConflictResolutionDTO conflictResolutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ConflictResolution : {}, {}", id, conflictResolutionDTO);
        if (conflictResolutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conflictResolutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conflictResolutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        conflictResolutionDTO = conflictResolutionService.update(conflictResolutionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conflictResolutionDTO.getId().toString()))
            .body(conflictResolutionDTO);
    }

    /**
     * {@code PATCH  /conflict-resolutions/:id} : Partial updates given fields of an existing conflictResolution, field will ignore if it is null
     *
     * @param id the id of the conflictResolutionDTO to save.
     * @param conflictResolutionDTO the conflictResolutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conflictResolutionDTO,
     * or with status {@code 400 (Bad Request)} if the conflictResolutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the conflictResolutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the conflictResolutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConflictResolutionDTO> partialUpdateConflictResolution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConflictResolutionDTO conflictResolutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ConflictResolution partially : {}, {}", id, conflictResolutionDTO);
        if (conflictResolutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conflictResolutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conflictResolutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConflictResolutionDTO> result = conflictResolutionService.partialUpdate(conflictResolutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conflictResolutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conflict-resolutions} : get all the conflictResolutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conflictResolutions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConflictResolutionDTO>> getAllConflictResolutions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ConflictResolutions");
        Page<ConflictResolutionDTO> page = conflictResolutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conflict-resolutions/:id} : get the "id" conflictResolution.
     *
     * @param id the id of the conflictResolutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conflictResolutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConflictResolutionDTO> getConflictResolution(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ConflictResolution : {}", id);
        Optional<ConflictResolutionDTO> conflictResolutionDTO = conflictResolutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conflictResolutionDTO);
    }

    /**
     * {@code DELETE  /conflict-resolutions/:id} : delete the "id" conflictResolution.
     *
     * @param id the id of the conflictResolutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConflictResolution(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ConflictResolution : {}", id);
        conflictResolutionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
