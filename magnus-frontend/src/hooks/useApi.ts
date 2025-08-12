import { useCallback, useEffect, useState } from 'react';
import { useStore } from '../store';
import { apiServices, ApiError, authService } from '../services/api';

// Custom hook for API integration with store
export const useApi = () => {
  const store = useStore();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Error handler
  const handleError = useCallback((error: ApiError) => {
    setError(error.message);
    console.error('API Error:', error);
    
    // Handle specific error cases
    if (error.status === 401) {
      // Unauthorized - redirect to login
      store.setCurrentUser(null);
      localStorage.removeItem(import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken');
      window.location.href = '/login';
    } else if (error.status === 403) {
      // Forbidden - show access denied message
      setError('Access denied. You do not have permission to perform this action.');
    }
  }, [store]);

  // Clear error
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  // Authentication
  const login = useCallback(async (credentials: { username: string; password: string; rememberMe?: boolean }) => {
    setIsLoading(true);
    clearError();
    
    try {
      const res = await apiServices.auth.login(credentials);
      if (res.id_token) {
        // After login, fetch current user
        const user = await apiServices.auth.getCurrentUser();
        store.setCurrentUser({
          id: user?.id,
          name: `${user?.firstName || ''} ${user?.lastName || ''}`.trim() || user?.login,
          email: user?.email,
          role: (user?.authorities?.includes('ROLE_ADMIN') ? 'admin' : 'sales') as any,
        } as any);
      }
      return res;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const logout = useCallback(async () => {
    try {
      await apiServices.auth.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Clear local data regardless of API response
      localStorage.removeItem(import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken');
      store.setCurrentUser(null);
    }
  }, [store]);

  // Budget Management
  const loadBudgets = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.budgets.getBudgets(params);
      store.setBudgets(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createBudget = useCallback(async (budgetData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.budgets.createBudget(budgetData);
      store.addBudget(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateBudget = useCallback(async (id: string, budgetData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.budgets.updateBudget({ ...budgetData, id });
      store.updateBudget(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteBudget = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.budgets.deleteBudget(id);
      store.deleteBudget(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Activity Management
  const loadActivities = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.activities.getActivities(params);
      store.setActivities(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createActivity = useCallback(async (activityData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.activities.createActivity(activityData);
      store.addActivity(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateActivity = useCallback(async (id: string, activityData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.activities.updateActivity({ ...activityData, id });
      store.updateActivity(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteActivity = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.activities.deleteActivity(id);
      store.deleteActivity(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Accommodation Management
  const loadAccommodations = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.accommodations.getAccommodations(params);
      store.setAccommodations(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createAccommodation = useCallback(async (accommodationData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.accommodations.createAccommodation(accommodationData);
      store.addAccommodation(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateAccommodation = useCallback(async (id: string, accommodationData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.accommodations.updateAccommodation({ ...accommodationData, id });
      store.updateAccommodation(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteAccommodation = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.accommodations.deleteAccommodation(id);
      store.deleteAccommodation(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Menu Management
  const loadMenus = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.menus.getMenus(params);
      store.setMenus(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createMenu = useCallback(async (menuData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.menus.createMenu(menuData);
      store.addMenu(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateMenu = useCallback(async (id: string, menuData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.menus.updateMenu({ ...menuData, id });
      store.updateMenu(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteMenu = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.menus.deleteMenu(id);
      store.deleteMenu(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Product Management
  const loadProducts = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.products.getProducts(params);
      store.setProducts(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createProduct = useCallback(async (productData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.products.createProduct(productData);
      store.addProduct(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateProduct = useCallback(async (id: string, productData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.products.updateProduct({ ...productData, id });
      store.updateProduct(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteProduct = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.products.deleteProduct(id);
      store.deleteProduct(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Transport Management
  const loadTransports = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.transports.getTransports(params);
      store.setTransportTemplates(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createTransport = useCallback(async (transportData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.transports.createTransport(transportData);
      store.addTransportTemplate(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateTransport = useCallback(async (id: string, transportData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.transports.updateTransport({ ...transportData, id });
      store.updateTransportTemplate(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteTransport = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.transports.deleteTransport(id);
      store.deleteTransportTemplate(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Task Management
  const loadTasks = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.tasks.getTasks(params);
      store.setTasks(response.data);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const createTask = useCallback(async (taskData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.tasks.createTask(taskData);
      store.addTask(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const updateTask = useCallback(async (id: string, taskData: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.tasks.updateTask({ ...taskData, id });
      store.updateTask(response as any);
      return response;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  const deleteTask = useCallback(async (id: string) => {
    setIsLoading(true);
    clearError();
    
    try {
      await apiServices.tasks.deleteTask(id);
      store.deleteTask(id);
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [store, handleError, clearError]);

  // Notification Management
  const loadNotifications = useCallback(async (params?: any) => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.notifications.getNotifications(params);
      // Update notifications in store (you'll need to add this to your store)
      return response.data;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [handleError, clearError]);

  const markNotificationAsRead = useCallback(async (id: string) => {
    try {
      await apiServices.notifications.markAsRead(id);
      store.markNotificationRead(parseInt(id));
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    }
  }, [store, handleError]);

  // Analytics
  const loadDashboardStats = useCallback(async () => {
    setIsLoading(true);
    clearError();
    
    try {
      const response = await apiServices.analytics.getDashboardStats();
      return response.data;
    } catch (error) {
      handleError(error as ApiError);
      throw error;
    } finally {
      setIsLoading(false);
    }
  }, [handleError, clearError]);

  // Initialize data on mount
  useEffect(() => {
    const initializeData = async () => {
      try {
        if (authService.isAuthenticated()) {
          // Ensure current user is populated on hard refresh
          if (!store.currentUser || !(store.currentUser as any).id) {
            try {
              const user = await authService.getCurrentUser();
              store.setCurrentUser({
                id: user?.id,
                name: `${user?.firstName || ''} ${user?.lastName || ''}`.trim() || user?.login,
                email: user?.email,
                role: (user?.authorities?.includes('ROLE_ADMIN') ? 'admin' : 'sales') as any,
              } as any);
            } catch (e) {
              // If token invalid, clear and bail
              localStorage.removeItem(import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken');
              return;
            }
          }

          await Promise.all([
            loadBudgets(),
            loadActivities(),
            loadAccommodations(),
            loadMenus(),
            loadProducts(),
            loadTransports(),
            loadTasks(),
            loadNotifications(),
          ]);
        }
      } catch (error) {
        console.error('Error initializing data:', error);
      }
    };

    initializeData();
  }, [loadBudgets, loadActivities, loadAccommodations, loadMenus, loadProducts, loadTransports, loadTasks, loadNotifications, store.currentUser]);

  return {
    // State
    isLoading,
    error,
    clearError,
    
    // Authentication
    login,
    logout,
    
    // Budget Management
    loadBudgets,
    createBudget,
    updateBudget,
    deleteBudget,
    
    // Activity Management
    loadActivities,
    createActivity,
    updateActivity,
    deleteActivity,
    
    // Accommodation Management
    loadAccommodations,
    createAccommodation,
    updateAccommodation,
    deleteAccommodation,
    
    // Menu Management
    loadMenus,
    createMenu,
    updateMenu,
    deleteMenu,
    
    // Product Management
    loadProducts,
    createProduct,
    updateProduct,
    deleteProduct,
    
    // Transport Management
    loadTransports,
    createTransport,
    updateTransport,
    deleteTransport,
    
    // Task Management
    loadTasks,
    createTask,
    updateTask,
    deleteTask,
    
    // Notification Management
    loadNotifications,
    markNotificationAsRead,
    
    // Analytics
    loadDashboardStats,
  };
}; 