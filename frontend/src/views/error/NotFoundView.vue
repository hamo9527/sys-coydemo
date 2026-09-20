<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面不存在</p>
    <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.not-found {
  min-height: 60vh;
  display: grid;
  place-content: center;
  text-align: center;
  gap: 8px;
}

h1 {
  margin: 0;
  font-size: 64px;
  color: var(--cailu-primary);
}

p {
  margin: 0 0 12px;
  color: var(--cailu-muted);
}
</style>
