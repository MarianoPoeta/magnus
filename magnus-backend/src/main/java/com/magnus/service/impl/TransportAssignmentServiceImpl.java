package com.magnus.service.impl;

import com.magnus.domain.TransportAssignment;
import com.magnus.repository.TransportAssignmentRepository;
import com.magnus.service.TransportAssignmentService;
import com.magnus.service.dto.TransportAssignmentDTO;
import com.magnus.service.mapper.TransportAssignmentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.TransportAssignment}.
 */
@Service
@Transactional
public class TransportAssignmentServiceImpl implements TransportAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(TransportAssignmentServiceImpl.class);

    private final TransportAssignmentRepository transportAssignmentRepository;

    private final TransportAssignmentMapper transportAssignmentMapper;

    public TransportAssignmentServiceImpl(
        TransportAssignmentRepository transportAssignmentRepository,
        TransportAssignmentMapper transportAssignmentMapper
    ) {
        this.transportAssignmentRepository = transportAssignmentRepository;
        this.transportAssignmentMapper = transportAssignmentMapper;
    }

    @Override
    public TransportAssignmentDTO save(TransportAssignmentDTO transportAssignmentDTO) {
        LOG.debug("Request to save TransportAssignment : {}", transportAssignmentDTO);
        TransportAssignment transportAssignment = transportAssignmentMapper.toEntity(transportAssignmentDTO);
        transportAssignment = transportAssignmentRepository.save(transportAssignment);
        return transportAssignmentMapper.toDto(transportAssignment);
    }

    @Override
    public TransportAssignmentDTO update(TransportAssignmentDTO transportAssignmentDTO) {
        LOG.debug("Request to update TransportAssignment : {}", transportAssignmentDTO);
        TransportAssignment transportAssignment = transportAssignmentMapper.toEntity(transportAssignmentDTO);
        transportAssignment = transportAssignmentRepository.save(transportAssignment);
        return transportAssignmentMapper.toDto(transportAssignment);
    }

    @Override
    public Optional<TransportAssignmentDTO> partialUpdate(TransportAssignmentDTO transportAssignmentDTO) {
        LOG.debug("Request to partially update TransportAssignment : {}", transportAssignmentDTO);

        return transportAssignmentRepository
            .findById(transportAssignmentDTO.getId())
            .map(existingTransportAssignment -> {
                transportAssignmentMapper.partialUpdate(existingTransportAssignment, transportAssignmentDTO);

                return existingTransportAssignment;
            })
            .map(transportAssignmentRepository::save)
            .map(transportAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportAssignmentDTO> findAll() {
        LOG.debug("Request to get all TransportAssignments");
        return transportAssignmentRepository
            .findAll()
            .stream()
            .map(transportAssignmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransportAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get TransportAssignment : {}", id);
        return transportAssignmentRepository.findById(id).map(transportAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TransportAssignment : {}", id);
        transportAssignmentRepository.deleteById(id);
    }
}
