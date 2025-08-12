/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_URL: string
  readonly VITE_ENVIRONMENT: string
  readonly VITE_WS_URL?: string
  readonly VITE_WEBSOCKET_URL?: string
  readonly VITE_JWT_STORAGE_KEY?: string
  // more env variables...
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
