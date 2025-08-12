package com.magnus.web.rest;

import static com.magnus.domain.CookingScheduleAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Budget;
import com.magnus.domain.CookingSchedule;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.MealType;
import com.magnus.repository.CookingScheduleRepository;
import com.magnus.service.dto.CookingScheduleDTO;
import com.magnus.service.mapper.CookingScheduleMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
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
 * Integration tests for the {@link CookingScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CookingScheduleResourceIT {

    private static final LocalDate DEFAULT_EVENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalTime DEFAULT_COOKING_TIME = LocalTime.NOON;
    private static final LocalTime UPDATED_COOKING_TIME = LocalTime.MAX.withNano(0);

    private static final MealType DEFAULT_MEAL_TYPE = MealType.BREAKFAST;
    private static final MealType UPDATED_MEAL_TYPE = MealType.LUNCH;

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_GUEST_COUNT = 1;
    private static final Integer UPDATED_GUEST_COUNT = 2;

    private static final String DEFAULT_SPECIAL_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_INSTRUCTIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_COMPLETED = false;
    private static final Boolean UPDATED_IS_COMPLETED = true;

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_INGREDIENTS_READY = false;
    private static final Boolean UPDATED_INGREDIENTS_READY = true;

    private static final Integer DEFAULT_ESTIMATED_DURATION = 0;
    private static final Integer UPDATED_ESTIMATED_DURATION = 1;

    private static final Integer DEFAULT_ACTUAL_DURATION = 0;
    private static final Integer UPDATED_ACTUAL_DURATION = 1;

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final ConflictStatus DEFAULT_CONFLICT_STATUS = ConflictStatus.NONE;
    private static final ConflictStatus UPDATED_CONFLICT_STATUS = ConflictStatus.DETECTED;

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cooking-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CookingScheduleRepository cookingScheduleRepository;

    @Autowired
    private CookingScheduleMapper cookingScheduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCookingScheduleMockMvc;

    private CookingSchedule cookingSchedule;

    private CookingSchedule insertedCookingSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookingSchedule createEntity(EntityManager em) {
        CookingSchedule cookingSchedule = new CookingSchedule()
            .eventDate(DEFAULT_EVENT_DATE)
            .cookingTime(DEFAULT_COOKING_TIME)
            .mealType(DEFAULT_MEAL_TYPE)
            .menuName(DEFAULT_MENU_NAME)
            .guestCount(DEFAULT_GUEST_COUNT)
            .specialInstructions(DEFAULT_SPECIAL_INSTRUCTIONS)
            .isCompleted(DEFAULT_IS_COMPLETED)
            .completedAt(DEFAULT_COMPLETED_AT)
            .ingredientsReady(DEFAULT_INGREDIENTS_READY)
            .estimatedDuration(DEFAULT_ESTIMATED_DURATION)
            .actualDuration(DEFAULT_ACTUAL_DURATION)
            .startedAt(DEFAULT_STARTED_AT)
            .version(DEFAULT_VERSION)
            .conflictStatus(DEFAULT_CONFLICT_STATUS)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            budget = BudgetResourceIT.createEntity(em);
            em.persist(budget);
            em.flush();
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        cookingSchedule.setBudget(budget);
        return cookingSchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookingSchedule createUpdatedEntity(EntityManager em) {
        CookingSchedule updatedCookingSchedule = new CookingSchedule()
            .eventDate(UPDATED_EVENT_DATE)
            .cookingTime(UPDATED_COOKING_TIME)
            .mealType(UPDATED_MEAL_TYPE)
            .menuName(UPDATED_MENU_NAME)
            .guestCount(UPDATED_GUEST_COUNT)
            .specialInstructions(UPDATED_SPECIAL_INSTRUCTIONS)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedAt(UPDATED_COMPLETED_AT)
            .ingredientsReady(UPDATED_INGREDIENTS_READY)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .startedAt(UPDATED_STARTED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            budget = BudgetResourceIT.createUpdatedEntity(em);
            em.persist(budget);
            em.flush();
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        updatedCookingSchedule.setBudget(budget);
        return updatedCookingSchedule;
    }

    @BeforeEach
    void initTest() {
        cookingSchedule = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCookingSchedule != null) {
            cookingScheduleRepository.delete(insertedCookingSchedule);
            insertedCookingSchedule = null;
        }
    }

    @Test
    @Transactional
    void createCookingSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);
        var returnedCookingScheduleDTO = om.readValue(
            restCookingScheduleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CookingScheduleDTO.class
        );

        // Validate the CookingSchedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCookingSchedule = cookingScheduleMapper.toEntity(returnedCookingScheduleDTO);
        assertCookingScheduleUpdatableFieldsEquals(returnedCookingSchedule, getPersistedCookingSchedule(returnedCookingSchedule));

        insertedCookingSchedule = returnedCookingSchedule;
    }

    @Test
    @Transactional
    void createCookingScheduleWithExistingId() throws Exception {
        // Create the CookingSchedule with an existing ID
        cookingSchedule.setId(1L);
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setEventDate(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCookingTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setCookingTime(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMealTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setMealType(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMenuNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setMenuName(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGuestCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setGuestCount(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsCompletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setIsCompleted(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIngredientsReadyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setIngredientsReady(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setVersion(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConflictStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setConflictStatus(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setCreatedAt(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cookingSchedule.setUpdatedAt(null);

        // Create the CookingSchedule, which fails.
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        restCookingScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCookingSchedules() throws Exception {
        // Initialize the database
        insertedCookingSchedule = cookingScheduleRepository.saveAndFlush(cookingSchedule);

        // Get all the cookingScheduleList
        restCookingScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cookingSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].cookingTime").value(hasItem(DEFAULT_COOKING_TIME.toString())))
            .andExpect(jsonPath("$.[*].mealType").value(hasItem(DEFAULT_MEAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME)))
            .andExpect(jsonPath("$.[*].guestCount").value(hasItem(DEFAULT_GUEST_COUNT)))
            .andExpect(jsonPath("$.[*].specialInstructions").value(hasItem(DEFAULT_SPECIAL_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].isCompleted").value(hasItem(DEFAULT_IS_COMPLETED)))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].ingredientsReady").value(hasItem(DEFAULT_INGREDIENTS_READY)))
            .andExpect(jsonPath("$.[*].estimatedDuration").value(hasItem(DEFAULT_ESTIMATED_DURATION)))
            .andExpect(jsonPath("$.[*].actualDuration").value(hasItem(DEFAULT_ACTUAL_DURATION)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].conflictStatus").value(hasItem(DEFAULT_CONFLICT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getCookingSchedule() throws Exception {
        // Initialize the database
        insertedCookingSchedule = cookingScheduleRepository.saveAndFlush(cookingSchedule);

        // Get the cookingSchedule
        restCookingScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, cookingSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cookingSchedule.getId().intValue()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.cookingTime").value(DEFAULT_COOKING_TIME.toString()))
            .andExpect(jsonPath("$.mealType").value(DEFAULT_MEAL_TYPE.toString()))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME))
            .andExpect(jsonPath("$.guestCount").value(DEFAULT_GUEST_COUNT))
            .andExpect(jsonPath("$.specialInstructions").value(DEFAULT_SPECIAL_INSTRUCTIONS))
            .andExpect(jsonPath("$.isCompleted").value(DEFAULT_IS_COMPLETED))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.ingredientsReady").value(DEFAULT_INGREDIENTS_READY))
            .andExpect(jsonPath("$.estimatedDuration").value(DEFAULT_ESTIMATED_DURATION))
            .andExpect(jsonPath("$.actualDuration").value(DEFAULT_ACTUAL_DURATION))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.conflictStatus").value(DEFAULT_CONFLICT_STATUS.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCookingSchedule() throws Exception {
        // Get the cookingSchedule
        restCookingScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCookingSchedule() throws Exception {
        // Initialize the database
        insertedCookingSchedule = cookingScheduleRepository.saveAndFlush(cookingSchedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cookingSchedule
        CookingSchedule updatedCookingSchedule = cookingScheduleRepository.findById(cookingSchedule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCookingSchedule are not directly saved in db
        em.detach(updatedCookingSchedule);
        updatedCookingSchedule
            .eventDate(UPDATED_EVENT_DATE)
            .cookingTime(UPDATED_COOKING_TIME)
            .mealType(UPDATED_MEAL_TYPE)
            .menuName(UPDATED_MENU_NAME)
            .guestCount(UPDATED_GUEST_COUNT)
            .specialInstructions(UPDATED_SPECIAL_INSTRUCTIONS)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedAt(UPDATED_COMPLETED_AT)
            .ingredientsReady(UPDATED_INGREDIENTS_READY)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .startedAt(UPDATED_STARTED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(updatedCookingSchedule);

        restCookingScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookingScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cookingScheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCookingScheduleToMatchAllProperties(updatedCookingSchedule);
    }

    @Test
    @Transactional
    void putNonExistingCookingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingSchedule.setId(longCount.incrementAndGet());

        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookingScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookingScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cookingScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCookingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingSchedule.setId(longCount.incrementAndGet());

        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cookingScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCookingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingSchedule.setId(longCount.incrementAndGet());

        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingScheduleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCookingScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedCookingSchedule = cookingScheduleRepository.saveAndFlush(cookingSchedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cookingSchedule using partial update
        CookingSchedule partialUpdatedCookingSchedule = new CookingSchedule();
        partialUpdatedCookingSchedule.setId(cookingSchedule.getId());

        partialUpdatedCookingSchedule
            .mealType(UPDATED_MEAL_TYPE)
            .guestCount(UPDATED_GUEST_COUNT)
            .completedAt(UPDATED_COMPLETED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCookingScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookingSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCookingSchedule))
            )
            .andExpect(status().isOk());

        // Validate the CookingSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCookingScheduleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCookingSchedule, cookingSchedule),
            getPersistedCookingSchedule(cookingSchedule)
        );
    }

    @Test
    @Transactional
    void fullUpdateCookingScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedCookingSchedule = cookingScheduleRepository.saveAndFlush(cookingSchedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cookingSchedule using partial update
        CookingSchedule partialUpdatedCookingSchedule = new CookingSchedule();
        partialUpdatedCookingSchedule.setId(cookingSchedule.getId());

        partialUpdatedCookingSchedule
            .eventDate(UPDATED_EVENT_DATE)
            .cookingTime(UPDATED_COOKING_TIME)
            .mealType(UPDATED_MEAL_TYPE)
            .menuName(UPDATED_MENU_NAME)
            .guestCount(UPDATED_GUEST_COUNT)
            .specialInstructions(UPDATED_SPECIAL_INSTRUCTIONS)
            .isCompleted(UPDATED_IS_COMPLETED)
            .completedAt(UPDATED_COMPLETED_AT)
            .ingredientsReady(UPDATED_INGREDIENTS_READY)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .startedAt(UPDATED_STARTED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCookingScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookingSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCookingSchedule))
            )
            .andExpect(status().isOk());

        // Validate the CookingSchedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCookingScheduleUpdatableFieldsEquals(
            partialUpdatedCookingSchedule,
            getPersistedCookingSchedule(partialUpdatedCookingSchedule)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCookingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingSchedule.setId(longCount.incrementAndGet());

        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookingScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cookingScheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cookingScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCookingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingSchedule.setId(longCount.incrementAndGet());

        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cookingScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCookingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cookingSchedule.setId(longCount.incrementAndGet());

        // Create the CookingSchedule
        CookingScheduleDTO cookingScheduleDTO = cookingScheduleMapper.toDto(cookingSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookingScheduleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cookingScheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookingSchedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCookingSchedule() throws Exception {
        // Initialize the database
        insertedCookingSchedule = cookingScheduleRepository.saveAndFlush(cookingSchedule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cookingSchedule
        restCookingScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cookingSchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cookingScheduleRepository.count();
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

    protected CookingSchedule getPersistedCookingSchedule(CookingSchedule cookingSchedule) {
        return cookingScheduleRepository.findById(cookingSchedule.getId()).orElseThrow();
    }

    protected void assertPersistedCookingScheduleToMatchAllProperties(CookingSchedule expectedCookingSchedule) {
        assertCookingScheduleAllPropertiesEquals(expectedCookingSchedule, getPersistedCookingSchedule(expectedCookingSchedule));
    }

    protected void assertPersistedCookingScheduleToMatchUpdatableProperties(CookingSchedule expectedCookingSchedule) {
        assertCookingScheduleAllUpdatablePropertiesEquals(expectedCookingSchedule, getPersistedCookingSchedule(expectedCookingSchedule));
    }
}
