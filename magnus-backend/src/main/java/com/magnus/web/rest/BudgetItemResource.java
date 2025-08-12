package com.magnus.web.rest;

import com.magnus.repository.BudgetItemRepository;
import com.magnus.service.BudgetItemService;
import com.magnus.service.dto.BudgetItemDTO;
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
 * REST controller for managing {@link com.magnus.domain.BudgetItem}.
 */
@RestController
@RequestMapping("/api/budget-items")
public class BudgetItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetItemResource.class);

    private static final String ENTITY_NAME = "budgetItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BudgetItemService budgetItemService;

    private final BudgetItemRepository budgetItemRepository;

    public BudgetItemResource(BudgetItemService budgetItemService, BudgetItemRepository budgetItemRepository) {
        this.budgetItemService = budgetItemService;
        this.budgetItemRepository = budgetItemRepository;
    }

    /**
     * {@code POST  /budget-items} : Create a new budgetItem.
     *
     * @param budgetItemDTO the budgetItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new budgetItemDTO, or with status {@code 400 (Bad Request)} if the budgetItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BudgetItemDTO> createBudgetItem(@Valid @RequestBody BudgetItemDTO budgetItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save BudgetItem : {}", budgetItemDTO);
        if (budgetItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new budgetItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        budgetItemDTO = budgetItemService.save(budgetItemDTO);
        return ResponseEntity.created(new URI("/api/budget-items/" + budgetItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, budgetItemDTO.getId().toString()))
            .body(budgetItemDTO);
    }

    /**
     * {@code PUT  /budget-items/:id} : Updates an existing budgetItem.
     *
     * @param id the id of the budgetItemDTO to save.
     * @param budgetItemDTO the budgetItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetItemDTO,
     * or with status {@code 400 (Bad Request)} if the budgetItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the budgetItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetItemDTO> updateBudgetItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BudgetItemDTO budgetItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BudgetItem : {}, {}", id, budgetItemDTO);
        if (budgetItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        budgetItemDTO = budgetItemService.update(budgetItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetItemDTO.getId().toString()))
            .body(budgetItemDTO);
    }

    /**
     * {@code PATCH  /budget-items/:id} : Partial updates given fields of an existing budgetItem, field will ignore if it is null
     *
     * @param id the id of the budgetItemDTO to save.
     * @param budgetItemDTO the budgetItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetItemDTO,
     * or with status {@code 400 (Bad Request)} if the budgetItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the budgetItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the budgetItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BudgetItemDTO> partialUpdateBudgetItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BudgetItemDTO budgetItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BudgetItem partially : {}, {}", id, budgetItemDTO);
        if (budgetItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!budgetItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BudgetItemDTO> result = budgetItemService.partialUpdate(budgetItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, budgetItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /budget-items} : get all the budgetItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of budgetItems in body.
     */
    @GetMapping("")
    public List<BudgetItemDTO> getAllBudgetItems() {
        LOG.debug("REST request to get all BudgetItems");
        return budgetItemService.findAll();
    }

    /**
     * {@code GET  /budget-items/:id} : get the "id" budgetItem.
     *
     * @param id the id of the budgetItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the budgetItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetItemDTO> getBudgetItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BudgetItem : {}", id);
        Optional<BudgetItemDTO> budgetItemDTO = budgetItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetItemDTO);
    }

    /**
     * {@code DELETE  /budget-items/:id} : delete the "id" budgetItem.
     *
     * @param id the id of the budgetItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BudgetItem : {}", id);
        budgetItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
