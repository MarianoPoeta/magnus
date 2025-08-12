package com.magnus.repository;

import com.magnus.domain.ShoppingItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShoppingItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {}
