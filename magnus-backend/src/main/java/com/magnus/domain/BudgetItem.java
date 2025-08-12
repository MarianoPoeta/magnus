package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Individual items within a budget
 */
@Entity
@Table(name = "budget_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^(menu|activity|transport|accommodation)$")
    @Column(name = "item_type", nullable = false)
    private String itemType;

    @NotNull
    @Size(max = 100)
    @Column(name = "template_id", length = 100, nullable = false)
    private String templateId;

    @NotNull
    @Size(max = 200)
    @Column(name = "template_name", length = 200, nullable = false)
    private String templateName;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "unit_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @DecimalMin(value = "0")
    @Column(name = "unit_cost", precision = 21, scale = 2)
    private BigDecimal unitCost;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @DecimalMin(value = "0")
    @Column(name = "total_cost", precision = 21, scale = 2)
    private BigDecimal totalCost;

    @Lob
    @Column(name = "customizations")
    private String customizations;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "is_customized", nullable = false)
    private Boolean isCustomized;

    @NotNull
    @Min(value = 1)
    @Column(name = "version", nullable = false)
    private Integer version;

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
            "budgetItems",
            "payments",
            "tasks",
            "transportAssignments",
            "cookingSchedules",
            "createdBy",
            "assignedTo",
            "client",
            "template",
            "weeklyPlan",
        },
        allowSetters = true
    )
    private Budget budget;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BudgetItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemType() {
        return this.itemType;
    }

    public BudgetItem itemType(String itemType) {
        this.setItemType(itemType);
        return this;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public BudgetItem templateId(String templateId) {
        this.setTemplateId(templateId);
        return this;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public BudgetItem templateName(String templateName) {
        this.setTemplateName(templateName);
        return this;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BudgetItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BudgetItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public BudgetItem unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public BudgetItem totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public BudgetItem totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getCustomizations() {
        return this.customizations;
    }

    public BudgetItem customizations(String customizations) {
        this.setCustomizations(customizations);
        return this;
    }

    public void setCustomizations(String customizations) {
        this.customizations = customizations;
    }

    public String getNotes() {
        return this.notes;
    }

    public BudgetItem notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsCustomized() {
        return this.isCustomized;
    }

    public BudgetItem isCustomized(Boolean isCustomized) {
        this.setIsCustomized(isCustomized);
        return this;
    }

    public void setIsCustomized(Boolean isCustomized) {
        this.isCustomized = isCustomized;
    }

    public Integer getVersion() {
        return this.version;
    }

    public BudgetItem version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BudgetItem createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public BudgetItem updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BudgetItem budget(Budget budget) {
        this.setBudget(budget);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetItem)) {
            return false;
        }
        return getId() != null && getId().equals(((BudgetItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetItem{" +
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
            "}";
    }
}
