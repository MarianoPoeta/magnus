package com.magnus.web.rest;

import static com.magnus.domain.BudgetAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.Budget;
import com.magnus.domain.Client;
import com.magnus.domain.enumeration.BudgetStatus;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.EventGender;
import com.magnus.domain.enumeration.PaymentStatus;
import com.magnus.repository.BudgetRepository;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.mapper.BudgetMapper;
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
 * Integration tests for the {@link BudgetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EVENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EVENT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_GUEST_COUNT = 1;
    private static final Integer UPDATED_GUEST_COUNT = 2;

    private static final EventGender DEFAULT_EVENT_GENDER = EventGender.MEN;
    private static final EventGender UPDATED_EVENT_GENDER = EventGender.WOMEN;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PROFIT_MARGIN = new BigDecimal(0);
    private static final BigDecimal UPDATED_PROFIT_MARGIN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MEALS_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MEALS_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ACTIVITIES_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACTIVITIES_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TRANSPORT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TRANSPORT_AMOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ACCOMMODATION_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACCOMMODATION_AMOUNT = new BigDecimal(1);

    private static final BudgetStatus DEFAULT_STATUS = BudgetStatus.DRAFT;
    private static final BudgetStatus UPDATED_STATUS = BudgetStatus.PENDING;

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.UNPAID;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.PARTIALLY_PAID;

    private static final Boolean DEFAULT_IS_CLOSED = false;
    private static final Boolean UPDATED_IS_CLOSED = true;

    private static final String DEFAULT_INTERNAL_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WORKFLOW_TRIGGERED = false;
    private static final Boolean UPDATED_WORKFLOW_TRIGGERED = true;

    private static final Instant DEFAULT_LAST_WORKFLOW_EXECUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_WORKFLOW_EXECUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final ConflictStatus DEFAULT_CONFLICT_STATUS = ConflictStatus.NONE;
    private static final ConflictStatus UPDATED_CONFLICT_STATUS = ConflictStatus.DETECTED;

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_APPROVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESERVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESERVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/budgets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetMockMvc;

    private Budget budget;

    private Budget insertedBudget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createEntity(EntityManager em) {
        Budget budget = new Budget()
            .name(DEFAULT_NAME)
            .clientName(DEFAULT_CLIENT_NAME)
            .eventDate(DEFAULT_EVENT_DATE)
            .eventLocation(DEFAULT_EVENT_LOCATION)
            .guestCount(DEFAULT_GUEST_COUNT)
            .eventGender(DEFAULT_EVENT_GENDER)
            .description(DEFAULT_DESCRIPTION)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .totalCost(DEFAULT_TOTAL_COST)
            .profitMargin(DEFAULT_PROFIT_MARGIN)
            .mealsAmount(DEFAULT_MEALS_AMOUNT)
            .activitiesAmount(DEFAULT_ACTIVITIES_AMOUNT)
            .transportAmount(DEFAULT_TRANSPORT_AMOUNT)
            .accommodationAmount(DEFAULT_ACCOMMODATION_AMOUNT)
            .status(DEFAULT_STATUS)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .isClosed(DEFAULT_IS_CLOSED)
            .internalNotes(DEFAULT_INTERNAL_NOTES)
            .clientNotes(DEFAULT_CLIENT_NOTES)
            .templateId(DEFAULT_TEMPLATE_ID)
            .workflowTriggered(DEFAULT_WORKFLOW_TRIGGERED)
            .lastWorkflowExecution(DEFAULT_LAST_WORKFLOW_EXECUTION)
            .version(DEFAULT_VERSION)
            .conflictStatus(DEFAULT_CONFLICT_STATUS)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .approvedAt(DEFAULT_APPROVED_AT)
            .reservedAt(DEFAULT_RESERVED_AT)
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
        budget.setCreatedBy(appUser);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        budget.setClient(client);
        return budget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createUpdatedEntity(EntityManager em) {
        Budget updatedBudget = new Budget()
            .name(UPDATED_NAME)
            .clientName(UPDATED_CLIENT_NAME)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .guestCount(UPDATED_GUEST_COUNT)
            .eventGender(UPDATED_EVENT_GENDER)
            .description(UPDATED_DESCRIPTION)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .profitMargin(UPDATED_PROFIT_MARGIN)
            .mealsAmount(UPDATED_MEALS_AMOUNT)
            .activitiesAmount(UPDATED_ACTIVITIES_AMOUNT)
            .transportAmount(UPDATED_TRANSPORT_AMOUNT)
            .accommodationAmount(UPDATED_ACCOMMODATION_AMOUNT)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .isClosed(UPDATED_IS_CLOSED)
            .internalNotes(UPDATED_INTERNAL_NOTES)
            .clientNotes(UPDATED_CLIENT_NOTES)
            .templateId(UPDATED_TEMPLATE_ID)
            .workflowTriggered(UPDATED_WORKFLOW_TRIGGERED)
            .lastWorkflowExecution(UPDATED_LAST_WORKFLOW_EXECUTION)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .approvedAt(UPDATED_APPROVED_AT)
            .reservedAt(UPDATED_RESERVED_AT)
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
        updatedBudget.setCreatedBy(appUser);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        updatedBudget.setClient(client);
        return updatedBudget;
    }

    @BeforeEach
    void initTest() {
        budget = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBudget != null) {
            budgetRepository.delete(insertedBudget);
            insertedBudget = null;
        }
    }

    @Test
    @Transactional
    void createBudget() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);
        var returnedBudgetDTO = om.readValue(
            restBudgetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetDTO.class
        );

        // Validate the Budget in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudget = budgetMapper.toEntity(returnedBudgetDTO);
        assertBudgetUpdatableFieldsEquals(returnedBudget, getPersistedBudget(returnedBudget));

        insertedBudget = returnedBudget;
    }

    @Test
    @Transactional
    void createBudgetWithExistingId() throws Exception {
        // Create the Budget with an existing ID
        budget.setId(1L);
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setName(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClientNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setClientName(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setEventDate(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGuestCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setGuestCount(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventGenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setEventGender(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setTotalAmount(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setStatus(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setPaymentStatus(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsClosedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setIsClosed(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkflowTriggeredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setWorkflowTriggered(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setVersion(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConflictStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setConflictStatus(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setCreatedAt(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budget.setUpdatedAt(null);

        // Create the Budget, which fails.
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        restBudgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBudgets() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get all the budgetList
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].eventLocation").value(hasItem(DEFAULT_EVENT_LOCATION)))
            .andExpect(jsonPath("$.[*].guestCount").value(hasItem(DEFAULT_GUEST_COUNT)))
            .andExpect(jsonPath("$.[*].eventGender").value(hasItem(DEFAULT_EVENT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].profitMargin").value(hasItem(sameNumber(DEFAULT_PROFIT_MARGIN))))
            .andExpect(jsonPath("$.[*].mealsAmount").value(hasItem(sameNumber(DEFAULT_MEALS_AMOUNT))))
            .andExpect(jsonPath("$.[*].activitiesAmount").value(hasItem(sameNumber(DEFAULT_ACTIVITIES_AMOUNT))))
            .andExpect(jsonPath("$.[*].transportAmount").value(hasItem(sameNumber(DEFAULT_TRANSPORT_AMOUNT))))
            .andExpect(jsonPath("$.[*].accommodationAmount").value(hasItem(sameNumber(DEFAULT_ACCOMMODATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isClosed").value(hasItem(DEFAULT_IS_CLOSED)))
            .andExpect(jsonPath("$.[*].internalNotes").value(hasItem(DEFAULT_INTERNAL_NOTES)))
            .andExpect(jsonPath("$.[*].clientNotes").value(hasItem(DEFAULT_CLIENT_NOTES)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].workflowTriggered").value(hasItem(DEFAULT_WORKFLOW_TRIGGERED)))
            .andExpect(jsonPath("$.[*].lastWorkflowExecution").value(hasItem(DEFAULT_LAST_WORKFLOW_EXECUTION.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].conflictStatus").value(hasItem(DEFAULT_CONFLICT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(DEFAULT_APPROVED_AT.toString())))
            .andExpect(jsonPath("$.[*].reservedAt").value(hasItem(DEFAULT_RESERVED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBudget() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc
            .perform(get(ENTITY_API_URL_ID, budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.eventLocation").value(DEFAULT_EVENT_LOCATION))
            .andExpect(jsonPath("$.guestCount").value(DEFAULT_GUEST_COUNT))
            .andExpect(jsonPath("$.eventGender").value(DEFAULT_EVENT_GENDER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.profitMargin").value(sameNumber(DEFAULT_PROFIT_MARGIN)))
            .andExpect(jsonPath("$.mealsAmount").value(sameNumber(DEFAULT_MEALS_AMOUNT)))
            .andExpect(jsonPath("$.activitiesAmount").value(sameNumber(DEFAULT_ACTIVITIES_AMOUNT)))
            .andExpect(jsonPath("$.transportAmount").value(sameNumber(DEFAULT_TRANSPORT_AMOUNT)))
            .andExpect(jsonPath("$.accommodationAmount").value(sameNumber(DEFAULT_ACCOMMODATION_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.isClosed").value(DEFAULT_IS_CLOSED))
            .andExpect(jsonPath("$.internalNotes").value(DEFAULT_INTERNAL_NOTES))
            .andExpect(jsonPath("$.clientNotes").value(DEFAULT_CLIENT_NOTES))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID))
            .andExpect(jsonPath("$.workflowTriggered").value(DEFAULT_WORKFLOW_TRIGGERED))
            .andExpect(jsonPath("$.lastWorkflowExecution").value(DEFAULT_LAST_WORKFLOW_EXECUTION.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.conflictStatus").value(DEFAULT_CONFLICT_STATUS.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.approvedAt").value(DEFAULT_APPROVED_AT.toString()))
            .andExpect(jsonPath("$.reservedAt").value(DEFAULT_RESERVED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudget() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budget
        Budget updatedBudget = budgetRepository.findById(budget.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudget are not directly saved in db
        em.detach(updatedBudget);
        updatedBudget
            .name(UPDATED_NAME)
            .clientName(UPDATED_CLIENT_NAME)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .guestCount(UPDATED_GUEST_COUNT)
            .eventGender(UPDATED_EVENT_GENDER)
            .description(UPDATED_DESCRIPTION)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .profitMargin(UPDATED_PROFIT_MARGIN)
            .mealsAmount(UPDATED_MEALS_AMOUNT)
            .activitiesAmount(UPDATED_ACTIVITIES_AMOUNT)
            .transportAmount(UPDATED_TRANSPORT_AMOUNT)
            .accommodationAmount(UPDATED_ACCOMMODATION_AMOUNT)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .isClosed(UPDATED_IS_CLOSED)
            .internalNotes(UPDATED_INTERNAL_NOTES)
            .clientNotes(UPDATED_CLIENT_NOTES)
            .templateId(UPDATED_TEMPLATE_ID)
            .workflowTriggered(UPDATED_WORKFLOW_TRIGGERED)
            .lastWorkflowExecution(UPDATED_LAST_WORKFLOW_EXECUTION)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .approvedAt(UPDATED_APPROVED_AT)
            .reservedAt(UPDATED_RESERVED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BudgetDTO budgetDTO = budgetMapper.toDto(updatedBudget);

        restBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetToMatchAllProperties(updatedBudget);
    }

    @Test
    @Transactional
    void putNonExistingBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBudgetWithPatch() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budget using partial update
        Budget partialUpdatedBudget = new Budget();
        partialUpdatedBudget.setId(budget.getId());

        partialUpdatedBudget
            .name(UPDATED_NAME)
            .eventDate(UPDATED_EVENT_DATE)
            .guestCount(UPDATED_GUEST_COUNT)
            .eventGender(UPDATED_EVENT_GENDER)
            .description(UPDATED_DESCRIPTION)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .profitMargin(UPDATED_PROFIT_MARGIN)
            .mealsAmount(UPDATED_MEALS_AMOUNT)
            .transportAmount(UPDATED_TRANSPORT_AMOUNT)
            .accommodationAmount(UPDATED_ACCOMMODATION_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .isClosed(UPDATED_IS_CLOSED)
            .templateId(UPDATED_TEMPLATE_ID)
            .lastWorkflowExecution(UPDATED_LAST_WORKFLOW_EXECUTION)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .approvedAt(UPDATED_APPROVED_AT)
            .createdAt(UPDATED_CREATED_AT);

        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudget.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudget))
            )
            .andExpect(status().isOk());

        // Validate the Budget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBudget, budget), getPersistedBudget(budget));
    }

    @Test
    @Transactional
    void fullUpdateBudgetWithPatch() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budget using partial update
        Budget partialUpdatedBudget = new Budget();
        partialUpdatedBudget.setId(budget.getId());

        partialUpdatedBudget
            .name(UPDATED_NAME)
            .clientName(UPDATED_CLIENT_NAME)
            .eventDate(UPDATED_EVENT_DATE)
            .eventLocation(UPDATED_EVENT_LOCATION)
            .guestCount(UPDATED_GUEST_COUNT)
            .eventGender(UPDATED_EVENT_GENDER)
            .description(UPDATED_DESCRIPTION)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .profitMargin(UPDATED_PROFIT_MARGIN)
            .mealsAmount(UPDATED_MEALS_AMOUNT)
            .activitiesAmount(UPDATED_ACTIVITIES_AMOUNT)
            .transportAmount(UPDATED_TRANSPORT_AMOUNT)
            .accommodationAmount(UPDATED_ACCOMMODATION_AMOUNT)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .isClosed(UPDATED_IS_CLOSED)
            .internalNotes(UPDATED_INTERNAL_NOTES)
            .clientNotes(UPDATED_CLIENT_NOTES)
            .templateId(UPDATED_TEMPLATE_ID)
            .workflowTriggered(UPDATED_WORKFLOW_TRIGGERED)
            .lastWorkflowExecution(UPDATED_LAST_WORKFLOW_EXECUTION)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .approvedAt(UPDATED_APPROVED_AT)
            .reservedAt(UPDATED_RESERVED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudget.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudget))
            )
            .andExpect(status().isOk());

        // Validate the Budget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetUpdatableFieldsEquals(partialUpdatedBudget, getPersistedBudget(partialUpdatedBudget));
    }

    @Test
    @Transactional
    void patchNonExistingBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budget.setId(longCount.incrementAndGet());

        // Create the Budget
        BudgetDTO budgetDTO = budgetMapper.toDto(budget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(budgetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Budget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBudget() throws Exception {
        // Initialize the database
        insertedBudget = budgetRepository.saveAndFlush(budget);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the budget
        restBudgetMockMvc
            .perform(delete(ENTITY_API_URL_ID, budget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return budgetRepository.count();
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

    protected Budget getPersistedBudget(Budget budget) {
        return budgetRepository.findById(budget.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetToMatchAllProperties(Budget expectedBudget) {
        assertBudgetAllPropertiesEquals(expectedBudget, getPersistedBudget(expectedBudget));
    }

    protected void assertPersistedBudgetToMatchUpdatableProperties(Budget expectedBudget) {
        assertBudgetAllUpdatablePropertiesEquals(expectedBudget, getPersistedBudget(expectedBudget));
    }
}
