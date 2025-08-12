package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.WeeklyPlanStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Weekly planning for logistics coordination
 */
@Entity
@Table(name = "weekly_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeeklyPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @NotNull
    @Column(name = "week_end", nullable = false)
    private LocalDate weekEnd;

    @NotNull
    @Size(max = 100)
    @Column(name = "plan_name", length = 100, nullable = false)
    private String planName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WeeklyPlanStatus status;

    @Min(value = 0)
    @Column(name = "total_budgets")
    private Integer totalBudgets;

    @Min(value = 0)
    @Column(name = "total_guests")
    private Integer totalGuests;

    @DecimalMin(value = "0")
    @Column(name = "estimated_cost", precision = 21, scale = 2)
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    @Column(name = "actual_cost", precision = 21, scale = 2)
    private BigDecimal actualCost;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "is_consolidated", nullable = false)
    private Boolean isConsolidated;

    @Column(name = "consolidated_at")
    private Instant consolidatedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "weeklyPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Budget> budgets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "weeklyPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "purchasedBy", "weeklyPlan" }, allowSetters = true)
    private Set<ShoppingItem> shoppingItems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "weeklyPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Set<Task> tasks = new HashSet<>();

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

    public WeeklyPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWeekStart() {
        return this.weekStart;
    }

    public WeeklyPlan weekStart(LocalDate weekStart) {
        this.setWeekStart(weekStart);
        return this;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return this.weekEnd;
    }

    public WeeklyPlan weekEnd(LocalDate weekEnd) {
        this.setWeekEnd(weekEnd);
        return this;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getPlanName() {
        return this.planName;
    }

    public WeeklyPlan planName(String planName) {
        this.setPlanName(planName);
        return this;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public WeeklyPlanStatus getStatus() {
        return this.status;
    }

    public WeeklyPlan status(WeeklyPlanStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WeeklyPlanStatus status) {
        this.status = status;
    }

    public Integer getTotalBudgets() {
        return this.totalBudgets;
    }

    public WeeklyPlan totalBudgets(Integer totalBudgets) {
        this.setTotalBudgets(totalBudgets);
        return this;
    }

    public void setTotalBudgets(Integer totalBudgets) {
        this.totalBudgets = totalBudgets;
    }

    public Integer getTotalGuests() {
        return this.totalGuests;
    }

    public WeeklyPlan totalGuests(Integer totalGuests) {
        this.setTotalGuests(totalGuests);
        return this;
    }

    public void setTotalGuests(Integer totalGuests) {
        this.totalGuests = totalGuests;
    }

    public BigDecimal getEstimatedCost() {
        return this.estimatedCost;
    }

    public WeeklyPlan estimatedCost(BigDecimal estimatedCost) {
        this.setEstimatedCost(estimatedCost);
        return this;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return this.actualCost;
    }

    public WeeklyPlan actualCost(BigDecimal actualCost) {
        this.setActualCost(actualCost);
        return this;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getNotes() {
        return this.notes;
    }

    public WeeklyPlan notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsConsolidated() {
        return this.isConsolidated;
    }

    public WeeklyPlan isConsolidated(Boolean isConsolidated) {
        this.setIsConsolidated(isConsolidated);
        return this;
    }

    public void setIsConsolidated(Boolean isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public Instant getConsolidatedAt() {
        return this.consolidatedAt;
    }

    public WeeklyPlan consolidatedAt(Instant consolidatedAt) {
        this.setConsolidatedAt(consolidatedAt);
        return this;
    }

    public void setConsolidatedAt(Instant consolidatedAt) {
        this.consolidatedAt = consolidatedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public WeeklyPlan createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public WeeklyPlan updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Budget> getBudgets() {
        return this.budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        if (this.budgets != null) {
            this.budgets.forEach(i -> i.setWeeklyPlan(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setWeeklyPlan(this));
        }
        this.budgets = budgets;
    }

    public WeeklyPlan budgets(Set<Budget> budgets) {
        this.setBudgets(budgets);
        return this;
    }

    public WeeklyPlan addBudget(Budget budget) {
        this.budgets.add(budget);
        budget.setWeeklyPlan(this);
        return this;
    }

    public WeeklyPlan removeBudget(Budget budget) {
        this.budgets.remove(budget);
        budget.setWeeklyPlan(null);
        return this;
    }

    public Set<ShoppingItem> getShoppingItems() {
        return this.shoppingItems;
    }

    public void setShoppingItems(Set<ShoppingItem> shoppingItems) {
        if (this.shoppingItems != null) {
            this.shoppingItems.forEach(i -> i.setWeeklyPlan(null));
        }
        if (shoppingItems != null) {
            shoppingItems.forEach(i -> i.setWeeklyPlan(this));
        }
        this.shoppingItems = shoppingItems;
    }

    public WeeklyPlan shoppingItems(Set<ShoppingItem> shoppingItems) {
        this.setShoppingItems(shoppingItems);
        return this;
    }

    public WeeklyPlan addShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.add(shoppingItem);
        shoppingItem.setWeeklyPlan(this);
        return this;
    }

    public WeeklyPlan removeShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItems.remove(shoppingItem);
        shoppingItem.setWeeklyPlan(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setWeeklyPlan(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setWeeklyPlan(this));
        }
        this.tasks = tasks;
    }

    public WeeklyPlan tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public WeeklyPlan addTask(Task task) {
        this.tasks.add(task);
        task.setWeeklyPlan(this);
        return this;
    }

    public WeeklyPlan removeTask(Task task) {
        this.tasks.remove(task);
        task.setWeeklyPlan(null);
        return this;
    }

    public AppUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public WeeklyPlan createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((WeeklyPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyPlan{" +
            "id=" + getId() +
            ", weekStart='" + getWeekStart() + "'" +
            ", weekEnd='" + getWeekEnd() + "'" +
            ", planName='" + getPlanName() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalBudgets=" + getTotalBudgets() +
            ", totalGuests=" + getTotalGuests() +
            ", estimatedCost=" + getEstimatedCost() +
            ", actualCost=" + getActualCost() +
            ", notes='" + getNotes() + "'" +
            ", isConsolidated='" + getIsConsolidated() + "'" +
            ", consolidatedAt='" + getConsolidatedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
