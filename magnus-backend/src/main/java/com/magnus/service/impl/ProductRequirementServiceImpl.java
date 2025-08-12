package com.magnus.service.impl;

import com.magnus.domain.ProductRequirement;
import com.magnus.repository.ProductRequirementRepository;
import com.magnus.service.ProductRequirementService;
import com.magnus.service.dto.ProductRequirementDTO;
import com.magnus.service.mapper.ProductRequirementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.ProductRequirement}.
 */
@Service
@Transactional
public class ProductRequirementServiceImpl implements ProductRequirementService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRequirementServiceImpl.class);

    private final ProductRequirementRepository productRequirementRepository;

    private final ProductRequirementMapper productRequirementMapper;

    public ProductRequirementServiceImpl(
        ProductRequirementRepository productRequirementRepository,
        ProductRequirementMapper productRequirementMapper
    ) {
        this.productRequirementRepository = productRequirementRepository;
        this.productRequirementMapper = productRequirementMapper;
    }

    @Override
    public ProductRequirementDTO save(ProductRequirementDTO productRequirementDTO) {
        LOG.debug("Request to save ProductRequirement : {}", productRequirementDTO);
        ProductRequirement productRequirement = productRequirementMapper.toEntity(productRequirementDTO);
        productRequirement = productRequirementRepository.save(productRequirement);
        return productRequirementMapper.toDto(productRequirement);
    }

    @Override
    public ProductRequirementDTO update(ProductRequirementDTO productRequirementDTO) {
        LOG.debug("Request to update ProductRequirement : {}", productRequirementDTO);
        ProductRequirement productRequirement = productRequirementMapper.toEntity(productRequirementDTO);
        productRequirement = productRequirementRepository.save(productRequirement);
        return productRequirementMapper.toDto(productRequirement);
    }

    @Override
    public Optional<ProductRequirementDTO> partialUpdate(ProductRequirementDTO productRequirementDTO) {
        LOG.debug("Request to partially update ProductRequirement : {}", productRequirementDTO);

        return productRequirementRepository
            .findById(productRequirementDTO.getId())
            .map(existingProductRequirement -> {
                productRequirementMapper.partialUpdate(existingProductRequirement, productRequirementDTO);

                return existingProductRequirement;
            })
            .map(productRequirementRepository::save)
            .map(productRequirementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductRequirementDTO> findAll() {
        LOG.debug("Request to get all ProductRequirements");
        return productRequirementRepository
            .findAll()
            .stream()
            .map(productRequirementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductRequirementDTO> findOne(Long id) {
        LOG.debug("Request to get ProductRequirement : {}", id);
        return productRequirementRepository.findById(id).map(productRequirementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductRequirement : {}", id);
        productRequirementRepository.deleteById(id);
    }
}
