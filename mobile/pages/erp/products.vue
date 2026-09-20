<template>
  <view class="page">
    <view class="bar">
      <input v-model="productId" type="number" placeholder="商品 ID" />
      <button size="mini" type="primary" @click="lookup">查询</button>
    </view>
    <view v-if="product" class="card">
      <text>{{ product.name }}（{{ product.sku }}）</text>
      <text>采购价 {{ product.purchasePrice }} / 销售价 {{ product.salePrice }}</text>
    </view>
    <view v-else class="empty">输入 ID 查询，或后续接入列表接口</view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { http } from '@/utils/http'

interface ProductView {
  id: number
  sku: string
  name: string
  purchasePrice: number
  salePrice: number
}

const productId = ref('')
const product = ref<ProductView | null>(null)

async function lookup() {
  const id = Number(productId.value)
  if (!id) {
    uni.showToast({ title: '请输入 ID', icon: 'none' })
    return
  }
  try {
    product.value = await http.get<ProductView>(`/erp/products/${id}`)
  } catch (error) {
    uni.showToast({ title: error instanceof Error ? error.message : '查询失败', icon: 'none' })
  }
}
</script>

<style scoped>
.page { padding: 24rpx; }
.bar { display: flex; gap: 12rpx; margin-bottom: 24rpx; }
.card, .empty {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
}
.card text { display: block; margin-bottom: 8rpx; }
</style>
