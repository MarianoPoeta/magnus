# 🎉 Party Budget Bliss - AI Context Index

## 📊 Application Overview
**Purpose**: Event management system for party/event planning companies
**Tech Stack**: React + TypeScript + Vite + Tailwind CSS + Zustand
**Architecture**: Frontend SPA with RESTful API integration ready
**Domain**: Event planning, budget management, task automation, workflow coordination

## 👥 User Roles & Workflows [[memory:2218056]]
1. **Salesperson**: Creates budgets → presents to clients → converts to "Reserva"
2. **Logistics Coordinator**: Weekly planning → purchases supplies → delivers to venues
3. **Cook**: Meal preparation → cooking schedules → ingredient management
4. **Admin**: System configuration → user management → global oversight

**Workflow**: Salesperson creates budget → Client approves → Status: "Reserva" → Auto-generates tasks for logistics & cook → Weekly planning & execution

## 🏗️ Core Architecture

### State Management (Zustand)
- **Location**: `src/store.ts`
- **Pattern**: Single store with typed actions
- **Key States**: users, budgets, tasks, activities, accommodations, menus, products, notifications

### Data Models
```typescript
// Core Types
Budget: client info + event details + selected items + status + amounts
Task: workflow automation + dependencies + role assignment + scheduling
User: role-based access + permissions
Activity/Menu/Accommodation/Product: templates for budget creation
```

### Service Layer
- **API Integration**: `src/services/api.ts` - RESTful endpoints ready
- **Workflow Automation**: `src/services/workflowAutomation.ts` - Auto task generation
- **Task Scheduling**: `src/services/taskScheduler.ts` - Smart scheduling with dependencies
- **Shopping Lists**: `src/services/shoppingListService.ts` - Consolidated purchasing
- **Cooking Schedules**: `src/services/cookingScheduleService.ts` - Meal timing & ingredients

## 🚀 Key Features Implemented

### Phase 1: Workflow Automation ✅
- **Auto Task Generation**: Budget status "reserva" → creates logistics + cook tasks
- **Task Dependencies**: Smart scheduling with blocking/dependency system
- **Role Assignment**: Automatic task distribution by role

### Phase 2: Weekly Planning ✅ 
- **Consolidated Shopping**: Multi-budget product consolidation
- **Weekly Dashboard**: `/weekly-planning` for logistics coordinators
- **Event Timeline**: Week view with all events and tasks

### Phase 3: Enhanced Task Management ✅
- **Task Dashboard**: `/tasks` with role-specific views
- **Shopping Integration**: Logistics can check off purchased items
- **Cooking Schedules**: Cook can modify ingredients and view timing
- **Progress Tracking**: Real-time status updates

## 🗂️ File Structure Summary

### Pages (`src/pages/`)
- `Dashboard.tsx` - Role-specific main dashboard
- `EnhancedBudgets.tsx` - Budget CRUD with advanced table
- `WeeklyPlanning.tsx` - Logistics weekly planning dashboard
- `TaskDashboard.tsx` - Role-specific task management
- `Activities.tsx`, `Accommodations.tsx`, `Menus.tsx`, `Products.tsx` - Resource management

### Components (`src/components/`)
- **Budget**: `budget/` - Budget creation wizard, forms, calculations
- **UI**: `ui/` - Reusable components (shadcn/ui based)
- **Layout**: `Layout.tsx`, `AppSidebar.tsx` - Navigation & structure

### Types (`src/types/`)
- **Core**: `Budget.ts`, `Task.ts`, `User.ts`, `Activity.ts`, `Menu.ts`, `Product.ts`
- **Enhanced**: `EnhancedBudget.ts` - Complex budget with calculations
- **Forms**: `Forms.ts` - Form validation and types

## 📱 Navigation & Routes

### Route Structure
```typescript
// Public
/login - Authentication

// Role-Based (all require auth)
/ or /dashboard - Role-specific dashboard
/budgets - Budget management (sales/admin)
/weekly-planning - Weekly planning (logistics)
/tasks - Task management (logistics/cook)
/activities, /accommodations, /menus, /products - Resource management (admin)
/clients - Client management (sales/admin)
```

### Role-Based Sidebar Navigation
- **Admin**: Panel Principal, Presupuestos, Clientes, Configuración
- **Sales**: Panel Principal, Crear Presupuestos, Gestión de Clientes  
- **Logistics**: Panel Principal, Planificación Semanal, Mis Tareas, Eventos Activos
- **Cook**: Panel Principal, Mis Tareas de Cocina, Eventos del Día

## 🔄 Workflow Automation Details

### Trigger System
```typescript
// When budget status changes to "reserva"
WorkflowAutomation.generateTasksFromReserva(budget)
→ Creates logistics tasks (shopping, delivery, setup)
→ Creates cook tasks (preparation, cooking, cleanup)
→ Auto-schedules with dependencies
→ Assigns to appropriate roles
```

### Task Types & Scheduling
- **Shopping** (Logistics): 3-7 days before event
- **Preparation** (Cook): 1-2 days before event  
- **Delivery** (Logistics): Day of event
- **Cooking** (Cook): Day of event with precise timing
- **Setup/Cleanup**: Event day coordination

### Smart Features
- **Product Consolidation**: Multiple budgets same week → combined shopping list
- **Ingredient Management**: Cook can modify quantities, add items
- **Progress Tracking**: Real-time purchase status, cooking progress
- **Dependencies**: Tasks auto-block/unblock based on completion

## 🎯 Current State & Integration Points

### Recently Implemented
1. **Enhanced Task System**: Dependencies, auto-scheduling, role filtering
2. **Shopping Lists**: Weekly consolidation, progress tracking, category grouping
3. **Cooking Schedules**: Ingredient management, timing optimization
4. **Role-Based Navigation**: Optimized sidebar for each user role
5. **Real-Time Updates**: Task status changes, shopping progress

### API Integration Ready
- **Endpoints**: Full RESTful API structure defined in `api.ts`
- **Error Handling**: Centralized error management
- **Authentication**: JWT token system ready
- **Pagination**: Built-in support for large datasets

### Key Integration Points
- **Budget → Tasks**: Automatic workflow trigger on status change
- **Tasks → Shopping**: Product requirements auto-generated
- **Shopping → Progress**: Real-time purchase tracking
- **Cooking → Timing**: Smart scheduling based on guest count
- **Notifications**: System-wide alerts for workflow events

## 🛠️ Development Patterns

### Component Patterns
- **Hooks**: Custom hooks in `src/hooks/` for reusable logic
- **Services**: Business logic separated from components
- **Types**: Comprehensive TypeScript throughout
- **Error Boundaries**: Graceful error handling

### Performance Optimizations
- **Lazy Loading**: All routes lazy-loaded
- **Memoization**: Performance hooks for heavy calculations
- **Efficient Re-renders**: Zustand optimizations

### UI/UX Standards
- **Design System**: Tailwind + shadcn/ui components
- **Responsive**: Mobile-first approach
- **Accessibility**: ARIA labels, keyboard navigation
- **Loading States**: Consistent loading indicators

## 🔍 Quick Reference

### Most Important Files
1. `src/store.ts` - Application state management
2. `src/services/workflowAutomation.ts` - Core business logic
3. `src/pages/TaskDashboard.tsx` - Main workflow interface
4. `src/pages/WeeklyPlanning.tsx` - Logistics coordination
5. `src/App.tsx` - Route configuration
6. `src/components/AppSidebar.tsx` - Role-based navigation

### Key Constants
- **Task Types**: shopping, reservation, delivery, cooking, preparation, setup, cleanup
- **Task Status**: todo, in_progress, done, blocked
- **User Roles**: admin, sales, logistics, cook
- **Budget Status**: draft, pending, approved, reserva, rejected, completed

### Business Rules
- Budget "reserva" status triggers automatic task generation
- Logistics handles all procurement and delivery
- Cook manages meal preparation and ingredient modifications
- Tasks have smart dependencies preventing workflow conflicts
- Weekly planning consolidates multiple events efficiently
