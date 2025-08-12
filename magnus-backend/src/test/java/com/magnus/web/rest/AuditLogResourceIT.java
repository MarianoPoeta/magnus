package com.magnus.web.rest;

import static com.magnus.domain.AuditLogAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.AuditLog;
import com.magnus.domain.enumeration.AuditAction;
import com.magnus.domain.enumeration.UserRole;
import com.magnus.repository.AuditLogRepository;
import com.magnus.service.dto.AuditLogDTO;
import com.magnus.service.mapper.AuditLogMapper;
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
 * Integration tests for the {@link AuditLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuditLogResourceIT {

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBBBBBBB";

    private static final AuditAction DEFAULT_ACTION = AuditAction.CREATE;
    private static final AuditAction UPDATED_ACTION = AuditAction.UPDATE;

    private static final String DEFAULT_OLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final UserRole DEFAULT_USER_ROLE = UserRole.ADMIN;
    private static final UserRole UPDATED_USER_ROLE = UserRole.SALES;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/audit-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditLogMockMvc;

    private AuditLog auditLog;

    private AuditLog insertedAuditLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditLog createEntity() {
        return new AuditLog()
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .action(DEFAULT_ACTION)
            .oldValue(DEFAULT_OLD_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .fieldName(DEFAULT_FIELD_NAME)
            .userId(DEFAULT_USER_ID)
            .userRole(DEFAULT_USER_ROLE)
            .timestamp(DEFAULT_TIMESTAMP)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .userAgent(DEFAULT_USER_AGENT)
            .sessionId(DEFAULT_SESSION_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditLog createUpdatedEntity() {
        return new AuditLog()
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .action(UPDATED_ACTION)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .fieldName(UPDATED_FIELD_NAME)
            .userId(UPDATED_USER_ID)
            .userRole(UPDATED_USER_ROLE)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .sessionId(UPDATED_SESSION_ID);
    }

    @BeforeEach
    void initTest() {
        auditLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAuditLog != null) {
            auditLogRepository.delete(insertedAuditLog);
            insertedAuditLog = null;
        }
    }

    @Test
    @Transactional
    void createAuditLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);
        var returnedAuditLogDTO = om.readValue(
            restAuditLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AuditLogDTO.class
        );

        // Validate the AuditLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuditLog = auditLogMapper.toEntity(returnedAuditLogDTO);
        assertAuditLogUpdatableFieldsEquals(returnedAuditLog, getPersistedAuditLog(returnedAuditLog));

        insertedAuditLog = returnedAuditLog;
    }

    @Test
    @Transactional
    void createAuditLogWithExistingId() throws Exception {
        // Create the AuditLog with an existing ID
        auditLog.setId(1L);
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setEntityType(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setEntityId(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setAction(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setTimestamp(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuditLogs() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userRole").value(hasItem(DEFAULT_USER_ROLE.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT)))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID)));
    }

    @Test
    @Transactional
    void getAuditLog() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get the auditLog
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL_ID, auditLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditLog.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.oldValue").value(DEFAULT_OLD_VALUE))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userRole").value(DEFAULT_USER_ROLE.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.userAgent").value(DEFAULT_USER_AGENT))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID));
    }

    @Test
    @Transactional
    void getNonExistingAuditLog() throws Exception {
        // Get the auditLog
        restAuditLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuditLog() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auditLog
        AuditLog updatedAuditLog = auditLogRepository.findById(auditLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAuditLog are not directly saved in db
        em.detach(updatedAuditLog);
        updatedAuditLog
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .action(UPDATED_ACTION)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .fieldName(UPDATED_FIELD_NAME)
            .userId(UPDATED_USER_ID)
            .userRole(UPDATED_USER_ROLE)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .sessionId(UPDATED_SESSION_ID);
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(updatedAuditLog);

        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuditLogToMatchAllProperties(updatedAuditLog);
    }

    @Test
    @Transactional
    void putNonExistingAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuditLogWithPatch() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auditLog using partial update
        AuditLog partialUpdatedAuditLog = new AuditLog();
        partialUpdatedAuditLog.setId(auditLog.getId());

        partialUpdatedAuditLog
            .entityType(UPDATED_ENTITY_TYPE)
            .fieldName(UPDATED_FIELD_NAME)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .sessionId(UPDATED_SESSION_ID);

        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuditLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAuditLog, auditLog), getPersistedAuditLog(auditLog));
    }

    @Test
    @Transactional
    void fullUpdateAuditLogWithPatch() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auditLog using partial update
        AuditLog partialUpdatedAuditLog = new AuditLog();
        partialUpdatedAuditLog.setId(auditLog.getId());

        partialUpdatedAuditLog
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .action(UPDATED_ACTION)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .fieldName(UPDATED_FIELD_NAME)
            .userId(UPDATED_USER_ID)
            .userRole(UPDATED_USER_ROLE)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .sessionId(UPDATED_SESSION_ID);

        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuditLogUpdatableFieldsEquals(partialUpdatedAuditLog, getPersistedAuditLog(partialUpdatedAuditLog));
    }

    @Test
    @Transactional
    void patchNonExistingAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuditLog() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the auditLog
        restAuditLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, auditLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return auditLogRepository.count();
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

    protected AuditLog getPersistedAuditLog(AuditLog auditLog) {
        return auditLogRepository.findById(auditLog.getId()).orElseThrow();
    }

    protected void assertPersistedAuditLogToMatchAllProperties(AuditLog expectedAuditLog) {
        assertAuditLogAllPropertiesEquals(expectedAuditLog, getPersistedAuditLog(expectedAuditLog));
    }

    protected void assertPersistedAuditLogToMatchUpdatableProperties(AuditLog expectedAuditLog) {
        assertAuditLogAllUpdatablePropertiesEquals(expectedAuditLog, getPersistedAuditLog(expectedAuditLog));
    }
}
