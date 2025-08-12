package com.magnus.web.rest;

import static com.magnus.domain.FoodItemAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.FoodItem;
import com.magnus.domain.enumeration.FoodCategory;
import com.magnus.repository.FoodItemRepository;
import com.magnus.service.dto.FoodItemDTO;
import com.magnus.service.mapper.FoodItemMapper;
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
 * Integration tests for the {@link FoodItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final FoodCategory DEFAULT_CATEGORY = FoodCategory.APPETIZER;
    private static final FoodCategory UPDATED_CATEGORY = FoodCategory.MAIN;

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BASE_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_BASE_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_BASE_COST = new BigDecimal(1);

    private static final String DEFAULT_SERVING_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SERVING_SIZE = "BBBBBBBBBB";

    private static final Integer DEFAULT_GUESTS_PER_UNIT = 1;
    private static final Integer UPDATED_GUESTS_PER_UNIT = 2;

    private static final Integer DEFAULT_MAX_UNITS = 1;
    private static final Integer UPDATED_MAX_UNITS = 2;

    private static final String DEFAULT_ALLERGENS = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGENS = "BBBBBBBBBB";

    private static final String DEFAULT_DIETARY_INFO = "AAAAAAAAAA";
    private static final String UPDATED_DIETARY_INFO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_TEMPLATE = false;
    private static final Boolean UPDATED_IS_TEMPLATE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/food-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodItemMapper foodItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodItemMockMvc;

    private FoodItem foodItem;

    private FoodItem insertedFoodItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodItem createEntity() {
        return new FoodItem()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .basePrice(DEFAULT_BASE_PRICE)
            .baseCost(DEFAULT_BASE_COST)
            .servingSize(DEFAULT_SERVING_SIZE)
            .guestsPerUnit(DEFAULT_GUESTS_PER_UNIT)
            .maxUnits(DEFAULT_MAX_UNITS)
            .allergens(DEFAULT_ALLERGENS)
            .dietaryInfo(DEFAULT_DIETARY_INFO)
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
    public static FoodItem createUpdatedEntity() {
        return new FoodItem()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .baseCost(UPDATED_BASE_COST)
            .servingSize(UPDATED_SERVING_SIZE)
            .guestsPerUnit(UPDATED_GUESTS_PER_UNIT)
            .maxUnits(UPDATED_MAX_UNITS)
            .allergens(UPDATED_ALLERGENS)
            .dietaryInfo(UPDATED_DIETARY_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        foodItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFoodItem != null) {
            foodItemRepository.delete(insertedFoodItem);
            insertedFoodItem = null;
        }
    }

    @Test
    @Transactional
    void createFoodItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);
        var returnedFoodItemDTO = om.readValue(
            restFoodItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FoodItemDTO.class
        );

        // Validate the FoodItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFoodItem = foodItemMapper.toEntity(returnedFoodItemDTO);
        assertFoodItemUpdatableFieldsEquals(returnedFoodItem, getPersistedFoodItem(returnedFoodItem));

        insertedFoodItem = returnedFoodItem;
    }

    @Test
    @Transactional
    void createFoodItemWithExistingId() throws Exception {
        // Create the FoodItem with an existing ID
        foodItem.setId(1L);
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setName(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setCategory(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBasePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setBasePrice(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGuestsPerUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setGuestsPerUnit(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setIsActive(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setIsTemplate(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setCreatedAt(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        foodItem.setUpdatedAt(null);

        // Create the FoodItem, which fails.
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        restFoodItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFoodItems() throws Exception {
        // Initialize the database
        insertedFoodItem = foodItemRepository.saveAndFlush(foodItem);

        // Get all the foodItemList
        restFoodItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(sameNumber(DEFAULT_BASE_PRICE))))
            .andExpect(jsonPath("$.[*].baseCost").value(hasItem(sameNumber(DEFAULT_BASE_COST))))
            .andExpect(jsonPath("$.[*].servingSize").value(hasItem(DEFAULT_SERVING_SIZE)))
            .andExpect(jsonPath("$.[*].guestsPerUnit").value(hasItem(DEFAULT_GUESTS_PER_UNIT)))
            .andExpect(jsonPath("$.[*].maxUnits").value(hasItem(DEFAULT_MAX_UNITS)))
            .andExpect(jsonPath("$.[*].allergens").value(hasItem(DEFAULT_ALLERGENS)))
            .andExpect(jsonPath("$.[*].dietaryInfo").value(hasItem(DEFAULT_DIETARY_INFO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isTemplate").value(hasItem(DEFAULT_IS_TEMPLATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getFoodItem() throws Exception {
        // Initialize the database
        insertedFoodItem = foodItemRepository.saveAndFlush(foodItem);

        // Get the foodItem
        restFoodItemMockMvc
            .perform(get(ENTITY_API_URL_ID, foodItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.basePrice").value(sameNumber(DEFAULT_BASE_PRICE)))
            .andExpect(jsonPath("$.baseCost").value(sameNumber(DEFAULT_BASE_COST)))
            .andExpect(jsonPath("$.servingSize").value(DEFAULT_SERVING_SIZE))
            .andExpect(jsonPath("$.guestsPerUnit").value(DEFAULT_GUESTS_PER_UNIT))
            .andExpect(jsonPath("$.maxUnits").value(DEFAULT_MAX_UNITS))
            .andExpect(jsonPath("$.allergens").value(DEFAULT_ALLERGENS))
            .andExpect(jsonPath("$.dietaryInfo").value(DEFAULT_DIETARY_INFO))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isTemplate").value(DEFAULT_IS_TEMPLATE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFoodItem() throws Exception {
        // Get the foodItem
        restFoodItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFoodItem() throws Exception {
        // Initialize the database
        insertedFoodItem = foodItemRepository.saveAndFlush(foodItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the foodItem
        FoodItem updatedFoodItem = foodItemRepository.findById(foodItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFoodItem are not directly saved in db
        em.detach(updatedFoodItem);
        updatedFoodItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .baseCost(UPDATED_BASE_COST)
            .servingSize(UPDATED_SERVING_SIZE)
            .guestsPerUnit(UPDATED_GUESTS_PER_UNIT)
            .maxUnits(UPDATED_MAX_UNITS)
            .allergens(UPDATED_ALLERGENS)
            .dietaryInfo(UPDATED_DIETARY_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(updatedFoodItem);

        restFoodItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(foodItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFoodItemToMatchAllProperties(updatedFoodItem);
    }

    @Test
    @Transactional
    void putNonExistingFoodItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodItem.setId(longCount.incrementAndGet());

        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(foodItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodItem.setId(longCount.incrementAndGet());

        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(foodItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodItem.setId(longCount.incrementAndGet());

        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodItemWithPatch() throws Exception {
        // Initialize the database
        insertedFoodItem = foodItemRepository.saveAndFlush(foodItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the foodItem using partial update
        FoodItem partialUpdatedFoodItem = new FoodItem();
        partialUpdatedFoodItem.setId(foodItem.getId());

        partialUpdatedFoodItem
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .guestsPerUnit(UPDATED_GUESTS_PER_UNIT)
            .maxUnits(UPDATED_MAX_UNITS)
            .dietaryInfo(UPDATED_DIETARY_INFO);

        restFoodItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFoodItem))
            )
            .andExpect(status().isOk());

        // Validate the FoodItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFoodItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFoodItem, foodItem), getPersistedFoodItem(foodItem));
    }

    @Test
    @Transactional
    void fullUpdateFoodItemWithPatch() throws Exception {
        // Initialize the database
        insertedFoodItem = foodItemRepository.saveAndFlush(foodItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the foodItem using partial update
        FoodItem partialUpdatedFoodItem = new FoodItem();
        partialUpdatedFoodItem.setId(foodItem.getId());

        partialUpdatedFoodItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .basePrice(UPDATED_BASE_PRICE)
            .baseCost(UPDATED_BASE_COST)
            .servingSize(UPDATED_SERVING_SIZE)
            .guestsPerUnit(UPDATED_GUESTS_PER_UNIT)
            .maxUnits(UPDATED_MAX_UNITS)
            .allergens(UPDATED_ALLERGENS)
            .dietaryInfo(UPDATED_DIETARY_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restFoodItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFoodItem))
            )
            .andExpect(status().isOk());

        // Validate the FoodItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFoodItemUpdatableFieldsEquals(partialUpdatedFoodItem, getPersistedFoodItem(partialUpdatedFoodItem));
    }

    @Test
    @Transactional
    void patchNonExistingFoodItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodItem.setId(longCount.incrementAndGet());

        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(foodItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodItem.setId(longCount.incrementAndGet());

        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(foodItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        foodItem.setId(longCount.incrementAndGet());

        // Create the FoodItem
        FoodItemDTO foodItemDTO = foodItemMapper.toDto(foodItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(foodItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodItem() throws Exception {
        // Initialize the database
        insertedFoodItem = foodItemRepository.saveAndFlush(foodItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the foodItem
        restFoodItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return foodItemRepository.count();
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

    protected FoodItem getPersistedFoodItem(FoodItem foodItem) {
        return foodItemRepository.findById(foodItem.getId()).orElseThrow();
    }

    protected void assertPersistedFoodItemToMatchAllProperties(FoodItem expectedFoodItem) {
        assertFoodItemAllPropertiesEquals(expectedFoodItem, getPersistedFoodItem(expectedFoodItem));
    }

    protected void assertPersistedFoodItemToMatchUpdatableProperties(FoodItem expectedFoodItem) {
        assertFoodItemAllUpdatablePropertiesEquals(expectedFoodItem, getPersistedFoodItem(expectedFoodItem));
    }
}
