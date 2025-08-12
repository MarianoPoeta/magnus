package com.magnus.web.rest;

import com.magnus.repository.FoodItemRepository;
import com.magnus.service.FoodItemService;
import com.magnus.service.dto.FoodItemDTO;
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
 * REST controller for managing {@link com.magnus.domain.FoodItem}.
 */
@RestController
@RequestMapping("/api/food-items")
public class FoodItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(FoodItemResource.class);

    private static final String ENTITY_NAME = "foodItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodItemService foodItemService;

    private final FoodItemRepository foodItemRepository;

    public FoodItemResource(FoodItemService foodItemService, FoodItemRepository foodItemRepository) {
        this.foodItemService = foodItemService;
        this.foodItemRepository = foodItemRepository;
    }

    /**
     * {@code POST  /food-items} : Create a new foodItem.
     *
     * @param foodItemDTO the foodItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodItemDTO, or with status {@code 400 (Bad Request)} if the foodItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FoodItemDTO> createFoodItem(@Valid @RequestBody FoodItemDTO foodItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save FoodItem : {}", foodItemDTO);
        if (foodItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new foodItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        foodItemDTO = foodItemService.save(foodItemDTO);
        return ResponseEntity.created(new URI("/api/food-items/" + foodItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, foodItemDTO.getId().toString()))
            .body(foodItemDTO);
    }

    /**
     * {@code PUT  /food-items/:id} : Updates an existing foodItem.
     *
     * @param id the id of the foodItemDTO to save.
     * @param foodItemDTO the foodItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodItemDTO,
     * or with status {@code 400 (Bad Request)} if the foodItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FoodItemDTO foodItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FoodItem : {}, {}", id, foodItemDTO);
        if (foodItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        foodItemDTO = foodItemService.update(foodItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodItemDTO.getId().toString()))
            .body(foodItemDTO);
    }

    /**
     * {@code PATCH  /food-items/:id} : Partial updates given fields of an existing foodItem, field will ignore if it is null
     *
     * @param id the id of the foodItemDTO to save.
     * @param foodItemDTO the foodItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodItemDTO,
     * or with status {@code 400 (Bad Request)} if the foodItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the foodItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoodItemDTO> partialUpdateFoodItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FoodItemDTO foodItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FoodItem partially : {}, {}", id, foodItemDTO);
        if (foodItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodItemDTO> result = foodItemService.partialUpdate(foodItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /food-items} : get all the foodItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodItems in body.
     */
    @GetMapping("")
    public List<FoodItemDTO> getAllFoodItems() {
        LOG.debug("REST request to get all FoodItems");
        return foodItemService.findAll();
    }

    /**
     * {@code GET  /food-items/:id} : get the "id" foodItem.
     *
     * @param id the id of the foodItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDTO> getFoodItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FoodItem : {}", id);
        Optional<FoodItemDTO> foodItemDTO = foodItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodItemDTO);
    }

    /**
     * {@code DELETE  /food-items/:id} : delete the "id" foodItem.
     *
     * @param id the id of the foodItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FoodItem : {}", id);
        foodItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
