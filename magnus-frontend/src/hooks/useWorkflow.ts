import { useState, useCallback } from 'react';
import { workflowService } from '../services/api';

interface WorkflowState {
  isLoading: boolean;
  error: string | null;
  lastAction: string | null;
}

interface WorkflowResult {
  success: boolean;
  data?: any;
  error?: string;
  message?: string;
}

export const useWorkflow = () => {
  const [state, setState] = useState<WorkflowState>({
    isLoading: false,
    error: null,
    lastAction: null,
  });

  // Trigger task generation for a budget
  const triggerTasks = useCallback(async (budgetId: number): Promise<WorkflowResult> => {
    try {
      setState(prev => ({ 
        ...prev, 
        isLoading: true, 
        error: null, 
        lastAction: 'trigger_tasks' 
      }));

      const result = await workflowService.triggerTasks(budgetId);
      
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: null 
      }));

      return {
        success: true,
        data: result,
        message: 'Tasks triggered successfully'
      };
    } catch (error: any) {
      const errorMessage = error.message || 'Failed to trigger tasks';
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: errorMessage 
      }));
      
      return {
        success: false,
        error: errorMessage
      };
    }
  }, []);

  // Update budget status (triggers workflow if changing to RESERVA)
  const updateBudgetStatus = useCallback(async (budgetId: number, status: string): Promise<WorkflowResult> => {
    try {
      setState(prev => ({ 
        ...prev, 
        isLoading: true, 
        error: null, 
        lastAction: 'update_budget_status' 
      }));

      const result = await workflowService.updateBudgetStatus(budgetId, status);
      
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: null 
      }));

      return {
        success: true,
        data: result,
        message: `Budget status updated to ${status}`
      };
    } catch (error: any) {
      const errorMessage = error.message || 'Failed to update budget status';
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: errorMessage 
      }));
      
      return {
        success: false,
        error: errorMessage
      };
    }
  }, []);

  // Approve budget (sets status to RESERVA and triggers workflow)
  const approveBudget = useCallback(async (budgetId: number): Promise<WorkflowResult> => {
    try {
      setState(prev => ({ 
        ...prev, 
        isLoading: true, 
        error: null, 
        lastAction: 'approve_budget' 
      }));

      const result = await workflowService.approveBudget(budgetId);
      
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: null 
      }));

      return {
        success: true,
        data: result,
        message: 'Budget approved successfully! Tasks will be generated automatically.'
      };
    } catch (error: any) {
      const errorMessage = error.message || 'Failed to approve budget';
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: errorMessage 
      }));
      
      return {
        success: false,
        error: errorMessage
      };
    }
  }, []);

  // Get budget status
  const getBudgetStatus = useCallback(async (budgetId: number): Promise<WorkflowResult> => {
    try {
      setState(prev => ({ 
        ...prev, 
        isLoading: true, 
        error: null, 
        lastAction: 'get_budget_status' 
      }));

      const result = await workflowService.getBudgetStatus(budgetId);
      
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: null 
      }));

      return {
        success: true,
        data: result,
        message: 'Budget status retrieved successfully'
      };
    } catch (error: any) {
      const errorMessage = error.message || 'Failed to get budget status';
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: errorMessage 
      }));
      
      return {
        success: false,
        error: errorMessage
      };
    }
  }, []);

  // Clear error
  const clearError = useCallback(() => {
    setState(prev => ({ ...prev, error: null }));
  }, []);

  // Utility function to simulate budget workflow testing
  const testWorkflow = useCallback(async (budgetId: number): Promise<WorkflowResult> => {
    try {
      setState(prev => ({ 
        ...prev, 
        isLoading: true, 
        error: null, 
        lastAction: 'test_workflow' 
      }));

      console.log('🔄 Starting workflow test for budget:', budgetId);
      
      // Step 1: Check current status
      const statusResult = await getBudgetStatus(budgetId);
      console.log('📊 Current budget status:', statusResult.data);
      
      // Step 2: Approve budget (triggers workflow)
      const approveResult = await approveBudget(budgetId);
      console.log('✅ Budget approved:', approveResult.data);
      
      // Step 3: Check status again
      const newStatusResult = await getBudgetStatus(budgetId);
      console.log('📊 New budget status:', newStatusResult.data);
      
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: null 
      }));

      return {
        success: true,
        data: {
          initialStatus: statusResult.data,
          approvalResult: approveResult.data,
          finalStatus: newStatusResult.data
        },
        message: 'Workflow test completed successfully'
      };
    } catch (error: any) {
      const errorMessage = error.message || 'Workflow test failed';
      setState(prev => ({ 
        ...prev, 
        isLoading: false, 
        error: errorMessage 
      }));
      
      return {
        success: false,
        error: errorMessage
      };
    }
  }, [approveBudget, getBudgetStatus]);

  return {
    // State
    isLoading: state.isLoading,
    error: state.error,
    lastAction: state.lastAction,
    
    // Functions
    triggerTasks,
    updateBudgetStatus,
    approveBudget,
    getBudgetStatus,
    testWorkflow,
    clearError,
  };
}; 