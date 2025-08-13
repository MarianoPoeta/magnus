# Operations and Background Agents Setup (Magnus)

This document explains how to enable and operate background agents and the essential tooling for this project, with exact file paths and commands for this repository structure.

Repo layout (relevant):
- Backend (JHipster, Spring Boot): `magnus-backend/`
- Frontend (Vite + React): `magnus-frontend/`
- Docker orchestration: `docker-compose.dev.yml`, `docker-compose.prod.yml`

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
