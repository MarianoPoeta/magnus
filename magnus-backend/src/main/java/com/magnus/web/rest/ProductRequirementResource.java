package com.magnus.web.rest;

import com.magnus.repository.ProductRequirementRepository;
import com.magnus.service.ProductRequirementService;
import com.magnus.service.dto.ProductRequirementDTO;
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
 * REST controller for managing {@link com.magnus.domain.ProductRequirement}.
 */
@RestController
@RequestMapping("/api/product-requirements")
public class ProductRequirementResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRequirementResource.class);

    private static final String ENTITY_NAME = "productRequirement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductRequirementService productRequirementService;

    private final ProductRequirementRepository productRequirementRepository;

    public ProductRequirementResource(
        ProductRequirementService productRequirementService,
        ProductRequirementRepository productRequirementRepository
    ) {
        this.productRequirementService = productRequirementService;
        this.productRequirementRepository = productRequirementRepository;
    }

    /**
     * {@code POST  /product-requirements} : Create a new productRequirement.
     *
     * @param productRequirementDTO the productRequirementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productRequirementDTO, or with status {@code 400 (Bad Request)} if the productRequirement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductRequirementDTO> createProductRequirement(@Valid @RequestBody ProductRequirementDTO productRequirementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductRequirement : {}", productRequirementDTO);
        if (productRequirementDTO.getId() != null) {
            throw new BadRequestAlertException("A new productRequirement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productRequirementDTO = productRequirementService.save(productRequirementDTO);
        return ResponseEntity.created(new URI("/api/product-requirements/" + productRequirementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productRequirementDTO.getId().toString()))
            .body(productRequirementDTO);
    }

    /**
     * {@code PUT  /product-requirements/:id} : Updates an existing productRequirement.
     *
     * @param id the id of the productRequirementDTO to save.
     * @param productRequirementDTO the productRequirementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productRequirementDTO,
     * or with status {@code 400 (Bad Request)} if the productRequirementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productRequirementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductRequirementDTO> updateProductRequirement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductRequirementDTO productRequirementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductRequirement : {}, {}", id, productRequirementDTO);
        if (productRequirementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productRequirementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRequirementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productRequirementDTO = productRequirementService.update(productRequirementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productRequirementDTO.getId().toString()))
            .body(productRequirementDTO);
    }

    /**
     * {@code PATCH  /product-requirements/:id} : Partial updates given fields of an existing productRequirement, field will ignore if it is null
     *
     * @param id the id of the productRequirementDTO to save.
     * @param productRequirementDTO the productRequirementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productRequirementDTO,
     * or with status {@code 400 (Bad Request)} if the productRequirementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productRequirementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productRequirementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductRequirementDTO> partialUpdateProductRequirement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductRequirementDTO productRequirementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductRequirement partially : {}, {}", id, productRequirementDTO);
        if (productRequirementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productRequirementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRequirementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductRequirementDTO> result = productRequirementService.partialUpdate(productRequirementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productRequirementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-requirements} : get all the productRequirements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productRequirements in body.
     */
    @GetMapping("")
    public List<ProductRequirementDTO> getAllProductRequirements() {
        LOG.debug("REST request to get all ProductRequirements");
        return productRequirementService.findAll();
    }

    /**
     * {@code GET  /product-requirements/:id} : get the "id" productRequirement.
     *
     * @param id the id of the productRequirementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productRequirementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductRequirementDTO> getProductRequirement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductRequirement : {}", id);
        Optional<ProductRequirementDTO> productRequirementDTO = productRequirementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productRequirementDTO);
    }

    /**
     * {@code DELETE  /product-requirements/:id} : delete the "id" productRequirement.
     *
     * @param id the id of the productRequirementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductRequirement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductRequirement : {}", id);
        productRequirementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
