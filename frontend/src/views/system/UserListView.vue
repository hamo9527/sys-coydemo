<template>
  <div class="page-card">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="dialogVisible = true">新建用户</el-button>
    </div>

    <el-form :inline="true" class="query" @submit.prevent="handleLookup">
      <el-form-item label="用户 ID">
        <el-input v-model="queryId" placeholder="按 ID 查询" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" native-type="submit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" stripe empty-text="暂无用户">
      <el-table-column prop="id" label="ID" width="120" />
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="realName" label="姓名" min-width="120" />
      <el-table-column prop="mobile" label="手机" min-width="120" />
      <el-table-column prop="orgId" label="组织" width="100" />
      <el-table-column prop="status" label="状态" width="80" />
    </el-table>

    <el-dialog v-model="dialogVisible" title="新建用户" width="480px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="form.mobile" />
        </el-form-item>
        <el-form-item label="组织 ID" prop="orgId">
          <el-input v-model="form.orgId" clearable placeholder="可选" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleCreate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { createUser, getUser } from '@/api/system'
import type { UserView } from '@/types/system'

const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const queryId = ref('')
const rows = ref<UserView[]>([])
const formRef = ref<FormInstance>()

const form = reactive<{
  username: string
  password: string
  realName: string
  mobile: string
  orgId: string
}>({
  username: '',
  password: '',
  realName: '',
  mobile: '',
  orgId: '',
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 64, message: '密码长度 6-64', trigger: 'blur' },
  ],
}

async function handleLookup() {
  const id = queryId.value.trim()
  if (!id) {
    ElMessage.warning('请输入有效用户 ID')
    return
  }
  loading.value = true
  try {
    const user = await getUser(id)
    rows.value = [user]
  } finally {
    loading.value = false
  }
}

async function handleCreate() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  saving.value = true
  try {
    const user = await createUser({
      username: form.username,
      password: form.password,
      realName: form.realName || undefined,
      mobile: form.mobile || undefined,
      orgId: form.orgId.trim() || undefined,
    })
    rows.value = [user, ...rows.value.filter((item) => item.id !== user.id)]
    dialogVisible.value = false
    ElMessage.success('用户已创建')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.query {
  margin-bottom: 8px;
}
</style>
