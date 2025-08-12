package com.magnus.service.dto;

import com.magnus.domain.enumeration.BudgetStatus;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.EventGender;
import com.magnus.domain.enumeration.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.Budget} entity.
 */
@Schema(description = "Core budget entity - triggers workflow automation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String name;

    @NotNull
    @Size(max = 100)
    private String clientName;

    @NotNull
    private LocalDate eventDate;

    @Size(max = 200)
    private String eventLocation;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    private Integer guestCount;

    @NotNull
    private EventGender eventGender;

    @Lob
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal totalAmount;

    @DecimalMin(value = "0")
    private BigDecimal totalCost;

    @DecimalMin(value = "0")
    private BigDecimal profitMargin;

    @DecimalMin(value = "0")
    private BigDecimal mealsAmount;

    @DecimalMin(value = "0")
    private BigDecimal activitiesAmount;

    @DecimalMin(value = "0")
    private BigDecimal transportAmount;

    @DecimalMin(value = "0")
    private BigDecimal accommodationAmount;

    @NotNull
    private BudgetStatus status;

    @NotNull
    private PaymentStatus paymentStatus;

    @NotNull
    private Boolean isClosed;

    @Lob
    private String internalNotes;

    @Lob
    private String clientNotes;

    @Size(max = 100)
    private String templateId;

    @NotNull
    private Boolean workflowTriggered;

    private Instant lastWorkflowExecution;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private ConflictStatus conflictStatus;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant approvedAt;

    private Instant reservedAt;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private AppUserDTO createdBy;

    private AppUserDTO assignedTo;

    @NotNull
    private ClientDTO client;

    private BudgetTemplateDTO template;

    private WeeklyPlanDTO weeklyPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public EventGender getEventGender() {
        return eventGender;
    }

    public void setEventGender(EventGender eventGender) {
        this.eventGender = eventGender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        this.profitMargin = profitMargin;
    }

    public BigDecimal getMealsAmount() {
        return mealsAmount;
    }

    public void setMealsAmount(BigDecimal mealsAmount) {
        this.mealsAmount = mealsAmount;
    }

    public BigDecimal getActivitiesAmount() {
        return activitiesAmount;
    }

    public void setActivitiesAmount(BigDecimal activitiesAmount) {
        this.activitiesAmount = activitiesAmount;
    }

    public BigDecimal getTransportAmount() {
        return transportAmount;
    }

    public void setTransportAmount(BigDecimal transportAmount) {
        this.transportAmount = transportAmount;
    }

    public BigDecimal getAccommodationAmount() {
        return accommodationAmount;
    }

    public void setAccommodationAmount(BigDecimal accommodationAmount) {
        this.accommodationAmount = accommodationAmount;
    }

    public BudgetStatus getStatus() {
        return status;
    }

    public void setStatus(BudgetStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public String getClientNotes() {
        return clientNotes;
    }

    public void setClientNotes(String clientNotes) {
        this.clientNotes = clientNotes;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Boolean getWorkflowTriggered() {
        return workflowTriggered;
    }

    public void setWorkflowTriggered(Boolean workflowTriggered) {
        this.workflowTriggered = workflowTriggered;
    }

    public Instant getLastWorkflowExecution() {
        return lastWorkflowExecution;
    }

    public void setLastWorkflowExecution(Instant lastWorkflowExecution) {
        this.lastWorkflowExecution = lastWorkflowExecution;
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

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Instant reservedAt) {
        this.reservedAt = reservedAt;
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

    public AppUserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(AppUserDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public BudgetTemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(BudgetTemplateDTO template) {
        this.template = template;
    }

    public WeeklyPlanDTO getWeeklyPlan() {
        return weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlanDTO weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetDTO)) {
            return false;
        }

        BudgetDTO budgetDTO = (BudgetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, budgetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", clientName='" + getClientName() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventLocation='" + getEventLocation() + "'" +
            ", guestCount=" + getGuestCount() +
            ", eventGender='" + getEventGender() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalCost=" + getTotalCost() +
            ", profitMargin=" + getProfitMargin() +
            ", mealsAmount=" + getMealsAmount() +
            ", activitiesAmount=" + getActivitiesAmount() +
            ", transportAmount=" + getTransportAmount() +
            ", accommodationAmount=" + getAccommodationAmount() +
            ", status='" + getStatus() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", isClosed='" + getIsClosed() + "'" +
            ", internalNotes='" + getInternalNotes() + "'" +
            ", clientNotes='" + getClientNotes() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", workflowTriggered='" + getWorkflowTriggered() + "'" +
            ", lastWorkflowExecution='" + getLastWorkflowExecution() + "'" +
            ", version=" + getVersion() +
            ", conflictStatus='" + getConflictStatus() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", approvedAt='" + getApprovedAt() + "'" +
            ", reservedAt='" + getReservedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy=" + getCreatedBy() +
            ", assignedTo=" + getAssignedTo() +
            ", client=" + getClient() +
            ", template=" + getTemplate() +
            ", weeklyPlan=" + getWeeklyPlan() +
            "}";
    }
}
