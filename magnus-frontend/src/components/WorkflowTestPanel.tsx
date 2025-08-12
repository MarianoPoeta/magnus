import React, { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { AlertCircle, CheckCircle, XCircle, RefreshCw } from 'lucide-react';
import { apiServices } from '@/services/api';

interface TestResult {
  success: boolean;
  message: string;
  data?: any;
  error?: string;
}

export const WorkflowTestPanel: React.FC = () => {
  const [testResults, setTestResults] = useState<TestResult[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [budgets, setBudgets] = useState<any[]>([]);
  const [tasks, setTasks] = useState<any[]>([]);

  const addTestResult = (result: TestResult) => {
    setTestResults(prev => [...prev, result]);
  };

  // New: Test backend connectivity
  const testBackendConnection = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/budgets', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        addTestResult({
          success: true,
          message: 'Backend connection successful',
          data: { status: response.status, statusText: response.statusText }
        });
      } else {
        addTestResult({
          success: false,
          message: `Backend connection failed: ${response.status} ${response.statusText}`,
          error: 'Connection failed'
        });
      }
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Backend connection failed',
        error: error instanceof Error ? error.message : 'Unknown error'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogin = async () => {
    setIsLoading(true);
    try {
      const response = await apiServices.auth.login({
        username: 'admin',
        password: 'admin'
      });
      
      // Handle JHipster response - it returns { id_token: string }
      setCurrentUser({ token: response.id_token, name: 'admin' });
      addTestResult({
        success: true,
        message: 'Login successful',
        data: response
      });
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Login failed',
        error: error instanceof Error ? error.message : 'Authentication failed'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      await apiServices.auth.logout();
      setCurrentUser(null);
      addTestResult({
        success: true,
        message: 'Logout successful'
      });
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Logout failed',
        error: error instanceof Error ? error.message : 'Logout failed'
      });
    }
  };

  const loadBudgets = async () => {
    setIsLoading(true);
    try {
      const response = await apiServices.budgets.getBudgets();
      // Handle PaginatedResponse structure - use 'data' property
      setBudgets(response.data || []);
      addTestResult({
        success: true,
        message: `Loaded ${response.data?.length || 0} budgets`,
        data: response.data
      });
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Failed to load budgets',
        error: error instanceof Error ? error.message : 'Failed to load budgets'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const loadTasks = async () => {
    setIsLoading(true);
    try {
      const response = await apiServices.tasks.getTasks();
      // Handle PaginatedResponse structure - use 'data' property
      setTasks(response.data || []);
      addTestResult({
        success: true,
        message: `Loaded ${response.data?.length || 0} tasks`,
        data: response.data
      });
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Failed to load tasks',
        error: error instanceof Error ? error.message : 'Failed to load tasks'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const createTestBudget = async () => {
    setIsLoading(true);
    try {
      const testBudget = {
        name: 'Test Budget',
        clientName: 'Test Client',
        eventDate: new Date().toISOString().split('T')[0],
        guestCount: 10,
        totalAmount: 1000,
        status: 'DRAFT',
        paymentStatus: 'UNPAID',
        isClosed: false,
        eventGender: 'MIXED',
        mealsAmount: 400,
        activitiesAmount: 300,
        transportAmount: 100,
        accommodationAmount: 200,
        workflowTriggered: false,
        version: 1,
        conflictStatus: 'NONE'
      };
      
      const response = await apiServices.budgets.createBudget(testBudget);
      addTestResult({
        success: true,
        message: 'Test budget created successfully',
        data: response
      });
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Failed to create test budget',
        error: error instanceof Error ? error.message : 'Budget creation failed'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleApproveBudget = async (budgetId: number) => {
    setIsLoading(true);
    try {
      // Update the budget status to RESERVA
      const updatedBudget = {
        id: budgetId,
        status: 'RESERVA',
        workflowTriggered: true
      };
      
      await apiServices.budgets.updateBudget(updatedBudget);
      addTestResult({
        success: true,
        message: `Budget ${budgetId} approved and status changed to RESERVA`,
        data: { budgetId, status: 'RESERVA' }
      });
      
      // Reload budgets to see the change
      await loadBudgets();
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Failed to approve budget',
        error: error instanceof Error ? error.message : 'Budget approval failed'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleTestCompleteWorkflow = async () => {
    setIsLoading(true);
    try {
      // 1. Create a test budget
      await createTestBudget();
      
      // 2. Load budgets to get the new budget
      await loadBudgets();
      
      // 3. If we have budgets, approve the first one
      if (budgets.length > 0) {
        await handleApproveBudget(budgets[0].id);
      }
      
      // 4. Load tasks to see if they were generated
      await loadTasks();
      
      addTestResult({
        success: true,
        message: 'Complete workflow test finished',
        data: { budgets: budgets.length, tasks: tasks.length }
      });
    } catch (error) {
      addTestResult({
        success: false,
        message: 'Complete workflow test failed',
        error: error instanceof Error ? error.message : 'Workflow test failed'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const clearResults = () => {
    setTestResults([]);
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <RefreshCw className="w-5 h-5" />
            Backend Integration Test Panel
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {/* Connection Tests */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <Button 
                onClick={testBackendConnection}
                disabled={isLoading}
                variant="outline"
                className="w-full"
              >
                Test Backend Connection
              </Button>
              
              <Button 
                onClick={handleLogin}
                disabled={isLoading}
                variant="outline"
                className="w-full"
              >
                Test Login
              </Button>
              
              <Button 
                onClick={handleLogout}
                disabled={isLoading || !currentUser}
                variant="outline"
                className="w-full"
              >
                Test Logout
              </Button>
            </div>

            {/* Data Loading Tests */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Button 
                onClick={loadBudgets}
                disabled={isLoading}
                variant="outline"
                className="w-full"
              >
                Load Budgets
              </Button>
              
              <Button 
                onClick={loadTasks}
                disabled={isLoading}
                variant="outline"
                className="w-full"
              >
                Load Tasks
              </Button>
            </div>

            {/* Workflow Tests */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Button 
                onClick={createTestBudget}
                disabled={isLoading}
                variant="outline"
                className="w-full"
              >
                Create Test Budget
              </Button>
              
              <Button 
                onClick={handleTestCompleteWorkflow}
                disabled={isLoading}
                variant="outline"
                className="w-full"
              >
                Complete Workflow Test
              </Button>
            </div>

            {/* Clear Results */}
            <div className="flex justify-end">
              <Button 
                onClick={clearResults}
                variant="ghost"
                size="sm"
              >
                Clear Results
              </Button>
            </div>

            {/* Test Results */}
            {testResults.length > 0 && (
              <div className="space-y-2 max-h-96 overflow-y-auto">
                <h3 className="font-semibold">Test Results:</h3>
                {testResults.map((result, index) => (
                  <div key={index} className="border rounded p-3 bg-gray-50">
                    <div className="flex items-center gap-2">
                      {result.success ? (
                        <CheckCircle className="w-4 h-4 text-green-500" />
                      ) : (
                        <XCircle className="w-4 h-4 text-red-500" />
                      )}
                      <Badge variant={result.success ? "default" : "destructive"}>
                        {result.success ? "SUCCESS" : "FAILED"}
                      </Badge>
                      <span className="text-sm">{result.message}</span>
                    </div>
                    {result.error && (
                      <div className="mt-2 text-sm text-red-600">
                        Error: {result.error}
                      </div>
                    )}
                    {result.data && (
                      <div className="mt-2 text-xs text-gray-600">
                        <pre>{JSON.stringify(result.data, null, 2)}</pre>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}

            {/* Current State */}
            <div className="border-t pt-4">
              <h3 className="font-semibold mb-2">Current State:</h3>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                <div>
                  <Badge variant="outline">
                    User: {currentUser ? currentUser.name : 'Not logged in'}
                  </Badge>
                </div>
                <div>
                  <Badge variant="outline">
                    Budgets: {budgets.length}
                  </Badge>
                </div>
                <div>
                  <Badge variant="outline">
                    Tasks: {tasks.length}
                  </Badge>
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}; 