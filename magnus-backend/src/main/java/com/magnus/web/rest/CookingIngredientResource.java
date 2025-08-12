package com.magnus.web.rest;

import com.magnus.repository.CookingIngredientRepository;
import com.magnus.service.CookingIngredientService;
import com.magnus.service.dto.CookingIngredientDTO;
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
 * REST controller for managing {@link com.magnus.domain.CookingIngredient}.
 */
@RestController
@RequestMapping("/api/cooking-ingredients")
public class CookingIngredientResource {

    private static final Logger LOG = LoggerFactory.getLogger(CookingIngredientResource.class);

    private static final String ENTITY_NAME = "cookingIngredient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CookingIngredientService cookingIngredientService;

    private final CookingIngredientRepository cookingIngredientRepository;

    public CookingIngredientResource(
        CookingIngredientService cookingIngredientService,
        CookingIngredientRepository cookingIngredientRepository
    ) {
        this.cookingIngredientService = cookingIngredientService;
        this.cookingIngredientRepository = cookingIngredientRepository;
    }

    /**
     * {@code POST  /cooking-ingredients} : Create a new cookingIngredient.
     *
     * @param cookingIngredientDTO the cookingIngredientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cookingIngredientDTO, or with status {@code 400 (Bad Request)} if the cookingIngredient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CookingIngredientDTO> createCookingIngredient(@Valid @RequestBody CookingIngredientDTO cookingIngredientDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CookingIngredient : {}", cookingIngredientDTO);
        if (cookingIngredientDTO.getId() != null) {
            throw new BadRequestAlertException("A new cookingIngredient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cookingIngredientDTO = cookingIngredientService.save(cookingIngredientDTO);
        return ResponseEntity.created(new URI("/api/cooking-ingredients/" + cookingIngredientDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cookingIngredientDTO.getId().toString()))
            .body(cookingIngredientDTO);
    }

    /**
     * {@code PUT  /cooking-ingredients/:id} : Updates an existing cookingIngredient.
     *
     * @param id the id of the cookingIngredientDTO to save.
     * @param cookingIngredientDTO the cookingIngredientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookingIngredientDTO,
     * or with status {@code 400 (Bad Request)} if the cookingIngredientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cookingIngredientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CookingIngredientDTO> updateCookingIngredient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CookingIngredientDTO cookingIngredientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CookingIngredient : {}, {}", id, cookingIngredientDTO);
        if (cookingIngredientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookingIngredientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookingIngredientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cookingIngredientDTO = cookingIngredientService.update(cookingIngredientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookingIngredientDTO.getId().toString()))
            .body(cookingIngredientDTO);
    }

    /**
     * {@code PATCH  /cooking-ingredients/:id} : Partial updates given fields of an existing cookingIngredient, field will ignore if it is null
     *
     * @param id the id of the cookingIngredientDTO to save.
     * @param cookingIngredientDTO the cookingIngredientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookingIngredientDTO,
     * or with status {@code 400 (Bad Request)} if the cookingIngredientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cookingIngredientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cookingIngredientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CookingIngredientDTO> partialUpdateCookingIngredient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CookingIngredientDTO cookingIngredientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CookingIngredient partially : {}, {}", id, cookingIngredientDTO);
        if (cookingIngredientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookingIngredientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookingIngredientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CookingIngredientDTO> result = cookingIngredientService.partialUpdate(cookingIngredientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookingIngredientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cooking-ingredients} : get all the cookingIngredients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cookingIngredients in body.
     */
    @GetMapping("")
    public List<CookingIngredientDTO> getAllCookingIngredients() {
        LOG.debug("REST request to get all CookingIngredients");
        return cookingIngredientService.findAll();
    }

    /**
     * {@code GET  /cooking-ingredients/:id} : get the "id" cookingIngredient.
     *
     * @param id the id of the cookingIngredientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cookingIngredientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CookingIngredientDTO> getCookingIngredient(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CookingIngredient : {}", id);
        Optional<CookingIngredientDTO> cookingIngredientDTO = cookingIngredientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cookingIngredientDTO);
    }

    /**
     * {@code DELETE  /cooking-ingredients/:id} : delete the "id" cookingIngredient.
     *
     * @param id the id of the cookingIngredientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCookingIngredient(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CookingIngredient : {}", id);
        cookingIngredientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
