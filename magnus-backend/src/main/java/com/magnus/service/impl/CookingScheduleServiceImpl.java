package com.magnus.service.impl;

import com.magnus.domain.CookingSchedule;
import com.magnus.repository.CookingScheduleRepository;
import com.magnus.service.CookingScheduleService;
import com.magnus.service.dto.CookingScheduleDTO;
import com.magnus.service.mapper.CookingScheduleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.CookingSchedule}.
 */
@Service
@Transactional
public class CookingScheduleServiceImpl implements CookingScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(CookingScheduleServiceImpl.class);

    private final CookingScheduleRepository cookingScheduleRepository;

    private final CookingScheduleMapper cookingScheduleMapper;

    public CookingScheduleServiceImpl(CookingScheduleRepository cookingScheduleRepository, CookingScheduleMapper cookingScheduleMapper) {
        this.cookingScheduleRepository = cookingScheduleRepository;
        this.cookingScheduleMapper = cookingScheduleMapper;
    }

    @Override
    public CookingScheduleDTO save(CookingScheduleDTO cookingScheduleDTO) {
        LOG.debug("Request to save CookingSchedule : {}", cookingScheduleDTO);
        CookingSchedule cookingSchedule = cookingScheduleMapper.toEntity(cookingScheduleDTO);
        cookingSchedule = cookingScheduleRepository.save(cookingSchedule);
        return cookingScheduleMapper.toDto(cookingSchedule);
    }

    @Override
    public CookingScheduleDTO update(CookingScheduleDTO cookingScheduleDTO) {
        LOG.debug("Request to update CookingSchedule : {}", cookingScheduleDTO);
        CookingSchedule cookingSchedule = cookingScheduleMapper.toEntity(cookingScheduleDTO);
        cookingSchedule = cookingScheduleRepository.save(cookingSchedule);
        return cookingScheduleMapper.toDto(cookingSchedule);
    }

    @Override
    public Optional<CookingScheduleDTO> partialUpdate(CookingScheduleDTO cookingScheduleDTO) {
        LOG.debug("Request to partially update CookingSchedule : {}", cookingScheduleDTO);

        return cookingScheduleRepository
            .findById(cookingScheduleDTO.getId())
            .map(existingCookingSchedule -> {
                cookingScheduleMapper.partialUpdate(existingCookingSchedule, cookingScheduleDTO);

                return existingCookingSchedule;
            })
            .map(cookingScheduleRepository::save)
            .map(cookingScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CookingScheduleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CookingSchedules");
        return cookingScheduleRepository.findAll(pageable).map(cookingScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CookingScheduleDTO> findOne(Long id) {
        LOG.debug("Request to get CookingSchedule : {}", id);
        return cookingScheduleRepository.findById(id).map(cookingScheduleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CookingSchedule : {}", id);
        cookingScheduleRepository.deleteById(id);
    }
}
