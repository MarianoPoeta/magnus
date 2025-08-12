package com.magnus.service.dto;

import com.magnus.domain.enumeration.AuditAction;
import com.magnus.domain.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.AuditLog} entity.
 */
@Schema(description = "Comprehensive audit trail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditLogDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String entityType;

    @NotNull
    @Size(max = 100)
    private String entityId;

    @NotNull
    private AuditAction action;

    @Lob
    private String oldValue;

    @Lob
    private String newValue;

    @Size(max = 100)
    private String fieldName;

    @Size(max = 50)
    private String userId;

    private UserRole userRole;

    @NotNull
    private Instant timestamp;

    @Size(max = 45)
    private String ipAddress;

    @Lob
    private String userAgent;

    @Size(max = 100)
    private String sessionId;

    private AppUserDTO user;

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

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AppUserDTO getUser() {
        return user;
    }

    public void setUser(AppUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLogDTO)) {
            return false;
        }

        AuditLogDTO auditLogDTO = (AuditLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, auditLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuditLogDTO{" +
            "id=" + getId() +
            ", entityType='" + getEntityType() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", action='" + getAction() + "'" +
            ", oldValue='" + getOldValue() + "'" +
            ", newValue='" + getNewValue() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userRole='" + getUserRole() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", sessionId='" + getSessionId() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
