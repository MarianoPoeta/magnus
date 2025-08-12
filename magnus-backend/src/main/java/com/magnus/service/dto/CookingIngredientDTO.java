package com.magnus.service.dto;

import com.magnus.domain.enumeration.ProductUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.magnus.domain.CookingIngredient} entity.
 */
@Schema(description = "Individual cooking ingredients with modification tracking")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CookingIngredientDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Double originalQuantity;

    @DecimalMin(value = "0")
    private Double modifiedQuantity;

    private ProductUnit modifiedUnit;

    @Lob
    private String notes;

    @NotNull
    private Boolean addedByUser;

    @NotNull
    private Boolean isAvailable;

    private Instant availableAt;

    @Size(max = 50)
    private String lastModifiedBy;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotNull
    private ProductRequirementDTO productRequirement;

    @NotNull
    private CookingScheduleDTO cookingSchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(Double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public Double getModifiedQuantity() {
        return modifiedQuantity;
    }

    public void setModifiedQuantity(Double modifiedQuantity) {
        this.modifiedQuantity = modifiedQuantity;
    }

    public ProductUnit getModifiedUnit() {
        return modifiedUnit;
    }

    public void setModifiedUnit(ProductUnit modifiedUnit) {
        this.modifiedUnit = modifiedUnit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(Boolean addedByUser) {
        this.addedByUser = addedByUser;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Instant getAvailableAt() {
        return availableAt;
    }

    public void setAvailableAt(Instant availableAt) {
        this.availableAt = availableAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public ProductRequirementDTO getProductRequirement() {
        return productRequirement;
    }

    public void setProductRequirement(ProductRequirementDTO productRequirement) {
        this.productRequirement = productRequirement;
    }

    public CookingScheduleDTO getCookingSchedule() {
        return cookingSchedule;
    }

    public void setCookingSchedule(CookingScheduleDTO cookingSchedule) {
        this.cookingSchedule = cookingSchedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CookingIngredientDTO)) {
            return false;
        }

        CookingIngredientDTO cookingIngredientDTO = (CookingIngredientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cookingIngredientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CookingIngredientDTO{" +
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
            ", productRequirement=" + getProductRequirement() +
            ", cookingSchedule=" + getCookingSchedule() +
            "}";
    }
}
