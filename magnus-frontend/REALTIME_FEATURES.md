# ⚡ Real-Time Features & Synchronization

## 🎯 Real-Time Architecture Overview

The Party Budget Bliss system implements sophisticated **real-time synchronization** to enable seamless collaboration between different user roles, ensuring instant updates across cook-logistics workflows, task management, and shopping coordination.

## 🔌 WebSocket Integration Strategy

### **Connection Management**
```typescript
// WebSocket service for real-time communication
class WebSocketService {
  private ws: WebSocket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectInterval = 1000;
  private heartbeatInterval = 30000;
  private eventHandlers: Map<string, Function[]> = new Map();
  
  connect(userId: string, userRole: string) {
    const wsUrl = `${import.meta.env.VITE_WS_URL}?userId=${userId}&role=${userRole}`;
    
    this.ws = new WebSocket(wsUrl);
    
    this.ws.onopen = () => {
      console.log('WebSocket connected');
      this.reconnectAttempts = 0;
      this.startHeartbeat();
      this.emit('connected');
    };
    
    this.ws.onmessage = (event) => {
      try {
        const message = JSON.parse(event.data);
        this.handleMessage(message);
      } catch (error) {
        console.error('Invalid WebSocket message:', error);
      }
    };
    
    this.ws.onclose = () => {
      console.log('WebSocket disconnected');
      this.attemptReconnect();
    };
    
    this.ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  }
  
  private attemptReconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      setTimeout(() => {
        console.log(`Reconnect attempt ${this.reconnectAttempts}`);
        this.connect();
      }, this.reconnectInterval * this.reconnectAttempts);
    }
  }
  
  private startHeartbeat() {
    setInterval(() => {
      if (this.ws?.readyState === WebSocket.OPEN) {
        this.send('heartbeat', { timestamp: Date.now() });
      }
    }, this.heartbeatInterval);
  }
  
  send(type: string, data: any) {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify({ type, data, timestamp: Date.now() }));
    }
  }
  
  on(eventType: string, handler: Function) {
    if (!this.eventHandlers.has(eventType)) {
      this.eventHandlers.set(eventType, []);
    }
    this.eventHandlers.get(eventType)!.push(handler);
  }
  
  private handleMessage(message: any) {
    const handlers = this.eventHandlers.get(message.type) || [];
    handlers.forEach(handler => handler(message.data));
  }
}
```

### **Event Types & Data Structures**
```typescript
// Real-time event definitions
interface WebSocketEvent {
  type: string;
  data: any;
  timestamp: number;
  userId: string;
  userRole: string;
}

// Specific event types
interface TaskStatusChangedEvent extends WebSocketEvent {
  type: 'task-status-changed';
  data: {
    taskId: string;
    oldStatus: TaskStatus;
    newStatus: TaskStatus;
    updatedBy: string;
    budgetId: string;
  };
}

interface IngredientUpdatedEvent extends WebSocketEvent {
  type: 'ingredient-updated';
  data: {
    scheduleId: string;
    ingredientId: string;
    oldQuantity: number;
    newQuantity: number;
    updatedBy: string;
    affectedShoppingItems: string[];
  };
}

interface ShoppingItemPurchasedEvent extends WebSocketEvent {
  type: 'shopping-item-purchased';
  data: {
    itemId: string;
    quantity: number;
    purchasedBy: string;
    affectedSchedules: string[];
    budgetIds: string[];
  };
}

interface BudgetStatusChangedEvent extends WebSocketEvent {
  type: 'budget-status-changed';
  data: {
    budgetId: string;
    oldStatus: BudgetStatus;
    newStatus: BudgetStatus;
    triggeredTasks: string[];
  };
}
```

## 🔄 Core Synchronization Patterns

### **Cook ↔ Logistics Synchronization**
```typescript
// Real-time ingredient modification sync
const useCookLogisticsSync = () => {
  const { updateShoppingItems, updateCookingSchedules, addNotification } = useStore();
  const websocket = useWebSocket();
  
  // Handle cook ingredient updates
  const handleIngredientUpdate = useCallback((scheduleId: string, ingredientId: string, updates: Partial<Ingredient>) => {
    // 1. Optimistic update to local cooking schedule
    updateCookingSchedules(schedules => 
      schedules.map(schedule => 
        schedule.id === scheduleId 
          ? updateIngredientInSchedule(schedule, ingredientId, updates)
          : schedule
      )
    );
    
    // 2. Calculate affected shopping items
    const affectedItems = calculateAffectedShoppingItems(scheduleId, ingredientId, updates);
    
    // 3. Update local shopping items
    updateShoppingItems(items => 
      items.map(item => {
        const affected = affectedItems.find(ai => ai.id === item.id);
        return affected ? { ...item, ...affected.updates } : item;
      })
    );
    
    // 4. Broadcast to other users
    websocket.send('ingredient-updated', {
      scheduleId,
      ingredientId,
      updates,
      affectedShoppingItems: affectedItems.map(ai => ai.id),
      timestamp: Date.now()
    });
    
    // 5. Notify logistics users
    addNotification({
      type: 'info',
      targetRole: 'logistics',
      message: `Ingredient requirements updated for ${getScheduleName(scheduleId)}`,
      data: { scheduleId, ingredientId }
    });
  }, [updateCookingSchedules, updateShoppingItems, websocket, addNotification]);
  
  // Handle incoming ingredient updates from other cooks
  websocket.on('ingredient-updated', useCallback((eventData: any) => {
    const { scheduleId, ingredientId, updates, affectedShoppingItems, userId } = eventData;
    
    // Skip if this is our own update
    if (userId === getCurrentUserId()) return;
    
    // Apply remote changes
    updateCookingSchedules(schedules => 
      schedules.map(schedule => 
        schedule.id === scheduleId 
          ? updateIngredientInSchedule(schedule, ingredientId, updates)
          : schedule
      )
    );
    
    // Update affected shopping items
    updateShoppingItems(items => 
      items.map(item => 
        affectedShoppingItems.includes(item.id) 
          ? recalculateShoppingItem(item, scheduleId)
          : item
      )
    );
    
    // Show notification
    addNotification({
      type: 'info',
      message: `Ingredient updated by ${getUserName(userId)}`,
      data: { scheduleId, ingredientId }
    });
  }, [updateCookingSchedules, updateShoppingItems, addNotification]));
  
  return { handleIngredientUpdate };
};
```

### **Task Progress Synchronization**
```typescript
// Real-time task status updates
const useTaskSync = () => {
  const { updateTask, addNotification } = useStore();
  const websocket = useWebSocket();
  
  const updateTaskStatus = useCallback((taskId: string, newStatus: TaskStatus, notes?: string) => {
    const task = getTaskById(taskId);
    
    // 1. Optimistic local update
    updateTask(taskId, { 
      status: newStatus, 
      updatedAt: new Date().toISOString(),
      ...(notes && { notes })
    });
    
    // 2. Broadcast update
    websocket.send('task-status-changed', {
      taskId,
      oldStatus: task.status,
      newStatus,
      notes,
      budgetId: task.relatedBudgetId,
      timestamp: Date.now()
    });
    
    // 3. Handle dependent tasks
    if (newStatus === 'done') {
      const dependentTasks = getDependentTasks(taskId);
      dependentTasks.forEach(depTask => {
        if (canStartTask(depTask)) {
          updateTask(depTask.id, { status: 'todo' });
          websocket.send('task-enabled', {
            taskId: depTask.id,
            enabledBy: taskId
          });
        }
      });
    }
    
    // 4. Notify relevant roles
    const targetRoles = getInterestedRoles(task);
    targetRoles.forEach(role => {
      addNotification({
        type: 'success',
        targetRole: role,
        message: `Task "${task.title}" marked as ${newStatus}`,
        data: { taskId, budgetId: task.relatedBudgetId }
      });
    });
  }, [updateTask, websocket, addNotification]);
  
  // Handle incoming task updates
  websocket.on('task-status-changed', useCallback((eventData: any) => {
    const { taskId, newStatus, notes, userId } = eventData;
    
    if (userId === getCurrentUserId()) return;
    
    updateTask(taskId, { 
      status: newStatus, 
      updatedAt: new Date().toISOString(),
      ...(notes && { notes })
    });
    
    // Show real-time notification
    const task = getTaskById(taskId);
    addNotification({
      type: 'info',
      message: `"${task.title}" updated by ${getUserName(userId)}`,
      data: { taskId }
    });
  }, [updateTask, addNotification]));
  
  return { updateTaskStatus };
};
```

### **Shopping Progress Broadcasting**
```typescript
// Real-time shopping purchase updates
const useShoppingSync = () => {
  const { updateShoppingItem, updateCookingAvailability, addNotification } = useStore();
  const websocket = useWebSocket();
  
  const markItemPurchased = useCallback((itemId: string, quantity: number, notes?: string) => {
    const item = getShoppingItemById(itemId);
    
    // 1. Optimistic update
    updateShoppingItem(itemId, {
      isPurchased: true,
      purchasedQuantity: quantity,
      purchasedAt: new Date().toISOString(),
      purchasedBy: getCurrentUserId(),
      ...(notes && { purchaseNotes: notes })
    });
    
    // 2. Update related cooking schedules
    const affectedSchedules = getCookingSchedulesForProduct(item.productId);
    affectedSchedules.forEach(schedule => {
      updateCookingAvailability(schedule.id, item.productId, true);
    });
    
    // 3. Broadcast purchase
    websocket.send('shopping-item-purchased', {
      itemId,
      quantity,
      notes,
      affectedSchedules: affectedSchedules.map(s => s.id),
      budgetIds: item.budgetIds,
      timestamp: Date.now()
    });
    
    // 4. Notify cooks about ingredient availability
    addNotification({
      type: 'success',
      targetRole: 'cook',
      message: `${item.productName} is now available (${quantity} ${item.unit})`,
      data: { itemId, productId: item.productId }
    });
    
    // 5. Check if all items for a budget are purchased
    checkBudgetCompleteness(item.budgetIds);
  }, [updateShoppingItem, updateCookingAvailability, websocket, addNotification]);
  
  // Handle bulk purchase operations
  const bulkPurchaseItems = useCallback((itemIds: string[], quantities: number[]) => {
    const purchaseData = itemIds.map((itemId, index) => ({
      itemId,
      quantity: quantities[index],
      timestamp: Date.now()
    }));
    
    // 1. Batch update locally
    batchUpdateShoppingItems(purchaseData);
    
    // 2. Broadcast bulk update
    websocket.send('bulk-shopping-purchase', {
      purchases: purchaseData,
      purchasedBy: getCurrentUserId()
    });
    
    // 3. Update cooking availability for all affected items
    const affectedProducts = getProductsFromItems(itemIds);
    batchUpdateCookingAvailability(affectedProducts);
    
    // 4. Send consolidated notification
    addNotification({
      type: 'success',
      targetRole: 'cook',
      message: `${itemIds.length} ingredients now available`,
      data: { itemIds, productIds: affectedProducts }
    });
  }, [batchUpdateShoppingItems, websocket, addNotification]);
  
  return { markItemPurchased, bulkPurchaseItems };
};
```

## 🎯 Conflict Resolution Strategies

### **Optimistic Concurrency Control**
```typescript
// Handle concurrent updates with conflict resolution
const useConflictResolution = () => {
  const [conflicts, setConflicts] = useState<ConflictInfo[]>([]);
  
  const handleConflict = useCallback(async (
    localData: any, 
    remoteData: any, 
    conflictType: ConflictType
  ): Promise<any> => {
    // Auto-resolve simple conflicts
    if (canAutoResolve(conflictType, localData, remoteData)) {
      return autoResolveConflict(localData, remoteData, conflictType);
    }
    
    // Show conflict resolution dialog for complex conflicts
    const resolution = await showConflictDialog({
      type: conflictType,
      local: localData,
      remote: remoteData,
      timestamp: Date.now()
    });
    
    return resolution.resolvedData;
  }, []);
  
  const autoResolveConflict = (local: any, remote: any, type: ConflictType) => {
    switch (type) {
      case 'ingredient-quantity':
        // Use the higher quantity for safety
        return { ...local, quantity: Math.max(local.quantity, remote.quantity) };
      
      case 'task-status':
        // Prioritize 'done' status over others
        const statusPriority = { 'done': 3, 'in_progress': 2, 'todo': 1, 'blocked': 0 };
        const localPriority = statusPriority[local.status] || 0;
        const remotePriority = statusPriority[remote.status] || 0;
        return localPriority >= remotePriority ? local : remote;
      
      case 'shopping-purchase':
        // If either side shows purchased, consider it purchased
        return {
          ...local,
          isPurchased: local.isPurchased || remote.isPurchased,
          purchasedQuantity: Math.max(local.purchasedQuantity || 0, remote.purchasedQuantity || 0)
        };
      
      default:
        // Default to remote (server wins)
        return remote;
    }
  };
  
  return { handleConflict, conflicts };
};
```

### **Real-Time Conflict Indicators**
```typescript
// Visual conflict indicators in UI
const ConflictIndicator = ({ conflictType, onResolve }: ConflictIndicatorProps) => {
  const [isResolving, setIsResolving] = useState(false);
  
  const handleResolve = async () => {
    setIsResolving(true);
    try {
      await onResolve();
    } finally {
      setIsResolving(false);
    }
  };
  
  return (
    <div className="flex items-center gap-2 p-2 bg-yellow-50 border border-yellow-200 rounded-md">
      <AlertTriangle className="h-4 w-4 text-yellow-600" />
      <span className="text-sm text-yellow-800">
        Conflict detected: {getConflictMessage(conflictType)}
      </span>
      <Button 
        size="sm" 
        variant="outline" 
        onClick={handleResolve}
        disabled={isResolving}
      >
        {isResolving ? 'Resolving...' : 'Resolve'}
      </Button>
    </div>
  );
};

// Usage in components
const IngredientEditor = ({ ingredient, scheduleId }) => {
  const [localQuantity, setLocalQuantity] = useState(ingredient.quantity);
  const [hasConflict, setHasConflict] = useState(false);
  const { handleIngredientUpdate } = useCookLogisticsSync();
  
  useEffect(() => {
    // Check for conflicts when remote data changes
    if (ingredient.quantity !== localQuantity && ingredient.updatedAt > lastLocalUpdate) {
      setHasConflict(true);
    }
  }, [ingredient.quantity, localQuantity]);
  
  return (
    <div className="space-y-2">
      {hasConflict && (
        <ConflictIndicator 
          conflictType="ingredient-quantity"
          onResolve={() => resolveIngredientConflict(ingredient, localQuantity)}
        />
      )}
      <Input
        value={localQuantity}
        onChange={(e) => setLocalQuantity(Number(e.target.value))}
        onBlur={() => handleIngredientUpdate(scheduleId, ingredient.id, { quantity: localQuantity })}
      />
    </div>
  );
};
```

## 📊 Real-Time Performance Optimization

### **Connection Pooling & Message Batching**
```typescript
// Optimize real-time performance with batching
class BatchedWebSocketService extends WebSocketService {
  private messageQueue: any[] = [];
  private batchTimeout: NodeJS.Timeout | null = null;
  private batchSize = 10;
  private batchDelay = 100; // ms
  
  sendBatched(type: string, data: any) {
    this.messageQueue.push({ type, data, timestamp: Date.now() });
    
    if (this.messageQueue.length >= this.batchSize) {
      this.flushQueue();
    } else if (!this.batchTimeout) {
      this.batchTimeout = setTimeout(() => this.flushQueue(), this.batchDelay);
    }
  }
  
  private flushQueue() {
    if (this.messageQueue.length > 0) {
      this.send('batch', { messages: this.messageQueue });
      this.messageQueue = [];
    }
    
    if (this.batchTimeout) {
      clearTimeout(this.batchTimeout);
      this.batchTimeout = null;
    }
  }
  
  // Handle batched messages from server
  private handleBatchMessage(batchData: any) {
    batchData.messages.forEach((message: any) => {
      this.handleMessage(message);
    });
  }
}
```

### **Selective Update Subscriptions**
```typescript
// Subscribe only to relevant updates based on user role and current view
const useSelectiveSubscriptions = () => {
  const { currentUser } = useStore();
  const location = useLocation();
  const websocket = useWebSocket();
  
  useEffect(() => {
    const subscriptions = getRelevantSubscriptions(currentUser.role, location.pathname);
    
    // Subscribe to role-specific events
    subscriptions.forEach(sub => {
      websocket.send('subscribe', {
        eventType: sub.eventType,
        filters: sub.filters,
        userId: currentUser.id
      });
    });
    
    return () => {
      // Unsubscribe when component unmounts or route changes
      subscriptions.forEach(sub => {
        websocket.send('unsubscribe', {
          eventType: sub.eventType,
          userId: currentUser.id
        });
      });
    };
  }, [currentUser.role, location.pathname]);
};

// Example subscription configuration
const getRelevantSubscriptions = (role: UserRole, pathname: string) => {
  const baseSubscriptions = [
    { eventType: 'notification', filters: { targetRole: role } }
  ];
  
  const routeSubscriptions = {
    '/tasks': [
      { eventType: 'task-status-changed', filters: { assignedToRole: role } },
      { eventType: 'task-created', filters: { assignedToRole: role } }
    ],
    '/shopping': [
      { eventType: 'shopping-item-purchased', filters: {} },
      { eventType: 'ingredient-updated', filters: {} }
    ],
    '/cooking-schedule': [
      { eventType: 'ingredient-updated', filters: { role: 'cook' } },
      { eventType: 'shopping-item-purchased', filters: {} }
    ]
  };
  
  return [...baseSubscriptions, ...(routeSubscriptions[pathname] || [])];
};
```

---

**Usage Tips:**
- Reference this notepad when implementing real-time features
- Use `@REALTIME_FEATURES.md` to understand synchronization patterns
- Implement proper conflict resolution for concurrent updates
- Use selective subscriptions to optimize performance
- Always handle connection failures gracefully
- Batch updates when possible to reduce WebSocket traffic
- Implement optimistic updates for better user experience 