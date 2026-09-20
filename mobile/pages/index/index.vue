<template>
  <view class="page">
    <view class="hero">
      <text class="title">材陆建材</text>
      <text class="sub">进销存 · 微OA · 财务</text>
    </view>
    <view class="grid">
      <navigator url="/pages/erp/products" class="card">商品档案</navigator>
      <navigator url="/pages/oa/approvals" class="card">审批中心</navigator>
      <navigator url="/pages/finance/receivables" class="card">应收管理</navigator>
    </view>
  </view>
</template>

<script setup lang="ts">
</script>

<style scoped>
.page {
  min-height: 100vh;
  padding: 32rpx;
  background: #f3f5f7;
}
.hero {
  padding: 48rpx 32rpx;
  border-radius: 24rpx;
  background: linear-gradient(145deg, #16352a, #1f6b4a);
  color: #fff;
  margin-bottom: 24rpx;
}
.title {
  display: block;
  font-size: 40rpx;
  font-weight: 700;
}
.sub {
  display: block;
  margin-top: 12rpx;
  opacity: 0.85;
}
.grid {
  display: grid;
  gap: 16rpx;
}
.card {
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  color: #1a1f1c;
}
</style>
