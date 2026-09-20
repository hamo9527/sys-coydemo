<template>
  <div class="doc">
    <div class="toolbar page-card">
      <div class="toolbar-left">
        <el-button @click="router.back()">返回</el-button>
        <el-button>附件</el-button>
        <el-button>日志</el-button>
        <el-button>打印</el-button>
        <el-button type="primary">发送</el-button>
        <el-button>生成</el-button>
        <el-button>余额</el-button>
      </div>
      <div class="toolbar-right">
        <el-button>上页</el-button>
        <el-button>下页</el-button>
        <el-button>复制</el-button>
      </div>
    </div>

    <div class="page-card form-card">
      <div class="form-head">
        <h2>编辑出库单</h2>
        <div class="stamp-effective">生效</div>
      </div>
      <el-alert
        type="warning"
        :closable="false"
        show-icon
        class="mb"
        title="后端暂无销售出库 API（erp_sales_order 仅有表结构）。本页为界面稿，不会提交数据。"
      />

      <el-form :model="form" label-width="96px" class="dense-form">
        <el-row :gutter="12">
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="系统单号">
              <el-input v-model="form.systemNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="自编号">
              <el-input v-model="form.customNo" clearable />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="开单日期" required>
              <el-date-picker
                v-model="form.billDate"
                type="date"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="出库库房" required>
              <el-select v-model="form.outboundWarehouse" style="width: 100%">
                <el-option label="订单出库" value="order" />
                <el-option label="零售出库" value="retail" />
                <el-option label="调拨出库" value="transfer" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="订货单号">
              <el-input v-model="form.orderNo" clearable placeholder="选择订货单">
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="订货自编号">
              <el-input v-model="form.orderCustomNo" disabled />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="订货日期">
              <el-date-picker
                v-model="form.orderDate"
                type="date"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="订单类型">
              <el-select v-model="form.orderType" style="width: 100%">
                <el-option label="新订" value="新订" />
                <el-option label="补订" value="补订" />
                <el-option label="常规订单" value="常规订单" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="客户编码" required>
              <el-input v-model="form.customerCode" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="客户名称" required>
              <el-input v-model="form.customerName" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="联系人">
              <el-input v-model="form.contact" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="联系电话" required>
              <el-input v-model="form.mobile" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="所属小区">
              <el-input v-model="form.community" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="12">
            <el-form-item label="送货地址" required>
              <el-input v-model="form.address" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="销售渠道">
              <el-select v-model="form.channel" style="width: 100%">
                <el-option label="零售" value="零售" />
                <el-option label="工程" value="工程" />
                <el-option label="门店" value="门店" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="客户等级">
              <el-input v-model="form.customerLevel" disabled />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="销售部门" required>
              <el-input v-model="form.salesDept" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="业务员">
              <el-input v-model="form.salesperson" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="导购员">
              <el-input v-model="form.guide" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="驻场设计师">
              <el-input v-model="form.designer" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="设计方式">
              <el-select v-model="form.designMethod" clearable placeholder="请选择" style="width: 100%">
                <el-option label="上门设计" value="上门设计" />
                <el-option label="到店设计" value="到店设计" />
                <el-option label="远程设计" value="远程设计" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="发货地点">
              <el-select v-model="form.shipFrom" clearable placeholder="请选择" style="width: 100%">
                <el-option label="总仓" value="总仓" />
                <el-option label="加工厂" value="加工厂" />
                <el-option label="门店仓" value="门店仓" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="送货方式">
              <el-select v-model="form.deliveryMethod" clearable placeholder="请选择" style="width: 100%">
                <el-option label="自提" value="自提" />
                <el-option label="配送" value="配送" />
                <el-option label="物流" value="物流" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="全屋备品">
              <el-input v-model="form.wholeHouse" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="经手人">
              <el-input v-model="form.handler" clearable>
                <template #suffix>
                  <el-icon class="lookup"><Search /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="对内备注">
              <el-input v-model="form.innerRemark" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :lg="6">
            <el-form-item label="对外备注">
              <el-input v-model="form.outerRemark" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <div class="page-card">
      <el-tabs v-model="detailTab">
        <el-tab-pane label="出库明细" name="outbound" />
        <el-tab-pane label="加工明细" name="process" />
        <el-tab-pane label="软件明细" name="software" />
      </el-tabs>

      <el-table
        v-show="detailTab === 'outbound'"
        :data="lines"
        border
        stripe
        height="320"
        show-summary
        :summary-method="getSummaries"
      >
        <el-table-column type="index" label="#" width="48" fixed />
        <el-table-column label="使用位置" min-width="100">
          <template #default="{ row }">
            <el-input v-model="row.position" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="商品编码" min-width="160">
          <template #default="{ row }">
            <el-input v-model="row.sku" size="small">
              <template #suffix>
                <el-icon class="lookup"><Search /></el-icon>
              </template>
            </el-input>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="brand" label="品牌名称" min-width="90" />
        <el-table-column prop="spec" label="商品规格" min-width="110" />
        <el-table-column label="商品等级" min-width="100">
          <template #default="{ row }">
            <el-select v-model="row.grade" size="small" style="width: 100%">
              <el-option label="优等" value="优等" />
              <el-option label="一级" value="一级" />
              <el-option label="合格" value="合格" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="批次/色号" min-width="100">
          <template #default="{ row }">
            <el-input v-model="row.batch" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="套数" width="80">
          <template #default="{ row }">
            <el-input-number v-model="row.sets" :controls="false" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="箱数" width="80">
          <template #default="{ row }">
            <el-input-number v-model="row.boxes" :controls="false" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="片数" width="80">
          <template #default="{ row }">
            <el-input-number v-model="row.pieces" :controls="false" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="数量" width="80">
          <template #default="{ row }">
            <el-input-number v-model="row.qty" :controls="false" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="64" />
        <el-table-column label="标准单价" width="96">
          <template #default="{ row }">
            <el-input-number v-model="row.stdPrice" :controls="false" :precision="2" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="折前金额" width="96">
          <template #default="{ row }">
            {{ pretaxAmount(row as OutboundLine) }}
          </template>
        </el-table-column>
        <el-table-column label="折扣" width="72">
          <template #default="{ row }">
            <el-input-number v-model="row.discount" :controls="false" :precision="2" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="折后单价" width="96">
          <template #default="{ row }">
            <el-input-number v-model="row.price" :controls="false" :precision="2" size="small" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="折后金额" width="96">
          <template #default="{ row }">
            {{ lineAmount(row as OutboundLine) }}
          </template>
        </el-table-column>
        <el-table-column label="仓库编码" min-width="110">
          <template #default="{ row }">
            <el-input v-model="row.warehouseCode" size="small">
              <template #suffix>
                <el-icon class="lookup"><Search /></el-icon>
              </template>
            </el-input>
          </template>
        </el-table-column>
        <el-table-column prop="warehouseName" label="仓库名称" min-width="110" />
        <el-table-column label="仓位名称" min-width="100">
          <template #default="{ row }">
            <el-select v-model="row.binName" clearable placeholder="请选择" size="small" style="width: 100%">
              <el-option label="A区" value="A区" />
              <el-option label="B区" value="B区" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="费用类别" min-width="110">
          <template #default="{ row }">
            <el-select v-model="row.feeType" clearable placeholder="请选择" size="small" style="width: 100%">
              <el-option label="正价商品" value="正价商品" />
              <el-option label="赠品" value="赠品" />
              <el-option label="样品" value="样品" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="占货" width="64" align="center">
          <template #default="{ row }">
            <el-checkbox v-model="row.occupy" />
          </template>
        </el-table-column>
        <el-table-column label="计业绩" width="72" align="center">
          <template #default="{ row }">
            <el-checkbox v-model="row.countPerf" />
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="detailTab !== 'outbound'" description="暂无明细" :image-size="72" />

      <div class="summary">
        <span>总箱数：<b>{{ totals.boxes }}</b></span>
        <span>总金额：<b>{{ totals.amount }}</b></span>
        <span>货款余额：<b>{{ totals.balance }}</b></span>
        <span>总平方：<b>{{ totals.sqm }}</b></span>
        <span>总重量(吨)：<b>{{ totals.weight }}</b></span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import type { TableColumnCtx } from 'element-plus'

type OutboundLine = {
  position: string
  sku: string
  name: string
  brand: string
  spec: string
  grade: string
  batch: string
  sets: number
  boxes: number
  pieces: number
  qty: number
  unit: string
  stdPrice: number
  discount: number
  price: number
  warehouseCode: string
  warehouseName: string
  binName: string
  feeType: string
  occupy: boolean
  countPerf: boolean
}

const router = useRouter()
const detailTab = ref('outbound')

const form = reactive({
  systemNo: 'CK-2026080017',
  customNo: '',
  billDate: '2026-08-13',
  outboundWarehouse: 'order',
  orderNo: 'DH-2026080012',
  orderCustomNo: '',
  orderDate: '2026-08-13',
  orderType: '新订',
  customerCode: 'KH-2026080002',
  customerName: '流程测试',
  contact: '流程测试',
  mobile: '流程测试',
  community: '',
  address: '流程测试',
  channel: '零售',
  customerLevel: '',
  salesDept: '总公司',
  salesperson: '刘晓',
  guide: '',
  designer: '',
  designMethod: '',
  shipFrom: '',
  deliveryMethod: '',
  wholeHouse: '',
  handler: '系统管理员',
  innerRemark: '',
  outerRemark: '',
})

const lines = ref<OutboundLine[]>([
  {
    position: '',
    sku: 'D5-L15701-BH4伊丽白',
    name: 'D5-L15701-BH4伊丽白',
    brand: '大角鱼',
    spec: '750*1500',
    grade: '优等',
    batch: 'Y48',
    sets: 0,
    boxes: 0,
    pieces: 20,
    qty: 20,
    unit: '片',
    stdPrice: 0,
    discount: 0,
    price: 10,
    warehouseCode: 'CK-0004',
    warehouseName: '加工厂仓',
    binName: '',
    feeType: '正价商品',
    occupy: false,
    countPerf: true,
  },
  {
    position: '',
    sku: 'D5-L15702-BH4伊丽白',
    name: 'D5-L15702-BH4伊丽白',
    brand: '大角鱼',
    spec: '750*1500',
    grade: '优等',
    batch: 'Y48',
    sets: 0,
    boxes: 20,
    pieces: 1,
    qty: 1,
    unit: '卷',
    stdPrice: 0,
    discount: 0,
    price: 160,
    warehouseCode: 'CK-0004',
    warehouseName: '加工厂仓',
    binName: '',
    feeType: '正价商品',
    occupy: false,
    countPerf: true,
  },
])

function pretaxAmount(row: OutboundLine) {
  return Number((row.qty * row.stdPrice).toFixed(2))
}

function lineAmount(row: OutboundLine) {
  return Number((row.qty * row.price).toFixed(2))
}

const totals = computed(() => {
  const amount = lines.value.reduce((sum, row) => sum + lineAmount(row), 0)
  return {
    boxes: lines.value.reduce((sum, row) => sum + row.boxes, 0),
    amount,
    balance: amount,
    sqm: 22.5,
    weight: 0.545,
  }
})

function getSummaries(param: {
  columns: TableColumnCtx<OutboundLine>[]
  data: OutboundLine[]
}) {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    const prop = column.property
    const label = column.label
    if (label === '套数') {
      sums[index] = String(data.reduce((s, r) => s + r.sets, 0))
      return
    }
    if (label === '箱数') {
      sums[index] = String(data.reduce((s, r) => s + r.boxes, 0))
      return
    }
    if (label === '数量') {
      sums[index] = String(data.reduce((s, r) => s + r.qty, 0))
      return
    }
    if (label === '折后金额') {
      sums[index] = String(data.reduce((s, r) => s + lineAmount(r), 0))
      return
    }
    if (prop) {
      sums[index] = ''
    } else {
      sums[index] = ''
    }
  })
  return sums
}
</script>

<style scoped lang="scss">
.doc {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding-top: 12px;
  padding-bottom: 12px;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.form-card {
  position: relative;
}

.form-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.form-head h2 {
  margin: 0;
  font-size: 18px;
}

.mb {
  margin-bottom: 12px;
}

.dense-form :deep(.el-form-item) {
  margin-bottom: 10px;
}

.lookup {
  cursor: pointer;
  color: #8a96a8;
}

.summary {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 18px;
  color: #3a465a;
  font-size: 13px;
}

.summary b {
  color: var(--cailu-text);
}
</style>
