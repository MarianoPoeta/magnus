package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Custom User entity with role-based access control
 * Replaces JHipster's built-in User entity
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$")
    @Column(name = "login", length = 50, nullable = false, unique = true)
    private String login;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @NotNull
    @Size(min = 5, max = 254)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", length = 254, nullable = false, unique = true)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Size(max = 500)
    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Lob
    @Column(name = "preferences")
    private String preferences;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Budget> createdBudgets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedTo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Budget> assignedBudgets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Set<Task> createdTasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedTo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Set<Task> assignedTasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requestedBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requestedBy", "fulfilledBy", "parentTask" }, allowSetters = true)
    private Set<Need> requestedNeeds = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fulfilledBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requestedBy", "fulfilledBy", "parentTask" }, allowSetters = true)
    private Set<Need> fulfilledNeeds = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createdBy" }, allowSetters = true)
    private Set<Product> createdProducts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createdBy", "targetUser" }, allowSetters = true)
    private Set<Notification> createdNotifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "targetUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createdBy", "targetUser" }, allowSetters = true)
    private Set<Notification> receivedNotifications = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchasedBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "purchasedBy", "weeklyPlan" }, allowSetters = true)
    private Set<ShoppingItem> purchasedItems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "budgets", "shoppingItems", "tasks", "createdBy" }, allowSetters = true)
    private Set<WeeklyPlan> createdWeeklyPlans = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "budgets", "createdBy" }, allowSetters = true)
    private Set<BudgetTemplate> createdTemplates = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createdBy" }, allowSetters = true)
    private Set<SystemConfig> createdConfigs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createdBy" }, allowSetters = true)
    private Set<WorkflowTrigger> createdTriggers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resolvedBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conflictUser", "resolvedBy" }, allowSetters = true)
    private Set<ConflictResolution> resolvedConflicts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public AppUser login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public AppUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public AppUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public AppUser phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return this.role;
    }

    public AppUser role(UserRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AppUser isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public AppUser profilePicture(String profilePicture) {
        this.setProfilePicture(profilePicture);
        return this;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPreferences() {
        return this.preferences;
    }

    public AppUser preferences(String preferences) {
        this.setPreferences(preferences);
        return this;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Instant getLastLoginAt() {
        return this.lastLoginAt;
    }

    public AppUser lastLoginAt(Instant lastLoginAt) {
        this.setLastLoginAt(lastLoginAt);
        return this;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public AppUser createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public AppUser updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Budget> getCreatedBudgets() {
        return this.createdBudgets;
    }

    public void setCreatedBudgets(Set<Budget> budgets) {
        if (this.createdBudgets != null) {
            this.createdBudgets.forEach(i -> i.setCreatedBy(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setCreatedBy(this));
        }
        this.createdBudgets = budgets;
    }

    public AppUser createdBudgets(Set<Budget> budgets) {
        this.setCreatedBudgets(budgets);
        return this;
    }

    public AppUser addCreatedBudgets(Budget budget) {
        this.createdBudgets.add(budget);
        budget.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedBudgets(Budget budget) {
        this.createdBudgets.remove(budget);
        budget.setCreatedBy(null);
        return this;
    }

    public Set<Budget> getAssignedBudgets() {
        return this.assignedBudgets;
    }

    public void setAssignedBudgets(Set<Budget> budgets) {
        if (this.assignedBudgets != null) {
            this.assignedBudgets.forEach(i -> i.setAssignedTo(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setAssignedTo(this));
        }
        this.assignedBudgets = budgets;
    }

    public AppUser assignedBudgets(Set<Budget> budgets) {
        this.setAssignedBudgets(budgets);
        return this;
    }

    public AppUser addAssignedBudgets(Budget budget) {
        this.assignedBudgets.add(budget);
        budget.setAssignedTo(this);
        return this;
    }

    public AppUser removeAssignedBudgets(Budget budget) {
        this.assignedBudgets.remove(budget);
        budget.setAssignedTo(null);
        return this;
    }

    public Set<Task> getCreatedTasks() {
        return this.createdTasks;
    }

    public void setCreatedTasks(Set<Task> tasks) {
        if (this.createdTasks != null) {
            this.createdTasks.forEach(i -> i.setCreatedBy(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setCreatedBy(this));
        }
        this.createdTasks = tasks;
    }

    public AppUser createdTasks(Set<Task> tasks) {
        this.setCreatedTasks(tasks);
        return this;
    }

    public AppUser addCreatedTasks(Task task) {
        this.createdTasks.add(task);
        task.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedTasks(Task task) {
        this.createdTasks.remove(task);
        task.setCreatedBy(null);
        return this;
    }

    public Set<Task> getAssignedTasks() {
        return this.assignedTasks;
    }

    public void setAssignedTasks(Set<Task> tasks) {
        if (this.assignedTasks != null) {
            this.assignedTasks.forEach(i -> i.setAssignedTo(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setAssignedTo(this));
        }
        this.assignedTasks = tasks;
    }

    public AppUser assignedTasks(Set<Task> tasks) {
        this.setAssignedTasks(tasks);
        return this;
    }

    public AppUser addAssignedTasks(Task task) {
        this.assignedTasks.add(task);
        task.setAssignedTo(this);
        return this;
    }

    public AppUser removeAssignedTasks(Task task) {
        this.assignedTasks.remove(task);
        task.setAssignedTo(null);
        return this;
    }

    public Set<Need> getRequestedNeeds() {
        return this.requestedNeeds;
    }

    public void setRequestedNeeds(Set<Need> needs) {
        if (this.requestedNeeds != null) {
            this.requestedNeeds.forEach(i -> i.setRequestedBy(null));
        }
        if (needs != null) {
            needs.forEach(i -> i.setRequestedBy(this));
        }
        this.requestedNeeds = needs;
    }

    public AppUser requestedNeeds(Set<Need> needs) {
        this.setRequestedNeeds(needs);
        return this;
    }

    public AppUser addRequestedNeeds(Need need) {
        this.requestedNeeds.add(need);
        need.setRequestedBy(this);
        return this;
    }

    public AppUser removeRequestedNeeds(Need need) {
        this.requestedNeeds.remove(need);
        need.setRequestedBy(null);
        return this;
    }

    public Set<Need> getFulfilledNeeds() {
        return this.fulfilledNeeds;
    }

    public void setFulfilledNeeds(Set<Need> needs) {
        if (this.fulfilledNeeds != null) {
            this.fulfilledNeeds.forEach(i -> i.setFulfilledBy(null));
        }
        if (needs != null) {
            needs.forEach(i -> i.setFulfilledBy(this));
        }
        this.fulfilledNeeds = needs;
    }

    public AppUser fulfilledNeeds(Set<Need> needs) {
        this.setFulfilledNeeds(needs);
        return this;
    }

    public AppUser addFulfilledNeeds(Need need) {
        this.fulfilledNeeds.add(need);
        need.setFulfilledBy(this);
        return this;
    }

    public AppUser removeFulfilledNeeds(Need need) {
        this.fulfilledNeeds.remove(need);
        need.setFulfilledBy(null);
        return this;
    }

    public Set<Product> getCreatedProducts() {
        return this.createdProducts;
    }

    public void setCreatedProducts(Set<Product> products) {
        if (this.createdProducts != null) {
            this.createdProducts.forEach(i -> i.setCreatedBy(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCreatedBy(this));
        }
        this.createdProducts = products;
    }

    public AppUser createdProducts(Set<Product> products) {
        this.setCreatedProducts(products);
        return this;
    }

    public AppUser addCreatedProducts(Product product) {
        this.createdProducts.add(product);
        product.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedProducts(Product product) {
        this.createdProducts.remove(product);
        product.setCreatedBy(null);
        return this;
    }

    public Set<Notification> getCreatedNotifications() {
        return this.createdNotifications;
    }

    public void setCreatedNotifications(Set<Notification> notifications) {
        if (this.createdNotifications != null) {
            this.createdNotifications.forEach(i -> i.setCreatedBy(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setCreatedBy(this));
        }
        this.createdNotifications = notifications;
    }

    public AppUser createdNotifications(Set<Notification> notifications) {
        this.setCreatedNotifications(notifications);
        return this;
    }

    public AppUser addCreatedNotifications(Notification notification) {
        this.createdNotifications.add(notification);
        notification.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedNotifications(Notification notification) {
        this.createdNotifications.remove(notification);
        notification.setCreatedBy(null);
        return this;
    }

    public Set<Notification> getReceivedNotifications() {
        return this.receivedNotifications;
    }

    public void setReceivedNotifications(Set<Notification> notifications) {
        if (this.receivedNotifications != null) {
            this.receivedNotifications.forEach(i -> i.setTargetUser(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setTargetUser(this));
        }
        this.receivedNotifications = notifications;
    }

    public AppUser receivedNotifications(Set<Notification> notifications) {
        this.setReceivedNotifications(notifications);
        return this;
    }

    public AppUser addReceivedNotifications(Notification notification) {
        this.receivedNotifications.add(notification);
        notification.setTargetUser(this);
        return this;
    }

    public AppUser removeReceivedNotifications(Notification notification) {
        this.receivedNotifications.remove(notification);
        notification.setTargetUser(null);
        return this;
    }

    public Set<ShoppingItem> getPurchasedItems() {
        return this.purchasedItems;
    }

    public void setPurchasedItems(Set<ShoppingItem> shoppingItems) {
        if (this.purchasedItems != null) {
            this.purchasedItems.forEach(i -> i.setPurchasedBy(null));
        }
        if (shoppingItems != null) {
            shoppingItems.forEach(i -> i.setPurchasedBy(this));
        }
        this.purchasedItems = shoppingItems;
    }

    public AppUser purchasedItems(Set<ShoppingItem> shoppingItems) {
        this.setPurchasedItems(shoppingItems);
        return this;
    }

    public AppUser addPurchasedItems(ShoppingItem shoppingItem) {
        this.purchasedItems.add(shoppingItem);
        shoppingItem.setPurchasedBy(this);
        return this;
    }

    public AppUser removePurchasedItems(ShoppingItem shoppingItem) {
        this.purchasedItems.remove(shoppingItem);
        shoppingItem.setPurchasedBy(null);
        return this;
    }

    public Set<WeeklyPlan> getCreatedWeeklyPlans() {
        return this.createdWeeklyPlans;
    }

    public void setCreatedWeeklyPlans(Set<WeeklyPlan> weeklyPlans) {
        if (this.createdWeeklyPlans != null) {
            this.createdWeeklyPlans.forEach(i -> i.setCreatedBy(null));
        }
        if (weeklyPlans != null) {
            weeklyPlans.forEach(i -> i.setCreatedBy(this));
        }
        this.createdWeeklyPlans = weeklyPlans;
    }

    public AppUser createdWeeklyPlans(Set<WeeklyPlan> weeklyPlans) {
        this.setCreatedWeeklyPlans(weeklyPlans);
        return this;
    }

    public AppUser addCreatedWeeklyPlans(WeeklyPlan weeklyPlan) {
        this.createdWeeklyPlans.add(weeklyPlan);
        weeklyPlan.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedWeeklyPlans(WeeklyPlan weeklyPlan) {
        this.createdWeeklyPlans.remove(weeklyPlan);
        weeklyPlan.setCreatedBy(null);
        return this;
    }

    public Set<BudgetTemplate> getCreatedTemplates() {
        return this.createdTemplates;
    }

    public void setCreatedTemplates(Set<BudgetTemplate> budgetTemplates) {
        if (this.createdTemplates != null) {
            this.createdTemplates.forEach(i -> i.setCreatedBy(null));
        }
        if (budgetTemplates != null) {
            budgetTemplates.forEach(i -> i.setCreatedBy(this));
        }
        this.createdTemplates = budgetTemplates;
    }

    public AppUser createdTemplates(Set<BudgetTemplate> budgetTemplates) {
        this.setCreatedTemplates(budgetTemplates);
        return this;
    }

    public AppUser addCreatedTemplates(BudgetTemplate budgetTemplate) {
        this.createdTemplates.add(budgetTemplate);
        budgetTemplate.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedTemplates(BudgetTemplate budgetTemplate) {
        this.createdTemplates.remove(budgetTemplate);
        budgetTemplate.setCreatedBy(null);
        return this;
    }

    public Set<SystemConfig> getCreatedConfigs() {
        return this.createdConfigs;
    }

    public void setCreatedConfigs(Set<SystemConfig> systemConfigs) {
        if (this.createdConfigs != null) {
            this.createdConfigs.forEach(i -> i.setCreatedBy(null));
        }
        if (systemConfigs != null) {
            systemConfigs.forEach(i -> i.setCreatedBy(this));
        }
        this.createdConfigs = systemConfigs;
    }

    public AppUser createdConfigs(Set<SystemConfig> systemConfigs) {
        this.setCreatedConfigs(systemConfigs);
        return this;
    }

    public AppUser addCreatedConfigs(SystemConfig systemConfig) {
        this.createdConfigs.add(systemConfig);
        systemConfig.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedConfigs(SystemConfig systemConfig) {
        this.createdConfigs.remove(systemConfig);
        systemConfig.setCreatedBy(null);
        return this;
    }

    public Set<WorkflowTrigger> getCreatedTriggers() {
        return this.createdTriggers;
    }

    public void setCreatedTriggers(Set<WorkflowTrigger> workflowTriggers) {
        if (this.createdTriggers != null) {
            this.createdTriggers.forEach(i -> i.setCreatedBy(null));
        }
        if (workflowTriggers != null) {
            workflowTriggers.forEach(i -> i.setCreatedBy(this));
        }
        this.createdTriggers = workflowTriggers;
    }

    public AppUser createdTriggers(Set<WorkflowTrigger> workflowTriggers) {
        this.setCreatedTriggers(workflowTriggers);
        return this;
    }

    public AppUser addCreatedTriggers(WorkflowTrigger workflowTrigger) {
        this.createdTriggers.add(workflowTrigger);
        workflowTrigger.setCreatedBy(this);
        return this;
    }

    public AppUser removeCreatedTriggers(WorkflowTrigger workflowTrigger) {
        this.createdTriggers.remove(workflowTrigger);
        workflowTrigger.setCreatedBy(null);
        return this;
    }

    public Set<ConflictResolution> getResolvedConflicts() {
        return this.resolvedConflicts;
    }

    public void setResolvedConflicts(Set<ConflictResolution> conflictResolutions) {
        if (this.resolvedConflicts != null) {
            this.resolvedConflicts.forEach(i -> i.setResolvedBy(null));
        }
        if (conflictResolutions != null) {
            conflictResolutions.forEach(i -> i.setResolvedBy(this));
        }
        this.resolvedConflicts = conflictResolutions;
    }

    public AppUser resolvedConflicts(Set<ConflictResolution> conflictResolutions) {
        this.setResolvedConflicts(conflictResolutions);
        return this;
    }

    public AppUser addResolvedConflicts(ConflictResolution conflictResolution) {
        this.resolvedConflicts.add(conflictResolution);
        conflictResolution.setResolvedBy(this);
        return this;
    }

    public AppUser removeResolvedConflicts(ConflictResolution conflictResolution) {
        this.resolvedConflicts.remove(conflictResolution);
        conflictResolution.setResolvedBy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", role='" + getRole() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", profilePicture='" + getProfilePicture() + "'" +
            ", preferences='" + getPreferences() + "'" +
            ", lastLoginAt='" + getLastLoginAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
