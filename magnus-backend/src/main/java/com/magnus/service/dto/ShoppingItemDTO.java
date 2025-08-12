package com.magnus.service.dto;

import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.ProductCategory;
import com.magnus.domain.enumeration.ProductUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.ShoppingItem} entity.
 */
@Schema(description = "Weekly consolidated shopping lists")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String productName;

    @NotNull
    @DecimalMin(value = "0")
    private Double totalQuantity;

    @NotNull
    private ProductUnit unit;

    @NotNull
    private ProductCategory category;

    @Lob
    private String budgetIds;

    @Lob
    private String clientNames;

    @NotNull
    private Boolean isPurchased;

    @DecimalMin(value = "0")
    private Double purchasedQuantity;

    @NotNull
    private LocalDate weekStart;

    @NotNull
    private LocalDate weekEnd;

    @Lob
    private String notes;

    @Size(max = 100)
    private String supplier;

    @Size(max = 200)
    private String supplierContact;

    @DecimalMin(value = "0")
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    private BigDecimal actualCost;

    private LocalDate deliveryDate;

    @NotNull
    private Boolean isConsolidated;

    private Instant consolidatedAt;

    private Instant purchasedAt;

    @NotNull
    private ConflictStatus conflictStatus;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private AppUserDTO purchasedBy;

    @NotNull
    private WeeklyPlanDTO weeklyPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getBudgetIds() {
        return budgetIds;
    }

    public void setBudgetIds(String budgetIds) {
        this.budgetIds = budgetIds;
    }

    public String getClientNames() {
        return clientNames;
    }

    public void setClientNames(String clientNames) {
        this.clientNames = clientNames;
    }

    public Boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public Double getPurchasedQuantity() {
        return purchasedQuantity;
    }

    public void setPurchasedQuantity(Double purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean getIsConsolidated() {
        return isConsolidated;
    }

    public void setIsConsolidated(Boolean isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public Instant getConsolidatedAt() {
        return consolidatedAt;
    }

    public void setConsolidatedAt(Instant consolidatedAt) {
        this.consolidatedAt = consolidatedAt;
    }

    public Instant getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(Instant purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public ConflictStatus getConflictStatus() {
        return conflictStatus;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public AppUserDTO getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(AppUserDTO purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public WeeklyPlanDTO getWeeklyPlan() {
        return weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlanDTO weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingItemDTO)) {
            return false;
        }

        ShoppingItemDTO shoppingItemDTO = (ShoppingItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shoppingItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingItemDTO{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", totalQuantity=" + getTotalQuantity() +
            ", unit='" + getUnit() + "'" +
            ", category='" + getCategory() + "'" +
            ", budgetIds='" + getBudgetIds() + "'" +
            ", clientNames='" + getClientNames() + "'" +
            ", isPurchased='" + getIsPurchased() + "'" +
            ", purchasedQuantity=" + getPurchasedQuantity() +
            ", weekStart='" + getWeekStart() + "'" +
            ", weekEnd='" + getWeekEnd() + "'" +
            ", notes='" + getNotes() + "'" +
            ", supplier='" + getSupplier() + "'" +
            ", supplierContact='" + getSupplierContact() + "'" +
            ", estimatedCost=" + getEstimatedCost() +
            ", actualCost=" + getActualCost() +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", isConsolidated='" + getIsConsolidated() + "'" +
            ", consolidatedAt='" + getConsolidatedAt() + "'" +
            ", purchasedAt='" + getPurchasedAt() + "'" +
            ", conflictStatus='" + getConflictStatus() + "'" +
            ", version=" + getVersion() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", purchasedBy=" + getPurchasedBy() +
            ", weeklyPlan=" + getWeeklyPlan() +
            "}";
    }
}
