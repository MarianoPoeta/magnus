package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.NotificationType;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * System notifications with role targeting
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_role")
    private UserRole targetRole;

    @Size(max = 50)
    @Column(name = "related_entity_type", length = 50)
    private String relatedEntityType;

    @Size(max = 100)
    @Column(name = "related_entity_id", length = 100)
    private String relatedEntityId;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @NotNull
    @Column(name = "is_global", nullable = false)
    private Boolean isGlobal;

    @NotNull
    @Column(name = "action_required", nullable = false)
    private Boolean actionRequired;

    @Size(max = 500)
    @Column(name = "action_url", length = 500)
    private String actionUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "read_at")
    private Instant readAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

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
    private AppUser targetUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Notification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return this.type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public UserRole getTargetRole() {
        return this.targetRole;
    }

    public Notification targetRole(UserRole targetRole) {
        this.setTargetRole(targetRole);
        return this;
    }

    public void setTargetRole(UserRole targetRole) {
        this.targetRole = targetRole;
    }

    public String getRelatedEntityType() {
        return this.relatedEntityType;
    }

    public Notification relatedEntityType(String relatedEntityType) {
        this.setRelatedEntityType(relatedEntityType);
        return this;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public String getRelatedEntityId() {
        return this.relatedEntityId;
    }

    public Notification relatedEntityId(String relatedEntityId) {
        this.setRelatedEntityId(relatedEntityId);
        return this;
    }

    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public Notification isRead(Boolean isRead) {
        this.setIsRead(isRead);
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsGlobal() {
        return this.isGlobal;
    }

    public Notification isGlobal(Boolean isGlobal) {
        this.setIsGlobal(isGlobal);
        return this;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Boolean getActionRequired() {
        return this.actionRequired;
    }

    public Notification actionRequired(Boolean actionRequired) {
        this.setActionRequired(actionRequired);
        return this;
    }

    public void setActionRequired(Boolean actionRequired) {
        this.actionRequired = actionRequired;
    }

    public String getActionUrl() {
        return this.actionUrl;
    }

    public Notification actionUrl(String actionUrl) {
        this.setActionUrl(actionUrl);
        return this;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public TaskPriority getPriority() {
        return this.priority;
    }

    public Notification priority(TaskPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public Notification expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getReadAt() {
        return this.readAt;
    }

    public Notification readAt(Instant readAt) {
        this.setReadAt(readAt);
        return this;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Notification createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AppUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public Notification createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    public AppUser getTargetUser() {
        return this.targetUser;
    }

    public void setTargetUser(AppUser appUser) {
        this.targetUser = appUser;
    }

    public Notification targetUser(AppUser appUser) {
        this.setTargetUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
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
            "}";
    }
}
