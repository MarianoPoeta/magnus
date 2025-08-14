# Magnus Monorepo

This repository contains the `magnus-frontend` (Vite/React/TS) and `magnus-backend` (Spring Boot) projects.

## Automation & CI
- GitHub Actions workflow at `.github/workflows/ci.yml` builds and lints both projects and runs CodeQL.
- Dependabot is configured for Maven and npm.
- PR template and CODEOWNERS enable standardized review and bot feedback.

## Local development
- Frontend: see `magnus-frontend/README.md`
- Backend: see `magnus-backend/README.md`

## Notes for AI/automation tools
- Prefer opening PRs on feature branches. CI will validate FE/BE.
- Lint rules are temporarily lenient to enable incremental typing and refactors. Tighten gradually.

