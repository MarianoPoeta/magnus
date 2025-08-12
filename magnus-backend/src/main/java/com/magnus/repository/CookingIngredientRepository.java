package com.magnus.repository;

import com.magnus.domain.CookingIngredient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CookingIngredient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CookingIngredientRepository extends JpaRepository<CookingIngredient, Long> {}
