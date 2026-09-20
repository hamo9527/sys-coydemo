import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { public: true, title: '登录' },
  },
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: '首页', module: 'home' },
      },
      {
        path: 'module/:key',
        name: 'ModuleHub',
        component: () => import('@/views/common/ModuleHubView.vue'),
        meta: { title: '模块中心' },
        beforeEnter(to) {
          const key = String(to.params.key || '')
          to.meta.module = key
          const titles: Record<string, string> = {
            goal: '目标',
            purchase: '进货',
            sales: '销货',
            stock: '存货',
            finance: '财务',
            aftersale: '售后',
            distribution: '分销',
            alliance: '联盟',
            master: '资料',
            settings: '设置',
            decision: '决策',
            cloud: '云店',
          }
          to.meta.title = titles[key] || '模块中心'
          return true
        },
      },
      {
        path: 'erp/products',
        name: 'ErpProducts',
        component: () => import('@/views/erp/ProductListView.vue'),
        meta: { title: '商品档案', module: 'master' },
      },
      {
        path: 'erp/stock',
        name: 'ErpStock',
        component: () => import('@/views/erp/StockListView.vue'),
        meta: { title: '库存台账', module: 'stock' },
      },
      {
        path: 'erp/purchase',
        name: 'ErpPurchase',
        component: () => import('@/views/erp/PurchaseListView.vue'),
        meta: { title: '采购单', module: 'purchase' },
      },
      {
        path: 'erp/purchase/create',
        name: 'ErpPurchaseCreate',
        component: () => import('@/views/erp/PurchaseOrderFormView.vue'),
        meta: { title: '新增:采购单', module: 'purchase' },
      },
      {
        path: 'erp/sales',
        name: 'ErpSales',
        component: () => import('@/views/erp/SalesPlaceholderView.vue'),
        meta: { title: '订货单', module: 'sales' },
      },
      {
        path: 'erp/outbound',
        name: 'ErpOutbound',
        component: () => import('@/views/erp/OutboundOrderView.vue'),
        meta: { title: '出库单', module: 'sales' },
      },
      {
        path: 'oa/approvals',
        name: 'OaApprovals',
        component: () => import('@/views/oa/ApprovalListView.vue'),
        meta: { title: '审批中心', module: 'settings' },
      },
      {
        path: 'finance/receivables',
        name: 'FinanceReceivables',
        component: () => import('@/views/finance/ReceivableListView.vue'),
        meta: { title: '应收管理', module: 'finance' },
      },
      {
        path: 'system/users',
        name: 'SystemUsers',
        component: () => import('@/views/system/UserListView.vue'),
        meta: { title: '用户管理', module: 'settings' },
      },
      {
        path: 'placeholder/:module/:name',
        name: 'Placeholder',
        component: () => import('@/views/common/PlaceholderView.vue'),
        meta: { title: '功能预留' },
        beforeEnter(to) {
          const moduleKey = String(to.params.module || '')
          to.meta.module = moduleKey
          to.meta.title = String(to.params.name || '功能预留')
          return true
        },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/NotFoundView.vue'),
    meta: { public: true, title: '页面不存在' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const userStore = useUserStore()
  document.title = `${to.meta.title || '材陆'} · 材陆建材`

  if (to.meta.public) {
    return true
  }

  if (!userStore.isLoggedIn) {
    return { name: 'Login', query: { redirect: to.fullPath } }
  }

  return true
})

export default router
