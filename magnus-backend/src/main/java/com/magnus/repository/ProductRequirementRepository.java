package com.magnus.repository;

import com.magnus.domain.ProductRequirement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductRequirement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRequirementRepository extends JpaRepository<ProductRequirement, Long> {}
