package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.ProductUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product requirements linking to various entities
 */
@Entity
@Table(name = "product_requirement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private ProductUnit unit;

    @Lob
    @Column(name = "notes")
    private String notes;

    @DecimalMin(value = "0")
    @Column(name = "estimated_cost", precision = 21, scale = 2)
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    @Column(name = "actual_cost", precision = 21, scale = 2)
    private BigDecimal actualCost;

    @NotNull
    @Column(name = "is_purchased", nullable = false)
    private Boolean isPurchased;

    @Size(max = 100)
    @Column(name = "purchased_by", length = 100)
    private String purchasedBy;

    @Column(name = "purchased_at")
    private Instant purchasedAt;

    @NotNull
    @Min(value = 1)
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conflict_status", nullable = false)
    private ConflictStatus conflictStatus;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "createdBy" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Task relatedTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productRequirements", "availableMenus" }, allowSetters = true)
    private FoodItem foodItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productRequirements", "transportAssignments" }, allowSetters = true)
    private Activity activity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductRequirement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public ProductRequirement quantity(Double quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public ProductUnit getUnit() {
        return this.unit;
    }

    public ProductRequirement unit(ProductUnit unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public String getNotes() {
        return this.notes;
    }

    public ProductRequirement notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getEstimatedCost() {
        return this.estimatedCost;
    }

    public ProductRequirement estimatedCost(BigDecimal estimatedCost) {
        this.setEstimatedCost(estimatedCost);
        return this;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return this.actualCost;
    }

    public ProductRequirement actualCost(BigDecimal actualCost) {
        this.setActualCost(actualCost);
        return this;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public Boolean getIsPurchased() {
        return this.isPurchased;
    }

    public ProductRequirement isPurchased(Boolean isPurchased) {
        this.setIsPurchased(isPurchased);
        return this;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public String getPurchasedBy() {
        return this.purchasedBy;
    }

    public ProductRequirement purchasedBy(String purchasedBy) {
        this.setPurchasedBy(purchasedBy);
        return this;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public Instant getPurchasedAt() {
        return this.purchasedAt;
    }

    public ProductRequirement purchasedAt(Instant purchasedAt) {
        this.setPurchasedAt(purchasedAt);
        return this;
    }

    public void setPurchasedAt(Instant purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public Integer getVersion() {
        return this.version;
    }

    public ProductRequirement version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ConflictStatus getConflictStatus() {
        return this.conflictStatus;
    }

    public ProductRequirement conflictStatus(ConflictStatus conflictStatus) {
        this.setConflictStatus(conflictStatus);
        return this;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ProductRequirement createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ProductRequirement updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductRequirement product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Task getRelatedTask() {
        return this.relatedTask;
    }

    public void setRelatedTask(Task task) {
        this.relatedTask = task;
    }

    public ProductRequirement relatedTask(Task task) {
        this.setRelatedTask(task);
        return this;
    }

    public FoodItem getFoodItem() {
        return this.foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public ProductRequirement foodItem(FoodItem foodItem) {
        this.setFoodItem(foodItem);
        return this;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ProductRequirement activity(Activity activity) {
        this.setActivity(activity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductRequirement)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductRequirement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductRequirement{" +
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
            "}";
    }
}
