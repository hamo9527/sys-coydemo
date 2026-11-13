<template>
  <div class="page-card">
    <div class="page-header">
      <h2>仓库档案</h2>
      <el-button type="primary" @click="openCreate">新增仓库</el-button>
    </div>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="关键字"><el-input v-model="keyword" clearable placeholder="仓库编码/名称" /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="status" clearable placeholder="全部" style="width: 130px">
          <el-option label="启用" :value="1" /><el-option label="停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" native-type="submit">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" v-loading="loading" stripe>
      <el-table-column prop="code" label="仓库编码" width="160" />
      <el-table-column prop="name" label="仓库名称" min-width="180" />
      <el-table-column prop="address" label="地址" min-width="220" />
      <el-table-column label="状态" width="100"><template #default="{ row }">{{ row.status === 1 ? '启用' : '停用' }}</template></el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row as WarehouseView)">编辑</el-button>
          <el-button link type="danger" @click="remove(row as WarehouseView)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="visible" :title="editingId ? '编辑仓库' : '新增仓库'" width="520px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="仓库编码" prop="code"><el-input v-model="form.code" /></el-form-item>
        <el-form-item label="仓库名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="仓库地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createWarehouse, deleteWarehouse, listWarehouses, updateWarehouse } from '@/api/erp'
import type { WarehouseView } from '@/types/erp'

const rows = ref<WarehouseView[]>([])
const loading = ref(false)
const saving = ref(false)
const keyword = ref('')
const status = ref<number>()
const visible = ref(false)
const editingId = ref<string | number | null>(null)
const formRef = ref<FormInstance>()
const form = reactive({ code: '', name: '', address: '', status: 1 })
const rules: FormRules = {
  code: [{ required: true, message: '请输入仓库编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入仓库名称', trigger: 'blur' }],
}

async function load() {
  loading.value = true
  try { rows.value = await listWarehouses({ keyword: keyword.value || undefined, status: status.value }) }
  finally { loading.value = false }
}
function openCreate() {
  editingId.value = null
  Object.assign(form, { code: '', name: '', address: '', status: 1 })
  visible.value = true
}
function openEdit(row: WarehouseView) {
  editingId.value = row.id
  Object.assign(form, { code: row.code, name: row.name, address: row.address || '', status: row.status })
  visible.value = true
}
async function save() {
  if (!(await formRef.value?.validate().catch(() => false))) return
  saving.value = true
  try {
    if (editingId.value === null) await createWarehouse({ ...form })
    else await updateWarehouse(editingId.value, { ...form })
    ElMessage.success('仓库档案已保存')
    visible.value = false
    await load()
  } finally { saving.value = false }
}
async function remove(row: WarehouseView) {
  await ElMessageBox.confirm(`确定删除仓库“${row.name}”吗？`, '删除确认', { type: 'warning' })
  await deleteWarehouse(row.id)
  ElMessage.success('仓库已删除')
  await load()
}
onMounted(() => { void load() })
</script>
