package com.magnus.repository;

import com.magnus.domain.WeeklyPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WeeklyPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {}
