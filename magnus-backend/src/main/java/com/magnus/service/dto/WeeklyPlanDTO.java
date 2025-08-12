package com.magnus.service.dto;

import com.magnus.domain.enumeration.WeeklyPlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.WeeklyPlan} entity.
 */
@Schema(description = "Weekly planning for logistics coordination")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeeklyPlanDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate weekStart;

    @NotNull
    private LocalDate weekEnd;

    @NotNull
    @Size(max = 100)
    private String planName;

    @NotNull
    private WeeklyPlanStatus status;

    @Min(value = 0)
    private Integer totalBudgets;

    @Min(value = 0)
    private Integer totalGuests;

    @DecimalMin(value = "0")
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    private BigDecimal actualCost;

    @Lob
    private String notes;

    @NotNull
    private Boolean isConsolidated;

    private Instant consolidatedAt;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private AppUserDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public WeeklyPlanStatus getStatus() {
        return status;
    }

    public void setStatus(WeeklyPlanStatus status) {
        this.status = status;
    }

    public Integer getTotalBudgets() {
        return totalBudgets;
    }

    public void setTotalBudgets(Integer totalBudgets) {
        this.totalBudgets = totalBudgets;
    }

    public Integer getTotalGuests() {
        return totalGuests;
    }

    public void setTotalGuests(Integer totalGuests) {
        this.totalGuests = totalGuests;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsConsolidated() {
        return isConsolidated;
    }

    public void setIsConsolidated(Boolean isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public Instant getConsolidatedAt() {
        return consolidatedAt;
    }

    public void setConsolidatedAt(Instant consolidatedAt) {
        this.consolidatedAt = consolidatedAt;
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

    public AppUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeeklyPlanDTO)) {
            return false;
        }

        WeeklyPlanDTO weeklyPlanDTO = (WeeklyPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weeklyPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeeklyPlanDTO{" +
            "id=" + getId() +
            ", weekStart='" + getWeekStart() + "'" +
            ", weekEnd='" + getWeekEnd() + "'" +
            ", planName='" + getPlanName() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalBudgets=" + getTotalBudgets() +
            ", totalGuests=" + getTotalGuests() +
            ", estimatedCost=" + getEstimatedCost() +
            ", actualCost=" + getActualCost() +
            ", notes='" + getNotes() + "'" +
            ", isConsolidated='" + getIsConsolidated() + "'" +
            ", consolidatedAt='" + getConsolidatedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
