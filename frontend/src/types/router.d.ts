import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    title?: string
    module?: string
    public?: boolean
  }
}

export {}
