package com.magnus.web.rest;

import static com.magnus.domain.TaskDependencyAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Task;
import com.magnus.domain.TaskDependency;
import com.magnus.domain.enumeration.DependencyType;
import com.magnus.repository.TaskDependencyRepository;
import com.magnus.service.dto.TaskDependencyDTO;
import com.magnus.service.mapper.TaskDependencyMapper;
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
 * Integration tests for the {@link TaskDependencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskDependencyResourceIT {

    private static final DependencyType DEFAULT_DEPENDENCY_TYPE = DependencyType.BLOCKS;
    private static final DependencyType UPDATED_DEPENDENCY_TYPE = DependencyType.REQUIRES;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/task-dependencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskDependencyRepository taskDependencyRepository;

    @Autowired
    private TaskDependencyMapper taskDependencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskDependencyMockMvc;

    private TaskDependency taskDependency;

    private TaskDependency insertedTaskDependency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskDependency createEntity(EntityManager em) {
        TaskDependency taskDependency = new TaskDependency()
            .dependencyType(DEFAULT_DEPENDENCY_TYPE)
            .notes(DEFAULT_NOTES)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        taskDependency.setPrerequisiteTask(task);
        // Add required entity
        taskDependency.setDependentTask(task);
        return taskDependency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskDependency createUpdatedEntity(EntityManager em) {
        TaskDependency updatedTaskDependency = new TaskDependency()
            .dependencyType(UPDATED_DEPENDENCY_TYPE)
            .notes(UPDATED_NOTES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Task task;
        if (TestUtil.findAll(em, Task.class).isEmpty()) {
            task = TaskResourceIT.createUpdatedEntity(em);
            em.persist(task);
            em.flush();
        } else {
            task = TestUtil.findAll(em, Task.class).get(0);
        }
        updatedTaskDependency.setPrerequisiteTask(task);
        // Add required entity
        updatedTaskDependency.setDependentTask(task);
        return updatedTaskDependency;
    }

    @BeforeEach
    void initTest() {
        taskDependency = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTaskDependency != null) {
            taskDependencyRepository.delete(insertedTaskDependency);
            insertedTaskDependency = null;
        }
    }

    @Test
    @Transactional
    void createTaskDependency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);
        var returnedTaskDependencyDTO = om.readValue(
            restTaskDependencyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDependencyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaskDependencyDTO.class
        );

        // Validate the TaskDependency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTaskDependency = taskDependencyMapper.toEntity(returnedTaskDependencyDTO);
        assertTaskDependencyUpdatableFieldsEquals(returnedTaskDependency, getPersistedTaskDependency(returnedTaskDependency));

        insertedTaskDependency = returnedTaskDependency;
    }

    @Test
    @Transactional
    void createTaskDependencyWithExistingId() throws Exception {
        // Create the TaskDependency with an existing ID
        taskDependency.setId(1L);
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskDependencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDependencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDependencyTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taskDependency.setDependencyType(null);

        // Create the TaskDependency, which fails.
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        restTaskDependencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDependencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taskDependency.setIsActive(null);

        // Create the TaskDependency, which fails.
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        restTaskDependencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDependencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        taskDependency.setCreatedAt(null);

        // Create the TaskDependency, which fails.
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        restTaskDependencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDependencyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaskDependencies() throws Exception {
        // Initialize the database
        insertedTaskDependency = taskDependencyRepository.saveAndFlush(taskDependency);

        // Get all the taskDependencyList
        restTaskDependencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskDependency.getId().intValue())))
            .andExpect(jsonPath("$.[*].dependencyType").value(hasItem(DEFAULT_DEPENDENCY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTaskDependency() throws Exception {
        // Initialize the database
        insertedTaskDependency = taskDependencyRepository.saveAndFlush(taskDependency);

        // Get the taskDependency
        restTaskDependencyMockMvc
            .perform(get(ENTITY_API_URL_ID, taskDependency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskDependency.getId().intValue()))
            .andExpect(jsonPath("$.dependencyType").value(DEFAULT_DEPENDENCY_TYPE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTaskDependency() throws Exception {
        // Get the taskDependency
        restTaskDependencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaskDependency() throws Exception {
        // Initialize the database
        insertedTaskDependency = taskDependencyRepository.saveAndFlush(taskDependency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskDependency
        TaskDependency updatedTaskDependency = taskDependencyRepository.findById(taskDependency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTaskDependency are not directly saved in db
        em.detach(updatedTaskDependency);
        updatedTaskDependency
            .dependencyType(UPDATED_DEPENDENCY_TYPE)
            .notes(UPDATED_NOTES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT);
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(updatedTaskDependency);

        restTaskDependencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDependencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskDependencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskDependencyToMatchAllProperties(updatedTaskDependency);
    }

    @Test
    @Transactional
    void putNonExistingTaskDependency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskDependency.setId(longCount.incrementAndGet());

        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskDependencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDependencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskDependencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaskDependency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskDependency.setId(longCount.incrementAndGet());

        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskDependencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskDependencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaskDependency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskDependency.setId(longCount.incrementAndGet());

        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskDependencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDependencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskDependencyWithPatch() throws Exception {
        // Initialize the database
        insertedTaskDependency = taskDependencyRepository.saveAndFlush(taskDependency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskDependency using partial update
        TaskDependency partialUpdatedTaskDependency = new TaskDependency();
        partialUpdatedTaskDependency.setId(taskDependency.getId());

        partialUpdatedTaskDependency.notes(UPDATED_NOTES);

        restTaskDependencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskDependency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskDependency))
            )
            .andExpect(status().isOk());

        // Validate the TaskDependency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskDependencyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTaskDependency, taskDependency),
            getPersistedTaskDependency(taskDependency)
        );
    }

    @Test
    @Transactional
    void fullUpdateTaskDependencyWithPatch() throws Exception {
        // Initialize the database
        insertedTaskDependency = taskDependencyRepository.saveAndFlush(taskDependency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the taskDependency using partial update
        TaskDependency partialUpdatedTaskDependency = new TaskDependency();
        partialUpdatedTaskDependency.setId(taskDependency.getId());

        partialUpdatedTaskDependency
            .dependencyType(UPDATED_DEPENDENCY_TYPE)
            .notes(UPDATED_NOTES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT);

        restTaskDependencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaskDependency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTaskDependency))
            )
            .andExpect(status().isOk());

        // Validate the TaskDependency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskDependencyUpdatableFieldsEquals(partialUpdatedTaskDependency, getPersistedTaskDependency(partialUpdatedTaskDependency));
    }

    @Test
    @Transactional
    void patchNonExistingTaskDependency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskDependency.setId(longCount.incrementAndGet());

        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskDependencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDependencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskDependencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaskDependency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskDependency.setId(longCount.incrementAndGet());

        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskDependencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskDependencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaskDependency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        taskDependency.setId(longCount.incrementAndGet());

        // Create the TaskDependency
        TaskDependencyDTO taskDependencyDTO = taskDependencyMapper.toDto(taskDependency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskDependencyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskDependencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaskDependency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaskDependency() throws Exception {
        // Initialize the database
        insertedTaskDependency = taskDependencyRepository.saveAndFlush(taskDependency);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the taskDependency
        restTaskDependencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, taskDependency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskDependencyRepository.count();
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

    protected TaskDependency getPersistedTaskDependency(TaskDependency taskDependency) {
        return taskDependencyRepository.findById(taskDependency.getId()).orElseThrow();
    }

    protected void assertPersistedTaskDependencyToMatchAllProperties(TaskDependency expectedTaskDependency) {
        assertTaskDependencyAllPropertiesEquals(expectedTaskDependency, getPersistedTaskDependency(expectedTaskDependency));
    }

    protected void assertPersistedTaskDependencyToMatchUpdatableProperties(TaskDependency expectedTaskDependency) {
        assertTaskDependencyAllUpdatablePropertiesEquals(expectedTaskDependency, getPersistedTaskDependency(expectedTaskDependency));
    }
}
