package com.magnus.repository;

import com.magnus.domain.Accommodation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Accommodation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {}
