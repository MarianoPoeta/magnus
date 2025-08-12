package com.magnus.service.dto;

import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.CookingSchedule} entity.
 */
@Schema(description = "Cooking schedules with ingredient management")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookingScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    private LocalTime cookingTime;

    @NotNull
    private MealType mealType;

    @NotNull
    @Size(max = 100)
    private String menuName;

    @NotNull
    @Min(value = 1)
    private Integer guestCount;

    @Lob
    private String specialInstructions;

    @NotNull
    private Boolean isCompleted;

    private Instant completedAt;

    @NotNull
    private Boolean ingredientsReady;

    @Min(value = 0)
    private Integer estimatedDuration;

    @Min(value = 0)
    private Integer actualDuration;

    private Instant startedAt;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private ConflictStatus conflictStatus;

    @Size(max = 50)
    private String lastModifiedBy;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private TaskDTO relatedTask;

    @NotNull
    private BudgetDTO budget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(LocalTime cookingTime) {
        this.cookingTime = cookingTime;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getIngredientsReady() {
        return ingredientsReady;
    }

    public void setIngredientsReady(Boolean ingredientsReady) {
        this.ingredientsReady = ingredientsReady;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Integer getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ConflictStatus getConflictStatus() {
        return conflictStatus;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TaskDTO getRelatedTask() {
        return relatedTask;
    }

    public void setRelatedTask(TaskDTO relatedTask) {
        this.relatedTask = relatedTask;
    }

    public BudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetDTO budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookingScheduleDTO)) {
            return false;
        }

        CookingScheduleDTO cookingScheduleDTO = (CookingScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cookingScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookingScheduleDTO{" +
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
            ", relatedTask=" + getRelatedTask() +
            ", budget=" + getBudget() +
            "}";
    }
}
