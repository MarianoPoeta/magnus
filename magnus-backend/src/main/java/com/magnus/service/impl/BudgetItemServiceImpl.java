package com.magnus.service.impl;

import com.magnus.domain.BudgetItem;
import com.magnus.repository.BudgetItemRepository;
import com.magnus.service.BudgetItemService;
import com.magnus.service.dto.BudgetItemDTO;
import com.magnus.service.mapper.BudgetItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.BudgetItem}.
 */
@Service
@Transactional
public class BudgetItemServiceImpl implements BudgetItemService {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetItemServiceImpl.class);

    private final BudgetItemRepository budgetItemRepository;

    private final BudgetItemMapper budgetItemMapper;

    public BudgetItemServiceImpl(BudgetItemRepository budgetItemRepository, BudgetItemMapper budgetItemMapper) {
        this.budgetItemRepository = budgetItemRepository;
        this.budgetItemMapper = budgetItemMapper;
    }

    @Override
    public BudgetItemDTO save(BudgetItemDTO budgetItemDTO) {
        LOG.debug("Request to save BudgetItem : {}", budgetItemDTO);
        BudgetItem budgetItem = budgetItemMapper.toEntity(budgetItemDTO);
        budgetItem = budgetItemRepository.save(budgetItem);
        return budgetItemMapper.toDto(budgetItem);
    }

    @Override
    public BudgetItemDTO update(BudgetItemDTO budgetItemDTO) {
        LOG.debug("Request to update BudgetItem : {}", budgetItemDTO);
        BudgetItem budgetItem = budgetItemMapper.toEntity(budgetItemDTO);
        budgetItem = budgetItemRepository.save(budgetItem);
        return budgetItemMapper.toDto(budgetItem);
    }

    @Override
    public Optional<BudgetItemDTO> partialUpdate(BudgetItemDTO budgetItemDTO) {
        LOG.debug("Request to partially update BudgetItem : {}", budgetItemDTO);

        return budgetItemRepository
            .findById(budgetItemDTO.getId())
            .map(existingBudgetItem -> {
                budgetItemMapper.partialUpdate(existingBudgetItem, budgetItemDTO);

                return existingBudgetItem;
            })
            .map(budgetItemRepository::save)
            .map(budgetItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetItemDTO> findAll() {
        LOG.debug("Request to get all BudgetItems");
        return budgetItemRepository.findAll().stream().map(budgetItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BudgetItemDTO> findOne(Long id) {
        LOG.debug("Request to get BudgetItem : {}", id);
        return budgetItemRepository.findById(id).map(budgetItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BudgetItem : {}", id);
        budgetItemRepository.deleteById(id);
    }
}
