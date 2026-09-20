<template>
  <div class="login-page">
    <div class="login-panel">
      <div class="intro">
        <p class="eyebrow">CAILU BOM</p>
        <h1>材陆建材管理系统</h1>
        <p class="desc">进销存 · 微OA · 财务，一套桌面端工作台。</p>
      </div>
      <el-form class="login-form" :model="form" @submit.prevent="handleLogin">
        <h2>登录</h2>
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            size="large"
            show-password
          />
        </el-form-item>
        <el-button type="primary" size="large" native-type="submit" :loading="loading" class="submit">
          进入系统
        </el-button>
        <p class="hint">默认账号 admin / admin123（首次启动自动创建）</p>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const form = reactive({
  username: 'admin',
  password: 'admin123',
})

async function handleLogin() {
  if (!form.username.trim() || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    await userStore.login(form.username.trim(), form.password)
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    await router.replace(redirect)
  } catch {
    // error toast handled by http interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 24px;
  background:
    radial-gradient(circle at 18% 18%, #d9e6ff 0%, transparent 42%),
    radial-gradient(circle at 88% 8%, #e8efff 0%, transparent 36%),
    linear-gradient(160deg, #f5f7fb 0%, #eef2f8 100%);
}

.login-panel {
  width: min(920px, 100%);
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 50px rgb(27 36 55 / 12%);
}

.intro {
  padding: 48px 40px;
  background:
    linear-gradient(145deg, rgb(27 36 55 / 94%), rgb(47 107 255 / 82%)),
    url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 40 40"><path fill="%23ffffff14" d="M0 40L40 0H20L0 20M40 40V20L20 40"/></svg>');
  color: #fff;
}

.eyebrow {
  margin: 0 0 12px;
  letter-spacing: 0.18em;
  font-size: 12px;
  opacity: 0.8;
}

.intro h1 {
  margin: 0 0 12px;
  font-size: 32px;
  line-height: 1.2;
}

.desc {
  margin: 0;
  opacity: 0.9;
  line-height: 1.6;
}

.login-form {
  padding: 48px 36px;
}

.login-form h2 {
  margin: 0 0 24px;
  font-size: 22px;
}

.submit {
  width: 100%;
  background: var(--cailu-primary);
  border-color: var(--cailu-primary);
}

.hint {
  margin: 16px 0 0;
  color: var(--cailu-muted);
  font-size: 12px;
}

@media (max-width: 768px) {
  .login-panel {
    grid-template-columns: 1fr;
  }

  .intro {
    padding: 28px 24px;
  }
}
</style>
