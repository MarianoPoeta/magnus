import { Budget } from '../types/Budget';
import { Task, ProductRequirement, CookingSchedule } from '../types/Task';
import { Activity } from '../types/Activity';
import { Menu } from '../types/Menu';
import { Product } from '../types/Product';
import { Accommodation } from '../types/Accommodation';
import { Client } from '../types/Client';

// Mock data for July 7-14, 2025
const MOCK_DATE_RANGE = {
  start: '2025-07-07',
  end: '2025-07-14'
};

export const mockClients: Client[] = [
  {
    id: 'client-1',
    name: 'Carlos Mendoza',
    email: 'carlos.mendoza@email.com',
    phone: '+34 666 123 456',
    address: 'Calle Mayor 15, Madrid',
    company: 'Mendoza & Associates',
    isActive: true,
    createdAt: '2025-06-15T10:00:00Z',
    updatedAt: '2025-06-15T10:00:00Z'
  },
  {
    id: 'client-2',
    name: 'María González',
    email: 'maria.gonzalez@corporativo.es',
    phone: '+34 677 789 123',
    address: 'Avenida de la Castellana 200, Madrid',
    company: 'TechCorp España',
    isActive: true,
    createdAt: '2025-06-10T14:30:00Z',
    updatedAt: '2025-06-10T14:30:00Z'
  },
  {
    id: 'client-3',
    name: 'Roberto Silva',
    email: 'roberto.silva@email.com',
    phone: '+34 688 456 789',
    address: 'Plaza del Sol 8, Valencia',
    isActive: true,
    createdAt: '2025-06-20T09:15:00Z',
    updatedAt: '2025-06-20T09:15:00Z'
  },
  {
    id: 'client-4',
    name: 'Ana Martínez',
    email: 'ana.martinez@startup.com',
    phone: '+34 699 111 222',
    address: 'Calle Serrano 45, Madrid',
    company: 'InnovaStartup',
    isActive: true,
    createdAt: '2025-06-25T16:00:00Z',
    updatedAt: '2025-06-25T16:00:00Z'
  }
];

export const mockProducts: Product[] = [
  // Carnes
  {
    id: 'prod-1',
    name: 'Costillas de Ternera Premium',
    category: 'carnes',
    cost: 28.50,
    unit: 'kg',
    description: 'Costillas de ternera de primera calidad para asado',
    estimatedPrice: 32.00,
    supplier: 'Carnicería Premium',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-2',
    name: 'Pollo Entero Ecológico',
    category: 'carnes',
    cost: 12.80,
    unit: 'unidades',
    description: 'Pollo entero de granja ecológica',
    estimatedPrice: 15.00,
    supplier: 'Carnicería Premium',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-3',
    name: 'Chorizo Ibérico',
    category: 'carnes',
    cost: 18.90,
    unit: 'kg',
    description: 'Chorizo ibérico curado artesanal',
    estimatedPrice: 22.00,
    supplier: 'Carnicería Premium',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },

  // Verduras
  {
    id: 'prod-4',
    name: 'Tomates Cherry Ecológicos',
    category: 'verduras',
    cost: 4.50,
    unit: 'kg',
    description: 'Tomates cherry de cultivo ecológico',
    estimatedPrice: 6.00,
    supplier: 'Verduras Frescas SA',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-5',
    name: 'Lechuga Iceberg',
    category: 'verduras',
    cost: 1.20,
    unit: 'unidades',
    description: 'Lechuga iceberg fresca',
    estimatedPrice: 2.00,
    supplier: 'Verduras Frescas SA',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-6',
    name: 'Pimientos Rojos',
    category: 'verduras',
    cost: 3.20,
    unit: 'kg',
    description: 'Pimientos rojos dulces',
    estimatedPrice: 4.50,
    supplier: 'Verduras Frescas SA',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },

  // Bebidas
  {
    id: 'prod-7',
    name: 'Cerveza Artesanal IPA',
    category: 'bebidas',
    cost: 2.80,
    unit: 'botellas',
    description: 'Cerveza artesanal IPA 33cl',
    estimatedPrice: 4.50,
    supplier: 'Distribuidora Central',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-8',
    name: 'Vino Tinto Reserva',
    category: 'bebidas',
    cost: 8.50,
    unit: 'botellas',
    description: 'Vino tinto reserva D.O. Rioja',
    estimatedPrice: 15.00,
    supplier: 'Distribuidora Central',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-9',
    name: 'Agua Mineral Premium',
    category: 'bebidas',
    cost: 0.80,
    unit: 'botellas',
    description: 'Agua mineral premium 1.5L',
    estimatedPrice: 1.50,
    supplier: 'Distribuidora Central',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },

  // Lácteos
  {
    id: 'prod-10',
    name: 'Queso Manchego Curado',
    category: 'lacteos',
    cost: 15.80,
    unit: 'kg',
    description: 'Queso manchego curado D.O.',
    estimatedPrice: 22.00,
    supplier: 'Lácteos del Valle',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-11',
    name: 'Mozzarella Fresca',
    category: 'lacteos',
    cost: 6.50,
    unit: 'kg',
    description: 'Mozzarella fresca italiana',
    estimatedPrice: 9.00,
    supplier: 'Lácteos del Valle',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },

  // Panadería
  {
    id: 'prod-12',
    name: 'Pan Artesanal Rústico',
    category: 'panaderia',
    cost: 2.20,
    unit: 'unidades',
    description: 'Pan artesanal rústico de masa madre',
    estimatedPrice: 3.50,
    supplier: 'Panadería Artesanal',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  },
  {
    id: 'prod-13',
    name: 'Focaccia con Hierbas',
    category: 'panaderia',
    cost: 3.80,
    unit: 'unidades',
    description: 'Focaccia italiana con hierbas aromáticas',
    estimatedPrice: 5.50,
    supplier: 'Panadería Artesanal',
    isActive: true,
    createdAt: '2025-06-01T00:00:00Z',
    updatedAt: '2025-06-01T00:00:00Z'
  }
];

export const mockBudgets: Budget[] = [
  {
    id: 'budget-1',
    name: 'Despedida de Soltero Carlos',
    clientName: 'Carlos Mendoza',
    eventType: 'Despedida de Soltero',
    activities: ['Go-Karting', 'Parrillada Nocturna', 'Bar Tour'],
    status: 'reserva',
    eventDate: '2025-07-08',
    totalAmount: 2800,
    mealsAmount: 1200,
    activitiesAmount: 1000,
    transportAmount: 300,
    accommodationAmount: 300,
    guestCount: 12,
    createdAt: '2025-06-20T10:00:00Z',
    templateId: 'template-1'
  },
  {
    id: 'budget-2',
    name: 'Evento Corporativo TechCorp',
    clientName: 'María González',
    eventType: 'Evento Corporativo',
    activities: ['Team Building', 'Cena de Gala', 'Presentación'],
    status: 'reserva',
    eventDate: '2025-07-10',
    totalAmount: 4500,
    mealsAmount: 2000,
    activitiesAmount: 1500,
    transportAmount: 500,
    accommodationAmount: 500,
    guestCount: 25,
    createdAt: '2025-06-25T14:30:00Z',
    templateId: 'template-2'
  },
  {
    id: 'budget-3',
    name: 'Cumpleaños Roberto Silva',
    clientName: 'Roberto Silva',
    eventType: 'Cumpleaños',
    activities: ['Paella Valenciana', 'Música en Vivo', 'Baile'],
    status: 'reserva',
    eventDate: '2025-07-12',
    totalAmount: 1800,
    mealsAmount: 800,
    activitiesAmount: 600,
    transportAmount: 200,
    accommodationAmount: 200,
    guestCount: 15,
    createdAt: '2025-06-28T16:45:00Z',
    templateId: 'template-3'
  },
  {
    id: 'budget-4',
    name: 'Celebración Startup InnovaStartup',
    clientName: 'Ana Martínez',
    eventType: 'Celebración Empresarial',
    activities: ['Cocktail Reception', 'Networking Dinner', 'Presentación Producto'],
    status: 'reserva',
    eventDate: '2025-07-14',
    totalAmount: 3200,
    mealsAmount: 1400,
    activitiesAmount: 1200,
    transportAmount: 300,
    accommodationAmount: 300,
    guestCount: 20,
    createdAt: '2025-07-01T11:20:00Z',
    templateId: 'template-4'
  }
];

export const mockTasks: Task[] = [
  // Carlos Mendoza - Despedida de Soltero (July 8)
  {
    id: 'task-1',
    type: 'shopping',
    description: 'Comprar ingredientes para parrillada - Despedida Carlos',
    relatedBudgetId: 'budget-1',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-06',
    estimatedDuration: 3,
    status: 'todo',
    priority: 'high',
    dependencies: [],
    autoScheduled: true,
    productRequirements: [
      {
        id: 'req-1',
        productId: 'prod-1',
        productName: 'Costillas de Ternera Premium',
        quantity: 4,
        unit: 'kg',
        category: 'carnes',
        isPurchased: false
      },
      {
        id: 'req-2',
        productId: 'prod-3',
        productName: 'Chorizo Ibérico',
        quantity: 2,
        unit: 'kg',
        category: 'carnes',
        isPurchased: false
      },
      {
        id: 'req-3',
        productId: 'prod-7',
        productName: 'Cerveza Artesanal IPA',
        quantity: 24,
        unit: 'botellas',
        category: 'bebidas',
        isPurchased: false
      }
    ],
    createdAt: '2025-06-20T10:30:00Z',
    updatedAt: '2025-06-20T10:30:00Z'
  },
  {
    id: 'task-2',
    type: 'cooking',
    description: 'Preparar parrillada para 12 personas - Despedida Carlos',
    relatedBudgetId: 'budget-1',
    assignedToRole: 'cook',
    assignedTo: 'cook-user',
    dueDate: '2025-07-08',
    estimatedDuration: 4,
    status: 'todo',
    priority: 'high',
    dependencies: [{ id: 'dep-1', dependsOnTaskId: 'task-1', dependencyType: 'requires' }],
    autoScheduled: true,
    cookingSchedule: {
      id: 'cook-schedule-1',
      eventDate: '2025-07-08',
      cookingTime: '19:00',
      mealType: 'dinner',
      menuName: 'Parrillada Premium',
      guestCount: 12,
      ingredients: [
        {
          id: 'ing-1',
          productId: 'prod-1',
          productName: 'Costillas de Ternera Premium',
          quantity: 4,
          unit: 'kg',
          category: 'carnes',
          isPurchased: false,
          notes: 'Marinar 4 horas antes de cocinar'
        },
        {
          id: 'ing-2',
          productId: 'prod-3',
          productName: 'Chorizo Ibérico',
          quantity: 2,
          unit: 'kg',
          category: 'carnes',
          isPurchased: false,
          notes: 'Cortar en rodajas de 1cm'
        }
      ],
      specialInstructions: 'Cocinar a fuego medio-alto. Servir con chimichurri casero.'
    },
    createdAt: '2025-06-20T10:45:00Z',
    updatedAt: '2025-06-20T10:45:00Z'
  },
  {
    id: 'task-3',
    type: 'delivery',
    description: 'Entregar equipamiento para parrillada - Despedida Carlos',
    relatedBudgetId: 'budget-1',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-08',
    estimatedDuration: 2,
    status: 'todo',
    priority: 'medium',
    dependencies: [],
    autoScheduled: true,
    notes: 'Incluir parrilla portátil, carbón, y utensilios',
    createdAt: '2025-06-20T11:00:00Z',
    updatedAt: '2025-06-20T11:00:00Z'
  },

  // María González - Evento Corporativo (July 10)
  {
    id: 'task-4',
    type: 'shopping',
    description: 'Comprar ingredientes cena gala - TechCorp',
    relatedBudgetId: 'budget-2',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-08',
    estimatedDuration: 4,
    status: 'todo',
    priority: 'high',
    dependencies: [],
    autoScheduled: true,
    productRequirements: [
      {
        id: 'req-4',
        productId: 'prod-2',
        productName: 'Pollo Entero Ecológico',
        quantity: 8,
        unit: 'unidades',
        category: 'carnes',
        isPurchased: false
      },
      {
        id: 'req-5',
        productId: 'prod-8',
        productName: 'Vino Tinto Reserva',
        quantity: 12,
        unit: 'botellas',
        category: 'bebidas',
        isPurchased: false
      },
      {
        id: 'req-6',
        productId: 'prod-10',
        productName: 'Queso Manchego Curado',
        quantity: 2,
        unit: 'kg',
        category: 'lacteos',
        isPurchased: false
      }
    ],
    createdAt: '2025-06-25T15:00:00Z',
    updatedAt: '2025-06-25T15:00:00Z'
  },
  {
    id: 'task-5',
    type: 'cooking',
    description: 'Preparar cena de gala para 25 personas - TechCorp',
    relatedBudgetId: 'budget-2',
    assignedToRole: 'cook',
    assignedTo: 'cook-user',
    dueDate: '2025-07-10',
    estimatedDuration: 6,
    status: 'todo',
    priority: 'high',
    dependencies: [{ id: 'dep-2', dependsOnTaskId: 'task-4', dependencyType: 'requires' }],
    autoScheduled: true,
    cookingSchedule: {
      id: 'cook-schedule-2',
      eventDate: '2025-07-10',
      cookingTime: '18:00',
      mealType: 'dinner',
      menuName: 'Cena de Gala Corporativa',
      guestCount: 25,
      ingredients: [
        {
          id: 'ing-3',
          productId: 'prod-2',
          productName: 'Pollo Entero Ecológico',
          quantity: 8,
          unit: 'unidades',
          category: 'carnes',
          isPurchased: false,
          notes: 'Deshuesar y marinar con hierbas mediterráneas'
        },
        {
          id: 'ing-4',
          productId: 'prod-10',
          productName: 'Queso Manchego Curado',
          quantity: 2,
          unit: 'kg',
          category: 'lacteos',
          isPurchased: false,
          notes: 'Cortar en láminas finas para entrada'
        }
      ],
      specialInstructions: 'Presentación elegante. Servir con guarnición de temporada.'
    },
    createdAt: '2025-06-25T15:15:00Z',
    updatedAt: '2025-06-25T15:15:00Z'
  },

  // Roberto Silva - Cumpleaños (July 12)
  {
    id: 'task-6',
    type: 'shopping',
    description: 'Comprar ingredientes paella valenciana - Cumpleaños Roberto',
    relatedBudgetId: 'budget-3',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-10',
    estimatedDuration: 3,
    status: 'todo',
    priority: 'medium',
    dependencies: [],
    autoScheduled: true,
    productRequirements: [
      {
        id: 'req-7',
        productId: 'prod-2',
        productName: 'Pollo Entero Ecológico',
        quantity: 4,
        unit: 'unidades',
        category: 'carnes',
        isPurchased: false
      },
      {
        id: 'req-8',
        productId: 'prod-4',
        productName: 'Tomates Cherry Ecológicos',
        quantity: 2,
        unit: 'kg',
        category: 'verduras',
        isPurchased: false
      },
      {
        id: 'req-9',
        productId: 'prod-6',
        productName: 'Pimientos Rojos',
        quantity: 1.5,
        unit: 'kg',
        category: 'verduras',
        isPurchased: false
      }
    ],
    createdAt: '2025-06-28T17:00:00Z',
    updatedAt: '2025-06-28T17:00:00Z'
  },
  {
    id: 'task-7',
    type: 'cooking',
    description: 'Preparar paella valenciana para 15 personas - Cumpleaños Roberto',
    relatedBudgetId: 'budget-3',
    assignedToRole: 'cook',
    assignedTo: 'cook-user',
    dueDate: '2025-07-12',
    estimatedDuration: 3,
    status: 'todo',
    priority: 'medium',
    dependencies: [{ id: 'dep-3', dependsOnTaskId: 'task-6', dependencyType: 'requires' }],
    autoScheduled: true,
    cookingSchedule: {
      id: 'cook-schedule-3',
      eventDate: '2025-07-12',
      cookingTime: '13:00',
      mealType: 'lunch',
      menuName: 'Paella Valenciana Tradicional',
      guestCount: 15,
      ingredients: [
        {
          id: 'ing-5',
          productId: 'prod-2',
          productName: 'Pollo Entero Ecológico',
          quantity: 4,
          unit: 'unidades',
          category: 'carnes',
          isPurchased: false,
          notes: 'Trocear en piezas medianas'
        },
        {
          id: 'ing-6',
          productId: 'prod-4',
          productName: 'Tomates Cherry Ecológicos',
          quantity: 2,
          unit: 'kg',
          category: 'verduras',
          isPurchased: false,
          notes: 'Rallar para el sofrito'
        }
      ],
      specialInstructions: 'Paella tradicional valenciana con garrofón y judía verde. Cocinar en paellera de 60cm.'
    },
    createdAt: '2025-06-28T17:15:00Z',
    updatedAt: '2025-06-28T17:15:00Z'
  },

  // Ana Martínez - Celebración Startup (July 14)
  {
    id: 'task-8',
    type: 'shopping',
    description: 'Comprar ingredientes cocktail reception - InnovaStartup',
    relatedBudgetId: 'budget-4',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-12',
    estimatedDuration: 3,
    status: 'todo',
    priority: 'medium',
    dependencies: [],
    autoScheduled: true,
    productRequirements: [
      {
        id: 'req-10',
        productId: 'prod-11',
        productName: 'Mozzarella Fresca',
        quantity: 2,
        unit: 'kg',
        category: 'lacteos',
        isPurchased: false
      },
      {
        id: 'req-11',
        productId: 'prod-12',
        productName: 'Pan Artesanal Rústico',
        quantity: 8,
        unit: 'unidades',
        category: 'panaderia',
        isPurchased: false
      },
      {
        id: 'req-12',
        productId: 'prod-8',
        productName: 'Vino Tinto Reserva',
        quantity: 8,
        unit: 'botellas',
        category: 'bebidas',
        isPurchased: false
      }
    ],
    createdAt: '2025-07-01T11:45:00Z',
    updatedAt: '2025-07-01T11:45:00Z'
  },
  {
    id: 'task-9',
    type: 'cooking',
    description: 'Preparar cocktail reception para 20 personas - InnovaStartup',
    relatedBudgetId: 'budget-4',
    assignedToRole: 'cook',
    assignedTo: 'cook-user',
    dueDate: '2025-07-14',
    estimatedDuration: 4,
    status: 'todo',
    priority: 'medium',
    dependencies: [{ id: 'dep-4', dependsOnTaskId: 'task-8', dependencyType: 'requires' }],
    autoScheduled: true,
    cookingSchedule: {
      id: 'cook-schedule-4',
      eventDate: '2025-07-14',
      cookingTime: '17:00',
      mealType: 'snack',
      menuName: 'Cocktail Reception Gourmet',
      guestCount: 20,
      ingredients: [
        {
          id: 'ing-7',
          productId: 'prod-11',
          productName: 'Mozzarella Fresca',
          quantity: 2,
          unit: 'kg',
          category: 'lacteos',
          isPurchased: false,
          notes: 'Cortar en bolitas para canapés'
        },
        {
          id: 'ing-8',
          productId: 'prod-12',
          productName: 'Pan Artesanal Rústico',
          quantity: 8,
          unit: 'unidades',
          category: 'panaderia',
          isPurchased: false,
          notes: 'Tostar y cortar en rebanadas finas'
        }
      ],
      specialInstructions: 'Preparar variedad de canapés gourmet. Presentación moderna y elegante.'
    },
    createdAt: '2025-07-01T12:00:00Z',
    updatedAt: '2025-07-01T12:00:00Z'
  },

  // Additional logistics tasks
  {
    id: 'task-10',
    type: 'setup',
    description: 'Montaje decoración y equipamiento - TechCorp',
    relatedBudgetId: 'budget-2',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-10',
    estimatedDuration: 3,
    status: 'todo',
    priority: 'high',
    dependencies: [],
    autoScheduled: true,
    notes: 'Incluir proyector, pantalla, sistema de sonido y decoración corporativa',
    createdAt: '2025-06-25T15:30:00Z',
    updatedAt: '2025-06-25T15:30:00Z'
  },
  {
    id: 'task-11',
    type: 'delivery',
    description: 'Entrega equipamiento paella - Cumpleaños Roberto',
    relatedBudgetId: 'budget-3',
    assignedToRole: 'logistics',
    assignedTo: 'logistics-user',
    dueDate: '2025-07-12',
    estimatedDuration: 1,
    status: 'todo',
    priority: 'medium',
    dependencies: [],
    autoScheduled: true,
    notes: 'Paellera de 60cm, gas butano, leña para fuego',
    createdAt: '2025-06-28T17:30:00Z',
    updatedAt: '2025-06-28T17:30:00Z'
  }
];

export const mockCookingSchedules: CookingSchedule[] = [
  {
    id: 'cook-schedule-1',
    eventDate: '2025-07-08',
    cookingTime: '19:00',
    mealType: 'dinner',
    menuName: 'Parrillada Premium - Despedida Carlos',
    guestCount: 12,
    ingredients: [
      {
        id: 'ing-1',
        productId: 'prod-1',
        productName: 'Costillas de Ternera Premium',
        quantity: 4,
        unit: 'kg',
        category: 'carnes',
        isPurchased: false,
        notes: 'Marinar 4 horas antes de cocinar'
      },
      {
        id: 'ing-2',
        productId: 'prod-3',
        productName: 'Chorizo Ibérico',
        quantity: 2,
        unit: 'kg',
        category: 'carnes',
        isPurchased: false,
        notes: 'Cortar en rodajas de 1cm'
      }
    ],
    specialInstructions: 'Cocinar a fuego medio-alto. Servir con chimichurri casero.'
  },
  {
    id: 'cook-schedule-2',
    eventDate: '2025-07-10',
    cookingTime: '18:00',
    mealType: 'dinner',
    menuName: 'Cena de Gala Corporativa - TechCorp',
    guestCount: 25,
    ingredients: [
      {
        id: 'ing-3',
        productId: 'prod-2',
        productName: 'Pollo Entero Ecológico',
        quantity: 8,
        unit: 'unidades',
        category: 'carnes',
        isPurchased: false,
        notes: 'Deshuesar y marinar con hierbas mediterráneas'
      },
      {
        id: 'ing-4',
        productId: 'prod-10',
        productName: 'Queso Manchego Curado',
        quantity: 2,
        unit: 'kg',
        category: 'lacteos',
        isPurchased: false,
        notes: 'Cortar en láminas finas para entrada'
      }
    ],
    specialInstructions: 'Presentación elegante. Servir con guarnición de temporada.'
  },
  {
    id: 'cook-schedule-3',
    eventDate: '2025-07-12',
    cookingTime: '13:00',
    mealType: 'lunch',
    menuName: 'Paella Valenciana Tradicional - Cumpleaños Roberto',
    guestCount: 15,
    ingredients: [
      {
        id: 'ing-5',
        productId: 'prod-2',
        productName: 'Pollo Entero Ecológico',
        quantity: 4,
        unit: 'unidades',
        category: 'carnes',
        isPurchased: false,
        notes: 'Trocear en piezas medianas'
      },
      {
        id: 'ing-6',
        productId: 'prod-4',
        productName: 'Tomates Cherry Ecológicos',
        quantity: 2,
        unit: 'kg',
        category: 'verduras',
        isPurchased: false,
        notes: 'Rallar para el sofrito'
      }
    ],
    specialInstructions: 'Paella tradicional valenciana con garrofón y judía verde. Cocinar en paellera de 60cm.'
  },
  {
    id: 'cook-schedule-4',
    eventDate: '2025-07-14',
    cookingTime: '17:00',
    mealType: 'snack',
    menuName: 'Cocktail Reception Gourmet - InnovaStartup',
    guestCount: 20,
    ingredients: [
      {
        id: 'ing-7',
        productId: 'prod-11',
        productName: 'Mozzarella Fresca',
        quantity: 2,
        unit: 'kg',
        category: 'lacteos',
        isPurchased: false,
        notes: 'Cortar en bolitas para canapés'
      },
      {
        id: 'ing-8',
        productId: 'prod-12',
        productName: 'Pan Artesanal Rústico',
        quantity: 8,
        unit: 'unidades',
        category: 'panaderia',
        isPurchased: false,
        notes: 'Tostar y cortar en rebanadas finas'
      }
    ],
    specialInstructions: 'Preparar variedad de canapés gourmet. Presentación moderna y elegante.'
  }
]; 