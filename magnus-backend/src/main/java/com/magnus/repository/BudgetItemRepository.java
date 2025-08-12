package com.magnus.repository;

import com.magnus.domain.BudgetItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BudgetItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {}
