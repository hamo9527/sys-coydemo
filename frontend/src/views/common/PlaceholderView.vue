<template>
  <div class="page-card placeholder">
    <el-result icon="info" :title="title" sub-title="该功能按素材图菜单已预留，业务接口开发中。">
      <template #extra>
        <el-button type="primary" @click="router.back()">返回</el-button>
        <el-button @click="router.push('/dashboard')">回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const title = computed(() => {
  const fromQuery = route.query.title
  if (typeof fromQuery === 'string' && fromQuery) {
    return fromQuery
  }
  return String(route.meta.title || '功能开发中')
})
</script>

<style scoped lang="scss">
.placeholder {
  min-height: 420px;
  display: grid;
  place-items: center;
}
</style>
