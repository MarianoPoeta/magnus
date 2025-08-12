package com.magnus.web.rest;

import com.magnus.repository.TaskDependencyRepository;
import com.magnus.service.TaskDependencyService;
import com.magnus.service.dto.TaskDependencyDTO;
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
 * REST controller for managing {@link com.magnus.domain.TaskDependency}.
 */
@RestController
@RequestMapping("/api/task-dependencies")
public class TaskDependencyResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaskDependencyResource.class);

    private static final String ENTITY_NAME = "taskDependency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskDependencyService taskDependencyService;

    private final TaskDependencyRepository taskDependencyRepository;

    public TaskDependencyResource(TaskDependencyService taskDependencyService, TaskDependencyRepository taskDependencyRepository) {
        this.taskDependencyService = taskDependencyService;
        this.taskDependencyRepository = taskDependencyRepository;
    }

    /**
     * {@code POST  /task-dependencies} : Create a new taskDependency.
     *
     * @param taskDependencyDTO the taskDependencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskDependencyDTO, or with status {@code 400 (Bad Request)} if the taskDependency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaskDependencyDTO> createTaskDependency(@Valid @RequestBody TaskDependencyDTO taskDependencyDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TaskDependency : {}", taskDependencyDTO);
        if (taskDependencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new taskDependency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taskDependencyDTO = taskDependencyService.save(taskDependencyDTO);
        return ResponseEntity.created(new URI("/api/task-dependencies/" + taskDependencyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taskDependencyDTO.getId().toString()))
            .body(taskDependencyDTO);
    }

    /**
     * {@code PUT  /task-dependencies/:id} : Updates an existing taskDependency.
     *
     * @param id the id of the taskDependencyDTO to save.
     * @param taskDependencyDTO the taskDependencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskDependencyDTO,
     * or with status {@code 400 (Bad Request)} if the taskDependencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskDependencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDependencyDTO> updateTaskDependency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaskDependencyDTO taskDependencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TaskDependency : {}, {}", id, taskDependencyDTO);
        if (taskDependencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDependencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskDependencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taskDependencyDTO = taskDependencyService.update(taskDependencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskDependencyDTO.getId().toString()))
            .body(taskDependencyDTO);
    }

    /**
     * {@code PATCH  /task-dependencies/:id} : Partial updates given fields of an existing taskDependency, field will ignore if it is null
     *
     * @param id the id of the taskDependencyDTO to save.
     * @param taskDependencyDTO the taskDependencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskDependencyDTO,
     * or with status {@code 400 (Bad Request)} if the taskDependencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskDependencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskDependencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskDependencyDTO> partialUpdateTaskDependency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaskDependencyDTO taskDependencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TaskDependency partially : {}, {}", id, taskDependencyDTO);
        if (taskDependencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDependencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskDependencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskDependencyDTO> result = taskDependencyService.partialUpdate(taskDependencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskDependencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /task-dependencies} : get all the taskDependencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taskDependencies in body.
     */
    @GetMapping("")
    public List<TaskDependencyDTO> getAllTaskDependencies() {
        LOG.debug("REST request to get all TaskDependencies");
        return taskDependencyService.findAll();
    }

    /**
     * {@code GET  /task-dependencies/:id} : get the "id" taskDependency.
     *
     * @param id the id of the taskDependencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskDependencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDependencyDTO> getTaskDependency(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TaskDependency : {}", id);
        Optional<TaskDependencyDTO> taskDependencyDTO = taskDependencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskDependencyDTO);
    }

    /**
     * {@code DELETE  /task-dependencies/:id} : delete the "id" taskDependency.
     *
     * @param id the id of the taskDependencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskDependency(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TaskDependency : {}", id);
        taskDependencyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
