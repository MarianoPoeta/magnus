package com.magnus.web.rest;

import static com.magnus.domain.BudgetTemplateAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.BudgetTemplate;
import com.magnus.domain.enumeration.TemplateType;
import com.magnus.repository.BudgetTemplateRepository;
import com.magnus.service.dto.BudgetTemplateDTO;
import com.magnus.service.mapper.BudgetTemplateMapper;
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
 * Integration tests for the {@link BudgetTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BudgetTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TemplateType DEFAULT_TYPE = TemplateType.MENU;
    private static final TemplateType UPDATED_TYPE = TemplateType.ACTIVITY;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_DATA = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_DATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/budget-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BudgetTemplateRepository budgetTemplateRepository;

    @Autowired
    private BudgetTemplateMapper budgetTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBudgetTemplateMockMvc;

    private BudgetTemplate budgetTemplate;

    private BudgetTemplate insertedBudgetTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetTemplate createEntity(EntityManager em) {
        BudgetTemplate budgetTemplate = new BudgetTemplate()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .category(DEFAULT_CATEGORY)
            .isActive(DEFAULT_IS_ACTIVE)
            .isSystem(DEFAULT_IS_SYSTEM)
            .configuration(DEFAULT_CONFIGURATION)
            .templateData(DEFAULT_TEMPLATE_DATA)
            .version(DEFAULT_VERSION)
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
        budgetTemplate.setCreatedBy(appUser);
        return budgetTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BudgetTemplate createUpdatedEntity(EntityManager em) {
        BudgetTemplate updatedBudgetTemplate = new BudgetTemplate()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .isActive(UPDATED_IS_ACTIVE)
            .isSystem(UPDATED_IS_SYSTEM)
            .configuration(UPDATED_CONFIGURATION)
            .templateData(UPDATED_TEMPLATE_DATA)
            .version(UPDATED_VERSION)
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
        updatedBudgetTemplate.setCreatedBy(appUser);
        return updatedBudgetTemplate;
    }

    @BeforeEach
    void initTest() {
        budgetTemplate = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBudgetTemplate != null) {
            budgetTemplateRepository.delete(insertedBudgetTemplate);
            insertedBudgetTemplate = null;
        }
    }

    @Test
    @Transactional
    void createBudgetTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);
        var returnedBudgetTemplateDTO = om.readValue(
            restBudgetTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BudgetTemplateDTO.class
        );

        // Validate the BudgetTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBudgetTemplate = budgetTemplateMapper.toEntity(returnedBudgetTemplateDTO);
        assertBudgetTemplateUpdatableFieldsEquals(returnedBudgetTemplate, getPersistedBudgetTemplate(returnedBudgetTemplate));

        insertedBudgetTemplate = returnedBudgetTemplate;
    }

    @Test
    @Transactional
    void createBudgetTemplateWithExistingId() throws Exception {
        // Create the BudgetTemplate with an existing ID
        budgetTemplate.setId(1L);
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setName(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setType(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setIsActive(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setIsSystem(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setVersion(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setCreatedAt(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        budgetTemplate.setUpdatedAt(null);

        // Create the BudgetTemplate, which fails.
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        restBudgetTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBudgetTemplates() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get all the budgetTemplateList
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(budgetTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].templateData").value(hasItem(DEFAULT_TEMPLATE_DATA)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getBudgetTemplate() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        // Get the budgetTemplate
        restBudgetTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, budgetTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(budgetTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION))
            .andExpect(jsonPath("$.templateData").value(DEFAULT_TEMPLATE_DATA))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBudgetTemplate() throws Exception {
        // Get the budgetTemplate
        restBudgetTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBudgetTemplate() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetTemplate
        BudgetTemplate updatedBudgetTemplate = budgetTemplateRepository.findById(budgetTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBudgetTemplate are not directly saved in db
        em.detach(updatedBudgetTemplate);
        updatedBudgetTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .isActive(UPDATED_IS_ACTIVE)
            .isSystem(UPDATED_IS_SYSTEM)
            .configuration(UPDATED_CONFIGURATION)
            .templateData(UPDATED_TEMPLATE_DATA)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(updatedBudgetTemplate);

        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBudgetTemplateToMatchAllProperties(updatedBudgetTemplate);
    }

    @Test
    @Transactional
    void putNonExistingBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, budgetTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBudgetTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetTemplate using partial update
        BudgetTemplate partialUpdatedBudgetTemplate = new BudgetTemplate();
        partialUpdatedBudgetTemplate.setId(budgetTemplate.getId());

        partialUpdatedBudgetTemplate
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .isActive(UPDATED_IS_ACTIVE)
            .configuration(UPDATED_CONFIGURATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetTemplate))
            )
            .andExpect(status().isOk());

        // Validate the BudgetTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBudgetTemplate, budgetTemplate),
            getPersistedBudgetTemplate(budgetTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateBudgetTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the budgetTemplate using partial update
        BudgetTemplate partialUpdatedBudgetTemplate = new BudgetTemplate();
        partialUpdatedBudgetTemplate.setId(budgetTemplate.getId());

        partialUpdatedBudgetTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .isActive(UPDATED_IS_ACTIVE)
            .isSystem(UPDATED_IS_SYSTEM)
            .configuration(UPDATED_CONFIGURATION)
            .templateData(UPDATED_TEMPLATE_DATA)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBudgetTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBudgetTemplate))
            )
            .andExpect(status().isOk());

        // Validate the BudgetTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBudgetTemplateUpdatableFieldsEquals(partialUpdatedBudgetTemplate, getPersistedBudgetTemplate(partialUpdatedBudgetTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, budgetTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(budgetTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBudgetTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        budgetTemplate.setId(longCount.incrementAndGet());

        // Create the BudgetTemplate
        BudgetTemplateDTO budgetTemplateDTO = budgetTemplateMapper.toDto(budgetTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBudgetTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(budgetTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BudgetTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBudgetTemplate() throws Exception {
        // Initialize the database
        insertedBudgetTemplate = budgetTemplateRepository.saveAndFlush(budgetTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the budgetTemplate
        restBudgetTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, budgetTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return budgetTemplateRepository.count();
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

    protected BudgetTemplate getPersistedBudgetTemplate(BudgetTemplate budgetTemplate) {
        return budgetTemplateRepository.findById(budgetTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedBudgetTemplateToMatchAllProperties(BudgetTemplate expectedBudgetTemplate) {
        assertBudgetTemplateAllPropertiesEquals(expectedBudgetTemplate, getPersistedBudgetTemplate(expectedBudgetTemplate));
    }

    protected void assertPersistedBudgetTemplateToMatchUpdatableProperties(BudgetTemplate expectedBudgetTemplate) {
        assertBudgetTemplateAllUpdatablePropertiesEquals(expectedBudgetTemplate, getPersistedBudgetTemplate(expectedBudgetTemplate));
    }
}
