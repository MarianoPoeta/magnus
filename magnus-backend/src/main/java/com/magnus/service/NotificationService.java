package com.magnus.service;

import com.magnus.domain.enumeration.UserRole;
import com.magnus.service.dto.NotificationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.magnus.domain.Notification}.
 */
public interface NotificationService {
    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationDTO save(NotificationDTO notificationDTO);

    /**
     * Updates a notification.
     *
     * @param notificationDTO the entity to update.
     * @return the persisted entity.
     */
    NotificationDTO update(NotificationDTO notificationDTO);

    /**
     * Partially updates a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO);

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificationDTO> findAll(Pageable pageable);

    /**
     * Get all notifications for a specific user role.
     *
     * @param targetRole the target role.
     * @return the list of entities.
     */
    List<NotificationDTO> findByTargetRole(UserRole targetRole);

    /**
     * Get all unread notifications for a specific user role.
     *
     * @param targetRole the target role.
     * @return the list of entities.
     */
    List<NotificationDTO> findUnreadByTargetRole(UserRole targetRole);

    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationDTO> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Create and send a workflow notification to users with the specified role.
     *
     * @param targetRole the role to notify.
     * @param title the notification title.
     * @param message the notification message.
     * @param relatedEntityId the ID of the related entity (e.g., budget ID).
     */
    void sendWorkflowNotification(UserRole targetRole, String title, String message, Long relatedEntityId);
}
