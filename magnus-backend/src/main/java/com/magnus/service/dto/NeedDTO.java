package com.magnus.service.dto;

import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Need} entity.
 */
@Schema(description = "Additional needs and requirements")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NeedDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String description;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @Size(max = 20)
    private String unit;

    @NotNull
    private TaskPriority urgency;

    @NotNull
    private TaskStatus status;

    @NotNull
    private LocalDate requestedDate;

    private LocalDate requiredDate;

    private LocalDate fulfilledDate;

    @DecimalMin(value = "0")
    private BigDecimal estimatedCost;

    @DecimalMin(value = "0")
    private BigDecimal actualCost;

    @Lob
    private String notes;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private AppUserDTO requestedBy;

    private AppUserDTO fulfilledBy;

    @NotNull
    private TaskDTO parentTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public TaskPriority getUrgency() {
        return urgency;
    }

    public void setUrgency(TaskPriority urgency) {
        this.urgency = urgency;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(LocalDate requiredDate) {
        this.requiredDate = requiredDate;
    }

    public LocalDate getFulfilledDate() {
        return fulfilledDate;
    }

    public void setFulfilledDate(LocalDate fulfilledDate) {
        this.fulfilledDate = fulfilledDate;
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

    public AppUserDTO getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(AppUserDTO requestedBy) {
        this.requestedBy = requestedBy;
    }

    public AppUserDTO getFulfilledBy() {
        return fulfilledBy;
    }

    public void setFulfilledBy(AppUserDTO fulfilledBy) {
        this.fulfilledBy = fulfilledBy;
    }

    public TaskDTO getParentTask() {
        return parentTask;
    }

    public void setParentTask(TaskDTO parentTask) {
        this.parentTask = parentTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NeedDTO)) {
            return false;
        }

        NeedDTO needDTO = (NeedDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, needDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NeedDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unit='" + getUnit() + "'" +
            ", urgency='" + getUrgency() + "'" +
            ", status='" + getStatus() + "'" +
            ", requestedDate='" + getRequestedDate() + "'" +
            ", requiredDate='" + getRequiredDate() + "'" +
            ", fulfilledDate='" + getFulfilledDate() + "'" +
            ", estimatedCost=" + getEstimatedCost() +
            ", actualCost=" + getActualCost() +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", requestedBy=" + getRequestedBy() +
            ", fulfilledBy=" + getFulfilledBy() +
            ", parentTask=" + getParentTask() +
            "}";
    }
}
