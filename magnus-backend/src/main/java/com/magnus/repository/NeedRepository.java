package com.magnus.repository;

import com.magnus.domain.Need;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Need entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NeedRepository extends JpaRepository<Need, Long> {}
