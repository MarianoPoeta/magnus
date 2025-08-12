package com.magnus.service.dto;

import com.magnus.domain.enumeration.FoodCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.magnus.domain.FoodItem} entity.
 */
@Schema(description = "Food items catalog")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FoodItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    private FoodCategory category;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0")
    private BigDecimal baseCost;

    @Size(max = 50)
    private String servingSize;

    @NotNull
    @Min(value = 1)
    private Integer guestsPerUnit;

    @Min(value = 1)
    private Integer maxUnits;

    @Lob
    private String allergens;

    @Lob
    private String dietaryInfo;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isTemplate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private Set<MenuDTO> availableMenus = new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public Integer getGuestsPerUnit() {
        return guestsPerUnit;
    }

    public void setGuestsPerUnit(Integer guestsPerUnit) {
        this.guestsPerUnit = guestsPerUnit;
    }

    public Integer getMaxUnits() {
        return maxUnits;
    }

    public void setMaxUnits(Integer maxUnits) {
        this.maxUnits = maxUnits;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getDietaryInfo() {
        return dietaryInfo;
    }

    public void setDietaryInfo(String dietaryInfo) {
        this.dietaryInfo = dietaryInfo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
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

    public Set<MenuDTO> getAvailableMenus() {
        return availableMenus;
    }

    public void setAvailableMenus(Set<MenuDTO> availableMenus) {
        this.availableMenus = availableMenus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodItemDTO)) {
            return false;
        }

        FoodItemDTO foodItemDTO = (FoodItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, foodItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", basePrice=" + getBasePrice() +
            ", baseCost=" + getBaseCost() +
            ", servingSize='" + getServingSize() + "'" +
            ", guestsPerUnit=" + getGuestsPerUnit() +
            ", maxUnits=" + getMaxUnits() +
            ", allergens='" + getAllergens() + "'" +
            ", dietaryInfo='" + getDietaryInfo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isTemplate='" + getIsTemplate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", availableMenus=" + getAvailableMenus() +
            "}";
    }
}
