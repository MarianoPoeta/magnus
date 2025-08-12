package com.magnus.web.rest;

import com.magnus.repository.TransportAssignmentRepository;
import com.magnus.service.TransportAssignmentService;
import com.magnus.service.dto.TransportAssignmentDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.magnus.domain.TransportAssignment}.
 */
@RestController
@RequestMapping("/api/transport-assignments")
public class TransportAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransportAssignmentResource.class);

    private static final String ENTITY_NAME = "transportAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransportAssignmentService transportAssignmentService;

    private final TransportAssignmentRepository transportAssignmentRepository;

    public TransportAssignmentResource(
        TransportAssignmentService transportAssignmentService,
        TransportAssignmentRepository transportAssignmentRepository
    ) {
        this.transportAssignmentService = transportAssignmentService;
        this.transportAssignmentRepository = transportAssignmentRepository;
    }

    /**
     * {@code POST  /transport-assignments} : Create a new transportAssignment.
     *
     * @param transportAssignmentDTO the transportAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transportAssignmentDTO, or with status {@code 400 (Bad Request)} if the transportAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransportAssignmentDTO> createTransportAssignment(
        @Valid @RequestBody TransportAssignmentDTO transportAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TransportAssignment : {}", transportAssignmentDTO);
        if (transportAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new transportAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transportAssignmentDTO = transportAssignmentService.save(transportAssignmentDTO);
        return ResponseEntity.created(new URI("/api/transport-assignments/" + transportAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transportAssignmentDTO.getId().toString()))
            .body(transportAssignmentDTO);
    }

    /**
     * {@code PUT  /transport-assignments/:id} : Updates an existing transportAssignment.
     *
     * @param id the id of the transportAssignmentDTO to save.
     * @param transportAssignmentDTO the transportAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the transportAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transportAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransportAssignmentDTO> updateTransportAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransportAssignmentDTO transportAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransportAssignment : {}, {}", id, transportAssignmentDTO);
        if (transportAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transportAssignmentDTO = transportAssignmentService.update(transportAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportAssignmentDTO.getId().toString()))
            .body(transportAssignmentDTO);
    }

    /**
     * {@code PATCH  /transport-assignments/:id} : Partial updates given fields of an existing transportAssignment, field will ignore if it is null
     *
     * @param id the id of the transportAssignmentDTO to save.
     * @param transportAssignmentDTO the transportAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transportAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the transportAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transportAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transportAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransportAssignmentDTO> partialUpdateTransportAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransportAssignmentDTO transportAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransportAssignment partially : {}, {}", id, transportAssignmentDTO);
        if (transportAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transportAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transportAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransportAssignmentDTO> result = transportAssignmentService.partialUpdate(transportAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transportAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transport-assignments} : get all the transportAssignments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transportAssignments in body.
     */
    @GetMapping("")
    public List<TransportAssignmentDTO> getAllTransportAssignments() {
        LOG.debug("REST request to get all TransportAssignments");
        return transportAssignmentService.findAll();
    }

    /**
     * {@code GET  /transport-assignments/:id} : get the "id" transportAssignment.
     *
     * @param id the id of the transportAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transportAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransportAssignmentDTO> getTransportAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransportAssignment : {}", id);
        Optional<TransportAssignmentDTO> transportAssignmentDTO = transportAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transportAssignmentDTO);
    }

    /**
     * {@code DELETE  /transport-assignments/:id} : delete the "id" transportAssignment.
     *
     * @param id the id of the transportAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransportAssignment : {}", id);
        transportAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
