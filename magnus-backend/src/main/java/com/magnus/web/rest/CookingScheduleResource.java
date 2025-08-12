package com.magnus.web.rest;

import com.magnus.repository.CookingScheduleRepository;
import com.magnus.service.CookingScheduleService;
import com.magnus.service.dto.CookingScheduleDTO;
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
 * REST controller for managing {@link com.magnus.domain.CookingSchedule}.
 */
@RestController
@RequestMapping("/api/cooking-schedules")
public class CookingScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CookingScheduleResource.class);

    private static final String ENTITY_NAME = "cookingSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CookingScheduleService cookingScheduleService;

    private final CookingScheduleRepository cookingScheduleRepository;

    public CookingScheduleResource(CookingScheduleService cookingScheduleService, CookingScheduleRepository cookingScheduleRepository) {
        this.cookingScheduleService = cookingScheduleService;
        this.cookingScheduleRepository = cookingScheduleRepository;
    }

    /**
     * {@code POST  /cooking-schedules} : Create a new cookingSchedule.
     *
     * @param cookingScheduleDTO the cookingScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cookingScheduleDTO, or with status {@code 400 (Bad Request)} if the cookingSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CookingScheduleDTO> createCookingSchedule(@Valid @RequestBody CookingScheduleDTO cookingScheduleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CookingSchedule : {}", cookingScheduleDTO);
        if (cookingScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new cookingSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cookingScheduleDTO = cookingScheduleService.save(cookingScheduleDTO);
        return ResponseEntity.created(new URI("/api/cooking-schedules/" + cookingScheduleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cookingScheduleDTO.getId().toString()))
            .body(cookingScheduleDTO);
    }

    /**
     * {@code PUT  /cooking-schedules/:id} : Updates an existing cookingSchedule.
     *
     * @param id the id of the cookingScheduleDTO to save.
     * @param cookingScheduleDTO the cookingScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookingScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the cookingScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cookingScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CookingScheduleDTO> updateCookingSchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CookingScheduleDTO cookingScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CookingSchedule : {}, {}", id, cookingScheduleDTO);
        if (cookingScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookingScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookingScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cookingScheduleDTO = cookingScheduleService.update(cookingScheduleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookingScheduleDTO.getId().toString()))
            .body(cookingScheduleDTO);
    }

    /**
     * {@code PATCH  /cooking-schedules/:id} : Partial updates given fields of an existing cookingSchedule, field will ignore if it is null
     *
     * @param id the id of the cookingScheduleDTO to save.
     * @param cookingScheduleDTO the cookingScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookingScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the cookingScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cookingScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cookingScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CookingScheduleDTO> partialUpdateCookingSchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CookingScheduleDTO cookingScheduleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CookingSchedule partially : {}, {}", id, cookingScheduleDTO);
        if (cookingScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookingScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookingScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CookingScheduleDTO> result = cookingScheduleService.partialUpdate(cookingScheduleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookingScheduleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cooking-schedules} : get all the cookingSchedules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cookingSchedules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CookingScheduleDTO>> getAllCookingSchedules(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CookingSchedules");
        Page<CookingScheduleDTO> page = cookingScheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cooking-schedules/:id} : get the "id" cookingSchedule.
     *
     * @param id the id of the cookingScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cookingScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CookingScheduleDTO> getCookingSchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CookingSchedule : {}", id);
        Optional<CookingScheduleDTO> cookingScheduleDTO = cookingScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cookingScheduleDTO);
    }

    /**
     * {@code DELETE  /cooking-schedules/:id} : delete the "id" cookingSchedule.
     *
     * @param id the id of the cookingScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCookingSchedule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CookingSchedule : {}", id);
        cookingScheduleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
