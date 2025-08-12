package com.magnus.service.dto;

import com.magnus.domain.enumeration.MenuType;
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
 * A DTO for the {@link com.magnus.domain.Menu} entity.
 */
@Schema(description = "Menu templates and configurations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    private MenuType type;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal pricePerPerson;

    @DecimalMin(value = "0")
    private BigDecimal costPerPerson;

    @NotNull
    @Min(value = 1)
    private Integer minPeople;

    @Min(value = 1)
    private Integer maxPeople;

    @NotNull
    @Size(max = 100)
    private String restaurant;

    @Min(value = 0)
    private Integer preparationTime;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isTemplate;

    @NotNull
    @Min(value = 1)
    private Integer version;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    private Set<FoodItemDTO> includedFoodItems = new HashSet<>();

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

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public BigDecimal getCostPerPerson() {
        return costPerPerson;
    }

    public void setCostPerPerson(BigDecimal costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    public Integer getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(Integer minPeople) {
        this.minPeople = minPeople;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
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

    public Set<FoodItemDTO> getIncludedFoodItems() {
        return includedFoodItems;
    }

    public void setIncludedFoodItems(Set<FoodItemDTO> includedFoodItems) {
        this.includedFoodItems = includedFoodItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuDTO)) {
            return false;
        }

        MenuDTO menuDTO = (MenuDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menuDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", pricePerPerson=" + getPricePerPerson() +
            ", costPerPerson=" + getCostPerPerson() +
            ", minPeople=" + getMinPeople() +
            ", maxPeople=" + getMaxPeople() +
            ", restaurant='" + getRestaurant() + "'" +
            ", preparationTime=" + getPreparationTime() +
            ", isActive='" + getIsActive() + "'" +
            ", isTemplate='" + getIsTemplate() + "'" +
            ", version=" + getVersion() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", includedFoodItems=" + getIncludedFoodItems() +
            "}";
    }
}
