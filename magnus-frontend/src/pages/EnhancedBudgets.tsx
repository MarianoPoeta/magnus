import React, { useEffect, useState } from 'react';
import { Plus } from 'lucide-react';
import { Button } from '../components/ui/button';
import { Dialog, DialogContent } from '../components/ui/dialog';
import { Alert, AlertDescription } from '../components/ui/alert';
import ResponsiveBudgetsTable from '../components/budget/ResponsiveBudgetsTable';
import UnifiedBudgetCreator from '../components/budget/UnifiedBudgetCreator';
import { LoadingSpinner } from '../components/LoadingSpinner';
import { useLoadingState } from '../hooks/useLoadingState';
import { useStore } from '../store';
import { useApi } from '../hooks/useApi';

const EnhancedBudgets = () => {
  const { budgets } = useStore();
  const { isLoading, error, withLoading, clearError } = useLoadingState();
  const { loadBudgets, createBudget, updateBudget, deleteBudget } = useApi();
  const [showCreator, setShowCreator] = useState(false);
  const [editingBudget, setEditingBudget] = useState<any>(null);

  useEffect(() => {
    // Ensure budgets are loaded from backend
    loadBudgets().catch(() => {/* handled in hook */});
  }, [loadBudgets]);

  // Transform existing budgets to include new fields
  const enhancedBudgets = budgets.map(budget => ({
    ...budget,
    isClosed: false,
    paymentStatus: (budget.status === 'completed' ? 'paid' : 'unpaid') as 'paid' | 'unpaid',
    createdAt: budget.createdAt || new Date().toISOString(),
    // Map status to expected values
    status: budget.status === 'reserva' ? 'approved' : budget.status
  }));

  const handleView = (id: string) => {
    window.location.href = `/budgets/${id}`;
  };

  const handleEdit = (id: string) => {
    const budget = enhancedBudgets.find(b => b.id === id);
    if (budget) {
      setEditingBudget(budget);
      setShowCreator(true);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm('¿Estás seguro de que quieres eliminar este presupuesto?')) return;

    await withLoading(async () => {
      await deleteBudget(id);
    }, 'Error al eliminar el presupuesto');
  };

  const handleSave = async (budget: any) => {
    const success = await withLoading(async () => {
      if (editingBudget) {
        await updateBudget(editingBudget.id, budget);
      } else {
        await createBudget({
          ...budget,
          name: budget.name || `${budget.clientName}'s Budget`,
          status: budget.status || 'draft',
        });
      }
      return true;
    }, 'Error al guardar el presupuesto');

    if (success) {
      setShowCreator(false);
      setEditingBudget(null);
    }
  };

  const handleCancel = () => {
    setShowCreator(false);
    setEditingBudget(null);
    clearError();
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <LoadingSpinner size="lg" text="Cargando presupuestos..." />
      </div>
    );
  }

  return (
    <>
      {/* Content with proper spacing */}
      <div className="space-y-6">
        {/* Error Display */}
        {error && (
          <Alert variant="destructive">
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        )}

        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div className="min-w-0 flex-1">
            <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 mb-2">Gestión de Presupuestos</h1>
            <p className="text-slate-600 text-sm sm:text-base">Crear, rastrear y gestionar presupuestos de despedidas de soltero eficientemente</p>
          </div>
          <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3">
            <Button
              onClick={() => setShowCreator(true)}
              disabled={isLoading}
              className="bg-blue-600 hover:bg-blue-700 text-white px-4 sm:px-6 py-2 rounded-lg font-medium flex items-center justify-center gap-2 transition-colors"
            >
              <Plus className="h-4 w-4" />
              <span className="sm:inline">Nuevo Presupuesto</span>
            </Button>
          </div>
        </div>

        {/* Responsive Budgets Table */}
        <ResponsiveBudgetsTable
          budgets={enhancedBudgets as any}
          onView={handleView}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />

        {/* Unified Budget Creator Dialog */}
        <Dialog open={showCreator} onOpenChange={setShowCreator}>
          <DialogContent className="max-w-[95vw] w-full max-h-[95vh] p-0 flex flex-col">
            <div className="flex-1 overflow-y-auto">
              <UnifiedBudgetCreator
                initialBudget={editingBudget}
                onSave={handleSave}
                onCancel={handleCancel}
              />
            </div>
          </DialogContent>
        </Dialog>
      </div>
    </>
  );
};

export default EnhancedBudgets;
