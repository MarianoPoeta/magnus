# Base Java Dev Container
ARG VARIANT="17-bullseye"
FROM mcr.microsoft.com/vscode/devcontainers/java:0-${VARIANT}

# Install Node via NVM (same as backend devcontainer pattern)
ARG NODE_VERSION="22.15.0"
RUN if [ "${NODE_VERSION}" != "none" ]; then \
    su vscode -c "umask 0002 && . /usr/local/share/nvm/nvm.sh && nvm install ${NODE_VERSION} 2>&1 && nvm alias default ${NODE_VERSION}"; \
  fi

# Optional: global utilities
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm i -g pnpm bun" 2>&1 || true

