package com.magnus.service.impl;

import com.magnus.domain.Transport;
import com.magnus.repository.TransportRepository;
import com.magnus.service.TransportService;
import com.magnus.service.dto.TransportDTO;
import com.magnus.service.mapper.TransportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.Transport}.
 */
@Service
@Transactional
public class TransportServiceImpl implements TransportService {

    private static final Logger LOG = LoggerFactory.getLogger(TransportServiceImpl.class);

    private final TransportRepository transportRepository;

    private final TransportMapper transportMapper;

    public TransportServiceImpl(TransportRepository transportRepository, TransportMapper transportMapper) {
        this.transportRepository = transportRepository;
        this.transportMapper = transportMapper;
    }

    @Override
    public TransportDTO save(TransportDTO transportDTO) {
        LOG.debug("Request to save Transport : {}", transportDTO);
        Transport transport = transportMapper.toEntity(transportDTO);
        transport = transportRepository.save(transport);
        return transportMapper.toDto(transport);
    }

    @Override
    public TransportDTO update(TransportDTO transportDTO) {
        LOG.debug("Request to update Transport : {}", transportDTO);
        Transport transport = transportMapper.toEntity(transportDTO);
        transport = transportRepository.save(transport);
        return transportMapper.toDto(transport);
    }

    @Override
    public Optional<TransportDTO> partialUpdate(TransportDTO transportDTO) {
        LOG.debug("Request to partially update Transport : {}", transportDTO);

        return transportRepository
            .findById(transportDTO.getId())
            .map(existingTransport -> {
                transportMapper.partialUpdate(existingTransport, transportDTO);

                return existingTransport;
            })
            .map(transportRepository::save)
            .map(transportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Transports");
        return transportRepository.findAll(pageable).map(transportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransportDTO> findOne(Long id) {
        LOG.debug("Request to get Transport : {}", id);
        return transportRepository.findById(id).map(transportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Transport : {}", id);
        transportRepository.deleteById(id);
    }
}
