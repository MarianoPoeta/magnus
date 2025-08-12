package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.ProductCategory;
import com.magnus.domain.enumeration.ProductUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Weekly consolidated shopping lists
 */
@Entity
@Table(name = "shopping_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "product_name", length = 100, nullable = false)
    private String productName;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_quantity", nullable = false)
    private Double totalQuantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private ProductUnit unit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    @Lob
    @Column(name = "budget_ids")
    private String budgetIds;

    @Lob
    @Column(name = "client_names")
    private String clientNames;

    @NotNull
    @Column(name = "is_purchased", nullable = false)
    private Boolean isPurchased;

    @DecimalMin(value = "0")
    @Column(name = "purchased_quantity")
    private Double purchasedQuantity;

    @NotNull
    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @NotNull
    @Column(name = "week_end", nullable = false)
    private LocalDate weekEnd;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Size(max = 100)
    @Column(name = "supplier", length = 100)
    private String supplier;

    @Size(max = 200)
    @Column(name = "supplier_contact", length = 200)
    private String supplierContact;

    @DecimalMin(value = "0")
    @Column(name = "estimated_cost", precision = 21, scale = 2)
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    @Column(name = "actual_cost", precision = 21, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @NotNull
    @Column(name = "is_consolidated", nullable = false)
    private Boolean isConsolidated;

    @Column(name = "consolidated_at")
    private Instant consolidatedAt;

    @Column(name = "purchased_at")
    private Instant purchasedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conflict_status", nullable = false)
    private ConflictStatus conflictStatus;

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

    @ManyToOne(fetch = FetchType.LAZY)
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
    private AppUser purchasedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "budgets", "shoppingItems", "tasks", "createdBy" }, allowSetters = true)
    private WeeklyPlan weeklyPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShoppingItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public ShoppingItem productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTotalQuantity() {
        return this.totalQuantity;
    }

    public ShoppingItem totalQuantity(Double totalQuantity) {
        this.setTotalQuantity(totalQuantity);
        return this;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public ProductUnit getUnit() {
        return this.unit;
    }

    public ShoppingItem unit(ProductUnit unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public ProductCategory getCategory() {
        return this.category;
    }

    public ShoppingItem category(ProductCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getBudgetIds() {
        return this.budgetIds;
    }

    public ShoppingItem budgetIds(String budgetIds) {
        this.setBudgetIds(budgetIds);
        return this;
    }

    public void setBudgetIds(String budgetIds) {
        this.budgetIds = budgetIds;
    }

    public String getClientNames() {
        return this.clientNames;
    }

    public ShoppingItem clientNames(String clientNames) {
        this.setClientNames(clientNames);
        return this;
    }

    public void setClientNames(String clientNames) {
        this.clientNames = clientNames;
    }

    public Boolean getIsPurchased() {
        return this.isPurchased;
    }

    public ShoppingItem isPurchased(Boolean isPurchased) {
        this.setIsPurchased(isPurchased);
        return this;
    }

    public void setIsPurchased(Boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public Double getPurchasedQuantity() {
        return this.purchasedQuantity;
    }

    public ShoppingItem purchasedQuantity(Double purchasedQuantity) {
        this.setPurchasedQuantity(purchasedQuantity);
        return this;
    }

    public void setPurchasedQuantity(Double purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }

    public LocalDate getWeekStart() {
        return this.weekStart;
    }

    public ShoppingItem weekStart(LocalDate weekStart) {
        this.setWeekStart(weekStart);
        return this;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return this.weekEnd;
    }

    public ShoppingItem weekEnd(LocalDate weekEnd) {
        this.setWeekEnd(weekEnd);
        return this;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getNotes() {
        return this.notes;
    }

    public ShoppingItem notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public ShoppingItem supplier(String supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierContact() {
        return this.supplierContact;
    }

    public ShoppingItem supplierContact(String supplierContact) {
        this.setSupplierContact(supplierContact);
        return this;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public BigDecimal getEstimatedCost() {
        return this.estimatedCost;
    }

    public ShoppingItem estimatedCost(BigDecimal estimatedCost) {
        this.setEstimatedCost(estimatedCost);
        return this;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return this.actualCost;
    }

    public ShoppingItem actualCost(BigDecimal actualCost) {
        this.setActualCost(actualCost);
        return this;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public LocalDate getDeliveryDate() {
        return this.deliveryDate;
    }

    public ShoppingItem deliveryDate(LocalDate deliveryDate) {
        this.setDeliveryDate(deliveryDate);
        return this;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Boolean getIsConsolidated() {
        return this.isConsolidated;
    }

    public ShoppingItem isConsolidated(Boolean isConsolidated) {
        this.setIsConsolidated(isConsolidated);
        return this;
    }

    public void setIsConsolidated(Boolean isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public Instant getConsolidatedAt() {
        return this.consolidatedAt;
    }

    public ShoppingItem consolidatedAt(Instant consolidatedAt) {
        this.setConsolidatedAt(consolidatedAt);
        return this;
    }

    public void setConsolidatedAt(Instant consolidatedAt) {
        this.consolidatedAt = consolidatedAt;
    }

    public Instant getPurchasedAt() {
        return this.purchasedAt;
    }

    public ShoppingItem purchasedAt(Instant purchasedAt) {
        this.setPurchasedAt(purchasedAt);
        return this;
    }

    public void setPurchasedAt(Instant purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public ConflictStatus getConflictStatus() {
        return this.conflictStatus;
    }

    public ShoppingItem conflictStatus(ConflictStatus conflictStatus) {
        this.setConflictStatus(conflictStatus);
        return this;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public Integer getVersion() {
        return this.version;
    }

    public ShoppingItem version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ShoppingItem createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ShoppingItem updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AppUser getPurchasedBy() {
        return this.purchasedBy;
    }

    public void setPurchasedBy(AppUser appUser) {
        this.purchasedBy = appUser;
    }

    public ShoppingItem purchasedBy(AppUser appUser) {
        this.setPurchasedBy(appUser);
        return this;
    }

    public WeeklyPlan getWeeklyPlan() {
        return this.weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlan weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    public ShoppingItem weeklyPlan(WeeklyPlan weeklyPlan) {
        this.setWeeklyPlan(weeklyPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingItem)) {
            return false;
        }
        return getId() != null && getId().equals(((ShoppingItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingItem{" +
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
            "}";
    }
}
