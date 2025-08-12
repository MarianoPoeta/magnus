package com.magnus.web.rest;

import static com.magnus.domain.NeedAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.Need;
import com.magnus.domain.Task;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.TaskStatus;
import com.magnus.repository.NeedRepository;
import com.magnus.service.dto.NeedDTO;
import com.magnus.service.mapper.NeedMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link NeedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NeedResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final TaskPriority DEFAULT_URGENCY = TaskPriority.LOW;
    private static final TaskPriority UPDATED_URGENCY = TaskPriority.MEDIUM;

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.TODO;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.IN_PROGRESS;

    private static final LocalDate DEFAULT_REQUESTED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUESTED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_REQUIRED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUIRED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FULFILLED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FULFILLED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_ESTIMATED_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ESTIMATED_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ACTUAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACTUAL_COST = new BigDecimal(1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/needs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NeedRepository needRepository;

    @Autowired
    private NeedMapper needMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNeedMockMvc;

    private Need need;

    private Need insertedNeed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Need createEntity(EntityManager em) {
        Need need = new Need()
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .unit(DEFAULT_UNIT)
            .urgency(DEFAULT_URGENCY)
            .status(DEFAULT_STATUS)
            .requestedDate(DEFAULT_REQUESTED_DATE)
            .requiredDate(DEFAULT_REQUIRED_DATE)
            .fulfilledDate(DEFAULT_FULFILLED_DATE)
            .estimatedCost(DEFAULT_ESTIMATED_COST)
            .actualCost(DEFAULT_ACTUAL_COST)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        need.setRequestedBy(appUser);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        need.setParentTask(task);
        return need;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Need createUpdatedEntity(EntityManager em) {
        Need updatedNeed = new Need()
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .urgency(UPDATED_URGENCY)
            .status(UPDATED_STATUS)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .requiredDate(UPDATED_REQUIRED_DATE)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createUpdatedEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        updatedNeed.setRequestedBy(appUser);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createUpdatedEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        updatedNeed.setParentTask(task);
        return updatedNeed;
    }

    @BeforeEach
    void initTest() {
        need = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedNeed != null) {
            needRepository.delete(insertedNeed);
            insertedNeed = null;
        }
    }

    @Test
    @Transactional
    void createNeed() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);
        var returnedNeedDTO = om.readValue(
            restNeedMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NeedDTO.class
        );

        // Validate the Need in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNeed = needMapper.toEntity(returnedNeedDTO);
        assertNeedUpdatableFieldsEquals(returnedNeed, getPersistedNeed(returnedNeed));

        insertedNeed = returnedNeed;
    }

    @Test
    @Transactional
    void createNeedWithExistingId() throws Exception {
        // Create the Need with an existing ID
        need.setId(1L);
        NeedDTO needDTO = needMapper.toDto(need);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setDescription(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setQuantity(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrgencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setUrgency(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setStatus(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setRequestedDate(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setCreatedAt(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        need.setUpdatedAt(null);

        // Create the Need, which fails.
        NeedDTO needDTO = needMapper.toDto(need);

        restNeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNeeds() throws Exception {
        // Initialize the database
        insertedNeed = needRepository.saveAndFlush(need);

        // Get all the needList
        restNeedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(need.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].urgency").value(hasItem(DEFAULT_URGENCY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestedDate").value(hasItem(DEFAULT_REQUESTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].requiredDate").value(hasItem(DEFAULT_REQUIRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].fulfilledDate").value(hasItem(DEFAULT_FULFILLED_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedCost").value(hasItem(sameNumber(DEFAULT_ESTIMATED_COST))))
            .andExpect(jsonPath("$.[*].actualCost").value(hasItem(sameNumber(DEFAULT_ACTUAL_COST))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getNeed() throws Exception {
        // Initialize the database
        insertedNeed = needRepository.saveAndFlush(need);

        // Get the need
        restNeedMockMvc
            .perform(get(ENTITY_API_URL_ID, need.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(need.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.urgency").value(DEFAULT_URGENCY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.requestedDate").value(DEFAULT_REQUESTED_DATE.toString()))
            .andExpect(jsonPath("$.requiredDate").value(DEFAULT_REQUIRED_DATE.toString()))
            .andExpect(jsonPath("$.fulfilledDate").value(DEFAULT_FULFILLED_DATE.toString()))
            .andExpect(jsonPath("$.estimatedCost").value(sameNumber(DEFAULT_ESTIMATED_COST)))
            .andExpect(jsonPath("$.actualCost").value(sameNumber(DEFAULT_ACTUAL_COST)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNeed() throws Exception {
        // Get the need
        restNeedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNeed() throws Exception {
        // Initialize the database
        insertedNeed = needRepository.saveAndFlush(need);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the need
        Need updatedNeed = needRepository.findById(need.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNeed are not directly saved in db
        em.detach(updatedNeed);
        updatedNeed
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .urgency(UPDATED_URGENCY)
            .status(UPDATED_STATUS)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .requiredDate(UPDATED_REQUIRED_DATE)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        NeedDTO needDTO = needMapper.toDto(updatedNeed);

        restNeedMockMvc
            .perform(put(ENTITY_API_URL_ID, needDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isOk());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNeedToMatchAllProperties(updatedNeed);
    }

    @Test
    @Transactional
    void putNonExistingNeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        need.setId(longCount.incrementAndGet());

        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(put(ENTITY_API_URL_ID, needDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        need.setId(longCount.incrementAndGet());

        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(needDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        need.setId(longCount.incrementAndGet());

        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNeedWithPatch() throws Exception {
        // Initialize the database
        insertedNeed = needRepository.saveAndFlush(need);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the need using partial update
        Need partialUpdatedNeed = new Need();
        partialUpdatedNeed.setId(need.getId());

        partialUpdatedNeed
            .description(UPDATED_DESCRIPTION)
            .unit(UPDATED_UNIT)
            .urgency(UPDATED_URGENCY)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .actualCost(UPDATED_ACTUAL_COST)
            .createdAt(UPDATED_CREATED_AT);

        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNeed))
            )
            .andExpect(status().isOk());

        // Validate the Need in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNeedUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNeed, need), getPersistedNeed(need));
    }

    @Test
    @Transactional
    void fullUpdateNeedWithPatch() throws Exception {
        // Initialize the database
        insertedNeed = needRepository.saveAndFlush(need);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the need using partial update
        Need partialUpdatedNeed = new Need();
        partialUpdatedNeed.setId(need.getId());

        partialUpdatedNeed
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .urgency(UPDATED_URGENCY)
            .status(UPDATED_STATUS)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .requiredDate(UPDATED_REQUIRED_DATE)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNeed))
            )
            .andExpect(status().isOk());

        // Validate the Need in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNeedUpdatableFieldsEquals(partialUpdatedNeed, getPersistedNeed(partialUpdatedNeed));
    }

    @Test
    @Transactional
    void patchNonExistingNeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        need.setId(longCount.incrementAndGet());

        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, needDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(needDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        need.setId(longCount.incrementAndGet());

        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(needDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNeed() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        need.setId(longCount.incrementAndGet());

        // Create the Need
        NeedDTO needDTO = needMapper.toDto(need);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNeedMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(needDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Need in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNeed() throws Exception {
        // Initialize the database
        insertedNeed = needRepository.saveAndFlush(need);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the need
        restNeedMockMvc
            .perform(delete(ENTITY_API_URL_ID, need.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return needRepository.count();
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

    protected Need getPersistedNeed(Need need) {
        return needRepository.findById(need.getId()).orElseThrow();
    }

    protected void assertPersistedNeedToMatchAllProperties(Need expectedNeed) {
        assertNeedAllPropertiesEquals(expectedNeed, getPersistedNeed(expectedNeed));
    }

    protected void assertPersistedNeedToMatchUpdatableProperties(Need expectedNeed) {
        assertNeedAllUpdatablePropertiesEquals(expectedNeed, getPersistedNeed(expectedNeed));
    }
}
