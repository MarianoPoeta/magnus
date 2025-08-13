# 🔄 Workflow Automation System

## 🎯 Core Business Logic
The Party Budget Bliss system centers around **automated workflow generation** that transforms budget approvals into coordinated task chains across multiple user roles.

## 🚀 Primary Workflow Trigger

### **Budget Status Change: "RESERVA"**
When a budget changes to "reserva" status, the system automatically:
1. **Generates Task Chains** - Creates interconnected tasks for logistics and cooking
2. **Assigns Roles** - Distributes tasks based on user roles and capabilities
3. **Sets Dependencies** - Establishes task ordering and blocking relationships
4. **Schedules Timing** - Calculates optimal task timing based on event date
5. **Triggers Notifications** - Alerts relevant users about new assignments

## 📋 Task Generation Patterns

### **Shopping Task Generation**
```typescript
// Triggered: 2-7 days before event
// Assigned to: Logistics Coordinator
// Dependencies: None (first in chain)
// Generates: ProductRequirement[] based on selected menus/activities
```

**Auto-Generated Requirements:**
- Extracts ingredients from selected menus
- Calculates quantities based on guest count
- Applies portion multipliers and waste factors
- Groups by supplier and category
- Sets purchase deadlines

### **Cooking Task Generation**
```typescript
// Triggered: Day of event (or day before for prep)
// Assigned to: Cook
// Dependencies: Shopping tasks must be completed
// Generates: CookingSchedule with precise timing
```

**Auto-Generated Schedules:**
- Calculates cooking start times based on serving time
- Sequences multiple dishes for optimal timing
- Includes prep time and cooking duration
- Adds buffer time for unexpected delays
- Integrates special dietary requirements

### **Delivery Task Generation**
```typescript
// Triggered: Day of event
// Assigned to: Logistics Coordinator
// Dependencies: Equipment availability
// Generates: Transport and setup schedules
```

**Auto-Generated Logistics:**
- Coordinates equipment delivery timing
- Plans transportation routes
- Schedules setup and breakdown times
- Manages inventory tracking
- Handles return logistics

## 🔄 Real-Time Synchronization

### **Cook → Logistics Sync**
When cooks modify ingredients:
1. **Detect Changes** - Monitor ingredient quantity/type modifications
2. **Recalculate Requirements** - Update shopping lists automatically
3. **Propagate Updates** - Notify logistics of changed requirements
4. **Maintain History** - Track all changes for audit purposes
5. **Resolve Conflicts** - Handle simultaneous updates gracefully

### **Shopping Progress Sync**
When logistics purchases items:
1. **Update Availability** - Mark items as purchased in real-time
2. **Notify Cooks** - Alert about ingredient availability
3. **Adjust Schedules** - Modify cooking tasks based on availability
4. **Track Expenses** - Update budget actuals vs. estimates
5. **Generate Alerts** - Warn about shortages or delays

## 📊 Shopping List Consolidation

### **Weekly Aggregation Algorithm**
```typescript
// Consolidates multiple budgets into single shopping list
// Groups products by: name, unit, category, supplier
// Calculates total quantities needed for the week
// Optimizes purchase timing and batch sizes
```

**Consolidation Rules:**
- **Same Product, Same Week** → Combine quantities
- **Different Units** → Convert to common units
- **Multiple Suppliers** → Choose preferred supplier
- **Bulk Discounts** → Optimize order sizes
- **Shelf Life** → Schedule purchases closer to usage

### **Purchase Tracking**
- **Partial Purchases** - Track what's bought vs. what's needed
- **Overages** - Handle quantity adjustments and waste
- **Substitutions** - Manage product alternatives
- **Cost Tracking** - Monitor actual vs. estimated costs
- **Supplier Performance** - Track delivery reliability

## ⚡ Automation Triggers

### **Budget Events**
- `status: 'draft' → 'reserva'` → Generate all tasks
- `guestCount changed` → Recalculate quantities
- `eventDate changed` → Reschedule all tasks
- `items added/removed` → Update requirements

### **Task Events**
- `shopping completed` → Enable cooking tasks
- `cooking started` → Update delivery schedules
- `task delayed` → Notify dependent tasks
- `task cancelled` → Cascade cancellations

### **Ingredient Events**
- `quantity modified` → Update shopping lists
- `ingredient added` → Add to procurement
- `ingredient removed` → Remove from lists
- `substitution made` → Update all references

## 🔧 Workflow Configuration

### **Task Scheduling Rules**
```typescript
const SCHEDULING_RULES = {
  shopping: {
    leadTime: '2-7 days',
    bufferTime: '1 day',
    batchOptimization: true
  },
  cooking: {
    leadTime: '0-1 days',
    prepTime: 'calculated',
    sequencing: 'automatic'
  },
  delivery: {
    leadTime: '0 days',
    setupTime: '2-4 hours',
    coordination: 'required'
  }
};
```

### **Dependency Management**
- **Hard Dependencies** - Must complete before next task
- **Soft Dependencies** - Preferred but not required
- **Conditional Dependencies** - Based on task parameters
- **Circular Detection** - Prevent infinite loops
- **Conflict Resolution** - Handle competing priorities

## 🎯 Smart Scheduling

### **Optimization Factors**
1. **Event Timing** - Work backward from serving time
2. **Resource Availability** - Check cook/logistics schedules
3. **Supplier Schedules** - Account for delivery windows
4. **Preparation Time** - Include realistic prep estimates
5. **Buffer Time** - Add safety margins for delays

### **Automatic Adjustments**
- **Weather Delays** - Extend timelines for outdoor events
- **Rush Orders** - Prioritize urgent tasks
- **Resource Conflicts** - Reschedule competing tasks
- **Supplier Issues** - Find alternative sources
- **Guest Count Changes** - Proportionally adjust all tasks

## 📈 Performance Monitoring

### **Workflow Metrics**
- **Task Completion Rate** - Percentage completed on time
- **Dependency Violations** - Tasks started before prerequisites
- **Resource Utilization** - Cook/logistics efficiency
- **Cost Variance** - Actual vs. estimated costs
- **Client Satisfaction** - Post-event feedback

### **Automated Alerts**
- **Late Tasks** - Notify when tasks fall behind
- **Missing Requirements** - Alert about unfulfilled needs
- **Budget Overruns** - Warn about cost excesses
- **Quality Issues** - Flag potential problems
- **Capacity Conflicts** - Identify resource bottlenecks

## 🛠️ Integration Points

### **External Systems**
- **Supplier APIs** - Real-time pricing and availability
- **Inventory Systems** - Stock level monitoring
- **Payment Processors** - Automatic payment handling
- **Calendar Systems** - Schedule coordination
- **Communication Platforms** - Automated notifications

### **Internal Services**
- **Task Generator** - Creates new tasks automatically
- **Scheduler** - Optimizes task timing
- **Sync Engine** - Maintains data consistency
- **Notification Service** - Sends role-based alerts
- **Analytics Engine** - Tracks performance metrics

## 🧩 Background Agents & CI

- Spring Scheduled jobs: example in backend `UserService.removeNotActivatedUsers()`; add new schedulers as needed for batch tasks.
- SonarQube: start with `docker compose -f magnus-backend/src/main/docker/sonar.yml up -d` and run analysis using the Maven goal shown in `magnus-backend/README.md`.
- CodeQL: enabled via `.github/workflows/ci.yml` to scan Java and JS/TS on PRs.

---

**Usage Tips:**
- Reference this notepad when implementing workflow features
- Use `@WORKFLOW_AUTOMATION.md` to understand business logic
- Consult scheduling rules when building task systems
- Follow automation patterns for consistency
- Always consider real-time sync requirements 