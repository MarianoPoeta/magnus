package com.magnus.web.rest;

import static com.magnus.domain.ConflictResolutionAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.ConflictResolution;
import com.magnus.repository.ConflictResolutionRepository;
import com.magnus.service.dto.ConflictResolutionDTO;
import com.magnus.service.mapper.ConflictResolutionMapper;
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
 * Integration tests for the {@link ConflictResolutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConflictResolutionResourceIT {

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_REMOTE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_REMOTE_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLVED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_RESOLVED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOLUTION_STRATEGY = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUTION_STRATEGY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RESOLVED = false;
    private static final Boolean UPDATED_IS_RESOLVED = true;

    private static final Instant DEFAULT_RESOLVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CONFLICT_DETECTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONFLICT_DETECTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/conflict-resolutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConflictResolutionRepository conflictResolutionRepository;

    @Autowired
    private ConflictResolutionMapper conflictResolutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConflictResolutionMockMvc;

    private ConflictResolution conflictResolution;

    private ConflictResolution insertedConflictResolution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConflictResolution createEntity() {
        return new ConflictResolution()
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .fieldName(DEFAULT_FIELD_NAME)
            .localValue(DEFAULT_LOCAL_VALUE)
            .remoteValue(DEFAULT_REMOTE_VALUE)
            .resolvedValue(DEFAULT_RESOLVED_VALUE)
            .resolutionStrategy(DEFAULT_RESOLUTION_STRATEGY)
            .isResolved(DEFAULT_IS_RESOLVED)
            .resolvedAt(DEFAULT_RESOLVED_AT)
            .conflictDetectedAt(DEFAULT_CONFLICT_DETECTED_AT)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConflictResolution createUpdatedEntity() {
        return new ConflictResolution()
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fieldName(UPDATED_FIELD_NAME)
            .localValue(UPDATED_LOCAL_VALUE)
            .remoteValue(UPDATED_REMOTE_VALUE)
            .resolvedValue(UPDATED_RESOLVED_VALUE)
            .resolutionStrategy(UPDATED_RESOLUTION_STRATEGY)
            .isResolved(UPDATED_IS_RESOLVED)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .conflictDetectedAt(UPDATED_CONFLICT_DETECTED_AT)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        conflictResolution = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedConflictResolution != null) {
            conflictResolutionRepository.delete(insertedConflictResolution);
            insertedConflictResolution = null;
        }
    }

    @Test
    @Transactional
    void createConflictResolution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);
        var returnedConflictResolutionDTO = om.readValue(
            restConflictResolutionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConflictResolutionDTO.class
        );

        // Validate the ConflictResolution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConflictResolution = conflictResolutionMapper.toEntity(returnedConflictResolutionDTO);
        assertConflictResolutionUpdatableFieldsEquals(
            returnedConflictResolution,
            getPersistedConflictResolution(returnedConflictResolution)
        );

        insertedConflictResolution = returnedConflictResolution;
    }

    @Test
    @Transactional
    void createConflictResolutionWithExistingId() throws Exception {
        // Create the ConflictResolution with an existing ID
        conflictResolution.setId(1L);
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conflictResolution.setEntityType(null);

        // Create the ConflictResolution, which fails.
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conflictResolution.setEntityId(null);

        // Create the ConflictResolution, which fails.
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResolutionStrategyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conflictResolution.setResolutionStrategy(null);

        // Create the ConflictResolution, which fails.
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsResolvedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conflictResolution.setIsResolved(null);

        // Create the ConflictResolution, which fails.
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConflictDetectedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conflictResolution.setConflictDetectedAt(null);

        // Create the ConflictResolution, which fails.
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conflictResolution.setCreatedAt(null);

        // Create the ConflictResolution, which fails.
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        restConflictResolutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConflictResolutions() throws Exception {
        // Initialize the database
        insertedConflictResolution = conflictResolutionRepository.saveAndFlush(conflictResolution);

        // Get all the conflictResolutionList
        restConflictResolutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conflictResolution.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].localValue").value(hasItem(DEFAULT_LOCAL_VALUE)))
            .andExpect(jsonPath("$.[*].remoteValue").value(hasItem(DEFAULT_REMOTE_VALUE)))
            .andExpect(jsonPath("$.[*].resolvedValue").value(hasItem(DEFAULT_RESOLVED_VALUE)))
            .andExpect(jsonPath("$.[*].resolutionStrategy").value(hasItem(DEFAULT_RESOLUTION_STRATEGY)))
            .andExpect(jsonPath("$.[*].isResolved").value(hasItem(DEFAULT_IS_RESOLVED)))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(DEFAULT_RESOLVED_AT.toString())))
            .andExpect(jsonPath("$.[*].conflictDetectedAt").value(hasItem(DEFAULT_CONFLICT_DETECTED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getConflictResolution() throws Exception {
        // Initialize the database
        insertedConflictResolution = conflictResolutionRepository.saveAndFlush(conflictResolution);

        // Get the conflictResolution
        restConflictResolutionMockMvc
            .perform(get(ENTITY_API_URL_ID, conflictResolution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conflictResolution.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.localValue").value(DEFAULT_LOCAL_VALUE))
            .andExpect(jsonPath("$.remoteValue").value(DEFAULT_REMOTE_VALUE))
            .andExpect(jsonPath("$.resolvedValue").value(DEFAULT_RESOLVED_VALUE))
            .andExpect(jsonPath("$.resolutionStrategy").value(DEFAULT_RESOLUTION_STRATEGY))
            .andExpect(jsonPath("$.isResolved").value(DEFAULT_IS_RESOLVED))
            .andExpect(jsonPath("$.resolvedAt").value(DEFAULT_RESOLVED_AT.toString()))
            .andExpect(jsonPath("$.conflictDetectedAt").value(DEFAULT_CONFLICT_DETECTED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConflictResolution() throws Exception {
        // Get the conflictResolution
        restConflictResolutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConflictResolution() throws Exception {
        // Initialize the database
        insertedConflictResolution = conflictResolutionRepository.saveAndFlush(conflictResolution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conflictResolution
        ConflictResolution updatedConflictResolution = conflictResolutionRepository.findById(conflictResolution.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConflictResolution are not directly saved in db
        em.detach(updatedConflictResolution);
        updatedConflictResolution
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fieldName(UPDATED_FIELD_NAME)
            .localValue(UPDATED_LOCAL_VALUE)
            .remoteValue(UPDATED_REMOTE_VALUE)
            .resolvedValue(UPDATED_RESOLVED_VALUE)
            .resolutionStrategy(UPDATED_RESOLUTION_STRATEGY)
            .isResolved(UPDATED_IS_RESOLVED)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .conflictDetectedAt(UPDATED_CONFLICT_DETECTED_AT)
            .createdAt(UPDATED_CREATED_AT);
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(updatedConflictResolution);

        restConflictResolutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conflictResolutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conflictResolutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConflictResolutionToMatchAllProperties(updatedConflictResolution);
    }

    @Test
    @Transactional
    void putNonExistingConflictResolution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conflictResolution.setId(longCount.incrementAndGet());

        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConflictResolutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conflictResolutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conflictResolutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConflictResolution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conflictResolution.setId(longCount.incrementAndGet());

        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConflictResolutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conflictResolutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConflictResolution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conflictResolution.setId(longCount.incrementAndGet());

        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConflictResolutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConflictResolutionWithPatch() throws Exception {
        // Initialize the database
        insertedConflictResolution = conflictResolutionRepository.saveAndFlush(conflictResolution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conflictResolution using partial update
        ConflictResolution partialUpdatedConflictResolution = new ConflictResolution();
        partialUpdatedConflictResolution.setId(conflictResolution.getId());

        partialUpdatedConflictResolution
            .fieldName(UPDATED_FIELD_NAME)
            .localValue(UPDATED_LOCAL_VALUE)
            .remoteValue(UPDATED_REMOTE_VALUE)
            .isResolved(UPDATED_IS_RESOLVED)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .conflictDetectedAt(UPDATED_CONFLICT_DETECTED_AT);

        restConflictResolutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConflictResolution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConflictResolution))
            )
            .andExpect(status().isOk());

        // Validate the ConflictResolution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConflictResolutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConflictResolution, conflictResolution),
            getPersistedConflictResolution(conflictResolution)
        );
    }

    @Test
    @Transactional
    void fullUpdateConflictResolutionWithPatch() throws Exception {
        // Initialize the database
        insertedConflictResolution = conflictResolutionRepository.saveAndFlush(conflictResolution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conflictResolution using partial update
        ConflictResolution partialUpdatedConflictResolution = new ConflictResolution();
        partialUpdatedConflictResolution.setId(conflictResolution.getId());

        partialUpdatedConflictResolution
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .fieldName(UPDATED_FIELD_NAME)
            .localValue(UPDATED_LOCAL_VALUE)
            .remoteValue(UPDATED_REMOTE_VALUE)
            .resolvedValue(UPDATED_RESOLVED_VALUE)
            .resolutionStrategy(UPDATED_RESOLUTION_STRATEGY)
            .isResolved(UPDATED_IS_RESOLVED)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .conflictDetectedAt(UPDATED_CONFLICT_DETECTED_AT)
            .createdAt(UPDATED_CREATED_AT);

        restConflictResolutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConflictResolution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConflictResolution))
            )
            .andExpect(status().isOk());

        // Validate the ConflictResolution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConflictResolutionUpdatableFieldsEquals(
            partialUpdatedConflictResolution,
            getPersistedConflictResolution(partialUpdatedConflictResolution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConflictResolution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conflictResolution.setId(longCount.incrementAndGet());

        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConflictResolutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conflictResolutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conflictResolutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConflictResolution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conflictResolution.setId(longCount.incrementAndGet());

        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConflictResolutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conflictResolutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConflictResolution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conflictResolution.setId(longCount.incrementAndGet());

        // Create the ConflictResolution
        ConflictResolutionDTO conflictResolutionDTO = conflictResolutionMapper.toDto(conflictResolution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConflictResolutionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(conflictResolutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConflictResolution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConflictResolution() throws Exception {
        // Initialize the database
        insertedConflictResolution = conflictResolutionRepository.saveAndFlush(conflictResolution);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the conflictResolution
        restConflictResolutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, conflictResolution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return conflictResolutionRepository.count();
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

    protected ConflictResolution getPersistedConflictResolution(ConflictResolution conflictResolution) {
        return conflictResolutionRepository.findById(conflictResolution.getId()).orElseThrow();
    }

    protected void assertPersistedConflictResolutionToMatchAllProperties(ConflictResolution expectedConflictResolution) {
        assertConflictResolutionAllPropertiesEquals(expectedConflictResolution, getPersistedConflictResolution(expectedConflictResolution));
    }

    protected void assertPersistedConflictResolutionToMatchUpdatableProperties(ConflictResolution expectedConflictResolution) {
        assertConflictResolutionAllUpdatablePropertiesEquals(
            expectedConflictResolution,
            getPersistedConflictResolution(expectedConflictResolution)
        );
    }
}
