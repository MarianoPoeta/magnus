package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Configurable workflow automation
 */
@Entity
@Table(name = "workflow_trigger")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowTrigger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "trigger_name", length = 100, nullable = false)
    private String triggerName;

    @NotNull
    @Size(max = 50)
    @Column(name = "entity_type", length = 50, nullable = false)
    private String entityType;

    @Lob
    @Column(name = "trigger_condition", nullable = false)
    private String triggerCondition;

    @NotNull
    @Size(max = 50)
    @Column(name = "action_type", length = 50, nullable = false)
    private String actionType;

    @Lob
    @Column(name = "action_configuration")
    private String actionConfiguration;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Min(value = 1)
    @Column(name = "execution_order", nullable = false)
    private Integer executionOrder;

    @Column(name = "last_executed")
    private Instant lastExecuted;

    @Min(value = 0)
    @Column(name = "execution_count")
    private Integer executionCount;

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

    public WorkflowTrigger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public WorkflowTrigger triggerName(String triggerName) {
        this.setTriggerName(triggerName);
        return this;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public WorkflowTrigger entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getTriggerCondition() {
        return this.triggerCondition;
    }

    public WorkflowTrigger triggerCondition(String triggerCondition) {
        this.setTriggerCondition(triggerCondition);
        return this;
    }

    public void setTriggerCondition(String triggerCondition) {
        this.triggerCondition = triggerCondition;
    }

    public String getActionType() {
        return this.actionType;
    }

    public WorkflowTrigger actionType(String actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionConfiguration() {
        return this.actionConfiguration;
    }

    public WorkflowTrigger actionConfiguration(String actionConfiguration) {
        this.setActionConfiguration(actionConfiguration);
        return this;
    }

    public void setActionConfiguration(String actionConfiguration) {
        this.actionConfiguration = actionConfiguration;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public WorkflowTrigger isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getExecutionOrder() {
        return this.executionOrder;
    }

    public WorkflowTrigger executionOrder(Integer executionOrder) {
        this.setExecutionOrder(executionOrder);
        return this;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public Instant getLastExecuted() {
        return this.lastExecuted;
    }

    public WorkflowTrigger lastExecuted(Instant lastExecuted) {
        this.setLastExecuted(lastExecuted);
        return this;
    }

    public void setLastExecuted(Instant lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public Integer getExecutionCount() {
        return this.executionCount;
    }

    public WorkflowTrigger executionCount(Integer executionCount) {
        this.setExecutionCount(executionCount);
        return this;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public WorkflowTrigger createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public WorkflowTrigger updatedAt(Instant updatedAt) {
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

    public WorkflowTrigger createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkflowTrigger)) {
            return false;
        }
        return getId() != null && getId().equals(((WorkflowTrigger) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowTrigger{" +
            "id=" + getId() +
            ", triggerName='" + getTriggerName() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", triggerCondition='" + getTriggerCondition() + "'" +
            ", actionType='" + getActionType() + "'" +
            ", actionConfiguration='" + getActionConfiguration() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", executionOrder=" + getExecutionOrder() +
            ", lastExecuted='" + getLastExecuted() + "'" +
            ", executionCount=" + getExecutionCount() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
