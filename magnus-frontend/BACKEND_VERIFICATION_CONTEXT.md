# 🔍 Backend Verification Context - Party Budget Bliss

## 🎯 Document Purpose
This document provides comprehensive context for AI agents to verify if the generated JHipster backend implementation fulfills all architectural, functional, and technical requirements for the Party Budget Bliss application.

## 📋 Application Overview

### **Core Purpose**
Sophisticated event management system with automated workflow generation, real-time task synchronization, and intelligent resource planning across multiple user roles.

### **Technology Stack Requirements**
- **Backend Framework**: Spring Boot (JHipster generated)
- **Database**: MySQL 8.0+ (Production) / H2 (Development)
- **Authentication**: JWT-based with role-based access control
- **Real-time Communication**: Spring WebSocket
- **Caching**: Redis for performance optimization
- **API Style**: RESTful with comprehensive CRUD operations

### **User Role System**
1. **ADMIN** - Full system access, configuration, user management
2. **SALES** - Budget creation, client management, proposal generation
3. **LOGISTICS** - Task management, shopping coordination, transportation
4. **COOK** - Cooking schedules, ingredient management, menu planning

## 🏗️ Core Entity Requirements

### **Primary Entities with Advanced Features**

#### **AppUser Entity**
```typescript
- Replaces JHipster's built-in User entity
- Fields: login, firstName, lastName, email, phone, role, isActive, profilePicture, preferences, lastLoginAt
- Relationships: Multiple named collections (createdBudgets, assignedBudgets, createdTasks, assignedTasks, etc.)
- Requirements: Proper role-based access control, profile management
```

#### **Budget Entity**
```typescript
- Core business entity that triggers workflow automation
- Fields: name, clientName, eventDate, eventLocation, guestCount, eventGender, totalAmount, status, workflowTriggered, version, conflictStatus
- Key Status Values: DRAFT, PENDING, APPROVED, RESERVA, REJECTED, COMPLETED, CANCELED
- Critical Trigger: Status change to "RESERVA" must auto-generate tasks
- Relationships: client, createdBy, assignedTo, template, weeklyPlan, budgetItems, payments, tasks, cookingSchedule
```

#### **Task Entity**
```typescript
- Generated automatically when Budget status = "RESERVA"
- Fields: title, description, type, priority, status, assignedToRole, dueDate, autoScheduled, isRecurring, parentTaskId, version, conflictStatus
- Task Types: SHOPPING, RESERVATION, DELIVERY, COOKING, PREPARATION, SETUP, CLEANUP, NEED
- Dependencies: TaskDependency entity with BLOCKS, REQUIRES, SUGGESTS types
- Role Assignment: Automatic assignment based on task type
```

#### **WeeklyPlan Entity**
```typescript
- Consolidates multiple budgets for logistics coordination
- Fields: weekStart, weekEnd, planName, status, totalBudgets, totalGuests, estimatedCost, actualCost, isConsolidated
- Status Values: DRAFT, IN_PROGRESS, COMPLETED, ARCHIVED
- Purpose: Groups budgets by week for shopping consolidation
```

#### **ShoppingItem Entity**
```typescript
- Consolidated shopping requirements across multiple budgets
- Fields: productName, totalQuantity, unit, category, budgetIds, clientNames, isPurchased, weekStart, weekEnd, supplier, isConsolidated, conflictStatus, version
- Business Logic: Automatic consolidation of requirements from multiple budgets
- Real-time Updates: Cook ingredient changes trigger shopping list regeneration
```

#### **CookingSchedule Entity**
```typescript
- Detailed cooking timeline and ingredient management
- Fields: eventDate, cookingTime, mealType, menuName, guestCount, specialInstructions, isCompleted, ingredientsReady, estimatedDuration, conflictStatus, version
- Real-time Sync: Ingredient modifications auto-update shopping lists
- Relationships: OneToOne with Task, OneToMany with CookingIngredient
```

#### **CookingIngredient Entity**
```typescript
- Individual ingredient requirements with modification tracking
- Fields: originalQuantity, modifiedQuantity, modifiedUnit, notes, addedByUser, isAvailable, lastModifiedBy, version
- Business Logic: Changes trigger shopping list recalculation
- Version Control: Track modifications for conflict resolution
```

### **Template System Entities**

#### **BudgetTemplate Entity**
```typescript
- Reusable templates for budget creation
- Fields: name, description, type, category, isActive, isSystem, configuration
- Template Types: MENU, ACTIVITY, TRANSPORT, ACCOMMODATION, BUDGET
- Integration: Used in budget creation wizard for rapid setup
```

#### **Template-Enabled Entities**
```typescript
- Menu, Activity, Transport, Accommodation entities must have "isTemplate" field
- Support both template mode and regular mode
- Version control for template modifications
```

### **Audit and Monitoring Entities**

#### **AuditLog Entity**
```typescript
- Comprehensive audit trail for all operations
- Fields: entityType, entityId, action, oldValue, newValue, fieldName, userId, userRole, timestamp, ipAddress, userAgent
- Action Types: CREATE, UPDATE, DELETE, STATUS_CHANGE, APPROVE, REJECT
- Requirements: Automatic logging for all CRUD operations
```

#### **ConflictResolution Entity**
```typescript
- Handles concurrent modification conflicts
- Fields: entityType, entityId, fieldName, localValue, remoteValue, resolvedValue, resolutionStrategy, isResolved
- Conflict Status: NONE, DETECTED, RESOLVED, ESCALATED
- Integration: Real-time conflict detection and resolution
```

#### **WorkflowTrigger Entity**
```typescript
- Configurable workflow automation rules
- Fields: triggerName, entityType, triggerCondition, actionType, actionConfiguration, isActive, executionOrder
- Purpose: Defines when and how workflows are triggered
- Business Logic: Budget status changes trigger task generation
```

## 🔄 Critical Workflow Requirements

### **Automatic Task Generation**
**Trigger**: Budget status changes to "RESERVA"
**Actions Required**:
1. Generate shopping tasks (2-7 days before event, assigned to LOGISTICS)
2. Generate cooking tasks (day of event, assigned to COOK)
3. Generate delivery/setup tasks (day of event, assigned to LOGISTICS)
4. Create task dependencies (shopping blocks cooking, etc.)
5. Calculate optimal timing based on event date
6. Send role-based notifications

### **Shopping List Consolidation**
**Business Logic**:
1. Group products by name, unit, category across multiple budgets
2. Calculate total quantities needed for the week
3. Track purchase progress with partial purchase support
4. Auto-update when cook modifies ingredient requirements
5. Generate consolidated shopping lists by week

### **Real-Time Synchronization Requirements**
**Cook ↔ Logistics Sync**:
1. Cook modifies cooking ingredient → Auto-update shopping list
2. Logistics purchases item → Notify cook of ingredient availability
3. Task status changes → Real-time updates across all users
4. Conflict detection for simultaneous modifications

## 📡 Required API Endpoints

### **Core CRUD Operations**
```
# Authentication
POST /api/authenticate
GET  /api/account

# Budget Management
GET    /api/budgets?page=0&size=20&status=RESERVA&sort=eventDate,desc
POST   /api/budgets
PUT    /api/budgets/{id}
PATCH  /api/budgets/{id}/status
DELETE /api/budgets/{id}

# Task Management
GET    /api/tasks?assignedToRole=LOGISTICS&status=TODO&sort=dueDate,asc
POST   /api/tasks
PUT    /api/tasks/{id}
PATCH  /api/tasks/{id}/status
DELETE /api/tasks/{id}

# Shopping Management
GET    /api/shopping-items?weekStart=2025-07-07&weekEnd=2025-07-13
POST   /api/shopping-items
PUT    /api/shopping-items/{id}
PATCH  /api/shopping-items/{id}/purchase
POST   /api/shopping-items/bulk-purchase

# Cooking Schedules
GET    /api/cooking-schedules?eventDate=2025-07-07
POST   /api/cooking-schedules
PUT    /api/cooking-schedules/{id}

# Templates
GET    /api/menus?type=LUNCH&isActive=true&isTemplate=true
GET    /api/activities?category=OUTDOOR&isActive=true
GET    /api/accommodations?type=VILLA&isActive=true
GET    /api/transports?vehicleType=BUS&isActive=true
```

### **Advanced Workflow Endpoints**
```
# Workflow Automation
POST   /api/workflows/trigger-task-generation/{budgetId}
POST   /api/workflows/consolidate-shopping/{weekStart}
POST   /api/workflows/sync-cooking-to-shopping/{scheduleId}

# Weekly Planning
GET    /api/weekly-plans?weekStart=2025-07-07
POST   /api/weekly-plans
PUT    /api/weekly-plans/{id}/consolidate

# Conflict Resolution
GET    /api/conflicts?entityType=CookingIngredient&isResolved=false
POST   /api/conflicts/{id}/resolve
```

### **Analytics and Reporting**
```
# Dashboard Analytics
GET    /api/analytics/dashboard-stats
GET    /api/analytics/budget-stats?startDate=2025-01-01&endDate=2025-12-31
GET    /api/analytics/task-completion-rates
GET    /api/analytics/shopping-progress/{weekStart}

# Role-specific Analytics
GET    /api/analytics/sales-performance
GET    /api/analytics/logistics-efficiency
GET    /api/analytics/cooking-schedule-adherence
```

## 🌐 WebSocket Requirements

### **Real-Time Event Types**
```
# Task Updates
/topic/task-status-changed
/topic/task-created
/topic/task-assigned

# Shopping Updates
/topic/shopping-item-purchased
/topic/shopping-list-consolidated
/topic/ingredient-availability-changed

# Cooking Updates
/topic/cooking-ingredient-modified
/topic/cooking-schedule-updated
/topic/ingredient-requirements-changed

# Budget Updates
/topic/budget-status-changed
/topic/workflow-triggered

# User-specific Notifications
/user/{userId}/queue/notifications
/user/{userId}/queue/task-assignments
/user/{userId}/queue/conflict-alerts
```

### **WebSocket Message Format**
```json
{
  "type": "task-status-changed",
  "data": {
    "taskId": "123",
    "oldStatus": "TODO",
    "newStatus": "IN_PROGRESS",
    "updatedBy": "user456",
    "timestamp": "2025-07-07T10:30:00Z",
    "affectedUsers": ["user789", "user012"]
  },
  "targetRoles": ["LOGISTICS", "COOK"],
  "priority": "NORMAL"
}
```

## 🔐 Security and Access Control

### **Role-Based Permissions Matrix**
| Resource | ADMIN | SALES | LOGISTICS | COOK |
|----------|-------|-------|-----------|------|
| Budgets | CRUD | CRUD | Read | Read |
| Tasks | CRUD | Read | CRUD (logistics tasks) | CRUD (cooking tasks) |
| Shopping | CRUD | Read | CRUD | Read |
| Cooking | CRUD | Read | Read | CRUD |
| Templates | CRUD | Read | Read | CRUD (menus only) |
| Users | CRUD | - | - | - |
| Config | CRUD | - | - | - |

### **Data Filtering Requirements**
- **SALES**: Only see budgets they created
- **LOGISTICS**: Only see approved budgets (status = RESERVA) and assigned tasks
- **COOK**: Only see cooking-related tasks and schedules
- **Cross-Role Data**: Shopping lists visible to both LOGISTICS and COOK

### **Authentication Requirements**
- JWT-based authentication with role claims
- Session management with token refresh
- Role-based route protection
- API endpoint security based on user role

## 📊 Performance Requirements

### **Database Indexing Strategy**
```sql
-- Critical indexes for performance
CREATE INDEX idx_budgets_status_date ON budgets(status, event_date);
CREATE INDEX idx_tasks_role_status ON tasks(assigned_to_role, status);
CREATE INDEX idx_shopping_week ON shopping_items(week_start, week_end);
CREATE INDEX idx_cooking_date ON cooking_schedules(event_date);
CREATE INDEX idx_audit_entity_time ON audit_logs(entity_type, timestamp);
```

### **Caching Requirements**
- **Templates**: 1-hour cache (rarely change)
- **Shopping Lists**: 15-minute cache (frequent updates)
- **Task Lists**: 5-minute cache (real-time critical)
- **Dashboard Analytics**: 30-minute cache
- **User Profiles**: 24-hour cache

### **Performance Targets**
- API Response Time: < 200ms for CRUD operations
- WebSocket Message Delivery: < 100ms
- Database Query Time: < 50ms for indexed queries
- Workflow Trigger Processing: < 2 seconds
- Shopping Consolidation: < 5 seconds for weekly aggregation

## 🧪 Business Logic Validation

### **Workflow Trigger Validation**
```
✅ Budget status change to "RESERVA" generates tasks
✅ Tasks are assigned to correct roles based on type
✅ Task dependencies are properly established
✅ Task timing is calculated based on event date
✅ Notifications are sent to assigned roles
```

### **Shopping Consolidation Validation**
```
✅ Products are grouped correctly by name and unit
✅ Quantities are summed across multiple budgets
✅ Purchase progress is tracked accurately
✅ Cook ingredient changes trigger list updates
✅ Partial purchases are handled correctly
```

### **Real-Time Sync Validation**
```
✅ Cook ingredient changes update shopping lists
✅ Shopping purchases notify cooks immediately
✅ Task status changes broadcast to relevant users
✅ Conflict detection works for simultaneous edits
✅ WebSocket connections handle reconnection gracefully
```

### **Role Access Validation**
```
✅ Users can only access data appropriate to their role
✅ CRUD operations are restricted by role permissions
✅ API endpoints return filtered data based on user role
✅ Workflow triggers respect role boundaries
```

## 🎯 Critical Integration Points

### **Frontend-Backend Integration**
1. **State Synchronization**: Zustand store must sync with backend APIs
2. **Real-Time Updates**: WebSocket integration for live data updates
3. **Optimistic Updates**: Frontend updates with backend confirmation
4. **Error Handling**: Graceful degradation and user feedback
5. **Conflict Resolution**: UI for handling concurrent modifications

### **Workflow Automation Integration**
1. **Trigger Mechanisms**: Budget status changes activate workflows
2. **Task Generation**: Automatic creation of interdependent tasks
3. **Scheduling Logic**: Smart timing based on event requirements
4. **Notification System**: Role-based alerts and assignments

### **Data Consistency Requirements**
1. **Transactional Integrity**: Workflow operations must be atomic
2. **Referential Integrity**: Proper foreign key relationships
3. **Audit Trail**: Complete history of all changes
4. **Conflict Resolution**: Handle concurrent modifications gracefully

## 📋 Verification Checklist

### **Entity Implementation**
- [ ] All 62+ entities generated with correct fields and relationships
- [ ] AppUser entity replaces built-in User with proper relationships
- [ ] Template entities have isTemplate flags
- [ ] Version and conflict tracking fields present
- [ ] Audit logging integrated into all entities

### **API Implementation**
- [ ] Complete CRUD operations for all entities
- [ ] Role-based filtering implemented
- [ ] Pagination and sorting supported
- [ ] Advanced workflow endpoints available
- [ ] WebSocket endpoints configured

### **Business Logic**
- [ ] Budget status change triggers task generation
- [ ] Shopping consolidation works across budgets
- [ ] Real-time sync between cook and logistics
- [ ] Task dependencies properly managed
- [ ] Role-based access control enforced

### **Performance and Security**
- [ ] Database indexes created for performance
- [ ] Caching strategy implemented
- [ ] JWT authentication with role claims
- [ ] API security based on user roles
- [ ] WebSocket authentication and authorization

### **Integration Readiness**
- [ ] OpenAPI/Swagger documentation available
- [ ] CORS configured for frontend integration
- [ ] WebSocket endpoint properly exposed
- [ ] Development and production configurations
- [ ] Database migration scripts included

---

This verification context ensures the generated JHipster backend fully supports the sophisticated Party Budget Bliss application with its advanced workflow automation, real-time synchronization, and intelligent resource planning capabilities. 