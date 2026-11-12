<template>
  <div class="page-card">
    <div class="page-header"><h2>色号档案</h2><el-button type="primary" @click="openCreate">新增色号</el-button></div>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="关键字"><el-input v-model="keyword" clearable placeholder="编码/名称/品牌/厂家色号" /></el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" native-type="submit">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" stripe v-loading="loading">
      <el-table-column prop="code" label="内部色号" width="140" /><el-table-column prop="name" label="色号名称" min-width="150" />
      <el-table-column prop="brandName" label="品牌" width="140" /><el-table-column prop="seriesName" label="系列" width="130" />
      <el-table-column prop="manufacturerColorCode" label="厂家色号" width="140" /><el-table-column prop="shortName" label="简称" width="110" />
      <el-table-column label="状态" width="90"><template #default="{ row }">{{ row.status === 1 ? '启用' : '停用' }}</template></el-table-column>
      <el-table-column label="操作" width="150" fixed="right"><template #default="{ row }">
        <el-button link type="primary" @click="openEdit(row)">编辑</el-button><el-button link type="danger" @click="remove(row)">删除</el-button>
      </template></el-table-column>
    </el-table>
    <el-alert class="tip" type="info" :closable="false" title="内部色号编码由本系统维护；厂家色号单独保存，避免不同品牌的厂家编码冲突。" />
    <el-dialog v-model="visible" :title="editingId ? '编辑色号' : '新增色号'" width="560px">
      <el-form :model="form" label-width="110px">
        <el-form-item label="内部色号编码"><el-input v-model="form.code" /></el-form-item><el-form-item label="色号名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="简称"><el-input v-model="form.shortName" /></el-form-item><el-form-item label="品牌"><el-input v-model="form.brandName" /></el-form-item>
        <el-form-item label="系列"><el-input v-model="form.seriesName" /></el-form-item><el-form-item label="厂家色号"><el-input v-model="form.manufacturerColorCode" /></el-form-item>
        <el-form-item label="色卡图片地址"><el-input v-model="form.swatchImageUrl" /></el-form-item><el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="visible = false">取消</el-button><el-button type="primary" :loading="saving" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createColor, deleteColor, listColors, updateColor } from '@/api/erp'
import type { ColorView } from '@/types/erp'
const rows=ref<ColorView[]>([]), keyword=ref(''), loading=ref(false), saving=ref(false), visible=ref(false)
const editingId=ref<string|number|null>(null)
const form=reactive({code:'',name:'',shortName:'',brandName:'',seriesName:'',manufacturerColorCode:'',swatchImageUrl:'',remark:'',status:1})
async function load(){loading.value=true;try{rows.value=await listColors(keyword.value||undefined)}finally{loading.value=false}}
function reset(){Object.assign(form,{code:'',name:'',shortName:'',brandName:'',seriesName:'',manufacturerColorCode:'',swatchImageUrl:'',remark:'',status:1})}
function openCreate(){editingId.value=null;reset();visible.value=true}
function openEdit(row:ColorView){editingId.value=row.id;Object.assign(form,{code:row.code,name:row.name,shortName:row.shortName||'',brandName:row.brandName||'',seriesName:row.seriesName||'',manufacturerColorCode:row.manufacturerColorCode||'',swatchImageUrl:row.swatchImageUrl||'',remark:row.remark||'',status:row.status});visible.value=true}
async function save(){if(!form.code.trim()||!form.name.trim()){ElMessage.warning('内部色号编码和色号名称必填');return}saving.value=true;try{if(editingId.value===null)await createColor({...form});else await updateColor(editingId.value,{...form});ElMessage.success('色号档案已保存');visible.value=false;await load()}finally{saving.value=false}}
async function remove(row:ColorView){await ElMessageBox.confirm('色号档案删除后不能用于新单据。确定删除吗？','删除确认',{type:'warning'});await deleteColor(row.id);ElMessage.success('色号已删除');await load()}
onMounted(()=>{void load()})
</script>
<style scoped>.tip{margin-top:16px}</style>
