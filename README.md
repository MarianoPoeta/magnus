# Magnus Monorepo

Includes:
- `magnus-backend/` (Spring Boot, JHipster, JWT)
- `magnus-frontend/` (React + Vite)
- `patches/` (local diff artifacts)

## Cloud Dev (Codespaces)
- Open in GitHub Codespaces. The devcontainer will:
  - Start MySQL in a side container (port 3306).
  - Install frontend deps and compile the backend.

### Running
- Backend: `cd magnus-backend && ./mvnw -DskipTests spring-boot:run`
- Frontend: `cd magnus-frontend && npm run dev` (Vite on 5173)

Create `magnus-frontend/.env`:
```
VITE_API_URL=http://localhost:8080/api
VITE_ENVIRONMENT=development
VITE_JWT_STORAGE_KEY=magnus_jwt
```

