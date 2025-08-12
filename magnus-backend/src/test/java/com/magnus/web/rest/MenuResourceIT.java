package com.magnus.web.rest;

import static com.magnus.domain.MenuAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Menu;
import com.magnus.domain.enumeration.MenuType;
import com.magnus.repository.MenuRepository;
import com.magnus.service.MenuService;
import com.magnus.service.dto.MenuDTO;
import com.magnus.service.mapper.MenuMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MenuResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MenuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final MenuType DEFAULT_TYPE = MenuType.BREAKFAST;
    private static final MenuType UPDATED_TYPE = MenuType.LUNCH;

    private static final BigDecimal DEFAULT_PRICE_PER_PERSON = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_PER_PERSON = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST_PER_PERSON = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST_PER_PERSON = new BigDecimal(1);

    private static final Integer DEFAULT_MIN_PEOPLE = 1;
    private static final Integer UPDATED_MIN_PEOPLE = 2;

    private static final Integer DEFAULT_MAX_PEOPLE = 1;
    private static final Integer UPDATED_MAX_PEOPLE = 2;

    private static final String DEFAULT_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREPARATION_TIME = 0;
    private static final Integer UPDATED_PREPARATION_TIME = 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_TEMPLATE = false;
    private static final Boolean UPDATED_IS_TEMPLATE = true;

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MenuRepository menuRepository;

    @Mock
    private MenuRepository menuRepositoryMock;

    @Autowired
    private MenuMapper menuMapper;

    @Mock
    private MenuService menuServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuMockMvc;

    private Menu menu;

    private Menu insertedMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createEntity() {
        return new Menu()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .pricePerPerson(DEFAULT_PRICE_PER_PERSON)
            .costPerPerson(DEFAULT_COST_PER_PERSON)
            .minPeople(DEFAULT_MIN_PEOPLE)
            .maxPeople(DEFAULT_MAX_PEOPLE)
            .restaurant(DEFAULT_RESTAURANT)
            .preparationTime(DEFAULT_PREPARATION_TIME)
            .isActive(DEFAULT_IS_ACTIVE)
            .isTemplate(DEFAULT_IS_TEMPLATE)
            .version(DEFAULT_VERSION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Menu createUpdatedEntity() {
        return new Menu()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerPerson(UPDATED_PRICE_PER_PERSON)
            .costPerPerson(UPDATED_COST_PER_PERSON)
            .minPeople(UPDATED_MIN_PEOPLE)
            .maxPeople(UPDATED_MAX_PEOPLE)
            .restaurant(UPDATED_RESTAURANT)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        menu = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMenu != null) {
            menuRepository.delete(insertedMenu);
            insertedMenu = null;
        }
    }

    @Test
    @Transactional
    void createMenu() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);
        var returnedMenuDTO = om.readValue(
            restMenuMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MenuDTO.class
        );

        // Validate the Menu in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMenu = menuMapper.toEntity(returnedMenuDTO);
        assertMenuUpdatableFieldsEquals(returnedMenu, getPersistedMenu(returnedMenu));

        insertedMenu = returnedMenu;
    }

    @Test
    @Transactional
    void createMenuWithExistingId() throws Exception {
        // Create the Menu with an existing ID
        menu.setId(1L);
        MenuDTO menuDTO = menuMapper.toDto(menu);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setName(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setType(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerPersonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setPricePerPerson(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinPeopleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setMinPeople(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestaurantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setRestaurant(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setIsActive(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setIsTemplate(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setVersion(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setCreatedAt(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        menu.setUpdatedAt(null);

        // Create the Menu, which fails.
        MenuDTO menuDTO = menuMapper.toDto(menu);

        restMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenus() throws Exception {
        // Initialize the database
        insertedMenu = menuRepository.saveAndFlush(menu);

        // Get all the menuList
        restMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pricePerPerson").value(hasItem(sameNumber(DEFAULT_PRICE_PER_PERSON))))
            .andExpect(jsonPath("$.[*].costPerPerson").value(hasItem(sameNumber(DEFAULT_COST_PER_PERSON))))
            .andExpect(jsonPath("$.[*].minPeople").value(hasItem(DEFAULT_MIN_PEOPLE)))
            .andExpect(jsonPath("$.[*].maxPeople").value(hasItem(DEFAULT_MAX_PEOPLE)))
            .andExpect(jsonPath("$.[*].restaurant").value(hasItem(DEFAULT_RESTAURANT)))
            .andExpect(jsonPath("$.[*].preparationTime").value(hasItem(DEFAULT_PREPARATION_TIME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isTemplate").value(hasItem(DEFAULT_IS_TEMPLATE)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenusWithEagerRelationshipsIsEnabled() throws Exception {
        when(menuServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(menuServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenusWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(menuServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(menuRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMenu() throws Exception {
        // Initialize the database
        insertedMenu = menuRepository.saveAndFlush(menu);

        // Get the menu
        restMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, menu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.pricePerPerson").value(sameNumber(DEFAULT_PRICE_PER_PERSON)))
            .andExpect(jsonPath("$.costPerPerson").value(sameNumber(DEFAULT_COST_PER_PERSON)))
            .andExpect(jsonPath("$.minPeople").value(DEFAULT_MIN_PEOPLE))
            .andExpect(jsonPath("$.maxPeople").value(DEFAULT_MAX_PEOPLE))
            .andExpect(jsonPath("$.restaurant").value(DEFAULT_RESTAURANT))
            .andExpect(jsonPath("$.preparationTime").value(DEFAULT_PREPARATION_TIME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isTemplate").value(DEFAULT_IS_TEMPLATE))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMenu() throws Exception {
        // Get the menu
        restMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenu() throws Exception {
        // Initialize the database
        insertedMenu = menuRepository.saveAndFlush(menu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menu
        Menu updatedMenu = menuRepository.findById(menu.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMenu are not directly saved in db
        em.detach(updatedMenu);
        updatedMenu
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerPerson(UPDATED_PRICE_PER_PERSON)
            .costPerPerson(UPDATED_COST_PER_PERSON)
            .minPeople(UPDATED_MIN_PEOPLE)
            .maxPeople(UPDATED_MAX_PEOPLE)
            .restaurant(UPDATED_RESTAURANT)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        MenuDTO menuDTO = menuMapper.toDto(updatedMenu);

        restMenuMockMvc
            .perform(put(ENTITY_API_URL_ID, menuDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isOk());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMenuToMatchAllProperties(updatedMenu);
    }

    @Test
    @Transactional
    void putNonExistingMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menu.setId(longCount.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(put(ENTITY_API_URL_ID, menuDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menu.setId(longCount.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menu.setId(longCount.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        insertedMenu = menuRepository.saveAndFlush(menu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerPerson(UPDATED_PRICE_PER_PERSON)
            .costPerPerson(UPDATED_COST_PER_PERSON)
            .minPeople(UPDATED_MIN_PEOPLE)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMenu, menu), getPersistedMenu(menu));
    }

    @Test
    @Transactional
    void fullUpdateMenuWithPatch() throws Exception {
        // Initialize the database
        insertedMenu = menuRepository.saveAndFlush(menu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menu using partial update
        Menu partialUpdatedMenu = new Menu();
        partialUpdatedMenu.setId(menu.getId());

        partialUpdatedMenu
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerPerson(UPDATED_PRICE_PER_PERSON)
            .costPerPerson(UPDATED_COST_PER_PERSON)
            .minPeople(UPDATED_MIN_PEOPLE)
            .maxPeople(UPDATED_MAX_PEOPLE)
            .restaurant(UPDATED_RESTAURANT)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenu))
            )
            .andExpect(status().isOk());

        // Validate the Menu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuUpdatableFieldsEquals(partialUpdatedMenu, getPersistedMenu(partialUpdatedMenu));
    }

    @Test
    @Transactional
    void patchNonExistingMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menu.setId(longCount.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menu.setId(longCount.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menu.setId(longCount.incrementAndGet());

        // Create the Menu
        MenuDTO menuDTO = menuMapper.toDto(menu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Menu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenu() throws Exception {
        // Initialize the database
        insertedMenu = menuRepository.saveAndFlush(menu);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the menu
        restMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, menu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return menuRepository.count();
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

    protected Menu getPersistedMenu(Menu menu) {
        return menuRepository.findById(menu.getId()).orElseThrow();
    }

    protected void assertPersistedMenuToMatchAllProperties(Menu expectedMenu) {
        assertMenuAllPropertiesEquals(expectedMenu, getPersistedMenu(expectedMenu));
    }

    protected void assertPersistedMenuToMatchUpdatableProperties(Menu expectedMenu) {
        assertMenuAllUpdatablePropertiesEquals(expectedMenu, getPersistedMenu(expectedMenu));
    }
}
