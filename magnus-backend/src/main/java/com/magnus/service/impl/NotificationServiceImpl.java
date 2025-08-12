package com.magnus.service.impl;

import com.magnus.domain.Notification;
import com.magnus.domain.enumeration.NotificationType;
import com.magnus.domain.enumeration.TaskPriority;
import com.magnus.domain.enumeration.UserRole;
import com.magnus.repository.NotificationRepository;
import com.magnus.service.NotificationService;
import com.magnus.service.dto.NotificationDTO;
import com.magnus.service.mapper.NotificationMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        LOG.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    public NotificationDTO update(NotificationDTO notificationDTO) {
        LOG.debug("Request to update Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        LOG.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> findByTargetRole(UserRole targetRole) {
        LOG.debug("Request to get Notifications for role: {}", targetRole);
        return notificationRepository.findByTargetRoleOrIsGlobalTrue(targetRole)
            .stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> findUnreadByTargetRole(UserRole targetRole) {
        LOG.debug("Request to get unread Notifications for role: {}", targetRole);
        return notificationRepository.findByTargetRoleAndIsReadFalseOrIsGlobalTrueAndIsReadFalse(targetRole)
            .stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendWorkflowNotification(UserRole targetRole, String title, String message, Long relatedEntityId) {
        LOG.debug("Sending workflow notification to role: {} - {}", targetRole, title);

        NotificationDTO notification = new NotificationDTO();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(NotificationType.INFO);
        notification.setTargetRole(targetRole);
        notification.setRelatedEntityType("Budget");
        notification.setRelatedEntityId(relatedEntityId != null ? relatedEntityId.toString() : null);
        notification.setIsRead(false);
        notification.setIsGlobal(false);
        notification.setActionRequired(false);
        notification.setPriority(TaskPriority.MEDIUM);
        notification.setCreatedAt(Instant.now());

        save(notification);

        LOG.info("Workflow notification sent to role {}: {}", targetRole, title);
    }
}
