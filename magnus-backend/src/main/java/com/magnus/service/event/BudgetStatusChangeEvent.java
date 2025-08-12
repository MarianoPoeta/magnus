package com.magnus.service.event;

import com.magnus.domain.enumeration.BudgetStatus;
import com.magnus.service.dto.BudgetDTO;
import org.springframework.context.ApplicationEvent;

/**
 * Event published when a budget's status changes.
 * Used to trigger workflow automation when status changes to RESERVA.
 */
public class BudgetStatusChangeEvent extends ApplicationEvent {

    private final BudgetDTO budget;
    private final BudgetStatus oldStatus;
    private final BudgetStatus newStatus;
    private final String changedBy;

    public BudgetStatusChangeEvent(Object source, BudgetDTO budget, BudgetStatus oldStatus, BudgetStatus newStatus, String changedBy) {
        super(source);
        this.budget = budget;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
    }

    public BudgetDTO getBudget() {
        return budget;
    }

    public BudgetStatus getOldStatus() {
        return oldStatus;
    }

    public BudgetStatus getNewStatus() {
        return newStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public Long getBudgetId() {
        return budget.getId();
    }

    public boolean isWorkflowTrigger() {
        return newStatus == BudgetStatus.RESERVA && oldStatus != BudgetStatus.RESERVA;
    }

    @Override
    public String toString() {
        return "BudgetStatusChangeEvent{" +
                "budgetId=" + getBudgetId() +
                ", budgetName='" + budget.getName() + '\'' +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
} 