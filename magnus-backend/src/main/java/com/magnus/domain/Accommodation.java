package com.magnus.domain;

import com.magnus.domain.enumeration.AccommodationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Accommodation catalog and templates
 */
@Entity
@Table(name = "accommodation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Accommodation implements Serializable {

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
    private AccommodationType type;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_per_night", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerNight;

    @DecimalMin(value = "0")
    @Column(name = "cost_per_night", precision = 21, scale = 2)
    private BigDecimal costPerNight;

    @NotNull
    @Min(value = 1)
    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Lob
    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "amenities")
    private String amenities;

    @Size(max = 10)
    @Column(name = "check_in_time", length = 10)
    private String checkInTime;

    @Size(max = 10)
    @Column(name = "check_out_time", length = 10)
    private String checkOutTime;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    @Column(name = "rating")
    private Double rating;

    @Lob
    @Column(name = "contact_info")
    private String contactInfo;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Accommodation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Accommodation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Accommodation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccommodationType getType() {
        return this.type;
    }

    public Accommodation type(AccommodationType type) {
        this.setType(type);
        return this;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public BigDecimal getPricePerNight() {
        return this.pricePerNight;
    }

    public Accommodation pricePerNight(BigDecimal pricePerNight) {
        this.setPricePerNight(pricePerNight);
        return this;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public BigDecimal getCostPerNight() {
        return this.costPerNight;
    }

    public Accommodation costPerNight(BigDecimal costPerNight) {
        this.setCostPerNight(costPerNight);
        return this;
    }

    public void setCostPerNight(BigDecimal costPerNight) {
        this.costPerNight = costPerNight;
    }

    public Integer getMaxOccupancy() {
        return this.maxOccupancy;
    }

    public Accommodation maxOccupancy(Integer maxOccupancy) {
        this.setMaxOccupancy(maxOccupancy);
        return this;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public String getAddress() {
        return this.address;
    }

    public Accommodation address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmenities() {
        return this.amenities;
    }

    public Accommodation amenities(String amenities) {
        this.setAmenities(amenities);
        return this;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getCheckInTime() {
        return this.checkInTime;
    }

    public Accommodation checkInTime(String checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return this.checkOutTime;
    }

    public Accommodation checkOutTime(String checkOutTime) {
        this.setCheckOutTime(checkOutTime);
        return this;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Double getRating() {
        return this.rating;
    }

    public Accommodation rating(Double rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public Accommodation contactInfo(String contactInfo) {
        this.setContactInfo(contactInfo);
        return this;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Accommodation isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTemplate() {
        return this.isTemplate;
    }

    public Accommodation isTemplate(Boolean isTemplate) {
        this.setIsTemplate(isTemplate);
        return this;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Accommodation createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Accommodation updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accommodation)) {
            return false;
        }
        return getId() != null && getId().equals(((Accommodation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Accommodation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", pricePerNight=" + getPricePerNight() +
            ", costPerNight=" + getCostPerNight() +
            ", maxOccupancy=" + getMaxOccupancy() +
            ", address='" + getAddress() + "'" +
            ", amenities='" + getAmenities() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOutTime='" + getCheckOutTime() + "'" +
            ", rating=" + getRating() +
            ", contactInfo='" + getContactInfo() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", isTemplate='" + getIsTemplate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
