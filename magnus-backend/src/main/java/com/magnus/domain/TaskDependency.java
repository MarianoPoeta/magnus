package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.DependencyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task dependency management
 */
@Entity
@Table(name = "task_dependency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDependency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dependency_type", nullable = false)
    private DependencyType dependencyType;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Task prerequisiteTask;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Task dependentTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TaskDependency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DependencyType getDependencyType() {
        return this.dependencyType;
    }

    public TaskDependency dependencyType(DependencyType dependencyType) {
        this.setDependencyType(dependencyType);
        return this;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public String getNotes() {
        return this.notes;
    }

    public TaskDependency notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TaskDependency isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TaskDependency createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Task getPrerequisiteTask() {
        return this.prerequisiteTask;
    }

    public void setPrerequisiteTask(Task task) {
        this.prerequisiteTask = task;
    }

    public TaskDependency prerequisiteTask(Task task) {
        this.setPrerequisiteTask(task);
        return this;
    }

    public Task getDependentTask() {
        return this.dependentTask;
    }

    public void setDependentTask(Task task) {
        this.dependentTask = task;
    }

    public TaskDependency dependentTask(Task task) {
        this.setDependentTask(task);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDependency)) {
            return false;
        }
        return getId() != null && getId().equals(((TaskDependency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDependency{" +
            "id=" + getId() +
            ", dependencyType='" + getDependencyType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
