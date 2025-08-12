package com.magnus.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.BudgetItem} entity.
 */
@Schema(description = "Individual items within a budget")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^(menu|activity|transport|accommodation)$")
    private String itemType;

    @NotNull
    @Size(max = 100)
    private String templateId;

    @NotNull
    @Size(max = 200)
    private String templateName;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0")
    private BigDecimal unitCost;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0")
    private BigDecimal totalCost;

    @Lob
    private String customizations;

    @Lob
    private String notes;

    @NotNull
    private Boolean isCustomized;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private BudgetDTO budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getCustomizations() {
        return customizations;
    }

    public void setCustomizations(String customizations) {
        this.customizations = customizations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsCustomized() {
        return isCustomized;
    }

    public void setIsCustomized(Boolean isCustomized) {
        this.isCustomized = isCustomized;
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

    public BudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetDTO budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetItemDTO)) {
            return false;
        }

        BudgetItemDTO budgetItemDTO = (BudgetItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetItemDTO{" +
            "id=" + getId() +
            ", itemType='" + getItemType() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", templateName='" + getTemplateName() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitCost=" + getUnitCost() +
            ", totalPrice=" + getTotalPrice() +
            ", totalCost=" + getTotalCost() +
            ", customizations='" + getCustomizations() + "'" +
            ", notes='" + getNotes() + "'" +
            ", isCustomized='" + getIsCustomized() + "'" +
            ", version=" + getVersion() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", budget=" + getBudget() +
            "}";
    }
}
