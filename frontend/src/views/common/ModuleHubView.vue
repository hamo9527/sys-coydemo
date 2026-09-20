<template>
  <div class="hub page-card">
    <div v-if="!menu" class="missing">
      <el-empty description="未找到该模块" />
    </div>
    <template v-else>
      <div class="hub-header">
        <h2>{{ menu.title }}</h2>
        <p>单据 · 报表 · 提醒</p>
      </div>
      <div class="hub-columns" :style="{ gridTemplateColumns: `repeat(${columns.length}, minmax(0, 1fr))` }">
        <section v-for="column in columns" :key="column.title" class="hub-column">
          <h3>{{ column.title }}</h3>
          <button
            v-for="link in column.links"
            :key="link.title"
            type="button"
            class="hub-link"
            :class="{ 'is-ready': link.ready }"
            @click="openLink(link)"
          >
            {{ link.title }}
            <span v-if="link.ready" class="badge">可用</span>
          </button>
        </section>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { findSideMenu, type HubLink } from '@/router/menus'

const route = useRoute()
const router = useRouter()

const menu = computed(() => {
  const key = String(route.params.key || '')
  return findSideMenu(key)
})

const columns = computed(() => menu.value?.hub || [])

function openLink(link: HubLink) {
  if (!link.path) {
    return
  }
  if (link.ready) {
    router.push(link.path)
    return
  }
  router.push({ path: link.path, query: { title: link.title } })
  ElMessage.info(`${link.title}：界面已按素材预留，业务开发中`)
}
</script>

<style scoped lang="scss">
.hub {
  min-height: calc(100vh - 140px);
}

.hub-header {
  margin-bottom: 20px;
}

.hub-header h2 {
  margin: 0 0 4px;
  font-size: 18px;
}

.hub-header p {
  margin: 0;
  color: var(--cailu-muted);
  font-size: 13px;
}

.hub-columns {
  display: grid;
  gap: 0;
}

.hub-column {
  padding: 0 28px;
  border-right: 1px dashed var(--cailu-border);
}

.hub-column:first-child {
  padding-left: 8px;
}

.hub-column:last-child {
  border-right: 0;
}

.hub-column h3 {
  margin: 0 0 12px;
  font-size: 15px;
}

.hub-link {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  border: 0;
  background: transparent;
  text-align: left;
  padding: 0;
}

.badge {
  font-size: 11px;
  color: #19b26b;
  background: #e9f9f0;
  border-radius: 999px;
  padding: 1px 6px;
}

@media (max-width: 960px) {
  .hub-columns {
    grid-template-columns: 1fr !important;
  }

  .hub-column {
    border-right: 0;
    border-bottom: 1px dashed var(--cailu-border);
    padding: 12px 0;
  }
}
</style>
