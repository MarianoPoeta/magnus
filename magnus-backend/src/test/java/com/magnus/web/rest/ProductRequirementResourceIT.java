package com.magnus.web.rest;

import static com.magnus.domain.ProductRequirementAsserts.*;
import static com.magnus.web.rest.TestUtil.createUpdateProxyForBean;
import static com.magnus.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magnus.IntegrationTest;
import com.magnus.domain.Product;
import com.magnus.domain.ProductRequirement;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.ProductUnit;
import com.magnus.repository.ProductRequirementRepository;
import com.magnus.service.dto.ProductRequirementDTO;
import com.magnus.service.mapper.ProductRequirementMapper;
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
 * Integration tests for the {@link ProductRequirementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductRequirementResourceIT {

    private static final Double DEFAULT_QUANTITY = 0D;
    private static final Double UPDATED_QUANTITY = 1D;

    private static final ProductUnit DEFAULT_UNIT = ProductUnit.KG;
    private static final ProductUnit UPDATED_UNIT = ProductUnit.G;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ESTIMATED_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ESTIMATED_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ACTUAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACTUAL_COST = new BigDecimal(1);

    private static final Boolean DEFAULT_IS_PURCHASED = false;
    private static final Boolean UPDATED_IS_PURCHASED = true;

    private static final String DEFAULT_PURCHASED_BY = "AAAAAAAAAA";
    private static final String UPDATED_PURCHASED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_PURCHASED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PURCHASED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final ConflictStatus DEFAULT_CONFLICT_STATUS = ConflictStatus.NONE;
    private static final ConflictStatus UPDATED_CONFLICT_STATUS = ConflictStatus.DETECTED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/product-requirements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRequirementRepository productRequirementRepository;

    @Autowired
    private ProductRequirementMapper productRequirementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductRequirementMockMvc;

    private ProductRequirement productRequirement;

    private ProductRequirement insertedProductRequirement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRequirement createEntity(EntityManager em) {
        ProductRequirement productRequirement = new ProductRequirement()
            .quantity(DEFAULT_QUANTITY)
            .unit(DEFAULT_UNIT)
            .notes(DEFAULT_NOTES)
            .estimatedCost(DEFAULT_ESTIMATED_COST)
            .actualCost(DEFAULT_ACTUAL_COST)
            .isPurchased(DEFAULT_IS_PURCHASED)
            .purchasedBy(DEFAULT_PURCHASED_BY)
            .purchasedAt(DEFAULT_PURCHASED_AT)
            .version(DEFAULT_VERSION)
            .conflictStatus(DEFAULT_CONFLICT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productRequirement.setProduct(product);
        return productRequirement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductRequirement createUpdatedEntity(EntityManager em) {
        ProductRequirement updatedProductRequirement = new ProductRequirement()
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .notes(UPDATED_NOTES)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedBy(UPDATED_PURCHASED_BY)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedProductRequirement.setProduct(product);
        return updatedProductRequirement;
    }

    @BeforeEach
    void initTest() {
        productRequirement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductRequirement != null) {
            productRequirementRepository.delete(insertedProductRequirement);
            insertedProductRequirement = null;
        }
    }

    @Test
    @Transactional
    void createProductRequirement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);
        var returnedProductRequirementDTO = om.readValue(
            restProductRequirementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductRequirementDTO.class
        );

        // Validate the ProductRequirement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductRequirement = productRequirementMapper.toEntity(returnedProductRequirementDTO);
        assertProductRequirementUpdatableFieldsEquals(
            returnedProductRequirement,
            getPersistedProductRequirement(returnedProductRequirement)
        );

        insertedProductRequirement = returnedProductRequirement;
    }

    @Test
    @Transactional
    void createProductRequirementWithExistingId() throws Exception {
        // Create the ProductRequirement with an existing ID
        productRequirement.setId(1L);
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setQuantity(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setUnit(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPurchasedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setIsPurchased(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setVersion(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConflictStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setConflictStatus(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setCreatedAt(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productRequirement.setUpdatedAt(null);

        // Create the ProductRequirement, which fails.
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        restProductRequirementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductRequirements() throws Exception {
        // Initialize the database
        insertedProductRequirement = productRequirementRepository.saveAndFlush(productRequirement);

        // Get all the productRequirementList
        restProductRequirementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productRequirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].estimatedCost").value(hasItem(sameNumber(DEFAULT_ESTIMATED_COST))))
            .andExpect(jsonPath("$.[*].actualCost").value(hasItem(sameNumber(DEFAULT_ACTUAL_COST))))
            .andExpect(jsonPath("$.[*].isPurchased").value(hasItem(DEFAULT_IS_PURCHASED)))
            .andExpect(jsonPath("$.[*].purchasedBy").value(hasItem(DEFAULT_PURCHASED_BY)))
            .andExpect(jsonPath("$.[*].purchasedAt").value(hasItem(DEFAULT_PURCHASED_AT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].conflictStatus").value(hasItem(DEFAULT_CONFLICT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getProductRequirement() throws Exception {
        // Initialize the database
        insertedProductRequirement = productRequirementRepository.saveAndFlush(productRequirement);

        // Get the productRequirement
        restProductRequirementMockMvc
            .perform(get(ENTITY_API_URL_ID, productRequirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productRequirement.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.estimatedCost").value(sameNumber(DEFAULT_ESTIMATED_COST)))
            .andExpect(jsonPath("$.actualCost").value(sameNumber(DEFAULT_ACTUAL_COST)))
            .andExpect(jsonPath("$.isPurchased").value(DEFAULT_IS_PURCHASED))
            .andExpect(jsonPath("$.purchasedBy").value(DEFAULT_PURCHASED_BY))
            .andExpect(jsonPath("$.purchasedAt").value(DEFAULT_PURCHASED_AT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.conflictStatus").value(DEFAULT_CONFLICT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductRequirement() throws Exception {
        // Get the productRequirement
        restProductRequirementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductRequirement() throws Exception {
        // Initialize the database
        insertedProductRequirement = productRequirementRepository.saveAndFlush(productRequirement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productRequirement
        ProductRequirement updatedProductRequirement = productRequirementRepository.findById(productRequirement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductRequirement are not directly saved in db
        em.detach(updatedProductRequirement);
        updatedProductRequirement
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .notes(UPDATED_NOTES)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedBy(UPDATED_PURCHASED_BY)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(updatedProductRequirement);

        restProductRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productRequirementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productRequirementDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductRequirementToMatchAllProperties(updatedProductRequirement);
    }

    @Test
    @Transactional
    void putNonExistingProductRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRequirement.setId(longCount.incrementAndGet());

        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productRequirementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRequirement.setId(longCount.incrementAndGet());

        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRequirementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRequirement.setId(longCount.incrementAndGet());

        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRequirementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductRequirementWithPatch() throws Exception {
        // Initialize the database
        insertedProductRequirement = productRequirementRepository.saveAndFlush(productRequirement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productRequirement using partial update
        ProductRequirement partialUpdatedProductRequirement = new ProductRequirement();
        partialUpdatedProductRequirement.setId(productRequirement.getId());

        partialUpdatedProductRequirement
            .quantity(UPDATED_QUANTITY)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .version(UPDATED_VERSION)
            .createdAt(UPDATED_CREATED_AT);

        restProductRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRequirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductRequirement))
            )
            .andExpect(status().isOk());

        // Validate the ProductRequirement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductRequirementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductRequirement, productRequirement),
            getPersistedProductRequirement(productRequirement)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductRequirementWithPatch() throws Exception {
        // Initialize the database
        insertedProductRequirement = productRequirementRepository.saveAndFlush(productRequirement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productRequirement using partial update
        ProductRequirement partialUpdatedProductRequirement = new ProductRequirement();
        partialUpdatedProductRequirement.setId(productRequirement.getId());

        partialUpdatedProductRequirement
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .notes(UPDATED_NOTES)
            .estimatedCost(UPDATED_ESTIMATED_COST)
            .actualCost(UPDATED_ACTUAL_COST)
            .isPurchased(UPDATED_IS_PURCHASED)
            .purchasedBy(UPDATED_PURCHASED_BY)
            .purchasedAt(UPDATED_PURCHASED_AT)
            .version(UPDATED_VERSION)
            .conflictStatus(UPDATED_CONFLICT_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restProductRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductRequirement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductRequirement))
            )
            .andExpect(status().isOk());

        // Validate the ProductRequirement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductRequirementUpdatableFieldsEquals(
            partialUpdatedProductRequirement,
            getPersistedProductRequirement(partialUpdatedProductRequirement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRequirement.setId(longCount.incrementAndGet());

        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productRequirementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRequirement.setId(longCount.incrementAndGet());

        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRequirementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productRequirementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductRequirement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productRequirement.setId(longCount.incrementAndGet());

        // Create the ProductRequirement
        ProductRequirementDTO productRequirementDTO = productRequirementMapper.toDto(productRequirement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductRequirementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productRequirementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductRequirement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductRequirement() throws Exception {
        // Initialize the database
        insertedProductRequirement = productRequirementRepository.saveAndFlush(productRequirement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productRequirement
        restProductRequirementMockMvc
            .perform(delete(ENTITY_API_URL_ID, productRequirement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRequirementRepository.count();
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

    protected ProductRequirement getPersistedProductRequirement(ProductRequirement productRequirement) {
        return productRequirementRepository.findById(productRequirement.getId()).orElseThrow();
    }

    protected void assertPersistedProductRequirementToMatchAllProperties(ProductRequirement expectedProductRequirement) {
        assertProductRequirementAllPropertiesEquals(expectedProductRequirement, getPersistedProductRequirement(expectedProductRequirement));
    }

    protected void assertPersistedProductRequirementToMatchUpdatableProperties(ProductRequirement expectedProductRequirement) {
        assertProductRequirementAllUpdatablePropertiesEquals(
            expectedProductRequirement,
            getPersistedProductRequirement(expectedProductRequirement)
        );
    }
}
