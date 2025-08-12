package com.magnus.web.rest;

import static com.magnus.domain.TransportAssignmentAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Transport;
import com.magnus.domain.TransportAssignment;
import com.magnus.repository.TransportAssignmentRepository;
import com.magnus.service.dto.TransportAssignmentDTO;
import com.magnus.service.mapper.TransportAssignmentMapper;
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
 * Integration tests for the {@link TransportAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransportAssignmentResourceIT {

    private static final Integer DEFAULT_GUEST_COUNT = 1;
    private static final Integer UPDATED_GUEST_COUNT = 2;

    private static final Double DEFAULT_DURATION = 0D;
    private static final Double UPDATED_DURATION = 1D;

    private static final Double DEFAULT_DISTANCE = 0D;
    private static final Double UPDATED_DISTANCE = 1D;

    private static final String DEFAULT_PICKUP_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DROPOFF_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_DROPOFF_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_PICKUP_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PICKUP_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RETURN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RETURN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_CALCULATED_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_CALCULATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_CALCULATED_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_CALCULATED_COST = new BigDecimal(1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transport-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransportAssignmentRepository transportAssignmentRepository;

    @Autowired
    private TransportAssignmentMapper transportAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransportAssignmentMockMvc;

    private TransportAssignment transportAssignment;

    private TransportAssignment insertedTransportAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransportAssignment createEntity(EntityManager em) {
        TransportAssignment transportAssignment = new TransportAssignment()
            .guestCount(DEFAULT_GUEST_COUNT)
            .duration(DEFAULT_DURATION)
            .distance(DEFAULT_DISTANCE)
            .pickupLocation(DEFAULT_PICKUP_LOCATION)
            .dropoffLocation(DEFAULT_DROPOFF_LOCATION)
            .pickupTime(DEFAULT_PICKUP_TIME)
            .returnTime(DEFAULT_RETURN_TIME)
            .calculatedPrice(DEFAULT_CALCULATED_PRICE)
            .calculatedCost(DEFAULT_CALCULATED_COST)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Transport transport;
        if (TestUtil.findAll(em, Transport.class).isEmpty()) {
            transport = TransportResourceIT.createEntity();
            em.persist(transport);
            em.flush();
        } else {
            transport = TestUtil.findAll(em, Transport.class).get(0);
        }
        transportAssignment.setTransport(transport);
        return transportAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransportAssignment createUpdatedEntity(EntityManager em) {
        TransportAssignment updatedTransportAssignment = new TransportAssignment()
            .guestCount(UPDATED_GUEST_COUNT)
            .duration(UPDATED_DURATION)
            .distance(UPDATED_DISTANCE)
            .pickupLocation(UPDATED_PICKUP_LOCATION)
            .dropoffLocation(UPDATED_DROPOFF_LOCATION)
            .pickupTime(UPDATED_PICKUP_TIME)
            .returnTime(UPDATED_RETURN_TIME)
            .calculatedPrice(UPDATED_CALCULATED_PRICE)
            .calculatedCost(UPDATED_CALCULATED_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Transport transport;
        if (TestUtil.findAll(em, Transport.class).isEmpty()) {
            transport = TransportResourceIT.createUpdatedEntity();
            em.persist(transport);
            em.flush();
        } else {
            transport = TestUtil.findAll(em, Transport.class).get(0);
        }
        updatedTransportAssignment.setTransport(transport);
        return updatedTransportAssignment;
    }

    @BeforeEach
    void initTest() {
        transportAssignment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTransportAssignment != null) {
            transportAssignmentRepository.delete(insertedTransportAssignment);
            insertedTransportAssignment = null;
        }
    }

    @Test
    @Transactional
    void createTransportAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);
        var returnedTransportAssignmentDTO = om.readValue(
            restTransportAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransportAssignmentDTO.class
        );

        // Validate the TransportAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransportAssignment = transportAssignmentMapper.toEntity(returnedTransportAssignmentDTO);
        assertTransportAssignmentUpdatableFieldsEquals(
            returnedTransportAssignment,
            getPersistedTransportAssignment(returnedTransportAssignment)
        );

        insertedTransportAssignment = returnedTransportAssignment;
    }

    @Test
    @Transactional
    void createTransportAssignmentWithExistingId() throws Exception {
        // Create the TransportAssignment with an existing ID
        transportAssignment.setId(1L);
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGuestCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transportAssignment.setGuestCount(null);

        // Create the TransportAssignment, which fails.
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        restTransportAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transportAssignment.setDuration(null);

        // Create the TransportAssignment, which fails.
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        restTransportAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCalculatedPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transportAssignment.setCalculatedPrice(null);

        // Create the TransportAssignment, which fails.
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        restTransportAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transportAssignment.setCreatedAt(null);

        // Create the TransportAssignment, which fails.
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        restTransportAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transportAssignment.setUpdatedAt(null);

        // Create the TransportAssignment, which fails.
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        restTransportAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransportAssignments() throws Exception {
        // Initialize the database
        insertedTransportAssignment = transportAssignmentRepository.saveAndFlush(transportAssignment);

        // Get all the transportAssignmentList
        restTransportAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].guestCount").value(hasItem(DEFAULT_GUEST_COUNT)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].pickupLocation").value(hasItem(DEFAULT_PICKUP_LOCATION)))
            .andExpect(jsonPath("$.[*].dropoffLocation").value(hasItem(DEFAULT_DROPOFF_LOCATION)))
            .andExpect(jsonPath("$.[*].pickupTime").value(hasItem(DEFAULT_PICKUP_TIME.toString())))
            .andExpect(jsonPath("$.[*].returnTime").value(hasItem(DEFAULT_RETURN_TIME.toString())))
            .andExpect(jsonPath("$.[*].calculatedPrice").value(hasItem(sameNumber(DEFAULT_CALCULATED_PRICE))))
            .andExpect(jsonPath("$.[*].calculatedCost").value(hasItem(sameNumber(DEFAULT_CALCULATED_COST))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTransportAssignment() throws Exception {
        // Initialize the database
        insertedTransportAssignment = transportAssignmentRepository.saveAndFlush(transportAssignment);

        // Get the transportAssignment
        restTransportAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, transportAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transportAssignment.getId().intValue()))
            .andExpect(jsonPath("$.guestCount").value(DEFAULT_GUEST_COUNT))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE))
            .andExpect(jsonPath("$.pickupLocation").value(DEFAULT_PICKUP_LOCATION))
            .andExpect(jsonPath("$.dropoffLocation").value(DEFAULT_DROPOFF_LOCATION))
            .andExpect(jsonPath("$.pickupTime").value(DEFAULT_PICKUP_TIME.toString()))
            .andExpect(jsonPath("$.returnTime").value(DEFAULT_RETURN_TIME.toString()))
            .andExpect(jsonPath("$.calculatedPrice").value(sameNumber(DEFAULT_CALCULATED_PRICE)))
            .andExpect(jsonPath("$.calculatedCost").value(sameNumber(DEFAULT_CALCULATED_COST)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransportAssignment() throws Exception {
        // Get the transportAssignment
        restTransportAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransportAssignment() throws Exception {
        // Initialize the database
        insertedTransportAssignment = transportAssignmentRepository.saveAndFlush(transportAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transportAssignment
        TransportAssignment updatedTransportAssignment = transportAssignmentRepository.findById(transportAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransportAssignment are not directly saved in db
        em.detach(updatedTransportAssignment);
        updatedTransportAssignment
            .guestCount(UPDATED_GUEST_COUNT)
            .duration(UPDATED_DURATION)
            .distance(UPDATED_DISTANCE)
            .pickupLocation(UPDATED_PICKUP_LOCATION)
            .dropoffLocation(UPDATED_DROPOFF_LOCATION)
            .pickupTime(UPDATED_PICKUP_TIME)
            .returnTime(UPDATED_RETURN_TIME)
            .calculatedPrice(UPDATED_CALCULATED_PRICE)
            .calculatedCost(UPDATED_CALCULATED_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(updatedTransportAssignment);

        restTransportAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransportAssignmentToMatchAllProperties(updatedTransportAssignment);
    }

    @Test
    @Transactional
    void putNonExistingTransportAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportAssignment.setId(longCount.incrementAndGet());

        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransportAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportAssignment.setId(longCount.incrementAndGet());

        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransportAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportAssignment.setId(longCount.incrementAndGet());

        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransportAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedTransportAssignment = transportAssignmentRepository.saveAndFlush(transportAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transportAssignment using partial update
        TransportAssignment partialUpdatedTransportAssignment = new TransportAssignment();
        partialUpdatedTransportAssignment.setId(transportAssignment.getId());

        partialUpdatedTransportAssignment
            .duration(UPDATED_DURATION)
            .distance(UPDATED_DISTANCE)
            .dropoffLocation(UPDATED_DROPOFF_LOCATION)
            .calculatedPrice(UPDATED_CALCULATED_PRICE)
            .calculatedCost(UPDATED_CALCULATED_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTransportAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransportAssignment))
            )
            .andExpect(status().isOk());

        // Validate the TransportAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransportAssignment, transportAssignment),
            getPersistedTransportAssignment(transportAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransportAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedTransportAssignment = transportAssignmentRepository.saveAndFlush(transportAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transportAssignment using partial update
        TransportAssignment partialUpdatedTransportAssignment = new TransportAssignment();
        partialUpdatedTransportAssignment.setId(transportAssignment.getId());

        partialUpdatedTransportAssignment
            .guestCount(UPDATED_GUEST_COUNT)
            .duration(UPDATED_DURATION)
            .distance(UPDATED_DISTANCE)
            .pickupLocation(UPDATED_PICKUP_LOCATION)
            .dropoffLocation(UPDATED_DROPOFF_LOCATION)
            .pickupTime(UPDATED_PICKUP_TIME)
            .returnTime(UPDATED_RETURN_TIME)
            .calculatedPrice(UPDATED_CALCULATED_PRICE)
            .calculatedCost(UPDATED_CALCULATED_COST)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTransportAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransportAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransportAssignment))
            )
            .andExpect(status().isOk());

        // Validate the TransportAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportAssignmentUpdatableFieldsEquals(
            partialUpdatedTransportAssignment,
            getPersistedTransportAssignment(partialUpdatedTransportAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransportAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportAssignment.setId(longCount.incrementAndGet());

        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transportAssignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransportAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportAssignment.setId(longCount.incrementAndGet());

        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransportAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transportAssignment.setId(longCount.incrementAndGet());

        // Create the TransportAssignment
        TransportAssignmentDTO transportAssignmentDTO = transportAssignmentMapper.toDto(transportAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transportAssignmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransportAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransportAssignment() throws Exception {
        // Initialize the database
        insertedTransportAssignment = transportAssignmentRepository.saveAndFlush(transportAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transportAssignment
        restTransportAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, transportAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transportAssignmentRepository.count();
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

    protected TransportAssignment getPersistedTransportAssignment(TransportAssignment transportAssignment) {
        return transportAssignmentRepository.findById(transportAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedTransportAssignmentToMatchAllProperties(TransportAssignment expectedTransportAssignment) {
        assertTransportAssignmentAllPropertiesEquals(
            expectedTransportAssignment,
            getPersistedTransportAssignment(expectedTransportAssignment)
        );
    }

    protected void assertPersistedTransportAssignmentToMatchUpdatableProperties(TransportAssignment expectedTransportAssignment) {
        assertTransportAssignmentAllUpdatablePropertiesEquals(
            expectedTransportAssignment,
            getPersistedTransportAssignment(expectedTransportAssignment)
        );
    }
}
