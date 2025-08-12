package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Additional needs and requirements
 */
@Entity
@Table(name = "need")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Need implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Size(max = 20)
    @Column(name = "unit", length = 20)
    private String unit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private TaskPriority urgency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @NotNull
    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;

    @Column(name = "required_date")
    private LocalDate requiredDate;

    @Column(name = "fulfilled_date")
    private LocalDate fulfilledDate;

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
    private AppUser requestedBy;

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
    private AppUser fulfilledBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Task parentTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Need id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Need description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Need quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return this.unit;
    }

    public Need unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public TaskPriority getUrgency() {
        return this.urgency;
    }

    public Need urgency(TaskPriority urgency) {
        this.setUrgency(urgency);
        return this;
    }

    public void setUrgency(TaskPriority urgency) {
        this.urgency = urgency;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public Need status(TaskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getRequestedDate() {
        return this.requestedDate;
    }

    public Need requestedDate(LocalDate requestedDate) {
        this.setRequestedDate(requestedDate);
        return this;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getRequiredDate() {
        return this.requiredDate;
    }

    public Need requiredDate(LocalDate requiredDate) {
        this.setRequiredDate(requiredDate);
        return this;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    public LocalDate getFulfilledDate() {
        return this.fulfilledDate;
    }

    public Need fulfilledDate(LocalDate fulfilledDate) {
        this.setFulfilledDate(fulfilledDate);
        return this;
    }

    public void setFulfilledDate(LocalDate fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
    }

    public BigDecimal getEstimatedCost() {
        return this.estimatedCost;
    }

    public Need estimatedCost(BigDecimal estimatedCost) {
        this.setEstimatedCost(estimatedCost);
        return this;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return this.actualCost;
    }

    public Need actualCost(BigDecimal actualCost) {
        this.setActualCost(actualCost);
        return this;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getNotes() {
        return this.notes;
    }

    public Need notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Need createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Need updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AppUser getRequestedBy() {
        return this.requestedBy;
    }

    public void setRequestedBy(AppUser appUser) {
        this.requestedBy = appUser;
    }

    public Need requestedBy(AppUser appUser) {
        this.setRequestedBy(appUser);
        return this;
    }

    public AppUser getFulfilledBy() {
        return this.fulfilledBy;
    }

    public void setFulfilledBy(AppUser appUser) {
        this.fulfilledBy = appUser;
    }

    public Need fulfilledBy(AppUser appUser) {
        this.setFulfilledBy(appUser);
        return this;
    }

    public Task getParentTask() {
        return this.parentTask;
    }

    public void setParentTask(Task task) {
        this.parentTask = task;
    }

    public Need parentTask(Task task) {
        this.setParentTask(task);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Need)) {
            return false;
        }
        return getId() != null && getId().equals(((Need) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Need{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", urgency='" + getUrgency() + "'" +
            ", status='" + getStatus() + "'" +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", requiredDate='" + getRequiredDate() + "'" +
            ", fulfilledDate='" + getFulfilledDate() + "'" +
            ", estimatedCost=" + getEstimatedCost() +
            ", actualCost=" + getActualCost() +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
