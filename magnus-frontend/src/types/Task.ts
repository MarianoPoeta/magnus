export type TaskType = 'shopping' | 'reservation' | 'delivery' | 'cooking' | 'need' | 'preparation' | 'setup' | 'cleanup';
export type TaskStatus = 'todo' | 'in_progress' | 'done' | 'blocked';
export type TaskPriority = 'low' | 'medium' | 'high' | 'urgent';

export interface TaskDependency {
  id: string;
  dependsOnTaskId: string;
  dependencyType: 'blocks' | 'requires' | 'suggests';
  notes?: string;
}

export interface Task {
  id: string;
  type: TaskType;
  description: string;
  relatedBudgetId: string;
  relatedMealId?: string;
  relatedActivityId?: string;
  assignedToRole: 'logistics' | 'cook';
  assignedTo?: string;
  dueDate: string;
  estimatedDuration?: number; // in hours
  status: TaskStatus;
  priority: TaskPriority;
  dependencies: TaskDependency[];
  blockedBy?: string[]; // IDs of tasks that block this one
  blocks?: string[]; // IDs of tasks that this task blocks
  autoScheduled?: boolean; // Whether this task was auto-scheduled
  notes?: string;
  invoiceUrl?: string;
  needs?: Need[];
  productRequirements?: ProductRequirement[]; // For shopping tasks
  cookingSchedule?: CookingSchedule; // For cooking tasks
  createdAt: string;
  updatedAt: string;
}

export interface ProductRequirement {
  id: string;
  productId: string;
  productName: string;
  quantity: number;
  unit: string;
  category: string;
  isPurchased: boolean;
  purchasedBy?: string;
  purchasedAt?: string;
  notes?: string;
}

export interface ShoppingItem {
  id: string;
  productName: string;
  totalQuantity: number;
  unit: string;
  category: string;
  budgetIds: string[];
  clientNames: string[];
  isPurchased: boolean;
  purchasedQuantity: number;
  notes?: string;
}

export interface CookingSchedule {
  id: string;
  eventDate: string;
  cookingTime: string; // HH:MM format
  mealType: 'breakfast' | 'lunch' | 'dinner' | 'snack';
  menuName: string;
  guestCount: number;
  ingredients: ProductRequirement[];
  specialInstructions?: string;
}

export type Need = {
  id: string;
  description: string;
  quantity: number;
  requestedBy: string;
  status: TaskStatus;
  fulfilledBy?: string;
  productRequirement?: ProductRequirement;
}; 