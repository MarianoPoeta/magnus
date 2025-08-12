import { Task, ShoppingItem, ProductRequirement } from '../types/Task';
import { Budget } from '../store';

export class ShoppingListService {
  /**
   * Generate consolidated shopping list for a week period
   */
  static generateWeeklyShoppingList(
    budgets: Budget[],
    tasks: Task[],
    weekStart: Date,
    weekEnd: Date
  ): ShoppingItem[] {
    // Filter budgets for the week that are approved (reserva status)
    const weeklyBudgets = budgets.filter(budget => {
      const eventDate = new Date(budget.eventDate);
      return eventDate >= weekStart && 
             eventDate <= weekEnd && 
             budget.status === 'reserva';
    });

    // Get all product requirements from tasks for these budgets
    const allProductRequirements: ProductRequirement[] = [];
    
    weeklyBudgets.forEach(budget => {
      // Get shopping tasks for this budget
      const shoppingTasks = tasks.filter(
        task => task.relatedBudgetId === budget.id && 
               task.type === 'shopping' && 
               task.assignedToRole === 'logistics'
      );

      shoppingTasks.forEach(task => {
        if (task.productRequirements) {
          allProductRequirements.push(...task.productRequirements);
        }
      });

      // Generate basic product requirements if none exist
      if (shoppingTasks.length === 0 || !shoppingTasks.some(t => t.productRequirements?.length)) {
        const basicRequirements = this.generateBasicProductRequirements(budget);
        allProductRequirements.push(...basicRequirements);
      }
    });

    // Consolidate products by name and unit
    const consolidatedMap = new Map<string, ShoppingItem>();

    allProductRequirements.forEach(req => {
      const key = `${req.productName}-${req.unit}`;
      
      if (consolidatedMap.has(key)) {
        const existing = consolidatedMap.get(key)!;
        existing.totalQuantity += req.quantity;
        
        // Find which budget this requirement belongs to
        const relatedBudget = weeklyBudgets.find(b => 
          tasks.some(t => t.relatedBudgetId === b.id && 
                         t.productRequirements?.some(pr => pr.id === req.id))
        );
        
        if (relatedBudget && !existing.budgetIds.includes(relatedBudget.id)) {
          existing.budgetIds.push(relatedBudget.id);
          existing.clientNames.push(relatedBudget.clientName);
        }
      } else {
        // Find which budget this requirement belongs to
        const relatedBudget = weeklyBudgets.find(b => 
          tasks.some(t => t.relatedBudgetId === b.id && 
                         t.productRequirements?.some(pr => pr.id === req.id))
        ) || weeklyBudgets[0]; // fallback to first budget if not found

        consolidatedMap.set(key, {
          id: `shopping-item-${Date.now()}-${Math.random()}`,
          productName: req.productName,
          totalQuantity: req.quantity,
          unit: req.unit,
          category: req.category,
          budgetIds: relatedBudget ? [relatedBudget.id] : [],
          clientNames: relatedBudget ? [relatedBudget.clientName] : [],
          isPurchased: req.isPurchased,
          purchasedQuantity: req.isPurchased ? req.quantity : 0,
          notes: req.notes
        });
      }
    });

    // Convert to array and sort by category, then by name
    return Array.from(consolidatedMap.values()).sort((a, b) => {
      if (a.category !== b.category) {
        return a.category.localeCompare(b.category);
      }
      return a.productName.localeCompare(b.productName);
    });
  }

  /**
   * Generate basic product requirements for a budget
   */
  private static generateBasicProductRequirements(budget: Budget): ProductRequirement[] {
    const requirements: ProductRequirement[] = [];
    const guestCount = budget.guestCount;
    const now = new Date();

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

    if (guestCount > 15) {
      requirements.push({
        id: `req-napkins-${budget.id}`,
        productId: 'napkins',
        productName: 'Servilletas',
        quantity: Math.ceil(guestCount / 10),
        unit: 'paquetes',
        category: 'Utensilios',
        isPurchased: false,
        notes: `Paquetes de 100 unidades`
      });
    }

    return requirements;
  }

  /**
   * Update shopping item purchase status
   */
  static updateShoppingItemStatus(
    shoppingItems: ShoppingItem[],
    itemId: string,
    purchasedQuantity: number,
    purchasedBy: string
  ): ShoppingItem[] {
    return shoppingItems.map(item => {
      if (item.id === itemId) {
        return {
          ...item,
          purchasedQuantity,
          isPurchased: purchasedQuantity >= item.totalQuantity,
          notes: item.notes ? `${item.notes} | Comprado por: ${purchasedBy}` : `Comprado por: ${purchasedBy}`
        };
      }
      return item;
    });
  }

  /**
   * Get shopping list progress for a week
   */
  static getShoppingProgress(shoppingItems: ShoppingItem[]) {
    const total = shoppingItems.length;
    const purchased = shoppingItems.filter(item => item.isPurchased).length;
    const partiallyPurchased = shoppingItems.filter(
      item => item.purchasedQuantity > 0 && !item.isPurchased
    ).length;

    return {
      total,
      purchased,
      partiallyPurchased,
      pending: total - purchased - partiallyPurchased,
      completionPercentage: total > 0 ? Math.round((purchased / total) * 100) : 0
    };
  }

  /**
   * Group shopping items by category
   */
  static groupByCategory(shoppingItems: ShoppingItem[]): Record<string, ShoppingItem[]> {
    return shoppingItems.reduce((groups, item) => {
      if (!groups[item.category]) {
        groups[item.category] = [];
      }
      groups[item.category].push(item);
      return groups;
    }, {} as Record<string, ShoppingItem[]>);
  }
} 