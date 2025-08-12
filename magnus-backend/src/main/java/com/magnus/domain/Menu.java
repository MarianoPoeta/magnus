package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.MenuType;
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
 * Menu templates and configurations
 */
@Entity
@Table(name = "menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Menu implements Serializable {

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
    private MenuType type;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_per_person", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerPerson;

    @DecimalMin(value = "0")
    @Column(name = "cost_per_person", precision = 21, scale = 2)
    private BigDecimal costPerPerson;

    @NotNull
    @Min(value = 1)
    @Column(name = "min_people", nullable = false)
    private Integer minPeople;

    @Min(value = 1)
    @Column(name = "max_people")
    private Integer maxPeople;

    @NotNull
    @Size(max = 100)
    @Column(name = "restaurant", length = 100, nullable = false)
    private String restaurant;

    @Min(value = 0)
    @Column(name = "preparation_time")
    private Integer preparationTime;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "is_template", nullable = false)
    private Boolean isTemplate;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menu" }, allowSetters = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_menu__included_food_items",
        joinColumns = @JoinColumn(name = "menu_id"),
        inverseJoinColumns = @JoinColumn(name = "included_food_items_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productRequirements", "availableMenus" }, allowSetters = true)
    private Set<FoodItem> includedFoodItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Menu name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Menu description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MenuType getType() {
        return this.type;
    }

    public Menu type(MenuType type) {
        this.setType(type);
        return this;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public BigDecimal getPricePerPerson() {
        return this.pricePerPerson;
    }

    public Menu pricePerPerson(BigDecimal pricePerPerson) {
        this.setPricePerPerson(pricePerPerson);
        return this;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public BigDecimal getCostPerPerson() {
        return this.costPerPerson;
    }

    public Menu costPerPerson(BigDecimal costPerPerson) {
        this.setCostPerPerson(costPerPerson);
        return this;
    }

    public void setCostPerPerson(BigDecimal costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    public Integer getMinPeople() {
        return this.minPeople;
    }

    public Menu minPeople(Integer minPeople) {
        this.setMinPeople(minPeople);
        return this;
    }

    public void setMinPeople(Integer minPeople) {
        this.minPeople = minPeople;
    }

    public Integer getMaxPeople() {
        return this.maxPeople;
    }

    public Menu maxPeople(Integer maxPeople) {
        this.setMaxPeople(maxPeople);
        return this;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getRestaurant() {
        return this.restaurant;
    }

    public Menu restaurant(String restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getPreparationTime() {
        return this.preparationTime;
    }

    public Menu preparationTime(Integer preparationTime) {
        this.setPreparationTime(preparationTime);
        return this;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Menu isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return this.isTemplate;
    }

    public Menu isTemplate(Boolean isTemplate) {
        this.setIsTemplate(isTemplate);
        return this;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Menu version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Menu createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Menu updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        if (this.menuItems != null) {
            this.menuItems.forEach(i -> i.setMenu(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setMenu(this));
        }
        this.menuItems = menuItems;
    }

    public Menu menuItems(Set<MenuItem> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public Menu addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setMenu(this);
        return this;
    }

    public Menu removeMenuItem(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setMenu(null);
        return this;
    }

    public Set<FoodItem> getIncludedFoodItems() {
        return this.includedFoodItems;
    }

    public void setIncludedFoodItems(Set<FoodItem> foodItems) {
        this.includedFoodItems = foodItems;
    }

    public Menu includedFoodItems(Set<FoodItem> foodItems) {
        this.setIncludedFoodItems(foodItems);
        return this;
    }

    public Menu addIncludedFoodItems(FoodItem foodItem) {
        this.includedFoodItems.add(foodItem);
        return this;
    }

    public Menu removeIncludedFoodItems(FoodItem foodItem) {
        this.includedFoodItems.remove(foodItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return getId() != null && getId().equals(((Menu) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
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
            "}";
    }
}
