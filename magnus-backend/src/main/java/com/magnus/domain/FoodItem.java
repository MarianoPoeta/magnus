package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.FoodCategory;
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
 * Food items catalog
 */
@Entity
@Table(name = "food_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FoodItem implements Serializable {

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
    private FoodCategory category;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "base_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @DecimalMin(value = "0")
    @Column(name = "base_cost", precision = 21, scale = 2)
    private BigDecimal baseCost;

    @Size(max = 50)
    @Column(name = "serving_size", length = 50)
    private String servingSize;

    @NotNull
    @Min(value = 1)
    @Column(name = "guests_per_unit", nullable = false)
    private Integer guestsPerUnit;

    @Min(value = 1)
    @Column(name = "max_units")
    private Integer maxUnits;

    @Lob
    @Column(name = "allergens")
    private String allergens;

    @Lob
    @Column(name = "dietary_info")
    private String dietaryInfo;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "foodItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "relatedTask", "foodItem", "activity" }, allowSetters = true)
    private Set<ProductRequirement> productRequirements = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "includedFoodItems")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "includedFoodItems" }, allowSetters = true)
    private Set<Menu> availableMenus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FoodItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FoodItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public FoodItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodCategory getCategory() {
        return this.category;
    }

    public FoodItem category(FoodCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public FoodItem basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBaseCost() {
        return this.baseCost;
    }

    public FoodItem baseCost(BigDecimal baseCost) {
        this.setBaseCost(baseCost);
        return this;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
    }

    public String getServingSize() {
        return this.servingSize;
    }

    public FoodItem servingSize(String servingSize) {
        this.setServingSize(servingSize);
        return this;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public Integer getGuestsPerUnit() {
        return this.guestsPerUnit;
    }

    public FoodItem guestsPerUnit(Integer guestsPerUnit) {
        this.setGuestsPerUnit(guestsPerUnit);
        return this;
    }

    public void setGuestsPerUnit(Integer guestsPerUnit) {
        this.guestsPerUnit = guestsPerUnit;
    }

    public Integer getMaxUnits() {
        return this.maxUnits;
    }

    public FoodItem maxUnits(Integer maxUnits) {
        this.setMaxUnits(maxUnits);
        return this;
    }

    public void setMaxUnits(Integer maxUnits) {
        this.maxUnits = maxUnits;
    }

    public String getAllergens() {
        return this.allergens;
    }

    public FoodItem allergens(String allergens) {
        this.setAllergens(allergens);
        return this;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getDietaryInfo() {
        return this.dietaryInfo;
    }

    public FoodItem dietaryInfo(String dietaryInfo) {
        this.setDietaryInfo(dietaryInfo);
        return this;
    }

    public void setDietaryInfo(String dietaryInfo) {
        this.dietaryInfo = dietaryInfo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public FoodItem isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return this.isTemplate;
    }

    public FoodItem isTemplate(Boolean isTemplate) {
        this.setIsTemplate(isTemplate);
        return this;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public FoodItem createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public FoodItem updatedAt(Instant updatedAt) {
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
            this.productRequirements.forEach(i -> i.setFoodItem(null));
        }
        if (productRequirements != null) {
            productRequirements.forEach(i -> i.setFoodItem(this));
        }
        this.productRequirements = productRequirements;
    }

    public FoodItem productRequirements(Set<ProductRequirement> productRequirements) {
        this.setProductRequirements(productRequirements);
        return this;
    }

    public FoodItem addProductRequirement(ProductRequirement productRequirement) {
        this.productRequirements.add(productRequirement);
        productRequirement.setFoodItem(this);
        return this;
    }

    public FoodItem removeProductRequirement(ProductRequirement productRequirement) {
        this.productRequirements.remove(productRequirement);
        productRequirement.setFoodItem(null);
        return this;
    }

    public Set<Menu> getAvailableMenus() {
        return this.availableMenus;
    }

    public void setAvailableMenus(Set<Menu> menus) {
        if (this.availableMenus != null) {
            this.availableMenus.forEach(i -> i.removeIncludedFoodItems(this));
        }
        if (menus != null) {
            menus.forEach(i -> i.addIncludedFoodItems(this));
        }
        this.availableMenus = menus;
    }

    public FoodItem availableMenus(Set<Menu> menus) {
        this.setAvailableMenus(menus);
        return this;
    }

    public FoodItem addAvailableMenus(Menu menu) {
        this.availableMenus.add(menu);
        menu.getIncludedFoodItems().add(this);
        return this;
    }

    public FoodItem removeAvailableMenus(Menu menu) {
        this.availableMenus.remove(menu);
        menu.getIncludedFoodItems().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodItem)) {
            return false;
        }
        return getId() != null && getId().equals(((FoodItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodItem{" +
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
            "}";
    }
}
