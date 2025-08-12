# Development Environment Setup Guide

This guide will help you set up the Party Budget Bliss frontend to work with the JHipster backend for development and testing.

## Prerequisites

- Node.js (v18 or higher)
- Java 17 or higher
- MySQL 8.0 or higher
- Git

## 🔧 Backend Setup (JHipster)

### 1. Database Setup

First, make sure MySQL is running and create the database:

```sql
CREATE DATABASE magnus;
CREATE USER 'magnus'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON magnus.* TO 'magnus'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Backend Configuration

Navigate to the backend directory:

```bash
cd magnus-backend
```

Update the database connection in `src/main/resources/config/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/magnus?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true
    username: magnus
    password: your_password
```

### 3. Start the Backend

```bash
# Install dependencies and start the backend
./mvnw spring-boot:run

# Or if you're on Windows
mvnw.cmd spring-boot:run
```

The backend will start on: **http://localhost:8080**

### 4. Verify Backend

- API Documentation: http://localhost:8080/swagger-ui/
- Health Check: http://localhost:8080/management/health
- Database will be automatically created with sample data

## 🎨 Frontend Setup

### 1. Environment Configuration

Create a `.env.development` file in the frontend root directory:

```env
# Vite frontend expects VITE_* variables
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080/websocket
VITE_ENVIRONMENT=development
VITE_APP_NAME=Party Budget Bliss
VITE_APP_VERSION=1.0.0
VITE_ENABLE_WORKFLOW_AUTOMATION=true
VITE_ENABLE_DEVELOPER_TOOLS=true
VITE_ENABLE_MOCK_DATA=false
VITE_JWT_STORAGE_KEY=magnus-auth-token
```

### 2. Install Dependencies

```bash
# Navigate to frontend directory
cd magnus-frontend

# Install dependencies
npm install

# Or using yarn
yarn install
```

### 3. Start the Frontend

```bash
# Start the development server
npm run dev

# Or using yarn
yarn dev
```

The frontend will start on: **http://localhost:5173** (or another available port)

## 🔐 Authentication Setup

### Default JHipster Users

The backend comes with default users for testing:

1. **Admin User:**
   - Username: `admin`
   - Password: `admin`
   - Role: Administrator

2. **Regular User:**
   - Username: `user`
   - Password: `user`
   - Role: User

### Using Authentication in Frontend

```typescript
import { useAuth } from './hooks/useAuth';

const LoginExample = () => {
  const { login, isLoading, error } = useAuth();

  const handleLogin = async () => {
    const result = await login({
      username: 'admin',
      password: 'admin',
      rememberMe: true
    });
    
    if (result.success) {
      console.log('Login successful!', result.user);
    } else {
      console.error('Login failed:', result.error);
    }
  };

  return (
    <button onClick={handleLogin} disabled={isLoading}>
      {isLoading ? 'Logging in...' : 'Login'}
    </button>
  );
};
```

## 🔄 Testing Workflow Automation

### Using the Workflow Hook

```typescript
import { useWorkflow } from './hooks/useWorkflow';

const WorkflowTestComponent = () => {
  const { approveBudget, testWorkflow, isLoading, error } = useWorkflow();

  const handleApproveBudget = async () => {
    const result = await approveBudget(1); // Budget ID 1
    
    if (result.success) {
      console.log('Budget approved! Tasks generated:', result.data);
    } else {
      console.error('Failed to approve budget:', result.error);
    }
  };

  const handleTestWorkflow = async () => {
    const result = await testWorkflow(1); // Budget ID 1
    
    if (result.success) {
      console.log('Workflow test completed:', result.data);
    } else {
      console.error('Workflow test failed:', result.error);
    }
  };

  return (
    <div>
      <button onClick={handleApproveBudget} disabled={isLoading}>
        Approve Budget (Triggers Workflow)
      </button>
      <button onClick={handleTestWorkflow} disabled={isLoading}>
        Test Complete Workflow
      </button>
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}
    </div>
  );
};
```

## 🎯 Available API Endpoints

### Authentication
- `POST /api/authenticate` - Login
- `GET /api/account` - Get current user
- `POST /api/register` - Register new user
- `POST /api/account/change-password` - Change password

### Budget Management
- `GET /api/budgets` - Get all budgets (paginated)
- `POST /api/budgets` - Create new budget
- `GET /api/budgets/{id}` - Get budget by ID
- `PUT /api/budgets/{id}` - Update budget
- `DELETE /api/budgets/{id}` - Delete budget

### Workflow Automation 🔥
- `POST /api/workflow/approve-budget/{id}` - Approve budget (triggers workflow)
- `POST /api/workflow/trigger-tasks/{id}` - Manually trigger tasks
- `PATCH /api/workflow/budget-status/{id}` - Update budget status
- `GET /api/workflow/budget-status/{id}` - Get budget status

### Task Management
- `GET /api/tasks` - Get all tasks (paginated)
- `POST /api/tasks` - Create new task
- `GET /api/tasks/{id}` - Get task by ID
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

### Other Resources
- `GET /api/activities` - Activities
- `GET /api/accommodations` - Accommodations
- `GET /api/menus` - Menus
- `GET /api/products` - Products
- `GET /api/transports` - Transports
- `GET /api/clients` - Clients
- `GET /api/app-users` - App Users (custom user management)

## 🔍 Testing the Integration

### 1. Test Basic Connectivity

```bash
# Test backend health
curl http://localhost:8080/management/health

# Test API endpoints
curl -X GET http://localhost:8080/api/budgets
```

### 2. Test Authentication

```bash
# Login and get token
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### 3. Test Workflow Automation

1. **Login to frontend** with admin credentials
2. **Create a budget** with future event date
3. **Approve the budget** using the workflow hook
4. **Check the tasks** to see automatically generated tasks
5. **Monitor the browser console** for workflow events

### 4. Sample Test Scenario

```typescript
// Complete workflow test scenario
const testCompleteWorkflow = async () => {
  try {
    // 1. Login
    const loginResult = await login({
      username: 'admin',
      password: 'admin'
    });
    
    if (!loginResult.success) {
      throw new Error('Login failed');
    }
    
    // 2. Create a budget
    const budget = await budgetService.createBudget({
      title: 'Test Event Budget',
      eventDate: '2024-02-15',
      clientId: 1,
      totalAmount: 5000.00,
      status: 'draft'
    });
    
    // 3. Approve budget (triggers workflow)
    const approvalResult = await approveBudget(budget.id);
    
    if (approvalResult.success) {
      console.log('✅ Budget approved, tasks generated automatically!');
    }
    
    // 4. Check generated tasks
    const tasks = await taskService.getTasks();
    console.log('📋 Generated tasks:', tasks);
    
  } catch (error) {
    console.error('❌ Test failed:', error);
  }
};
```

## 🚀 Production Deployment Notes

For production deployment, update the environment variables:

```env
VITE_API_URL=https://your-backend-domain.com/api
VITE_WS_URL=wss://your-backend-domain.com/websocket
VITE_ENVIRONMENT=production
```

## 📝 Troubleshooting

### Common Issues

1. **CORS Errors**: The backend is already configured to allow localhost:3000. If you're using a different port, update the CORS configuration in `application-dev.yml`.

2. **Database Connection**: Make sure MySQL is running and the database credentials are correct.

3. **Port Conflicts**: If port 8080 is busy, you can change it in `application-dev.yml`:
   ```yaml
   server:
     port: 8081
   ```

4. **Authentication Issues**: Check that the JWT secret is properly configured and tokens are being stored in localStorage.

### Debug Mode

Enable debug mode by setting a custom flag if needed:
```env
VITE_ENABLE_DEBUG_PANEL=true
```

This will show detailed API requests and responses in the browser console.

## 🎉 You're Ready!

With this setup, you can:
- ✅ Login with JHipster authentication
- ✅ Create and manage budgets
- ✅ Test workflow automation
- ✅ See automatic task generation
- ✅ Monitor real-time events
- ✅ Persist data across sessions

Start developing and testing your Party Budget Bliss application!

---

**Happy Coding! 🎈** 