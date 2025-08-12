package com.magnus.web.rest;

import static com.magnus.domain.MenuItemAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Menu;
import com.magnus.domain.MenuItem;
import com.magnus.domain.enumeration.FoodCategory;
import com.magnus.repository.MenuItemRepository;
import com.magnus.service.dto.MenuItemDTO;
import com.magnus.service.mapper.MenuItemMapper;
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
 * Integration tests for the {@link MenuItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final FoodCategory DEFAULT_CATEGORY = FoodCategory.APPETIZER;
    private static final FoodCategory UPDATED_CATEGORY = FoodCategory.MAIN;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST = new BigDecimal(1);

    private static final String DEFAULT_ALLERGENS = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGENS = "BBBBBBBBBB";

    private static final String DEFAULT_DIETARY_INFO = "AAAAAAAAAA";
    private static final String UPDATED_DIETARY_INFO = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREPARATION_TIME = 0;
    private static final Integer UPDATED_PREPARATION_TIME = 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/menu-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuItemMockMvc;

    private MenuItem menuItem;

    private MenuItem insertedMenuItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createEntity(EntityManager em) {
        MenuItem menuItem = new MenuItem()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .price(DEFAULT_PRICE)
            .cost(DEFAULT_COST)
            .allergens(DEFAULT_ALLERGENS)
            .dietaryInfo(DEFAULT_DIETARY_INFO)
            .preparationTime(DEFAULT_PREPARATION_TIME)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Menu menu;
        if (TestUtil.findAll(em, Menu.class).isEmpty()) {
            menu = MenuResourceIT.createEntity();
            em.persist(menu);
            em.flush();
        } else {
            menu = TestUtil.findAll(em, Menu.class).get(0);
        }
        menuItem.setMenu(menu);
        return menuItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItem createUpdatedEntity(EntityManager em) {
        MenuItem updatedMenuItem = new MenuItem()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .price(UPDATED_PRICE)
            .cost(UPDATED_COST)
            .allergens(UPDATED_ALLERGENS)
            .dietaryInfo(UPDATED_DIETARY_INFO)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Menu menu;
        if (TestUtil.findAll(em, Menu.class).isEmpty()) {
            menu = MenuResourceIT.createUpdatedEntity();
            em.persist(menu);
            em.flush();
        } else {
            menu = TestUtil.findAll(em, Menu.class).get(0);
        }
        updatedMenuItem.setMenu(menu);
        return updatedMenuItem;
    }

    @BeforeEach
    void initTest() {
        menuItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMenuItem != null) {
            menuItemRepository.delete(insertedMenuItem);
            insertedMenuItem = null;
        }
    }

    @Test
    @Transactional
    void createMenuItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);
        var returnedMenuItemDTO = om.readValue(
            restMenuItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MenuItemDTO.class
        );

        // Validate the MenuItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMenuItem = menuItemMapper.toEntity(returnedMenuItemDTO);
        assertMenuItemUpdatableFieldsEquals(returnedMenuItem, getPersistedMenuItem(returnedMenuItem));

        insertedMenuItem = returnedMenuItem;
    }

    @Test
    @Transactional
    void createMenuItemWithExistingId() throws Exception {
        // Create the MenuItem with an existing ID
        menuItem.setId(1L);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuItem.setName(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuItem.setCategory(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuItem.setPrice(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuItem.setIsActive(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuItem.setCreatedAt(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menuItem.setUpdatedAt(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenuItems() throws Exception {
        // Initialize the database
        insertedMenuItem = menuItemRepository.saveAndFlush(menuItem);

        // Get all the menuItemList
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].allergens").value(hasItem(DEFAULT_ALLERGENS)))
            .andExpect(jsonPath("$.[*].dietaryInfo").value(hasItem(DEFAULT_DIETARY_INFO)))
            .andExpect(jsonPath("$.[*].preparationTime").value(hasItem(DEFAULT_PREPARATION_TIME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getMenuItem() throws Exception {
        // Initialize the database
        insertedMenuItem = menuItemRepository.saveAndFlush(menuItem);

        // Get the menuItem
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL_ID, menuItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.allergens").value(DEFAULT_ALLERGENS))
            .andExpect(jsonPath("$.dietaryInfo").value(DEFAULT_DIETARY_INFO))
            .andExpect(jsonPath("$.preparationTime").value(DEFAULT_PREPARATION_TIME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuItem() throws Exception {
        // Initialize the database
        insertedMenuItem = menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuItem
        MenuItem updatedMenuItem = menuItemRepository.findById(menuItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMenuItem are not directly saved in db
        em.detach(updatedMenuItem);
        updatedMenuItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .price(UPDATED_PRICE)
            .cost(UPDATED_COST)
            .allergens(UPDATED_ALLERGENS)
            .dietaryInfo(UPDATED_DIETARY_INFO)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(updatedMenuItem);

        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMenuItemToMatchAllProperties(updatedMenuItem);
    }

    @Test
    @Transactional
    void putNonExistingMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        insertedMenuItem = menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .price(UPDATED_PRICE)
            .cost(UPDATED_COST)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .createdAt(UPDATED_CREATED_AT);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMenuItem, menuItem), getPersistedMenuItem(menuItem));
    }

    @Test
    @Transactional
    void fullUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        insertedMenuItem = menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuItem using partial update
        MenuItem partialUpdatedMenuItem = new MenuItem();
        partialUpdatedMenuItem.setId(menuItem.getId());

        partialUpdatedMenuItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .price(UPDATED_PRICE)
            .cost(UPDATED_COST)
            .allergens(UPDATED_ALLERGENS)
            .dietaryInfo(UPDATED_DIETARY_INFO)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuItem))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuItemUpdatableFieldsEquals(partialUpdatedMenuItem, getPersistedMenuItem(partialUpdatedMenuItem));
    }

    @Test
    @Transactional
    void patchNonExistingMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuItem.setId(longCount.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuItem() throws Exception {
        // Initialize the database
        insertedMenuItem = menuItemRepository.saveAndFlush(menuItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the menuItem
        restMenuItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return menuItemRepository.count();
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

    protected MenuItem getPersistedMenuItem(MenuItem menuItem) {
        return menuItemRepository.findById(menuItem.getId()).orElseThrow();
    }

    protected void assertPersistedMenuItemToMatchAllProperties(MenuItem expectedMenuItem) {
        assertMenuItemAllPropertiesEquals(expectedMenuItem, getPersistedMenuItem(expectedMenuItem));
    }

    protected void assertPersistedMenuItemToMatchUpdatableProperties(MenuItem expectedMenuItem) {
        assertMenuItemAllUpdatablePropertiesEquals(expectedMenuItem, getPersistedMenuItem(expectedMenuItem));
    }
}
