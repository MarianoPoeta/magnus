package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.TaskStatus;
import com.magnus.domain.enumeration.TaskType;
import com.magnus.domain.enumeration.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task management with workflow automation
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

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
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TaskType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "assigned_to_role", nullable = false)
    private UserRole assignedToRole;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "due_time")
    private LocalTime dueTime;

    @Min(value = 0)
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Min(value = 0)
    @Column(name = "actual_duration")
    private Integer actualDuration;

    @Size(max = 200)
    @Column(name = "location", length = 200)
    private String location;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Size(max = 500)
    @Column(name = "invoice_url", length = 500)
    private String invoiceUrl;

    @NotNull
    @Column(name = "auto_scheduled", nullable = false)
    private Boolean autoScheduled;

    @NotNull
    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring;

    @Size(max = 100)
    @Column(name = "parent_task_id", length = 100)
    private String parentTaskId;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @NotNull
    @Min(value = 1)
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conflict_status", nullable = false)
    private ConflictStatus conflictStatus;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requestedBy", "fulfilledBy", "parentTask" }, allowSetters = true)
    private Set<Need> needs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "relatedTask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "relatedTask", "foodItem", "activity" }, allowSetters = true)
    private Set<ProductRequirement> productRequirements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dependentTask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prerequisiteTask", "dependentTask" }, allowSetters = true)
    private Set<TaskDependency> taskDependencies = new HashSet<>();

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
    private AppUser assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "budgets", "shoppingItems", "tasks", "createdBy" }, allowSetters = true)
    private WeeklyPlan weeklyPlan;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "budgetItems",
            "payments",
            "tasks",
            "transportAssignments",
            "cookingSchedules",
            "createdBy",
            "assignedTo",
            "client",
            "template",
            "weeklyPlan",
        },
        allowSetters = true
    )
    private Budget relatedBudget;

    @JsonIgnoreProperties(value = { "relatedTask", "cookingIngredients", "budget" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "relatedTask")
    private CookingSchedule cookingSchedule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Task title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Task description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return this.type;
    }

    public Task type(TaskType type) {
        this.setType(type);
        return this;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskPriority getPriority() {
        return this.priority;
    }

    public Task priority(TaskPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public Task status(TaskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserRole getAssignedToRole() {
        return this.assignedToRole;
    }

    public Task assignedToRole(UserRole assignedToRole) {
        this.setAssignedToRole(assignedToRole);
        return this;
    }

    public void setAssignedToRole(UserRole assignedToRole) {
        this.assignedToRole = assignedToRole;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Task dueDate(LocalDate dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalTime getDueTime() {
        return this.dueTime;
    }

    public Task dueTime(LocalTime dueTime) {
        this.setDueTime(dueTime);
        return this;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

    public Integer getEstimatedDuration() {
        return this.estimatedDuration;
    }

    public Task estimatedDuration(Integer estimatedDuration) {
        this.setEstimatedDuration(estimatedDuration);
        return this;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Integer getActualDuration() {
        return this.actualDuration;
    }

    public Task actualDuration(Integer actualDuration) {
        this.setActualDuration(actualDuration);
        return this;
    }

    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getLocation() {
        return this.location;
    }

    public Task location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequirements() {
        return this.requirements;
    }

    public Task requirements(String requirements) {
        this.setRequirements(requirements);
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getNotes() {
        return this.notes;
    }

    public Task notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getInvoiceUrl() {
        return this.invoiceUrl;
    }

    public Task invoiceUrl(String invoiceUrl) {
        this.setInvoiceUrl(invoiceUrl);
        return this;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public Boolean getAutoScheduled() {
        return this.autoScheduled;
    }

    public Task autoScheduled(Boolean autoScheduled) {
        this.setAutoScheduled(autoScheduled);
        return this;
    }

    public void setAutoScheduled(Boolean autoScheduled) {
        this.autoScheduled = autoScheduled;
    }

    public Boolean getIsRecurring() {
        return this.isRecurring;
    }

    public Task isRecurring(Boolean isRecurring) {
        this.setIsRecurring(isRecurring);
        return this;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getParentTaskId() {
        return this.parentTaskId;
    }

    public Task parentTaskId(String parentTaskId) {
        this.setParentTaskId(parentTaskId);
        return this;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public Task completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public Task startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Task version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ConflictStatus getConflictStatus() {
        return this.conflictStatus;
    }

    public Task conflictStatus(ConflictStatus conflictStatus) {
        this.setConflictStatus(conflictStatus);
        return this;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Task lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Task createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Task updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Need> getNeeds() {
        return this.needs;
    }

    public void setNeeds(Set<Need> needs) {
        if (this.needs != null) {
            this.needs.forEach(i -> i.setParentTask(null));
        }
        if (needs != null) {
            needs.forEach(i -> i.setParentTask(this));
        }
        this.needs = needs;
    }

    public Task needs(Set<Need> needs) {
        this.setNeeds(needs);
        return this;
    }

    public Task addNeed(Need need) {
        this.needs.add(need);
        need.setParentTask(this);
        return this;
    }

    public Task removeNeed(Need need) {
        this.needs.remove(need);
        need.setParentTask(null);
        return this;
    }

    public Set<ProductRequirement> getProductRequirements() {
        return this.productRequirements;
    }

    public void setProductRequirements(Set<ProductRequirement> productRequirements) {
        if (this.productRequirements != null) {
            this.productRequirements.forEach(i -> i.setRelatedTask(null));
        }
        if (productRequirements != null) {
            productRequirements.forEach(i -> i.setRelatedTask(this));
        }
        this.productRequirements = productRequirements;
    }

    public Task productRequirements(Set<ProductRequirement> productRequirements) {
        this.setProductRequirements(productRequirements);
        return this;
    }

    public Task addProductRequirement(ProductRequirement productRequirement) {
        this.productRequirements.add(productRequirement);
        productRequirement.setRelatedTask(this);
        return this;
    }

    public Task removeProductRequirement(ProductRequirement productRequirement) {
        this.productRequirements.remove(productRequirement);
        productRequirement.setRelatedTask(null);
        return this;
    }

    public Set<TaskDependency> getTaskDependencies() {
        return this.taskDependencies;
    }

    public void setTaskDependencies(Set<TaskDependency> taskDependencies) {
        if (this.taskDependencies != null) {
            this.taskDependencies.forEach(i -> i.setDependentTask(null));
        }
        if (taskDependencies != null) {
            taskDependencies.forEach(i -> i.setDependentTask(this));
        }
        this.taskDependencies = taskDependencies;
    }

    public Task taskDependencies(Set<TaskDependency> taskDependencies) {
        this.setTaskDependencies(taskDependencies);
        return this;
    }

    public Task addTaskDependencies(TaskDependency taskDependency) {
        this.taskDependencies.add(taskDependency);
        taskDependency.setDependentTask(this);
        return this;
    }

    public Task removeTaskDependencies(TaskDependency taskDependency) {
        this.taskDependencies.remove(taskDependency);
        taskDependency.setDependentTask(null);
        return this;
    }

    public AppUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public Task createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    public AppUser getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(AppUser appUser) {
        this.assignedTo = appUser;
    }

    public Task assignedTo(AppUser appUser) {
        this.setAssignedTo(appUser);
        return this;
    }

    public WeeklyPlan getWeeklyPlan() {
        return this.weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlan weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    public Task weeklyPlan(WeeklyPlan weeklyPlan) {
        this.setWeeklyPlan(weeklyPlan);
        return this;
    }

    public Budget getRelatedBudget() {
        return this.relatedBudget;
    }

    public void setRelatedBudget(Budget budget) {
        this.relatedBudget = budget;
    }

    public Task relatedBudget(Budget budget) {
        this.setRelatedBudget(budget);
        return this;
    }

    public CookingSchedule getCookingSchedule() {
        return this.cookingSchedule;
    }

    public void setCookingSchedule(CookingSchedule cookingSchedule) {
        if (this.cookingSchedule != null) {
            this.cookingSchedule.setRelatedTask(null);
        }
        if (cookingSchedule != null) {
            cookingSchedule.setRelatedTask(this);
        }
        this.cookingSchedule = cookingSchedule;
    }

    public Task cookingSchedule(CookingSchedule cookingSchedule) {
        this.setCookingSchedule(cookingSchedule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return getId() != null && getId().equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
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
            "}";
    }
}
