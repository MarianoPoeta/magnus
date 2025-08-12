package com.magnus.service.dto;

import com.magnus.domain.enumeration.DependencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.TaskDependency} entity.
 */
@Schema(description = "Task dependency management")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDependencyDTO implements Serializable {

    private Long id;

    @NotNull
    private DependencyType dependencyType;

    @Size(max = 500)
    private String notes;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Instant createdAt;

    @NotNull
    private TaskDTO prerequisiteTask;

    @NotNull
    private TaskDTO dependentTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DependencyType getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public TaskDTO getPrerequisiteTask() {
        return prerequisiteTask;
    }

    public void setPrerequisiteTask(TaskDTO prerequisiteTask) {
        this.prerequisiteTask = prerequisiteTask;
    }

    public TaskDTO getDependentTask() {
        return dependentTask;
    }

    public void setDependentTask(TaskDTO dependentTask) {
        this.dependentTask = dependentTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDependencyDTO)) {
            return false;
        }

        TaskDependencyDTO taskDependencyDTO = (TaskDependencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDependencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDependencyDTO{" +
            "id=" + getId() +
            ", dependencyType='" + getDependencyType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", prerequisiteTask=" + getPrerequisiteTask() +
            ", dependentTask=" + getDependentTask() +
            "}";
    }
}
