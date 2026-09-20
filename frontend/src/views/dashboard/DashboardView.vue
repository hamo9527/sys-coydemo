<template>
  <div class="dashboard">
    <div class="reminders">
      <div
        v-for="item in reminders"
        :key="item.title"
        class="reminder"
        :style="{ background: item.bg, color: item.color }"
      >
        <div class="reminder-title">{{ item.title }}</div>
        <div class="reminder-count">{{ item.count }}</div>
      </div>
    </div>

    <div class="middle">
      <div class="page-card panel">
        <div class="panel-block">
          <div class="panel-title">本月收款</div>
          <div class="empty">
            <el-empty description="暂无收款数据" :image-size="72" />
          </div>
        </div>
        <div class="panel-block">
          <div class="panel-title">本月订单</div>
          <div class="order-stats">
            <div v-for="stat in orderStats" :key="stat.label" class="order-stat">
              <div class="label">{{ stat.label }}</div>
              <div class="value">{{ stat.value }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="page-card panel prospect">
        <div class="panel-title">意向客户</div>
        <div class="chart-wrap">
          <div class="pie" />
          <div class="pie-label">
            <div>今日跟踪</div>
            <strong>2</strong>
          </div>
        </div>
        <ul class="prospect-list">
          <li><span>今日跟踪</span><b>2</b></li>
          <li><span>本月新增</span><b>0</b></li>
          <li><span>本月成交</span><b>0</b></li>
        </ul>
      </div>

      <div class="right-col">
        <div class="page-card notice">
          <div class="panel-title">公告通知</div>
          <el-empty description="暂无公告" :image-size="48" />
        </div>
        <div class="page-card shortcuts">
          <div class="panel-title">常用功能</div>
          <div class="shortcut-grid">
            <button
              v-for="item in shortcuts"
              :key="item.title"
              type="button"
              class="shortcut"
              @click="router.push(item.path)"
            >
              <span class="shortcut-icon" :style="{ background: item.color }">
                <el-icon :size="16"><component :is="item.icon" /></el-icon>
              </span>
              <span>{{ item.title }}</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="page-card todos">
      <el-tabs v-model="todoTab">
        <el-tab-pane label="待办事项" name="todo" />
        <el-tab-pane label="已办事项" name="done" />
        <el-tab-pane label="发起事项" name="mine" />
        <el-tab-pane label="抄送事项" name="cc" />
      </el-tabs>
      <el-empty description="暂无事项" :image-size="80" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, type Component } from 'vue'
import { useRouter } from 'vue-router'
import {
  Box,
  Checked,
  Document,
  Download,
  Money,
  Notebook,
  Share,
  ShoppingCart,
  Upload,
  Van,
  Wallet,
} from '@element-plus/icons-vue'

type Shortcut = {
  title: string
  path: string
  color: string
  icon: Component
}

const router = useRouter()
const todoTab = ref('todo')

const reminders = [
  { title: '意向客户跟踪提醒', count: 2, bg: '#e8f1ff', color: '#2f6bff' },
  { title: '意向客户到期提醒', count: 0, bg: '#e9f9f0', color: '#19b26b' },
  { title: '订单送货提醒', count: 10, bg: '#fff6e8', color: '#f5a623' },
  { title: '应收欠款提醒', count: 4, bg: '#ffecec', color: '#f56c6c' },
  { title: '客户未下单提醒', count: 1, bg: '#f3eaff', color: '#8b5cf6' },
]

const orderStats = [
  { label: '新订', value: 0 },
  { label: '补货', value: 0 },
  { label: '取消', value: 0 },
]

const shortcuts: Shortcut[] = [
  { title: '新手指引', path: '/dashboard', color: '#2f6bff', icon: Notebook },
  { title: '库存查询', path: '/erp/stock', color: '#19b26b', icon: Box },
  { title: '订单查询', path: '/erp/sales', color: '#f5a623', icon: Document },
  { title: '意向客户', path: '/module/sales', color: '#8b5cf6', icon: Checked },
  { title: '订货单', path: '/erp/sales', color: '#2f6bff', icon: ShoppingCart },
  { title: '出库单', path: '/erp/outbound', color: '#19b26b', icon: Upload },
  { title: '销售退货单', path: '/module/sales', color: '#f56c6c', icon: Van },
  { title: '预收款单', path: '/finance/receivables', color: '#f5a623', icon: Wallet },
  { title: '收款单', path: '/finance/receivables', color: '#2f6bff', icon: Money },
  { title: '采购单', path: '/erp/purchase', color: '#19b26b', icon: Download },
  { title: '入库单', path: '/module/purchase', color: '#8b5cf6', icon: Box },
  { title: '调拨单', path: '/module/stock', color: '#f5a623', icon: Share },
]
</script>

<style scoped lang="scss">
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reminders {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 12px;
}

.reminder {
  border-radius: 10px;
  padding: 14px 16px;
  min-height: 84px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.reminder-title {
  font-size: 13px;
  opacity: 0.9;
}

.reminder-count {
  font-size: 28px;
  font-weight: 700;
  line-height: 1;
}

.middle {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr 1.2fr;
  gap: 12px;
}

.panel-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
}

.panel-block + .panel-block {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px dashed var(--cailu-border);
}

.order-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.order-stat {
  background: #f7f9fc;
  border-radius: 8px;
  padding: 12px;
  text-align: center;
}

.order-stat .label {
  color: var(--cailu-muted);
  font-size: 12px;
}

.order-stat .value {
  margin-top: 6px;
  font-size: 22px;
  font-weight: 700;
}

.prospect {
  display: flex;
  flex-direction: column;
}

.chart-wrap {
  position: relative;
  height: 160px;
  display: grid;
  place-items: center;
}

.pie {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: conic-gradient(#2f6bff 0 72deg, #dbe7ff 72deg 360deg);
}

.pie-label {
  position: absolute;
  text-align: center;
  font-size: 12px;
  color: var(--cailu-muted);
}

.pie-label strong {
  display: block;
  font-size: 22px;
  color: var(--cailu-text);
}

.prospect-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.prospect-list li {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-top: 1px solid var(--cailu-border);
  font-size: 13px;
}

.right-col {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notice {
  min-height: 120px;
}

.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px 6px;
}

.shortcut {
  border: 0;
  background: transparent;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #3a465a;
  cursor: pointer;
  padding: 4px;
}

.shortcut-icon {
  width: 34px;
  height: 34px;
  border-radius: 10px;
  color: #fff;
  display: grid;
  place-items: center;
}

.todos {
  min-height: 220px;
}

@media (max-width: 1200px) {
  .reminders {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .middle {
    grid-template-columns: 1fr;
  }

  .shortcut-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}
</style>
