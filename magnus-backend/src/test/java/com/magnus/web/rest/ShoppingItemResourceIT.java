package com.magnus.web.rest;

import static com.magnus.domain.ShoppingItemAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.ShoppingItem;
import com.magnus.domain.WeeklyPlan;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.ProductCategory;
import com.magnus.domain.enumeration.ProductUnit;
import com.magnus.repository.ShoppingItemRepository;
import com.magnus.service.dto.ShoppingItemDTO;
import com.magnus.service.mapper.ShoppingItemMapper;
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
 * Integration tests for the {@link ShoppingItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShoppingItemResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_QUANTITY = 0D;
    private static final Double UPDATED_TOTAL_QUANTITY = 1D;

    private static final ProductUnit DEFAULT_UNIT = ProductUnit.KG;
    private static final ProductUnit UPDATED_UNIT = ProductUnit.G;

    private static final ProductCategory DEFAULT_CATEGORY = ProductCategory.MEAT;
    private static final ProductCategory UPDATED_CATEGORY = ProductCategory.VEGETABLES;

    private static final String DEFAULT_BUDGET_IDS = "AAAAAAAAAA";
    private static final String UPDATED_BUDGET_IDS = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAMES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PURCHASED = false;
    private static final Boolean UPDATED_IS_PURCHASED = true;

    private static final Double DEFAULT_PURCHASED_QUANTITY = 0D;
    private static final Double UPDATED_PURCHASED_QUANTITY = 1D;

    private static final LocalDate DEFAULT_WEEK_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WEEK_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_WEEK_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WEEK_END = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPLIER_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_CONTACT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ESTIMATED_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ESTIMATED_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ACTUAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACTUAL_COST = new BigDecimal(1);

    private static final LocalDate DEFAULT_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_CONSOLIDATED = false;
    private static final Boolean UPDATED_IS_CONSOLIDATED = true;

    private static final Instant DEFAULT_CONSOLIDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSOLIDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PURCHASED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PURCHASED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ConflictStatus DEFAULT_CONFLICT_STATUS = ConflictStatus.NONE;
    private static final ConflictStatus UPDATED_CONFLICT_STATUS = ConflictStatus.DETECTED;

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shopping-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private ShoppingItemMapper shoppingItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingItemMockMvc;

    private ShoppingItem shoppingItem;

    private ShoppingItem insertedShoppingItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingItem createEntity(EntityManager em) {
        ShoppingItem shoppingItem = new ShoppingItem()
            .productName(DEFAULT_PRODUCT_NAME)
            .totalQuantity(DEFAULT_TOTAL_QUANTITY)
            .unit(DEFAULT_UNIT)
            .category(DEFAULT_CATEGORY)
            .budgetIds(DEFAULT_BUDGET_IDS)
            .clientNames(DEFAULT_CLIENT_NAMES)
            .isPurchased(DEFAULT_IS_PURCHASED)
            .purchasedQuantity(DEFAULT_PURCHASED_QUANTITY)
            .weekStart(DEFAULT_WEEK_START)
            .weekEnd(DEFAULT_WEEK_END)
            .notes(DEFAULT_NOTES)
            .supplier(DEFAULT_SUPPLIER)
            .supplierContact(DEFAULT_SUPPLIER_CONTACT)
            .estimatedCost(DEFAULT_ESTIMATED_COST)
            .actualCost(DEFAULT_ACTUAL_COST)
            .deliveryDate(DEFAULT_DELIVERY_DATE)
            .isConsolidated(DEFAULT_IS_CONSOLIDATED)
            .consolidatedAt(DEFAULT_CONSOLIDATED_AT)
            .purchasedAt(DEFAULT_PURCHASED_AT)
            .conflictStatus(DEFAULT_CONFLICT_STATUS)
            .version(DEFAULT_VERSION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        WeeklyPlan weeklyPlan;
        if (TestUtil.findAll(em, WeeklyPlan.class).isEmpty()) {
            weeklyPlan = WeeklyPlanResourceIT.createEntity(em);
            em.persist(weeklyPlan);
            em.flush();
        } else {
            weeklyPlan = TestUtil.findAll(em, WeeklyPlan.class).get(0);
        }
        shoppingItem.setWeeklyPlan(weeklyPlan);
        return shoppingItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingItem createUpdatedEntity(EntityManager em) {
        ShoppingItem updatedShoppingItem = new ShoppingItem()
            .productName(UPDATED_PRODUCT_NAME)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .unit(UPDATED_UNIT)
            .category(UPDATED_CATEGORY)
            .budgetIds(UPDATED_BUDGET_IDS)
            .clientNames(UPDATED_CLIENT_NAMES)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedQuantity(UPDATED_PURCHASED_QUANTITY)
            .weekStart(UPDATED_WEEK_START)
            .weekEnd(UPDATED_WEEK_END)
            .notes(UPDATED_NOTES)
            .supplier(UPDATED_SUPPLIER)
            .supplierContact(UPDATED_SUPPLIER_CONTACT)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .isConsolidated(UPDATED_IS_CONSOLIDATED)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        WeeklyPlan weeklyPlan;
        if (TestUtil.findAll(em, WeeklyPlan.class).isEmpty()) {
            weeklyPlan = WeeklyPlanResourceIT.createUpdatedEntity(em);
            em.persist(weeklyPlan);
            em.flush();
        } else {
            weeklyPlan = TestUtil.findAll(em, WeeklyPlan.class).get(0);
        }
        updatedShoppingItem.setWeeklyPlan(weeklyPlan);
        return updatedShoppingItem;
    }

    @BeforeEach
    void initTest() {
        shoppingItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedShoppingItem != null) {
            shoppingItemRepository.delete(insertedShoppingItem);
            insertedShoppingItem = null;
        }
    }

    @Test
    @Transactional
    void createShoppingItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);
        var returnedShoppingItemDTO = om.readValue(
            restShoppingItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShoppingItemDTO.class
        );

        // Validate the ShoppingItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShoppingItem = shoppingItemMapper.toEntity(returnedShoppingItemDTO);
        assertShoppingItemUpdatableFieldsEquals(returnedShoppingItem, getPersistedShoppingItem(returnedShoppingItem));

        insertedShoppingItem = returnedShoppingItem;
    }

    @Test
    @Transactional
    void createShoppingItemWithExistingId() throws Exception {
        // Create the ShoppingItem with an existing ID
        shoppingItem.setId(1L);
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setProductName(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setTotalQuantity(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setUnit(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setCategory(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPurchasedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setIsPurchased(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeekStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setWeekStart(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeekEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setWeekEnd(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsConsolidatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setIsConsolidated(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConflictStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setConflictStatus(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setVersion(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setCreatedAt(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingItem.setUpdatedAt(null);

        // Create the ShoppingItem, which fails.
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        restShoppingItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShoppingItems() throws Exception {
        // Initialize the database
        insertedShoppingItem = shoppingItemRepository.saveAndFlush(shoppingItem);

        // Get all the shoppingItemList
        restShoppingItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].budgetIds").value(hasItem(DEFAULT_BUDGET_IDS)))
            .andExpect(jsonPath("$.[*].clientNames").value(hasItem(DEFAULT_CLIENT_NAMES)))
            .andExpect(jsonPath("$.[*].isPurchased").value(hasItem(DEFAULT_IS_PURCHASED)))
            .andExpect(jsonPath("$.[*].purchasedQuantity").value(hasItem(DEFAULT_PURCHASED_QUANTITY)))
            .andExpect(jsonPath("$.[*].weekStart").value(hasItem(DEFAULT_WEEK_START.toString())))
            .andExpect(jsonPath("$.[*].weekEnd").value(hasItem(DEFAULT_WEEK_END.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].supplierContact").value(hasItem(DEFAULT_SUPPLIER_CONTACT)))
            .andExpect(jsonPath("$.[*].estimatedCost").value(hasItem(sameNumber(DEFAULT_ESTIMATED_COST))))
            .andExpect(jsonPath("$.[*].actualCost").value(hasItem(sameNumber(DEFAULT_ACTUAL_COST))))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].isConsolidated").value(hasItem(DEFAULT_IS_CONSOLIDATED)))
            .andExpect(jsonPath("$.[*].consolidatedAt").value(hasItem(DEFAULT_CONSOLIDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].purchasedAt").value(hasItem(DEFAULT_PURCHASED_AT.toString())))
            .andExpect(jsonPath("$.[*].conflictStatus").value(hasItem(DEFAULT_CONFLICT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getShoppingItem() throws Exception {
        // Initialize the database
        insertedShoppingItem = shoppingItemRepository.saveAndFlush(shoppingItem);

        // Get the shoppingItem
        restShoppingItemMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingItem.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.totalQuantity").value(DEFAULT_TOTAL_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.budgetIds").value(DEFAULT_BUDGET_IDS))
            .andExpect(jsonPath("$.clientNames").value(DEFAULT_CLIENT_NAMES))
            .andExpect(jsonPath("$.isPurchased").value(DEFAULT_IS_PURCHASED))
            .andExpect(jsonPath("$.purchasedQuantity").value(DEFAULT_PURCHASED_QUANTITY))
            .andExpect(jsonPath("$.weekStart").value(DEFAULT_WEEK_START.toString()))
            .andExpect(jsonPath("$.weekEnd").value(DEFAULT_WEEK_END.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER))
            .andExpect(jsonPath("$.supplierContact").value(DEFAULT_SUPPLIER_CONTACT))
            .andExpect(jsonPath("$.estimatedCost").value(sameNumber(DEFAULT_ESTIMATED_COST)))
            .andExpect(jsonPath("$.actualCost").value(sameNumber(DEFAULT_ACTUAL_COST)))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.isConsolidated").value(DEFAULT_IS_CONSOLIDATED))
            .andExpect(jsonPath("$.consolidatedAt").value(DEFAULT_CONSOLIDATED_AT.toString()))
            .andExpect(jsonPath("$.purchasedAt").value(DEFAULT_PURCHASED_AT.toString()))
            .andExpect(jsonPath("$.conflictStatus").value(DEFAULT_CONFLICT_STATUS.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShoppingItem() throws Exception {
        // Get the shoppingItem
        restShoppingItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShoppingItem() throws Exception {
        // Initialize the database
        insertedShoppingItem = shoppingItemRepository.saveAndFlush(shoppingItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingItem
        ShoppingItem updatedShoppingItem = shoppingItemRepository.findById(shoppingItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShoppingItem are not directly saved in db
        em.detach(updatedShoppingItem);
        updatedShoppingItem
            .productName(UPDATED_PRODUCT_NAME)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .unit(UPDATED_UNIT)
            .category(UPDATED_CATEGORY)
            .budgetIds(UPDATED_BUDGET_IDS)
            .clientNames(UPDATED_CLIENT_NAMES)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedQuantity(UPDATED_PURCHASED_QUANTITY)
            .weekStart(UPDATED_WEEK_START)
            .weekEnd(UPDATED_WEEK_END)
            .notes(UPDATED_NOTES)
            .supplier(UPDATED_SUPPLIER)
            .supplierContact(UPDATED_SUPPLIER_CONTACT)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .isConsolidated(UPDATED_IS_CONSOLIDATED)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(updatedShoppingItem);

        restShoppingItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShoppingItemToMatchAllProperties(updatedShoppingItem);
    }

    @Test
    @Transactional
    void putNonExistingShoppingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingItem.setId(longCount.incrementAndGet());

        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingItem.setId(longCount.incrementAndGet());

        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingItem.setId(longCount.incrementAndGet());

        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingItemWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingItem = shoppingItemRepository.saveAndFlush(shoppingItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingItem using partial update
        ShoppingItem partialUpdatedShoppingItem = new ShoppingItem();
        partialUpdatedShoppingItem.setId(shoppingItem.getId());

        partialUpdatedShoppingItem
            .productName(UPDATED_PRODUCT_NAME)
            .unit(UPDATED_UNIT)
            .isPurchased(UPDATED_IS_PURCHASED)
            .weekStart(UPDATED_WEEK_START)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT);

        restShoppingItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingItem))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShoppingItem, shoppingItem),
            getPersistedShoppingItem(shoppingItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateShoppingItemWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingItem = shoppingItemRepository.saveAndFlush(shoppingItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingItem using partial update
        ShoppingItem partialUpdatedShoppingItem = new ShoppingItem();
        partialUpdatedShoppingItem.setId(shoppingItem.getId());

        partialUpdatedShoppingItem
            .productName(UPDATED_PRODUCT_NAME)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .unit(UPDATED_UNIT)
            .category(UPDATED_CATEGORY)
            .budgetIds(UPDATED_BUDGET_IDS)
            .clientNames(UPDATED_CLIENT_NAMES)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedQuantity(UPDATED_PURCHASED_QUANTITY)
            .weekStart(UPDATED_WEEK_START)
            .weekEnd(UPDATED_WEEK_END)
            .notes(UPDATED_NOTES)
            .supplier(UPDATED_SUPPLIER)
            .supplierContact(UPDATED_SUPPLIER_CONTACT)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .deliveryDate(UPDATED_DELIVERY_DATE)
            .isConsolidated(UPDATED_IS_CONSOLIDATED)
            .consolidatedAt(UPDATED_CONSOLIDATED_AT)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restShoppingItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingItem))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingItemUpdatableFieldsEquals(partialUpdatedShoppingItem, getPersistedShoppingItem(partialUpdatedShoppingItem));
    }

    @Test
    @Transactional
    void patchNonExistingShoppingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingItem.setId(longCount.incrementAndGet());

        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingItem.setId(longCount.incrementAndGet());

        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingItem.setId(longCount.incrementAndGet());

        // Create the ShoppingItem
        ShoppingItemDTO shoppingItemDTO = shoppingItemMapper.toDto(shoppingItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shoppingItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingItem() throws Exception {
        // Initialize the database
        insertedShoppingItem = shoppingItemRepository.saveAndFlush(shoppingItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shoppingItem
        restShoppingItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shoppingItemRepository.count();
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

    protected ShoppingItem getPersistedShoppingItem(ShoppingItem shoppingItem) {
        return shoppingItemRepository.findById(shoppingItem.getId()).orElseThrow();
    }

    protected void assertPersistedShoppingItemToMatchAllProperties(ShoppingItem expectedShoppingItem) {
        assertShoppingItemAllPropertiesEquals(expectedShoppingItem, getPersistedShoppingItem(expectedShoppingItem));
    }

    protected void assertPersistedShoppingItemToMatchUpdatableProperties(ShoppingItem expectedShoppingItem) {
        assertShoppingItemAllUpdatablePropertiesEquals(expectedShoppingItem, getPersistedShoppingItem(expectedShoppingItem));
    }
}
