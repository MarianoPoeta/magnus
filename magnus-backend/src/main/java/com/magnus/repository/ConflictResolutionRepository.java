package com.magnus.repository;

import com.magnus.domain.ConflictResolution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConflictResolution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConflictResolutionRepository extends JpaRepository<ConflictResolution, Long> {}
