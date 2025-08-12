package com.magnus.service.impl;

import com.magnus.domain.Budget;
import com.magnus.domain.enumeration.BudgetStatus;
import com.magnus.repository.BudgetRepository;
import com.magnus.service.BudgetService;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.event.BudgetStatusChangeEvent;
import com.magnus.service.mapper.BudgetMapper;
import com.magnus.security.SecurityUtils;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.Budget}.
 */
@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetServiceImpl.class);

    private final BudgetRepository budgetRepository;

    private final BudgetMapper budgetMapper;

    private final ApplicationEventPublisher eventPublisher;

    public BudgetServiceImpl(BudgetRepository budgetRepository, BudgetMapper budgetMapper, ApplicationEventPublisher eventPublisher) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public BudgetDTO save(BudgetDTO budgetDTO) {
        LOG.debug("Request to save Budget : {}", budgetDTO);
        Budget budget = budgetMapper.toEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        return budgetMapper.toDto(budget);
    }

    @Override
    public BudgetDTO update(BudgetDTO budgetDTO) {
        LOG.debug("Request to update Budget : {}", budgetDTO);
        
        // Get the existing budget to check for status changes
        Optional<Budget> existingBudgetOpt = budgetRepository.findById(budgetDTO.getId());
        BudgetStatus oldStatus = existingBudgetOpt.map(Budget::getStatus).orElse(null);
        
        Budget budget = budgetMapper.toEntity(budgetDTO);
        
        // Set workflow triggered flag and timestamp if status changes to RESERVA
        if (budget.getStatus() == BudgetStatus.RESERVA && oldStatus != BudgetStatus.RESERVA) {
            budget.setWorkflowTriggered(true);
            budget.setLastWorkflowExecution(Instant.now());
            budget.setReservedAt(Instant.now());
        }
        
        budget = budgetRepository.save(budget);
        BudgetDTO savedBudgetDTO = budgetMapper.toDto(budget);
        
        // Publish status change event if status has changed
        if (oldStatus != null && !oldStatus.equals(budget.getStatus())) {
            publishStatusChangeEvent(savedBudgetDTO, oldStatus, budget.getStatus());
        }
        
        return savedBudgetDTO;
    }

    @Override
    public Optional<BudgetDTO> partialUpdate(BudgetDTO budgetDTO) {
        LOG.debug("Request to partially update Budget : {}", budgetDTO);

        // Track the old status for event publishing
        final BudgetStatus[] oldStatus = new BudgetStatus[1];

        return budgetRepository
            .findById(budgetDTO.getId())
            .map(existingBudget -> {
                oldStatus[0] = existingBudget.getStatus();
                budgetMapper.partialUpdate(existingBudget, budgetDTO);

                // Set workflow triggered flag and timestamp if status changes to RESERVA
                if (existingBudget.getStatus() == BudgetStatus.RESERVA && oldStatus[0] != BudgetStatus.RESERVA) {
                    existingBudget.setWorkflowTriggered(true);
                    existingBudget.setLastWorkflowExecution(Instant.now());
                    existingBudget.setReservedAt(Instant.now());
                }

                return existingBudget;
            })
            .map(budgetRepository::save)
            .map(savedBudget -> {
                BudgetDTO savedBudgetDTO = budgetMapper.toDto(savedBudget);
                
                // Publish status change event if status has changed
                if (oldStatus[0] != null && !oldStatus[0].equals(savedBudget.getStatus())) {
                    publishStatusChangeEvent(savedBudgetDTO, oldStatus[0], savedBudget.getStatus());
                }
                
                return savedBudgetDTO;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BudgetDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Budgets");
        return budgetRepository.findAll(pageable).map(budgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BudgetDTO> findOne(Long id) {
        LOG.debug("Request to get Budget : {}", id);
        return budgetRepository.findById(id).map(budgetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Budget : {}", id);
        budgetRepository.deleteById(id);
    }

    /**
     * Publishes a budget status change event for workflow automation.
     */
    private void publishStatusChangeEvent(BudgetDTO budget, BudgetStatus oldStatus, BudgetStatus newStatus) {
        String currentUser = SecurityUtils.getCurrentUserLogin().orElse("system");
        
        LOG.info("Publishing budget status change event: Budget {} changed from {} to {} by {}", 
                budget.getId(), oldStatus, newStatus, currentUser);
        
        BudgetStatusChangeEvent event = new BudgetStatusChangeEvent(
            this, 
            budget, 
            oldStatus, 
            newStatus, 
            currentUser
        );
        
        eventPublisher.publishEvent(event);
    }

    /**
     * Updates budget status and triggers workflow if appropriate.
     * This method can be called directly for status changes that need workflow automation.
     */
    public BudgetDTO updateBudgetStatus(Long budgetId, BudgetStatus newStatus) {
        LOG.debug("Request to update budget status: {} to {}", budgetId, newStatus);
        
        BudgetDTO budget = findOne(budgetId)
            .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));
        budget.setStatus(newStatus);
        
        return update(budget);
    }
}
