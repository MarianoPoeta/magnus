package com.magnus.web.rest;

import com.magnus.repository.BudgetTemplateRepository;
import com.magnus.service.BudgetTemplateService;
import com.magnus.service.dto.BudgetTemplateDTO;
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
 * REST controller for managing {@link com.magnus.domain.BudgetTemplate}.
 */
@RestController
@RequestMapping("/api/budget-templates")
public class BudgetTemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetTemplateResource.class);

    private static final String ENTITY_NAME = "budgetTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BudgetTemplateService budgetTemplateService;

    private final BudgetTemplateRepository budgetTemplateRepository;

    public BudgetTemplateResource(BudgetTemplateService budgetTemplateService, BudgetTemplateRepository budgetTemplateRepository) {
        this.budgetTemplateService = budgetTemplateService;
        this.budgetTemplateRepository = budgetTemplateRepository;
    }

    /**
     * {@code POST  /budget-templates} : Create a new budgetTemplate.
     *
     * @param budgetTemplateDTO the budgetTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new budgetTemplateDTO, or with status {@code 400 (Bad Request)} if the budgetTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BudgetTemplateDTO> createBudgetTemplate(@Valid @RequestBody BudgetTemplateDTO budgetTemplateDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BudgetTemplate : {}", budgetTemplateDTO);
        if (budgetTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new budgetTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        budgetTemplateDTO = budgetTemplateService.save(budgetTemplateDTO);
        return ResponseEntity.created(new URI("/api/budget-templates/" + budgetTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, budgetTemplateDTO.getId().toString()))
            .body(budgetTemplateDTO);
    }

    /**
     * {@code PUT  /budget-templates/:id} : Updates an existing budgetTemplate.
     *
     * @param id the id of the budgetTemplateDTO to save.
     * @param budgetTemplateDTO the budgetTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the budgetTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the budgetTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetTemplateDTO> updateBudgetTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BudgetTemplateDTO budgetTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BudgetTemplate : {}, {}", id, budgetTemplateDTO);
        if (budgetTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        budgetTemplateDTO = budgetTemplateService.update(budgetTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetTemplateDTO.getId().toString()))
            .body(budgetTemplateDTO);
    }

    /**
     * {@code PATCH  /budget-templates/:id} : Partial updates given fields of an existing budgetTemplate, field will ignore if it is null
     *
     * @param id the id of the budgetTemplateDTO to save.
     * @param budgetTemplateDTO the budgetTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the budgetTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the budgetTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the budgetTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BudgetTemplateDTO> partialUpdateBudgetTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BudgetTemplateDTO budgetTemplateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BudgetTemplate partially : {}, {}", id, budgetTemplateDTO);
        if (budgetTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BudgetTemplateDTO> result = budgetTemplateService.partialUpdate(budgetTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /budget-templates} : get all the budgetTemplates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of budgetTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BudgetTemplateDTO>> getAllBudgetTemplates(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of BudgetTemplates");
        Page<BudgetTemplateDTO> page = budgetTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /budget-templates/:id} : get the "id" budgetTemplate.
     *
     * @param id the id of the budgetTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the budgetTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetTemplateDTO> getBudgetTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BudgetTemplate : {}", id);
        Optional<BudgetTemplateDTO> budgetTemplateDTO = budgetTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetTemplateDTO);
    }

    /**
     * {@code DELETE  /budget-templates/:id} : delete the "id" budgetTemplate.
     *
     * @param id the id of the budgetTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BudgetTemplate : {}", id);
        budgetTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
