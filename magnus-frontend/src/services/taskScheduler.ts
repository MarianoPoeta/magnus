import { Task, TaskType, TaskPriority, ProductRequirement, CookingSchedule } from '../types/Task';

export interface SchedulingRule {
  taskType: TaskType;
  defaultDuration: number;
  defaultPriority: TaskPriority;
  idealDaysBefore: number;
  assignedToRole: 'logistics' | 'cook';
}

export const SCHEDULING_RULES: Record<TaskType, SchedulingRule> = {
  shopping: {
    taskType: 'shopping',
    defaultDuration: 3,
    defaultPriority: 'high',
    idealDaysBefore: 2,
    assignedToRole: 'logistics'
  },
  reservation: {
    taskType: 'reservation',
    defaultDuration: 1,
    defaultPriority: 'urgent',
    idealDaysBefore: 7,
    assignedToRole: 'logistics'
  },
  delivery: {
    taskType: 'delivery',
    defaultDuration: 2,
    defaultPriority: 'high',
    idealDaysBefore: 1,
    assignedToRole: 'logistics'
  },
  preparation: {
    taskType: 'preparation',
    defaultDuration: 4,
    defaultPriority: 'medium',
    idealDaysBefore: 1,
    assignedToRole: 'cook'
  },
  cooking: {
    taskType: 'cooking',
    defaultDuration: 6,
    defaultPriority: 'urgent',
    idealDaysBefore: 0,
    assignedToRole: 'cook'
  },
  setup: {
    taskType: 'setup',
    defaultDuration: 2,
    defaultPriority: 'medium',
    idealDaysBefore: 0,
    assignedToRole: 'logistics'
  },
  cleanup: {
    taskType: 'cleanup',
    defaultDuration: 2,
    defaultPriority: 'low',
    idealDaysBefore: 0,
    assignedToRole: 'logistics'
  },
  need: {
    taskType: 'need',
    defaultDuration: 1,
    defaultPriority: 'medium',
    idealDaysBefore: 1,
    assignedToRole: 'logistics'
  }
};

export class TaskScheduler {
  static generateTasksFromBudget(budget: any): Task[] {
    const eventDate = new Date(budget.eventDate);
    const tasks: Task[] = [];
    const now = new Date();

    // Generate basic tasks
    const taskTypes: TaskType[] = ['reservation', 'shopping', 'delivery', 'preparation', 'cooking', 'setup'];
    
    if (budget.guestCount > 20) {
      taskTypes.push('need');
    }

    taskTypes.forEach((taskType, index) => {
      const rule = SCHEDULING_RULES[taskType];
      const dueDate = new Date(eventDate);
      dueDate.setDate(dueDate.getDate() - rule.idealDaysBefore);
      
      const task: Task = {
        id: `${budget.id}_${taskType}_${index}`,
        type: taskType,
        description: this.generateDescription(taskType, budget),
        relatedBudgetId: budget.id,
        assignedToRole: rule.assignedToRole,
        dueDate: dueDate.toISOString(),
        estimatedDuration: rule.defaultDuration,
        status: 'todo',
        priority: rule.defaultPriority,
        dependencies: [],
        autoScheduled: true,
        createdAt: now.toISOString(),
        updatedAt: now.toISOString(),
        // Add product requirements for shopping tasks
        ...(taskType === 'shopping' && { 
          productRequirements: this.generateProductRequirements(budget) 
        }),
        // Add cooking schedule for cooking tasks
        ...(taskType === 'cooking' && { 
          cookingSchedule: this.generateCookingSchedule(budget, taskType) 
        })
      };

      tasks.push(task);
    });

    return tasks;
  }

  private static generateDescription(taskType: TaskType, budget: any): string {
    const client = budget.clientName;
    const guests = budget.guestCount;

    switch (taskType) {
      case 'reservation':
        return `Confirmar reservas para evento de ${client} (${guests} invitados)`;
      case 'shopping':
        return `Comprar suministros para evento de ${client}`;
      case 'delivery':
        return `Entregar suministros para evento de ${client}`;
      case 'preparation':
        return `Preparar ingredientes para evento de ${client}`;
      case 'cooking':
        return `Cocinar para evento de ${client} (${guests} invitados)`;
      case 'setup':
        return `Montar evento de ${client}`;
      case 'cleanup':
        return `Limpiar después del evento de ${client}`;
      case 'need':
        return `Gestionar necesidades especiales para ${client}`;
      default:
        return `Tarea para evento de ${client}`;
    }
  }

  static getTasksByRole(tasks: Task[], role: 'logistics' | 'cook'): Task[] {
    return tasks
      .filter(task => task.assignedToRole === role)
      .sort((a, b) => {
        const priorityOrder = { urgent: 4, high: 3, medium: 2, low: 1 };
        const priorityDiff = priorityOrder[b.priority] - priorityOrder[a.priority];
        
        if (priorityDiff !== 0) return priorityDiff;
        
        return new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime();
      });
  }

  /**
   * Generate basic product requirements for a shopping task
   */
  private static generateProductRequirements(budget: any): ProductRequirement[] {
    const requirements: ProductRequirement[] = [];
    const guestCount = budget.guestCount;

    // Basic supplies based on guest count
    requirements.push({
      id: `req-beverages-${budget.id}`,
      productId: 'beverages',
      productName: 'Bebidas variadas',
      quantity: Math.ceil(guestCount * 1.5),
      unit: 'unidades',
      category: 'Bebidas',
      isPurchased: false,
      notes: `Para ${guestCount} invitados`
    });

    requirements.push({
      id: `req-plates-${budget.id}`,
      productId: 'disposable-plates',
      productName: 'Platos y vasos desechables',
      quantity: guestCount,
      unit: 'sets',
      category: 'Utensilios',
      isPurchased: false,
      notes: `Sets completos para ${guestCount} personas`
    });

    if (guestCount > 20) {
      requirements.push({
        id: `req-ice-${budget.id}`,
        productId: 'ice-bags',
        productName: 'Bolsas de hielo',
        quantity: Math.ceil(guestCount / 20),
        unit: 'bolsas',
        category: 'Refrigeración',
        isPurchased: false,
        notes: `Bolsas de 5kg para evento grande`
      });
    }

    return requirements;
  }

  /**
   * Generate basic cooking schedule for a cooking task
   */
  private static generateCookingSchedule(budget: any, taskType: TaskType): CookingSchedule {
    const eventDate = budget.eventDate;
    const guestCount = budget.guestCount;
    
    // Estimate cooking time based on guest count
    let hoursBeforeEvent = 2;
    if (guestCount > 50) {
      hoursBeforeEvent = 4;
    } else if (guestCount > 20) {
      hoursBeforeEvent = 3;
    }

    const cookingDateTime = new Date(eventDate);
    cookingDateTime.setHours(cookingDateTime.getHours() - hoursBeforeEvent);
    
    return {
      id: `cooking-schedule-${budget.id}`,
      eventDate,
      cookingTime: `${cookingDateTime.getHours().toString().padStart(2, '0')}:${cookingDateTime.getMinutes().toString().padStart(2, '0')}`,
      mealType: 'lunch',
      menuName: 'Menú principal',
      guestCount,
      ingredients: this.generateBasicIngredients(budget),
      specialInstructions: `Preparar para ${guestCount} personas. Coordinar con logística para entrega de ingredientes.`
    };
  }

  /**
   * Generate basic ingredients for cooking
   */
  private static generateBasicIngredients(budget: any): ProductRequirement[] {
    const ingredients: ProductRequirement[] = [];
    const guestCount = budget.guestCount;

    ingredients.push(
      {
        id: `ing-rice-${budget.id}`,
        productId: 'rice',
        productName: 'Arroz',
        quantity: Math.ceil(guestCount * 0.1),
        unit: 'kg',
        category: 'Granos',
        isPurchased: false,
        notes: 'Arroz blanco de grano largo'
      },
      {
        id: `ing-chicken-${budget.id}`,
        productId: 'chicken',
        productName: 'Pollo',
        quantity: Math.ceil(guestCount * 0.25),
        unit: 'kg',
        category: 'Carnes',
        isPurchased: false,
        notes: 'Pollo fresco, sin menudencias'
      },
      {
        id: `ing-vegetables-${budget.id}`,
        productId: 'vegetables',
        productName: 'Vegetales mixtos',
        quantity: Math.ceil(guestCount * 0.15),
        unit: 'kg',
        category: 'Verduras',
        isPurchased: false,
        notes: 'Zanahoria, cebolla, pimentón'
      }
    );

    return ingredients;
  }
} 