package com.magnus.service.impl;

import com.magnus.domain.BudgetTemplate;
import com.magnus.repository.BudgetTemplateRepository;
import com.magnus.service.BudgetTemplateService;
import com.magnus.service.dto.BudgetTemplateDTO;
import com.magnus.service.mapper.BudgetTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.BudgetTemplate}.
 */
@Service
@Transactional
public class BudgetTemplateServiceImpl implements BudgetTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetTemplateServiceImpl.class);

    private final BudgetTemplateRepository budgetTemplateRepository;

    private final BudgetTemplateMapper budgetTemplateMapper;

    public BudgetTemplateServiceImpl(BudgetTemplateRepository budgetTemplateRepository, BudgetTemplateMapper budgetTemplateMapper) {
        this.budgetTemplateRepository = budgetTemplateRepository;
        this.budgetTemplateMapper = budgetTemplateMapper;
    }

    @Override
    public BudgetTemplateDTO save(BudgetTemplateDTO budgetTemplateDTO) {
        LOG.debug("Request to save BudgetTemplate : {}", budgetTemplateDTO);
        BudgetTemplate budgetTemplate = budgetTemplateMapper.toEntity(budgetTemplateDTO);
        budgetTemplate = budgetTemplateRepository.save(budgetTemplate);
        return budgetTemplateMapper.toDto(budgetTemplate);
    }

    @Override
    public BudgetTemplateDTO update(BudgetTemplateDTO budgetTemplateDTO) {
        LOG.debug("Request to update BudgetTemplate : {}", budgetTemplateDTO);
        BudgetTemplate budgetTemplate = budgetTemplateMapper.toEntity(budgetTemplateDTO);
        budgetTemplate = budgetTemplateRepository.save(budgetTemplate);
        return budgetTemplateMapper.toDto(budgetTemplate);
    }

    @Override
    public Optional<BudgetTemplateDTO> partialUpdate(BudgetTemplateDTO budgetTemplateDTO) {
        LOG.debug("Request to partially update BudgetTemplate : {}", budgetTemplateDTO);

        return budgetTemplateRepository
            .findById(budgetTemplateDTO.getId())
            .map(existingBudgetTemplate -> {
                budgetTemplateMapper.partialUpdate(existingBudgetTemplate, budgetTemplateDTO);

                return existingBudgetTemplate;
            })
            .map(budgetTemplateRepository::save)
            .map(budgetTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BudgetTemplateDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BudgetTemplates");
        return budgetTemplateRepository.findAll(pageable).map(budgetTemplateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BudgetTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get BudgetTemplate : {}", id);
        return budgetTemplateRepository.findById(id).map(budgetTemplateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BudgetTemplate : {}", id);
        budgetTemplateRepository.deleteById(id);
    }
}
