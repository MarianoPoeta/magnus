package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Conflict resolution for concurrent modifications
 */
@Entity
@Table(name = "conflict_resolution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConflictResolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "entity_type", length = 50, nullable = false)
    private String entityType;

    @NotNull
    @Size(max = 100)
    @Column(name = "entity_id", length = 100, nullable = false)
    private String entityId;

    @Size(max = 100)
    @Column(name = "field_name", length = 100)
    private String fieldName;

    @Lob
    @Column(name = "local_value")
    private String localValue;

    @Lob
    @Column(name = "remote_value")
    private String remoteValue;

    @Lob
    @Column(name = "resolved_value")
    private String resolvedValue;

    @NotNull
    @Size(max = 50)
    @Column(name = "resolution_strategy", length = 50, nullable = false)
    private String resolutionStrategy;

    @NotNull
    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @NotNull
    @Column(name = "conflict_detected_at", nullable = false)
    private Instant conflictDetectedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

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
    private AppUser conflictUser;

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
    private AppUser resolvedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConflictResolution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public ConflictResolution entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public ConflictResolution entityId(String entityId) {
        this.setEntityId(entityId);
        return this;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public ConflictResolution fieldName(String fieldName) {
        this.setFieldName(fieldName);
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLocalValue() {
        return this.localValue;
    }

    public ConflictResolution localValue(String localValue) {
        this.setLocalValue(localValue);
        return this;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }

    public String getRemoteValue() {
        return this.remoteValue;
    }

    public ConflictResolution remoteValue(String remoteValue) {
        this.setRemoteValue(remoteValue);
        return this;
    }

    public void setRemoteValue(String remoteValue) {
        this.remoteValue = remoteValue;
    }

    public String getResolvedValue() {
        return this.resolvedValue;
    }

    public ConflictResolution resolvedValue(String resolvedValue) {
        this.setResolvedValue(resolvedValue);
        return this;
    }

    public void setResolvedValue(String resolvedValue) {
        this.resolvedValue = resolvedValue;
    }

    public String getResolutionStrategy() {
        return this.resolutionStrategy;
    }

    public ConflictResolution resolutionStrategy(String resolutionStrategy) {
        this.setResolutionStrategy(resolutionStrategy);
        return this;
    }

    public void setResolutionStrategy(String resolutionStrategy) {
        this.resolutionStrategy = resolutionStrategy;
    }

    public Boolean getIsResolved() {
        return this.isResolved;
    }

    public ConflictResolution isResolved(Boolean isResolved) {
        this.setIsResolved(isResolved);
        return this;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public Instant getResolvedAt() {
        return this.resolvedAt;
    }

    public ConflictResolution resolvedAt(Instant resolvedAt) {
        this.setResolvedAt(resolvedAt);
        return this;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Instant getConflictDetectedAt() {
        return this.conflictDetectedAt;
    }

    public ConflictResolution conflictDetectedAt(Instant conflictDetectedAt) {
        this.setConflictDetectedAt(conflictDetectedAt);
        return this;
    }

    public void setConflictDetectedAt(Instant conflictDetectedAt) {
        this.conflictDetectedAt = conflictDetectedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ConflictResolution createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AppUser getConflictUser() {
        return this.conflictUser;
    }

    public void setConflictUser(AppUser appUser) {
        this.conflictUser = appUser;
    }

    public ConflictResolution conflictUser(AppUser appUser) {
        this.setConflictUser(appUser);
        return this;
    }

    public AppUser getResolvedBy() {
        return this.resolvedBy;
    }

    public void setResolvedBy(AppUser appUser) {
        this.resolvedBy = appUser;
    }

    public ConflictResolution resolvedBy(AppUser appUser) {
        this.setResolvedBy(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConflictResolution)) {
            return false;
        }
        return getId() != null && getId().equals(((ConflictResolution) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConflictResolution{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", localValue='" + getLocalValue() + "'" +
            ", remoteValue='" + getRemoteValue() + "'" +
            ", resolvedValue='" + getResolvedValue() + "'" +
            ", resolutionStrategy='" + getResolutionStrategy() + "'" +
            ", isResolved='" + getIsResolved() + "'" +
            ", resolvedAt='" + getResolvedAt() + "'" +
            ", conflictDetectedAt='" + getConflictDetectedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
