package com.magnus.web.rest;

import static com.magnus.domain.TransportAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Transport;
import com.magnus.domain.enumeration.VehicleType;
import com.magnus.repository.TransportRepository;
import com.magnus.service.dto.TransportDTO;
import com.magnus.service.mapper.TransportMapper;
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
 * Integration tests for the {@link TransportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final VehicleType DEFAULT_VEHICLE_TYPE = VehicleType.BUS;
    private static final VehicleType UPDATED_VEHICLE_TYPE = VehicleType.MINIVAN;

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final BigDecimal DEFAULT_PRICE_PER_HOUR = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_PER_HOUR = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRICE_PER_KM = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_PER_KM = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST_PER_HOUR = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST_PER_HOUR = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST_PER_KM = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST_PER_KM = new BigDecimal(1);

    private static final Boolean DEFAULT_INCLUDES_DRIVER = false;
    private static final Boolean UPDATED_INCLUDES_DRIVER = true;

    private static final BigDecimal DEFAULT_DRIVER_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_DRIVER_COST = new BigDecimal(1);

    private static final String DEFAULT_FUEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FUEL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_INFO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_TEMPLATE = false;
    private static final Boolean UPDATED_IS_TEMPLATE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private TransportMapper transportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransportMockMvc;

    private Transport transport;

    private Transport insertedTransport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transport createEntity() {
        return new Transport()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .capacity(DEFAULT_CAPACITY)
            .pricePerHour(DEFAULT_PRICE_PER_HOUR)
            .pricePerKm(DEFAULT_PRICE_PER_KM)
            .costPerHour(DEFAULT_COST_PER_HOUR)
            .costPerKm(DEFAULT_COST_PER_KM)
            .includesDriver(DEFAULT_INCLUDES_DRIVER)
            .driverCost(DEFAULT_DRIVER_COST)
            .fuelType(DEFAULT_FUEL_TYPE)
            .contactInfo(DEFAULT_CONTACT_INFO)
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
    public static Transport createUpdatedEntity() {
        return new Transport()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .capacity(UPDATED_CAPACITY)
            .pricePerHour(UPDATED_PRICE_PER_HOUR)
            .pricePerKm(UPDATED_PRICE_PER_KM)
            .costPerHour(UPDATED_COST_PER_HOUR)
            .costPerKm(UPDATED_COST_PER_KM)
            .includesDriver(UPDATED_INCLUDES_DRIVER)
            .driverCost(UPDATED_DRIVER_COST)
            .fuelType(UPDATED_FUEL_TYPE)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        transport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransport != null) {
            transportRepository.delete(insertedTransport);
            insertedTransport = null;
        }
    }

    @Test
    @Transactional
    void createTransport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);
        var returnedTransportDTO = om.readValue(
            restTransportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransportDTO.class
        );

        // Validate the Transport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransport = transportMapper.toEntity(returnedTransportDTO);
        assertTransportUpdatableFieldsEquals(returnedTransport, getPersistedTransport(returnedTransport));

        insertedTransport = returnedTransport;
    }

    @Test
    @Transactional
    void createTransportWithExistingId() throws Exception {
        // Create the Transport with an existing ID
        transport.setId(1L);
        TransportDTO transportDTO = transportMapper.toDto(transport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setName(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVehicleTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setVehicleType(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setCapacity(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerHourIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setPricePerHour(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncludesDriverIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setIncludesDriver(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setIsActive(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setIsTemplate(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setCreatedAt(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transport.setUpdatedAt(null);

        // Create the Transport, which fails.
        TransportDTO transportDTO = transportMapper.toDto(transport);

        restTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransports() throws Exception {
        // Initialize the database
        insertedTransport = transportRepository.saveAndFlush(transport);

        // Get all the transportList
        restTransportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].pricePerHour").value(hasItem(sameNumber(DEFAULT_PRICE_PER_HOUR))))
            .andExpect(jsonPath("$.[*].pricePerKm").value(hasItem(sameNumber(DEFAULT_PRICE_PER_KM))))
            .andExpect(jsonPath("$.[*].costPerHour").value(hasItem(sameNumber(DEFAULT_COST_PER_HOUR))))
            .andExpect(jsonPath("$.[*].costPerKm").value(hasItem(sameNumber(DEFAULT_COST_PER_KM))))
            .andExpect(jsonPath("$.[*].includesDriver").value(hasItem(DEFAULT_INCLUDES_DRIVER)))
            .andExpect(jsonPath("$.[*].driverCost").value(hasItem(sameNumber(DEFAULT_DRIVER_COST))))
            .andExpect(jsonPath("$.[*].fuelType").value(hasItem(DEFAULT_FUEL_TYPE)))
            .andExpect(jsonPath("$.[*].contactInfo").value(hasItem(DEFAULT_CONTACT_INFO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isTemplate").value(hasItem(DEFAULT_IS_TEMPLATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getTransport() throws Exception {
        // Initialize the database
        insertedTransport = transportRepository.saveAndFlush(transport);

        // Get the transport
        restTransportMockMvc
            .perform(get(ENTITY_API_URL_ID, transport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.pricePerHour").value(sameNumber(DEFAULT_PRICE_PER_HOUR)))
            .andExpect(jsonPath("$.pricePerKm").value(sameNumber(DEFAULT_PRICE_PER_KM)))
            .andExpect(jsonPath("$.costPerHour").value(sameNumber(DEFAULT_COST_PER_HOUR)))
            .andExpect(jsonPath("$.costPerKm").value(sameNumber(DEFAULT_COST_PER_KM)))
            .andExpect(jsonPath("$.includesDriver").value(DEFAULT_INCLUDES_DRIVER))
            .andExpect(jsonPath("$.driverCost").value(sameNumber(DEFAULT_DRIVER_COST)))
            .andExpect(jsonPath("$.fuelType").value(DEFAULT_FUEL_TYPE))
            .andExpect(jsonPath("$.contactInfo").value(DEFAULT_CONTACT_INFO))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isTemplate").value(DEFAULT_IS_TEMPLATE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransport() throws Exception {
        // Get the transport
        restTransportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransport() throws Exception {
        // Initialize the database
        insertedTransport = transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transport
        Transport updatedTransport = transportRepository.findById(transport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransport are not directly saved in db
        em.detach(updatedTransport);
        updatedTransport
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .capacity(UPDATED_CAPACITY)
            .pricePerHour(UPDATED_PRICE_PER_HOUR)
            .pricePerKm(UPDATED_PRICE_PER_KM)
            .costPerHour(UPDATED_COST_PER_HOUR)
            .costPerKm(UPDATED_COST_PER_KM)
            .includesDriver(UPDATED_INCLUDES_DRIVER)
            .driverCost(UPDATED_DRIVER_COST)
            .fuelType(UPDATED_FUEL_TYPE)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TransportDTO transportDTO = transportMapper.toDto(updatedTransport);

        restTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransportToMatchAllProperties(updatedTransport);
    }

    @Test
    @Transactional
    void putNonExistingTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransportWithPatch() throws Exception {
        // Initialize the database
        insertedTransport = transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transport using partial update
        Transport partialUpdatedTransport = new Transport();
        partialUpdatedTransport.setId(transport.getId());

        partialUpdatedTransport
            .description(UPDATED_DESCRIPTION)
            .capacity(UPDATED_CAPACITY)
            .pricePerHour(UPDATED_PRICE_PER_HOUR)
            .pricePerKm(UPDATED_PRICE_PER_KM)
            .includesDriver(UPDATED_INCLUDES_DRIVER)
            .fuelType(UPDATED_FUEL_TYPE)
            .contactInfo(UPDATED_CONTACT_INFO);

        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransport))
            )
            .andExpect(status().isOk());

        // Validate the Transport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransport, transport),
            getPersistedTransport(transport)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransportWithPatch() throws Exception {
        // Initialize the database
        insertedTransport = transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transport using partial update
        Transport partialUpdatedTransport = new Transport();
        partialUpdatedTransport.setId(transport.getId());

        partialUpdatedTransport
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .capacity(UPDATED_CAPACITY)
            .pricePerHour(UPDATED_PRICE_PER_HOUR)
            .pricePerKm(UPDATED_PRICE_PER_KM)
            .costPerHour(UPDATED_COST_PER_HOUR)
            .costPerKm(UPDATED_COST_PER_KM)
            .includesDriver(UPDATED_INCLUDES_DRIVER)
            .driverCost(UPDATED_DRIVER_COST)
            .fuelType(UPDATED_FUEL_TYPE)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransport))
            )
            .andExpect(status().isOk());

        // Validate the Transport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransportUpdatableFieldsEquals(partialUpdatedTransport, getPersistedTransport(partialUpdatedTransport));
    }

    @Test
    @Transactional
    void patchNonExistingTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transport.setId(longCount.incrementAndGet());

        // Create the Transport
        TransportDTO transportDTO = transportMapper.toDto(transport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransport() throws Exception {
        // Initialize the database
        insertedTransport = transportRepository.saveAndFlush(transport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transport
        restTransportMockMvc
            .perform(delete(ENTITY_API_URL_ID, transport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transportRepository.count();
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

    protected Transport getPersistedTransport(Transport transport) {
        return transportRepository.findById(transport.getId()).orElseThrow();
    }

    protected void assertPersistedTransportToMatchAllProperties(Transport expectedTransport) {
        assertTransportAllPropertiesEquals(expectedTransport, getPersistedTransport(expectedTransport));
    }

    protected void assertPersistedTransportToMatchUpdatableProperties(Transport expectedTransport) {
        assertTransportAllUpdatablePropertiesEquals(expectedTransport, getPersistedTransport(expectedTransport));
    }
}
