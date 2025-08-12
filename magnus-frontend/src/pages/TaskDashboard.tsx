import React, { useState, useMemo } from 'react';
import { useStore } from '../store';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Badge } from '../components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/ui/tabs';
import { Checkbox } from '../components/ui/checkbox';
import { Input } from '../components/ui/input';
import { Textarea } from '../components/ui/textarea';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import {
  CheckCircle2,
  Clock,
  AlertTriangle,
  PlayCircle,
  Calendar,
  Users,
  ShoppingCart,
  ChefHat,
  Filter,
  Plus,
  Minus,
  Edit3,
  Trash2,
  Save
} from 'lucide-react';
import { TaskScheduler } from '../services/taskScheduler';
import { ShoppingListService } from '../services/shoppingListService';
import { CookingScheduleService } from '../services/cookingScheduleService';
import { ShoppingItem, CookingSchedule, ProductRequirement, Task } from '../types/Task';
import { useApi } from '../hooks/useApi';
import { useToast } from '../components/ui/use-toast';

const TaskDashboard: React.FC = () => {
  const { tasks, currentUser, updateTask, budgets, products } = useStore();
  const { updateTask: updateTaskApi } = useApi();
  const { toast } = useToast();
  const [selectedPriority, setSelectedPriority] = useState<string>('all');
  const [selectedStatus, setSelectedStatus] = useState<string>('all');
  const [currentWeek, setCurrentWeek] = useState(() => {
    const now = new Date();
    const startOfWeek = new Date(now.setDate(now.getDate() - now.getDay()));
    return startOfWeek;
  });
  const [shoppingItems, setShoppingItems] = useState<ShoppingItem[]>([]);
  const [cookingSchedules, setCookingSchedules] = useState<CookingSchedule[]>([]);
  
  // Product editing state
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [editingProducts, setEditingProducts] = useState<ProductRequirement[]>([]);
  const [isProductDialogOpen, setIsProductDialogOpen] = useState(false);
  const [showProductSuggestions, setShowProductSuggestions] = useState<number | null>(null);
  
  // Logistics state
  const [shoppingSearchTerm, setShoppingSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [shoppingViewMode, setShoppingViewMode] = useState<'category' | 'supplier' | 'event'>('category');
  const [bulkSelection, setBulkSelection] = useState<Set<string>>(new Set());

  // Filter tasks for current user role
  const userTasks = useMemo(() => {
    if (!currentUser) return [];
    return tasks.filter(task => task.assignedToRole === currentUser.role);
  }, [tasks, currentUser]);

  // Apply filters
  const filteredTasks = useMemo(() => {
    return userTasks.filter(task => {
      const priorityMatch = selectedPriority === 'all' || task.priority === selectedPriority;
      const statusMatch = selectedStatus === 'all' || task.status === selectedStatus;
      return priorityMatch && statusMatch;
    });
  }, [userTasks, selectedPriority, selectedStatus]);

  // Get next available tasks (not blocked)
  const availableTasks = useMemo(() => {
    if (!currentUser) return [];
    if (currentUser.role !== 'logistics' && currentUser.role !== 'cook') return [];
    return TaskScheduler.getTasksByRole(tasks, currentUser.role);
  }, [tasks, currentUser]);

  // Task statistics
  const taskStats = useMemo(() => {
    return {
      total: userTasks.length,
      todo: userTasks.filter(t => t.status === 'todo').length,
      inProgress: userTasks.filter(t => t.status === 'in_progress').length,
      done: userTasks.filter(t => t.status === 'done').length,
      blocked: userTasks.filter(t => t.status === 'blocked').length,
      urgent: userTasks.filter(t => t.priority === 'urgent').length,
      overdue: userTasks.filter(t => new Date(t.dueDate) < new Date() && t.status !== 'done').length
    };
  }, [userTasks]);

  // Generate shopping list for logistics users
  React.useEffect(() => {
    if (currentUser?.role === 'logistics') {
      const weekEnd = new Date(currentWeek);
      weekEnd.setDate(weekEnd.getDate() + 6);
      
      const weeklyItems = ShoppingListService.generateWeeklyShoppingList(
        budgets,
        tasks,
        currentWeek,
        weekEnd
      );
      setShoppingItems(weeklyItems);
    }
  }, [currentUser, budgets, tasks, currentWeek]);

  // Generate cooking schedules for cook users
  React.useEffect(() => {
    if (currentUser?.role === 'cook') {
      const weekEnd = new Date(currentWeek);
      weekEnd.setDate(weekEnd.getDate() + 6);
      
      const schedules = CookingScheduleService.generateCookingSchedules(
        budgets,
        tasks,
        currentWeek,
        weekEnd
      );
      setCookingSchedules(schedules);
    }
  }, [currentUser, budgets, tasks, currentWeek]);

  // Sync changes between cook and logistics
  React.useEffect(() => {
    if (currentUser?.role === 'logistics') {
      syncCookChangesToLogistics();
    }
  }, [tasks]); // Re-sync when tasks change

  const handleShoppingItemUpdate = (itemId: string, purchasedQuantity: number) => {
    const updatedItems = ShoppingListService.updateShoppingItemStatus(
      shoppingItems,
      itemId,
      purchasedQuantity,
      currentUser?.name || 'Usuario'
    );
    setShoppingItems(updatedItems);
  };

  const handleIngredientUpdate = (scheduleId: string, ingredientId: string, updates: Partial<ProductRequirement>) => {
    const updatedSchedules = CookingScheduleService.updateIngredient(
      cookingSchedules,
      scheduleId,
      ingredientId,
      updates
    );
    setCookingSchedules(updatedSchedules);
  };

  // Product editing functions
  const openProductEditor = (task: Task) => {
    setEditingTask(task);
    
    // Get products from either productRequirements or cookingSchedule.ingredients
    let productsToEdit: ProductRequirement[] = [];
    if (task.productRequirements && task.productRequirements.length > 0) {
      productsToEdit = [...task.productRequirements];
    } else if (task.cookingSchedule?.ingredients && task.cookingSchedule.ingredients.length > 0) {
      productsToEdit = [...task.cookingSchedule.ingredients];
    }
    // If no products exist, start with an empty array
    
    setEditingProducts(productsToEdit);
    setIsProductDialogOpen(true);
  };

  const closeProductEditor = () => {
    setEditingTask(null);
    setEditingProducts([]);
    setIsProductDialogOpen(false);
  };

  const addNewProduct = () => {
    const newProduct: ProductRequirement = {
      id: `temp-${Date.now()}`,
      productId: '',
      productName: '',
      quantity: 1,
      unit: 'unidades',
      category: 'otros',
      isPurchased: false,
      notes: ''
    };
    setEditingProducts([...editingProducts, newProduct]);
  };

  const updateEditingProduct = (index: number, updates: Partial<ProductRequirement>) => {
    const updatedProducts = [...editingProducts];
    updatedProducts[index] = { ...updatedProducts[index], ...updates };
    setEditingProducts(updatedProducts);
  };

  const removeEditingProduct = (index: number) => {
    const updatedProducts = editingProducts.filter((_, i) => i !== index);
    setEditingProducts(updatedProducts);
  };

  const saveProductChanges = () => {
    if (!editingTask) return;

    const updatedTask = { ...editingTask };
    
    // Update either productRequirements or cookingSchedule.ingredients based on task type
    if (editingTask.productRequirements !== undefined) {
      // Task already has productRequirements field
      updatedTask.productRequirements = editingProducts;
    } else if (editingTask.cookingSchedule) {
      // Task has a cooking schedule
      updatedTask.cookingSchedule = {
        ...editingTask.cookingSchedule,
        ingredients: editingProducts
      };
    } else if (editingTask.type === 'cooking' || editingTask.type === 'preparation') {
      // Task is cooking-related but doesn't have productRequirements yet
      updatedTask.productRequirements = editingProducts;
    }

    // Update the task in the store
    updateTask(updatedTask);
    
    // If it's a cooking schedule, also update the local state
    if (editingTask.cookingSchedule) {
      const updatedSchedules = cookingSchedules.map(schedule => 
        schedule.id === editingTask.cookingSchedule?.id 
          ? { ...schedule, ingredients: editingProducts }
          : schedule
      );
      setCookingSchedules(updatedSchedules);
    }

    closeProductEditor();
  };

  // Enhanced logistics functions
  const syncCookChangesToLogistics = () => {
    // When cook updates ingredients, regenerate shopping list
    if (currentUser?.role === 'logistics') {
      const weekEnd = new Date(currentWeek);
      weekEnd.setDate(weekEnd.getDate() + 6);
      
      const weeklyItems = ShoppingListService.generateWeeklyShoppingList(
        budgets,
        tasks,
        currentWeek,
        weekEnd
      );
      setShoppingItems(weeklyItems);
    }
  };

  const handleBulkPurchase = (itemIds: string[], purchased: boolean) => {
    let updatedItems = [...shoppingItems];
    itemIds.forEach(itemId => {
      updatedItems = ShoppingListService.updateShoppingItemStatus(
        updatedItems,
        itemId,
        purchased ? updatedItems.find(item => item.id === itemId)?.totalQuantity || 0 : 0,
        currentUser?.name || 'Usuario'
      );
    });
    setShoppingItems(updatedItems);
    setBulkSelection(new Set());
  };

  const toggleBulkSelection = (itemId: string) => {
    const newSelection = new Set(bulkSelection);
    if (newSelection.has(itemId)) {
      newSelection.delete(itemId);
    } else {
      newSelection.add(itemId);
    }
    setBulkSelection(newSelection);
  };

  const getFilteredShoppingItems = () => {
    let filtered = shoppingItems;
    
    // Filter by search term
    if (shoppingSearchTerm) {
      filtered = filtered.filter(item => 
        item.productName.toLowerCase().includes(shoppingSearchTerm.toLowerCase()) ||
        item.clientNames.some(name => name.toLowerCase().includes(shoppingSearchTerm.toLowerCase()))
      );
    }
    
    // Filter by category
    if (selectedCategory !== 'all') {
      filtered = filtered.filter(item => item.category === selectedCategory);
    }
    
    return filtered;
  };

  const getCategoryIcon = (category: string) => {
    const icons: Record<string, string> = {
      'carnes': '🥩',
      'verduras': '🥬',
      'frutas': '🍎',
      'lacteos': '🥛',
      'cereales': '🌾',
      'condimentos': '🧂',
      'bebidas': '🥤',
      'panaderia': '🍞',
      'congelados': '🧊',
      'otros': '📦'
    };
    return icons[category] || '📦';
  };

  const getSupplierInfo = (category: string) => {
    const suppliers: Record<string, { name: string; contact: string; specialty: string }> = {
      'carnes': { name: 'Carnicería Premium', contact: '+34 123 456 789', specialty: 'Carnes selectas' },
      'verduras': { name: 'Verduras Frescas SA', contact: '+34 987 654 321', specialty: 'Productos orgánicos' },
      'lacteos': { name: 'Lácteos del Valle', contact: '+34 555 123 456', specialty: 'Productos frescos' },
      'bebidas': { name: 'Distribuidora Central', contact: '+34 666 789 123', specialty: 'Bebidas y licores' },
      'otros': { name: 'Proveedor General', contact: '+34 444 555 666', specialty: 'Productos varios' }
    };
    return suppliers[category] || suppliers['otros'];
  };

  const handleStatusChange = (taskId: string, newStatus: 'todo' | 'in_progress' | 'done' | 'blocked') => {
    const task = tasks.find(t => t.id === taskId);
    if (task) {
      const updated = { 
        ...task,
        status: newStatus,
        updatedAt: new Date().toISOString()
      } as any;
      updateTask(updated);
      // Best-effort server sync
      if ((updated as any).id) {
        updateTaskApi((updated as any).id, updated)
          .then(() => {
            toast({ title: 'Tarea actualizada', description: 'El estado se sincronizó con el servidor.' });
          })
          .catch(() => {
            toast({ title: 'Sincronización fallida', description: 'No se pudo guardar en el servidor. Intentaremos más tarde.', variant: 'destructive' });
          });
      }
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'urgent': return 'bg-red-500 text-white';
      case 'high': return 'bg-orange-500 text-white';
      case 'medium': return 'bg-yellow-500 text-black';
      case 'low': return 'bg-green-500 text-white';
      default: return 'bg-gray-500 text-white';
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'done': return <CheckCircle2 className="h-4 w-4 text-green-600" />;
      case 'in_progress': return <PlayCircle className="h-4 w-4 text-blue-600" />;
      case 'blocked': return <AlertTriangle className="h-4 w-4 text-red-600" />;
      default: return <Clock className="h-4 w-4 text-gray-600" />;
    }
  };

  const isOverdue = (dueDate: string, status: string) => {
    return new Date(dueDate) < new Date() && status !== 'done';
  };

  if (!currentUser) {
    return <div>No hay usuario autenticado</div>;
  }

  return (
    <div className="space-y-6 p-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Panel de Tareas</h1>
          <p className="text-slate-600 mt-2">
            Gestión de tareas para {currentUser.role === 'logistics' ? 'Logística' : 'Cocina'}
          </p>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-600">Total Tareas</p>
                <p className="text-2xl font-bold text-slate-900">{taskStats.total}</p>
              </div>
              <Calendar className="h-8 w-8 text-blue-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-600">En Progreso</p>
                <p className="text-2xl font-bold text-slate-900">{taskStats.inProgress}</p>
              </div>
              <PlayCircle className="h-8 w-8 text-blue-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-600">Urgentes</p>
                <p className="text-2xl font-bold text-slate-900">{taskStats.urgent}</p>
              </div>
              <AlertTriangle className="h-8 w-8 text-red-600" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-slate-600">Completadas</p>
                <p className="text-2xl font-bold text-slate-900">{taskStats.done}</p>
              </div>
              <CheckCircle2 className="h-8 w-8 text-green-600" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Main Content */}
      <Tabs defaultValue="available" className="space-y-6">
        <TabsList>
          <TabsTrigger value="available">Tareas Disponibles</TabsTrigger>
          <TabsTrigger value="all">Todas las Tareas</TabsTrigger>
          <TabsTrigger value="schedule">Cronograma</TabsTrigger>
          {currentUser.role === 'logistics' && (
            <TabsTrigger value="shopping">
              <ShoppingCart className="h-4 w-4 mr-2" />
              Lista de Compras
            </TabsTrigger>
          )}
          {currentUser.role === 'cook' && (
            <TabsTrigger value="cooking">
              <ChefHat className="h-4 w-4 mr-2" />
              Programación de Cocina
            </TabsTrigger>
          )}
        </TabsList>

        {/* Available Tasks Tab */}
        <TabsContent value="available" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Próximas Tareas Disponibles</CardTitle>
              <CardDescription>
                Tareas que puedes comenzar ahora (sin dependencias bloqueantes)
              </CardDescription>
            </CardHeader>
            <CardContent>
              {availableTasks.length === 0 ? (
                <div className="text-center py-8">
                  <CheckCircle2 className="h-12 w-12 mx-auto mb-4 text-green-300" />
                  <h3 className="text-lg font-medium text-slate-900 mb-2">¡Todas las tareas completadas!</h3>
                  <p className="text-slate-600">No hay tareas disponibles en este momento</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {availableTasks.slice(0, 5).map((task) => (
                    <div key={task.id} className="flex items-center justify-between p-4 bg-slate-50 rounded-lg border">
                      <div className="flex items-center gap-4">
                        {getStatusIcon(task.status)}
                        <div>
                          <h4 className="font-medium text-slate-900">{task.description}</h4>
                          <div className="flex items-center gap-2 mt-1">
                            <Badge className={getPriorityColor(task.priority)}>
                              {task.priority}
                            </Badge>
                            <span className="text-sm text-slate-600">
                              Vence: {new Date(task.dueDate).toLocaleDateString('es-ES')}
                            </span>
                            {task.estimatedDuration && (
                              <span className="text-sm text-slate-600">
                                ~{task.estimatedDuration}h
                              </span>
                            )}
                            {isOverdue(task.dueDate, task.status) && (
                              <Badge className="bg-red-100 text-red-800">
                                Retrasada
                              </Badge>
                            )}
                          </div>
                        </div>
                      </div>
                      <div className="flex items-center gap-2">
                        {task.status === 'todo' && (
                          <Button 
                            size="sm" 
                            onClick={() => handleStatusChange(task.id, 'in_progress')}
                          >
                            Comenzar
                          </Button>
                        )}
                        {currentUser.role === 'cook' && (task.type === 'cooking' || task.type === 'preparation' || task.productRequirements || task.cookingSchedule) && (
                          <Button 
                            size="sm" 
                            variant="outline"
                            onClick={() => openProductEditor(task)}
                            className="gap-1"
                          >
                            <Edit3 className="h-3 w-3" />
                            {(task.productRequirements && task.productRequirements.length > 0) || (task.cookingSchedule && task.cookingSchedule.ingredients.length > 0) ? 'Editar' : 'Agregar'} Productos
                          </Button>
                        )}
                        {task.status === 'in_progress' && (
                          <Button 
                            size="sm" 
                            variant="outline"
                            onClick={() => handleStatusChange(task.id, 'done')}
                          >
                            Completar
                          </Button>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* All Tasks Tab */}
        <TabsContent value="all" className="space-y-4">
          {/* Filters */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Filter className="h-5 w-5" />
                Filtros
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex gap-4">
                <div>
                  <label className="text-sm font-medium">Prioridad:</label>
                  <select 
                    value={selectedPriority} 
                    onChange={(e) => setSelectedPriority(e.target.value)}
                    className="ml-2 border rounded px-2 py-1"
                  >
                    <option value="all">Todas</option>
                    <option value="urgent">Urgente</option>
                    <option value="high">Alta</option>
                    <option value="medium">Media</option>
                    <option value="low">Baja</option>
                  </select>
                </div>
                <div>
                  <label className="text-sm font-medium">Estado:</label>
                  <select 
                    value={selectedStatus} 
                    onChange={(e) => setSelectedStatus(e.target.value)}
                    className="ml-2 border rounded px-2 py-1"
                  >
                    <option value="all">Todos</option>
                    <option value="todo">Por hacer</option>
                    <option value="in_progress">En progreso</option>
                    <option value="done">Completado</option>
                    <option value="blocked">Bloqueado</option>
                  </select>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Tasks List */}
          <Card>
            <CardHeader>
              <CardTitle>Todas las Tareas ({filteredTasks.length})</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {filteredTasks.map((task) => (
                  <div key={task.id} className="flex items-center justify-between p-3 border rounded-lg">
                    <div className="flex items-center gap-3">
                      {getStatusIcon(task.status)}
                      <div>
                        <h4 className="font-medium">{task.description}</h4>
                        <div className="flex items-center gap-2 mt-1">
                          <Badge className={getPriorityColor(task.priority)}>
                            {task.priority}
                          </Badge>
                          <span className="text-sm text-slate-600">
                            {new Date(task.dueDate).toLocaleDateString('es-ES')}
                          </span>
                          {task.autoScheduled && (
                            <Badge variant="outline">Auto-programada</Badge>
                          )}
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Badge variant="outline">{task.status}</Badge>
                      {currentUser.role === 'cook' && (task.type === 'cooking' || task.type === 'preparation' || task.productRequirements || task.cookingSchedule) && (
                        <Button 
                          size="sm" 
                          variant="outline"
                          onClick={() => openProductEditor(task)}
                          className="gap-1"
                        >
                          <Edit3 className="h-3 w-3" />
                          {(task.productRequirements && task.productRequirements.length > 0) || (task.cookingSchedule && task.cookingSchedule.ingredients.length > 0) ? 'Editar' : 'Agregar'} Productos
                        </Button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Schedule Tab */}
        <TabsContent value="schedule" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Cronograma de Tareas</CardTitle>
              <CardDescription>Vista temporal de todas las tareas</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {/* Group tasks by date */}
                {Object.entries(
                  userTasks.reduce((groups: Record<string, typeof userTasks>, task) => {
                    const date = new Date(task.dueDate).toDateString();
                    if (!groups[date]) groups[date] = [];
                    groups[date].push(task);
                    return groups;
                  }, {})
                )
                .sort(([a], [b]) => new Date(a).getTime() - new Date(b).getTime())
                .map(([date, dayTasks]) => (
                  <div key={date} className="border-l-2 border-blue-200 pl-4">
                    <div className="flex items-center gap-3 mb-2">
                      <div className="w-3 h-3 bg-blue-600 rounded-full -ml-6 border-2 border-white"></div>
                      <h4 className="font-medium">
                        {new Date(date).toLocaleDateString('es-ES', { 
                          weekday: 'long', 
                          year: 'numeric', 
                          month: 'long', 
                          day: 'numeric' 
                        })}
                      </h4>
                    </div>
                    <div className="space-y-2 mb-4">
                      {dayTasks.map((task) => (
                        <div key={task.id} className="flex items-center gap-2 text-sm">
                          {getStatusIcon(task.status)}
                          <span className="flex-1">{task.description}</span>
                          <Badge className={getPriorityColor(task.priority)}>
                            {task.priority}
                          </Badge>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Enhanced Shopping List Tab - Only for Logistics */}
        {currentUser.role === 'logistics' && (
          <TabsContent value="shopping" className="space-y-4">
            {/* Header with controls */}
            <div className="flex flex-col lg:flex-row gap-4 lg:items-center lg:justify-between">
              <div>
                <h2 className="text-2xl font-bold">Lista de Compras Semanal</h2>
                <p className="text-slate-600">
                  Semana del {currentWeek.toLocaleDateString('es-ES')} al {new Date(currentWeek.getTime() + 6 * 24 * 60 * 60 * 1000).toLocaleDateString('es-ES')}
                </p>
              </div>
              
              {/* View mode toggles */}
              <div className="flex items-center gap-2">
                <Select value={shoppingViewMode} onValueChange={(value: 'category' | 'supplier' | 'event') => setShoppingViewMode(value)}>
                  <SelectTrigger className="w-32">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="category">Por Categoría</SelectItem>
                    <SelectItem value="supplier">Por Proveedor</SelectItem>
                    <SelectItem value="event">Por Evento</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* Search and filters */}
            <Card>
              <CardContent className="p-4">
                <div className="flex flex-col md:flex-row gap-4">
                  <div className="flex-1">
                    <Input
                      placeholder="Buscar productos o clientes..."
                      value={shoppingSearchTerm}
                      onChange={(e) => setShoppingSearchTerm(e.target.value)}
                      className="w-full"
                    />
                  </div>
                  <div className="flex gap-2">
                    <Select value={selectedCategory} onValueChange={setSelectedCategory}>
                      <SelectTrigger className="w-40">
                        <SelectValue placeholder="Categoría" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="all">Todas las categorías</SelectItem>
                        <SelectItem value="carnes">🥩 Carnes</SelectItem>
                        <SelectItem value="verduras">🥬 Verduras</SelectItem>
                        <SelectItem value="frutas">🍎 Frutas</SelectItem>
                        <SelectItem value="lacteos">🥛 Lácteos</SelectItem>
                        <SelectItem value="cereales">🌾 Cereales</SelectItem>
                        <SelectItem value="condimentos">🧂 Condimentos</SelectItem>
                        <SelectItem value="bebidas">🥤 Bebidas</SelectItem>
                        <SelectItem value="panaderia">🍞 Panadería</SelectItem>
                        <SelectItem value="congelados">🧊 Congelados</SelectItem>
                        <SelectItem value="otros">📦 Otros</SelectItem>
                      </SelectContent>
                    </Select>
                    
                    {bulkSelection.size > 0 && (
                      <div className="flex gap-2">
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleBulkPurchase(Array.from(bulkSelection), true)}
                        >
                          Marcar como Comprado ({bulkSelection.size})
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => setBulkSelection(new Set())}
                        >
                          Deseleccionar
                        </Button>
                      </div>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Shopping content */}
            {getFilteredShoppingItems().length === 0 ? (
              <Card>
                <CardContent className="text-center py-12">
                  <ShoppingCart className="h-16 w-16 mx-auto mb-4 text-gray-300" />
                  <h3 className="text-xl font-medium text-slate-900 mb-2">
                    {shoppingItems.length === 0 ? 'No hay compras programadas' : 'No se encontraron productos'}
                  </h3>
                  <p className="text-slate-600">
                    {shoppingItems.length === 0 
                      ? 'No se encontraron eventos aprobados para esta semana'
                      : 'Intenta con diferentes filtros de búsqueda'
                    }
                  </p>
                </CardContent>
              </Card>
            ) : (
              <>
                {/* Progress Overview */}
                <Card>
                  <CardContent className="p-6">
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                      <div className="text-center">
                        <div className="text-2xl font-bold text-blue-600">
                          {getFilteredShoppingItems().length}
                        </div>
                        <div className="text-sm text-slate-600">Total Productos</div>
                      </div>
                      <div className="text-center">
                        <div className="text-2xl font-bold text-green-600">
                          {ShoppingListService.getShoppingProgress(getFilteredShoppingItems()).purchased}
                        </div>
                        <div className="text-sm text-slate-600">Completados</div>
                      </div>
                      <div className="text-center">
                        <div className="text-2xl font-bold text-orange-600">
                          {getFilteredShoppingItems().filter(item => !item.isPurchased).length}
                        </div>
                        <div className="text-sm text-slate-600">Pendientes</div>
                      </div>
                      <div className="text-center">
                        <div className="text-2xl font-bold text-purple-600">
                          {ShoppingListService.getShoppingProgress(getFilteredShoppingItems()).completionPercentage.toFixed(0)}%
                        </div>
                        <div className="text-sm text-slate-600">Progreso</div>
                      </div>
                    </div>
                    
                    <div className="mt-4">
                      <div className="flex items-center justify-between mb-2">
                        <span className="text-sm font-medium">Progreso General</span>
                        <span className="text-sm text-slate-600">
                          {ShoppingListService.getShoppingProgress(getFilteredShoppingItems()).purchased} de {getFilteredShoppingItems().length}
                        </span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-3">
                        <div 
                          className="bg-gradient-to-r from-blue-500 to-green-500 h-3 rounded-full transition-all duration-300" 
                          style={{ width: `${ShoppingListService.getShoppingProgress(getFilteredShoppingItems()).completionPercentage}%` }}
                        ></div>
                      </div>
                    </div>
                  </CardContent>
                </Card>

                {/* Shopping Items by Category */}
                {shoppingViewMode === 'category' && (
                  <div className="space-y-6">
                    {Object.entries(ShoppingListService.groupByCategory(getFilteredShoppingItems())).map(([category, items]) => (
                      <Card key={category}>
                        <CardHeader className="pb-3">
                          <div className="flex items-center justify-between">
                            <CardTitle className="flex items-center gap-3">
                              <span className="text-2xl">{getCategoryIcon(category)}</span>
                              <div>
                                <div className="capitalize">{category}</div>
                                <div className="text-sm font-normal text-slate-600">
                                  {items.length} productos • {getSupplierInfo(category).name}
                                </div>
                              </div>
                            </CardTitle>
                            <div className="flex items-center gap-2">
                              <Badge variant="outline">
                                {items.filter(item => item.isPurchased).length} / {items.length} completados
                              </Badge>
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => {
                                  const categoryItemIds = items.map(item => item.id);
                                  const newSelection = new Set(bulkSelection);
                                  categoryItemIds.forEach(id => newSelection.add(id));
                                  setBulkSelection(newSelection);
                                }}
                              >
                                Seleccionar Categoría
                              </Button>
                            </div>
                          </div>
                        </CardHeader>
                        <CardContent>
                          <div className="space-y-3">
                            {items.map((item) => (
                              <div 
                                key={item.id} 
                                className={`p-4 border rounded-lg transition-all hover:bg-slate-50 ${
                                  item.isPurchased ? 'bg-green-50 border-green-200' : 'bg-white'
                                } ${
                                  bulkSelection.has(item.id) ? 'ring-2 ring-blue-500' : ''
                                }`}
                              >
                                <div className="flex items-center justify-between">
                                  <div className="flex items-center gap-4">
                                    <div className="flex items-center gap-2">
                                      <Checkbox
                                        checked={bulkSelection.has(item.id)}
                                        onCheckedChange={() => toggleBulkSelection(item.id)}
                                      />
                                      <Checkbox
                                        checked={item.isPurchased}
                                        onCheckedChange={(checked) => {
                                          const quantity = checked ? item.totalQuantity : 0;
                                          handleShoppingItemUpdate(item.id, quantity);
                                        }}
                                      />
                                    </div>
                                    <div className="flex-1">
                                      <h6 className={`font-medium ${item.isPurchased ? 'line-through text-slate-500' : 'text-slate-900'}`}>
                                        {item.productName}
                                      </h6>
                                      <div className="flex items-center gap-4 text-sm text-slate-600 mt-1">
                                        <span className="font-mono bg-slate-100 px-2 py-1 rounded">
                                          {item.totalQuantity} {item.unit}
                                        </span>
                                        <span>Para: {item.clientNames.join(', ')}</span>
                                        <span>📞 {getSupplierInfo(category).contact}</span>
                                      </div>
                                      {item.notes && (
                                        <p className="text-xs text-slate-500 mt-2 bg-yellow-50 p-2 rounded border-l-2 border-yellow-400">
                                          💡 {item.notes}
                                        </p>
                                      )}
                                    </div>
                                  </div>
                                  
                                  {!item.isPurchased && (
                                    <div className="flex items-center gap-3">
                                      <div className="flex items-center gap-2">
                                        <Input
                                          type="number"
                                          min="0"
                                          max={item.totalQuantity}
                                          value={item.purchasedQuantity || 0}
                                          onChange={(e) => handleShoppingItemUpdate(item.id, parseInt(e.target.value) || 0)}
                                          className="w-20"
                                          placeholder="0"
                                        />
                                        <span className="text-sm text-slate-600">/ {item.totalQuantity}</span>
                                      </div>
                                      <Button
                                        size="sm"
                                        onClick={() => handleShoppingItemUpdate(item.id, item.totalQuantity)}
                                      >
                                        Marcar Completo
                                      </Button>
                                    </div>
                                  )}
                                  
                                  {item.isPurchased && (
                                    <div className="flex items-center gap-2 text-green-600">
                                      <CheckCircle2 className="h-5 w-5" />
                                      <span className="text-sm font-medium">Completado</span>
                                    </div>
                                  )}
                                </div>
                              </div>
                            ))}
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                )}

                {/* View by Supplier */}
                {shoppingViewMode === 'supplier' && (
                  <div className="space-y-6">
                    {Object.entries(
                      getFilteredShoppingItems().reduce((groups: Record<string, typeof shoppingItems>, item) => {
                        const supplier = getSupplierInfo(item.category).name;
                        if (!groups[supplier]) groups[supplier] = [];
                        groups[supplier].push(item);
                        return groups;
                      }, {})
                    ).map(([supplier, items]) => (
                      <Card key={supplier}>
                        <CardHeader>
                          <CardTitle className="flex items-center justify-between">
                            <div>
                              <div>{supplier}</div>
                              <div className="text-sm font-normal text-slate-600">
                                {items.length} productos • {getSupplierInfo(items[0].category).contact}
                              </div>
                            </div>
                            <Badge variant="outline">
                              {items.filter(item => item.isPurchased).length} / {items.length} completados
                            </Badge>
                          </CardTitle>
                        </CardHeader>
                        <CardContent>
                          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                            {items.map((item) => (
                              <div key={item.id} className={`p-3 border rounded ${item.isPurchased ? 'bg-green-50' : 'bg-white'}`}>
                                <div className="flex items-center gap-2">
                                  <Checkbox
                                    checked={item.isPurchased}
                                    onCheckedChange={(checked) => {
                                      const quantity = checked ? item.totalQuantity : 0;
                                      handleShoppingItemUpdate(item.id, quantity);
                                    }}
                                  />
                                  <div className="flex-1">
                                    <h6 className="font-medium">{item.productName}</h6>
                                    <div className="text-sm text-slate-600">
                                      {item.totalQuantity} {item.unit} • {item.category}
                                    </div>
                                  </div>
                                </div>
                              </div>
                            ))}
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                )}

                {/* View by Event */}
                {shoppingViewMode === 'event' && (
                  <div className="space-y-6">
                    {Object.entries(
                      getFilteredShoppingItems().reduce((groups: Record<string, typeof shoppingItems>, item) => {
                        const client = item.clientNames[0] || 'Cliente Desconocido';
                        if (!groups[client]) groups[client] = [];
                        groups[client].push(item);
                        return groups;
                      }, {})
                    ).map(([client, items]) => (
                      <Card key={client}>
                        <CardHeader>
                          <CardTitle className="flex items-center justify-between">
                            <div>
                              <div>{client}</div>
                              <div className="text-sm font-normal text-slate-600">
                                {items.length} productos requeridos
                              </div>
                            </div>
                            <Badge variant="outline">
                              {items.filter(item => item.isPurchased).length} / {items.length} completados
                            </Badge>
                          </CardTitle>
                        </CardHeader>
                        <CardContent>
                          <div className="space-y-2">
                            {items.map((item) => (
                              <div key={item.id} className={`p-3 border rounded flex items-center gap-3 ${item.isPurchased ? 'bg-green-50' : 'bg-white'}`}>
                                <Checkbox
                                  checked={item.isPurchased}
                                  onCheckedChange={(checked) => {
                                    const quantity = checked ? item.totalQuantity : 0;
                                    handleShoppingItemUpdate(item.id, quantity);
                                  }}
                                />
                                <span className="text-lg">{getCategoryIcon(item.category)}</span>
                                <div className="flex-1">
                                  <h6 className="font-medium">{item.productName}</h6>
                                  <div className="text-sm text-slate-600">
                                    {item.totalQuantity} {item.unit} • {item.category}
                                  </div>
                                </div>
                              </div>
                            ))}
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                )}
              </>
            )}
          </TabsContent>
        )}

        {/* Cooking Schedule Tab - Only for Cook */}
        {currentUser.role === 'cook' && (
          <TabsContent value="cooking" className="space-y-4">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <ChefHat className="h-5 w-5" />
                  Programación de Cocina
                </CardTitle>
                <CardDescription>
                  Cronograma de cocción y gestión de ingredientes
                </CardDescription>
              </CardHeader>
              <CardContent>
                {cookingSchedules.length === 0 ? (
                  <div className="text-center py-8">
                    <ChefHat className="h-12 w-12 mx-auto mb-4 text-gray-300" />
                    <h3 className="text-lg font-medium text-slate-900 mb-2">No hay programación de cocina</h3>
                    <p className="text-slate-600">No se encontraron eventos programados para cocinar esta semana</p>
                  </div>
                ) : (
                  <div className="space-y-6">
                    {/* Upcoming cooking tasks */}
                    <div className="p-4 bg-orange-50 rounded-lg">
                      <h5 className="font-medium mb-2 flex items-center gap-2">
                        <Clock className="h-4 w-4" />
                        Próximas Tareas de Cocina
                      </h5>
                      {CookingScheduleService.getUpcomingTasks(cookingSchedules, 3).map((schedule) => (
                        <div key={schedule.id} className="flex items-center gap-3 py-2 border-b last:border-b-0">
                          <Badge variant="outline">{schedule.mealType}</Badge>
                          <span className="flex-1">{schedule.menuName}</span>
                          <span className="text-sm text-slate-600">
                            {new Date(schedule.eventDate).toLocaleDateString('es-ES')} a las {schedule.cookingTime}
                          </span>
                          <span className="text-sm text-slate-500">
                            {schedule.guestCount} personas
                          </span>
                        </div>
                      ))}
                    </div>

                    {/* All cooking schedules */}
                    {cookingSchedules.map((schedule) => (
                      <Card key={schedule.id} className="border-l-4 border-orange-500">
                        <CardHeader>
                          <div className="flex items-center justify-between">
                            <div>
                              <CardTitle className="text-lg">{schedule.menuName}</CardTitle>
                              <CardDescription>
                                {new Date(schedule.eventDate).toLocaleDateString('es-ES')} a las {schedule.cookingTime} • {schedule.guestCount} personas
                              </CardDescription>
                            </div>
                            <Badge className="bg-orange-100 text-orange-800">
                              {schedule.mealType}
                            </Badge>
                          </div>
                        </CardHeader>
                        <CardContent>
                          {schedule.specialInstructions && (
                            <div className="mb-4 p-3 bg-yellow-50 rounded border-l-4 border-yellow-400">
                              <p className="text-sm">{schedule.specialInstructions}</p>
                            </div>
                          )}
                          <div>
                            <div className="flex items-center justify-between mb-3">
                              <h6 className="font-medium">Ingredientes Requeridos</h6>
                              <Button
                                size="sm"
                                variant="outline"
                                onClick={() => {
                                  // Find the task that corresponds to this cooking schedule
                                  const relatedTask = tasks.find(task => 
                                    task.cookingSchedule?.id === schedule.id ||
                                    (task.type === 'cooking' && task.relatedBudgetId)
                                  );
                                  if (relatedTask) {
                                    // Create a temporary task with the cooking schedule for editing
                                    const taskWithSchedule = {
                                      ...relatedTask,
                                      cookingSchedule: schedule
                                    };
                                    openProductEditor(taskWithSchedule);
                                  }
                                }}
                                className="gap-1"
                              >
                                <Edit3 className="h-3 w-3" />
                                Editar Ingredientes
                              </Button>
                            </div>
                            <div className="space-y-2">
                              {schedule.ingredients.map((ingredient) => (
                                <div key={ingredient.id} className="flex items-center justify-between p-2 border rounded">
                                  <div className="flex items-center gap-3">
                                    <Checkbox
                                      checked={ingredient.isPurchased}
                                      onCheckedChange={(checked) => 
                                        handleIngredientUpdate(schedule.id, ingredient.id, { isPurchased: checked as boolean })
                                      }
                                    />
                                    <div>
                                      <span className="font-medium">{ingredient.productName}</span>
                                      <div className="text-sm text-slate-600">
                                        {ingredient.quantity} {ingredient.unit}
                                      </div>
                                      {ingredient.notes && (
                                        <div className="text-xs text-slate-500">{ingredient.notes}</div>
                                      )}
                                    </div>
                                  </div>
                                  <div className="flex items-center gap-2">
                                    <Button
                                      size="sm"
                                      variant="outline"
                                      onClick={() => {
                                        const newQuantity = ingredient.quantity + 1;
                                        handleIngredientUpdate(schedule.id, ingredient.id, { quantity: newQuantity });
                                      }}
                                    >
                                      <Plus className="h-3 w-3" />
                                    </Button>
                                    <span className="text-sm w-12 text-center">{ingredient.quantity}</span>
                                    <Button
                                      size="sm"
                                      variant="outline"
                                      onClick={() => {
                                        const newQuantity = Math.max(0, ingredient.quantity - 1);
                                        handleIngredientUpdate(schedule.id, ingredient.id, { quantity: newQuantity });
                                      }}
                                    >
                                      <Minus className="h-3 w-3" />
                                    </Button>
                                  </div>
                                </div>
                              ))}
                            </div>
                          </div>
                        </CardContent>
                      </Card>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>
          </TabsContent>
        )}
      </Tabs>

      {/* Product Editing Dialog */}
      <Dialog open={isProductDialogOpen} onOpenChange={setIsProductDialogOpen}>
        <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle>Editar Productos/Ingredientes</DialogTitle>
            <DialogDescription>
              Modifica los productos requeridos para esta tarea. Puedes agregar, editar o eliminar ingredientes según sea necesario.
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h4 className="font-medium">Lista de Productos</h4>
              <Button onClick={addNewProduct} size="sm" className="gap-2">
                <Plus className="h-4 w-4" />
                Agregar Producto
              </Button>
            </div>

            <div className="space-y-3">
              {editingProducts.map((product, index) => (
                <Card key={product.id || index} className="p-4">
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                    {/* Product Name */}
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Nombre del Producto</label>
                      <div className="relative">
                        <Input
                          value={product.productName}
                          onChange={(e) => {
                            updateEditingProduct(index, { productName: e.target.value });
                            setShowProductSuggestions(index);
                          }}
                          onFocus={() => setShowProductSuggestions(index)}
                          onBlur={() => setTimeout(() => setShowProductSuggestions(null), 200)}
                          placeholder="Nombre del producto o buscar..."
                        />
                        {products && products.length > 0 && product.productName && showProductSuggestions === index && (
                          <div className="absolute z-10 w-full bg-white border border-gray-200 rounded-md shadow-lg mt-1">
                            {products
                              .filter(p => p.name.toLowerCase().includes(product.productName.toLowerCase()) && p.isActive)
                              .slice(0, 5)
                              .map((p) => (
                                <div
                                  key={p.id}
                                  className="px-3 py-2 hover:bg-gray-100 cursor-pointer text-sm"
                                  onClick={() => {
                                    updateEditingProduct(index, {
                                      productId: p.id,
                                      productName: p.name,
                                      category: p.category,
                                      unit: p.unit
                                    });
                                    setShowProductSuggestions(null);
                                  }}
                                >
                                  <div className="font-medium">{p.name}</div>
                                  <div className="text-gray-500 text-xs">{p.category} • {p.unit}</div>
                                </div>
                              ))
                            }
                          </div>
                        )}
                      </div>
                    </div>

                    {/* Quantity */}
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Cantidad</label>
                      <Input
                        type="number"
                        min="0"
                        step="0.1"
                        value={product.quantity}
                        onChange={(e) => updateEditingProduct(index, { quantity: parseFloat(e.target.value) || 0 })}
                        placeholder="Cantidad"
                      />
                    </div>

                    {/* Unit */}
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Unidad</label>
                      <Select
                        value={product.unit}
                        onValueChange={(value) => updateEditingProduct(index, { unit: value })}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Seleccionar unidad" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="kg">Kilogramos (kg)</SelectItem>
                          <SelectItem value="g">Gramos (g)</SelectItem>
                          <SelectItem value="l">Litros (l)</SelectItem>
                          <SelectItem value="ml">Mililitros (ml)</SelectItem>
                          <SelectItem value="unidades">Unidades</SelectItem>
                          <SelectItem value="piezas">Piezas</SelectItem>
                          <SelectItem value="cajas">Cajas</SelectItem>
                          <SelectItem value="paquetes">Paquetes</SelectItem>
                          <SelectItem value="botellas">Botellas</SelectItem>
                          <SelectItem value="latas">Latas</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>

                    {/* Category */}
                    <div className="space-y-2">
                      <label className="text-sm font-medium">Categoría</label>
                      <Select
                        value={product.category}
                        onValueChange={(value) => updateEditingProduct(index, { category: value })}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Seleccionar categoría" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="carnes">Carnes</SelectItem>
                          <SelectItem value="verduras">Verduras</SelectItem>
                          <SelectItem value="frutas">Frutas</SelectItem>
                          <SelectItem value="lacteos">Lácteos</SelectItem>
                          <SelectItem value="cereales">Cereales</SelectItem>
                          <SelectItem value="condimentos">Condimentos</SelectItem>
                          <SelectItem value="bebidas">Bebidas</SelectItem>
                          <SelectItem value="panaderia">Panadería</SelectItem>
                          <SelectItem value="congelados">Congelados</SelectItem>
                          <SelectItem value="otros">Otros</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                  </div>

                  {/* Notes */}
                  <div className="mt-4 space-y-2">
                    <label className="text-sm font-medium">Notas/Instrucciones</label>
                    <Textarea
                      value={product.notes || ''}
                      onChange={(e) => updateEditingProduct(index, { notes: e.target.value })}
                      placeholder="Notas especiales, instrucciones de preparación, etc."
                      rows={2}
                    />
                  </div>

                  {/* Actions */}
                  <div className="mt-4 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <Checkbox
                        checked={product.isPurchased}
                        onCheckedChange={(checked) => updateEditingProduct(index, { isPurchased: checked as boolean })}
                      />
                      <span className="text-sm">Comprado/Obtenido</span>
                    </div>
                    <Button
                      variant="destructive"
                      size="sm"
                      onClick={() => removeEditingProduct(index)}
                      className="gap-1"
                    >
                      <Trash2 className="h-3 w-3" />
                      Eliminar
                    </Button>
                  </div>
                </Card>
              ))}

              {editingProducts.length === 0 && (
                <Card className="p-8 text-center">
                  <div className="text-gray-400 mb-2">
                    <ShoppingCart className="h-12 w-12 mx-auto mb-2" />
                    <p>No hay productos agregados</p>
                    <p className="text-sm">Haz clic en "Agregar Producto" para comenzar</p>
                  </div>
                </Card>
              )}
            </div>
          </div>

          <DialogFooter className="gap-2">
            <Button variant="outline" onClick={closeProductEditor}>
              Cancelar
            </Button>
            <Button onClick={saveProductChanges} className="gap-2">
              <Save className="h-4 w-4" />
              Guardar Cambios
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default TaskDashboard; 