package com.magnus.web.rest;

import static com.magnus.domain.CookingIngredientAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.CookingIngredient;
import com.magnus.domain.CookingSchedule;
import com.magnus.domain.ProductRequirement;
import com.magnus.domain.enumeration.ProductUnit;
import com.magnus.repository.CookingIngredientRepository;
import com.magnus.service.dto.CookingIngredientDTO;
import com.magnus.service.mapper.CookingIngredientMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CookingIngredientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CookingIngredientResourceIT {

    private static final Double DEFAULT_ORIGINAL_QUANTITY = 0D;
    private static final Double UPDATED_ORIGINAL_QUANTITY = 1D;

    private static final Double DEFAULT_MODIFIED_QUANTITY = 0D;
    private static final Double UPDATED_MODIFIED_QUANTITY = 1D;

    private static final ProductUnit DEFAULT_MODIFIED_UNIT = ProductUnit.KG;
    private static final ProductUnit UPDATED_MODIFIED_UNIT = ProductUnit.G;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ADDED_BY_USER = false;
    private static final Boolean UPDATED_ADDED_BY_USER = true;

    private static final Boolean DEFAULT_IS_AVAILABLE = false;
    private static final Boolean UPDATED_IS_AVAILABLE = true;

    private static final Instant DEFAULT_AVAILABLE_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AVAILABLE_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cooking-ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CookingIngredientRepository cookingIngredientRepository;

    @Autowired
    private CookingIngredientMapper cookingIngredientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCookingIngredientMockMvc;

    private CookingIngredient cookingIngredient;

    private CookingIngredient insertedCookingIngredient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookingIngredient createEntity(EntityManager em) {
        CookingIngredient cookingIngredient = new CookingIngredient()
            .originalQuantity(DEFAULT_ORIGINAL_QUANTITY)
            .modifiedQuantity(DEFAULT_MODIFIED_QUANTITY)
            .modifiedUnit(DEFAULT_MODIFIED_UNIT)
            .notes(DEFAULT_NOTES)
            .addedByUser(DEFAULT_ADDED_BY_USER)
            .isAvailable(DEFAULT_IS_AVAILABLE)
            .availableAt(DEFAULT_AVAILABLE_AT)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .version(DEFAULT_VERSION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        ProductRequirement productRequirement;
        if (TestUtil.findAll(em, ProductRequirement.class).isEmpty()) {
            productRequirement = ProductRequirementResourceIT.createEntity(em);
            em.persist(productRequirement);
            em.flush();
        } else {
            productRequirement = TestUtil.findAll(em, ProductRequirement.class).get(0);
        }
        cookingIngredient.setProductRequirement(productRequirement);
        // Add required entity
        CookingSchedule cookingSchedule;
        if (TestUtil.findAll(em, CookingSchedule.class).isEmpty()) {
            cookingSchedule = CookingScheduleResourceIT.createEntity(em);
            em.persist(cookingSchedule);
            em.flush();
        } else {
            cookingSchedule = TestUtil.findAll(em, CookingSchedule.class).get(0);
        }
        cookingIngredient.setCookingSchedule(cookingSchedule);
        return cookingIngredient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookingIngredient createUpdatedEntity(EntityManager em) {
        CookingIngredient updatedCookingIngredient = new CookingIngredient()
            .originalQuantity(UPDATED_ORIGINAL_QUANTITY)
            .modifiedQuantity(UPDATED_MODIFIED_QUANTITY)
            .modifiedUnit(UPDATED_MODIFIED_UNIT)
            .notes(UPDATED_NOTES)
            .addedByUser(UPDATED_ADDED_BY_USER)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .availableAt(UPDATED_AVAILABLE_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        ProductRequirement productRequirement;
        if (TestUtil.findAll(em, ProductRequirement.class).isEmpty()) {
            productRequirement = ProductRequirementResourceIT.createUpdatedEntity(em);
            em.persist(productRequirement);
            em.flush();
        } else {
            productRequirement = TestUtil.findAll(em, ProductRequirement.class).get(0);
        }
        updatedCookingIngredient.setProductRequirement(productRequirement);
        // Add required entity
        CookingSchedule cookingSchedule;
        if (TestUtil.findAll(em, CookingSchedule.class).isEmpty()) {
            cookingSchedule = CookingScheduleResourceIT.createUpdatedEntity(em);
            em.persist(cookingSchedule);
            em.flush();
        } else {
            cookingSchedule = TestUtil.findAll(em, CookingSchedule.class).get(0);
        }
        updatedCookingIngredient.setCookingSchedule(cookingSchedule);
        return updatedCookingIngredient;
    }

    @BeforeEach
    void initTest() {
        cookingIngredient = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCookingIngredient != null) {
            cookingIngredientRepository.delete(insertedCookingIngredient);
            insertedCookingIngredient = null;
        }
    }

    @Test
    @Transactional
    void createCookingIngredient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);
        var returnedCookingIngredientDTO = om.readValue(
            restCookingIngredientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CookingIngredientDTO.class
        );

        // Validate the CookingIngredient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCookingIngredient = cookingIngredientMapper.toEntity(returnedCookingIngredientDTO);
        assertCookingIngredientUpdatableFieldsEquals(returnedCookingIngredient, getPersistedCookingIngredient(returnedCookingIngredient));

        insertedCookingIngredient = returnedCookingIngredient;
    }

    @Test
    @Transactional
    void createCookingIngredientWithExistingId() throws Exception {
        // Create the CookingIngredient with an existing ID
        cookingIngredient.setId(1L);
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOriginalQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingIngredient.setOriginalQuantity(null);

        // Create the CookingIngredient, which fails.
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddedByUserIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingIngredient.setAddedByUser(null);

        // Create the CookingIngredient, which fails.
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAvailableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingIngredient.setIsAvailable(null);

        // Create the CookingIngredient, which fails.
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingIngredient.setVersion(null);

        // Create the CookingIngredient, which fails.
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingIngredient.setCreatedAt(null);

        // Create the CookingIngredient, which fails.
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingIngredient.setUpdatedAt(null);

        // Create the CookingIngredient, which fails.
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        restCookingIngredientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCookingIngredients() throws Exception {
        // Initialize the database
        insertedCookingIngredient = cookingIngredientRepository.saveAndFlush(cookingIngredient);

        // Get all the cookingIngredientList
        restCookingIngredientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cookingIngredient.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalQuantity").value(hasItem(DEFAULT_ORIGINAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].modifiedQuantity").value(hasItem(DEFAULT_MODIFIED_QUANTITY)))
            .andExpect(jsonPath("$.[*].modifiedUnit").value(hasItem(DEFAULT_MODIFIED_UNIT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].addedByUser").value(hasItem(DEFAULT_ADDED_BY_USER)))
            .andExpect(jsonPath("$.[*].isAvailable").value(hasItem(DEFAULT_IS_AVAILABLE)))
            .andExpect(jsonPath("$.[*].availableAt").value(hasItem(DEFAULT_AVAILABLE_AT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCookingIngredient() throws Exception {
        // Initialize the database
        insertedCookingIngredient = cookingIngredientRepository.saveAndFlush(cookingIngredient);

        // Get the cookingIngredient
        restCookingIngredientMockMvc
            .perform(get(ENTITY_API_URL_ID, cookingIngredient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cookingIngredient.getId().intValue()))
            .andExpect(jsonPath("$.originalQuantity").value(DEFAULT_ORIGINAL_QUANTITY))
            .andExpect(jsonPath("$.modifiedQuantity").value(DEFAULT_MODIFIED_QUANTITY))
            .andExpect(jsonPath("$.modifiedUnit").value(DEFAULT_MODIFIED_UNIT.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.addedByUser").value(DEFAULT_ADDED_BY_USER))
            .andExpect(jsonPath("$.isAvailable").value(DEFAULT_IS_AVAILABLE))
            .andExpect(jsonPath("$.availableAt").value(DEFAULT_AVAILABLE_AT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCookingIngredient() throws Exception {
        // Get the cookingIngredient
        restCookingIngredientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCookingIngredient() throws Exception {
        // Initialize the database
        insertedCookingIngredient = cookingIngredientRepository.saveAndFlush(cookingIngredient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cookingIngredient
        CookingIngredient updatedCookingIngredient = cookingIngredientRepository.findById(cookingIngredient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCookingIngredient are not directly saved in db
        em.detach(updatedCookingIngredient);
        updatedCookingIngredient
            .originalQuantity(UPDATED_ORIGINAL_QUANTITY)
            .modifiedQuantity(UPDATED_MODIFIED_QUANTITY)
            .modifiedUnit(UPDATED_MODIFIED_UNIT)
            .notes(UPDATED_NOTES)
            .addedByUser(UPDATED_ADDED_BY_USER)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .availableAt(UPDATED_AVAILABLE_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(updatedCookingIngredient);

        restCookingIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookingIngredientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cookingIngredientDTO))
            )
            .andExpect(status().isOk());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCookingIngredientToMatchAllProperties(updatedCookingIngredient);
    }

    @Test
    @Transactional
    void putNonExistingCookingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingIngredient.setId(longCount.incrementAndGet());

        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookingIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookingIngredientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cookingIngredientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCookingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingIngredient.setId(longCount.incrementAndGet());

        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingIngredientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cookingIngredientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCookingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingIngredient.setId(longCount.incrementAndGet());

        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingIngredientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCookingIngredientWithPatch() throws Exception {
        // Initialize the database
        insertedCookingIngredient = cookingIngredientRepository.saveAndFlush(cookingIngredient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cookingIngredient using partial update
        CookingIngredient partialUpdatedCookingIngredient = new CookingIngredient();
        partialUpdatedCookingIngredient.setId(cookingIngredient.getId());

        partialUpdatedCookingIngredient
            .originalQuantity(UPDATED_ORIGINAL_QUANTITY)
            .modifiedUnit(UPDATED_MODIFIED_UNIT)
            .addedByUser(UPDATED_ADDED_BY_USER)
            .availableAt(UPDATED_AVAILABLE_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCookingIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookingIngredient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCookingIngredient))
            )
            .andExpect(status().isOk());

        // Validate the CookingIngredient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCookingIngredientUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCookingIngredient, cookingIngredient),
            getPersistedCookingIngredient(cookingIngredient)
        );
    }

    @Test
    @Transactional
    void fullUpdateCookingIngredientWithPatch() throws Exception {
        // Initialize the database
        insertedCookingIngredient = cookingIngredientRepository.saveAndFlush(cookingIngredient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cookingIngredient using partial update
        CookingIngredient partialUpdatedCookingIngredient = new CookingIngredient();
        partialUpdatedCookingIngredient.setId(cookingIngredient.getId());

        partialUpdatedCookingIngredient
            .originalQuantity(UPDATED_ORIGINAL_QUANTITY)
            .modifiedQuantity(UPDATED_MODIFIED_QUANTITY)
            .modifiedUnit(UPDATED_MODIFIED_UNIT)
            .notes(UPDATED_NOTES)
            .addedByUser(UPDATED_ADDED_BY_USER)
            .isAvailable(UPDATED_IS_AVAILABLE)
            .availableAt(UPDATED_AVAILABLE_AT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCookingIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookingIngredient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCookingIngredient))
            )
            .andExpect(status().isOk());

        // Validate the CookingIngredient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCookingIngredientUpdatableFieldsEquals(
            partialUpdatedCookingIngredient,
            getPersistedCookingIngredient(partialUpdatedCookingIngredient)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCookingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingIngredient.setId(longCount.incrementAndGet());

        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookingIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cookingIngredientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cookingIngredientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCookingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingIngredient.setId(longCount.incrementAndGet());

        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingIngredientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cookingIngredientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCookingIngredient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingIngredient.setId(longCount.incrementAndGet());

        // Create the CookingIngredient
        CookingIngredientDTO cookingIngredientDTO = cookingIngredientMapper.toDto(cookingIngredient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingIngredientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cookingIngredientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookingIngredient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCookingIngredient() throws Exception {
        // Initialize the database
        insertedCookingIngredient = cookingIngredientRepository.saveAndFlush(cookingIngredient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cookingIngredient
        restCookingIngredientMockMvc
            .perform(delete(ENTITY_API_URL_ID, cookingIngredient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cookingIngredientRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CookingIngredient getPersistedCookingIngredient(CookingIngredient cookingIngredient) {
        return cookingIngredientRepository.findById(cookingIngredient.getId()).orElseThrow();
    }

    protected void assertPersistedCookingIngredientToMatchAllProperties(CookingIngredient expectedCookingIngredient) {
        assertCookingIngredientAllPropertiesEquals(expectedCookingIngredient, getPersistedCookingIngredient(expectedCookingIngredient));
    }

    protected void assertPersistedCookingIngredientToMatchUpdatableProperties(CookingIngredient expectedCookingIngredient) {
        assertCookingIngredientAllUpdatablePropertiesEquals(
            expectedCookingIngredient,
            getPersistedCookingIngredient(expectedCookingIngredient)
        );
    }
}
