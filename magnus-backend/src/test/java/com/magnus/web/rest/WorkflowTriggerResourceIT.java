package com.magnus.web.rest;

import static com.magnus.domain.WorkflowTriggerAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.WorkflowTrigger;
import com.magnus.repository.WorkflowTriggerRepository;
import com.magnus.service.dto.WorkflowTriggerDTO;
import com.magnus.service.mapper.WorkflowTriggerMapper;
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
 * Integration tests for the {@link WorkflowTriggerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowTriggerResourceIT {

    private static final String DEFAULT_TRIGGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGGER_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_CONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_CONFIGURATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_EXECUTION_ORDER = 1;
    private static final Integer UPDATED_EXECUTION_ORDER = 2;

    private static final Instant DEFAULT_LAST_EXECUTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_EXECUTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_EXECUTION_COUNT = 0;
    private static final Integer UPDATED_EXECUTION_COUNT = 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/workflow-triggers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowTriggerRepository workflowTriggerRepository;

    @Autowired
    private WorkflowTriggerMapper workflowTriggerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowTriggerMockMvc;

    private WorkflowTrigger workflowTrigger;

    private WorkflowTrigger insertedWorkflowTrigger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowTrigger createEntity(EntityManager em) {
        WorkflowTrigger workflowTrigger = new WorkflowTrigger()
            .triggerName(DEFAULT_TRIGGER_NAME)
            .entityType(DEFAULT_ENTITY_TYPE)
            .triggerCondition(DEFAULT_TRIGGER_CONDITION)
            .actionType(DEFAULT_ACTION_TYPE)
            .actionConfiguration(DEFAULT_ACTION_CONFIGURATION)
            .isActive(DEFAULT_IS_ACTIVE)
            .executionOrder(DEFAULT_EXECUTION_ORDER)
            .lastExecuted(DEFAULT_LAST_EXECUTED)
            .executionCount(DEFAULT_EXECUTION_COUNT)
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
        workflowTrigger.setCreatedBy(appUser);
        return workflowTrigger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowTrigger createUpdatedEntity(EntityManager em) {
        WorkflowTrigger updatedWorkflowTrigger = new WorkflowTrigger()
            .triggerName(UPDATED_TRIGGER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .triggerCondition(UPDATED_TRIGGER_CONDITION)
            .actionType(UPDATED_ACTION_TYPE)
            .actionConfiguration(UPDATED_ACTION_CONFIGURATION)
            .isActive(UPDATED_IS_ACTIVE)
            .executionOrder(UPDATED_EXECUTION_ORDER)
            .lastExecuted(UPDATED_LAST_EXECUTED)
            .executionCount(UPDATED_EXECUTION_COUNT)
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
        updatedWorkflowTrigger.setCreatedBy(appUser);
        return updatedWorkflowTrigger;
    }

    @BeforeEach
    void initTest() {
        workflowTrigger = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflowTrigger != null) {
            workflowTriggerRepository.delete(insertedWorkflowTrigger);
            insertedWorkflowTrigger = null;
        }
    }

    @Test
    @Transactional
    void createWorkflowTrigger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);
        var returnedWorkflowTriggerDTO = om.readValue(
            restWorkflowTriggerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowTriggerDTO.class
        );

        // Validate the WorkflowTrigger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflowTrigger = workflowTriggerMapper.toEntity(returnedWorkflowTriggerDTO);
        assertWorkflowTriggerUpdatableFieldsEquals(returnedWorkflowTrigger, getPersistedWorkflowTrigger(returnedWorkflowTrigger));

        insertedWorkflowTrigger = returnedWorkflowTrigger;
    }

    @Test
    @Transactional
    void createWorkflowTriggerWithExistingId() throws Exception {
        // Create the WorkflowTrigger with an existing ID
        workflowTrigger.setId(1L);
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTriggerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setTriggerName(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setEntityType(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setActionType(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setIsActive(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExecutionOrderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setExecutionOrder(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setCreatedAt(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowTrigger.setUpdatedAt(null);

        // Create the WorkflowTrigger, which fails.
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkflowTriggers() throws Exception {
        // Initialize the database
        insertedWorkflowTrigger = workflowTriggerRepository.saveAndFlush(workflowTrigger);

        // Get all the workflowTriggerList
        restWorkflowTriggerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowTrigger.getId().intValue())))
            .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].triggerCondition").value(hasItem(DEFAULT_TRIGGER_CONDITION)))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE)))
            .andExpect(jsonPath("$.[*].actionConfiguration").value(hasItem(DEFAULT_ACTION_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].executionOrder").value(hasItem(DEFAULT_EXECUTION_ORDER)))
            .andExpect(jsonPath("$.[*].lastExecuted").value(hasItem(DEFAULT_LAST_EXECUTED.toString())))
            .andExpect(jsonPath("$.[*].executionCount").value(hasItem(DEFAULT_EXECUTION_COUNT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getWorkflowTrigger() throws Exception {
        // Initialize the database
        insertedWorkflowTrigger = workflowTriggerRepository.saveAndFlush(workflowTrigger);

        // Get the workflowTrigger
        restWorkflowTriggerMockMvc
            .perform(get(ENTITY_API_URL_ID, workflowTrigger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowTrigger.getId().intValue()))
            .andExpect(jsonPath("$.triggerName").value(DEFAULT_TRIGGER_NAME))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.triggerCondition").value(DEFAULT_TRIGGER_CONDITION))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE))
            .andExpect(jsonPath("$.actionConfiguration").value(DEFAULT_ACTION_CONFIGURATION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.executionOrder").value(DEFAULT_EXECUTION_ORDER))
            .andExpect(jsonPath("$.lastExecuted").value(DEFAULT_LAST_EXECUTED.toString()))
            .andExpect(jsonPath("$.executionCount").value(DEFAULT_EXECUTION_COUNT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkflowTrigger() throws Exception {
        // Get the workflowTrigger
        restWorkflowTriggerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflowTrigger() throws Exception {
        // Initialize the database
        insertedWorkflowTrigger = workflowTriggerRepository.saveAndFlush(workflowTrigger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowTrigger
        WorkflowTrigger updatedWorkflowTrigger = workflowTriggerRepository.findById(workflowTrigger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflowTrigger are not directly saved in db
        em.detach(updatedWorkflowTrigger);
        updatedWorkflowTrigger
            .triggerName(UPDATED_TRIGGER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .triggerCondition(UPDATED_TRIGGER_CONDITION)
            .actionType(UPDATED_ACTION_TYPE)
            .actionConfiguration(UPDATED_ACTION_CONFIGURATION)
            .isActive(UPDATED_IS_ACTIVE)
            .executionOrder(UPDATED_EXECUTION_ORDER)
            .lastExecuted(UPDATED_LAST_EXECUTED)
            .executionCount(UPDATED_EXECUTION_COUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(updatedWorkflowTrigger);

        restWorkflowTriggerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowTriggerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowTriggerDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowTriggerToMatchAllProperties(updatedWorkflowTrigger);
    }

    @Test
    @Transactional
    void putNonExistingWorkflowTrigger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTrigger.setId(longCount.incrementAndGet());

        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowTriggerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowTriggerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowTriggerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflowTrigger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTrigger.setId(longCount.incrementAndGet());

        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTriggerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowTriggerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflowTrigger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTrigger.setId(longCount.incrementAndGet());

        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTriggerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowTriggerWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowTrigger = workflowTriggerRepository.saveAndFlush(workflowTrigger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowTrigger using partial update
        WorkflowTrigger partialUpdatedWorkflowTrigger = new WorkflowTrigger();
        partialUpdatedWorkflowTrigger.setId(workflowTrigger.getId());

        partialUpdatedWorkflowTrigger
            .triggerName(UPDATED_TRIGGER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .executionOrder(UPDATED_EXECUTION_ORDER)
            .executionCount(UPDATED_EXECUTION_COUNT);

        restWorkflowTriggerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowTrigger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowTrigger))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowTrigger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowTriggerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkflowTrigger, workflowTrigger),
            getPersistedWorkflowTrigger(workflowTrigger)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkflowTriggerWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowTrigger = workflowTriggerRepository.saveAndFlush(workflowTrigger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowTrigger using partial update
        WorkflowTrigger partialUpdatedWorkflowTrigger = new WorkflowTrigger();
        partialUpdatedWorkflowTrigger.setId(workflowTrigger.getId());

        partialUpdatedWorkflowTrigger
            .triggerName(UPDATED_TRIGGER_NAME)
            .entityType(UPDATED_ENTITY_TYPE)
            .triggerCondition(UPDATED_TRIGGER_CONDITION)
            .actionType(UPDATED_ACTION_TYPE)
            .actionConfiguration(UPDATED_ACTION_CONFIGURATION)
            .isActive(UPDATED_IS_ACTIVE)
            .executionOrder(UPDATED_EXECUTION_ORDER)
            .lastExecuted(UPDATED_LAST_EXECUTED)
            .executionCount(UPDATED_EXECUTION_COUNT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restWorkflowTriggerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowTrigger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowTrigger))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowTrigger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowTriggerUpdatableFieldsEquals(
            partialUpdatedWorkflowTrigger,
            getPersistedWorkflowTrigger(partialUpdatedWorkflowTrigger)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWorkflowTrigger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTrigger.setId(longCount.incrementAndGet());

        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowTriggerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowTriggerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowTriggerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflowTrigger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTrigger.setId(longCount.incrementAndGet());

        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTriggerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowTriggerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflowTrigger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTrigger.setId(longCount.incrementAndGet());

        // Create the WorkflowTrigger
        WorkflowTriggerDTO workflowTriggerDTO = workflowTriggerMapper.toDto(workflowTrigger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTriggerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowTriggerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowTrigger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkflowTrigger() throws Exception {
        // Initialize the database
        insertedWorkflowTrigger = workflowTriggerRepository.saveAndFlush(workflowTrigger);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workflowTrigger
        restWorkflowTriggerMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflowTrigger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workflowTriggerRepository.count();
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

    protected WorkflowTrigger getPersistedWorkflowTrigger(WorkflowTrigger workflowTrigger) {
        return workflowTriggerRepository.findById(workflowTrigger.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowTriggerToMatchAllProperties(WorkflowTrigger expectedWorkflowTrigger) {
        assertWorkflowTriggerAllPropertiesEquals(expectedWorkflowTrigger, getPersistedWorkflowTrigger(expectedWorkflowTrigger));
    }

    protected void assertPersistedWorkflowTriggerToMatchUpdatableProperties(WorkflowTrigger expectedWorkflowTrigger) {
        assertWorkflowTriggerAllUpdatablePropertiesEquals(expectedWorkflowTrigger, getPersistedWorkflowTrigger(expectedWorkflowTrigger));
    }
}
