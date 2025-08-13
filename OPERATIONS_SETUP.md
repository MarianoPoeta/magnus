# Magnus Operations and Background Agents Setup

This comprehensive guide explains how to enable, operate, and observe background agents, and how to configure CI, quality, and production operations for the Magnus project. It is written to be approachable for varying technical backgrounds with step-by-step instructions, links to exact files in this repository, and copy-paste friendly commands.

Repo layout (relevant):
- Backend (JHipster, Spring Boot): `magnus-backend/`
- Frontend (Vite + React): `magnus-frontend/`
- Docker orchestration: `docker-compose.dev.yml`, `docker-compose.prod.yml`

## Introduction

Magnus is a full‑stack application for managing event budgets, logistics, and operations. It includes:
- A backend built with JHipster (Spring Boot) for APIs, authentication (JWT), scheduled jobs, and observability endpoints.
- A frontend built with Vite + React for modern UX and admin workflows.

Objectives and goals
- Automation: Use background agents (scheduled jobs) to eliminate repetitive manual work (shopping consolidation, reminders, hygiene).
- Observability: Instrument the system with Prometheus + Grafana for metrics and dashboards.
- Error Tracking: Capture frontend exceptions with Sentry to proactively fix issues.

## Project Setup

### 1) Clone and prerequisites
Install prerequisites:
- Java 17+
- Node.js 20+
- Docker Desktop
- Git

Clone the repository and open in your editor:
```bash
git clone <your-repo-url>
cd magnus
```

### 2) Backend (JHipster, Spring Boot)
- Directory: `magnus-backend/`
- Dev run (hot reload):
```bash
cd magnus-backend
./mvnw -DskipTests spring-boot:run
```
- Test + verify:
```bash
./mvnw -ntp -DskipTests=false verify
```
- Key files:
  - `src/main/resources/config/application-dev.yml` – local dev config
  - `src/main/resources/config/application-prod.yml` – prod config
  - `src/main/java/com/magnus/MagnusApp.java` – main application (enable scheduling here)

### 3) Frontend (Vite + React)
- Directory: `magnus-frontend/`
- Install and run dev server:
```bash
cd magnus-frontend
npm i
npm run dev
```
- Configure local `.env` (create if missing):
```env
VITE_API_URL=http://localhost:8080/api
VITE_ENVIRONMENT=development
VITE_JWT_STORAGE_KEY=magnus-auth-token
```

### 4) Docker Orchestration
- Development compose: `docker-compose.dev.yml`
- Production compose: `docker-compose.prod.yml`

Dev run (backend+frontend+MySQL, with hot reload):
```bash
docker compose -f docker-compose.dev.yml up --build
```

Prod run (Nginx for frontend static, backend in prod):
```bash
docker compose -f docker-compose.prod.yml up --build -d
```

Services exposed
- MySQL @ 3306
- Backend @ 8080
- Frontend (dev Vite) @ 5173
- Frontend (prod Nginx) @ 8081

## Background Agents and Operations

Purpose: automate repetitive ops so your team spends less time coordinating manually.

Where to enable scheduling
- File: `magnus-backend/src/main/java/com/magnus/MagnusApp.java`
- Add `@EnableScheduling` to the Spring Boot application (or a configuration class).

Example:
```java
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MagnusApp {
  // ... existing code ...
}
```

Where to add jobs
- Create file: `magnus-backend/src/main/java/com/magnus/service/SchedulerJobs.java`
- Annotate with `@Service` and use `@Scheduled(cron = "...")` methods.

Skeleton example:
```java
package com.magnus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerJobs {
  private static final Logger log = LoggerFactory.getLogger(SchedulerJobs.class);

  // Weekly shopping list consolidation: Mon 06:00
  @Scheduled(cron = "0 0 6 * * MON")
  public void consolidateWeeklyShopping() {
    log.info("[CRON] consolidateWeeklyShopping: start");
    // TODO: implement consolidation across budgets
    log.info("[CRON] consolidateWeeklyShopping: end");
  }

  // Reservation deadline reminders: Daily 09:00
  @Scheduled(cron = "0 0 9 * * *")
  public void sendReservationReminders() {
    log.info("[CRON] sendReservationReminders: start");
    // TODO
    log.info("[CRON] sendReservationReminders: end");
  }

  // Price freshness watchdog: Daily 07:00
  @Scheduled(cron = "0 0 7 * * *")
  public void priceFreshnessWatchdog() {
    log.info("[CRON] priceFreshnessWatchdog: start");
    // TODO
    log.info("[CRON] priceFreshnessWatchdog: end");
  }

  // Notification digests: Daily 17:00
  @Scheduled(cron = "0 0 17 * * *")
  public void sendRoleDigests() {
    log.info("[CRON] sendRoleDigests: start");
    // TODO
    log.info("[CRON] sendRoleDigests: end");
  }

  // Data hygiene & maintenance: Daily 02:30
  @Scheduled(cron = "0 30 2 * * *")
  public void dataHygieneMaintenance() {
    log.info("[CRON] dataHygieneMaintenance: start");
    // TODO
    log.info("[CRON] dataHygieneMaintenance: end");
  }
}
```

Recommended jobs (detail)
- Weekly shopping list consolidation (Mon 06:00): Aggregate `ShoppingItem`s needed for the coming week across all approved/reserved budgets; output to a task queue for logistics.
- Reservation deadline reminders (Daily 09:00): Remind sales/logistics about budgets near event date missing reservations or purchases.
- Price freshness watchdog (Daily 07:00): Flag `Product` prices older than N days; create admin review tasks.
- Notification digests (Daily 17:00): Send one digest per role (sales/logistics/cook) summarizing actionable tasks to reduce noise.
- Data hygiene & maintenance (Daily 02:30): Clean stale rows, rotate/trim logs, refresh caches/indexes.

Verifying job executions
- Run backend in dev:
```bash
cd magnus-backend && ./mvnw spring-boot:run
```
- Logs: confirm each job logs a start/end at scheduled times.
- Health: `http://localhost:8080/management/health` should be UP.

Optional reliability pings (Healthchecks.io)
- Create a check per job (start/success/fail).
- In each scheduled method, issue an HTTP request to Healthchecks on start/end. Example using Java 11 HttpClient:
```java
private void ping(String url) {
  try {
    java.net.http.HttpClient.newHttpClient()
      .send(java.net.http.HttpRequest.newBuilder(java.net.URI.create(url)).GET().build(),
            java.net.http.HttpResponse.BodyHandlers.discarding());
  } catch (Exception ignored) {}
}
```

## Continuous Integration and Security

### CodeQL (Static Analysis)
- Workflow file: `.github/workflows/ci.yml` – runs on push/PR for Java and JS/TS.
- View alerts: GitHub → Security → Code scanning alerts.
- Optional weekly scan schedule snippet:
```yaml
on:
  schedule:
    - cron: '0 3 * * 1'
```

### Dependabot (Dependency Updates)
- Config file: `.github/dependabot.yml` – opens PRs for Maven/npm weekly.
```yaml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/magnus-backend"
    schedule:
      interval: "weekly"
  - package-ecosystem: "npm"
    directory: "/magnus-frontend"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 5
```

## Code Quality Assurance (SonarQube / SonarCloud)

Option A: Local SonarQube (free)
- Start server:
```bash
docker compose -f magnus-backend/src/main/docker/sonar.yml up -d
```
- Analyze backend code:
```bash
cd magnus-backend
./mvnw -Pprod clean verify sonar:sonar
```
- UI: `http://localhost:9001`
- Config: `magnus-backend/sonar-project.properties`

Option B: SonarCloud (hosted)
- Create a project; add `SONAR_TOKEN` GitHub secret.
- Update CI step:
```bash
./mvnw -Pprod sonar:sonar -Dsonar.login=${{ secrets.SONAR_TOKEN }}
```

Quality gates and trends
- Define quality gate thresholds (coverage, duplicated lines, bugs, vulnerabilities).
- Track trends over time per PR and main branch to prevent regressions.

## Observability and Monitoring

### Prometheus + Grafana
- Start monitoring stack:
```bash
docker compose -f magnus-backend/src/main/docker/monitoring.yml up -d
```
- Configuration files:
  - Prometheus: `magnus-backend/src/main/docker/prometheus/prometheus.yml`
  - Grafana provisioning: `magnus-backend/src/main/docker/grafana/provisioning/`
- UIs:
  - Prometheus: `http://localhost:9090`
  - Grafana: `http://localhost:3000`
- Dashboards:
  - Import JVM + Spring Boot dashboards; point data source to the pre-configured Prometheus in compose.

### Uptime monitors (UptimeRobot or similar)
- Add monitors for:
  - Backend: `http://<host>:8080/management/health`
  - Frontend: `http://<host>:8081`
- For prod, ensure actuator endpoints are exposed (see `application-prod.yml`).

## Error Tracking with Sentry (Frontend)

Environment variables (template): `magnus-frontend/environment.template`
```env
VITE_SENTRY_DSN=your_sentry_dsn
VITE_SENTRY_ENVIRONMENT=production
```

Initialize Sentry in `magnus-frontend/src/main.tsx`:
```ts
// magnus-frontend/src/main.tsx
import * as Sentry from '@sentry/react';
import { BrowserTracing } from '@sentry/tracing';

Sentry.init({
  dsn: import.meta.env.VITE_SENTRY_DSN,
  environment: import.meta.env.VITE_SENTRY_ENVIRONMENT || 'development',
  integrations: [new BrowserTracing()],
  tracesSampleRate: 0.1,
});
```

Build and deploy frontend:
```bash
cd magnus-frontend && npm run build
```

## Job Reliability (Healthchecks.io)

Create checks per scheduled job and ping on start/success/failure.

Example pattern in `SchedulerJobs`:
```java
private static final String SHOPPING_START = "https://hc-ping.com/<uuid-start>";
private static final String SHOPPING_SUCCESS = "https://hc-ping.com/<uuid-success>";
private static final String SHOPPING_FAIL = "https://hc-ping.com/<uuid-fail>";

@Scheduled(cron = "0 0 6 * * MON")
public void consolidateWeeklyShopping() {
  ping(SHOPPING_START);
  try {
    // work
    ping(SHOPPING_SUCCESS);
  } catch (Exception e) {
    ping(SHOPPING_FAIL);
  }
}
```

## Docker Orchestration

### Development (`docker-compose.dev.yml`)
Services:
- `db`: MySQL 8.0
- `backend`: Maven builder running Spring Boot in dev
- `frontend`: Vite dev server

Run:
```bash
docker compose -f docker-compose.dev.yml up --build
```

### Production (`docker-compose.prod.yml`)
Services:
- `db`: MySQL 8.0
- `backend`: Built Spring Boot JAR (prod)
- `frontend`: Nginx serving `magnus-frontend/dist` with API proxy to backend

Run:
```bash
docker compose -f docker-compose.prod.yml up --build -d
```

## Project Modifications

To fully enable operations:
1) Enable scheduling
   - Add `@EnableScheduling` to `MagnusApp`.
2) Create `SchedulerJobs` class with 5 jobs
   - Place under `magnus-backend/src/main/java/com/magnus/service/`.
3) Configure Sentry DSN in frontend
   - Add variables to `.env`; initialize in `src/main.tsx` as shown.
4) Add Healthchecks pings into scheduled jobs
   - Ping on start/success/fail.

## Quick Checklist

- [ ] Add/verify `@EnableScheduling` in backend
- [ ] Create `SchedulerJobs` with 5 jobs
- [ ] Add UptimeRobot monitors
- [ ] Start Prometheus+Grafana (optional but recommended)
- [ ] Configure Sentry DSN in frontend (optional)
- [ ] CodeQL active and Dependabot file added
- [ ] Optionally wire SonarCloud in CI with token

## Commands Reference

- Backend development: `cd magnus-backend && ./mvnw spring-boot:run`
- Frontend development: `cd magnus-frontend && npm run dev`
- Development Docker: `docker compose -f docker-compose.dev.yml up --build`
- Production Docker: `docker compose -f docker-compose.prod.yml up --build -d`
- Local Sonar: `docker compose -f magnus-backend/src/main/docker/sonar.yml up -d`
- Sonar analysis: `cd magnus-backend && ./mvnw -Pprod clean verify sonar:sonar`

## 1) Background Agents (Scheduled Jobs)

Purpose: automate repetitive ops (shopping consolidation, reminders, price freshness, daily digests, hygiene) so the team spends less time on manual coordination.

Where to enable scheduling:
- File: `magnus-backend/src/main/java/com/magnus/MagnusApp.java`
- Ensure the application has `@EnableScheduling` on a configuration class. If it is missing, add it to the main Spring Boot application class.

Where to add jobs:
- File: `magnus-backend/src/main/java/com/magnus/service/SchedulerJobs.java`
- Class notes:
  - Annotate with `@Service`.
  - Add one method per job with `@Scheduled(cron = "...")`.
  - Make jobs idempotent (safe to re-run).

Recommended jobs (cron schedules):
- Weekly shopping list consolidation: Mon 06:00
  - Description: Consolidate `ShoppingItem`s for the week across budgets.
- Reservation deadline reminders: Daily 09:00
  - Description: Notify sales/logistics of budgets near event date missing tasks/purchases.
- Price freshness watchdog: Daily 07:00
  - Description: Flag `Product` prices older than N days for admin review.
- Notification digests: Daily 17:00
  - Description: One actionable digest per role (sales/logistics/cook) to reduce noise.
- Data hygiene & maintenance: Daily 02:30
  - Description: Clean stale rows, rotate/trim logs, refresh caches/indexes.

Verifying jobs
- Run backend in dev: `cd magnus-backend && ./mvnw spring-boot:run`
- Logs: confirm each job logs a start/end entry at the scheduled time.
- Health endpoints: `http://localhost:8080/management/health` should be UP.

Optional reliability pings
- Healthchecks.io: create a check per job and add pings at job start/end so you get alerts if schedules stop running.

## 2) CI & Security

### 2.1 CodeQL (already configured)
- Workflow file: `.github/workflows/ci.yml`
- What it does: scans Java and JS/TS on push/PR; shows alerts in GitHub → Security → Code scanning.
- Optional: add a weekly scheduled scan by adding to `on:` in the workflow:
  ```yaml
  schedule:
    - cron: '0 3 * * 1'
  ```

### 2.2 Dependabot (dependency updates)
- Config file: `.github/dependabot.yml` (add this file)
- Action: automatically opens PRs to update Maven and npm dependencies weekly.

Suggested content:
```yaml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/magnus-backend"
    schedule:
      interval: "weekly"
  - package-ecosystem: "npm"
    directory: "/magnus-frontend"
    schedule:
      interval: "weekly"
    open-pull-requests-limit: 5
```

## 3) Code Quality (SonarQube / SonarCloud)

Option A: Local SonarQube (free)
- Start the server:
  ```bash
  docker compose -f magnus-backend/src/main/docker/sonar.yml up -d
  ```
- Analyze backend code:
  ```bash
  cd magnus-backend
  ./mvnw -Pprod clean verify sonar:sonar
  ```
- Sonar UI: `http://localhost:9001`
- Config file used: `magnus-backend/sonar-project.properties`

Option B: SonarCloud (hosted)
- Create a project on SonarCloud; add `SONAR_TOKEN` as a GitHub secret.
- Extend `.github/workflows/ci.yml` to run:
  ```bash
  ./mvnw -Pprod sonar:sonar -Dsonar.login=${{ secrets.SONAR_TOKEN }}
  ```

## 4) Observability and Uptime

### 4.1 Prometheus + Grafana (already scaffolded by JHipster)
- Start stack:
  ```bash
  docker compose -f magnus-backend/src/main/docker/monitoring.yml up -d
  ```
- Files:
  - Prometheus config: `magnus-backend/src/main/docker/prometheus/prometheus.yml`
  - Grafana provisioning: `magnus-backend/src/main/docker/grafana/provisioning/`
- UI:
  - Prometheus: `http://localhost:9090`
  - Grafana: `http://localhost:3000`
- Dashboards: import JVM/Spring dashboards, target backend Prometheus endpoint (pre-configured in compose).

### 4.2 Uptime monitoring (UptimeRobot or similar)
- Add monitors for:
  - Backend health: `http://<host>:8080/management/health`
  - Frontend: `http://<host>:8081`
- For prod, ensure actuation endpoints are exposed (defaults are ok in JHipster prod profile):
  - `magnus-backend/src/main/resources/config/application-prod.yml`

## 5) Error Tracking (Sentry – frontend)

- Environment variables (template): `magnus-frontend/environment.template`
  - Set:
    ```env
    VITE_SENTRY_DSN=your_sentry_dsn
    VITE_SENTRY_ENVIRONMENT=production
    ```
- Initialize Sentry in frontend entrypoint:
  - File: `magnus-frontend/src/main.tsx` (add Sentry init at app bootstrap)
- Build: `cd magnus-frontend && npm run build`

## 6) Job Reliability (Healthchecks.io)

- Create checks per scheduled job.
- In each scheduled method (see `SchedulerJobs`), issue an HTTP request to the check’s URL at success/fail as desired.
- Helps detect cron failures quickly.

## 7) Docker Orchestration (for dev and prod)

### Dev (hot reload + DB + backend + frontend)
- File: `docker-compose.dev.yml`
- Run:
  ```bash
  docker compose -f docker-compose.dev.yml up --build
  ```
- Services:
  - MySQL @ 3306
  - Backend @ 8080 (dev runner)
  - Frontend @ 5173 (Vite) – uses `VITE_API_URL=http://localhost:8080/api`

### Prod (Nginx + backend)
- File: `docker-compose.prod.yml`
- Run:
  ```bash
  docker compose -f docker-compose.prod.yml up --build -d
  ```
- Services:
  - Backend @ 8080
  - Frontend @ 8081 (Nginx serving `magnus-frontend/dist`)
- Nginx proxy: `magnus-frontend/nginx.conf` (proxies `/api/` to backend)

## 8) When to use what (Magnus specifically)

- Background agents: keep operations flowing
  - Weekly shopping → produce consolidated shopping lists for logistics
  - Reservation reminders → keeps budgets moving toward execution
  - Price freshness → maintain margins; admins review flagged products
  - Role digests → summarize “do this today” per role (reduce noise)
  - Hygiene → long-term performance

- CodeQL & Dependabot: always on
  - Review alerts weekly; prioritize critical/high

- Sonar: quality gates and trends
  - Run locally weekly or on PRs (SonarCloud)

- Prometheus + Grafana: visibility
  - Monitor JVM, request rates, errors; alert on spikes

- UptimeRobot: external uptime confirmation
  - Alerts on outages quickly

- Sentry: real‑world errors
  - Capture frontend exceptions with user context; triage weekly

## 9) Do I need to modify the project?

- Not required for: CodeQL (already present), Docker orchestration (present), Prometheus/Grafana (compose present), UptimeRobot (external), Dependabot (add file above).
- Recommended to implement:
  - Ensure scheduling is enabled (`@EnableScheduling`)
  - Add `SchedulerJobs` class (see section 1)
  - Initialize Sentry in `magnus-frontend/src/main.tsx` if you set DSN
  - Optionally add Healthchecks pings into scheduled jobs

## 10) Quick Checklist

- [ ] Add/verify `@EnableScheduling` in backend
- [ ] Create `SchedulerJobs` with 5 jobs
- [ ] Add UptimeRobot monitors
- [ ] Start Prometheus+Grafana (optional but recommended)
- [ ] Configure Sentry DSN in frontend (optional)
- [ ] CodeQL active (already) and Dependabot file (added above)
- [ ] Optionally wire SonarCloud in CI with token

## 11) Commands Reference

- Backend dev: `cd magnus-backend && ./mvnw spring-boot:run`
- Frontend dev: `cd magnus-frontend && npm run dev`
- Dev Docker: `docker compose -f docker-compose.dev.yml up --build`
- Prod Docker: `docker compose -f docker-compose.prod.yml up --build -d`
- Local Sonar: `docker compose -f magnus-backend/src/main/docker/sonar.yml up -d`
- Sonar analysis: `cd magnus-backend && ./mvnw -Pprod clean verify sonar:sonar`

---

If you need me to implement any of the optional steps above, I can do so in a separate branch without touching business logic.
