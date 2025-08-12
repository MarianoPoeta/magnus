package com.magnus.service.dto;

import com.magnus.domain.enumeration.NotificationType;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Notification} entity.
 */
@Schema(description = "System notifications with role targeting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String title;

    @Lob
    private String message;

    @NotNull
    private NotificationType type;

    private UserRole targetRole;

    @Size(max = 50)
    private String relatedEntityType;

    @Size(max = 100)
    private String relatedEntityId;

    @NotNull
    private Boolean isRead;

    @NotNull
    private Boolean isGlobal;

    @NotNull
    private Boolean actionRequired;

    @Size(max = 500)
    private String actionUrl;

    @NotNull
    private TaskPriority priority;

    private Instant expiresAt;

    private Instant readAt;

    @NotNull
    private Instant createdAt;

    @NotNull
    private AppUserDTO createdBy;

    private AppUserDTO targetUser;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public UserRole getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(UserRole targetRole) {
        this.targetRole = targetRole;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public String getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Boolean getActionRequired() {
        return actionRequired;
    }

    public void setActionRequired(Boolean actionRequired) {
        this.actionRequired = actionRequired;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AppUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public AppUserDTO getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(AppUserDTO targetUser) {
        this.targetUser = targetUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", type='" + getType() + "'" +
            ", targetRole='" + getTargetRole() + "'" +
            ", relatedEntityType='" + getRelatedEntityType() + "'" +
            ", relatedEntityId='" + getRelatedEntityId() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", isGlobal='" + getIsGlobal() + "'" +
            ", actionRequired='" + getActionRequired() + "'" +
            ", actionUrl='" + getActionUrl() + "'" +
            ", priority='" + getPriority() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", readAt='" + getReadAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", targetUser=" + getTargetUser() +
            "}";
    }
}
