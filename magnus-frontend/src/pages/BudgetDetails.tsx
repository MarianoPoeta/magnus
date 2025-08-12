
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';
// Layout is provided by the router; do not wrap here
import { Button } from '../components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { LoadingSpinner } from '../components/LoadingSpinner';
import { apiServices } from '../services/api';

const BudgetDetails: React.FC = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [budget, setBudget] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchBudget = async () => {
      if (!id) return;
      setIsLoading(true);
      setError(null);
      try {
        const data = await apiServices.budgets.getBudgetById(Number(id));
        setBudget(data);
      } catch (e: any) {
        setError(e?.message || 'Error loading budget');
      } finally {
        setIsLoading(false);
      }
    };
    fetchBudget();
  }, [id]);

  return (
      <div className="space-y-6">
        <div className="flex items-center space-x-4">
          <Button 
            variant="ghost" 
            onClick={() => navigate('/budgets')}
            className="p-2"
          >
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div>
            <h1 className="text-3xl font-bold text-slate-900">Budget Details</h1>
            <p className="text-slate-600">Budget ID: {id}</p>
          </div>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Budget Information</CardTitle>
          </CardHeader>
          <CardContent>
            {isLoading && <LoadingSpinner text="Cargando presupuesto..." />}
            {error && <p className="text-red-600">{error}</p>}
            {!isLoading && !error && budget && (
              <div className="space-y-3">
                <div>
                  <span className="text-slate-500">Nombre:</span>
                  <div className="font-medium text-slate-900">{budget.name || budget.title || `Presupuesto #${id}`}</div>
                </div>
                <div>
                  <span className="text-slate-500">Cliente:</span>
                  <div className="font-medium text-slate-900">{budget.client?.name || budget.clientName || '—'}</div>
                </div>
                <div>
                  <span className="text-slate-500">Total:</span>
                  <div className="font-medium text-slate-900">{typeof budget.totalAmount === 'number' ? `$${budget.totalAmount}` : '—'}</div>
                </div>
                <div>
                  <span className="text-slate-500">Estado:</span>
                  <div className="font-medium text-slate-900">{budget.status || '—'}</div>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
  );
};

export default BudgetDetails;
