package com.magnus.service.impl;

import com.magnus.domain.WeeklyPlan;
import com.magnus.repository.WeeklyPlanRepository;
import com.magnus.service.WeeklyPlanService;
import com.magnus.service.dto.WeeklyPlanDTO;
import com.magnus.service.mapper.WeeklyPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.WeeklyPlan}.
 */
@Service
@Transactional
public class WeeklyPlanServiceImpl implements WeeklyPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(WeeklyPlanServiceImpl.class);

    private final WeeklyPlanRepository weeklyPlanRepository;

    private final WeeklyPlanMapper weeklyPlanMapper;

    public WeeklyPlanServiceImpl(WeeklyPlanRepository weeklyPlanRepository, WeeklyPlanMapper weeklyPlanMapper) {
        this.weeklyPlanRepository = weeklyPlanRepository;
        this.weeklyPlanMapper = weeklyPlanMapper;
    }

    @Override
    public WeeklyPlanDTO save(WeeklyPlanDTO weeklyPlanDTO) {
        LOG.debug("Request to save WeeklyPlan : {}", weeklyPlanDTO);
        WeeklyPlan weeklyPlan = weeklyPlanMapper.toEntity(weeklyPlanDTO);
        weeklyPlan = weeklyPlanRepository.save(weeklyPlan);
        return weeklyPlanMapper.toDto(weeklyPlan);
    }

    @Override
    public WeeklyPlanDTO update(WeeklyPlanDTO weeklyPlanDTO) {
        LOG.debug("Request to update WeeklyPlan : {}", weeklyPlanDTO);
        WeeklyPlan weeklyPlan = weeklyPlanMapper.toEntity(weeklyPlanDTO);
        weeklyPlan = weeklyPlanRepository.save(weeklyPlan);
        return weeklyPlanMapper.toDto(weeklyPlan);
    }

    @Override
    public Optional<WeeklyPlanDTO> partialUpdate(WeeklyPlanDTO weeklyPlanDTO) {
        LOG.debug("Request to partially update WeeklyPlan : {}", weeklyPlanDTO);

        return weeklyPlanRepository
            .findById(weeklyPlanDTO.getId())
            .map(existingWeeklyPlan -> {
                weeklyPlanMapper.partialUpdate(existingWeeklyPlan, weeklyPlanDTO);

                return existingWeeklyPlan;
            })
            .map(weeklyPlanRepository::save)
            .map(weeklyPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WeeklyPlanDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WeeklyPlans");
        return weeklyPlanRepository.findAll(pageable).map(weeklyPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WeeklyPlanDTO> findOne(Long id) {
        LOG.debug("Request to get WeeklyPlan : {}", id);
        return weeklyPlanRepository.findById(id).map(weeklyPlanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WeeklyPlan : {}", id);
        weeklyPlanRepository.deleteById(id);
    }
}
