package com.magnus.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.TransportAssignment} entity.
 */
@Schema(description = "Transport assignments for budgets")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransportAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer guestCount;

    @NotNull
    @DecimalMin(value = "0")
    private Double duration;

    @DecimalMin(value = "0")
    private Double distance;

    @Size(max = 200)
    private String pickupLocation;

    @Size(max = 200)
    private String dropoffLocation;

    private Instant pickupTime;

    private Instant returnTime;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal calculatedPrice;

    @DecimalMin(value = "0")
    private BigDecimal calculatedCost;

    @Lob
    private String notes;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private TransportDTO transport;

    private BudgetDTO budget;

    private ActivityDTO activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public Instant getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Instant pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Instant getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Instant returnTime) {
        this.returnTime = returnTime;
    }

    public BigDecimal getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public BigDecimal getCalculatedCost() {
        return calculatedCost;
    }

    public void setCalculatedCost(BigDecimal calculatedCost) {
        this.calculatedCost = calculatedCost;
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

    public TransportDTO getTransport() {
        return transport;
    }

    public void setTransport(TransportDTO transport) {
        this.transport = transport;
    }

    public BudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetDTO budget) {
        this.budget = budget;
    }

    public ActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransportAssignmentDTO)) {
            return false;
        }

        TransportAssignmentDTO transportAssignmentDTO = (TransportAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transportAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportAssignmentDTO{" +
            "id=" + getId() +
            ", guestCount=" + getGuestCount() +
            ", duration=" + getDuration() +
            ", distance=" + getDistance() +
            ", pickupLocation='" + getPickupLocation() + "'" +
            ", dropoffLocation='" + getDropoffLocation() + "'" +
            ", pickupTime='" + getPickupTime() + "'" +
            ", returnTime='" + getReturnTime() + "'" +
            ", calculatedPrice=" + getCalculatedPrice() +
            ", calculatedCost=" + getCalculatedCost() +
            ", notes='" + getNotes() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", transport=" + getTransport() +
            ", budget=" + getBudget() +
            ", activity=" + getActivity() +
            "}";
    }
}
