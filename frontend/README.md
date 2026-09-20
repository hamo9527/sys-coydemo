# 材陆建材 · Web 前端

Vue3 + TypeScript + Vite + Element Plus，对接 `backend` 的 `/api/**` 契约。

```bash
cd frontend
npm install
npm run dev
```

| 项 | 说明 |
|----|------|
| 开发地址 | http://127.0.0.1:5173 |
| API 代理 | `/api` → `http://127.0.0.1:8080` |
| 登录 | `admin` / `admin123`（JWT） |
| ID | 后端雪花 Long 以**字符串**下发，前端 `Id = string` |

## 已对接页面（与后端一致）

| 页面 | API |
|------|-----|
| 登录 | `POST /api/system/auth/login` |
| 用户管理 | `POST/GET /api/system/users` |
| 商品档案 | `GET/POST /api/erp/products` |
| 库存台账 | `GET /api/erp/stocks` |
| 采购单 / 新建 | `GET/POST /api/erp/purchases` · `POST .../inbounds` |
| 审批中心 | `GET/POST /api/oa/approvals` · `POST .../submissions` |
| 应收管理 | `GET/POST /api/finance/receivables` · `POST .../payments` |

订货单 / 出库单：仅有表结构或 UI 稿，**无后端 API**（菜单未标 ready）。
