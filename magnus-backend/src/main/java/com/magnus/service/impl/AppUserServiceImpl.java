package com.magnus.service.impl;

import com.magnus.domain.AppUser;
import com.magnus.repository.AppUserRepository;
import com.magnus.service.AppUserService;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.mapper.AppUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    public AppUserServiceImpl(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    public AppUserDTO save(AppUserDTO appUserDTO) {
        LOG.debug("Request to save AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(appUser);
    }

    @Override
    public AppUserDTO update(AppUserDTO appUserDTO) {
        LOG.debug("Request to update AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);
        appUser = appUserRepository.save(appUser);
        return appUserMapper.toDto(appUser);
    }

    @Override
    public Optional<AppUserDTO> partialUpdate(AppUserDTO appUserDTO) {
        LOG.debug("Request to partially update AppUser : {}", appUserDTO);

        return appUserRepository
            .findById(appUserDTO.getId())
            .map(existingAppUser -> {
                appUserMapper.partialUpdate(existingAppUser, appUserDTO);

                return existingAppUser;
            })
            .map(appUserRepository::save)
            .map(appUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppUserDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppUsers");
        return appUserRepository.findAll(pageable).map(appUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOne(Long id) {
        LOG.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id).map(appUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
