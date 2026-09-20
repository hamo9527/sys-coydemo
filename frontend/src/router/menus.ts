export type HubLink = {
  title: string
  path?: string
  ready?: boolean
}

export type HubColumn = {
  title: string
  links: HubLink[]
}

export type SideMenuItem = {
  key: string
  title: string
  icon: string
  path?: string
  hub?: HubColumn[]
}

/**
 * 侧栏与模块中心对齐桌面「素材图」ERP 导航。
 * ready=true 的链接指向已实现页面，其余进入占位说明。
 */
export const sideMenus: SideMenuItem[] = [
  {
    key: 'home',
    title: '首页',
    icon: 'HomeFilled',
    path: '/dashboard',
  },
  {
    key: 'goal',
    title: '目标',
    icon: 'Aim',
    hub: [
      {
        title: '目标管理',
        links: [
          { title: '销售目标', path: '/placeholder/goal/sales' },
          { title: '回款目标', path: '/placeholder/goal/collection' },
        ],
      },
    ],
  },
  {
    key: 'purchase',
    title: '进货',
    icon: 'Download',
    hub: [
      {
        title: '进货单据',
        links: [
          { title: '请购单', path: '/placeholder/purchase/requisition' },
          { title: '采购单', path: '/erp/purchase', ready: true },
          { title: '新增采购单', path: '/erp/purchase/create', ready: true },
          { title: '发货单', path: '/placeholder/purchase/shipment' },
          { title: '入库单', path: '/placeholder/purchase/inbound' },
          { title: '采购退货单', path: '/placeholder/purchase/return' },
          { title: '预付款单', path: '/placeholder/purchase/prepay' },
          { title: '付款单', path: '/placeholder/purchase/payment' },
          { title: '应付冲销单', path: '/placeholder/purchase/ap-offset' },
          { title: '数量入库单', path: '/placeholder/purchase/qty-inbound' },
          { title: '数量采购退货单', path: '/placeholder/purchase/qty-return' },
          { title: '内部应付单', path: '/placeholder/purchase/internal-ap' },
        ],
      },
      {
        title: '进货报表',
        links: [
          { title: '请购跟踪查询', path: '/placeholder/purchase/req-track' },
          { title: '采购跟踪查询', path: '/placeholder/purchase/po-track' },
          { title: '采购未入库统计', path: '/placeholder/purchase/unreceived' },
          { title: '采购汇总统计', path: '/placeholder/purchase/summary' },
          { title: '入库费用统计', path: '/placeholder/purchase/inbound-fee' },
          { title: '采购付款统计', path: '/placeholder/purchase/pay-stats' },
          { title: '供应商结算统计', path: '/placeholder/purchase/supplier-settle' },
          { title: '预付款核销统计', path: '/placeholder/purchase/prepay-writeoff' },
          { title: '付款日报', path: '/placeholder/purchase/pay-daily' },
          { title: '应付对账', path: '/placeholder/purchase/ap-reconcile' },
          { title: '采购排产计划查询', path: '/placeholder/purchase/schedule' },
        ],
      },
      {
        title: '进货提醒',
        links: [
          { title: '智能采购提醒', path: '/placeholder/purchase/smart-alert' },
          { title: '采购到货提醒', path: '/placeholder/purchase/arrival-alert' },
          { title: '应付欠款提醒', path: '/placeholder/purchase/ap-debt-alert' },
        ],
      },
    ],
  },
  {
    key: 'sales',
    title: '销货',
    icon: 'Upload',
    hub: [
      {
        title: '销货单据',
        links: [
          { title: '意向客户', path: '/placeholder/sales/prospect' },
          { title: '意向伙伴', path: '/placeholder/sales/partner' },
          { title: '订货单', path: '/erp/sales' },
          { title: '出库单', path: '/erp/outbound' },
          { title: '销售退货单', path: '/placeholder/sales/return' },
          { title: '预收款单', path: '/placeholder/sales/prepay' },
          { title: '收款单', path: '/finance/receivables', ready: true },
          { title: '应收冲销单', path: '/placeholder/sales/ar-offset' },
          { title: '应收冲差单', path: '/placeholder/sales/ar-diff' },
          { title: '派车送货单', path: '/placeholder/sales/dispatch' },
          { title: '完工单', path: '/placeholder/sales/complete' },
          { title: '业务报备单', path: '/placeholder/sales/report' },
          { title: '成本订货单', path: '/placeholder/sales/cost-order' },
          { title: '数量出库单', path: '/placeholder/sales/qty-outbound' },
          { title: '数量销售退货单', path: '/placeholder/sales/qty-return' },
          { title: '历史单据', path: '/placeholder/sales/history' },
          { title: '内部应收单', path: '/placeholder/sales/internal-ar' },
        ],
      },
      {
        title: '销货报表',
        links: [
          { title: '销售价格查询', path: '/placeholder/sales/price' },
          { title: '客户信息查询', path: '/placeholder/sales/customer' },
          { title: '意向客户分析', path: '/placeholder/sales/prospect-analysis' },
          { title: '订货金额跟踪查询', path: '/placeholder/sales/amount-track' },
          { title: '订货数量跟踪查询', path: '/placeholder/sales/qty-track' },
          { title: '商品销售排行榜', path: '/placeholder/sales/rank' },
          { title: '未销售商品查询', path: '/placeholder/sales/unsold' },
          { title: '应收冲销统计', path: '/placeholder/sales/ar-offset-stats' },
          { title: '派车费用统计', path: '/placeholder/sales/dispatch-fee' },
          { title: '销售汇总统计', path: '/placeholder/sales/summary' },
          { title: '订单欠款查询', path: '/placeholder/sales/order-debt' },
          { title: '销售回款统计', path: '/placeholder/sales/collection-stats' },
          { title: '客户结算统计', path: '/placeholder/sales/customer-settle' },
          { title: '客户余额查询', path: '/placeholder/sales/customer-balance' },
          { title: '应收对账', path: '/placeholder/sales/ar-reconcile' },
        ],
      },
      {
        title: '销货提醒',
        links: [
          { title: '意向客户跟踪提醒', path: '/placeholder/sales/alert-follow' },
          { title: '意向客户到期提醒', path: '/placeholder/sales/alert-expire' },
          { title: '客户未下单提醒', path: '/placeholder/sales/alert-no-order' },
          { title: '订单未测量提醒', path: '/placeholder/sales/alert-measure' },
          { title: '订单送货提醒', path: '/placeholder/sales/alert-delivery' },
          { title: '库存占货超期提醒', path: '/placeholder/sales/alert-reserve' },
          { title: '待出库提醒', path: '/placeholder/sales/alert-outbound' },
          { title: '未派车提醒', path: '/placeholder/sales/alert-dispatch' },
          { title: '申购超标提醒', path: '/placeholder/sales/alert-over' },
          { title: '预收未出库提醒', path: '/placeholder/sales/alert-prepay' },
          { title: '应收未核销提醒', path: '/placeholder/sales/alert-unwritten' },
          { title: '应收欠款提醒', path: '/placeholder/sales/alert-ar-debt' },
          { title: '客户生日提醒', path: '/placeholder/sales/alert-birthday' },
        ],
      },
    ],
  },
  {
    key: 'stock',
    title: '存货',
    icon: 'Box',
    hub: [
      {
        title: '存货单据',
        links: [
          { title: '调拨单', path: '/placeholder/stock/transfer' },
          { title: '拆分单', path: '/placeholder/stock/split' },
          { title: '组装单', path: '/placeholder/stock/assemble' },
          { title: '报废单', path: '/placeholder/stock/scrap' },
          { title: '盘点单', path: '/placeholder/stock/count' },
          { title: '成本调拨单', path: '/placeholder/stock/cost-transfer' },
          { title: '数量拆分单', path: '/placeholder/stock/qty-split' },
          { title: '数量组装单', path: '/placeholder/stock/qty-assemble' },
          { title: '数量盘点单', path: '/placeholder/stock/qty-count' },
        ],
      },
      {
        title: '存货报表',
        links: [
          { title: '库存汇总查询', path: '/erp/stock', ready: true },
          { title: '库存余额查询', path: '/erp/stock', ready: true },
          { title: '库存跟踪查询', path: '/placeholder/stock/track' },
          { title: '金额库存台账', path: '/placeholder/stock/amount-ledger' },
          { title: '数量库存台账', path: '/erp/stock', ready: true },
          { title: '库存自检表', path: '/placeholder/stock/self-check' },
          { title: '报废商品统计', path: '/placeholder/stock/scrap-stats' },
          { title: '盘点盈亏数量分析', path: '/placeholder/stock/count-qty' },
          { title: '盘点盈亏金额分析', path: '/placeholder/stock/count-amount' },
          { title: '调拨成本查询', path: '/placeholder/stock/transfer-cost' },
          { title: '同盟仓汇总查询', path: '/placeholder/stock/alliance' },
        ],
      },
      {
        title: '存货提醒',
        links: [
          { title: '呆滞商品提醒', path: '/placeholder/stock/alert-slow' },
          { title: '保质期到期提醒', path: '/placeholder/stock/alert-expiry' },
          { title: '仓库缺货提醒', path: '/placeholder/stock/alert-oos' },
          { title: '安全库存提醒', path: '/placeholder/stock/alert-safety' },
          { title: '小色号预警提醒', path: '/placeholder/stock/alert-shade' },
          { title: '加工未调拨提醒', path: '/placeholder/stock/alert-process' },
          { title: '库龄超期提醒', path: '/placeholder/stock/alert-age' },
        ],
      },
    ],
  },
  {
    key: 'finance',
    title: '财务',
    icon: 'Money',
    hub: [
      {
        title: '财务单据',
        links: [
          { title: '待摊费用', path: '/placeholder/finance/prepaid' },
          { title: '业务费用单', path: '/placeholder/finance/expense' },
          { title: '加工结算单', path: '/placeholder/finance/process-settle' },
          { title: '施工结算单', path: '/placeholder/finance/construct-settle' },
          { title: '工资单', path: '/placeholder/finance/payroll' },
          { title: '其他支出单', path: '/placeholder/finance/other-out' },
          { title: '其他收入单', path: '/placeholder/finance/other-in' },
          { title: '资金调拨单', path: '/placeholder/finance/fund-transfer' },
          { title: '成本调整单', path: '/placeholder/finance/cost-adjust' },
          { title: '应收管理', path: '/finance/receivables', ready: true },
        ],
      },
      {
        title: '财务报表',
        links: [
          { title: '施工费往来对账', path: '/placeholder/finance/construct-recon' },
          { title: '费用汇总统计', path: '/placeholder/finance/expense-summary' },
          { title: '其他支出统计', path: '/placeholder/finance/other-out-stats' },
          { title: '其他收入统计', path: '/placeholder/finance/other-in-stats' },
          { title: '其他往来对账', path: '/placeholder/finance/other-recon' },
          { title: '手续费支出统计', path: '/placeholder/finance/fee-stats' },
          { title: '合伙人分成统计', path: '/placeholder/finance/partner-share' },
          { title: '支票到账统计', path: '/placeholder/finance/check-stats' },
          { title: '收银交款统计', path: '/placeholder/finance/cashier-stats' },
          { title: '收支流水账', path: '/placeholder/finance/io-journal' },
          { title: '资金流水账', path: '/placeholder/finance/cash-journal' },
        ],
      },
      {
        title: '财务提醒',
        links: [{ title: '收银交款提醒', path: '/placeholder/finance/cashier-alert' }],
      },
    ],
  },
  {
    key: 'aftersale',
    title: '售后',
    icon: 'Service',
    hub: [
      {
        title: '售后业务',
        links: [
          { title: '售后工单', path: '/placeholder/aftersale/ticket' },
          { title: '投诉处理', path: '/placeholder/aftersale/complaint' },
        ],
      },
    ],
  },
  {
    key: 'distribution',
    title: '分销',
    icon: 'Share',
    hub: [
      {
        title: '分销业务',
        links: [
          { title: '分销订单', path: '/placeholder/distribution/order' },
          { title: '渠道管理', path: '/placeholder/distribution/channel' },
        ],
      },
    ],
  },
  {
    key: 'alliance',
    title: '联盟',
    icon: 'Connection',
    hub: [
      {
        title: '联盟协作',
        links: [
          { title: '同盟仓', path: '/placeholder/alliance/warehouse' },
          { title: '同盟往来', path: '/placeholder/alliance/ledger' },
        ],
      },
    ],
  },
  {
    key: 'master',
    title: '资料',
    icon: 'Notebook',
    hub: [
      {
        title: '基础资料',
        links: [
          { title: '商品档案', path: '/erp/products', ready: true },
          { title: '客户档案', path: '/placeholder/master/customer' },
          { title: '供应商档案', path: '/placeholder/master/supplier' },
          { title: '仓库档案', path: '/placeholder/master/warehouse' },
        ],
      },
    ],
  },
  {
    key: 'settings',
    title: '设置',
    icon: 'Setting',
    hub: [
      {
        title: '系统设置',
        links: [
          { title: '用户管理', path: '/system/users', ready: true },
          { title: '审批中心', path: '/oa/approvals', ready: true },
          { title: '组织权限', path: '/placeholder/settings/rbac' },
        ],
      },
    ],
  },
  {
    key: 'decision',
    title: '决策',
    icon: 'DataAnalysis',
    hub: [
      {
        title: '经营决策',
        links: [
          { title: '经营看板', path: '/placeholder/decision/board' },
          { title: '毛利分析', path: '/placeholder/decision/margin' },
        ],
      },
    ],
  },
  {
    key: 'cloud',
    title: '云店',
    icon: 'Shop',
    hub: [
      {
        title: '云店运营',
        links: [
          { title: '商品上架', path: '/placeholder/cloud/listing' },
          { title: '线上订单', path: '/placeholder/cloud/orders' },
        ],
      },
    ],
  },
]

export function findSideMenu(key: string): SideMenuItem | undefined {
  return sideMenus.find((item) => item.key === key)
}
