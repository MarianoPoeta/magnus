# Magnus Monorepo

Includes:
- `magnus-backend/` (Spring Boot, JHipster, JWT)
- `magnus-frontend/` (React + Vite)
- `patches/` (local diff artifacts)

## Quick Start

Prereqs: Docker Desktop, Node 20+, Java 17+.

### Dev (non-Docker)
- Backend: `cd magnus-backend && ./mvnw -DskipTests spring-boot:run`
- Frontend: `cd magnus-frontend && npm i && npm run dev` (Vite on 5173)

Create `magnus-frontend/.env`:
```
VITE_API_URL=http://localhost:8080/api
VITE_ENVIRONMENT=development
VITE_JWT_STORAGE_KEY=magnus-auth-token
```

### Dev (Docker Compose)
From repo root:
```
docker compose -f docker-compose.dev.yml up --build
```
- Backend: `http://localhost:8080`
- Frontend: `http://localhost:5173`
- MySQL: `localhost:3306` (db: magnus)

### Prod (Docker Compose)
```
docker compose -f docker-compose.prod.yml up --build -d
```
- Frontend (nginx): `http://localhost:8081`
- Backend (prod profile): `http://localhost:8080`

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

## Backend/Frontend Integration
- Frontend `src/services/api.ts` targets JHipster endpoints (JWT based). Pages now load from backend:
  - Products: `/api/products` (enum mapping handled in `useApi`)
  - Food Items: `/api/food-items`
- Mocks remain in the store for fallback during development but are overridden when authenticated and API is reachable.

## Background Agents & Automation
- Spring `@Scheduled` tasks available and example in `UserService.removeNotActivatedUsers()`.
- Recommended: GitHub Actions with CodeQL and Sonar (see `magnus-backend/sonar-project.properties` and `src/main/docker/sonar.yml`).

## Docker Notes
- Backend compose files live under `magnus-backend/src/main/docker/` (app.yml, services.yml, mysql.yml).
- Root-level compose files orchestrate backend + frontend together for Dev/Prod.

## Security
- CORS is open for local dev on common ports (`application-dev.yml`).
- JWT is stored under `VITE_JWT_STORAGE_KEY` in localStorage.

## Roadmap / Improvements
- Replace remaining local-only mutations with API calls where applicable (Transports/Admin templates).
- Add CI via GitHub Actions for backend unit tests and frontend lint/build.
- Add SAST (CodeQL) and Sonar scan on PRs.

