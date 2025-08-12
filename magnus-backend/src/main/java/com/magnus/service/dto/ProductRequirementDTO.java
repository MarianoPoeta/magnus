package com.magnus.service.dto;

import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.ProductUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.ProductRequirement} entity.
 */
@Schema(description = "Product requirements linking to various entities")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductRequirementDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Double quantity;

    @NotNull
    private ProductUnit unit;

    @Lob
    private String notes;

    @DecimalMin(value = "0")
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    private BigDecimal actualCost;

    @NotNull
    private Boolean isPurchased;

    @Size(max = 100)
    private String purchasedBy;

    private Instant purchasedAt;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private ConflictStatus conflictStatus;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private ProductDTO product;

    private TaskDTO relatedTask;

    private FoodItemDTO foodItem;

    private ActivityDTO activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Boolean getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public String getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public Instant getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(Instant purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ConflictStatus getConflictStatus() {
        return conflictStatus;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public TaskDTO getRelatedTask() {
        return relatedTask;
    }

    public void setRelatedTask(TaskDTO relatedTask) {
        this.relatedTask = relatedTask;
    }

    public FoodItemDTO getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItemDTO foodItem) {
        this.foodItem = foodItem;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductRequirementDTO)) {
            return false;
        }

        ProductRequirementDTO productRequirementDTO = (ProductRequirementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productRequirementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductRequirementDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", notes='" + getNotes() + "'" +
            ", estimatedCost=" + getEstimatedCost() +
            ", actualCost=" + getActualCost() +
            ", isPurchased='" + getIsPurchased() + "'" +
            ", purchasedBy='" + getPurchasedBy() + "'" +
            ", purchasedAt='" + getPurchasedAt() + "'" +
            ", version=" + getVersion() +
            ", conflictStatus='" + getConflictStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", product=" + getProduct() +
            ", relatedTask=" + getRelatedTask() +
            ", foodItem=" + getFoodItem() +
            ", activity=" + getActivity() +
            "}";
    }
}
