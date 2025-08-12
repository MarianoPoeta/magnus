package com.magnus.repository;

import com.magnus.domain.Notification;
import com.magnus.domain.enumeration.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find notifications by target role or global notifications.
     */
    List<Notification> findByTargetRoleOrIsGlobalTrue(UserRole targetRole);
    
    /**
     * Find unread notifications by target role or global unread notifications.
     */
    List<Notification> findByTargetRoleAndIsReadFalseOrIsGlobalTrueAndIsReadFalse(UserRole targetRole);
}
