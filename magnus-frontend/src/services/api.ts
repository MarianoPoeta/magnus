// API Configuration and Service Layer
// This file handles all backend communication for Party Budget Bliss

// Base API Configuration
export const API_CONFIG = {
  BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  TIMEOUT: 10000,
  RETRY_ATTEMPTS: 3,
};

// Token storage key from environment for JHipster JWT
const TOKEN_KEY = import.meta.env.VITE_JWT_STORAGE_KEY || 'authToken';

// JHipster API Response Types
export interface JHipsterResponse<T = any> {
  // JHipster returns data directly, not wrapped in success/data structure
  data?: T;
  // For paginated responses
  content?: T[];
  totalElements?: number;
  totalPages?: number;
  size?: number;
  number?: number;
  first?: boolean;
  last?: boolean;
}

// Keep existing types for backward compatibility
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  error?: string;
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}

// Error Handling
export class ApiError extends Error {
  constructor(
    public status: number,
    public message: string,
    public data?: any
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

// HTTP Client
class ApiClient {
  private baseURL: string;
  private timeout: number;

  constructor(baseURL: string, timeout: number) {
    this.baseURL = baseURL;
    this.timeout = timeout;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseURL}${endpoint}`;
    const token = localStorage.getItem(TOKEN_KEY);

    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
      },
      ...options,
    };

    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), this.timeout);

      const response = await fetch(url, {
        ...config,
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        throw new ApiError(
          response.status,
          `HTTP ${response.status}: ${response.statusText}`,
          await response.json().catch(() => null)
        );
      }

      // JHipster returns data directly
      const data = await response.json();
      return data;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      }
      throw new ApiError(0, error instanceof Error ? error.message : 'Network error');
    }
  }

  // Generic HTTP methods
  async get<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'GET' });
  }

  async post<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async put<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async patch<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PATCH',
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  async delete<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'DELETE' });
  }
}

// Create API client instance
export const apiClient = new ApiClient(API_CONFIG.BASE_URL, API_CONFIG.TIMEOUT);

// JHipster Authentication Service
export const authService = {
  async login(credentials: { username: string; password: string; rememberMe?: boolean }) {
    const response = await apiClient.post<{ id_token: string }>('/authenticate', credentials);
    if (response.id_token) {
      localStorage.setItem(TOKEN_KEY, response.id_token);
    }
    return response;
  },

  async register(userData: { 
    login: string; 
    email: string; 
    password: string; 
    firstName?: string; 
    lastName?: string; 
    langKey?: string;
  }) {
    return apiClient.post<any>('/register', userData);
  },

  async logout() {
    localStorage.removeItem(TOKEN_KEY);
    return Promise.resolve();
  },

  async getCurrentUser() {
    return apiClient.get<any>('/account');
  },

  async updateAccount(userData: any) {
    return apiClient.post<any>('/account', userData);
  },

  async changePassword(passwordData: { currentPassword: string; newPassword: string }) {
    return apiClient.post<any>('/account/change-password', passwordData);
  },

  async resetPasswordInit(email: string) {
    return apiClient.post<any>('/account/reset-password/init', { email });
  },

  async resetPasswordFinish(key: string, newPassword: string) {
    return apiClient.post<any>('/account/reset-password/finish', { key, newPassword });
  },

  async activateAccount(key: string) {
    return apiClient.get<any>(`/activate?key=${key}`);
  },

  isAuthenticated(): boolean {
    return !!localStorage.getItem(TOKEN_KEY);
  },

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  },
};

// Helper function to convert JHipster paginated response to our format
function convertPaginatedResponse<T>(jhipsterResponse: any): PaginatedResponse<T> {
  return {
    data: jhipsterResponse.content || jhipsterResponse,
    pagination: {
      page: jhipsterResponse.number || 0,
      limit: jhipsterResponse.size || 20,
      total: jhipsterResponse.totalElements || 0,
      totalPages: jhipsterResponse.totalPages || 0,
    },
  };
}

// User Management Service (uses JHipster's user endpoints)
export const userService = {
  async getUsers(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/users?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getUserById(login: string) {
    return apiClient.get<any>(`/users/${login}`);
  },

  async createUser(userData: any) {
    return apiClient.post<any>('/admin/users', userData);
  },

  async updateUser(userData: any) {
    return apiClient.put<any>('/admin/users', userData);
  },

  async deleteUser(login: string) {
    return apiClient.delete(`/admin/users/${login}`);
  },

  async getAuthorities() {
    return apiClient.get<string[]>('/authorities');
  },
};

// App Users (domain users for createdBy links)
export const appUserService = {
  async getAppUsers(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    const response = await apiClient.get<any>(`/app-users?${queryParams}`);
    return convertPaginatedResponse(response);
  },
};

// Budget Management Service (JHipster format)
export const budgetService = {
  async getBudgets(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/budgets?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getBudgetById(id: number) {
    return apiClient.get<any>(`/budgets/${id}`);
  },

  async createBudget(budgetData: any) {
    return apiClient.post<any>('/budgets', budgetData);
  },

  async updateBudget(budgetData: any) {
    return apiClient.put<any>(`/budgets/${budgetData.id}`, budgetData);
  },

  async deleteBudget(id: number) {
    return apiClient.delete(`/budgets/${id}`);
  },
};

// Activity Management Service
export const activityService = {
  async getActivities(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/activities?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getActivityById(id: number) {
    return apiClient.get<any>(`/activities/${id}`);
  },

  async createActivity(activityData: any) {
    return apiClient.post<any>('/activities', activityData);
  },

  async updateActivity(activityData: any) {
    return apiClient.put<any>(`/activities/${activityData.id}`, activityData);
  },

  async deleteActivity(id: number) {
    return apiClient.delete(`/activities/${id}`);
  },
};

// Accommodation Management Service
export const accommodationService = {
  async getAccommodations(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/accommodations?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getAccommodationById(id: number) {
    return apiClient.get<any>(`/accommodations/${id}`);
  },

  async createAccommodation(accommodationData: any) {
    return apiClient.post<any>('/accommodations', accommodationData);
  },

  async updateAccommodation(accommodationData: any) {
    return apiClient.put<any>(`/accommodations/${accommodationData.id}`, accommodationData);
  },

  async deleteAccommodation(id: number) {
    return apiClient.delete(`/accommodations/${id}`);
  },
};

// Menu Management Service
export const menuService = {
  async getMenus(params?: { page?: number; size?: number; sort?: string; eagerload?: boolean }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    if (params?.eagerload !== undefined) queryParams.append('eagerload', params.eagerload.toString());
    
    const response = await apiClient.get<any>(`/menus?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getMenuById(id: number) {
    return apiClient.get<any>(`/menus/${id}`);
  },

  async createMenu(menuData: any) {
    return apiClient.post<any>('/menus', menuData);
  },

  async updateMenu(menuData: any) {
    return apiClient.put<any>(`/menus/${menuData.id}`, menuData);
  },

  async deleteMenu(id: number) {
    return apiClient.delete(`/menus/${id}`);
  },
};

// Product Management Service
export const productService = {
  async getProducts(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/products?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getProductById(id: number) {
    return apiClient.get<any>(`/products/${id}`);
  },

  async createProduct(productData: any) {
    return apiClient.post<any>('/products', productData);
  },

  async updateProduct(productData: any) {
    return apiClient.put<any>(`/products/${productData.id}`, productData);
  },

  async deleteProduct(id: number) {
    return apiClient.delete(`/products/${id}`);
  },
};

// Transport Management Service
export const transportService = {
  async getTransports(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/transports?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getTransportById(id: number) {
    return apiClient.get<any>(`/transports/${id}`);
  },

  async createTransport(transportData: any) {
    return apiClient.post<any>('/transports', transportData);
  },

  async updateTransport(transportData: any) {
    return apiClient.put<any>(`/transports/${transportData.id}`, transportData);
  },

  async deleteTransport(id: number) {
    return apiClient.delete(`/transports/${id}`);
  },
};

// Task Management Service
export const taskService = {
  async getTasks(params?: { page?: number; size?: number; sort?: string; filter?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    if (params?.filter) queryParams.append('filter', params.filter);
    
    const response = await apiClient.get<any>(`/tasks?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getTaskById(id: number) {
    return apiClient.get<any>(`/tasks/${id}`);
  },

  async createTask(taskData: any) {
    return apiClient.post<any>('/tasks', taskData);
  },

  async updateTask(taskData: any) {
    return apiClient.put<any>(`/tasks/${taskData.id}`, taskData);
  },

  async deleteTask(id: number) {
    return apiClient.delete(`/tasks/${id}`);
  },
};

// Client Management Service
export const clientService = {
  async getClients(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/clients?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getClientById(id: number) {
    return apiClient.get<any>(`/clients/${id}`);
  },

  async createClient(clientData: any) {
    return apiClient.post<any>('/clients', clientData);
  },

  async updateClient(clientData: any) {
    return apiClient.put<any>(`/clients/${clientData.id}`, clientData);
  },

  async deleteClient(id: number) {
    return apiClient.delete(`/clients/${id}`);
  },
};

// App User Management Service (Custom users) - duplicate removed (see earlier definition)

// Notification Management Service
export const notificationService = {
  async getNotifications(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    const response = await apiClient.get<any>(`/notifications?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async markAsRead(id: number) {
    // Partial update to set read flag; backend supports PATCH
    return apiClient.patch<any>(`/notifications/${id}`, { id, isRead: true });
  },
};

// 🔥 NEW: Workflow Automation Service
export const workflowService = {
  async triggerTasks(budgetId: number) {
    return apiClient.post<any>(`/workflow/trigger-tasks/${budgetId}`);
  },

  async updateBudgetStatus(budgetId: number, status: string) {
    return apiClient.patch<any>(`/workflow/budget-status/${budgetId}`, { status });
  },

  async approveBudget(budgetId: number) {
    return apiClient.post<any>(`/workflow/approve-budget/${budgetId}`);
  },

  async getBudgetStatus(budgetId: number) {
    return apiClient.get<any>(`/workflow/budget-status/${budgetId}`);
  },
};

// Shopping Item Management Service
export const shoppingItemService = {
  async getShoppingItems(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/shopping-items?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getShoppingItemById(id: number) {
    return apiClient.get<any>(`/shopping-items/${id}`);
  },

  async createShoppingItem(shoppingItemData: any) {
    return apiClient.post<any>('/shopping-items', shoppingItemData);
  },

  async updateShoppingItem(shoppingItemData: any) {
    return apiClient.put<any>(`/shopping-items/${shoppingItemData.id}`, shoppingItemData);
  },

  async deleteShoppingItem(id: number) {
    return apiClient.delete(`/shopping-items/${id}`);
  },
};

// Cooking Schedule Management Service
export const cookingScheduleService = {
  async getCookingSchedules(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/cooking-schedules?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getCookingScheduleById(id: number) {
    return apiClient.get<any>(`/cooking-schedules/${id}`);
  },

  async createCookingSchedule(cookingScheduleData: any) {
    return apiClient.post<any>('/cooking-schedules', cookingScheduleData);
  },

  async updateCookingSchedule(cookingScheduleData: any) {
    return apiClient.put<any>(`/cooking-schedules/${cookingScheduleData.id}`, cookingScheduleData);
  },

  async deleteCookingSchedule(id: number) {
    return apiClient.delete(`/cooking-schedules/${id}`);
  },
};

// Food Item Management Service
export const foodItemService = {
  async getFoodItems() {
    return apiClient.get<any[]>('/food-items');
  },

  async getFoodItemById(id: number) {
    return apiClient.get<any>(`/food-items/${id}`);
  },

  async createFoodItem(foodItemData: any) {
    return apiClient.post<any>('/food-items', foodItemData);
  },

  async updateFoodItem(foodItemData: any) {
    return apiClient.put<any>(`/food-items/${foodItemData.id}`, foodItemData);
  },

  async deleteFoodItem(id: number) {
    return apiClient.delete(`/food-items/${id}`);
  },
};

// Menu Item Management Service
export const menuItemService = {
  async getMenuItems() {
    return apiClient.get<any[]>('/menu-items');
  },

  async getMenuItemById(id: number) {
    return apiClient.get<any>(`/menu-items/${id}`);
  },

  async createMenuItem(menuItemData: any) {
    return apiClient.post<any>('/menu-items', menuItemData);
  },

  async updateMenuItem(menuItemData: any) {
    return apiClient.put<any>(`/menu-items/${menuItemData.id}`, menuItemData);
  },

  async deleteMenuItem(id: number) {
    return apiClient.delete(`/menu-items/${id}`);
  },
};

// Payment Management Service
export const paymentService = {
  async getPayments() {
    return apiClient.get<any[]>('/payments');
  },

  async getPaymentById(id: number) {
    return apiClient.get<any>(`/payments/${id}`);
  },

  async createPayment(paymentData: any) {
    return apiClient.post<any>('/payments', paymentData);
  },

  async updatePayment(paymentData: any) {
    return apiClient.put<any>(`/payments/${paymentData.id}`, paymentData);
  },

  async deletePayment(id: number) {
    return apiClient.delete(`/payments/${id}`);
  },
};

// Weekly Plan Management Service
export const weeklyPlanService = {
  async getWeeklyPlans(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/weekly-plans?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getWeeklyPlanById(id: number) {
    return apiClient.get<any>(`/weekly-plans/${id}`);
  },

  async createWeeklyPlan(weeklyPlanData: any) {
    return apiClient.post<any>('/weekly-plans', weeklyPlanData);
  },

  async updateWeeklyPlan(weeklyPlanData: any) {
    return apiClient.put<any>(`/weekly-plans/${weeklyPlanData.id}`, weeklyPlanData);
  },

  async deleteWeeklyPlan(id: number) {
    return apiClient.delete(`/weekly-plans/${id}`);
  },
};

// Analytics Service (basic placeholders mapped to backend if available later)
export const analyticsService = {
  async getDashboardStats() {
    // Derive minimal stats from server if an endpoint exists later; for now return empty object to prevent frontend errors
    return { data: {} } as any;
  },
};

// System Configuration Service
export const systemConfigService = {
  async getSystemConfigs(params?: { page?: number; size?: number; sort?: string }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.sort) queryParams.append('sort', params.sort);
    
    const response = await apiClient.get<any>(`/system-configs?${queryParams}`);
    return convertPaginatedResponse(response);
  },

  async getSystemConfigById(id: number) {
    return apiClient.get<any>(`/system-configs/${id}`);
  },

  async createSystemConfig(systemConfigData: any) {
    return apiClient.post<any>('/system-configs', systemConfigData);
  },

  async updateSystemConfig(systemConfigData: any) {
    return apiClient.put<any>(`/system-configs/${systemConfigData.id}`, systemConfigData);
  },

  async deleteSystemConfig(id: number) {
    return apiClient.delete(`/system-configs/${id}`);
  },
};

// Export all services
export const apiServices = {
  auth: authService,
  users: userService,
  budgets: budgetService,
  activities: activityService,
  accommodations: accommodationService,
  menus: menuService,
  products: productService,
  transports: transportService,
  tasks: taskService,
  clients: clientService,
  appUsers: appUserService,
  workflow: workflowService, // 🔥 NEW
  notifications: notificationService,
  shoppingItems: shoppingItemService,
  cookingSchedules: cookingScheduleService,
  foodItems: foodItemService,
  menuItems: menuItemService,
  payments: paymentService,
  weeklyPlans: weeklyPlanService,
  systemConfigs: systemConfigService,
  analytics: analyticsService,
}; 