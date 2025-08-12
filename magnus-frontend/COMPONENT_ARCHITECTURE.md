# 🧩 Component Architecture Guide

## 🎨 Design System Foundation

### **UI Framework Stack**
- **Base**: shadcn/ui components (Radix UI primitives)
- **Styling**: Tailwind CSS with custom design tokens
- **Icons**: Lucide React for consistent iconography
- **Animations**: CSS transitions with Tailwind utilities
- **Typography**: System font stack with defined hierarchy

### **Color Palette & Conventions**
```typescript
// Status Colors
const STATUS_COLORS = {
  success: 'green',    // Completed tasks, approved budgets
  warning: 'orange',   // Pending actions, draft status
  danger: 'red',       // Errors, cancelled items
  info: 'blue',        // General information, active states
  neutral: 'slate'     // Default states, disabled items
};

// Role Colors
const ROLE_COLORS = {
  admin: 'purple',
  sales: 'blue', 
  logistics: 'green',
  cook: 'orange'
};
```

## 🏗️ Component Hierarchy

### **Layout Components**
- `Layout.tsx` - Main application wrapper with sidebar
- `AppSidebar.tsx` - Role-based navigation with user context
- `BreadcrumbNav.tsx` - Contextual navigation breadcrumbs
- `ErrorBoundary.tsx` - Global error handling with recovery

### **Feature Components Structure**
```
src/components/
├── budget/              # Budget creation & management
│   ├── UnifiedBudgetCreator.tsx    # Main wizard component
│   ├── BudgetCreationWizard.tsx    # Step-based creation
│   ├── BasicInfoSection.tsx       # Client & event details
│   ├── MealsTab.tsx               # Menu selection
│   ├── ActivitiesTab.tsx          # Activity selection
│   ├── StayTab.tsx                # Accommodation selection
│   └── TransportTab.tsx           # Transport selection
├── ui/                  # Reusable UI primitives
└── [feature]/           # Feature-specific components
```

## 🎯 Component Patterns

### **Wizard Pattern** (`UnifiedBudgetCreator.tsx`)
```typescript
// Multi-step process with progress tracking
const WIZARD_STEPS = ['basic', 'templates', 'review'];

// State management for complex forms
const [step, setStep] = useState<'basic' | 'templates' | 'review'>('basic');
const [customization, setCustomization] = useState<CustomizationMode>({
  isCustomizing: false,
  activeTemplate: null,
  templateType: null
});

// Progress calculation
const progress = useMemo(() => {
  // Calculate completion based on filled fields and validation
}, [budget, templateGroups, validateBudget]);
```

**Usage Pattern:**
- Multi-step forms with validation
- Progress indication with step navigation
- State persistence across steps
- Conditional step access based on completion

### **Template Selection Pattern**
```typescript
// Expandable sections with selection tracking
interface TemplateGroup {
  id: string;
  title: string;
  icon: React.ComponentType<any>;
  color: string;
  description: string;
  templates: any[];
  selectedItems: BudgetItem[];
}

// Selection state management
const [expandedSections, setExpandedSections] = useState<Set<string>>(new Set());
const [selectedItems, setSelectedItems] = useState<Record<string, any[]>>({});
```

**Usage Pattern:**
- Categorized item selection with grouping
- Expandable/collapsible sections
- Visual selection indicators
- Bulk operations support

### **Real-Time Dashboard Pattern** (`TaskDashboard.tsx`)
```typescript
// Multi-view dashboard with filters
const VIEW_MODES = ['list', 'category', 'supplier'] as const;
const [viewMode, setViewMode] = useState<typeof VIEW_MODES[number]>('category');

// Real-time data updates
const [shoppingItems, setShoppingItems] = useState<ShoppingItem[]>([]);
const [cookingSchedules, setCookingSchedules] = useState<CookingSchedule[]>([]);

// Bulk operations state
const [selectedItems, setSelectedItems] = useState<Set<string>>(new Set());
const [bulkOperationMode, setBulkOperationMode] = useState(false);
```

**Usage Pattern:**
- Multiple view modes for same data
- Real-time updates with optimistic UI
- Bulk selection and operations
- Advanced filtering and search

## 🔧 Custom Hooks Architecture

### **Business Logic Hooks**
```typescript
// Complex state management for budget workflows
useBudgetWorkflow(initialBudget?: Partial<EnhancedBudget>)
// Returns: { budget, addItem, removeItem, updateItem, validateBudget }

// Real-time calculation engine
useBudgetCalculation(props: BudgetCalculationProps)
// Returns: { totalAmount, breakdown, calculateItemCost }

// Template management and selection
useTemplates()
// Returns: { meals, activities, transport, stay, loading, error }
```

### **UI State Hooks**
```typescript
// Loading states with error handling
useLoadingState(initialState = false)
// Returns: { loading, setLoading, withLoading }

// Search functionality with debouncing
useSearch<T>(items: T[], searchableFields: (keyof T)[])
// Returns: { searchTerm, setSearchTerm, filteredItems }

// Performance optimization utilities
usePerformance()
// Returns: { memoizedCallback, debounce, throttle }
```

## 🎨 UI Component Conventions

### **Form Components**
```typescript
// Consistent form field structure
interface FormFieldProps {
  label: string;
  id: string;
  error?: string | null;
  required?: boolean;
  description?: string;
  children: React.ReactNode;
}

// Validation pattern
const [errors, setErrors] = useState<Record<string, string>>({});
const validateField = useCallback((field: string, value: any) => {
  // Validation logic
}, []);
```

### **Data Display Components**
```typescript
// Card-based layouts for consistency
const ItemCard = ({ title, description, actions, status, onClick }) => (
  <Card className={`transition-all duration-200 hover:shadow-md ${
    status === 'selected' ? 'ring-2 ring-blue-500' : ''
  }`}>
    <CardContent>
      {/* Content with consistent spacing */}
    </CardContent>
  </Card>
);

// Table patterns with responsive design
const ResponsiveTable = ({ data, columns, onRowClick }) => {
  // Mobile card view + desktop table view
};
```

### **Modal Dialog Patterns**
```typescript
// Consistent modal structure
const ActionDialog = ({ isOpen, onClose, title, children, actions }) => (
  <Dialog open={isOpen} onOpenChange={onClose}>
    <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto">
      <DialogHeader>
        <DialogTitle>{title}</DialogTitle>
      </DialogHeader>
      <div className="space-y-6">
        {children}
      </div>
      <div className="flex justify-end gap-3">
        {actions}
      </div>
    </DialogContent>
  </Dialog>
);
```

## 🔄 State Management Patterns

### **Zustand Store Structure**
```typescript
// Feature-based store slices
interface StoreState {
  // Entity management
  clients: Client[];
  budgets: Budget[];
  tasks: Task[];
  
  // Actions grouped by feature
  addClient: (client: Client) => void;
  updateClient: (client: Client) => void;
  deleteClient: (clientId: string) => void;
  
  // UI state
  notifications: Notification[];
  toasts: Toast[];
}
```

### **Local Component State**
```typescript
// Complex form state management
const [formData, setFormData] = useState<FormData>(initialData);
const [validation, setValidation] = useState<ValidationState>({});
const [isDirty, setIsDirty] = useState(false);

// Update pattern with validation
const updateField = useCallback((field: string, value: any) => {
  setFormData(prev => ({ ...prev, [field]: value }));
  setIsDirty(true);
  validateField(field, value);
}, [validateField]);
```

## 🎯 Accessibility Patterns

### **Keyboard Navigation**
```typescript
// Focus management for complex interactions
const handleKeyDown = useCallback((e: KeyboardEvent) => {
  switch (e.key) {
    case 'Escape': onClose(); break;
    case 'Enter': e.ctrlKey && onSave(); break;
    case 'Tab': handleTabNavigation(e); break;
  }
}, [onClose, onSave]);

// Focus trap for modals
useFocusTrap(isModalOpen, modalRef);
```

### **Screen Reader Support**
```typescript
// Semantic HTML with ARIA labels
<button
  aria-label={`Edit ${item.name}`}
  aria-describedby={`${item.id}-description`}
  onClick={() => handleEdit(item)}
>
  <Edit className="h-4 w-4" />
</button>

// Live regions for dynamic updates
<div aria-live="polite" aria-atomic="true">
  {statusMessage}
</div>
```

## 📱 Responsive Design Patterns

### **Mobile-First Components**
```typescript
// Responsive layout with Tailwind
const ResponsiveLayout = ({ children }) => (
  <div className="
    grid grid-cols-1 gap-4
    md:grid-cols-2 md:gap-6
    lg:grid-cols-3 lg:gap-8
    xl:grid-cols-4
  ">
    {children}
  </div>
);

// Mobile navigation patterns
const MobileMenu = () => {
  const isMobile = useIsMobile();
  return isMobile ? <DrawerNavigation /> : <SidebarNavigation />;
};
```

### **Touch-Friendly Interactions**
```typescript
// Larger touch targets for mobile
const TouchButton = ({ children, ...props }) => (
  <button 
    className="min-h-[44px] min-w-[44px] p-3 touch-manipulation"
    {...props}
  >
    {children}
  </button>
);
```

## 🚀 Performance Patterns

### **Optimization Techniques**
```typescript
// Memoization for expensive components
const ExpensiveComponent = React.memo(({ data, onUpdate }) => {
  const processedData = useMemo(() => 
    expensiveCalculation(data), [data]
  );
  
  const handleUpdate = useCallback((id: string) => {
    onUpdate(id);
  }, [onUpdate]);
  
  return <ComplexUI data={processedData} onUpdate={handleUpdate} />;
});

// Virtual scrolling for large lists
const VirtualizedList = ({ items, renderItem }) => {
  const { visibleItems, containerRef } = useVirtualScroll(items);
  return (
    <div ref={containerRef}>
      {visibleItems.map(renderItem)}
    </div>
  );
};
```

---

**Usage Tips:**
- Reference this notepad when building new components
- Use `@COMPONENT_ARCHITECTURE.md` to understand UI patterns
- Follow established conventions for consistency
- Consider accessibility and performance from the start
- Leverage custom hooks for reusable business logic 