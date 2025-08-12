package com.magnus.web.rest;

import static com.magnus.domain.BudgetItemAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Budget;
import com.magnus.domain.BudgetItem;
import com.magnus.repository.BudgetItemRepository;
import com.magnus.service.dto.BudgetItemDTO;
import com.magnus.service.mapper.BudgetItemMapper;
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
 * Integration tests for the {@link BudgetItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetItemResourceIT {

    private static final String DEFAULT_ITEM_TYPE = "transport";
    private static final String UPDATED_ITEM_TYPE = "menu";

    private static final String DEFAULT_TEMPLATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(1);

    private static final String DEFAULT_CUSTOMIZATIONS = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMIZATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CUSTOMIZED = false;
    private static final Boolean UPDATED_IS_CUSTOMIZED = true;

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/budget-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetItemRepository budgetItemRepository;

    @Autowired
    private BudgetItemMapper budgetItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetItemMockMvc;

    private BudgetItem budgetItem;

    private BudgetItem insertedBudgetItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetItem createEntity(EntityManager em) {
        BudgetItem budgetItem = new BudgetItem()
            .itemType(DEFAULT_ITEM_TYPE)
            .templateId(DEFAULT_TEMPLATE_ID)
            .templateName(DEFAULT_TEMPLATE_NAME)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .unitCost(DEFAULT_UNIT_COST)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .totalCost(DEFAULT_TOTAL_COST)
            .customizations(DEFAULT_CUSTOMIZATIONS)
            .notes(DEFAULT_NOTES)
            .isCustomized(DEFAULT_IS_CUSTOMIZED)
            .version(DEFAULT_VERSION)
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
        budgetItem.setBudget(budget);
        return budgetItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetItem createUpdatedEntity(EntityManager em) {
        BudgetItem updatedBudgetItem = new BudgetItem()
            .itemType(UPDATED_ITEM_TYPE)
            .templateId(UPDATED_TEMPLATE_ID)
            .templateName(UPDATED_TEMPLATE_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitCost(UPDATED_UNIT_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalCost(UPDATED_TOTAL_COST)
            .customizations(UPDATED_CUSTOMIZATIONS)
            .notes(UPDATED_NOTES)
            .isCustomized(UPDATED_IS_CUSTOMIZED)
            .version(UPDATED_VERSION)
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
        updatedBudgetItem.setBudget(budget);
        return updatedBudgetItem;
    }

    @BeforeEach
    void initTest() {
        budgetItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBudgetItem != null) {
            budgetItemRepository.delete(insertedBudgetItem);
            insertedBudgetItem = null;
        }
    }

    @Test
    @Transactional
    void createBudgetItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);
        var returnedBudgetItemDTO = om.readValue(
            restBudgetItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetItemDTO.class
        );

        // Validate the BudgetItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudgetItem = budgetItemMapper.toEntity(returnedBudgetItemDTO);
        assertBudgetItemUpdatableFieldsEquals(returnedBudgetItem, getPersistedBudgetItem(returnedBudgetItem));

        insertedBudgetItem = returnedBudgetItem;
    }

    @Test
    @Transactional
    void createBudgetItemWithExistingId() throws Exception {
        // Create the BudgetItem with an existing ID
        budgetItem.setId(1L);
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkItemTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setItemType(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemplateIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setTemplateId(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemplateNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setTemplateName(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setQuantity(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setUnitPrice(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setTotalPrice(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsCustomizedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setIsCustomized(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setVersion(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setCreatedAt(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetItem.setUpdatedAt(null);

        // Create the BudgetItem, which fails.
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        restBudgetItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBudgetItems() throws Exception {
        // Initialize the database
        insertedBudgetItem = budgetItemRepository.saveAndFlush(budgetItem);

        // Get all the budgetItemList
        restBudgetItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemType").value(hasItem(DEFAULT_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].templateId").value(hasItem(DEFAULT_TEMPLATE_ID)))
            .andExpect(jsonPath("$.[*].templateName").value(hasItem(DEFAULT_TEMPLATE_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].customizations").value(hasItem(DEFAULT_CUSTOMIZATIONS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isCustomized").value(hasItem(DEFAULT_IS_CUSTOMIZED)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBudgetItem() throws Exception {
        // Initialize the database
        insertedBudgetItem = budgetItemRepository.saveAndFlush(budgetItem);

        // Get the budgetItem
        restBudgetItemMockMvc
            .perform(get(ENTITY_API_URL_ID, budgetItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budgetItem.getId().intValue()))
            .andExpect(jsonPath("$.itemType").value(DEFAULT_ITEM_TYPE))
            .andExpect(jsonPath("$.templateId").value(DEFAULT_TEMPLATE_ID))
            .andExpect(jsonPath("$.templateName").value(DEFAULT_TEMPLATE_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.customizations").value(DEFAULT_CUSTOMIZATIONS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isCustomized").value(DEFAULT_IS_CUSTOMIZED))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBudgetItem() throws Exception {
        // Get the budgetItem
        restBudgetItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudgetItem() throws Exception {
        // Initialize the database
        insertedBudgetItem = budgetItemRepository.saveAndFlush(budgetItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetItem
        BudgetItem updatedBudgetItem = budgetItemRepository.findById(budgetItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudgetItem are not directly saved in db
        em.detach(updatedBudgetItem);
        updatedBudgetItem
            .itemType(UPDATED_ITEM_TYPE)
            .templateId(UPDATED_TEMPLATE_ID)
            .templateName(UPDATED_TEMPLATE_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitCost(UPDATED_UNIT_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalCost(UPDATED_TOTAL_COST)
            .customizations(UPDATED_CUSTOMIZATIONS)
            .notes(UPDATED_NOTES)
            .isCustomized(UPDATED_IS_CUSTOMIZED)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(updatedBudgetItem);

        restBudgetItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetItemToMatchAllProperties(updatedBudgetItem);
    }

    @Test
    @Transactional
    void putNonExistingBudgetItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetItem.setId(longCount.incrementAndGet());

        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudgetItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetItem.setId(longCount.incrementAndGet());

        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudgetItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetItem.setId(longCount.incrementAndGet());

        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBudgetItemWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetItem = budgetItemRepository.saveAndFlush(budgetItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetItem using partial update
        BudgetItem partialUpdatedBudgetItem = new BudgetItem();
        partialUpdatedBudgetItem.setId(budgetItem.getId());

        partialUpdatedBudgetItem.itemType(UPDATED_ITEM_TYPE).quantity(UPDATED_QUANTITY).notes(UPDATED_NOTES).updatedAt(UPDATED_UPDATED_AT);

        restBudgetItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetItem))
            )
            .andExpect(status().isOk());

        // Validate the BudgetItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBudgetItem, budgetItem),
            getPersistedBudgetItem(budgetItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateBudgetItemWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetItem = budgetItemRepository.saveAndFlush(budgetItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetItem using partial update
        BudgetItem partialUpdatedBudgetItem = new BudgetItem();
        partialUpdatedBudgetItem.setId(budgetItem.getId());

        partialUpdatedBudgetItem
            .itemType(UPDATED_ITEM_TYPE)
            .templateId(UPDATED_TEMPLATE_ID)
            .templateName(UPDATED_TEMPLATE_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .unitCost(UPDATED_UNIT_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalCost(UPDATED_TOTAL_COST)
            .customizations(UPDATED_CUSTOMIZATIONS)
            .notes(UPDATED_NOTES)
            .isCustomized(UPDATED_IS_CUSTOMIZED)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBudgetItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetItem))
            )
            .andExpect(status().isOk());

        // Validate the BudgetItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetItemUpdatableFieldsEquals(partialUpdatedBudgetItem, getPersistedBudgetItem(partialUpdatedBudgetItem));
    }

    @Test
    @Transactional
    void patchNonExistingBudgetItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetItem.setId(longCount.incrementAndGet());

        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudgetItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetItem.setId(longCount.incrementAndGet());

        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudgetItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetItem.setId(longCount.incrementAndGet());

        // Create the BudgetItem
        BudgetItemDTO budgetItemDTO = budgetItemMapper.toDto(budgetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(budgetItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBudgetItem() throws Exception {
        // Initialize the database
        insertedBudgetItem = budgetItemRepository.saveAndFlush(budgetItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the budgetItem
        restBudgetItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, budgetItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return budgetItemRepository.count();
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

    protected BudgetItem getPersistedBudgetItem(BudgetItem budgetItem) {
        return budgetItemRepository.findById(budgetItem.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetItemToMatchAllProperties(BudgetItem expectedBudgetItem) {
        assertBudgetItemAllPropertiesEquals(expectedBudgetItem, getPersistedBudgetItem(expectedBudgetItem));
    }

    protected void assertPersistedBudgetItemToMatchUpdatableProperties(BudgetItem expectedBudgetItem) {
        assertBudgetItemAllUpdatablePropertiesEquals(expectedBudgetItem, getPersistedBudgetItem(expectedBudgetItem));
    }
}
