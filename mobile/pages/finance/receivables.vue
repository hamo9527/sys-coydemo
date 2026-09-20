<template>
  <view class="page">
    <view class="bar">
      <input v-model="receivableId" type="number" placeholder="应收 ID" />
      <button size="mini" type="primary" @click="lookup">查询</button>
    </view>
    <view v-if="receivable" class="card">
      <text>{{ receivable.billNo }}</text>
      <text>金额 {{ receivable.amount }} / 已收 {{ receivable.receivedAmount }}</text>
      <text>状态 {{ receivable.status }}</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { http } from '@/utils/http'

interface ReceivableView {
  id: number
  billNo: string
  amount: number
  receivedAmount: number
  status: string
}

const receivableId = ref('')
const receivable = ref<ReceivableView | null>(null)

async function lookup() {
  const id = Number(receivableId.value)
  if (!id) {
    return
  }
  receivable.value = await http.get<ReceivableView>(`/finance/receivables/${id}`)
}
</script>

<style scoped>
.page { padding: 24rpx; }
.bar { display: flex; gap: 12rpx; margin-bottom: 24rpx; }
.card { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.card text { display: block; margin-bottom: 8rpx; }
</style>
