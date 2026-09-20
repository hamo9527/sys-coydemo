<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand" title="材陆建材">
        <span class="brand-mark">材</span>
      </div>
      <nav class="side-nav">
        <button
          v-for="item in sideMenus"
          :key="item.key"
          type="button"
          class="side-item"
          :class="{ active: activeKey === item.key }"
          @click="handleSideClick(item)"
        >
          <el-icon :size="18">
            <component :is="iconMap[item.icon]" />
          </el-icon>
          <span>{{ item.title }}</span>
        </button>
      </nav>
    </aside>

    <section class="main">
      <header class="header">
        <div class="header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-input
            v-model="keyword"
            class="search"
            clearable
            placeholder="搜索商品库存"
            :prefix-icon="Search"
          />
          <el-button text class="icon-btn">
            <el-icon :size="18"><Calendar /></el-icon>
          </el-button>
          <el-badge is-dot class="notify">
            <el-button text class="icon-btn">
              <el-icon :size="18"><Bell /></el-icon>
            </el-button>
          </el-badge>
          <div class="user">
            <el-avatar :size="28" class="avatar">{{ avatarText }}</el-avatar>
            <span>{{ displayName || '系统管理员' }}</span>
          </div>
          <el-button text type="danger" @click="handleLogout">退出</el-button>
        </div>
      </header>

      <div v-if="tabs.length" class="tabs-bar" @click="hideContextMenu">
        <button
          v-for="tab in tabs"
          :key="tab.fullPath"
          type="button"
          class="tab"
          :class="{ active: tab.fullPath === route.fullPath }"
          @click="router.push(tab.fullPath)"
          @contextmenu.prevent="openContextMenu($event, tab.fullPath)"
        >
          <span>{{ tab.title }}</span>
          <el-icon
            v-if="!tab.affix"
            class="tab-close"
            @click.stop="handleClose(tab.fullPath)"
          >
            <Close />
          </el-icon>
        </button>
      </div>

      <ul
        v-show="context.visible"
        class="tab-menu"
        :style="{ left: `${context.x}px`, top: `${context.y}px` }"
        @click.stop
      >
        <li @click="runMenuAction('close')">
          <el-icon><Close /></el-icon>
          <span>关闭</span>
        </li>
        <li @click="runMenuAction('refresh')">
          <el-icon><RefreshRight /></el-icon>
          <span>刷新</span>
        </li>
        <li class="divider" />
        <li @click="runMenuAction('others')">
          <el-icon><Remove /></el-icon>
          <span>关闭其他</span>
        </li>
        <li @click="runMenuAction('all')">
          <el-icon><CircleClose /></el-icon>
          <span>关闭全部</span>
        </li>
        <li @click="runMenuAction('right')">
          <el-icon><DArrowRight /></el-icon>
          <span>关闭右侧所有</span>
        </li>
        <li @click="runMenuAction('left')">
          <el-icon><DArrowLeft /></el-icon>
          <span>关闭左侧所有</span>
        </li>
      </ul>

      <main class="content" @click="hideContextMenu">
        <router-view :key="viewKey" />
      </main>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch, type Component } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'
import {
  Aim,
  Bell,
  Box,
  Calendar,
  CircleClose,
  Close,
  Connection,
  DArrowLeft,
  DArrowRight,
  DataAnalysis,
  Download,
  HomeFilled,
  Money,
  Notebook,
  RefreshRight,
  Remove,
  Search,
  Service,
  Setting,
  Share,
  Shop,
  Upload,
} from '@element-plus/icons-vue'
import { sideMenus, type SideMenuItem } from '@/router/menus'
import { useTabsStore } from '@/stores/tabs'
import { useUserStore } from '@/stores/user'

type MenuAction = 'close' | 'refresh' | 'others' | 'all' | 'right' | 'left'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const tabsStore = useTabsStore()
const { displayName } = storeToRefs(userStore)
const { tabs, refreshKey } = storeToRefs(tabsStore)

const keyword = ref('')
const context = reactive({
  visible: false,
  x: 0,
  y: 0,
  fullPath: '',
})

const iconMap: Record<string, Component> = {
  HomeFilled,
  Aim,
  Download,
  Upload,
  Box,
  Money,
  Service,
  Share,
  Connection,
  Notebook,
  Setting,
  DataAnalysis,
  Shop,
}

const activeKey = computed(() => {
  const module = route.meta.module
  if (typeof module === 'string' && module) {
    return module
  }
  if (route.path.startsWith('/dashboard')) {
    return 'home'
  }
  return ''
})

const currentTitle = computed(() => {
  const queryTitle = typeof route.query.title === 'string' ? route.query.title : ''
  return queryTitle || String(route.meta.title || '首页')
})
const avatarText = computed(() => (displayName.value || '管').slice(0, 1))
const viewKey = computed(() => `${route.fullPath}::${refreshKey.value}`)

watch(
  () => route.fullPath,
  () => {
    const queryTitle = typeof route.query.title === 'string' ? route.query.title : ''
    const title = queryTitle || String(route.meta.title || '未命名')
    tabsStore.upsertTab({
      title,
      path: route.path,
      fullPath: route.fullPath,
      affix: route.path === '/dashboard',
    })
  },
  { immediate: true },
)

function handleSideClick(item: SideMenuItem) {
  if (item.path) {
    router.push(item.path)
    return
  }
  router.push(`/module/${item.key}`)
}

function handleClose(fullPath: string) {
  const next = tabsStore.closeTab(fullPath)
  if (next && route.fullPath === fullPath) {
    router.push(next)
  }
}

function openContextMenu(event: MouseEvent, fullPath: string) {
  context.visible = true
  context.fullPath = fullPath
  context.x = Math.min(event.clientX, window.innerWidth - 180)
  context.y = Math.min(event.clientY, window.innerHeight - 240)
}

function hideContextMenu() {
  context.visible = false
}

function runMenuAction(action: MenuAction) {
  const target = context.fullPath
  hideContextMenu()
  if (!target) {
    return
  }

  if (action === 'refresh') {
    if (route.fullPath !== target) {
      router.push(target).then(() => tabsStore.refresh())
      return
    }
    tabsStore.refresh()
    return
  }

  if (action === 'close') {
    handleClose(target)
    return
  }

  if (action === 'others') {
    tabsStore.closeOthers(target)
    if (route.fullPath !== target) {
      router.push(target)
    }
    return
  }

  if (action === 'all') {
    const home = tabsStore.closeAll()
    router.push(home)
    return
  }

  if (action === 'right') {
    tabsStore.closeRight(target)
    if (!tabs.value.some((tab) => tab.fullPath === route.fullPath)) {
      router.push(target)
    }
    return
  }

  if (action === 'left') {
    tabsStore.closeLeft(target)
    if (!tabs.value.some((tab) => tab.fullPath === route.fullPath)) {
      router.push(target)
    }
  }
}

function handleLogout() {
  userStore.logout()
  router.push({ name: 'Login' })
}

onMounted(() => {
  window.addEventListener('click', hideContextMenu)
  window.addEventListener('blur', hideContextMenu)
})

onBeforeUnmount(() => {
  window.removeEventListener('click', hideContextMenu)
  window.removeEventListener('blur', hideContextMenu)
})
</script>

<style scoped lang="scss">
.layout {
  display: flex;
  min-height: 100vh;
  background: var(--cailu-bg);
}

.sidebar {
  width: 72px;
  flex-shrink: 0;
  background: var(--cailu-sidebar);
  color: #d7deea;
  display: flex;
  flex-direction: column;
}

.brand {
  height: 56px;
  display: grid;
  place-items: center;
  border-bottom: 1px solid rgb(255 255 255 / 6%);
}

.brand-mark {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #4d7fff, #2f6bff);
  color: #fff;
  font-weight: 700;
  display: grid;
  place-items: center;
}

.side-nav {
  flex: 1;
  overflow: auto;
  padding: 8px 0 16px;
}

.side-item {
  width: 100%;
  border: 0;
  background: transparent;
  color: inherit;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 4px;
  font-size: 12px;
  cursor: pointer;
}

.side-item:hover,
.side-item.active {
  background: var(--cailu-sidebar-active);
  color: #fff;
}

.main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.header {
  height: 56px;
  background: #fff;
  border-bottom: 1px solid var(--cailu-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search {
  width: 220px;
}

.icon-btn {
  color: #5b677a;
}

.notify :deep(.el-badge__content.is-dot) {
  right: 10px;
  top: 8px;
}

.user {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 4px;
  color: #3a465a;
  font-size: 13px;
}

.avatar {
  background: #2f6bff;
  color: #fff;
  font-size: 13px;
}

.tabs-bar {
  display: flex;
  gap: 4px;
  padding: 8px 12px 0;
  background: #f7f8fa;
  border-bottom: 1px solid var(--cailu-border);
  overflow-x: auto;
}

.tab {
  border: 1px solid var(--cailu-border);
  border-bottom: 0;
  background: #eef1f6;
  color: #5b677a;
  border-radius: 6px 6px 0 0;
  padding: 6px 12px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  cursor: pointer;
  white-space: nowrap;
}

.tab.active {
  background: #fff;
  color: var(--cailu-primary);
  font-weight: 600;
}

.tab-close {
  font-size: 12px;
}

.tab-menu {
  position: fixed;
  z-index: 3000;
  margin: 0;
  padding: 6px 0;
  list-style: none;
  min-width: 160px;
  background: #fff;
  border: 1px solid var(--cailu-border);
  border-radius: 8px;
  box-shadow: 0 8px 24px rgb(16 24 40 / 12%);
}

.tab-menu li {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  font-size: 13px;
  color: #3a465a;
  cursor: pointer;
}

.tab-menu li:hover {
  background: #f5f7fb;
  color: var(--cailu-primary);
}

.tab-menu .divider {
  height: 1px;
  padding: 0;
  margin: 4px 0;
  background: var(--cailu-border);
  cursor: default;
}

.tab-menu .divider:hover {
  background: var(--cailu-border);
}

.content {
  flex: 1;
  padding: 12px 16px 20px;
  overflow: auto;
}
</style>
