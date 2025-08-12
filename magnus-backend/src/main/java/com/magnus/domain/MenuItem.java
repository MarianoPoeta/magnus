package com.magnus.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.magnus.domain.enumeration.FoodCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Individual menu items
 */
@Entity
@Table(name = "menu_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuItem implements Serializable {

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
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @DecimalMin(value = "0")
    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    @Lob
    @Column(name = "allergens")
    private String allergens;

    @Lob
    @Column(name = "dietary_info")
    private String dietaryInfo;

    @Min(value = 0)
    @Column(name = "preparation_time")
    private Integer preparationTime;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "menuItems", "includedFoodItems" }, allowSetters = true)
    private Menu menu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MenuItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MenuItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MenuItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodCategory getCategory() {
        return this.category;
    }

    public MenuItem category(FoodCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MenuItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public MenuItem cost(BigDecimal cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getAllergens() {
        return this.allergens;
    }

    public MenuItem allergens(String allergens) {
        this.setAllergens(allergens);
        return this;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getDietaryInfo() {
        return this.dietaryInfo;
    }

    public MenuItem dietaryInfo(String dietaryInfo) {
        this.setDietaryInfo(dietaryInfo);
        return this;
    }

    public void setDietaryInfo(String dietaryInfo) {
        this.dietaryInfo = dietaryInfo;
    }

    public Integer getPreparationTime() {
        return this.preparationTime;
    }

    public MenuItem preparationTime(Integer preparationTime) {
        this.setPreparationTime(preparationTime);
        return this;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MenuItem isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public MenuItem createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public MenuItem updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MenuItem menu(Menu menu) {
        this.setMenu(menu);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItem)) {
            return false;
        }
        return getId() != null && getId().equals(((MenuItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", price=" + getPrice() +
            ", cost=" + getCost() +
            ", allergens='" + getAllergens() + "'" +
            ", dietaryInfo='" + getDietaryInfo() + "'" +
            ", preparationTime=" + getPreparationTime() +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
