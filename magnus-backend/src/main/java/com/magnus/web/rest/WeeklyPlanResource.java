package com.magnus.web.rest;

import com.magnus.repository.WeeklyPlanRepository;
import com.magnus.service.WeeklyPlanService;
import com.magnus.service.dto.WeeklyPlanDTO;
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
 * REST controller for managing {@link com.magnus.domain.WeeklyPlan}.
 */
@RestController
@RequestMapping("/api/weekly-plans")
public class WeeklyPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(WeeklyPlanResource.class);

    private static final String ENTITY_NAME = "weeklyPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeeklyPlanService weeklyPlanService;

    private final WeeklyPlanRepository weeklyPlanRepository;

    public WeeklyPlanResource(WeeklyPlanService weeklyPlanService, WeeklyPlanRepository weeklyPlanRepository) {
        this.weeklyPlanService = weeklyPlanService;
        this.weeklyPlanRepository = weeklyPlanRepository;
    }

    /**
     * {@code POST  /weekly-plans} : Create a new weeklyPlan.
     *
     * @param weeklyPlanDTO the weeklyPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weeklyPlanDTO, or with status {@code 400 (Bad Request)} if the weeklyPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WeeklyPlanDTO> createWeeklyPlan(@Valid @RequestBody WeeklyPlanDTO weeklyPlanDTO) throws URISyntaxException {
        LOG.debug("REST request to save WeeklyPlan : {}", weeklyPlanDTO);
        if (weeklyPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklyPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        weeklyPlanDTO = weeklyPlanService.save(weeklyPlanDTO);
        return ResponseEntity.created(new URI("/api/weekly-plans/" + weeklyPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, weeklyPlanDTO.getId().toString()))
            .body(weeklyPlanDTO);
    }

    /**
     * {@code PUT  /weekly-plans/:id} : Updates an existing weeklyPlan.
     *
     * @param id the id of the weeklyPlanDTO to save.
     * @param weeklyPlanDTO the weeklyPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyPlanDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weeklyPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WeeklyPlanDTO> updateWeeklyPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WeeklyPlanDTO weeklyPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WeeklyPlan : {}, {}", id, weeklyPlanDTO);
        if (weeklyPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        weeklyPlanDTO = weeklyPlanService.update(weeklyPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyPlanDTO.getId().toString()))
            .body(weeklyPlanDTO);
    }

    /**
     * {@code PATCH  /weekly-plans/:id} : Partial updates given fields of an existing weeklyPlan, field will ignore if it is null
     *
     * @param id the id of the weeklyPlanDTO to save.
     * @param weeklyPlanDTO the weeklyPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyPlanDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weeklyPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weeklyPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeeklyPlanDTO> partialUpdateWeeklyPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WeeklyPlanDTO weeklyPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WeeklyPlan partially : {}, {}", id, weeklyPlanDTO);
        if (weeklyPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeeklyPlanDTO> result = weeklyPlanService.partialUpdate(weeklyPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weekly-plans} : get all the weeklyPlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weeklyPlans in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WeeklyPlanDTO>> getAllWeeklyPlans(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of WeeklyPlans");
        Page<WeeklyPlanDTO> page = weeklyPlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weekly-plans/:id} : get the "id" weeklyPlan.
     *
     * @param id the id of the weeklyPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weeklyPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WeeklyPlanDTO> getWeeklyPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WeeklyPlan : {}", id);
        Optional<WeeklyPlanDTO> weeklyPlanDTO = weeklyPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weeklyPlanDTO);
    }

    /**
     * {@code DELETE  /weekly-plans/:id} : delete the "id" weeklyPlan.
     *
     * @param id the id of the weeklyPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeeklyPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WeeklyPlan : {}", id);
        weeklyPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
