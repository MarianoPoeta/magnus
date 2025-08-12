import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Toaster from './components/ui/toaster';
import ErrorBoundary from './components/ErrorBoundary';

import { useStore } from './store';
import { useApi } from './hooks/useApi';
import { authService } from './services/api';

// Direct imports to fix dynamic import issues
import Dashboard from './pages/Dashboard';
import EnhancedBudgets from './pages/EnhancedBudgets';
import BudgetDetails from './pages/BudgetDetails';
import Activities from './pages/Activities';
import Accommodations from './pages/Accommodations';
import Menus from './pages/Menus';
import Products from './pages/Products';
import Foods from './pages/Foods';
import Transports from './pages/Transports';
import EnhancedConfiguration from './pages/EnhancedConfiguration';
import AdminConfig from './pages/AdminConfig';
import SalesDashboard from './pages/SalesDashboard';
import Profile from './pages/Profile';
import Login from './pages/Login';
import NotFound from './pages/NotFound';
import Clients from './pages/Clients';
import WeeklyPlanning from './pages/WeeklyPlanning';
import TaskDashboard from './pages/TaskDashboard';
import WorkflowTest from './pages/WorkflowTest';

import './App.css';



function RequireAuth({ children }: { children: JSX.Element }) {
  const isAuthed = authService.isAuthenticated();
  if (!isAuthed) return <Navigate to="/login" />;
  return children;
}

const App: React.FC = () => {
  // Mount API integration to load data when authenticated
  useApi();

  return (
    <ErrorBoundary>
      <Router>
        <Routes>
          {/* Public route */}
          <Route path="/login" element={<Login />} />

          {/* Private routes wrapped with Layout */}
          <Route element={<RequireAuth><Layout /></RequireAuth>}>
            <Route path="/" element={<Dashboard />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/budgets" element={<EnhancedBudgets />} />
            <Route path="/budgets/:id" element={<BudgetDetails />} />
            <Route path="/activities" element={<Activities />} />
            <Route path="/accommodations" element={<Accommodations />} />
            <Route path="/menus" element={<Menus />} />
            <Route path="/products" element={<Products />} />
            <Route path="/foods" element={<Foods />} />
            <Route path="/transports" element={<Transports />} />
            <Route path="/clients" element={<Clients />} />
            <Route path="/weekly-planning" element={<WeeklyPlanning />} />
            <Route path="/tasks" element={<TaskDashboard />} />
            <Route path="/configuration" element={<EnhancedConfiguration />} />
            <Route path="/admin" element={<AdminConfig />} />
            <Route path="/workflow-test" element={<WorkflowTest />} />
            <Route path="/sales-dashboard" element={<SalesDashboard />} />
            <Route path="/profile" element={<Profile />} />
          </Route>

          {/* Fallback */}
          <Route path="*" element={<NotFound />} />
        </Routes>
        <Toaster />
      </Router>
    </ErrorBoundary>
  );
};

export default App;
