package com.magnus.service.impl;

import com.magnus.domain.Need;
import com.magnus.repository.NeedRepository;
import com.magnus.service.NeedService;
import com.magnus.service.dto.NeedDTO;
import com.magnus.service.mapper.NeedMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.Need}.
 */
@Service
@Transactional
public class NeedServiceImpl implements NeedService {

    private static final Logger LOG = LoggerFactory.getLogger(NeedServiceImpl.class);

    private final NeedRepository needRepository;

    private final NeedMapper needMapper;

    public NeedServiceImpl(NeedRepository needRepository, NeedMapper needMapper) {
        this.needRepository = needRepository;
        this.needMapper = needMapper;
    }

    @Override
    public NeedDTO save(NeedDTO needDTO) {
        LOG.debug("Request to save Need : {}", needDTO);
        Need need = needMapper.toEntity(needDTO);
        need = needRepository.save(need);
        return needMapper.toDto(need);
    }

    @Override
    public NeedDTO update(NeedDTO needDTO) {
        LOG.debug("Request to update Need : {}", needDTO);
        Need need = needMapper.toEntity(needDTO);
        need = needRepository.save(need);
        return needMapper.toDto(need);
    }

    @Override
    public Optional<NeedDTO> partialUpdate(NeedDTO needDTO) {
        LOG.debug("Request to partially update Need : {}", needDTO);

        return needRepository
            .findById(needDTO.getId())
            .map(existingNeed -> {
                needMapper.partialUpdate(existingNeed, needDTO);

                return existingNeed;
            })
            .map(needRepository::save)
            .map(needMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NeedDTO> findAll() {
        LOG.debug("Request to get all Needs");
        return needRepository.findAll().stream().map(needMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NeedDTO> findOne(Long id) {
        LOG.debug("Request to get Need : {}", id);
        return needRepository.findById(id).map(needMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Need : {}", id);
        needRepository.deleteById(id);
    }
}
