package com.magnus.web.rest;

import static com.magnus.domain.SystemConfigAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AppUser;
import com.magnus.domain.SystemConfig;
import com.magnus.repository.SystemConfigRepository;
import com.magnus.service.dto.SystemConfigDTO;
import com.magnus.service.mapper.SystemConfigMapper;
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
 * Integration tests for the {@link SystemConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemConfigResourceIT {

    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDATION_RULES = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATION_RULES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemConfigMockMvc;

    private SystemConfig systemConfig;

    private SystemConfig insertedSystemConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createEntity(EntityManager em) {
        SystemConfig systemConfig = new SystemConfig()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .isActive(DEFAULT_IS_ACTIVE)
            .category(DEFAULT_CATEGORY)
            .isSystem(DEFAULT_IS_SYSTEM)
            .dataType(DEFAULT_DATA_TYPE)
            .validationRules(DEFAULT_VALIDATION_RULES)
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
        systemConfig.setCreatedBy(appUser);
        return systemConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createUpdatedEntity(EntityManager em) {
        SystemConfig updatedSystemConfig = new SystemConfig()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .category(UPDATED_CATEGORY)
            .isSystem(UPDATED_IS_SYSTEM)
            .dataType(UPDATED_DATA_TYPE)
            .validationRules(UPDATED_VALIDATION_RULES)
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
        updatedSystemConfig.setCreatedBy(appUser);
        return updatedSystemConfig;
    }

    @BeforeEach
    void initTest() {
        systemConfig = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSystemConfig != null) {
            systemConfigRepository.delete(insertedSystemConfig);
            insertedSystemConfig = null;
        }
    }

    @Test
    @Transactional
    void createSystemConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);
        var returnedSystemConfigDTO = om.readValue(
            restSystemConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemConfigDTO.class
        );

        // Validate the SystemConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemConfig = systemConfigMapper.toEntity(returnedSystemConfigDTO);
        assertSystemConfigUpdatableFieldsEquals(returnedSystemConfig, getPersistedSystemConfig(returnedSystemConfig));

        insertedSystemConfig = returnedSystemConfig;
    }

    @Test
    @Transactional
    void createSystemConfigWithExistingId() throws Exception {
        // Create the SystemConfig with an existing ID
        systemConfig.setId(1L);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConfigKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setConfigKey(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setIsActive(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setIsSystem(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setCreatedAt(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setUpdatedAt(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemConfigs() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].validationRules").value(hasItem(DEFAULT_VALIDATION_RULES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        // Get the systemConfig
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, systemConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfig.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.validationRules").value(DEFAULT_VALIDATION_RULES))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemConfig() throws Exception {
        // Get the systemConfig
        restSystemConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig
        SystemConfig updatedSystemConfig = systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemConfig are not directly saved in db
        em.detach(updatedSystemConfig);
        updatedSystemConfig
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .category(UPDATED_CATEGORY)
            .isSystem(UPDATED_IS_SYSTEM)
            .dataType(UPDATED_DATA_TYPE)
            .validationRules(UPDATED_VALIDATION_RULES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(updatedSystemConfig);

        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemConfigToMatchAllProperties(updatedSystemConfig);
    }

    @Test
    @Transactional
    void putNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfig partialUpdatedSystemConfig = new SystemConfig();
        partialUpdatedSystemConfig.setId(systemConfig.getId());

        partialUpdatedSystemConfig
            .configKey(UPDATED_CONFIG_KEY)
            .isActive(UPDATED_IS_ACTIVE)
            .isSystem(UPDATED_IS_SYSTEM)
            .dataType(UPDATED_DATA_TYPE)
            .validationRules(UPDATED_VALIDATION_RULES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfig))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemConfig, systemConfig),
            getPersistedSystemConfig(systemConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfig partialUpdatedSystemConfig = new SystemConfig();
        partialUpdatedSystemConfig.setId(systemConfig.getId());

        partialUpdatedSystemConfig
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .isActive(UPDATED_IS_ACTIVE)
            .category(UPDATED_CATEGORY)
            .isSystem(UPDATED_IS_SYSTEM)
            .dataType(UPDATED_DATA_TYPE)
            .validationRules(UPDATED_VALIDATION_RULES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfig))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigUpdatableFieldsEquals(partialUpdatedSystemConfig, getPersistedSystemConfig(partialUpdatedSystemConfig));
    }

    @Test
    @Transactional
    void patchNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemConfig
        restSystemConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemConfigRepository.count();
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

    protected SystemConfig getPersistedSystemConfig(SystemConfig systemConfig) {
        return systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
    }

    protected void assertPersistedSystemConfigToMatchAllProperties(SystemConfig expectedSystemConfig) {
        assertSystemConfigAllPropertiesEquals(expectedSystemConfig, getPersistedSystemConfig(expectedSystemConfig));
    }

    protected void assertPersistedSystemConfigToMatchUpdatableProperties(SystemConfig expectedSystemConfig) {
        assertSystemConfigAllUpdatablePropertiesEquals(expectedSystemConfig, getPersistedSystemConfig(expectedSystemConfig));
    }
}
