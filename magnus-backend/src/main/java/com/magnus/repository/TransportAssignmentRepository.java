package com.magnus.repository;

import com.magnus.domain.TransportAssignment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransportAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransportAssignmentRepository extends JpaRepository<TransportAssignment, Long> {}
