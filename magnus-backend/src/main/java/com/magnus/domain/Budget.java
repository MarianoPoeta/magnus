package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.BudgetStatus;
import com.magnus.domain.enumeration.ConflictStatus;
import com.magnus.domain.enumeration.EventGender;
import com.magnus.domain.enumeration.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Core budget entity - triggers workflow automation
 */
@Entity
@Table(name = "budget")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @NotNull
    @Size(max = 100)
    @Column(name = "client_name", length = 100, nullable = false)
    private String clientName;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Size(max = 200)
    @Column(name = "event_location", length = 200)
    private String eventLocation;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    @Column(name = "guest_count", nullable = false)
    private Integer guestCount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_gender", nullable = false)
    private EventGender eventGender;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @DecimalMin(value = "0")
    @Column(name = "total_cost", precision = 21, scale = 2)
    private BigDecimal totalCost;

    @DecimalMin(value = "0")
    @Column(name = "profit_margin", precision = 21, scale = 2)
    private BigDecimal profitMargin;

    @DecimalMin(value = "0")
    @Column(name = "meals_amount", precision = 21, scale = 2)
    private BigDecimal mealsAmount;

    @DecimalMin(value = "0")
    @Column(name = "activities_amount", precision = 21, scale = 2)
    private BigDecimal activitiesAmount;

    @DecimalMin(value = "0")
    @Column(name = "transport_amount", precision = 21, scale = 2)
    private BigDecimal transportAmount;

    @DecimalMin(value = "0")
    @Column(name = "accommodation_amount", precision = 21, scale = 2)
    private BigDecimal accommodationAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BudgetStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @NotNull
    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @Lob
    @Column(name = "internal_notes")
    private String internalNotes;

    @Lob
    @Column(name = "client_notes")
    private String clientNotes;

    @Size(max = 100)
    @Column(name = "template_id", length = 100)
    private String templateId;

    @NotNull
    @Column(name = "workflow_triggered", nullable = false)
    private Boolean workflowTriggered;

    @Column(name = "last_workflow_execution")
    private Instant lastWorkflowExecution;

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

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "reserved_at")
    private Instant reservedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "budget" }, allowSetters = true)
    private Set<BudgetItem> budgetItems = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "budget" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "relatedBudget")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "needs", "productRequirements", "taskDependencies", "createdBy", "assignedTo", "weeklyPlan", "relatedBudget", "cookingSchedule",
        },
        allowSetters = true
    )
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transport", "budget", "activity" }, allowSetters = true)
    private Set<TransportAssignment> transportAssignments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relatedTask", "cookingIngredients", "budget" }, allowSetters = true)
    private Set<CookingSchedule> cookingSchedules = new HashSet<>();

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "budgets" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_template_id")
    @JsonIgnoreProperties(value = { "budgets", "createdBy" }, allowSetters = true)
    private BudgetTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "budgets", "shoppingItems", "tasks", "createdBy" }, allowSetters = true)
    private WeeklyPlan weeklyPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Budget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Budget name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientName() {
        return this.clientName;
    }

    public Budget clientName(String clientName) {
        this.setClientName(clientName);
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getEventDate() {
        return this.eventDate;
    }

    public Budget eventDate(LocalDate eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return this.eventLocation;
    }

    public Budget eventLocation(String eventLocation) {
        this.setEventLocation(eventLocation);
        return this;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Integer getGuestCount() {
        return this.guestCount;
    }

    public Budget guestCount(Integer guestCount) {
        this.setGuestCount(guestCount);
        return this;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public EventGender getEventGender() {
        return this.eventGender;
    }

    public Budget eventGender(EventGender eventGender) {
        this.setEventGender(eventGender);
        return this;
    }

    public void setEventGender(EventGender eventGender) {
        this.eventGender = eventGender;
    }

    public String getDescription() {
        return this.description;
    }

    public Budget description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Budget totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalCost() {
        return this.totalCost;
    }

    public Budget totalCost(BigDecimal totalCost) {
        this.setTotalCost(totalCost);
        return this;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getProfitMargin() {
        return this.profitMargin;
    }

    public Budget profitMargin(BigDecimal profitMargin) {
        this.setProfitMargin(profitMargin);
        return this;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        this.profitMargin = profitMargin;
    }

    public BigDecimal getMealsAmount() {
        return this.mealsAmount;
    }

    public Budget mealsAmount(BigDecimal mealsAmount) {
        this.setMealsAmount(mealsAmount);
        return this;
    }

    public void setMealsAmount(BigDecimal mealsAmount) {
        this.mealsAmount = mealsAmount;
    }

    public BigDecimal getActivitiesAmount() {
        return this.activitiesAmount;
    }

    public Budget activitiesAmount(BigDecimal activitiesAmount) {
        this.setActivitiesAmount(activitiesAmount);
        return this;
    }

    public void setActivitiesAmount(BigDecimal activitiesAmount) {
        this.activitiesAmount = activitiesAmount;
    }

    public BigDecimal getTransportAmount() {
        return this.transportAmount;
    }

    public Budget transportAmount(BigDecimal transportAmount) {
        this.setTransportAmount(transportAmount);
        return this;
    }

    public void setTransportAmount(BigDecimal transportAmount) {
        this.transportAmount = transportAmount;
    }

    public BigDecimal getAccommodationAmount() {
        return this.accommodationAmount;
    }

    public Budget accommodationAmount(BigDecimal accommodationAmount) {
        this.setAccommodationAmount(accommodationAmount);
        return this;
    }

    public void setAccommodationAmount(BigDecimal accommodationAmount) {
        this.accommodationAmount = accommodationAmount;
    }

    public BudgetStatus getStatus() {
        return this.status;
    }

    public Budget status(BudgetStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BudgetStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Budget paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getIsClosed() {
        return this.isClosed;
    }

    public Budget isClosed(Boolean isClosed) {
        this.setIsClosed(isClosed);
        return this;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public String getInternalNotes() {
        return this.internalNotes;
    }

    public Budget internalNotes(String internalNotes) {
        this.setInternalNotes(internalNotes);
        return this;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public String getClientNotes() {
        return this.clientNotes;
    }

    public Budget clientNotes(String clientNotes) {
        this.setClientNotes(clientNotes);
        return this;
    }

    public void setClientNotes(String clientNotes) {
        this.clientNotes = clientNotes;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public Budget templateId(String templateId) {
        this.setTemplateId(templateId);
        return this;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Boolean getWorkflowTriggered() {
        return this.workflowTriggered;
    }

    public Budget workflowTriggered(Boolean workflowTriggered) {
        this.setWorkflowTriggered(workflowTriggered);
        return this;
    }

    public void setWorkflowTriggered(Boolean workflowTriggered) {
        this.workflowTriggered = workflowTriggered;
    }

    public Instant getLastWorkflowExecution() {
        return this.lastWorkflowExecution;
    }

    public Budget lastWorkflowExecution(Instant lastWorkflowExecution) {
        this.setLastWorkflowExecution(lastWorkflowExecution);
        return this;
    }

    public void setLastWorkflowExecution(Instant lastWorkflowExecution) {
        this.lastWorkflowExecution = lastWorkflowExecution;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Budget version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ConflictStatus getConflictStatus() {
        return this.conflictStatus;
    }

    public Budget conflictStatus(ConflictStatus conflictStatus) {
        this.setConflictStatus(conflictStatus);
        return this;
    }

    public void setConflictStatus(ConflictStatus conflictStatus) {
        this.conflictStatus = conflictStatus;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Budget lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getApprovedAt() {
        return this.approvedAt;
    }

    public Budget approvedAt(Instant approvedAt) {
        this.setApprovedAt(approvedAt);
        return this;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Instant getReservedAt() {
        return this.reservedAt;
    }

    public Budget reservedAt(Instant reservedAt) {
        this.setReservedAt(reservedAt);
        return this;
    }

    public void setReservedAt(Instant reservedAt) {
        this.reservedAt = reservedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Budget createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Budget updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<BudgetItem> getBudgetItems() {
        return this.budgetItems;
    }

    public void setBudgetItems(Set<BudgetItem> budgetItems) {
        if (this.budgetItems != null) {
            this.budgetItems.forEach(i -> i.setBudget(null));
        }
        if (budgetItems != null) {
            budgetItems.forEach(i -> i.setBudget(this));
        }
        this.budgetItems = budgetItems;
    }

    public Budget budgetItems(Set<BudgetItem> budgetItems) {
        this.setBudgetItems(budgetItems);
        return this;
    }

    public Budget addBudgetItem(BudgetItem budgetItem) {
        this.budgetItems.add(budgetItem);
        budgetItem.setBudget(this);
        return this;
    }

    public Budget removeBudgetItem(BudgetItem budgetItem) {
        this.budgetItems.remove(budgetItem);
        budgetItem.setBudget(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setBudget(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setBudget(this));
        }
        this.payments = payments;
    }

    public Budget payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Budget addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setBudget(this);
        return this;
    }

    public Budget removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setBudget(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setRelatedBudget(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setRelatedBudget(this));
        }
        this.tasks = tasks;
    }

    public Budget tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Budget addTask(Task task) {
        this.tasks.add(task);
        task.setRelatedBudget(this);
        return this;
    }

    public Budget removeTask(Task task) {
        this.tasks.remove(task);
        task.setRelatedBudget(null);
        return this;
    }

    public Set<TransportAssignment> getTransportAssignments() {
        return this.transportAssignments;
    }

    public void setTransportAssignments(Set<TransportAssignment> transportAssignments) {
        if (this.transportAssignments != null) {
            this.transportAssignments.forEach(i -> i.setBudget(null));
        }
        if (transportAssignments != null) {
            transportAssignments.forEach(i -> i.setBudget(this));
        }
        this.transportAssignments = transportAssignments;
    }

    public Budget transportAssignments(Set<TransportAssignment> transportAssignments) {
        this.setTransportAssignments(transportAssignments);
        return this;
    }

    public Budget addTransportAssignment(TransportAssignment transportAssignment) {
        this.transportAssignments.add(transportAssignment);
        transportAssignment.setBudget(this);
        return this;
    }

    public Budget removeTransportAssignment(TransportAssignment transportAssignment) {
        this.transportAssignments.remove(transportAssignment);
        transportAssignment.setBudget(null);
        return this;
    }

    public Set<CookingSchedule> getCookingSchedules() {
        return this.cookingSchedules;
    }

    public void setCookingSchedules(Set<CookingSchedule> cookingSchedules) {
        if (this.cookingSchedules != null) {
            this.cookingSchedules.forEach(i -> i.setBudget(null));
        }
        if (cookingSchedules != null) {
            cookingSchedules.forEach(i -> i.setBudget(this));
        }
        this.cookingSchedules = cookingSchedules;
    }

    public Budget cookingSchedules(Set<CookingSchedule> cookingSchedules) {
        this.setCookingSchedules(cookingSchedules);
        return this;
    }

    public Budget addCookingSchedule(CookingSchedule cookingSchedule) {
        this.cookingSchedules.add(cookingSchedule);
        cookingSchedule.setBudget(this);
        return this;
    }

    public Budget removeCookingSchedule(CookingSchedule cookingSchedule) {
        this.cookingSchedules.remove(cookingSchedule);
        cookingSchedule.setBudget(null);
        return this;
    }

    public AppUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public Budget createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    public AppUser getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(AppUser appUser) {
        this.assignedTo = appUser;
    }

    public Budget assignedTo(AppUser appUser) {
        this.setAssignedTo(appUser);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Budget client(Client client) {
        this.setClient(client);
        return this;
    }

    public BudgetTemplate getTemplate() {
        return this.template;
    }

    public void setTemplate(BudgetTemplate budgetTemplate) {
        this.template = budgetTemplate;
    }

    public Budget template(BudgetTemplate budgetTemplate) {
        this.setTemplate(budgetTemplate);
        return this;
    }

    public WeeklyPlan getWeeklyPlan() {
        return this.weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlan weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    public Budget weeklyPlan(WeeklyPlan weeklyPlan) {
        this.setWeeklyPlan(weeklyPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Budget)) {
            return false;
        }
        return getId() != null && getId().equals(((Budget) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Budget{" +
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
            "}";
    }
}
