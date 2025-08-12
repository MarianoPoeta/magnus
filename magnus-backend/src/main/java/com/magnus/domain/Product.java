package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ProductCategory;
import com.magnus.domain.enumeration.ProductUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product catalog with supplier information
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private ProductUnit unit;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_per_unit", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerUnit;

    @DecimalMin(value = "0")
    @Column(name = "min_order_quantity")
    private Double minOrderQuantity;

    @DecimalMin(value = "0")
    @Column(name = "max_order_quantity")
    private Double maxOrderQuantity;

    @Size(max = 100)
    @Column(name = "supplier", length = 100)
    private String supplier;

    @Lob
    @Column(name = "supplier_contact")
    private String supplierContact;

    @Min(value = 0)
    @Column(name = "lead_time")
    private Integer leadTime;

    @Min(value = 0)
    @Column(name = "shelf_life")
    private Integer shelfLife;

    @Lob
    @Column(name = "storage_conditions")
    private String storageConditions;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_updated_price")
    private Instant lastUpdatedPrice;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "createdBudgets",
            "assignedBudgets",
            "createdTasks",
            "assignedTasks",
            "requestedNeeds",
            "fulfilledNeeds",
            "createdProducts",
            "createdNotifications",
            "receivedNotifications",
            "purchasedItems",
            "createdWeeklyPlans",
            "createdTemplates",
            "createdConfigs",
            "createdTriggers",
            "resolvedConflicts",
        },
        allowSetters = true
    )
    private AppUser createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return this.category;
    }

    public Product category(ProductCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductUnit getUnit() {
        return this.unit;
    }

    public Product unit(ProductUnit unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getPricePerUnit() {
        return this.pricePerUnit;
    }

    public Product pricePerUnit(BigDecimal pricePerUnit) {
        this.setPricePerUnit(pricePerUnit);
        return this;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Double getMinOrderQuantity() {
        return this.minOrderQuantity;
    }

    public Product minOrderQuantity(Double minOrderQuantity) {
        this.setMinOrderQuantity(minOrderQuantity);
        return this;
    }

    public void setMinOrderQuantity(Double minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public Double getMaxOrderQuantity() {
        return this.maxOrderQuantity;
    }

    public Product maxOrderQuantity(Double maxOrderQuantity) {
        this.setMaxOrderQuantity(maxOrderQuantity);
        return this;
    }

    public void setMaxOrderQuantity(Double maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public Product supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierContact() {
        return this.supplierContact;
    }

    public Product supplierContact(String supplierContact) {
        this.setSupplierContact(supplierContact);
        return this;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public Integer getLeadTime() {
        return this.leadTime;
    }

    public Product leadTime(Integer leadTime) {
        this.setLeadTime(leadTime);
        return this;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getShelfLife() {
        return this.shelfLife;
    }

    public Product shelfLife(Integer shelfLife) {
        this.setShelfLife(shelfLife);
        return this;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getStorageConditions() {
        return this.storageConditions;
    }

    public Product storageConditions(String storageConditions) {
        this.setStorageConditions(storageConditions);
        return this;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Product isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getLastUpdatedPrice() {
        return this.lastUpdatedPrice;
    }

    public Product lastUpdatedPrice(Instant lastUpdatedPrice) {
        this.setLastUpdatedPrice(lastUpdatedPrice);
        return this;
    }

    public void setLastUpdatedPrice(Instant lastUpdatedPrice) {
        this.lastUpdatedPrice = lastUpdatedPrice;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Product createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Product updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AppUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public Product createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", unit='" + getUnit() + "'" +
            ", pricePerUnit=" + getPricePerUnit() +
            ", minOrderQuantity=" + getMinOrderQuantity() +
            ", maxOrderQuantity=" + getMaxOrderQuantity() +
            ", supplier='" + getSupplier() + "'" +
            ", supplierContact='" + getSupplierContact() + "'" +
            ", leadTime=" + getLeadTime() +
            ", shelfLife=" + getShelfLife() +
            ", storageConditions='" + getStorageConditions() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", lastUpdatedPrice='" + getLastUpdatedPrice() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
