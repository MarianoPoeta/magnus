package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.ProductUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Individual cooking ingredients with modification tracking
 */
@Entity
@Table(name = "cooking_ingredient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookingIngredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "original_quantity", nullable = false)
    private Double originalQuantity;

    @DecimalMin(value = "0")
    @Column(name = "modified_quantity")
    private Double modifiedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "modified_unit")
    private ProductUnit modifiedUnit;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "added_by_user", nullable = false)
    private Boolean addedByUser;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "available_at")
    private Instant availableAt;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "product", "relatedTask", "foodItem", "activity" }, allowSetters = true)
    private ProductRequirement productRequirement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "relatedTask", "cookingIngredients", "budget" }, allowSetters = true)
    private CookingSchedule cookingSchedule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CookingIngredient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOriginalQuantity() {
        return this.originalQuantity;
    }

    public CookingIngredient originalQuantity(Double originalQuantity) {
        this.setOriginalQuantity(originalQuantity);
        return this;
    }

    public void setOriginalQuantity(Double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public Double getModifiedQuantity() {
        return this.modifiedQuantity;
    }

    public CookingIngredient modifiedQuantity(Double modifiedQuantity) {
        this.setModifiedQuantity(modifiedQuantity);
        return this;
    }

    public void setModifiedQuantity(Double modifiedQuantity) {
        this.modifiedQuantity = modifiedQuantity;
    }

    public ProductUnit getModifiedUnit() {
        return this.modifiedUnit;
    }

    public CookingIngredient modifiedUnit(ProductUnit modifiedUnit) {
        this.setModifiedUnit(modifiedUnit);
        return this;
    }

    public void setModifiedUnit(ProductUnit modifiedUnit) {
        this.modifiedUnit = modifiedUnit;
    }

    public String getNotes() {
        return this.notes;
    }

    public CookingIngredient notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getAddedByUser() {
        return this.addedByUser;
    }

    public CookingIngredient addedByUser(Boolean addedByUser) {
        this.setAddedByUser(addedByUser);
        return this;
    }

    public void setAddedByUser(Boolean addedByUser) {
        this.addedByUser = addedByUser;
    }

    public Boolean getIsAvailable() {
        return this.isAvailable;
    }

    public CookingIngredient isAvailable(Boolean isAvailable) {
        this.setIsAvailable(isAvailable);
        return this;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Instant getAvailableAt() {
        return this.availableAt;
    }

    public CookingIngredient availableAt(Instant availableAt) {
        this.setAvailableAt(availableAt);
        return this;
    }

    public void setAvailableAt(Instant availableAt) {
        this.availableAt = availableAt;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public CookingIngredient lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getVersion() {
        return this.version;
    }

    public CookingIngredient version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public CookingIngredient createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public CookingIngredient updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductRequirement getProductRequirement() {
        return this.productRequirement;
    }

    public void setProductRequirement(ProductRequirement productRequirement) {
        this.productRequirement = productRequirement;
    }

    public CookingIngredient productRequirement(ProductRequirement productRequirement) {
        this.setProductRequirement(productRequirement);
        return this;
    }

    public CookingSchedule getCookingSchedule() {
        return this.cookingSchedule;
    }

    public void setCookingSchedule(CookingSchedule cookingSchedule) {
        this.cookingSchedule = cookingSchedule;
    }

    public CookingIngredient cookingSchedule(CookingSchedule cookingSchedule) {
        this.setCookingSchedule(cookingSchedule);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookingIngredient)) {
            return false;
        }
        return getId() != null && getId().equals(((CookingIngredient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookingIngredient{" +
            "id=" + getId() +
            ", originalQuantity=" + getOriginalQuantity() +
            ", modifiedQuantity=" + getModifiedQuantity() +
            ", modifiedUnit='" + getModifiedUnit() + "'" +
            ", notes='" + getNotes() + "'" +
            ", addedByUser='" + getAddedByUser() + "'" +
            ", isAvailable='" + getIsAvailable() + "'" +
            ", availableAt='" + getAvailableAt() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", version=" + getVersion() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
