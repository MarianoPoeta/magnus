package com.magnus.service;

import com.magnus.service.event.BudgetStatusChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event listener that handles budget status changes and triggers workflow automation.
 * Listens for BudgetStatusChangeEvent and triggers workflow when status changes to RESERVA.
 */
@Component
public class BudgetStatusChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(BudgetStatusChangeListener.class);

    private final WorkflowAutomationService workflowAutomationService;

    public BudgetStatusChangeListener(WorkflowAutomationService workflowAutomationService) {
        this.workflowAutomationService = workflowAutomationService;
    }

    /**
     * Handles budget status change events.
     * Triggers workflow automation when budget status changes to RESERVA.
     */
    @EventListener
    @Async
    @Transactional
    public void handleBudgetStatusChange(BudgetStatusChangeEvent event) {
        LOG.info("Received budget status change event: {}", event);

        try {
            // Check if this status change should trigger workflow automation
            if (event.isWorkflowTrigger()) {
                LOG.info("Budget status changed to RESERVA - triggering workflow automation for budget: {}", 
                        event.getBudgetId());
                
                // Trigger the workflow automation
                workflowAutomationService.triggerWorkflowForBudget(event.getBudget());
                
                LOG.info("Workflow automation triggered successfully for budget: {}", event.getBudgetId());
            } else {
                LOG.debug("Budget status change does not trigger workflow: {} -> {}", 
                         event.getOldStatus(), event.getNewStatus());
            }
            
        } catch (Exception e) {
            LOG.error("Error handling budget status change event for budget: {}", event.getBudgetId(), e);
            // In a production system, you might want to:
            // - Send error notifications
            // - Store failed events for retry
            // - Update budget with error status
            throw new RuntimeException("Failed to handle budget status change", e);
        }
    }

    /**
     * Handles other budget-related events that might need processing.
     * This method can be extended to handle additional workflow scenarios.
     */
    @EventListener
    @Async
    public void handleBudgetWorkflowEvents(BudgetStatusChangeEvent event) {
        LOG.debug("Processing additional workflow logic for budget status change: {}", event);
        
        // Additional workflow logic can be added here, such as:
        // - Sending client notifications
        // - Updating external systems
        // - Triggering reports
        // - Managing resource allocations
    }
} 