package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.TemplateType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Reusable templates for budget creation
 */
@Entity
@Table(name = "budget_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BudgetTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TemplateType type;

    @Size(max = 50)
    @Column(name = "category", length = 50)
    private String category;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_system", nullable = false)
    private Boolean isSystem;

    @Lob
    @Column(name = "configuration")
    private String configuration;

    @Lob
    @Column(name = "template_data")
    private String templateData;

    @NotNull
    @Min(value = 1)
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
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
    private Set<Budget> budgets = new HashSet<>();

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BudgetTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BudgetTemplate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public BudgetTemplate description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TemplateType getType() {
        return this.type;
    }

    public BudgetTemplate type(TemplateType type) {
        this.setType(type);
        return this;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public String getCategory() {
        return this.category;
    }

    public BudgetTemplate category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public BudgetTemplate isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsSystem() {
        return this.isSystem;
    }

    public BudgetTemplate isSystem(Boolean isSystem) {
        this.setIsSystem(isSystem);
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getConfiguration() {
        return this.configuration;
    }

    public BudgetTemplate configuration(String configuration) {
        this.setConfiguration(configuration);
        return this;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getTemplateData() {
        return this.templateData;
    }

    public BudgetTemplate templateData(String templateData) {
        this.setTemplateData(templateData);
        return this;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public Integer getVersion() {
        return this.version;
    }

    public BudgetTemplate version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public BudgetTemplate createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public BudgetTemplate updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Budget> getBudgets() {
        return this.budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        if (this.budgets != null) {
            this.budgets.forEach(i -> i.setTemplate(null));
        }
        if (budgets != null) {
            budgets.forEach(i -> i.setTemplate(this));
        }
        this.budgets = budgets;
    }

    public BudgetTemplate budgets(Set<Budget> budgets) {
        this.setBudgets(budgets);
        return this;
    }

    public BudgetTemplate addBudget(Budget budget) {
        this.budgets.add(budget);
        budget.setTemplate(this);
        return this;
    }

    public BudgetTemplate removeBudget(Budget budget) {
        this.budgets.remove(budget);
        budget.setTemplate(null);
        return this;
    }

    public AppUser getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(AppUser appUser) {
        this.createdBy = appUser;
    }

    public BudgetTemplate createdBy(AppUser appUser) {
        this.setCreatedBy(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BudgetTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((BudgetTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BudgetTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", category='" + getCategory() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isSystem='" + getIsSystem() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", templateData='" + getTemplateData() + "'" +
            ", version=" + getVersion() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
