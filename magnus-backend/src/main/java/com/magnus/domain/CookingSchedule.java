package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.MealType;
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
 * Cooking schedules with ingredient management
 */
@Entity
@Table(name = "cooking_schedule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookingSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @NotNull
    @Column(name = "cooking_time", nullable = false)
    private LocalTime cookingTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @NotNull
    @Size(max = 100)
    @Column(name = "menu_name", length = 100, nullable = false)
    private String menuName;

    @NotNull
    @Min(value = 1)
    @Column(name = "guest_count", nullable = false)
    private Integer guestCount;

    @Lob
    @Column(name = "special_instructions")
    private String specialInstructions;

    @NotNull
    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Column(name = "completed_at")
    private Instant completedAt;

    @NotNull
    @Column(name = "ingredients_ready", nullable = false)
    private Boolean ingredientsReady;

    @Min(value = 0)
    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Min(value = 0)
    @Column(name = "actual_duration")
    private Integer actualDuration;

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

    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Task relatedTask;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cookingSchedule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productRequirement", "cookingSchedule" }, allowSetters = true)
    private Set<CookingIngredient> cookingIngredients = new HashSet<>();

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
    private Budget budget;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CookingSchedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return this.eventDate;
    }

    public CookingSchedule eventDate(LocalDate eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getCookingTime() {
        return this.cookingTime;
    }

    public CookingSchedule cookingTime(LocalTime cookingTime) {
        this.setCookingTime(cookingTime);
        return this;
    }

    public void setCookingTime(LocalTime cookingTime) {
        this.cookingTime = cookingTime;
    }

    public MealType getMealType() {
        return this.mealType;
    }

    public CookingSchedule mealType(MealType mealType) {
        this.setMealType(mealType);
        return this;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public CookingSchedule menuName(String menuName) {
        this.setMenuName(menuName);
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getGuestCount() {
        return this.guestCount;
    }

    public CookingSchedule guestCount(Integer guestCount) {
        this.setGuestCount(guestCount);
        return this;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public CookingSchedule specialInstructions(String specialInstructions) {
        this.setSpecialInstructions(specialInstructions);
        return this;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Boolean getIsCompleted() {
        return this.isCompleted;
    }

    public CookingSchedule isCompleted(Boolean isCompleted) {
        this.setIsCompleted(isCompleted);
        return this;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public CookingSchedule completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getIngredientsReady() {
        return this.ingredientsReady;
    }

    public CookingSchedule ingredientsReady(Boolean ingredientsReady) {
        this.setIngredientsReady(ingredientsReady);
        return this;
    }

    public void setIngredientsReady(Boolean ingredientsReady) {
        this.ingredientsReady = ingredientsReady;
    }

    public Integer getEstimatedDuration() {
        return this.estimatedDuration;
    }

    public CookingSchedule estimatedDuration(Integer estimatedDuration) {
        this.setEstimatedDuration(estimatedDuration);
        return this;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Integer getActualDuration() {
        return this.actualDuration;
    }

    public CookingSchedule actualDuration(Integer actualDuration) {
        this.setActualDuration(actualDuration);
        return this;
    }

    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public CookingSchedule startedAt(Instant startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Integer getVersion() {
        return this.version;
    }

    public CookingSchedule version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ConflictStatus getConflictStatus() {
        return this.conflictStatus;
    }

    public CookingSchedule conflictStatus(ConflictStatus conflictStatus) {
        this.setConflictStatus(conflictStatus);
        return this;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public CookingSchedule lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CookingSchedule createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CookingSchedule updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Task getRelatedTask() {
        return this.relatedTask;
    }

    public void setRelatedTask(Task task) {
        this.relatedTask = task;
    }

    public CookingSchedule relatedTask(Task task) {
        this.setRelatedTask(task);
        return this;
    }

    public Set<CookingIngredient> getCookingIngredients() {
        return this.cookingIngredients;
    }

    public void setCookingIngredients(Set<CookingIngredient> cookingIngredients) {
        if (this.cookingIngredients != null) {
            this.cookingIngredients.forEach(i -> i.setCookingSchedule(null));
        }
        if (cookingIngredients != null) {
            cookingIngredients.forEach(i -> i.setCookingSchedule(this));
        }
        this.cookingIngredients = cookingIngredients;
    }

    public CookingSchedule cookingIngredients(Set<CookingIngredient> cookingIngredients) {
        this.setCookingIngredients(cookingIngredients);
        return this;
    }

    public CookingSchedule addCookingIngredient(CookingIngredient cookingIngredient) {
        this.cookingIngredients.add(cookingIngredient);
        cookingIngredient.setCookingSchedule(this);
        return this;
    }

    public CookingSchedule removeCookingIngredient(CookingIngredient cookingIngredient) {
        this.cookingIngredients.remove(cookingIngredient);
        cookingIngredient.setCookingSchedule(null);
        return this;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public CookingSchedule budget(Budget budget) {
        this.setBudget(budget);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookingSchedule)) {
            return false;
        }
        return getId() != null && getId().equals(((CookingSchedule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookingSchedule{" +
            "id=" + getId() +
            ", eventDate='" + getEventDate() + "'" +
            ", cookingTime='" + getCookingTime() + "'" +
            ", mealType='" + getMealType() + "'" +
            ", menuName='" + getMenuName() + "'" +
            ", guestCount=" + getGuestCount() +
            ", specialInstructions='" + getSpecialInstructions() + "'" +
            ", isCompleted='" + getIsCompleted() + "'" +
            ", completedAt='" + getCompletedAt() + "'" +
            ", ingredientsReady='" + getIngredientsReady() + "'" +
            ", estimatedDuration=" + getEstimatedDuration() +
            ", actualDuration=" + getActualDuration() +
            ", startedAt='" + getStartedAt() + "'" +
            ", version=" + getVersion() +
            ", conflictStatus='" + getConflictStatus() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
