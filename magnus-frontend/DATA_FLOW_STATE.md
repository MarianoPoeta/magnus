# 🌊 Data Flow & State Management

## 🎯 State Architecture Overview

The Party Budget Bliss system uses **Zustand** for global state management with a carefully designed data flow that supports real-time synchronization, optimistic updates, and role-based access patterns.

## 🏪 Zustand Store Structure

### **Global Store Organization**
```typescript
interface StoreState {
  // Core Entities
  clients: Client[];
  budgets: Budget[];
  tasks: Task[];
  products: Product[];
  menus: Menu[];
  activities: Activity[];
  accommodations: Accommodation[];
  
  // Workflow Data
  shoppingItems: ShoppingItem[];
  cookingSchedules: CookingSchedule[];
  transportAssignments: TransportAssignment[];
  
  // User Context
  currentUser: User;
  notifications: Notification[];
  toasts: Toast[];
  
  // Actions (CRUD + Business Logic)
  addBudget: (budget: Budget) => void;
  updateBudget: (budget: Budget) => void;
  deleteBudget: (budgetId: string) => void;
  
  // Workflow Actions
  generateTasksFromBudget: (budgetId: string) => void;
  syncCookingToShopping: (scheduleId: string) => void;
  consolidateShoppingLists: (weekStart: string) => void;
}
```

### **Store Persistence Strategy**
```typescript
// Selective persistence with zustand/middleware/persist
const persistConfig = {
  name: 'party-budget-store',
  partialize: (state) => ({
    // Persist core data
    clients: state.clients,
    budgets: state.budgets,
    products: state.products,
    currentUser: state.currentUser,
    
    // Don't persist real-time data
    // tasks, shoppingItems, notifications will reload
  }),
  version: 1,
  migrate: handleStoreMigration
};
```

## 🔄 Data Flow Patterns

### **Budget Creation Flow**
```
User Input → Form Validation → Zustand Store → Local Storage → Background Sync
     ↓
Client Creation (if new) → Budget Save → Template Association → Status Update
     ↓
[IF status = 'reserva'] → Workflow Trigger → Task Generation → Role Assignment
```

**Implementation:**
```typescript
const createBudget = async (budgetData: BudgetInput) => {
  // 1. Validate and create client if needed
  const client = findOrCreateClient(budgetData.clientInfo);
  
  // 2. Save budget with optimistic update
  const budget = { ...budgetData, clientId: client.id, id: generateId() };
  addBudget(budget); // Immediate UI update
  
  // 3. Background sync to server
  try {
    await api.createBudget(budget);
    addToast({ type: 'success', message: 'Budget created successfully' });
  } catch (error) {
    // Rollback on failure
    deleteBudget(budget.id);
    addToast({ type: 'error', message: 'Failed to create budget' });
  }
  
  // 4. Trigger workflow if approved
  if (budget.status === 'reserva') {
    generateTasksFromBudget(budget.id);
  }
};
```

### **Real-Time Task Synchronization**
```
Cook Modifies Ingredients → Update Cooking Schedule → Trigger Shopping List Update
     ↓
Recalculate Quantities → Update Shopping Items → Notify Logistics Role
     ↓
WebSocket Broadcast → UI Updates → Conflict Resolution (if needed)
```

**Implementation:**
```typescript
const updateCookingIngredient = (scheduleId: string, ingredientId: string, updates: Partial<Ingredient>) => {
  // 1. Optimistic update to cooking schedule
  setCookingSchedules(schedules => 
    schedules.map(schedule => 
      schedule.id === scheduleId 
        ? { ...schedule, ingredients: updateIngredient(schedule.ingredients, ingredientId, updates) }
        : schedule
    )
  );
  
  // 2. Recalculate shopping requirements
  const affectedShoppingItems = recalculateShoppingFromCooking(scheduleId);
  
  // 3. Update shopping lists
  setShoppingItems(items => 
    items.map(item => {
      const updated = affectedShoppingItems.find(ai => ai.id === item.id);
      return updated ? { ...item, ...updated } : item;
    })
  );
  
  // 4. Notify affected users
  addNotification({
    type: 'info',
    message: 'Ingredient requirements updated',
    targetRole: 'logistics'
  });
  
  // 5. WebSocket broadcast (when implemented)
  websocket.broadcast('ingredient-updated', { scheduleId, ingredientId, updates });
};
```

### **Shopping List Consolidation Flow**
```
Multiple Budgets → Extract Requirements → Group by Product → Calculate Totals
     ↓
Apply Business Rules → Optimize Quantities → Generate Consolidated List
     ↓
Track Purchase Progress → Update Individual Budgets → Real-time Sync
```

**Implementation:**
```typescript
const consolidateShoppingLists = (weekStart: string, weekEnd: string) => {
  // 1. Get all budgets for the week
  const weekBudgets = budgets.filter(b => 
    isDateInRange(b.eventDate, weekStart, weekEnd) && b.status === 'reserva'
  );
  
  // 2. Extract all product requirements
  const allRequirements = weekBudgets.flatMap(budget => 
    extractProductRequirements(budget)
  );
  
  // 3. Group and consolidate by product
  const consolidatedItems = allRequirements.reduce((acc, req) => {
    const key = `${req.productId}-${req.unit}`;
    if (acc[key]) {
      acc[key].totalQuantity += req.quantity;
      acc[key].budgetIds.push(req.budgetId);
    } else {
      acc[key] = {
        id: generateId(),
        productId: req.productId,
        productName: req.productName,
        totalQuantity: req.quantity,
        unit: req.unit,
        category: req.category,
        budgetIds: [req.budgetId],
        weekStart,
        weekEnd,
        isPurchased: false
      };
    }
    return acc;
  }, {} as Record<string, ShoppingItem>);
  
  // 4. Update store
  setShoppingItems(Object.values(consolidatedItems));
};
```

## 🔄 State Synchronization Patterns

### **Optimistic Updates**
```typescript
// Pattern for immediate UI feedback with rollback capability
const optimisticUpdate = async <T>(
  optimisticAction: () => void,
  serverAction: () => Promise<T>,
  rollbackAction: () => void
) => {
  // 1. Immediate UI update
  optimisticAction();
  
  try {
    // 2. Server sync
    const result = await serverAction();
    return result;
  } catch (error) {
    // 3. Rollback on failure
    rollbackAction();
    throw error;
  }
};

// Usage example
const markItemPurchased = (itemId: string, quantity: number) => {
  const originalItem = shoppingItems.find(item => item.id === itemId);
  
  optimisticUpdate(
    // Optimistic update
    () => updateShoppingItem(itemId, { isPurchased: true, purchasedQuantity: quantity }),
    // Server action
    () => api.updateShoppingItem(itemId, { isPurchased: true, purchasedQuantity: quantity }),
    // Rollback
    () => updateShoppingItem(itemId, originalItem)
  );
};
```

### **Conflict Resolution**
```typescript
// Handle simultaneous updates from different users
const resolveConflicts = (localVersion: any, serverVersion: any, conflictStrategy: 'merge' | 'server-wins' | 'user-choice') => {
  switch (conflictStrategy) {
    case 'merge':
      return mergeVersions(localVersion, serverVersion);
    case 'server-wins':
      return serverVersion;
    case 'user-choice':
      return showConflictDialog(localVersion, serverVersion);
  }
};

// Example for ingredient updates
const handleIngredientConflict = (localIngredient: Ingredient, serverIngredient: Ingredient) => {
  if (localIngredient.quantity !== serverIngredient.quantity) {
    // Show user choice dialog
    return showConflictDialog({
      title: 'Quantity Conflict',
      message: `Ingredient quantity was updated by another user`,
      choices: [
        { label: `Keep your change (${localIngredient.quantity})`, value: localIngredient },
        { label: `Use other change (${serverIngredient.quantity})`, value: serverIngredient }
      ]
    });
  }
  return serverIngredient;
};
```

### **Real-Time Event Handling**
```typescript
// WebSocket integration pattern (planned implementation)
const websocketHandlers = {
  'task-status-changed': (event: TaskStatusEvent) => {
    updateTask(event.taskId, { status: event.newStatus });
    
    if (event.userId !== currentUser.id) {
      addNotification({
        type: 'info',
        message: `Task "${event.taskTitle}" updated by ${event.userName}`,
        timestamp: Date.now()
      });
    }
  },
  
  'shopping-item-purchased': (event: ShoppingPurchaseEvent) => {
    updateShoppingItem(event.itemId, {
      isPurchased: true,
      purchasedQuantity: event.quantity,
      purchasedBy: event.userId
    });
    
    // Update related cooking schedules
    updateCookingAvailability(event.itemId, true);
  },
  
  'cooking-ingredient-modified': (event: IngredientUpdateEvent) => {
    if (event.userId !== currentUser.id) {
      // Handle conflict resolution
      const currentIngredient = getCurrentIngredient(event.scheduleId, event.ingredientId);
      const resolvedIngredient = resolveConflicts(
        currentIngredient, 
        event.updatedIngredient, 
        'user-choice'
      );
      
      updateCookingIngredient(event.scheduleId, event.ingredientId, resolvedIngredient);
    }
  }
};
```

## 📊 Data Validation & Consistency

### **Input Validation**
```typescript
// Multi-layer validation strategy
const validateBudget = (budget: Budget): ValidationResult => {
  const errors: ValidationError[] = [];
  
  // Required field validation
  if (!budget.clientName) errors.push({ field: 'clientName', message: 'Client name is required' });
  if (!budget.eventDate) errors.push({ field: 'eventDate', message: 'Event date is required' });
  if (budget.guestCount < 1) errors.push({ field: 'guestCount', message: 'Guest count must be positive' });
  
  // Business rule validation
  if (new Date(budget.eventDate) < new Date()) {
    errors.push({ field: 'eventDate', message: 'Event date cannot be in the past' });
  }
  
  if (budget.totalAmount < 0) {
    errors.push({ field: 'totalAmount', message: 'Total amount cannot be negative' });
  }
  
  // Cross-field validation
  const calculatedTotal = calculateBudgetTotal(budget);
  if (Math.abs(budget.totalAmount - calculatedTotal) > 0.01) {
    errors.push({ field: 'totalAmount', message: 'Total amount does not match calculated sum' });
  }
  
  return { isValid: errors.length === 0, errors };
};
```

### **Data Consistency Checks**
```typescript
// Periodic consistency validation
const validateStoreConsistency = () => {
  const issues: ConsistencyIssue[] = [];
  
  // Check budget-task relationships
  budgets.forEach(budget => {
    if (budget.status === 'reserva') {
      const relatedTasks = tasks.filter(task => task.relatedBudgetId === budget.id);
      if (relatedTasks.length === 0) {
        issues.push({
          type: 'missing-tasks',
          budgetId: budget.id,
          message: 'Approved budget has no associated tasks'
        });
      }
    }
  });
  
  // Check shopping-cooking consistency
  cookingSchedules.forEach(schedule => {
    schedule.ingredients.forEach(ingredient => {
      const shoppingItem = shoppingItems.find(item => 
        item.productId === ingredient.productId && 
        isDateInRange(schedule.eventDate, item.weekStart, item.weekEnd)
      );
      
      if (!shoppingItem) {
        issues.push({
          type: 'missing-shopping-item',
          scheduleId: schedule.id,
          ingredientId: ingredient.id,
          message: 'Ingredient not found in shopping list'
        });
      }
    });
  });
  
  return issues;
};
```

## 🎯 Performance Optimization

### **Selective Updates**
```typescript
// Update only affected components using granular selectors
const useBudgetsList = () => useStore(state => state.budgets, shallow);
const useBudgetById = (id: string) => useStore(state => 
  state.budgets.find(b => b.id === id)
);
const useTasksByRole = (role: UserRole) => useStore(state => 
  state.tasks.filter(t => t.assignedToRole === role)
);

// Memoized computations
const useShoppingProgress = () => useStore(state => {
  const items = state.shoppingItems;
  return useMemo(() => ({
    total: items.length,
    purchased: items.filter(item => item.isPurchased).length,
    percentage: items.length > 0 ? (items.filter(item => item.isPurchased).length / items.length) * 100 : 0
  }), [items]);
});
```

### **Batch Updates**
```typescript
// Group multiple updates to reduce re-renders
const batchUpdate = (updates: (() => void)[]) => {
  startTransition(() => {
    updates.forEach(update => update());
  });
};

// Example: Bulk task status update
const bulkUpdateTaskStatus = (taskIds: string[], newStatus: TaskStatus) => {
  batchUpdate([
    () => setTasks(tasks => 
      tasks.map(task => 
        taskIds.includes(task.id) ? { ...task, status: newStatus } : task
      )
    ),
    () => addNotification({
      type: 'success',
      message: `${taskIds.length} tasks updated to ${newStatus}`
    })
  ]);
};
```

---

**Usage Tips:**
- Reference this notepad when implementing data flows
- Use `@DATA_FLOW_STATE.md` to understand state patterns
- Follow optimistic update patterns for better UX
- Implement proper conflict resolution for real-time features
- Always validate data at multiple layers for consistency 