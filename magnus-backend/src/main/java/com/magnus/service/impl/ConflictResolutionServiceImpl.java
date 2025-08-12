package com.magnus.service.impl;

import com.magnus.domain.ConflictResolution;
import com.magnus.repository.ConflictResolutionRepository;
import com.magnus.service.ConflictResolutionService;
import com.magnus.service.dto.ConflictResolutionDTO;
import com.magnus.service.mapper.ConflictResolutionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.ConflictResolution}.
 */
@Service
@Transactional
public class ConflictResolutionServiceImpl implements ConflictResolutionService {

    private static final Logger LOG = LoggerFactory.getLogger(ConflictResolutionServiceImpl.class);

    private final ConflictResolutionRepository conflictResolutionRepository;

    private final ConflictResolutionMapper conflictResolutionMapper;

    public ConflictResolutionServiceImpl(
        ConflictResolutionRepository conflictResolutionRepository,
        ConflictResolutionMapper conflictResolutionMapper
    ) {
        this.conflictResolutionRepository = conflictResolutionRepository;
        this.conflictResolutionMapper = conflictResolutionMapper;
    }

    @Override
    public ConflictResolutionDTO save(ConflictResolutionDTO conflictResolutionDTO) {
        LOG.debug("Request to save ConflictResolution : {}", conflictResolutionDTO);
        ConflictResolution conflictResolution = conflictResolutionMapper.toEntity(conflictResolutionDTO);
        conflictResolution = conflictResolutionRepository.save(conflictResolution);
        return conflictResolutionMapper.toDto(conflictResolution);
    }

    @Override
    public ConflictResolutionDTO update(ConflictResolutionDTO conflictResolutionDTO) {
        LOG.debug("Request to update ConflictResolution : {}", conflictResolutionDTO);
        ConflictResolution conflictResolution = conflictResolutionMapper.toEntity(conflictResolutionDTO);
        conflictResolution = conflictResolutionRepository.save(conflictResolution);
        return conflictResolutionMapper.toDto(conflictResolution);
    }

    @Override
    public Optional<ConflictResolutionDTO> partialUpdate(ConflictResolutionDTO conflictResolutionDTO) {
        LOG.debug("Request to partially update ConflictResolution : {}", conflictResolutionDTO);

        return conflictResolutionRepository
            .findById(conflictResolutionDTO.getId())
            .map(existingConflictResolution -> {
                conflictResolutionMapper.partialUpdate(existingConflictResolution, conflictResolutionDTO);

                return existingConflictResolution;
            })
            .map(conflictResolutionRepository::save)
            .map(conflictResolutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConflictResolutionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ConflictResolutions");
        return conflictResolutionRepository.findAll(pageable).map(conflictResolutionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConflictResolutionDTO> findOne(Long id) {
        LOG.debug("Request to get ConflictResolution : {}", id);
        return conflictResolutionRepository.findById(id).map(conflictResolutionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ConflictResolution : {}", id);
        conflictResolutionRepository.deleteById(id);
    }
}
