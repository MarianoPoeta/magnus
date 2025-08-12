package com.magnus.web.rest;

import com.magnus.repository.ShoppingItemRepository;
import com.magnus.service.ShoppingItemService;
import com.magnus.service.dto.ShoppingItemDTO;
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
 * REST controller for managing {@link com.magnus.domain.ShoppingItem}.
 */
@RestController
@RequestMapping("/api/shopping-items")
public class ShoppingItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingItemResource.class);

    private static final String ENTITY_NAME = "shoppingItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShoppingItemService shoppingItemService;

    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingItemResource(ShoppingItemService shoppingItemService, ShoppingItemRepository shoppingItemRepository) {
        this.shoppingItemService = shoppingItemService;
        this.shoppingItemRepository = shoppingItemRepository;
    }

    /**
     * {@code POST  /shopping-items} : Create a new shoppingItem.
     *
     * @param shoppingItemDTO the shoppingItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingItemDTO, or with status {@code 400 (Bad Request)} if the shoppingItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShoppingItemDTO> createShoppingItem(@Valid @RequestBody ShoppingItemDTO shoppingItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShoppingItem : {}", shoppingItemDTO);
        if (shoppingItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new shoppingItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shoppingItemDTO = shoppingItemService.save(shoppingItemDTO);
        return ResponseEntity.created(new URI("/api/shopping-items/" + shoppingItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shoppingItemDTO.getId().toString()))
            .body(shoppingItemDTO);
    }

    /**
     * {@code PUT  /shopping-items/:id} : Updates an existing shoppingItem.
     *
     * @param id the id of the shoppingItemDTO to save.
     * @param shoppingItemDTO the shoppingItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingItemDTO,
     * or with status {@code 400 (Bad Request)} if the shoppingItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingItemDTO> updateShoppingItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShoppingItemDTO shoppingItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShoppingItem : {}, {}", id, shoppingItemDTO);
        if (shoppingItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shoppingItemDTO = shoppingItemService.update(shoppingItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingItemDTO.getId().toString()))
            .body(shoppingItemDTO);
    }

    /**
     * {@code PATCH  /shopping-items/:id} : Partial updates given fields of an existing shoppingItem, field will ignore if it is null
     *
     * @param id the id of the shoppingItemDTO to save.
     * @param shoppingItemDTO the shoppingItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingItemDTO,
     * or with status {@code 400 (Bad Request)} if the shoppingItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shoppingItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shoppingItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShoppingItemDTO> partialUpdateShoppingItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShoppingItemDTO shoppingItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShoppingItem partially : {}, {}", id, shoppingItemDTO);
        if (shoppingItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shoppingItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shoppingItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShoppingItemDTO> result = shoppingItemService.partialUpdate(shoppingItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shopping-items} : get all the shoppingItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppingItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShoppingItemDTO>> getAllShoppingItems(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ShoppingItems");
        Page<ShoppingItemDTO> page = shoppingItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shopping-items/:id} : get the "id" shoppingItem.
     *
     * @param id the id of the shoppingItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingItemDTO> getShoppingItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShoppingItem : {}", id);
        Optional<ShoppingItemDTO> shoppingItemDTO = shoppingItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingItemDTO);
    }

    /**
     * {@code DELETE  /shopping-items/:id} : delete the "id" shoppingItem.
     *
     * @param id the id of the shoppingItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShoppingItem : {}", id);
        shoppingItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
