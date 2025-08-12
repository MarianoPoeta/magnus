# 🏗️ Project Architecture Overview

## 🎯 System Purpose
**Party Budget Bliss** is a comprehensive event management system with sophisticated workflow automation, real-time task synchronization, and intelligent resource planning across multiple user roles.

## 🔧 Core Technologies
- **Frontend**: React 18 + TypeScript + Vite
- **State Management**: Zustand (persistent store)
- **UI Framework**: Tailwind CSS + shadcn/ui components
- **Backend Integration**: RESTful APIs + WebSocket (planned)
- **Database**: MySQL (production) / H2 (development)
- **Build System**: Vite + ESBuild

## 👥 User Roles & Responsibilities

### **Sales Representative**
- Creates and manages budgets
- Handles client relationships
- Converts drafts to "reserva" status (triggers workflows)
- Manages pricing and proposals

### **Logistics Coordinator**
- Handles weekly planning and shopping lists
- Manages product procurement and deliveries
- Coordinates transportation and setup
- Tracks inventory and supplier relationships

### **Cook**
- Manages cooking schedules and meal preparation
- Modifies ingredient requirements (auto-syncs to shopping lists)
- Handles menu customization and dietary requirements
- Oversees food safety and quality control

### **Admin**
- System configuration and user management
- Template creation and maintenance
- Analytics and reporting
- System-wide settings and permissions

## 🔄 Core Workflow Pattern

```
Budget Creation → Client Approval → "RESERVA" Status → Automatic Task Generation
     ↓
[Shopping Tasks] → [Cooking Tasks] → [Delivery Tasks] → [Setup Tasks]
     ↓
Real-time Synchronization → Progress Tracking → Completion
```

## 🏛️ Architecture Layers

### **Presentation Layer**
- React components with TypeScript
- Responsive design with Tailwind CSS
- Real-time UI updates via WebSocket integration
- Role-based component rendering

### **Business Logic Layer**
- Zustand store for state management
- Custom hooks for complex operations
- Service layer for API integration
- Workflow automation engines

### **Data Layer**
- RESTful API endpoints
- WebSocket connections for real-time features
- Local storage for offline capabilities
- Optimistic updates with rollback

## 🎨 UI/UX Patterns

### **Design System**
- Consistent color palette and typography
- Standardized component variants
- Accessibility-first approach
- Mobile-responsive layouts

### **Navigation Patterns**
- Role-based sidebar navigation
- Breadcrumb navigation for deep pages
- Quick actions and shortcuts
- Context-aware menus

### **Data Display Patterns**
- Card-based layouts for listings
- Table views with advanced filtering
- Modal dialogs for detailed forms
- Progress indicators for multi-step processes

## 🔐 Security & Access Control

### **Role-Based Access Control (RBAC)**
- Route-level protection
- Component-level conditional rendering
- Action-level permissions
- Data filtering by user role

### **Authentication Flow**
- JWT-based authentication
- Role assignment and validation
- Session management
- Logout and security cleanup

## 📊 Performance Considerations

### **Optimization Strategies**
- React.memo for expensive components
- useCallback and useMemo for heavy calculations
- Lazy loading for code splitting
- Virtual scrolling for large lists

### **Caching Strategy**
- API response caching
- Local storage for frequently accessed data
- Optimistic updates for better UX
- Background data synchronization

## 🚀 Scalability Features

### **Code Organization**
- Feature-based folder structure
- Reusable component library
- Custom hooks for business logic
- Service layer abstractions

### **Data Management**
- Normalized state structure
- Efficient data fetching patterns
- Real-time updates without polling
- Conflict resolution strategies

## 📱 Mobile Considerations

### **Responsive Design**
- Mobile-first approach
- Touch-friendly interfaces
- Simplified navigation for small screens
- Progressive Web App capabilities

---

**Usage Tips:**
- Reference this notepad when making architectural decisions
- Use `@PROJECT_ARCHITECTURE.md` to get system-wide context
- Consult role definitions when implementing access controls
- Follow established patterns for consistency 