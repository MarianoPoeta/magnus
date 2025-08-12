package com.magnus.repository;

import com.magnus.domain.FoodItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FoodItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {}
