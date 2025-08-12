package com.magnus.service.impl;

import com.magnus.domain.AuditLog;
import com.magnus.repository.AuditLogRepository;
import com.magnus.service.AuditLogService;
import com.magnus.service.dto.AuditLogDTO;
import com.magnus.service.mapper.AuditLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.AuditLog}.
 */
@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    private final AuditLogRepository auditLogRepository;

    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public AuditLogDTO save(AuditLogDTO auditLogDTO) {
        LOG.debug("Request to save AuditLog : {}", auditLogDTO);
        AuditLog auditLog = auditLogMapper.toEntity(auditLogDTO);
        auditLog = auditLogRepository.save(auditLog);
        return auditLogMapper.toDto(auditLog);
    }

    @Override
    public AuditLogDTO update(AuditLogDTO auditLogDTO) {
        LOG.debug("Request to update AuditLog : {}", auditLogDTO);
        AuditLog auditLog = auditLogMapper.toEntity(auditLogDTO);
        auditLog = auditLogRepository.save(auditLog);
        return auditLogMapper.toDto(auditLog);
    }

    @Override
    public Optional<AuditLogDTO> partialUpdate(AuditLogDTO auditLogDTO) {
        LOG.debug("Request to partially update AuditLog : {}", auditLogDTO);

        return auditLogRepository
            .findById(auditLogDTO.getId())
            .map(existingAuditLog -> {
                auditLogMapper.partialUpdate(existingAuditLog, auditLogDTO);

                return existingAuditLog;
            })
            .map(auditLogRepository::save)
            .map(auditLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AuditLogs");
        return auditLogRepository.findAll(pageable).map(auditLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuditLogDTO> findOne(Long id) {
        LOG.debug("Request to get AuditLog : {}", id);
        return auditLogRepository.findById(id).map(auditLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AuditLog : {}", id);
        auditLogRepository.deleteById(id);
    }
}
