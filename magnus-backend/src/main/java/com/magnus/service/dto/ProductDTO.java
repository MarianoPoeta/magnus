package com.magnus.service.dto;

import com.magnus.domain.enumeration.ProductCategory;
import com.magnus.domain.enumeration.ProductUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Product} entity.
 */
@Schema(description = "Product catalog with supplier information")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    private ProductCategory category;

    @NotNull
    private ProductUnit unit;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal pricePerUnit;

    @DecimalMin(value = "0")
    private Double minOrderQuantity;

    @DecimalMin(value = "0")
    private Double maxOrderQuantity;

    @Size(max = 100)
    private String supplier;

    @Lob
    private String supplierContact;

    @Min(value = 0)
    private Integer leadTime;

    @Min(value = 0)
    private Integer shelfLife;

    @Lob
    private String storageConditions;

    @NotNull
    private Boolean isActive;

    private Instant lastUpdatedPrice;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private AppUserDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Double getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Double minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public Double getMaxOrderQuantity() {
        return maxOrderQuantity;
    }

    public void setMaxOrderQuantity(Double maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getLastUpdatedPrice() {
        return lastUpdatedPrice;
    }

    public void setLastUpdatedPrice(Instant lastUpdatedPrice) {
        this.lastUpdatedPrice = lastUpdatedPrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AppUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
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
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
