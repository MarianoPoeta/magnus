package com.magnus.repository;

import com.magnus.domain.CookingSchedule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CookingSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CookingScheduleRepository extends JpaRepository<CookingSchedule, Long> {}
