package com.magnus.web.rest;

import static com.magnus.domain.AccommodationAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Accommodation;
import com.magnus.domain.enumeration.AccommodationType;
import com.magnus.repository.AccommodationRepository;
import com.magnus.service.dto.AccommodationDTO;
import com.magnus.service.mapper.AccommodationMapper;
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
 * Integration tests for the {@link AccommodationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccommodationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final AccommodationType DEFAULT_TYPE = AccommodationType.SINGLE;
    private static final AccommodationType UPDATED_TYPE = AccommodationType.DOUBLE;

    private static final BigDecimal DEFAULT_PRICE_PER_NIGHT = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_PER_NIGHT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST_PER_NIGHT = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST_PER_NIGHT = new BigDecimal(1);

    private static final Integer DEFAULT_MAX_OCCUPANCY = 1;
    private static final Integer UPDATED_MAX_OCCUPANCY = 2;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_AMENITIES = "AAAAAAAAAA";
    private static final String UPDATED_AMENITIES = "BBBBBBBBBB";

    private static final String DEFAULT_CHECK_IN_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_IN_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_CHECK_OUT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_OUT_TIME = "BBBBBBBBBB";

    private static final Double DEFAULT_RATING = 0D;
    private static final Double UPDATED_RATING = 1D;

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

    private static final String ENTITY_API_URL = "/api/accommodations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private AccommodationMapper accommodationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccommodationMockMvc;

    private Accommodation accommodation;

    private Accommodation insertedAccommodation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accommodation createEntity() {
        return new Accommodation()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .pricePerNight(DEFAULT_PRICE_PER_NIGHT)
            .costPerNight(DEFAULT_COST_PER_NIGHT)
            .maxOccupancy(DEFAULT_MAX_OCCUPANCY)
            .address(DEFAULT_ADDRESS)
            .amenities(DEFAULT_AMENITIES)
            .checkInTime(DEFAULT_CHECK_IN_TIME)
            .checkOutTime(DEFAULT_CHECK_OUT_TIME)
            .rating(DEFAULT_RATING)
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
    public static Accommodation createUpdatedEntity() {
        return new Accommodation()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerNight(UPDATED_PRICE_PER_NIGHT)
            .costPerNight(UPDATED_COST_PER_NIGHT)
            .maxOccupancy(UPDATED_MAX_OCCUPANCY)
            .address(UPDATED_ADDRESS)
            .amenities(UPDATED_AMENITIES)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .rating(UPDATED_RATING)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        accommodation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAccommodation != null) {
            accommodationRepository.delete(insertedAccommodation);
            insertedAccommodation = null;
        }
    }

    @Test
    @Transactional
    void createAccommodation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);
        var returnedAccommodationDTO = om.readValue(
            restAccommodationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccommodationDTO.class
        );

        // Validate the Accommodation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAccommodation = accommodationMapper.toEntity(returnedAccommodationDTO);
        assertAccommodationUpdatableFieldsEquals(returnedAccommodation, getPersistedAccommodation(returnedAccommodation));

        insertedAccommodation = returnedAccommodation;
    }

    @Test
    @Transactional
    void createAccommodationWithExistingId() throws Exception {
        // Create the Accommodation with an existing ID
        accommodation.setId(1L);
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setName(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setType(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerNightIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setPricePerNight(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxOccupancyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setMaxOccupancy(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setIsActive(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setIsTemplate(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setCreatedAt(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accommodation.setUpdatedAt(null);

        // Create the Accommodation, which fails.
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        restAccommodationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccommodations() throws Exception {
        // Initialize the database
        insertedAccommodation = accommodationRepository.saveAndFlush(accommodation);

        // Get all the accommodationList
        restAccommodationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accommodation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pricePerNight").value(hasItem(sameNumber(DEFAULT_PRICE_PER_NIGHT))))
            .andExpect(jsonPath("$.[*].costPerNight").value(hasItem(sameNumber(DEFAULT_COST_PER_NIGHT))))
            .andExpect(jsonPath("$.[*].maxOccupancy").value(hasItem(DEFAULT_MAX_OCCUPANCY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].amenities").value(hasItem(DEFAULT_AMENITIES)))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(DEFAULT_CHECK_IN_TIME)))
            .andExpect(jsonPath("$.[*].checkOutTime").value(hasItem(DEFAULT_CHECK_OUT_TIME)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].contactInfo").value(hasItem(DEFAULT_CONTACT_INFO)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isTemplate").value(hasItem(DEFAULT_IS_TEMPLATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAccommodation() throws Exception {
        // Initialize the database
        insertedAccommodation = accommodationRepository.saveAndFlush(accommodation);

        // Get the accommodation
        restAccommodationMockMvc
            .perform(get(ENTITY_API_URL_ID, accommodation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accommodation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.pricePerNight").value(sameNumber(DEFAULT_PRICE_PER_NIGHT)))
            .andExpect(jsonPath("$.costPerNight").value(sameNumber(DEFAULT_COST_PER_NIGHT)))
            .andExpect(jsonPath("$.maxOccupancy").value(DEFAULT_MAX_OCCUPANCY))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.amenities").value(DEFAULT_AMENITIES))
            .andExpect(jsonPath("$.checkInTime").value(DEFAULT_CHECK_IN_TIME))
            .andExpect(jsonPath("$.checkOutTime").value(DEFAULT_CHECK_OUT_TIME))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.contactInfo").value(DEFAULT_CONTACT_INFO))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isTemplate").value(DEFAULT_IS_TEMPLATE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccommodation() throws Exception {
        // Get the accommodation
        restAccommodationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccommodation() throws Exception {
        // Initialize the database
        insertedAccommodation = accommodationRepository.saveAndFlush(accommodation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accommodation
        Accommodation updatedAccommodation = accommodationRepository.findById(accommodation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccommodation are not directly saved in db
        em.detach(updatedAccommodation);
        updatedAccommodation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerNight(UPDATED_PRICE_PER_NIGHT)
            .costPerNight(UPDATED_COST_PER_NIGHT)
            .maxOccupancy(UPDATED_MAX_OCCUPANCY)
            .address(UPDATED_ADDRESS)
            .amenities(UPDATED_AMENITIES)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .rating(UPDATED_RATING)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(updatedAccommodation);

        restAccommodationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accommodationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accommodationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccommodationToMatchAllProperties(updatedAccommodation);
    }

    @Test
    @Transactional
    void putNonExistingAccommodation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accommodation.setId(longCount.incrementAndGet());

        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccommodationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accommodationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accommodationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccommodation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accommodation.setId(longCount.incrementAndGet());

        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccommodationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accommodationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccommodation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accommodation.setId(longCount.incrementAndGet());

        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccommodationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccommodationWithPatch() throws Exception {
        // Initialize the database
        insertedAccommodation = accommodationRepository.saveAndFlush(accommodation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accommodation using partial update
        Accommodation partialUpdatedAccommodation = new Accommodation();
        partialUpdatedAccommodation.setId(accommodation.getId());

        partialUpdatedAccommodation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pricePerNight(UPDATED_PRICE_PER_NIGHT)
            .costPerNight(UPDATED_COST_PER_NIGHT)
            .amenities(UPDATED_AMENITIES)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .rating(UPDATED_RATING)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAccommodationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccommodation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccommodation))
            )
            .andExpect(status().isOk());

        // Validate the Accommodation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccommodationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccommodation, accommodation),
            getPersistedAccommodation(accommodation)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccommodationWithPatch() throws Exception {
        // Initialize the database
        insertedAccommodation = accommodationRepository.saveAndFlush(accommodation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accommodation using partial update
        Accommodation partialUpdatedAccommodation = new Accommodation();
        partialUpdatedAccommodation.setId(accommodation.getId());

        partialUpdatedAccommodation
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .pricePerNight(UPDATED_PRICE_PER_NIGHT)
            .costPerNight(UPDATED_COST_PER_NIGHT)
            .maxOccupancy(UPDATED_MAX_OCCUPANCY)
            .address(UPDATED_ADDRESS)
            .amenities(UPDATED_AMENITIES)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOutTime(UPDATED_CHECK_OUT_TIME)
            .rating(UPDATED_RATING)
            .contactInfo(UPDATED_CONTACT_INFO)
            .isActive(UPDATED_IS_ACTIVE)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAccommodationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccommodation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccommodation))
            )
            .andExpect(status().isOk());

        // Validate the Accommodation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccommodationUpdatableFieldsEquals(partialUpdatedAccommodation, getPersistedAccommodation(partialUpdatedAccommodation));
    }

    @Test
    @Transactional
    void patchNonExistingAccommodation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accommodation.setId(longCount.incrementAndGet());

        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccommodationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accommodationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accommodationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccommodation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accommodation.setId(longCount.incrementAndGet());

        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccommodationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accommodationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccommodation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accommodation.setId(longCount.incrementAndGet());

        // Create the Accommodation
        AccommodationDTO accommodationDTO = accommodationMapper.toDto(accommodation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccommodationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accommodationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accommodation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccommodation() throws Exception {
        // Initialize the database
        insertedAccommodation = accommodationRepository.saveAndFlush(accommodation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accommodation
        restAccommodationMockMvc
            .perform(delete(ENTITY_API_URL_ID, accommodation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accommodationRepository.count();
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

    protected Accommodation getPersistedAccommodation(Accommodation accommodation) {
        return accommodationRepository.findById(accommodation.getId()).orElseThrow();
    }

    protected void assertPersistedAccommodationToMatchAllProperties(Accommodation expectedAccommodation) {
        assertAccommodationAllPropertiesEquals(expectedAccommodation, getPersistedAccommodation(expectedAccommodation));
    }

    protected void assertPersistedAccommodationToMatchUpdatableProperties(Accommodation expectedAccommodation) {
        assertAccommodationAllUpdatablePropertiesEquals(expectedAccommodation, getPersistedAccommodation(expectedAccommodation));
    }
}
