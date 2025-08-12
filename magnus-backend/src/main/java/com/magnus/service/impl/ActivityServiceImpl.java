package com.magnus.service.impl;

import com.magnus.domain.Activity;
import com.magnus.repository.ActivityRepository;
import com.magnus.service.ActivityService;
import com.magnus.service.dto.ActivityDTO;
import com.magnus.service.mapper.ActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.Activity}.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    @Override
    public ActivityDTO save(ActivityDTO activityDTO) {
        LOG.debug("Request to save Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity = activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    @Override
    public ActivityDTO update(ActivityDTO activityDTO) {
        LOG.debug("Request to update Activity : {}", activityDTO);
        Activity activity = activityMapper.toEntity(activityDTO);
        activity = activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    @Override
    public Optional<ActivityDTO> partialUpdate(ActivityDTO activityDTO) {
        LOG.debug("Request to partially update Activity : {}", activityDTO);

        return activityRepository
            .findById(activityDTO.getId())
            .map(existingActivity -> {
                activityMapper.partialUpdate(existingActivity, activityDTO);

                return existingActivity;
            })
            .map(activityRepository::save)
            .map(activityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Activities");
        return activityRepository.findAll(pageable).map(activityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityDTO> findOne(Long id) {
        LOG.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id).map(activityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Activity : {}", id);
        activityRepository.deleteById(id);
    }
}
