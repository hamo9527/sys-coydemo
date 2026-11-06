<template>
  <div class="page-card">
    <div class="page-header">
      <h2>商品档案</h2>
      <div class="page-actions"><el-button @click="openImportDialog">导入商品</el-button><el-button type="primary" @click="dialogVisible = true">新建商品</el-button></div>
    </div>
    <el-form :inline="true" class="query" @submit.prevent="handleSearch">
      <el-form-item label="关键词">
        <el-input v-model="keyword" placeholder="商品编码 / 助记码 / 商品名称 / 品牌名称" clearable />
      </el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" native-type="submit">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" stripe border v-loading="loading" empty-text="暂无商品" style="width: 100%">
      <el-table-column prop="sku" label="*商品编码" min-width="130" />
      <el-table-column prop="mnemonicCode" label="助记码" min-width="110" />
      <el-table-column prop="name" label="*商品名称" min-width="150" />
      <el-table-column prop="brandName" label="*品牌名称" min-width="130" />
      <el-table-column prop="category" label="商品类别" min-width="110" />
      <el-table-column prop="spec" label="商品规格" min-width="120" />
      <el-table-column prop="unit" label="*单位" min-width="80" />
      <el-table-column prop="auxiliaryUnit" label="辅助单位" min-width="90" />
      <el-table-column prop="auxiliarySpec" label="辅助规格" min-width="110" />
      <el-table-column prop="packagingUnit" label="包装单位" min-width="90" />
      <el-table-column prop="packagingSpec" label="包装规格" min-width="110" />
      <el-table-column prop="unitWeight" label="单重" min-width="90" />
      <el-table-column prop="minimumOrderQty" label="最低起订量" min-width="110" />
      <el-table-column prop="minStock" label="最小库存" min-width="100" />
      <el-table-column prop="maxStock" label="最大库存" min-width="100" />
      <el-table-column label="小色号库存" min-width="110"><template #default="{ row }">{{ row.smallColorStock ? '是' : '否' }}</template></el-table-column>
      <el-table-column prop="stockAgeThreshold" label="库龄阈值" min-width="100" />
      <el-table-column prop="obsoleteDays" label="呆滞天数" min-width="100" />
      <el-table-column prop="shelfLifeDays" label="保质天数" min-width="100" />
      <el-table-column prop="productAttribute" label="*商品属性" min-width="110" />
      <el-table-column label="虚拟商品" min-width="90"><template #default="{ row }">{{ row.virtualProduct ? '是' : '否' }}</template></el-table-column>
      <el-table-column label="包安装" min-width="80"><template #default="{ row }">{{ row.installationRequired ? '是' : '否' }}</template></el-table-column>
      <el-table-column label="云店可查" min-width="90"><template #default="{ row }">{{ row.cloudStoreVisible ? '是' : '否' }}</template></el-table-column>
      <el-table-column prop="productionProcess" label="生产工艺" min-width="140" />
      <el-table-column prop="productionOrigin" label="生产产地" min-width="120" />
      <el-table-column prop="usageLocation" label="使用位置" min-width="120" />
      <el-table-column prop="vrUrl" label="VR地址" min-width="160" />
      <el-table-column prop="remark" label="备注" min-width="160" />
    </el-table>
    <div class="pager">
      <el-pagination v-model:current-page="page" v-model:page-size="size" layout="total, prev, pager, next" :total="total" @current-change="loadList" @size-change="handleSizeChange" />
    </div>
    <el-dialog v-model="importDialogVisible" title="导入商品" width="820px" destroy-on-close>
      <el-radio-group v-model="importMode" class="import-mode">
        <el-radio-button value="excel">Excel 表格</el-radio-button>
        <el-radio-button value="txt">TXT 文本</el-radio-button>
      </el-radio-group>
      <template v-if="importMode === 'excel'">
        <p class="import-tip">支持 .xlsx 文件，第一行为表头。必需列：商品编码、商品名称、品牌名称、单位、商品属性。</p>
        <div class="template-actions"><el-button link type="primary" @click="downloadExcelTemplate">下载 Excel 模板</el-button><input type="file" accept=".xlsx" @change="handleExcelFileChange" /></div>
        <p v-if="excelRows.length" class="import-tip">已读取 {{ excelRows.length }} 行，预览前 5 行：</p>
        <el-table v-if="excelPreviewRows.length" :data="excelPreviewRows" size="small" border max-height="220">
          <el-table-column prop="line" label="行" width="55" />
          <el-table-column prop="sku" label="商品编码" />
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="brandName" label="品牌名称" />
          <el-table-column prop="unit" label="单位" width="80" />
          <el-table-column prop="productAttribute" label="商品属性" />
        </el-table>
      </template>
      <template v-else>
        <p class="import-tip">TXT 第一行为列名，支持制表符、逗号或竖线分隔。请将文件列名对应到商品字段。</p>
        <div class="txt-toolbar">
          <div class="template-actions"><el-button link type="primary" @click="downloadTxtTemplate">下载 TXT 模板</el-button><input type="file" accept=".txt,.tsv,.csv" @change="handleTxtFileChange" /></div>
          <el-select v-model="txtDelimiter" style="width: 150px" @change="parseTxtContent">
            <el-option label="制表符（Tab）" value="tab" />
            <el-option label="逗号（,）" value="," />
            <el-option label="竖线（|）" value="|" />
          </el-select>
        </div>
        <el-form v-if="txtHeaders.length" class="mapping-grid" label-position="top">
          <el-form-item v-for="field in importFields" :key="field.key" :label="field.label">
            <el-select v-model="mapping[field.key]" placeholder="不导入此字段">
              <el-option v-for="(header, index) in txtHeaders" :key="index" :label="header || ('第 ' + (index + 1) + ' 列')" :value="String(index)" />
            </el-select>
            <span v-if="field.required" class="required-mark">必填</span>
          </el-form-item>
        </el-form>
        <p v-if="txtRows.length" class="import-tip">已读取 {{ txtRows.length }} 行，预览前 5 行：</p>
        <el-table v-if="txtPreviewRows.length" :data="txtPreviewRows" size="small" border max-height="220">
          <el-table-column prop="line" label="行" width="55" />
          <el-table-column prop="sku" label="商品编码" />
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="brandName" label="品牌名称" />
          <el-table-column prop="unit" label="单位" width="80" />
          <el-table-column prop="productAttribute" label="商品属性" />
        </el-table>
      </template>
      <el-alert v-if="importErrors.length" type="warning" :closable="false" title="部分数据未导入" class="import-errors">
        <div v-for="(error, index) in importErrors" :key="index">{{ error }}</div>
      </el-alert>
      <template #footer>
        <el-button @click="importDialogVisible = false">关闭</el-button>
        <el-button v-if="importMode === 'excel'" type="primary" :loading="importLoading" :disabled="!excelRows.length" @click="handleExcelImport">开始导入</el-button>
        <el-button v-else type="primary" :loading="importLoading" :disabled="!txtRows.length" @click="handleTxtImport">开始导入</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="dialogVisible" title="新建商品" width="900px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="*商品编码" prop="sku"><el-input v-model="form.sku" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="助记码" prop="mnemonicCode"><el-input v-model="form.mnemonicCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*商品名称" prop="name"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*品牌名称" prop="brandName"><el-input v-model="form.brandName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="商品类别" prop="category"><el-input v-model="form.category" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="商品规格" prop="spec"><el-input v-model="form.spec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*单位" prop="unit"><el-input v-model="form.unit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="辅助单位" prop="auxiliaryUnit"><el-input v-model="form.auxiliaryUnit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="辅助规格" prop="auxiliarySpec"><el-input v-model="form.auxiliarySpec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="包装单位" prop="packagingUnit"><el-input v-model="form.packagingUnit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="包装规格" prop="packagingSpec"><el-input v-model="form.packagingSpec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="单重" prop="unitWeight"><el-input-number v-model="form.unitWeight" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最低起订量" prop="minimumOrderQty"><el-input-number v-model="form.minimumOrderQty" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最小库存" prop="minStock"><el-input-number v-model="form.minStock" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最大库存" prop="maxStock"><el-input-number v-model="form.maxStock" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="小色号库存" prop="smallColorStock"><el-switch v-model="form.smallColorStock" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="库龄阈值" prop="stockAgeThreshold"><el-input-number v-model="form.stockAgeThreshold" :min="0" :precision="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="呆滞天数" prop="obsoleteDays"><el-input-number v-model="form.obsoleteDays" :min="0" :precision="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="保质天数" prop="shelfLifeDays"><el-input-number v-model="form.shelfLifeDays" :min="0" :precision="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*商品属性" prop="productAttribute"><el-input v-model="form.productAttribute" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="虚拟商品" prop="virtualProduct"><el-switch v-model="form.virtualProduct" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="包安装" prop="installationRequired"><el-switch v-model="form.installationRequired" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="云店可查" prop="cloudStoreVisible"><el-switch v-model="form.cloudStoreVisible" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="生产工艺" prop="productionProcess"><el-input v-model="form.productionProcess" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="生产产地" prop="productionOrigin"><el-input v-model="form.productionOrigin" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="使用位置" prop="usageLocation"><el-input v-model="form.usageLocation" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="VR地址" prop="vrUrl"><el-input v-model="form.vrUrl" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleCreate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { createProduct, downloadProductExcelTemplate, importProducts, listProducts, previewProductsExcel } from '@/api/erp'
import type { CreateProductRequest, ProductImportRow, ProductImportResult, ProductView } from '@/types/erp'

const route = useRoute()
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)

const importDialogVisible = ref(false)
const importMode = ref<'excel' | 'txt'>('excel')
const importLoading = ref(false)
const importErrors = ref<string[]>([])
const excelRows = ref<ProductImportRow[]>([])
const txtContent = ref('')
const txtDelimiter = ref('tab')
const txtHeaders = ref<string[]>([])
const txtRows = ref<string[][]>([])
const importFields: Array<{ key: keyof ProductImportRow; label: string; required: boolean; aliases: string[] }> = [
  { key: 'sku', label: '商品编码', required: true, aliases: ['商品编码', '*商品编码', '商品编号', 'sku'] },
  { key: 'name', label: '商品名称', required: true, aliases: ['商品名称', '*商品名称', 'name'] },
  { key: 'brandName', label: '品牌名称', required: true, aliases: ['品牌名称', '*品牌名称', '品牌', 'brandname'] },
  { key: 'unit', label: '单位', required: true, aliases: ['单位', '*单位', '计量单位', 'unit'] },
  { key: 'productAttribute', label: '商品属性', required: true, aliases: ['商品属性', '*商品属性', 'productattribute'] },
  { key: 'mnemonicCode', label: '助记码', required: false, aliases: ['助记码', 'mnemoniccode'] },
  { key: 'category', label: '商品类别', required: false, aliases: ['商品类别', '商品分类', 'category'] },
  { key: 'spec', label: '商品规格', required: false, aliases: ['商品规格', '规格', 'spec'] },
  { key: 'remark', label: '备注', required: false, aliases: ['备注', 'remark'] },
]
const mapping = reactive<Record<keyof ProductImportRow, string>>({
  sku: '', name: '', brandName: '', unit: '', productAttribute: '',
  mnemonicCode: '', category: '', spec: '', remark: '',
})
const excelPreviewRows = computed(() => excelRows.value.slice(0, 5).map((row, index) => ({ line: index + 2, ...row })))
const txtPreviewRows = computed(() => txtRows.value.slice(0, 5).map((row, index) => ({
  line: index + 2,
  sku: mappedValue(row, 'sku'),
  name: mappedValue(row, 'name'),
  brandName: mappedValue(row, 'brandName'),
  unit: mappedValue(row, 'unit'),
  productAttribute: mappedValue(row, 'productAttribute'),
})))

const keyword = ref(typeof route.query.keyword === 'string' ? route.query.keyword : '')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref<ProductView[]>([])
const formRef = ref<FormInstance>()

const form = reactive<CreateProductRequest>({
  sku: '', mnemonicCode: '', name: '', brandName: '', category: '', spec: '', unit: '',
  auxiliaryUnit: '', auxiliarySpec: '', packagingUnit: '', packagingSpec: '',
  unitWeight: undefined, minimumOrderQty: undefined, minStock: undefined, maxStock: undefined,
  smallColorStock: false, stockAgeThreshold: undefined, obsoleteDays: undefined, shelfLifeDays: undefined,
  productAttribute: '', virtualProduct: false, installationRequired: false, cloudStoreVisible: false,
  productionProcess: '', productionOrigin: '', usageLocation: '', vrUrl: '', remark: '',
  purchasePrice: 0, salePrice: 0,
})

const rules: FormRules<CreateProductRequest> = {
  sku: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  brandName: [{ required: true, message: '请输入品牌名称', trigger: 'blur' }],
  unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
  productAttribute: [{ required: true, message: '请选择商品属性', trigger: 'change' }],
}



function saveBlob(blob: Blob, filename: string) {
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  link.remove()
  URL.revokeObjectURL(url)
}

async function downloadExcelTemplate() {
  const blob = await downloadProductExcelTemplate()
  saveBlob(blob, '商品档案导入模板.xlsx')
}

function downloadTxtTemplate() {
  const headers = ['*商品编码', '*商品名称', '*品牌名称', '*单位', '*商品属性', '助记码', '商品类别', '商品规格', '备注']
  const content = '\uFEFF' + headers.join('\t') + '\r\n'
  saveBlob(new Blob([content], { type: 'text/plain;charset=utf-8' }), '商品档案导入模板.txt')
}
function openImportDialog() {
  importErrors.value = []
  excelRows.value = []
  txtContent.value = ''
  txtHeaders.value = []
  txtRows.value = []
  importDialogVisible.value = true
}

function normalizeHeader(value: string) {
  return value.trim().replace(/\s+/g, '').toLowerCase()
}

async function handleExcelFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return
  importLoading.value = true
  importErrors.value = []
  excelRows.value = []
  try {
    excelRows.value = await previewProductsExcel(file)
    ElMessage.success('已读取 ' + excelRows.value.length + ' 行，请确认后导入')
  } finally {
    importLoading.value = false
    input.value = ''
  }
}

async function handleExcelImport() {
  importLoading.value = true
  importErrors.value = []
  try {
    const result = await importProducts(excelRows.value)
    showImportResult(result)
    await loadList()
  } finally {
    importLoading.value = false
  }
}

async function handleTxtFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return
  txtContent.value = await file.text()
  input.value = ''
  importErrors.value = []
  parseTxtContent()
}

function parseTxtContent() {
  const lines = txtContent.value.replace(/^\uFEFF/, '').split(/\r?\n/).filter((line) => line.trim())
  if (!lines.length) {
    txtHeaders.value = []
    txtRows.value = []
    return
  }
  const delimiter = txtDelimiter.value === 'tab' ? '\t' : txtDelimiter.value
  txtHeaders.value = lines[0].split(delimiter).map((value) => value.trim())
  txtRows.value = lines.slice(1).map((line) => line.split(delimiter).map((value) => value.trim()))
  for (const field of importFields) {
    const index = txtHeaders.value.findIndex((header) => field.aliases.includes(normalizeHeader(header)))
    mapping[field.key] = index >= 0 ? String(index) : ''
  }
}

function mappedValue(row: string[], key: keyof ProductImportRow) {
  const column = mapping[key]
  return column === '' ? '' : (row[Number(column)] || '').trim()
}

function showImportResult(result: ProductImportResult) {
  importErrors.value = result.errors
  if (result.failureCount === 0) {
    ElMessage.success('导入完成，成功 ' + result.successCount + ' 条')
  } else {
    ElMessage.warning('导入完成，成功 ' + result.successCount + ' 条，失败 ' + result.failureCount + ' 条')
  }
}

async function handleTxtImport() {
  const missing = importFields.filter((field) => field.required && mapping[field.key] === '')
  if (missing.length) {
    ElMessage.warning('请先映射必填列：' + missing.map((field) => field.label).join('、'))
    return
  }
  const rows: ProductImportRow[] = txtRows.value.map((row) => ({
    sku: mappedValue(row, 'sku'),
    name: mappedValue(row, 'name'),
    brandName: mappedValue(row, 'brandName'),
    unit: mappedValue(row, 'unit'),
    productAttribute: mappedValue(row, 'productAttribute'),
    mnemonicCode: mappedValue(row, 'mnemonicCode'),
    category: mappedValue(row, 'category'),
    spec: mappedValue(row, 'spec'),
    remark: mappedValue(row, 'remark'),
  }))
  importLoading.value = true
  importErrors.value = []
  try {
    const result = await importProducts(rows)
    showImportResult(result)
    await loadList()
  } finally {
    importLoading.value = false
  }
}

async function loadList() {
  loading.value = true
  try {
    const result = await listProducts({ keyword: keyword.value || undefined, page: page.value, size: size.value })
    rows.value = result.records
    total.value = result.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  void loadList()
}

function handleSizeChange() {
  page.value = 1
  void loadList()
}

async function handleCreate() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    await createProduct({ ...form })
    dialogVisible.value = false
    ElMessage.success('商品已创建')
    await loadList()
  } finally {
    saving.value = false
  }
}

watch(
  () => route.query.keyword,
  (value) => {
    keyword.value = typeof value === 'string' ? value : ''
    page.value = 1
    void loadList()
  },
)

onMounted(() => {
  void loadList()
})
</script>

<style scoped>
.page-actions { display: flex; gap: 8px; }
.query { margin-bottom: 8px; }
.import-mode { margin-bottom: 12px; }
.import-tip { color: var(--el-text-color-secondary); font-size: 13px; margin: 8px 0 12px; }
.template-actions { display: flex; align-items: center; gap: 14px; flex-wrap: wrap; }\n.txt-toolbar { display: flex; align-items: center; gap: 16px; margin-bottom: 12px; }
.mapping-grid { display: grid; grid-template-columns: 1fr 1fr; column-gap: 20px; }
.mapping-grid :deep(.el-form-item) { margin-bottom: 10px; }
.required-mark { color: var(--el-color-danger); margin-left: 8px; font-size: 12px; }
.import-errors { margin-top: 12px; max-height: 180px; overflow: auto; }
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>
