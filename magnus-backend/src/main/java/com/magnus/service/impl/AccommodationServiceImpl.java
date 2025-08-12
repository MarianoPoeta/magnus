package com.magnus.service.impl;

import com.magnus.domain.Accommodation;
import com.magnus.repository.AccommodationRepository;
import com.magnus.service.AccommodationService;
import com.magnus.service.dto.AccommodationDTO;
import com.magnus.service.mapper.AccommodationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.Accommodation}.
 */
@Service
@Transactional
public class AccommodationServiceImpl implements AccommodationService {

    private static final Logger LOG = LoggerFactory.getLogger(AccommodationServiceImpl.class);

    private final AccommodationRepository accommodationRepository;

    private final AccommodationMapper accommodationMapper;

    public AccommodationServiceImpl(AccommodationRepository accommodationRepository, AccommodationMapper accommodationMapper) {
        this.accommodationRepository = accommodationRepository;
        this.accommodationMapper = accommodationMapper;
    }

    @Override
    public AccommodationDTO save(AccommodationDTO accommodationDTO) {
        LOG.debug("Request to save Accommodation : {}", accommodationDTO);
        Accommodation accommodation = accommodationMapper.toEntity(accommodationDTO);
        accommodation = accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public AccommodationDTO update(AccommodationDTO accommodationDTO) {
        LOG.debug("Request to update Accommodation : {}", accommodationDTO);
        Accommodation accommodation = accommodationMapper.toEntity(accommodationDTO);
        accommodation = accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public Optional<AccommodationDTO> partialUpdate(AccommodationDTO accommodationDTO) {
        LOG.debug("Request to partially update Accommodation : {}", accommodationDTO);

        return accommodationRepository
            .findById(accommodationDTO.getId())
            .map(existingAccommodation -> {
                accommodationMapper.partialUpdate(existingAccommodation, accommodationDTO);

                return existingAccommodation;
            })
            .map(accommodationRepository::save)
            .map(accommodationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccommodationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Accommodations");
        return accommodationRepository.findAll(pageable).map(accommodationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccommodationDTO> findOne(Long id) {
        LOG.debug("Request to get Accommodation : {}", id);
        return accommodationRepository.findById(id).map(accommodationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Accommodation : {}", id);
        accommodationRepository.deleteById(id);
    }
}
