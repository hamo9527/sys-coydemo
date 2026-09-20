import { defineStore } from 'pinia'
import { ref } from 'vue'

export type AppTab = {
  title: string
  path: string
  fullPath: string
  affix?: boolean
}

const HOME_TAB: AppTab = {
  title: '首页',
  path: '/dashboard',
  fullPath: '/dashboard',
  affix: true,
}

export const useTabsStore = defineStore('tabs', () => {
  const tabs = ref<AppTab[]>([{ ...HOME_TAB }])
  const refreshKey = ref(0)

  function upsertTab(tab: Omit<AppTab, 'affix'> & { affix?: boolean }) {
    const existing = tabs.value.find((item) => item.fullPath === tab.fullPath)
    if (existing) {
      existing.title = tab.title
      existing.path = tab.path
      return
    }
    tabs.value.push({
      title: tab.title,
      path: tab.path,
      fullPath: tab.fullPath,
      affix: tab.affix ?? false,
    })
  }

  function closeTab(fullPath: string) {
    const index = tabs.value.findIndex((tab) => tab.fullPath === fullPath)
    if (index < 0) {
      return null
    }
    const target = tabs.value[index]
    if (target?.affix) {
      return null
    }
    tabs.value.splice(index, 1)
    if (tabs.value.length === 0) {
      tabs.value.push({ ...HOME_TAB })
    }
    const next = tabs.value[index] || tabs.value[index - 1] || tabs.value[0]
    return next?.fullPath ?? HOME_TAB.fullPath
  }

  function closeOthers(fullPath: string) {
    const current = tabs.value.find((tab) => tab.fullPath === fullPath)
    tabs.value = tabs.value.filter((tab) => tab.affix || tab.fullPath === fullPath)
    if (current && !tabs.value.some((tab) => tab.fullPath === fullPath)) {
      tabs.value.push(current)
    }
    if (tabs.value.length === 0) {
      tabs.value.push({ ...HOME_TAB })
    }
  }

  function closeAll() {
    tabs.value = tabs.value.filter((tab) => tab.affix)
    if (tabs.value.length === 0) {
      tabs.value.push({ ...HOME_TAB })
    }
    return HOME_TAB.fullPath
  }

  function closeRight(fullPath: string) {
    const index = tabs.value.findIndex((tab) => tab.fullPath === fullPath)
    if (index < 0) {
      return
    }
    tabs.value = tabs.value.filter((tab, i) => i <= index || tab.affix)
  }

  function closeLeft(fullPath: string) {
    const index = tabs.value.findIndex((tab) => tab.fullPath === fullPath)
    if (index < 0) {
      return
    }
    tabs.value = tabs.value.filter((tab, i) => i >= index || tab.affix)
  }

  function refresh() {
    refreshKey.value += 1
  }

  return {
    tabs,
    refreshKey,
    upsertTab,
    closeTab,
    closeOthers,
    closeAll,
    closeRight,
    closeLeft,
    refresh,
  }
})
