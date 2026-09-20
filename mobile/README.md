# 材陆建材 · UniApp 移动端

与 Web 共用后端 `/api/**` 契约，模块与 `frontend` / `backend` 一致：进销存、微OA、财务。

## 现状

当前为可扩展骨架（pages + http 封装），建议用 **HBuilderX** 打开本目录，或接入 `@dcloudio/vite-plugin-uni` 后再完整编译。

## 页面

| 页面 | 路径 | API |
|------|------|-----|
| 工作台 | `pages/index/index` | — |
| 商品 | `pages/erp/products` | `GET /api/erp/products/{id}` |
| 审批 | `pages/oa/approvals` | `GET /api/oa/approvals/{id}` · `POST .../submissions` |
| 应收 | `pages/finance/receivables` | `GET /api/finance/receivables/{id}` |
