package com.magnus.repository;

import com.magnus.domain.WorkflowTrigger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkflowTrigger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowTriggerRepository extends JpaRepository<WorkflowTrigger, Long> {}
