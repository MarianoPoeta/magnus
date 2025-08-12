package com.magnus.web.rest;

import static com.magnus.domain.WeeklyPlanAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.WeeklyPlan;
import com.magnus.domain.enumeration.WeeklyPlanStatus;
import com.magnus.repository.WeeklyPlanRepository;
import com.magnus.service.dto.WeeklyPlanDTO;
import com.magnus.service.mapper.WeeklyPlanMapper;
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
 * Integration tests for the {@link WeeklyPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeeklyPlanResourceIT {

    private static final LocalDate DEFAULT_WEEK_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WEEK_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_WEEK_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WEEK_END = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_NAME = "BBBBBBBBBB";

    private static final WeeklyPlanStatus DEFAULT_STATUS = WeeklyPlanStatus.DRAFT;
    private static final WeeklyPlanStatus UPDATED_STATUS = WeeklyPlanStatus.IN_PROGRESS;

    private static final Integer DEFAULT_TOTAL_BUDGETS = 0;
    private static final Integer UPDATED_TOTAL_BUDGETS = 1;

    private static final Integer DEFAULT_TOTAL_GUESTS = 0;
    private static final Integer UPDATED_TOTAL_GUESTS = 1;

    private static final BigDecimal DEFAULT_ESTIMATED_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ESTIMATED_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ACTUAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACTUAL_COST = new BigDecimal(1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CONSOLIDATED = false;
    private static final Boolean UPDATED_IS_CONSOLIDATED = true;

    private static final Instant DEFAULT_CONSOLIDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSOLIDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/weekly-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WeeklyPlanRepository weeklyPlanRepository;

    @Autowired
    private WeeklyPlanMapper weeklyPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeeklyPlanMockMvc;

    private WeeklyPlan weeklyPlan;

    private WeeklyPlan insertedWeeklyPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeeklyPlan createEntity(EntityManager em) {
        WeeklyPlan weeklyPlan = new WeeklyPlan()
            .weekStart(DEFAULT_WEEK_START)
            .weekEnd(DEFAULT_WEEK_END)
            .planName(DEFAULT_PLAN_NAME)
            .status(DEFAULT_STATUS)
            .totalBudgets(DEFAULT_TOTAL_BUDGETS)
            .totalGuests(DEFAULT_TOTAL_GUESTS)
            .estimatedCost(DEFAULT_ESTIMATED_COST)
            .actualCost(DEFAULT_ACTUAL_COST)
            .notes(DEFAULT_NOTES)
            .isConsolidated(DEFAULT_IS_CONSOLIDATED)
            .consolidatedAt(DEFAULT_CONSOLIDATED_AT)
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
        weeklyPlan.setCreatedBy(appUser);
        return weeklyPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeeklyPlan createUpdatedEntity(EntityManager em) {
        WeeklyPlan updatedWeeklyPlan = new WeeklyPlan()
            .weekStart(UPDATED_WEEK_START)
            .weekEnd(UPDATED_WEEK_END)
            .planName(UPDATED_PLAN_NAME)
            .status(UPDATED_STATUS)
            .totalBudgets(UPDATED_TOTAL_BUDGETS)
            .totalGuests(UPDATED_TOTAL_GUESTS)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .notes(UPDATED_NOTES)
            .isConsolidated(UPDATED_IS_CONSOLIDATED)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
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
        updatedWeeklyPlan.setCreatedBy(appUser);
        return updatedWeeklyPlan;
    }

    @BeforeEach
    void initTest() {
        weeklyPlan = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWeeklyPlan != null) {
            weeklyPlanRepository.delete(insertedWeeklyPlan);
            insertedWeeklyPlan = null;
        }
    }

    @Test
    @Transactional
    void createWeeklyPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);
        var returnedWeeklyPlanDTO = om.readValue(
            restWeeklyPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WeeklyPlanDTO.class
        );

        // Validate the WeeklyPlan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWeeklyPlan = weeklyPlanMapper.toEntity(returnedWeeklyPlanDTO);
        assertWeeklyPlanUpdatableFieldsEquals(returnedWeeklyPlan, getPersistedWeeklyPlan(returnedWeeklyPlan));

        insertedWeeklyPlan = returnedWeeklyPlan;
    }

    @Test
    @Transactional
    void createWeeklyPlanWithExistingId() throws Exception {
        // Create the WeeklyPlan with an existing ID
        weeklyPlan.setId(1L);
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWeekStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setWeekStart(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeekEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setWeekEnd(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlanNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setPlanName(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setStatus(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsConsolidatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setIsConsolidated(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setCreatedAt(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        weeklyPlan.setUpdatedAt(null);

        // Create the WeeklyPlan, which fails.
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        restWeeklyPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWeeklyPlans() throws Exception {
        // Initialize the database
        insertedWeeklyPlan = weeklyPlanRepository.saveAndFlush(weeklyPlan);

        // Get all the weeklyPlanList
        restWeeklyPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekStart").value(hasItem(DEFAULT_WEEK_START.toString())))
            .andExpect(jsonPath("$.[*].weekEnd").value(hasItem(DEFAULT_WEEK_END.toString())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalBudgets").value(hasItem(DEFAULT_TOTAL_BUDGETS)))
            .andExpect(jsonPath("$.[*].totalGuests").value(hasItem(DEFAULT_TOTAL_GUESTS)))
            .andExpect(jsonPath("$.[*].estimatedCost").value(hasItem(sameNumber(DEFAULT_ESTIMATED_COST))))
            .andExpect(jsonPath("$.[*].actualCost").value(hasItem(sameNumber(DEFAULT_ACTUAL_COST))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isConsolidated").value(hasItem(DEFAULT_IS_CONSOLIDATED)))
            .andExpect(jsonPath("$.[*].consolidatedAt").value(hasItem(DEFAULT_CONSOLIDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getWeeklyPlan() throws Exception {
        // Initialize the database
        insertedWeeklyPlan = weeklyPlanRepository.saveAndFlush(weeklyPlan);

        // Get the weeklyPlan
        restWeeklyPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, weeklyPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weeklyPlan.getId().intValue()))
            .andExpect(jsonPath("$.weekStart").value(DEFAULT_WEEK_START.toString()))
            .andExpect(jsonPath("$.weekEnd").value(DEFAULT_WEEK_END.toString()))
            .andExpect(jsonPath("$.planName").value(DEFAULT_PLAN_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalBudgets").value(DEFAULT_TOTAL_BUDGETS))
            .andExpect(jsonPath("$.totalGuests").value(DEFAULT_TOTAL_GUESTS))
            .andExpect(jsonPath("$.estimatedCost").value(sameNumber(DEFAULT_ESTIMATED_COST)))
            .andExpect(jsonPath("$.actualCost").value(sameNumber(DEFAULT_ACTUAL_COST)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isConsolidated").value(DEFAULT_IS_CONSOLIDATED))
            .andExpect(jsonPath("$.consolidatedAt").value(DEFAULT_CONSOLIDATED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWeeklyPlan() throws Exception {
        // Get the weeklyPlan
        restWeeklyPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeeklyPlan() throws Exception {
        // Initialize the database
        insertedWeeklyPlan = weeklyPlanRepository.saveAndFlush(weeklyPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weeklyPlan
        WeeklyPlan updatedWeeklyPlan = weeklyPlanRepository.findById(weeklyPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWeeklyPlan are not directly saved in db
        em.detach(updatedWeeklyPlan);
        updatedWeeklyPlan
            .weekStart(UPDATED_WEEK_START)
            .weekEnd(UPDATED_WEEK_END)
            .planName(UPDATED_PLAN_NAME)
            .status(UPDATED_STATUS)
            .totalBudgets(UPDATED_TOTAL_BUDGETS)
            .totalGuests(UPDATED_TOTAL_GUESTS)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .notes(UPDATED_NOTES)
            .isConsolidated(UPDATED_IS_CONSOLIDATED)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(updatedWeeklyPlan);

        restWeeklyPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weeklyPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weeklyPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWeeklyPlanToMatchAllProperties(updatedWeeklyPlan);
    }

    @Test
    @Transactional
    void putNonExistingWeeklyPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weeklyPlan.setId(longCount.incrementAndGet());

        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weeklyPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weeklyPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeeklyPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weeklyPlan.setId(longCount.incrementAndGet());

        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weeklyPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeeklyPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weeklyPlan.setId(longCount.incrementAndGet());

        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeeklyPlanWithPatch() throws Exception {
        // Initialize the database
        insertedWeeklyPlan = weeklyPlanRepository.saveAndFlush(weeklyPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weeklyPlan using partial update
        WeeklyPlan partialUpdatedWeeklyPlan = new WeeklyPlan();
        partialUpdatedWeeklyPlan.setId(weeklyPlan.getId());

        partialUpdatedWeeklyPlan
            .weekStart(UPDATED_WEEK_START)
            .planName(UPDATED_PLAN_NAME)
            .totalBudgets(UPDATED_TOTAL_BUDGETS)
            .totalGuests(UPDATED_TOTAL_GUESTS)
            .notes(UPDATED_NOTES)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT);

        restWeeklyPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeeklyPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeeklyPlan))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeeklyPlanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWeeklyPlan, weeklyPlan),
            getPersistedWeeklyPlan(weeklyPlan)
        );
    }

    @Test
    @Transactional
    void fullUpdateWeeklyPlanWithPatch() throws Exception {
        // Initialize the database
        insertedWeeklyPlan = weeklyPlanRepository.saveAndFlush(weeklyPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weeklyPlan using partial update
        WeeklyPlan partialUpdatedWeeklyPlan = new WeeklyPlan();
        partialUpdatedWeeklyPlan.setId(weeklyPlan.getId());

        partialUpdatedWeeklyPlan
            .weekStart(UPDATED_WEEK_START)
            .weekEnd(UPDATED_WEEK_END)
            .planName(UPDATED_PLAN_NAME)
            .status(UPDATED_STATUS)
            .totalBudgets(UPDATED_TOTAL_BUDGETS)
            .totalGuests(UPDATED_TOTAL_GUESTS)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .notes(UPDATED_NOTES)
            .isConsolidated(UPDATED_IS_CONSOLIDATED)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restWeeklyPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeeklyPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeeklyPlan))
            )
            .andExpect(status().isOk());

        // Validate the WeeklyPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeeklyPlanUpdatableFieldsEquals(partialUpdatedWeeklyPlan, getPersistedWeeklyPlan(partialUpdatedWeeklyPlan));
    }

    @Test
    @Transactional
    void patchNonExistingWeeklyPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weeklyPlan.setId(longCount.incrementAndGet());

        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeeklyPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weeklyPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weeklyPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeeklyPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weeklyPlan.setId(longCount.incrementAndGet());

        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weeklyPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeeklyPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        weeklyPlan.setId(longCount.incrementAndGet());

        // Create the WeeklyPlan
        WeeklyPlanDTO weeklyPlanDTO = weeklyPlanMapper.toDto(weeklyPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeeklyPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(weeklyPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeeklyPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeeklyPlan() throws Exception {
        // Initialize the database
        insertedWeeklyPlan = weeklyPlanRepository.saveAndFlush(weeklyPlan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the weeklyPlan
        restWeeklyPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, weeklyPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return weeklyPlanRepository.count();
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

    protected WeeklyPlan getPersistedWeeklyPlan(WeeklyPlan weeklyPlan) {
        return weeklyPlanRepository.findById(weeklyPlan.getId()).orElseThrow();
    }

    protected void assertPersistedWeeklyPlanToMatchAllProperties(WeeklyPlan expectedWeeklyPlan) {
        assertWeeklyPlanAllPropertiesEquals(expectedWeeklyPlan, getPersistedWeeklyPlan(expectedWeeklyPlan));
    }

    protected void assertPersistedWeeklyPlanToMatchUpdatableProperties(WeeklyPlan expectedWeeklyPlan) {
        assertWeeklyPlanAllUpdatablePropertiesEquals(expectedWeeklyPlan, getPersistedWeeklyPlan(expectedWeeklyPlan));
    }
}
