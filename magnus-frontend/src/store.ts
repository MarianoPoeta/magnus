import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { User } from './types/User';
import { Task } from './types/Task';
import { Activity } from './types/Activity';
import { Accommodation } from './types/Accommodation';
import { Menu } from './types/Menu';
import { Food } from './types/Food';
import { Product } from './types/Product';
import { TransportTemplate } from './types/Budget';
import { Client } from './types/Client';
import { WorkflowAutomation, WorkflowTrigger } from './services/workflowAutomation';

// Development flag - set to true to enable session persistence (Vite)
const isDevelopment = import.meta.env.MODE === 'development';

export interface Notification {
  id: number;
  text: string;
  time: string;
  read?: boolean;
  role?: 'admin' | 'sales' | 'logistics' | 'cook';
}

export interface Budget {
  id: string;
  name: string;
  clientName: string;
  eventType: string;
  activities: string[];
  status: 'draft' | 'pending' | 'approved' | 'reserva' | 'rejected' | 'completed';
  eventDate: string;
  totalAmount: number;
  mealsAmount: number;
  activitiesAmount: number;
  transportAmount: number;
  accommodationAmount: number;
  guestCount: number;
  createdAt: string;
  templateId: string;
  selectedMenus?: Menu[];
  selectedAccommodations?: Accommodation[];
}

export interface Toast {
  id: string;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  duration?: number;
}

export interface StoreState {
  // User Management
  users: User[];
  currentUser: User | null;
  setCurrentUser: (user: User | null) => void;
  
  // Client Management
  clients: Client[];
  setClients: (clients: Client[]) => void;
  addClient: (client: Client) => void;
  updateClient: (client: Client) => void;
  deleteClient: (clientId: string) => void;
  findOrCreateClient: (clientData: { name: string; email: string; phone: string }) => Client;
  
  // Tasks Management
  tasks: Task[];
  setTasks: (tasks: Task[]) => void;
  updateTask: (task: Task) => void;
  addTask: (task: Task) => void;
  deleteTask: (taskId: string) => void;
  
  // Budget Management
  budgets: Budget[];
  setBudgets: (budgets: Budget[]) => void;
  addBudget: (budget: Budget) => void;
  updateBudget: (budget: Budget) => void;
  deleteBudget: (budgetId: string) => void;
  
  // Activity Management
  activities: Activity[];
  setActivities: (activities: Activity[]) => void;
  addActivity: (activity: Activity) => void;
  updateActivity: (activity: Activity) => void;
  deleteActivity: (activityId: string) => void;
  
  // Accommodation Management
  accommodations: Accommodation[];
  setAccommodations: (accommodations: Accommodation[]) => void;
  addAccommodation: (accommodation: Accommodation) => void;
  updateAccommodation: (accommodation: Accommodation) => void;
  deleteAccommodation: (accommodationId: string) => void;
  
  // Menu Management
  menus: Menu[];
  setMenus: (menus: Menu[]) => void;
  addMenu: (menu: Menu) => void;
  updateMenu: (menu: Menu) => void;
  deleteMenu: (menuId: string) => void;
  
  // Food Management
  foods: Food[];
  setFoods: (foods: Food[]) => void;
  addFood: (food: Food) => void;
  updateFood: (food: Food) => void;
  deleteFood: (foodId: string) => void;
  
  // Product Management
  products: Product[];
  setProducts: (products: Product[]) => void;
  addProduct: (product: Product) => void;
  updateProduct: (product: Product) => void;
  deleteProduct: (productId: string) => void;
  
  // Transport Template Management
  transportTemplates: TransportTemplate[];
  setTransportTemplates: (templates: TransportTemplate[]) => void;
  addTransportTemplate: (template: TransportTemplate) => void;
  updateTransportTemplate: (template: TransportTemplate) => void;
  deleteTransportTemplate: (templateId: string) => void;
  
  // Notification Management
  notifications: Notification[];
  addNotification: (notification: Notification) => void;
  markNotificationRead: (id: number) => void;
  
  // Toast Management
  toasts: Toast[];
  addToast: (toast: Toast) => void;
  removeToast: (id: string) => void;
}

// Enhanced store with better persistence and error handling
export const useStore = create<StoreState>()(
  persist(
    (set, get) => ({
      // User Management
      users: [],
      currentUser: null,
      setCurrentUser: (user: User | null) => {
        set({ currentUser: user });
        // Save to sessionStorage for immediate persistence
        if (typeof window !== 'undefined') {
          if (user) {
            sessionStorage.setItem('currentUser', JSON.stringify(user));
          } else {
            sessionStorage.removeItem('currentUser');
          }
        }
      },
      
      // Client Management
      clients: [],
      setClients: (clients: Client[]) => set({ clients }),
      addClient: (client: Client) => {
        set((state) => ({ clients: [...state.clients, client] }));
      },
      updateClient: (client: Client) => {
        set((state) => ({
          clients: state.clients.map((c) => (c.id === client.id ? client : c))
        }));
      },
      deleteClient: (clientId: string) => {
        set((state) => ({
          clients: state.clients.filter((c) => c.id !== clientId)
        }));
      },
      findOrCreateClient: (clientData: { name: string; email: string; phone: string }) => {
        const state = get();
        
        // Try to find existing client by email or phone
        let existingClient = state.clients.find(
          c => c.email.toLowerCase() === clientData.email.toLowerCase() ||
               c.phone === clientData.phone
        );
        
        if (existingClient) {
          // Update existing client with new information
          const updatedClient = {
            ...existingClient,
            name: clientData.name,
            email: clientData.email,
            phone: clientData.phone,
            updatedAt: new Date().toISOString()
          };
          state.updateClient(updatedClient);
          return updatedClient;
        } else {
          // Create new client
          const newClient: Client = {
            id: `client-${Date.now()}`,
            name: clientData.name,
            email: clientData.email,
            phone: clientData.phone,
            isActive: true,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
          };
          state.addClient(newClient);
          return newClient;
        }
      },
      
      // Tasks Management
      tasks: [],
      setTasks: (tasks: Task[]) => set({ tasks }),
      updateTask: (task: Task) => {
        set((state) => ({
          tasks: state.tasks.map((t) => (t.id === task.id ? task : t))
        }));
      },
      addTask: (task: Task) => {
        set((state) => ({ tasks: [...state.tasks, task] }));
      },
      deleteTask: (taskId: string) => {
        set((state) => ({
          tasks: state.tasks.filter((t) => t.id !== taskId)
        }));
      },
      
      // Budget Management
      budgets: [],
      setBudgets: (budgets: Budget[]) => set({ budgets }),
      addBudget: (budget: Budget) => {
        set((state) => ({ budgets: [...state.budgets, budget] }));
      },
      updateBudget: (budget: Budget) => {
        set((state) => {
          const prevBudget = state.budgets.find((b) => b.id === budget.id);
          const statusChangedToReserva = prevBudget && prevBudget.status !== 'reserva' && budget.status === 'reserva';
          const updatedBudgets = state.budgets.map((b) => (b.id === budget.id ? budget : b));
          
          // Enhanced workflow automation
          if (statusChangedToReserva && prevBudget) {
            const trigger: WorkflowTrigger = {
              budgetId: budget.id,
              previousStatus: prevBudget.status,
              newStatus: budget.status,
              triggerDate: new Date().toISOString()
            };

            const workflowResult = WorkflowAutomation.handleStatusChange(
              trigger,
              budget,
              state.products,
              state.menus,
              state.activities
            );

            if (workflowResult) {
              // Add generated tasks
              workflowResult.tasks.forEach(task => {
                state.addTask(task);
              });

              // Add generated notifications
              workflowResult.notifications.forEach(notification => {
                state.addNotification(notification);
              });

              // Add success toast
              state.addToast({
                id: `workflow-${Date.now()}`,
                message: `Reserva confirmada. Se generaron ${workflowResult.tasks.length} tareas automáticamente.`,
                type: 'success',
                duration: 5000
              });
            }
          }
          
          return { budgets: updatedBudgets };
        });
      },
      deleteBudget: (budgetId: string) => {
        set((state) => ({
          budgets: state.budgets.filter((b) => b.id !== budgetId)
        }));
      },
      
      // Activity Management
      activities: [],
      setActivities: (activities: Activity[]) => set({ activities }),
      addActivity: (activity: Activity) => {
        set((state) => ({ activities: [...state.activities, activity] }));
      },
      updateActivity: (activity: Activity) => {
        set((state) => ({
          activities: state.activities.map((a) => (a.id === activity.id ? activity : a))
        }));
      },
      deleteActivity: (activityId: string) => {
        set((state) => ({
          activities: state.activities.filter((a) => a.id !== activityId)
        }));
      },
      
      // Accommodation Management
      accommodations: [],
      setAccommodations: (accommodations: Accommodation[]) => set({ accommodations }),
      addAccommodation: (accommodation: Accommodation) => {
        set((state) => ({ accommodations: [...state.accommodations, accommodation] }));
      },
      updateAccommodation: (accommodation: Accommodation) => {
        set((state) => ({
          accommodations: state.accommodations.map((a) => (a.id === accommodation.id ? accommodation : a))
        }));
      },
      deleteAccommodation: (accommodationId: string) => {
        set((state) => ({
          accommodations: state.accommodations.filter((a) => a.id !== accommodationId)
        }));
      },
      
      // Menu Management
      menus: [],
      setMenus: (menus: Menu[]) => set({ menus }),
      addMenu: (menu: Menu) => {
        set((state) => ({ menus: [...state.menus, menu] }));
      },
      updateMenu: (menu: Menu) => {
        set((state) => ({
          menus: state.menus.map((m) => (m.id === menu.id ? menu : m))
        }));
      },
      deleteMenu: (menuId: string) => {
        set((state) => ({
          menus: state.menus.filter((m) => m.id !== menuId)
        }));
      },
      
      // Food Management
      foods: [],
      setFoods: (foods: Food[]) => set({ foods }),
      addFood: (food: Food) => {
        set((state) => ({ foods: [...state.foods, food] }));
      },
      updateFood: (food: Food) => {
        set((state) => ({
          foods: state.foods.map((f) => (f.id === food.id ? food : f))
        }));
      },
      deleteFood: (foodId: string) => {
        set((state) => ({
          foods: state.foods.filter((f) => f.id !== foodId)
        }));
      },
      
      // Product Management
      products: [],
      setProducts: (products: Product[]) => set({ products }),
      addProduct: (product: Product) => {
        set((state) => ({ products: [...state.products, product] }));
      },
      updateProduct: (product: Product) => {
        set((state) => ({
          products: state.products.map((p) => (p.id === product.id ? product : p))
        }));
      },
      deleteProduct: (productId: string) => {
        set((state) => ({
          products: state.products.filter((p) => p.id !== productId)
        }));
      },
      
      // Transport Template Management
      transportTemplates: [],
      setTransportTemplates: (templates: TransportTemplate[]) => set({ transportTemplates: templates }),
      addTransportTemplate: (template: TransportTemplate) => {
        set((state) => ({ transportTemplates: [...state.transportTemplates, template] }));
      },
      updateTransportTemplate: (template: TransportTemplate) => {
        set((state) => ({
          transportTemplates: state.transportTemplates.map((t) => (t.id === template.id ? template : t))
        }));
      },
      deleteTransportTemplate: (templateId: string) => {
        set((state) => ({
          transportTemplates: state.transportTemplates.filter((t) => t.id !== templateId)
        }));
      },
      
      // Notification Management
      notifications: [],
      addNotification: (notification: Notification) => {
        set((state) => ({ notifications: [...state.notifications, notification] }));
      },
      markNotificationRead: (id: number) => {
        set((state) => ({
          notifications: state.notifications.map((n) =>
            n.id === id ? { ...n, read: true } : n
          )
        }));
      },
      
      // Toast Management
      toasts: [],
      addToast: (toast: Toast) => {
        set((state) => ({ toasts: [...state.toasts, toast] }));
        // Auto-remove toast after duration
        setTimeout(() => {
          get().removeToast(toast.id);
        }, toast.duration || 5000);
      },
      removeToast: (id: string) => {
        set((state) => ({
          toasts: state.toasts.filter((t) => t.id !== id)
        }));
      },
    }),
    {
      name: 'magnus-frontend-storage',
      storage: createJSONStorage(() => sessionStorage),
      partialize: (state) => ({
        clients: state.clients,
        budgets: state.budgets,
        activities: state.activities,
        accommodations: state.accommodations,
        menus: state.menus,
        products: state.products,
        transportTemplates: state.transportTemplates,
        tasks: state.tasks,
        notifications: state.notifications,
      }),
      onRehydrateStorage: () => (state) => {
        // Restore current user from sessionStorage if available
        if (typeof window !== 'undefined') {
          const savedUser = sessionStorage.getItem('currentUser');
          if (savedUser && state) {
            try {
              const user = JSON.parse(savedUser);
              state.setCurrentUser(user);
            } catch (error) {
              console.error('Error restoring user from sessionStorage:', error);
            }
          }
        }
      },
    }
  )
); 