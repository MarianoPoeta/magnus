package com.magnus.web.rest;

import com.magnus.repository.NeedRepository;
import com.magnus.service.NeedService;
import com.magnus.service.dto.NeedDTO;
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
 * REST controller for managing {@link com.magnus.domain.Need}.
 */
@RestController
@RequestMapping("/api/needs")
public class NeedResource {

    private static final Logger LOG = LoggerFactory.getLogger(NeedResource.class);

    private static final String ENTITY_NAME = "need";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NeedService needService;

    private final NeedRepository needRepository;

    public NeedResource(NeedService needService, NeedRepository needRepository) {
        this.needService = needService;
        this.needRepository = needRepository;
    }

    /**
     * {@code POST  /needs} : Create a new need.
     *
     * @param needDTO the needDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new needDTO, or with status {@code 400 (Bad Request)} if the need has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NeedDTO> createNeed(@Valid @RequestBody NeedDTO needDTO) throws URISyntaxException {
        LOG.debug("REST request to save Need : {}", needDTO);
        if (needDTO.getId() != null) {
            throw new BadRequestAlertException("A new need cannot already have an ID", ENTITY_NAME, "idexists");
        }
        needDTO = needService.save(needDTO);
        return ResponseEntity.created(new URI("/api/needs/" + needDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, needDTO.getId().toString()))
            .body(needDTO);
    }

    /**
     * {@code PUT  /needs/:id} : Updates an existing need.
     *
     * @param id the id of the needDTO to save.
     * @param needDTO the needDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated needDTO,
     * or with status {@code 400 (Bad Request)} if the needDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the needDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NeedDTO> updateNeed(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NeedDTO needDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Need : {}, {}", id, needDTO);
        if (needDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, needDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!needRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        needDTO = needService.update(needDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, needDTO.getId().toString()))
            .body(needDTO);
    }

    /**
     * {@code PATCH  /needs/:id} : Partial updates given fields of an existing need, field will ignore if it is null
     *
     * @param id the id of the needDTO to save.
     * @param needDTO the needDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated needDTO,
     * or with status {@code 400 (Bad Request)} if the needDTO is not valid,
     * or with status {@code 404 (Not Found)} if the needDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the needDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NeedDTO> partialUpdateNeed(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NeedDTO needDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Need partially : {}, {}", id, needDTO);
        if (needDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, needDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!needRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NeedDTO> result = needService.partialUpdate(needDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, needDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /needs} : get all the needs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of needs in body.
     */
    @GetMapping("")
    public List<NeedDTO> getAllNeeds() {
        LOG.debug("REST request to get all Needs");
        return needService.findAll();
    }

    /**
     * {@code GET  /needs/:id} : get the "id" need.
     *
     * @param id the id of the needDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the needDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NeedDTO> getNeed(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Need : {}", id);
        Optional<NeedDTO> needDTO = needService.findOne(id);
        return ResponseUtil.wrapOrNotFound(needDTO);
    }

    /**
     * {@code DELETE  /needs/:id} : delete the "id" need.
     *
     * @param id the id of the needDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNeed(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Need : {}", id);
        needService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
