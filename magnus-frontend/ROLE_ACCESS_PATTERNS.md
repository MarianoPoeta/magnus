# 🔐 Role-Based Access Patterns

## 🎯 Role Hierarchy & Permissions

The Party Budget Bliss system implements a sophisticated **role-based access control (RBAC)** system with four distinct user roles, each with specific permissions and workflow responsibilities.

## 👥 User Roles Definition

### **🔧 Admin**
**Primary Responsibilities:**
- System configuration and maintenance
- User management and role assignment
- Template creation and global settings
- Analytics and system-wide reporting
- Data backup and security management

**Access Level:** Full system access with override capabilities

### **💼 Sales Representative**
**Primary Responsibilities:**
- Budget creation and client management
- Proposal generation and pricing
- Client communication and relationship management
- Converting drafts to "reserva" status (triggers workflows)

**Access Level:** Read/Write on budgets, clients, templates (view-only)

### **📦 Logistics Coordinator**
**Primary Responsibilities:**
- Weekly planning and resource coordination
- Shopping list management and procurement
- Supplier relationship management
- Transportation and equipment logistics
- Inventory tracking and delivery coordination

**Access Level:** Read/Write on tasks, shopping lists, products, transport

### **👨‍🍳 Cook**
**Primary Responsibilities:**
- Menu planning and cooking schedules
- Ingredient requirement management
- Recipe modification and dietary accommodations
- Kitchen operations and food safety
- Real-time ingredient adjustments

**Access Level:** Read/Write on cooking schedules, ingredient modifications, menus

## 🛡️ Permission Matrix

| Resource | Admin | Sales | Logistics | Cook |
|----------|-------|-------|-----------|------|
| **Budgets** | CRUD | CRUD | Read | Read |
| **Clients** | CRUD | CRUD | Read | - |
| **Tasks** | CRUD | Read | CRUD | Read/Update* |
| **Shopping Lists** | CRUD | Read | CRUD | Read |
| **Cooking Schedules** | CRUD | Read | Read | CRUD |
| **Products** | CRUD | Read | CRUD | Read |
| **Menus** | CRUD | Create/Read | Read | CRUD |
| **Activities** | CRUD | Read | Read | - |
| **Users** | CRUD | - | - | - |
| **System Config** | CRUD | - | - | - |

*\* Cooks can only update tasks assigned to them*

## 🔄 Role-Based Component Rendering

### **Conditional Navigation**
```typescript
// Role-based sidebar navigation
const getNavigationItems = (userRole: UserRole): NavigationItem[] => {
  const baseItems = [
    { path: '/dashboard', label: 'Dashboard', icon: Home }
  ];
  
  const roleSpecificItems = {
    admin: [
      { path: '/users', label: 'User Management', icon: Users },
      { path: '/config', label: 'System Config', icon: Settings },
      { path: '/analytics', label: 'Analytics', icon: BarChart },
      { path: '/budgets', label: 'All Budgets', icon: DollarSign },
      { path: '/tasks', label: 'All Tasks', icon: CheckSquare }
    ],
    sales: [
      { path: '/budgets', label: 'My Budgets', icon: DollarSign },
      { path: '/clients', label: 'Clients', icon: Users },
      { path: '/templates', label: 'Templates', icon: FileTemplate },
      { path: '/sales-dashboard', label: 'Sales Analytics', icon: TrendingUp }
    ],
    logistics: [
      { path: '/tasks', label: 'My Tasks', icon: CheckSquare },
      { path: '/shopping', label: 'Shopping Lists', icon: ShoppingCart },
      { path: '/weekly-planning', label: 'Weekly Planning', icon: Calendar },
      { path: '/products', label: 'Products', icon: Package },
      { path: '/transport', label: 'Transport', icon: Truck }
    ],
    cook: [
      { path: '/tasks', label: 'Cooking Tasks', icon: ChefHat },
      { path: '/cooking-schedule', label: 'Cooking Schedule', icon: Clock },
      { path: '/menus', label: 'Menus', icon: BookOpen },
      { path: '/ingredients', label: 'Ingredients', icon: Apple }
    ]
  };
  
  return [...baseItems, ...roleSpecificItems[userRole]];
};
```

### **Component Access Control**
```typescript
// Higher-order component for role-based access
const withRoleAccess = <P extends object>(
  Component: React.ComponentType<P>,
  allowedRoles: UserRole[],
  fallbackComponent?: React.ComponentType
) => {
  return (props: P) => {
    const { currentUser } = useStore();
    
    if (!allowedRoles.includes(currentUser.role)) {
      if (fallbackComponent) {
        return React.createElement(fallbackComponent);
      }
      
      return (
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <Shield className="h-12 w-12 text-slate-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-slate-900">Access Denied</h3>
            <p className="text-slate-500">You don't have permission to view this content.</p>
          </div>
        </div>
      );
    }
    
    return <Component {...props} />;
  };
};

// Usage examples
const AdminOnlyConfig = withRoleAccess(ConfigurationPanel, ['admin']);
const LogisticsTaskView = withRoleAccess(TaskDashboard, ['admin', 'logistics']);
const CookMenuEditor = withRoleAccess(MenuEditor, ['admin', 'cook']);
```

### **Action-Level Permissions**
```typescript
// Permission checking utility
const usePermissions = () => {
  const { currentUser } = useStore();
  
  const canCreate = (resource: string): boolean => {
    const permissions = {
      budget: ['admin', 'sales'],
      task: ['admin', 'logistics'],
      menu: ['admin', 'cook'],
      client: ['admin', 'sales'],
      product: ['admin', 'logistics']
    };
    
    return permissions[resource]?.includes(currentUser.role) ?? false;
  };
  
  const canEdit = (resource: string, ownerId?: string): boolean => {
    // Admin can edit anything
    if (currentUser.role === 'admin') return true;
    
    // Users can edit their own creations
    if (ownerId && ownerId === currentUser.id) return true;
    
    // Role-specific edit permissions
    const editPermissions = {
      budget: ['sales'],
      task: ['logistics'], // + own tasks for cooks
      menu: ['cook'],
      client: ['sales'],
      product: ['logistics']
    };
    
    return editPermissions[resource]?.includes(currentUser.role) ?? false;
  };
  
  const canDelete = (resource: string, ownerId?: string): boolean => {
    // More restrictive than edit permissions
    if (currentUser.role === 'admin') return true;
    
    const deletePermissions = {
      budget: ['admin', 'sales'], // Sales can delete only drafts
      task: ['admin'], // Only admin can delete tasks
      menu: ['admin', 'cook'],
      client: ['admin'], // Only admin can delete clients
      product: ['admin', 'logistics']
    };
    
    return deletePermissions[resource]?.includes(currentUser.role) ?? false;
  };
  
  return { canCreate, canEdit, canDelete };
};

// Component usage
const ActionButtons = ({ resource, item }) => {
  const { canEdit, canDelete } = usePermissions();
  
  return (
    <div className="flex gap-2">
      {canEdit(resource, item.createdBy) && (
        <Button size="sm" onClick={() => handleEdit(item)}>
          <Edit className="h-4 w-4" />
        </Button>
      )}
      {canDelete(resource, item.createdBy) && (
        <Button size="sm" variant="destructive" onClick={() => handleDelete(item)}>
          <Trash className="h-4 w-4" />
        </Button>
      )}
    </div>
  );
};
```

## 🔄 Role-Based Data Filtering

### **Data Access Patterns**
```typescript
// Filter data based on user role and access rights
const useRoleFilteredData = () => {
  const { currentUser, budgets, tasks, clients } = useStore();
  
  const getFilteredBudgets = (): Budget[] => {
    switch (currentUser.role) {
      case 'admin':
        return budgets; // All budgets
      case 'sales':
        return budgets.filter(b => b.createdBy === currentUser.id);
      case 'logistics':
      case 'cook':
        return budgets.filter(b => b.status === 'reserva'); // Only approved budgets
      default:
        return [];
    }
  };
  
  const getFilteredTasks = (): Task[] => {
    switch (currentUser.role) {
      case 'admin':
        return tasks; // All tasks
      case 'sales':
        return tasks.filter(t => 
          budgets.some(b => b.id === t.relatedBudgetId && b.createdBy === currentUser.id)
        );
      case 'logistics':
        return tasks.filter(t => t.assignedToRole === 'logistics');
      case 'cook':
        return tasks.filter(t => t.assignedToRole === 'cook');
      default:
        return [];
    }
  };
  
  const getFilteredClients = (): Client[] => {
    switch (currentUser.role) {
      case 'admin':
      case 'sales':
        return clients;
      case 'logistics':
        // Only clients with active budgets
        return clients.filter(c => 
          budgets.some(b => b.clientId === c.id && b.status === 'reserva')
        );
      case 'cook':
        return []; // Cooks don't need client access
      default:
        return [];
    }
  };
  
  return {
    budgets: getFilteredBudgets(),
    tasks: getFilteredTasks(),
    clients: getFilteredClients()
  };
};
```

### **API Request Filtering**
```typescript
// Add role-based parameters to API requests
const useRoleBasedAPI = () => {
  const { currentUser } = useStore();
  
  const buildAPIParams = (baseParams: any = {}) => {
    const roleParams = {
      admin: {}, // No additional filters for admin
      sales: {
        createdBy: currentUser.id,
        includeOwnOnly: true
      },
      logistics: {
        assignedToRole: 'logistics',
        status: ['reserva', 'in_progress']
      },
      cook: {
        assignedToRole: 'cook',
        includeIngredients: true
      }
    };
    
    return {
      ...baseParams,
      ...roleParams[currentUser.role],
      userRole: currentUser.role
    };
  };
  
  const apiCall = async (endpoint: string, params: any = {}) => {
    const filteredParams = buildAPIParams(params);
    return api.request(endpoint, filteredParams);
  };
  
  return { apiCall, buildAPIParams };
};
```

## 🎯 Role-Specific Workflows

### **Sales Workflow**
```typescript
// Sales-specific budget creation workflow
const useSalesWorkflow = () => {
  const { canCreate, canEdit } = usePermissions();
  const { addBudget, addClient, generateTasksFromBudget } = useStore();
  
  const createBudgetWorkflow = async (budgetData: BudgetInput) => {
    if (!canCreate('budget')) {
      throw new Error('Insufficient permissions to create budget');
    }
    
    // 1. Create/update client
    const client = await handleClientCreation(budgetData.clientInfo);
    
    // 2. Create budget as draft
    const budget = await addBudget({
      ...budgetData,
      clientId: client.id,
      status: 'draft',
      createdBy: currentUser.id
    });
    
    // 3. Sales can immediately approve if they have authority
    if (budgetData.autoApprove && canApprove()) {
      await approveBudget(budget.id);
    }
    
    return budget;
  };
  
  const approveBudget = async (budgetId: string) => {
    const budget = await updateBudget(budgetId, { status: 'reserva' });
    
    // Trigger automatic task generation
    await generateTasksFromBudget(budgetId);
    
    // Notify logistics and cook teams
    await sendRoleNotifications(['logistics', 'cook'], {
      type: 'budget-approved',
      budgetId,
      message: `New event approved: ${budget.name}`
    });
    
    return budget;
  };
  
  return { createBudgetWorkflow, approveBudget };
};
```

### **Logistics Workflow**
```typescript
// Logistics-specific task and shopping management
const useLogisticsWorkflow = () => {
  const { currentUser } = useStore();
  
  const manageWeeklyPlanning = async (weekStart: string) => {
    // 1. Get all approved budgets for the week
    const weeklyBudgets = await getWeeklyBudgets(weekStart);
    
    // 2. Consolidate shopping requirements
    const consolidatedList = await consolidateShoppingLists(weekStart);
    
    // 3. Optimize supplier orders
    const optimizedOrders = await optimizeSupplierOrders(consolidatedList);
    
    // 4. Create delivery schedules
    const deliverySchedule = await createDeliverySchedules(optimizedOrders);
    
    // 5. Notify cooks about ingredient availability
    await notifyCooksAboutIngredients(consolidatedList);
    
    return {
      shoppingList: consolidatedList,
      supplierOrders: optimizedOrders,
      deliverySchedule
    };
  };
  
  const purchaseItems = async (itemIds: string[], quantities: number[]) => {
    // 1. Update shopping list items
    await bulkUpdateShoppingItems(itemIds, {
      isPurchased: true,
      purchasedBy: currentUser.id,
      purchasedAt: new Date().toISOString()
    });
    
    // 2. Update cooking task availability
    await updateCookingTaskAvailability(itemIds);
    
    // 3. Notify cooks about ingredient availability
    await notifyRoleAboutUpdates('cook', {
      type: 'ingredients-available',
      items: itemIds,
      message: 'New ingredients available for cooking'
    });
    
    return true;
  };
  
  return { manageWeeklyPlanning, purchaseItems };
};
```

### **Cook Workflow**
```typescript
// Cook-specific ingredient and schedule management
const useCookWorkflow = () => {
  const { updateCookingSchedule, syncCookingToShopping } = useStore();
  
  const modifyIngredients = async (scheduleId: string, modifications: IngredientUpdate[]) => {
    // 1. Update cooking schedule
    await updateCookingSchedule(scheduleId, { 
      ingredients: applyModifications(modifications) 
    });
    
    // 2. Recalculate shopping requirements
    await syncCookingToShopping(scheduleId);
    
    // 3. Notify logistics about changes
    await notifyLogisticsAboutChanges(scheduleId, modifications);
    
    // 4. Check for conflicts with existing purchases
    const conflicts = await checkPurchaseConflicts(modifications);
    if (conflicts.length > 0) {
      await handleIngredientConflicts(conflicts);
    }
    
    return true;
  };
  
  const planMealTiming = async (scheduleId: string, timingUpdates: TimingUpdate) => {
    // 1. Validate timing constraints
    const validation = await validateMealTiming(timingUpdates);
    if (!validation.isValid) {
      throw new Error(`Invalid timing: ${validation.errors.join(', ')}`);
    }
    
    // 2. Update cooking schedule
    await updateCookingSchedule(scheduleId, timingUpdates);
    
    // 3. Adjust related delivery schedules
    await adjustDeliveryTiming(scheduleId, timingUpdates);
    
    return true;
  };
  
  return { modifyIngredients, planMealTiming };
};
```

## 🔐 Security Implementation

### **Route Protection**
```typescript
// Protected route wrapper
const ProtectedRoute = ({ 
  children, 
  allowedRoles, 
  requireAuth = true 
}: ProtectedRouteProps) => {
  const { currentUser, isAuthenticated } = useAuth();
  
  if (requireAuth && !isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  if (allowedRoles && !allowedRoles.includes(currentUser?.role)) {
    return <Navigate to="/unauthorized" replace />;
  }
  
  return <>{children}</>;
};

// Route configuration with role requirements
const routes = [
  {
    path: '/budgets',
    element: <ProtectedRoute allowedRoles={['admin', 'sales']}><BudgetsPage /></ProtectedRoute>
  },
  {
    path: '/tasks',
    element: <ProtectedRoute allowedRoles={['admin', 'logistics', 'cook']}><TasksPage /></ProtectedRoute>
  },
  {
    path: '/admin',
    element: <ProtectedRoute allowedRoles={['admin']}><AdminPanel /></ProtectedRoute>
  }
];
```

### **API Security**
```typescript
// Role-based API middleware
const roleBasedApiMiddleware = (req: Request, res: Response, next: NextFunction) => {
  const userRole = req.user?.role;
  const resource = req.params.resource;
  const action = req.method.toLowerCase();
  
  const hasPermission = checkPermission(userRole, resource, action);
  
  if (!hasPermission) {
    return res.status(403).json({
      error: 'Insufficient permissions',
      required: getRequiredRoles(resource, action),
      current: userRole
    });
  }
  
  // Add role-based filters to query
  req.roleFilters = getRoleFilters(userRole, resource);
  next();
};
```

---

**Usage Tips:**
- Reference this notepad when implementing role-based features
- Use `@ROLE_ACCESS_PATTERNS.md` to understand permission patterns
- Always check permissions before rendering components or actions
- Implement both client-side and server-side permission checks
- Use higher-order components for consistent access control
- Filter data appropriately based on user roles 