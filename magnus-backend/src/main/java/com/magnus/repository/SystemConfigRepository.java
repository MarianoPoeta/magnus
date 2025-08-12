package com.magnus.repository;

import com.magnus.domain.SystemConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {}
