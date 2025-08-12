package com.magnus.service.dto;

import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.TaskStatus;
import com.magnus.domain.enumeration.TaskType;
import com.magnus.domain.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Task} entity.
 */
@Schema(description = "Task management with workflow automation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @Lob
    private String description;

    @NotNull
    private TaskType type;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private TaskStatus status;

    @NotNull
    private UserRole assignedToRole;

    @NotNull
    private LocalDate dueDate;

    private LocalTime dueTime;

    @Min(value = 0)
    private Integer estimatedDuration;

    @Min(value = 0)
    private Integer actualDuration;

    @Size(max = 200)
    private String location;

    @Lob
    private String requirements;

    @Lob
    private String notes;

    @Size(max = 500)
    private String invoiceUrl;

    @NotNull
    private Boolean autoScheduled;

    @NotNull
    private Boolean isRecurring;

    @Size(max = 100)
    private String parentTaskId;

    private Instant completedAt;

    private Instant startedAt;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private ConflictStatus conflictStatus;

    @Size(max = 50)
    private String lastModifiedBy;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private AppUserDTO createdBy;

    private AppUserDTO assignedTo;

    private WeeklyPlanDTO weeklyPlan;

    @NotNull
    private BudgetDTO relatedBudget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserRole getAssignedToRole() {
        return assignedToRole;
    }

    public void setAssignedToRole(UserRole assignedToRole) {
        this.assignedToRole = assignedToRole;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Integer getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public Boolean getAutoScheduled() {
        return autoScheduled;
    }

    public void setAutoScheduled(Boolean autoScheduled) {
        this.autoScheduled = autoScheduled;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
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

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
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

    public AppUserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(AppUserDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    public WeeklyPlanDTO getWeeklyPlan() {
        return weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlanDTO weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    public BudgetDTO getRelatedBudget() {
        return relatedBudget;
    }

    public void setRelatedBudget(BudgetDTO relatedBudget) {
        this.relatedBudget = relatedBudget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", priority='" + getPriority() + "'" +
            ", status='" + getStatus() + "'" +
            ", assignedToRole='" + getAssignedToRole() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", dueTime='" + getDueTime() + "'" +
            ", estimatedDuration=" + getEstimatedDuration() +
            ", actualDuration=" + getActualDuration() +
            ", location='" + getLocation() + "'" +
            ", requirements='" + getRequirements() + "'" +
            ", notes='" + getNotes() + "'" +
            ", invoiceUrl='" + getInvoiceUrl() + "'" +
            ", autoScheduled='" + getAutoScheduled() + "'" +
            ", isRecurring='" + getIsRecurring() + "'" +
            ", parentTaskId='" + getParentTaskId() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", version=" + getVersion() +
            ", conflictStatus='" + getConflictStatus() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", assignedTo=" + getAssignedTo() +
            ", weeklyPlan=" + getWeeklyPlan() +
            ", relatedBudget=" + getRelatedBudget() +
            "}";
    }
}
