package com.magnus.web.rest;

import static com.magnus.domain.TaskAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.Budget;
import com.magnus.domain.Task;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.TaskStatus;
import com.magnus.domain.enumeration.TaskType;
import com.magnus.domain.enumeration.UserRole;
import com.magnus.repository.TaskRepository;
import com.magnus.service.dto.TaskDTO;
import com.magnus.service.mapper.TaskMapper;
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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TaskType DEFAULT_TYPE = TaskType.SHOPPING;
    private static final TaskType UPDATED_TYPE = TaskType.RESERVATION;

    private static final TaskPriority DEFAULT_PRIORITY = TaskPriority.LOW;
    private static final TaskPriority UPDATED_PRIORITY = TaskPriority.MEDIUM;

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.TODO;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.IN_PROGRESS;

    private static final UserRole DEFAULT_ASSIGNED_TO_ROLE = UserRole.ADMIN;
    private static final UserRole UPDATED_ASSIGNED_TO_ROLE = UserRole.SALES;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalTime DEFAULT_DUE_TIME = LocalTime.NOON;
    private static final LocalTime UPDATED_DUE_TIME = LocalTime.MAX.withNano(0);

    private static final Integer DEFAULT_ESTIMATED_DURATION = 0;
    private static final Integer UPDATED_ESTIMATED_DURATION = 1;

    private static final Integer DEFAULT_ACTUAL_DURATION = 0;
    private static final Integer UPDATED_ACTUAL_DURATION = 1;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_INVOICE_URL = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AUTO_SCHEDULED = false;
    private static final Boolean UPDATED_AUTO_SCHEDULED = true;

    private static final Boolean DEFAULT_IS_RECURRING = false;
    private static final Boolean UPDATED_IS_RECURRING = true;

    private static final String DEFAULT_PARENT_TASK_ID = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_TASK_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    private Task insertedTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .priority(DEFAULT_PRIORITY)
            .status(DEFAULT_STATUS)
            .assignedToRole(DEFAULT_ASSIGNED_TO_ROLE)
            .dueDate(DEFAULT_DUE_DATE)
            .dueTime(DEFAULT_DUE_TIME)
            .estimatedDuration(DEFAULT_ESTIMATED_DURATION)
            .actualDuration(DEFAULT_ACTUAL_DURATION)
            .location(DEFAULT_LOCATION)
            .requirements(DEFAULT_REQUIREMENTS)
            .notes(DEFAULT_NOTES)
            .invoiceUrl(DEFAULT_INVOICE_URL)
            .autoScheduled(DEFAULT_AUTO_SCHEDULED)
            .isRecurring(DEFAULT_IS_RECURRING)
            .parentTaskId(DEFAULT_PARENT_TASK_ID)
            .completedAt(DEFAULT_COMPLETED_AT)
            .startedAt(DEFAULT_STARTED_AT)
            .version(DEFAULT_VERSION)
            .conflictStatus(DEFAULT_CONFLICT_STATUS)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
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
        task.setCreatedBy(appUser);
        // Add required entity
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            budget = BudgetResourceIT.createEntity(em);
            em.persist(budget);
            em.flush();
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        task.setRelatedBudget(budget);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task updatedTask = new Task()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS)
            .assignedToRole(UPDATED_ASSIGNED_TO_ROLE)
            .dueDate(UPDATED_DUE_DATE)
            .dueTime(UPDATED_DUE_TIME)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .location(UPDATED_LOCATION)
            .requirements(UPDATED_REQUIREMENTS)
            .notes(UPDATED_NOTES)
            .invoiceUrl(UPDATED_INVOICE_URL)
            .autoScheduled(UPDATED_AUTO_SCHEDULED)
            .isRecurring(UPDATED_IS_RECURRING)
            .parentTaskId(UPDATED_PARENT_TASK_ID)
            .completedAt(UPDATED_COMPLETED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
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
        updatedTask.setCreatedBy(appUser);
        // Add required entity
        Budget budget;
        if (TestUtil.findAll(em, Budget.class).isEmpty()) {
            budget = BudgetResourceIT.createUpdatedEntity(em);
            em.persist(budget);
            em.flush();
        } else {
            budget = TestUtil.findAll(em, Budget.class).get(0);
        }
        updatedTask.setRelatedBudget(budget);
        return updatedTask;
    }

    @BeforeEach
    void initTest() {
        task = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTask != null) {
            taskRepository.delete(insertedTask);
            insertedTask = null;
        }
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        var returnedTaskDTO = om.readValue(
            restTaskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TaskDTO.class
        );

        // Validate the Task in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTask = taskMapper.toEntity(returnedTaskDTO);
        assertTaskUpdatableFieldsEquals(returnedTask, getPersistedTask(returnedTask));

        insertedTask = returnedTask;
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setTitle(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setType(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setPriority(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setStatus(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssignedToRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setAssignedToRole(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setDueDate(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAutoScheduledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setAutoScheduled(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsRecurringIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setIsRecurring(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setVersion(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConflictStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setConflictStatus(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setCreatedAt(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        task.setUpdatedAt(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].assignedToRole").value(hasItem(DEFAULT_ASSIGNED_TO_ROLE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueTime").value(hasItem(DEFAULT_DUE_TIME.toString())))
            .andExpect(jsonPath("$.[*].estimatedDuration").value(hasItem(DEFAULT_ESTIMATED_DURATION)))
            .andExpect(jsonPath("$.[*].actualDuration").value(hasItem(DEFAULT_ACTUAL_DURATION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].invoiceUrl").value(hasItem(DEFAULT_INVOICE_URL)))
            .andExpect(jsonPath("$.[*].autoScheduled").value(hasItem(DEFAULT_AUTO_SCHEDULED)))
            .andExpect(jsonPath("$.[*].isRecurring").value(hasItem(DEFAULT_IS_RECURRING)))
            .andExpect(jsonPath("$.[*].parentTaskId").value(hasItem(DEFAULT_PARENT_TASK_ID)))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].conflictStatus").value(hasItem(DEFAULT_CONFLICT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.assignedToRole").value(DEFAULT_ASSIGNED_TO_ROLE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.dueTime").value(DEFAULT_DUE_TIME.toString()))
            .andExpect(jsonPath("$.estimatedDuration").value(DEFAULT_ESTIMATED_DURATION))
            .andExpect(jsonPath("$.actualDuration").value(DEFAULT_ACTUAL_DURATION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.invoiceUrl").value(DEFAULT_INVOICE_URL))
            .andExpect(jsonPath("$.autoScheduled").value(DEFAULT_AUTO_SCHEDULED))
            .andExpect(jsonPath("$.isRecurring").value(DEFAULT_IS_RECURRING))
            .andExpect(jsonPath("$.parentTaskId").value(DEFAULT_PARENT_TASK_ID))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.conflictStatus").value(DEFAULT_CONFLICT_STATUS.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS)
            .assignedToRole(UPDATED_ASSIGNED_TO_ROLE)
            .dueDate(UPDATED_DUE_DATE)
            .dueTime(UPDATED_DUE_TIME)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .location(UPDATED_LOCATION)
            .requirements(UPDATED_REQUIREMENTS)
            .notes(UPDATED_NOTES)
            .invoiceUrl(UPDATED_INVOICE_URL)
            .autoScheduled(UPDATED_AUTO_SCHEDULED)
            .isRecurring(UPDATED_IS_RECURRING)
            .parentTaskId(UPDATED_PARENT_TASK_ID)
            .completedAt(UPDATED_COMPLETED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc
            .perform(put(ENTITY_API_URL_ID, taskDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTaskToMatchAllProperties(updatedTask);
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL_ID, taskDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .assignedToRole(UPDATED_ASSIGNED_TO_ROLE)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .invoiceUrl(UPDATED_INVOICE_URL)
            .parentTaskId(UPDATED_PARENT_TASK_ID)
            .completedAt(UPDATED_COMPLETED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTask, task), getPersistedTask(task));
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .status(UPDATED_STATUS)
            .assignedToRole(UPDATED_ASSIGNED_TO_ROLE)
            .dueDate(UPDATED_DUE_DATE)
            .dueTime(UPDATED_DUE_TIME)
            .estimatedDuration(UPDATED_ESTIMATED_DURATION)
            .actualDuration(UPDATED_ACTUAL_DURATION)
            .location(UPDATED_LOCATION)
            .requirements(UPDATED_REQUIREMENTS)
            .notes(UPDATED_NOTES)
            .invoiceUrl(UPDATED_INVOICE_URL)
            .autoScheduled(UPDATED_AUTO_SCHEDULED)
            .isRecurring(UPDATED_IS_RECURRING)
            .parentTaskId(UPDATED_PARENT_TASK_ID)
            .completedAt(UPDATED_COMPLETED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTaskUpdatableFieldsEquals(partialUpdatedTask, getPersistedTask(partialUpdatedTask));
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        task.setId(longCount.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        insertedTask = taskRepository.saveAndFlush(task);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return taskRepository.count();
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

    protected Task getPersistedTask(Task task) {
        return taskRepository.findById(task.getId()).orElseThrow();
    }

    protected void assertPersistedTaskToMatchAllProperties(Task expectedTask) {
        assertTaskAllPropertiesEquals(expectedTask, getPersistedTask(expectedTask));
    }

    protected void assertPersistedTaskToMatchUpdatableProperties(Task expectedTask) {
        assertTaskAllUpdatablePropertiesEquals(expectedTask, getPersistedTask(expectedTask));
    }
}
