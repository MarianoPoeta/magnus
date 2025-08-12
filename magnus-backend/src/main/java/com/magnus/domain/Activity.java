package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ActivityCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Activity catalog and templates
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activity implements Serializable {

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
    @Column(name = "category", nullable = false)
    private ActivityCategory category;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "base_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @DecimalMin(value = "0")
    @Column(name = "base_cost", precision = 21, scale = 2)
    private BigDecimal baseCost;

    @NotNull
    @Min(value = 1)
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Min(value = 1)
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @NotNull
    @Size(max = 200)
    @Column(name = "location", length = 200, nullable = false)
    private String location;

    @NotNull
    @Column(name = "transport_required", nullable = false)
    private Boolean transportRequired;

    @NotNull
    @Column(name = "transport_included", nullable = false)
    private Boolean transportIncluded;

    @Lob
    @Column(name = "equipment_provided")
    private String equipmentProvided;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_template", nullable = false)
    private Boolean isTemplate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "relatedTask", "foodItem", "activity" }, allowSetters = true)
    private Set<ProductRequirement> productRequirements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activity")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transport", "budget", "activity" }, allowSetters = true)
    private Set<TransportAssignment> transportAssignments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Activity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Activity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityCategory getCategory() {
        return this.category;
    }

    public Activity category(ActivityCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public Activity basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBaseCost() {
        return this.baseCost;
    }

    public Activity baseCost(BigDecimal baseCost) {
        this.setBaseCost(baseCost);
        return this;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Activity duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMaxCapacity() {
        return this.maxCapacity;
    }

    public Activity maxCapacity(Integer maxCapacity) {
        this.setMaxCapacity(maxCapacity);
        return this;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getLocation() {
        return this.location;
    }

    public Activity location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getTransportRequired() {
        return this.transportRequired;
    }

    public Activity transportRequired(Boolean transportRequired) {
        this.setTransportRequired(transportRequired);
        return this;
    }

    public void setTransportRequired(Boolean transportRequired) {
        this.transportRequired = transportRequired;
    }

    public Boolean getTransportIncluded() {
        return this.transportIncluded;
    }

    public Activity transportIncluded(Boolean transportIncluded) {
        this.setTransportIncluded(transportIncluded);
        return this;
    }

    public void setTransportIncluded(Boolean transportIncluded) {
        this.transportIncluded = transportIncluded;
    }

    public String getEquipmentProvided() {
        return this.equipmentProvided;
    }

    public Activity equipmentProvided(String equipmentProvided) {
        this.setEquipmentProvided(equipmentProvided);
        return this;
    }

    public void setEquipmentProvided(String equipmentProvided) {
        this.equipmentProvided = equipmentProvided;
    }

    public String getRequirements() {
        return this.requirements;
    }

    public Activity requirements(String requirements) {
        this.setRequirements(requirements);
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Activity isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return this.isTemplate;
    }

    public Activity isTemplate(Boolean isTemplate) {
        this.setIsTemplate(isTemplate);
        return this;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Activity createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Activity updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ProductRequirement> getProductRequirements() {
        return this.productRequirements;
    }

    public void setProductRequirements(Set<ProductRequirement> productRequirements) {
        if (this.productRequirements != null) {
            this.productRequirements.forEach(i -> i.setActivity(null));
        }
        if (productRequirements != null) {
            productRequirements.forEach(i -> i.setActivity(this));
        }
        this.productRequirements = productRequirements;
    }

    public Activity productRequirements(Set<ProductRequirement> productRequirements) {
        this.setProductRequirements(productRequirements);
        return this;
    }

    public Activity addProductRequirement(ProductRequirement productRequirement) {
        this.productRequirements.add(productRequirement);
        productRequirement.setActivity(this);
        return this;
    }

    public Activity removeProductRequirement(ProductRequirement productRequirement) {
        this.productRequirements.remove(productRequirement);
        productRequirement.setActivity(null);
        return this;
    }

    public Set<TransportAssignment> getTransportAssignments() {
        return this.transportAssignments;
    }

    public void setTransportAssignments(Set<TransportAssignment> transportAssignments) {
        if (this.transportAssignments != null) {
            this.transportAssignments.forEach(i -> i.setActivity(null));
        }
        if (transportAssignments != null) {
            transportAssignments.forEach(i -> i.setActivity(this));
        }
        this.transportAssignments = transportAssignments;
    }

    public Activity transportAssignments(Set<TransportAssignment> transportAssignments) {
        this.setTransportAssignments(transportAssignments);
        return this;
    }

    public Activity addTransportAssignment(TransportAssignment transportAssignment) {
        this.transportAssignments.add(transportAssignment);
        transportAssignment.setActivity(this);
        return this;
    }

    public Activity removeTransportAssignment(TransportAssignment transportAssignment) {
        this.transportAssignments.remove(transportAssignment);
        transportAssignment.setActivity(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return getId() != null && getId().equals(((Activity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", basePrice=" + getBasePrice() +
            ", baseCost=" + getBaseCost() +
            ", duration=" + getDuration() +
            ", maxCapacity=" + getMaxCapacity() +
            ", location='" + getLocation() + "'" +
            ", transportRequired='" + getTransportRequired() + "'" +
            ", transportIncluded='" + getTransportIncluded() + "'" +
            ", equipmentProvided='" + getEquipmentProvided() + "'" +
            ", requirements='" + getRequirements() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isTemplate='" + getIsTemplate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
