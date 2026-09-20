<template>
  <view class="page">
    <view class="bar">
      <input v-model="approvalId" type="number" placeholder="审批 ID" />
      <button size="mini" type="primary" @click="lookup">查询</button>
    </view>
    <view v-if="approval" class="card">
      <text>{{ approval.title }}</text>
      <text>状态：{{ approval.status }}</text>
      <button
        v-if="approval.status === 'draft'"
        size="mini"
        type="primary"
        @click="submit"
      >
        提交审批
      </button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { http } from '@/utils/http'

interface ApprovalView {
  id: number
  title: string
  status: string
}

const approvalId = ref('')
const approval = ref<ApprovalView | null>(null)

async function lookup() {
  const id = Number(approvalId.value)
  if (!id) {
    return
  }
  approval.value = await http.get<ApprovalView>(`/oa/approvals/${id}`)
}

async function submit() {
  if (!approval.value) {
    return
  }
  approval.value = await http.post<ApprovalView>(
    `/oa/approvals/${approval.value.id}/submissions`,
  )
  uni.showToast({ title: '已提交' })
}
</script>

<style scoped>
.page { padding: 24rpx; }
.bar { display: flex; gap: 12rpx; margin-bottom: 24rpx; }
.card { background: #fff; border-radius: 16rpx; padding: 24rpx; }
.card text { display: block; margin-bottom: 8rpx; }
</style>
