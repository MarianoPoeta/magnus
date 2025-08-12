package com.magnus.web.rest;

import static com.magnus.domain.ActivityAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Activity;
import com.magnus.domain.enumeration.ActivityCategory;
import com.magnus.repository.ActivityRepository;
import com.magnus.service.dto.ActivityDTO;
import com.magnus.service.mapper.ActivityMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ActivityCategory DEFAULT_CATEGORY = ActivityCategory.ADVENTURE;
    private static final ActivityCategory UPDATED_CATEGORY = ActivityCategory.OUTDOOR;

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BASE_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_BASE_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_BASE_COST = new BigDecimal(1);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Integer DEFAULT_MAX_CAPACITY = 1;
    private static final Integer UPDATED_MAX_CAPACITY = 2;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TRANSPORT_REQUIRED = false;
    private static final Boolean UPDATED_TRANSPORT_REQUIRED = true;

    private static final Boolean DEFAULT_TRANSPORT_INCLUDED = false;
    private static final Boolean UPDATED_TRANSPORT_INCLUDED = true;

    private static final String DEFAULT_EQUIPMENT_PROVIDED = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_PROVIDED = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_TEMPLATE = false;
    private static final Boolean UPDATED_IS_TEMPLATE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityMockMvc;

    private Activity activity;

    private Activity insertedActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createEntity() {
        return new Activity()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .basePrice(DEFAULT_BASE_PRICE)
            .baseCost(DEFAULT_BASE_COST)
            .duration(DEFAULT_DURATION)
            .maxCapacity(DEFAULT_MAX_CAPACITY)
            .location(DEFAULT_LOCATION)
            .transportRequired(DEFAULT_TRANSPORT_REQUIRED)
            .transportIncluded(DEFAULT_TRANSPORT_INCLUDED)
            .equipmentProvided(DEFAULT_EQUIPMENT_PROVIDED)
            .requirements(DEFAULT_REQUIREMENTS)
            .isActive(DEFAULT_IS_ACTIVE)
            .isTemplate(DEFAULT_IS_TEMPLATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createUpdatedEntity() {
        return new Activity()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .baseCost(UPDATED_BASE_COST)
            .duration(UPDATED_DURATION)
            .maxCapacity(UPDATED_MAX_CAPACITY)
            .location(UPDATED_LOCATION)
            .transportRequired(UPDATED_TRANSPORT_REQUIRED)
            .transportIncluded(UPDATED_TRANSPORT_INCLUDED)
            .equipmentProvided(UPDATED_EQUIPMENT_PROVIDED)
            .requirements(UPDATED_REQUIREMENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        activity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedActivity != null) {
            activityRepository.delete(insertedActivity);
            insertedActivity = null;
        }
    }

    @Test
    @Transactional
    void createActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);
        var returnedActivityDTO = om.readValue(
            restActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActivityDTO.class
        );

        // Validate the Activity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedActivity = activityMapper.toEntity(returnedActivityDTO);
        assertActivityUpdatableFieldsEquals(returnedActivity, getPersistedActivity(returnedActivity));

        insertedActivity = returnedActivity;
    }

    @Test
    @Transactional
    void createActivityWithExistingId() throws Exception {
        // Create the Activity with an existing ID
        activity.setId(1L);
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setName(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setCategory(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBasePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setBasePrice(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setDuration(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setMaxCapacity(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setLocation(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransportRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setTransportRequired(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransportIncludedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setTransportIncluded(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setIsActive(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setIsTemplate(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setCreatedAt(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activity.setUpdatedAt(null);

        // Create the Activity, which fails.
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        restActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActivities() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get all the activityList
        restActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(sameNumber(DEFAULT_BASE_PRICE))))
            .andExpect(jsonPath("$.[*].baseCost").value(hasItem(sameNumber(DEFAULT_BASE_COST))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].maxCapacity").value(hasItem(DEFAULT_MAX_CAPACITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].transportRequired").value(hasItem(DEFAULT_TRANSPORT_REQUIRED)))
            .andExpect(jsonPath("$.[*].transportIncluded").value(hasItem(DEFAULT_TRANSPORT_INCLUDED)))
            .andExpect(jsonPath("$.[*].equipmentProvided").value(hasItem(DEFAULT_EQUIPMENT_PROVIDED)))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isTemplate").value(hasItem(DEFAULT_IS_TEMPLATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getActivity() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.basePrice").value(sameNumber(DEFAULT_BASE_PRICE)))
            .andExpect(jsonPath("$.baseCost").value(sameNumber(DEFAULT_BASE_COST)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.maxCapacity").value(DEFAULT_MAX_CAPACITY))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.transportRequired").value(DEFAULT_TRANSPORT_REQUIRED))
            .andExpect(jsonPath("$.transportIncluded").value(DEFAULT_TRANSPORT_INCLUDED))
            .andExpect(jsonPath("$.equipmentProvided").value(DEFAULT_EQUIPMENT_PROVIDED))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isTemplate").value(DEFAULT_IS_TEMPLATE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivity() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity
        Activity updatedActivity = activityRepository.findById(activity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivity are not directly saved in db
        em.detach(updatedActivity);
        updatedActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .baseCost(UPDATED_BASE_COST)
            .duration(UPDATED_DURATION)
            .maxCapacity(UPDATED_MAX_CAPACITY)
            .location(UPDATED_LOCATION)
            .transportRequired(UPDATED_TRANSPORT_REQUIRED)
            .transportIncluded(UPDATED_TRANSPORT_INCLUDED)
            .equipmentProvided(UPDATED_EQUIPMENT_PROVIDED)
            .requirements(UPDATED_REQUIREMENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ActivityDTO activityDTO = activityMapper.toDto(updatedActivity);

        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivityToMatchAllProperties(updatedActivity);
    }

    @Test
    @Transactional
    void putNonExistingActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .duration(UPDATED_DURATION)
            .location(UPDATED_LOCATION)
            .transportRequired(UPDATED_TRANSPORT_REQUIRED)
            .equipmentProvided(UPDATED_EQUIPMENT_PROVIDED)
            .requirements(UPDATED_REQUIREMENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT);

        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedActivity, activity), getPersistedActivity(activity));
    }

    @Test
    @Transactional
    void fullUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        partialUpdatedActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .baseCost(UPDATED_BASE_COST)
            .duration(UPDATED_DURATION)
            .maxCapacity(UPDATED_MAX_CAPACITY)
            .location(UPDATED_LOCATION)
            .transportRequired(UPDATED_TRANSPORT_REQUIRED)
            .transportIncluded(UPDATED_TRANSPORT_INCLUDED)
            .equipmentProvided(UPDATED_EQUIPMENT_PROVIDED)
            .requirements(UPDATED_REQUIREMENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivity))
            )
            .andExpect(status().isOk());

        // Validate the Activity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityUpdatableFieldsEquals(partialUpdatedActivity, getPersistedActivity(partialUpdatedActivity));
    }

    @Test
    @Transactional
    void patchNonExistingActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activity.setId(longCount.incrementAndGet());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivity() throws Exception {
        // Initialize the database
        insertedActivity = activityRepository.saveAndFlush(activity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activity
        restActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, activity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activityRepository.count();
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

    protected Activity getPersistedActivity(Activity activity) {
        return activityRepository.findById(activity.getId()).orElseThrow();
    }

    protected void assertPersistedActivityToMatchAllProperties(Activity expectedActivity) {
        assertActivityAllPropertiesEquals(expectedActivity, getPersistedActivity(expectedActivity));
    }

    protected void assertPersistedActivityToMatchUpdatableProperties(Activity expectedActivity) {
        assertActivityAllUpdatablePropertiesEquals(expectedActivity, getPersistedActivity(expectedActivity));
    }
}
