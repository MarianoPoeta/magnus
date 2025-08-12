package com.magnus.service.impl;

import com.magnus.domain.SystemConfig;
import com.magnus.repository.SystemConfigRepository;
import com.magnus.service.SystemConfigService;
import com.magnus.service.dto.SystemConfigDTO;
import com.magnus.service.mapper.SystemConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.SystemConfig}.
 */
@Service
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    public SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
    }

    @Override
    public SystemConfigDTO save(SystemConfigDTO systemConfigDTO) {
        LOG.debug("Request to save SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfig = systemConfigRepository.save(systemConfig);
        return systemConfigMapper.toDto(systemConfig);
    }

    @Override
    public SystemConfigDTO update(SystemConfigDTO systemConfigDTO) {
        LOG.debug("Request to update SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfig = systemConfigRepository.save(systemConfig);
        return systemConfigMapper.toDto(systemConfig);
    }

    @Override
    public Optional<SystemConfigDTO> partialUpdate(SystemConfigDTO systemConfigDTO) {
        LOG.debug("Request to partially update SystemConfig : {}", systemConfigDTO);

        return systemConfigRepository
            .findById(systemConfigDTO.getId())
            .map(existingSystemConfig -> {
                systemConfigMapper.partialUpdate(existingSystemConfig, systemConfigDTO);

                return existingSystemConfig;
            })
            .map(systemConfigRepository::save)
            .map(systemConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemConfigDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SystemConfigs");
        return systemConfigRepository.findAll(pageable).map(systemConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemConfigDTO> findOne(Long id) {
        LOG.debug("Request to get SystemConfig : {}", id);
        return systemConfigRepository.findById(id).map(systemConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SystemConfig : {}", id);
        systemConfigRepository.deleteById(id);
    }
}
