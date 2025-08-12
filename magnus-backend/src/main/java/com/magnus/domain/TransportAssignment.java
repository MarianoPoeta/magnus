package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Transport assignments for budgets
 */
@Entity
@Table(name = "transport_assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransportAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "guest_count", nullable = false)
    private Integer guestCount;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "duration", nullable = false)
    private Double duration;

    @DecimalMin(value = "0")
    @Column(name = "distance")
    private Double distance;

    @Size(max = 200)
    @Column(name = "pickup_location", length = 200)
    private String pickupLocation;

    @Size(max = 200)
    @Column(name = "dropoff_location", length = 200)
    private String dropoffLocation;

    @Column(name = "pickup_time")
    private Instant pickupTime;

    @Column(name = "return_time")
    private Instant returnTime;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "calculated_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal calculatedPrice;

    @DecimalMin(value = "0")
    @Column(name = "calculated_cost", precision = 21, scale = 2)
    private BigDecimal calculatedCost;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productRequirements", "transportAssignments" }, allowSetters = true)
    private Activity activity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransportAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGuestCount() {
        return this.guestCount;
    }

    public TransportAssignment guestCount(Integer guestCount) {
        this.setGuestCount(guestCount);
        return this;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public Double getDuration() {
        return this.duration;
    }

    public TransportAssignment duration(Double duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDistance() {
        return this.distance;
    }

    public TransportAssignment distance(Double distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getPickupLocation() {
        return this.pickupLocation;
    }

    public TransportAssignment pickupLocation(String pickupLocation) {
        this.setPickupLocation(pickupLocation);
        return this;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return this.dropoffLocation;
    }

    public TransportAssignment dropoffLocation(String dropoffLocation) {
        this.setDropoffLocation(dropoffLocation);
        return this;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public Instant getPickupTime() {
        return this.pickupTime;
    }

    public TransportAssignment pickupTime(Instant pickupTime) {
        this.setPickupTime(pickupTime);
        return this;
    }

    public void setPickupTime(Instant pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Instant getReturnTime() {
        return this.returnTime;
    }

    public TransportAssignment returnTime(Instant returnTime) {
        this.setReturnTime(returnTime);
        return this;
    }

    public void setReturnTime(Instant returnTime) {
        this.returnTime = returnTime;
    }

    public BigDecimal getCalculatedPrice() {
        return this.calculatedPrice;
    }

    public TransportAssignment calculatedPrice(BigDecimal calculatedPrice) {
        this.setCalculatedPrice(calculatedPrice);
        return this;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public BigDecimal getCalculatedCost() {
        return this.calculatedCost;
    }

    public TransportAssignment calculatedCost(BigDecimal calculatedCost) {
        this.setCalculatedCost(calculatedCost);
        return this;
    }

    public void setCalculatedCost(BigDecimal calculatedCost) {
        this.calculatedCost = calculatedCost;
    }

    public String getNotes() {
        return this.notes;
    }

    public TransportAssignment notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public TransportAssignment createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public TransportAssignment updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Transport getTransport() {
        return this.transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public TransportAssignment transport(Transport transport) {
        this.setTransport(transport);
        return this;
    }

    public Budget getBudget() {
        return this.budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public TransportAssignment budget(Budget budget) {
        this.setBudget(budget);
        return this;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public TransportAssignment activity(Activity activity) {
        this.setActivity(activity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransportAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((TransportAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportAssignment{" +
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
            "}";
    }
}
