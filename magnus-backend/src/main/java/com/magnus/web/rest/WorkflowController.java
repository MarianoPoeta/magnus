package com.magnus.web.rest;

import com.magnus.domain.enumeration.BudgetStatus;
import com.magnus.service.WorkflowAutomationService;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.impl.BudgetServiceImpl;
import com.magnus.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import jakarta.validation.Valid;
import java.net.URISyntaxException;

/**
 * REST controller for workflow automation operations.
 * Provides endpoints for triggering workflows, managing budget status changes, and workflow-related operations.
 */
@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowController.class);

    private static final String ENTITY_NAME = "workflow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowAutomationService workflowAutomationService;
    private final BudgetServiceImpl budgetService;

    public WorkflowController(
        WorkflowAutomationService workflowAutomationService,
        BudgetServiceImpl budgetService
    ) {
        this.workflowAutomationService = workflowAutomationService;
        this.budgetService = budgetService;
    }

    /**
     * {@code POST /api/workflow/trigger-tasks/{budgetId}} : Manually trigger task generation for a budget.
     *
     * @param budgetId the id of the budget to trigger workflow for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if successful.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trigger-tasks/{budgetId}")
    public ResponseEntity<Void> triggerTaskGeneration(@PathVariable Long budgetId) throws URISyntaxException {
        LOG.debug("REST request to trigger task generation for budget : {}", budgetId);

        BudgetDTO budget = budgetService
            .findOne(budgetId)
            .orElseThrow(() -> new BadRequestAlertException("Budget not found", ENTITY_NAME, "budgetnotfound"));
        if (budget.getStatus() != BudgetStatus.RESERVA) {
            throw new BadRequestAlertException("Budget must be in RESERVA status to trigger workflow", ENTITY_NAME, "invalidstatus");
        }

        try {
            workflowAutomationService.triggerWorkflowForBudget(budget);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, "Workflow triggered successfully for budget " + budgetId, budgetId.toString()))
                .build();
        } catch (Exception e) {
            LOG.error("Error triggering workflow for budget: {}", budgetId, e);
            throw new BadRequestAlertException("Failed to trigger workflow: " + e.getMessage(), ENTITY_NAME, "workflowerror");
        }
    }

    /**
     * {@code PATCH /api/workflow/budget-status/{budgetId}} : Update budget status and trigger workflow if appropriate.
     *
     * @param budgetId the id of the budget to update.
     * @param statusRequest the new status to set.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetDTO.
     */
    @PatchMapping("/budget-status/{budgetId}")
    public ResponseEntity<BudgetDTO> updateBudgetStatus(
        @PathVariable Long budgetId,
        @Valid @RequestBody BudgetStatusUpdateRequest statusRequest
    ) {
        LOG.debug("REST request to update budget status: {} to {}", budgetId, statusRequest.getStatus());

        if (budgetId == null) {
            throw new BadRequestAlertException("Invalid budget id", ENTITY_NAME, "idnull");
        }

        try {
            BudgetDTO updatedBudget = budgetService.updateBudgetStatus(budgetId, statusRequest.getStatus());
            
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "budget", budgetId.toString()))
                .body(updatedBudget);
        } catch (Exception e) {
            LOG.error("Error updating budget status: {}", budgetId, e);
            throw new BadRequestAlertException("Failed to update budget status: " + e.getMessage(), ENTITY_NAME, "statusupdateerror");
        }
    }

    /**
     * {@code POST /api/workflow/approve-budget/{budgetId}} : Approve a budget (change status to RESERVA and trigger workflow).
     *
     * @param budgetId the id of the budget to approve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetDTO.
     */
    @PostMapping("/approve-budget/{budgetId}")
    public ResponseEntity<BudgetDTO> approveBudget(@PathVariable Long budgetId) {
        LOG.debug("REST request to approve budget : {}", budgetId);

        if (budgetId == null) {
            throw new BadRequestAlertException("Invalid budget id", ENTITY_NAME, "idnull");
        }

        BudgetDTO budget = budgetService
            .findOne(budgetId)
            .orElseThrow(() -> new BadRequestAlertException("Budget not found", ENTITY_NAME, "budgetnotfound"));
        if (budget.getStatus() == BudgetStatus.RESERVA) {
            throw new BadRequestAlertException("Budget is already approved", ENTITY_NAME, "alreadyapproved");
        }

        try {
            BudgetDTO approvedBudget = budgetService.updateBudgetStatus(budgetId, BudgetStatus.RESERVA);
            
            return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(applicationName, "Budget approved and workflow triggered", budgetId.toString()))
                .body(approvedBudget);
        } catch (Exception e) {
            LOG.error("Error approving budget: {}", budgetId, e);
            throw new BadRequestAlertException("Failed to approve budget: " + e.getMessage(), ENTITY_NAME, "approvalerror");
        }
    }

    /**
     * {@code GET /api/workflow/budget-status/{budgetId}} : Get workflow status for a budget.
     *
     * @param budgetId the id of the budget to check.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflow status.
     */
    @GetMapping("/budget-status/{budgetId}")
    public ResponseEntity<WorkflowStatusResponse> getBudgetWorkflowStatus(@PathVariable Long budgetId) {
        LOG.debug("REST request to get workflow status for budget : {}", budgetId);

        BudgetDTO budget = budgetService
            .findOne(budgetId)
            .orElseThrow(() -> new BadRequestAlertException("Budget not found", ENTITY_NAME, "budgetnotfound"));
        WorkflowStatusResponse status = new WorkflowStatusResponse(
            budget.getId(),
            budget.getStatus(),
            budget.getWorkflowTriggered(),
            budget.getLastWorkflowExecution()
        );

        return ResponseEntity.ok().body(status);
    }

    /**
     * Request object for budget status updates.
     */
    public static class BudgetStatusUpdateRequest {
        private BudgetStatus status;
        private String notes;

        public BudgetStatus getStatus() {
            return status;
        }

        public void setStatus(BudgetStatus status) {
            this.status = status;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    /**
     * Response object for workflow status queries.
     */
    public static class WorkflowStatusResponse {
        private Long budgetId;
        private BudgetStatus status;
        private Boolean workflowTriggered;
        private java.time.Instant lastWorkflowExecution;

        public WorkflowStatusResponse(Long budgetId, BudgetStatus status, Boolean workflowTriggered, java.time.Instant lastWorkflowExecution) {
            this.budgetId = budgetId;
            this.status = status;
            this.workflowTriggered = workflowTriggered;
            this.lastWorkflowExecution = lastWorkflowExecution;
        }

        public Long getBudgetId() {
            return budgetId;
        }

        public void setBudgetId(Long budgetId) {
            this.budgetId = budgetId;
        }

        public BudgetStatus getStatus() {
            return status;
        }

        public void setStatus(BudgetStatus status) {
            this.status = status;
        }

        public Boolean getWorkflowTriggered() {
            return workflowTriggered;
        }

        public void setWorkflowTriggered(Boolean workflowTriggered) {
            this.workflowTriggered = workflowTriggered;
        }

        public java.time.Instant getLastWorkflowExecution() {
            return lastWorkflowExecution;
        }

        public void setLastWorkflowExecution(java.time.Instant lastWorkflowExecution) {
            this.lastWorkflowExecution = lastWorkflowExecution;
        }
    }
} 