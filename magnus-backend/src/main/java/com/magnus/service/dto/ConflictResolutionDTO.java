package com.magnus.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.ConflictResolution} entity.
 */
@Schema(description = "Conflict resolution for concurrent modifications")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConflictResolutionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String entityType;

    @NotNull
    @Size(max = 100)
    private String entityId;

    @Size(max = 100)
    private String fieldName;

    @Lob
    private String localValue;

    @Lob
    private String remoteValue;

    @Lob
    private String resolvedValue;

    @NotNull
    @Size(max = 50)
    private String resolutionStrategy;

    @NotNull
    private Boolean isResolved;

    private Instant resolvedAt;

    @NotNull
    private Instant conflictDetectedAt;

    @NotNull
    private Instant createdAt;

    private AppUserDTO conflictUser;

    private AppUserDTO resolvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLocalValue() {
        return localValue;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }

    public String getRemoteValue() {
        return remoteValue;
    }

    public void setRemoteValue(String remoteValue) {
        this.remoteValue = remoteValue;
    }

    public String getResolvedValue() {
        return resolvedValue;
    }

    public void setResolvedValue(String resolvedValue) {
        this.resolvedValue = resolvedValue;
    }

    public String getResolutionStrategy() {
        return resolutionStrategy;
    }

    public void setResolutionStrategy(String resolutionStrategy) {
        this.resolutionStrategy = resolutionStrategy;
    }

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Instant getConflictDetectedAt() {
        return conflictDetectedAt;
    }

    public void setConflictDetectedAt(Instant conflictDetectedAt) {
        this.conflictDetectedAt = conflictDetectedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AppUserDTO getConflictUser() {
        return conflictUser;
    }

    public void setConflictUser(AppUserDTO conflictUser) {
        this.conflictUser = conflictUser;
    }

    public AppUserDTO getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(AppUserDTO resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConflictResolutionDTO)) {
            return false;
        }

        ConflictResolutionDTO conflictResolutionDTO = (ConflictResolutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conflictResolutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConflictResolutionDTO{" +
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
            ", conflictUser=" + getConflictUser() +
            ", resolvedBy=" + getResolvedBy() +
            "}";
    }
}
