package com.magnus.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.WorkflowTrigger} entity.
 */
@Schema(description = "Configurable workflow automation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WorkflowTriggerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String triggerName;

    @NotNull
    @Size(max = 50)
    private String entityType;

    @Lob
    private String triggerCondition;

    @NotNull
    @Size(max = 50)
    private String actionType;

    @Lob
    private String actionConfiguration;

    @NotNull
    private Boolean isActive;

    @NotNull
    @Min(value = 1)
    private Integer executionOrder;

    private Instant lastExecuted;

    @Min(value = 0)
    private Integer executionCount;

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

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getTriggerCondition() {
        return triggerCondition;
    }

    public void setTriggerCondition(String triggerCondition) {
        this.triggerCondition = triggerCondition;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionConfiguration() {
        return actionConfiguration;
    }

    public void setActionConfiguration(String actionConfiguration) {
        this.actionConfiguration = actionConfiguration;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public Instant getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(Instant lastExecuted) {
        this.lastExecuted = lastExecuted;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
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
        if (!(o instanceof WorkflowTriggerDTO)) {
            return false;
        }

        WorkflowTriggerDTO workflowTriggerDTO = (WorkflowTriggerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workflowTriggerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkflowTriggerDTO{" +
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
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
