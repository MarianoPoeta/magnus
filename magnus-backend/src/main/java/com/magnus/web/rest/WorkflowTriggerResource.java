package com.magnus.web.rest;

import com.magnus.repository.WorkflowTriggerRepository;
import com.magnus.service.WorkflowTriggerService;
import com.magnus.service.dto.WorkflowTriggerDTO;
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
 * REST controller for managing {@link com.magnus.domain.WorkflowTrigger}.
 */
@RestController
@RequestMapping("/api/workflow-triggers")
public class WorkflowTriggerResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTriggerResource.class);

    private static final String ENTITY_NAME = "workflowTrigger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowTriggerService workflowTriggerService;

    private final WorkflowTriggerRepository workflowTriggerRepository;

    public WorkflowTriggerResource(WorkflowTriggerService workflowTriggerService, WorkflowTriggerRepository workflowTriggerRepository) {
        this.workflowTriggerService = workflowTriggerService;
        this.workflowTriggerRepository = workflowTriggerRepository;
    }

    /**
     * {@code POST  /workflow-triggers} : Create a new workflowTrigger.
     *
     * @param workflowTriggerDTO the workflowTriggerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workflowTriggerDTO, or with status {@code 400 (Bad Request)} if the workflowTrigger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkflowTriggerDTO> createWorkflowTrigger(@Valid @RequestBody WorkflowTriggerDTO workflowTriggerDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WorkflowTrigger : {}", workflowTriggerDTO);
        if (workflowTriggerDTO.getId() != null) {
            throw new BadRequestAlertException("A new workflowTrigger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workflowTriggerDTO = workflowTriggerService.save(workflowTriggerDTO);
        return ResponseEntity.created(new URI("/api/workflow-triggers/" + workflowTriggerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workflowTriggerDTO.getId().toString()))
            .body(workflowTriggerDTO);
    }

    /**
     * {@code PUT  /workflow-triggers/:id} : Updates an existing workflowTrigger.
     *
     * @param id the id of the workflowTriggerDTO to save.
     * @param workflowTriggerDTO the workflowTriggerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowTriggerDTO,
     * or with status {@code 400 (Bad Request)} if the workflowTriggerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workflowTriggerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkflowTriggerDTO> updateWorkflowTrigger(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkflowTriggerDTO workflowTriggerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WorkflowTrigger : {}, {}", id, workflowTriggerDTO);
        if (workflowTriggerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowTriggerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowTriggerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workflowTriggerDTO = workflowTriggerService.update(workflowTriggerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowTriggerDTO.getId().toString()))
            .body(workflowTriggerDTO);
    }

    /**
     * {@code PATCH  /workflow-triggers/:id} : Partial updates given fields of an existing workflowTrigger, field will ignore if it is null
     *
     * @param id the id of the workflowTriggerDTO to save.
     * @param workflowTriggerDTO the workflowTriggerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowTriggerDTO,
     * or with status {@code 400 (Bad Request)} if the workflowTriggerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workflowTriggerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workflowTriggerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkflowTriggerDTO> partialUpdateWorkflowTrigger(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkflowTriggerDTO workflowTriggerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WorkflowTrigger partially : {}, {}", id, workflowTriggerDTO);
        if (workflowTriggerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowTriggerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowTriggerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkflowTriggerDTO> result = workflowTriggerService.partialUpdate(workflowTriggerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowTriggerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workflow-triggers} : get all the workflowTriggers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workflowTriggers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorkflowTriggerDTO>> getAllWorkflowTriggers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of WorkflowTriggers");
        Page<WorkflowTriggerDTO> page = workflowTriggerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workflow-triggers/:id} : get the "id" workflowTrigger.
     *
     * @param id the id of the workflowTriggerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflowTriggerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowTriggerDTO> getWorkflowTrigger(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WorkflowTrigger : {}", id);
        Optional<WorkflowTriggerDTO> workflowTriggerDTO = workflowTriggerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workflowTriggerDTO);
    }

    /**
     * {@code DELETE  /workflow-triggers/:id} : delete the "id" workflowTrigger.
     *
     * @param id the id of the workflowTriggerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflowTrigger(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WorkflowTrigger : {}", id);
        workflowTriggerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
