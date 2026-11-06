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
          <div class="search-wrap">
            <el-input
              v-model="keyword"
              class="search"
              clearable
              placeholder="搜索系统功能 / 单据 / 报表"
              :prefix-icon="Search"
              @focus="searchVisible = true"
              @keydown="handleSearchKeydown"
            />
            <div v-if="searchVisible && keyword.trim()" class="search-dropdown">
              <template v-if="filteredSearchItems.length">
                <div class="search-section-title">系统功能</div>
                <button
                  v-for="(item, index) in filteredSearchItems"
                  :key="`${item.path}-${item.title}`"
                  type="button"
                  class="search-option"
                  :class="{ selected: index === searchActiveIndex }"
                  @mousedown.prevent
                  @mouseenter="searchActiveIndex = index"
                  @click="openSearchItem(item)"
                >
                  <span class="search-option-main">
                    <span class="search-option-title">{{ item.title }}</span>
                    <span class="search-option-module">{{ item.module }}</span>
                  </span>
                  <span class="search-option-hint">进入功能</span>
                  <el-icon><ArrowRight /></el-icon>
                </button>
              </template>

              <div v-if="productLoading || productResults.length" class="search-section-title">
                商品
                <span v-if="productLoading" class="search-loading">正在搜索…</span>
              </div>
              <button
                v-for="(product, index) in productResults"
                :key="`product-${product.id}`"
                type="button"
                class="search-option product-option"
                :class="{ selected: filteredSearchItems.length + index === searchActiveIndex }"
                @mousedown.prevent
                @mouseenter="searchActiveIndex = filteredSearchItems.length + index"
                @click="openProductResult(product)"
              >
                <span class="product-search-icon"><el-icon><Box /></el-icon></span>
                <span class="search-option-main">
                  <span class="search-option-title">{{ product.name }}</span>
                  <span class="search-option-module">
                    {{ product.sku }} · {{ product.brandName }}<template v-if="product.spec"> · {{ product.spec }}</template>
                  </span>
                </span>
                <span class="search-option-hint">查看商品</span>
                <el-icon><ArrowRight /></el-icon>
              </button>

              <div
                v-if="!filteredSearchItems.length && !productLoading && !productResults.length"
                class="search-empty"
              >
                没有找到匹配的功能或商品
              </div>
              <div v-if="productResults.length" class="search-footer">
                <span>商品搜索结果</span>
                <span>共显示 {{ productResults.length }} 条</span>
              </div>
            </div>
          </div>
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
  ArrowRight,
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
import { listProducts } from '@/api/erp'
import type { ProductView } from '@/types/erp'

type MenuAction = 'close' | 'refresh' | 'others' | 'all' | 'right' | 'left'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const tabsStore = useTabsStore()
const { displayName } = storeToRefs(userStore)
const { tabs, refreshKey } = storeToRefs(tabsStore)

type SearchItem = {
  title: string
  module: string
  path: string
  keywords: string[]
}

const keyword = ref('')
const searchVisible = ref(false)
const searchActiveIndex = ref(0)
const productLoading = ref(false)
const productResults = ref<ProductView[]>([])
let productSearchTimer: ReturnType<typeof setTimeout> | undefined

const searchItems = computed<SearchItem[]>(() => {
  const items: SearchItem[] = [
    { title: '首页', module: '首页', path: '/dashboard', keywords: ['工作台', '主页'] },
  ]

  for (const menu of sideMenus) {
    if (menu.path) {
      items.push({
        title: menu.title,
        module: menu.title,
        path: menu.path,
        keywords: [menu.key],
      })
    } else {
      items.push({
        title: menu.title,
        module: menu.title,
        path: `/module/${menu.key}`,
        keywords: [menu.key],
      })
    }

    for (const column of menu.hub || []) {
      for (const link of column.links) {
        if (!link.path) continue
        items.push({
          title: link.title,
          module: `${menu.title} · ${column.title}`,
          path: link.path,
          keywords: [menu.key, column.title, link.ready ? '已实现' : '功能预留'],
        })
      }
    }
  }

  const unique = new Map<string, SearchItem>()
  for (const item of items) {
    const key = `${item.path}::${item.title}`
    if (!unique.has(key)) unique.set(key, item)
  }
  return [...unique.values()]
})

const filteredSearchItems = computed(() => {
  const query = keyword.value.trim().toLocaleLowerCase()
  if (!query) return []

  return searchItems.value
    .filter((item) => {
      const searchableText = [item.title, item.module, ...item.keywords]
        .join(' ')
        .toLocaleLowerCase()
      return searchableText.includes(query)
    })
    .slice(0, 8)
})

const searchResultCount = computed(() => filteredSearchItems.value.length + productResults.value.length)

watch(keyword, (value) => {
  const query = value.trim()
  searchActiveIndex.value = 0
  searchVisible.value = Boolean(query)
  productResults.value = []
  productLoading.value = false

  if (productSearchTimer) clearTimeout(productSearchTimer)
  if (!query) return

  productSearchTimer = setTimeout(async () => {
    productLoading.value = true
    try {
      const result = await listProducts({ keyword: query, page: 1, size: 6 })
      if (keyword.value.trim() === query) {
        productResults.value = result.records
      }
    } catch {
      if (keyword.value.trim() === query) productResults.value = []
    } finally {
      if (keyword.value.trim() === query) productLoading.value = false
    }
  }, 250)
})
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

function openSearchItem(item: SearchItem) {
  searchVisible.value = false
  keyword.value = ''
  searchActiveIndex.value = 0
  router.push(item.path)
}

function openProductResult(product: ProductView) {
  const productKeyword = product.sku || product.name
  searchVisible.value = false
  keyword.value = ''
  searchActiveIndex.value = 0
  router.push({ path: '/erp/products', query: { keyword: productKeyword } })
}

function handleSearchKeydown(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    searchVisible.value = false
    return
  }

  if (event.key === 'ArrowDown') {
    event.preventDefault()
    searchVisible.value = Boolean(keyword.value.trim())
    if (searchResultCount.value) {
      searchActiveIndex.value = (searchActiveIndex.value + 1) % searchResultCount.value
    }
    return
  }

  if (event.key === 'ArrowUp') {
    event.preventDefault()
    if (searchResultCount.value) {
      searchActiveIndex.value =
        (searchActiveIndex.value - 1 + searchResultCount.value) % searchResultCount.value
    }
    return
  }

  if (event.key === 'Enter' && searchResultCount.value) {
    event.preventDefault()
    const index = searchActiveIndex.value
    if (index < filteredSearchItems.value.length) {
      openSearchItem(filteredSearchItems.value[index])
    } else {
      const product = productResults.value[index - filteredSearchItems.value.length]
      if (product) openProductResult(product)
    }
  }
}

function handleSearchOutsideClick(event: MouseEvent) {
  const target = event.target
  if (target instanceof Element && !target.closest('.search-wrap')) {
    searchVisible.value = false
  }
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
  window.addEventListener('click', handleSearchOutsideClick)
  window.addEventListener('blur', hideContextMenu)
})

onBeforeUnmount(() => {
  window.removeEventListener('click', hideContextMenu)
  window.removeEventListener('click', handleSearchOutsideClick)
  window.removeEventListener('blur', hideContextMenu)
  if (productSearchTimer) clearTimeout(productSearchTimer)
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

.search-wrap {
  position: relative;
}

.search {
  width: 260px;
}

.search-dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  z-index: 3100;
  width: 390px;
  max-height: 440px;
  overflow-y: auto;
  padding: 8px;
  background: #fff;
  border: 1px solid var(--cailu-border);
  border-radius: 10px;
  box-shadow: 0 12px 32px rgb(16 24 40 / 16%);
}

.search-section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 10px 5px;
  color: #8a94a6;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.04em;
}

.search-loading {
  font-weight: 400;
  letter-spacing: 0;
}

.search-option-hint {
  margin-left: auto;
  color: #9aa4b2;
  font-size: 11px;
  white-space: nowrap;
}

.product-option {
  justify-content: flex-start;
}

.product-search-icon {
  width: 30px;
  height: 30px;
  flex: 0 0 30px;
  display: grid;
  place-items: center;
  border-radius: 7px;
  background: #eef4ff;
  color: var(--cailu-primary);
}

.search-footer {
  display: flex;
  justify-content: space-between;
  padding: 8px 10px 3px;
  border-top: 1px solid #edf0f5;
  color: #9aa4b2;
  font-size: 11px;
}

.search-option {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 9px 10px;
  border: 0;
  border-radius: 6px;
  background: transparent;
  color: #3a465a;
  text-align: left;
  cursor: pointer;
}

.search-option:hover,
.search-option.selected {
  background: #f2f5fb;
  color: var(--cailu-primary);
}

.search-option-main {
  display: flex;
  flex-direction: column;
  gap: 3px;
  min-width: 0;
}

.search-option-title {
  font-size: 13px;
  font-weight: 600;
}

.search-option-module {
  color: #8a94a6;
  font-size: 11px;
}

.search-empty {
  padding: 16px;
  color: #8a94a6;
  font-size: 13px;
  text-align: center;
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
