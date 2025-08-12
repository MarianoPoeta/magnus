import { Task, CookingSchedule, ProductRequirement } from '../types/Task';
import { Budget } from '../store';

export class CookingScheduleService {
  /**
   * Generate cooking schedules for a period
   */
  static generateCookingSchedules(
    budgets: Budget[],
    tasks: Task[],
    startDate: Date,
    endDate: Date
  ): CookingSchedule[] {
    // Filter budgets for the period that are approved
    const periodBudgets = budgets.filter(budget => {
      const eventDate = new Date(budget.eventDate);
      return eventDate >= startDate && 
             eventDate <= endDate && 
             budget.status === 'reserva';
    });

    const schedules: CookingSchedule[] = [];

    periodBudgets.forEach(budget => {
      // Get cooking tasks for this budget
      const cookingTasks = tasks.filter(
        task => task.relatedBudgetId === budget.id && 
               task.type === 'cooking' && 
               task.assignedToRole === 'cook'
      );

      // If no cooking tasks exist, generate basic ones
      if (cookingTasks.length === 0) {
        const basicSchedules = this.generateBasicCookingSchedules(budget);
        schedules.push(...basicSchedules);
      } else {
        // Convert existing cooking tasks to schedules
        cookingTasks.forEach(task => {
          if (task.cookingSchedule) {
            schedules.push(task.cookingSchedule);
          } else {
            // Generate schedule from task data
            const schedule = this.taskToCookingSchedule(task, budget);
            schedules.push(schedule);
          }
        });
      }
    });

    // Sort by date and time
    return schedules.sort((a, b) => {
      const dateComparison = new Date(a.eventDate).getTime() - new Date(b.eventDate).getTime();
      if (dateComparison !== 0) return dateComparison;
      
      return a.cookingTime.localeCompare(b.cookingTime);
    });
  }

  /**
   * Generate basic cooking schedules for a budget
   */
  private static generateBasicCookingSchedules(budget: Budget): CookingSchedule[] {
    const schedules: CookingSchedule[] = [];
    const eventDate = budget.eventDate;
    const guestCount = budget.guestCount;

    // Generate schedules based on typical meal times
    // For now, assume lunch is the main meal
    schedules.push({
      id: `cook-schedule-${budget.id}-lunch`,
      eventDate,
      cookingTime: '11:00', // Start cooking 2 hours before typical lunch
      mealType: 'lunch',
      menuName: 'Menú principal',
      guestCount,
      ingredients: this.generateBasicIngredients(budget, 'lunch'),
      specialInstructions: `Preparar para ${guestCount} personas. Coordinar con logística para entrega de ingredientes.`
    });

    // Add breakfast if it's a full-day event or large event
    if (guestCount > 30) {
      schedules.push({
        id: `cook-schedule-${budget.id}-breakfast`,
        eventDate,
        cookingTime: '07:00',
        mealType: 'breakfast',
        menuName: 'Desayuno',
        guestCount,
        ingredients: this.generateBasicIngredients(budget, 'breakfast'),
        specialInstructions: `Desayuno ligero para ${guestCount} personas.`
      });
    }

    return schedules;
  }

  /**
   * Convert a task to a cooking schedule
   */
  private static taskToCookingSchedule(task: Task, budget: Budget): CookingSchedule {
    return {
      id: `schedule-from-task-${task.id}`,
      eventDate: budget.eventDate,
      cookingTime: this.estimateCookingTime(task, budget),
      mealType: this.determineMealType(task),
      menuName: task.description || 'Menú',
      guestCount: budget.guestCount,
      ingredients: task.productRequirements || this.generateBasicIngredients(budget, 'lunch'),
      specialInstructions: task.notes
    };
  }

  /**
   * Generate basic ingredients for a meal
   */
  private static generateBasicIngredients(budget: Budget, mealType: string): ProductRequirement[] {
    const ingredients: ProductRequirement[] = [];
    const guestCount = budget.guestCount;

    if (mealType === 'breakfast') {
      ingredients.push(
        {
          id: `ing-bread-${budget.id}`,
          productId: 'bread',
          productName: 'Pan tostado',
          quantity: Math.ceil(guestCount / 4),
          unit: 'barras',
          category: 'Panadería',
          isPurchased: false,
          notes: 'Pan integral o blanco'
        },
        {
          id: `ing-coffee-${budget.id}`,
          productId: 'coffee',
          productName: 'Café',
          quantity: Math.ceil(guestCount / 20),
          unit: 'paquetes',
          category: 'Bebidas calientes',
          isPurchased: false,
          notes: 'Café molido para cafetera'
        }
      );
    } else {
      // Lunch/dinner ingredients
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
    }

    return ingredients;
  }

  /**
   * Estimate cooking time based on task and budget
   */
  private static estimateCookingTime(task: Task, budget: Budget): string {
    const eventDate = new Date(budget.eventDate);
    const guestCount = budget.guestCount;

    // Estimate cooking start time based on guest count and meal complexity
    let hoursBeforeEvent = 2; // default 2 hours before event

    if (guestCount > 50) {
      hoursBeforeEvent = 4;
    } else if (guestCount > 20) {
      hoursBeforeEvent = 3;
    }

    const cookingStart = new Date(eventDate);
    cookingStart.setHours(cookingStart.getHours() - hoursBeforeEvent);

    return `${cookingStart.getHours().toString().padStart(2, '0')}:${cookingStart.getMinutes().toString().padStart(2, '0')}`;
  }

  /**
   * Determine meal type from task description
   */
  private static determineMealType(task: Task): 'breakfast' | 'lunch' | 'dinner' | 'snack' {
    const description = task.description.toLowerCase();
    
    if (description.includes('desayuno') || description.includes('breakfast')) {
      return 'breakfast';
    }
    if (description.includes('cena') || description.includes('dinner')) {
      return 'dinner';
    }
    if (description.includes('merienda') || description.includes('snack')) {
      return 'snack';
    }
    
    return 'lunch'; // default
  }

  /**
   * Update ingredients for a cooking schedule
   */
  static updateIngredients(
    schedules: CookingSchedule[],
    scheduleId: string,
    ingredients: ProductRequirement[]
  ): CookingSchedule[] {
    return schedules.map(schedule => {
      if (schedule.id === scheduleId) {
        return {
          ...schedule,
          ingredients
        };
      }
      return schedule;
    });
  }

  /**
   * Add or remove ingredient from a schedule
   */
  static updateIngredient(
    schedules: CookingSchedule[],
    scheduleId: string,
    ingredientId: string,
    updates: Partial<ProductRequirement>
  ): CookingSchedule[] {
    return schedules.map(schedule => {
      if (schedule.id === scheduleId) {
        return {
          ...schedule,
          ingredients: schedule.ingredients.map(ingredient => {
            if (ingredient.id === ingredientId) {
              return { ...ingredient, ...updates };
            }
            return ingredient;
          })
        };
      }
      return schedule;
    });
  }

  /**
   * Get cooking schedule for a specific day
   */
  static getScheduleForDay(schedules: CookingSchedule[], date: string): CookingSchedule[] {
    return schedules.filter(schedule => schedule.eventDate === date);
  }

  /**
   * Get next upcoming cooking tasks
   */
  static getUpcomingTasks(schedules: CookingSchedule[], limit = 5): CookingSchedule[] {
    const now = new Date();
    
    return schedules
      .filter(schedule => {
        const scheduleDateTime = new Date(`${schedule.eventDate}T${schedule.cookingTime}`);
        return scheduleDateTime > now;
      })
      .slice(0, limit);
  }
} 