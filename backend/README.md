# 建材管理系统 · 后端

模块化单体 + DDD：进销存、微 OA、财务。

```bash
cd backend/docker && docker compose up -d mysql redis
cd .. && mvn -pl cailu-bom-bootstrap -am spring-boot:run
```

| 项 | 选型 |
|----|------|
| 架构 | 模块化单体 + DDD + 前后端分离 |
| 后端 | Spring Boot 3.3 · MyBatis Plus 3.5 · MySQL · Redis · RabbitMQ（事件接入后再加 AMQP） |
| 前端 | Vue3 · TypeScript · Vite · Element Plus（`../frontend`） |
| 部署 | Docker · Nginx |

## 模块

```
backend/
├── cailu-bom-common
├── cailu-bom-system
├── cailu-bom-erp
├── cailu-bom-oa
├── cailu-bom-finance
├── cailu-bom-bootstrap
├── sql/
└── docker/
```

四层：`interfaces` → `application` → `domain` → `infrastructure`。

## API（与前端契约）

| Method | Path | 说明 |
|--------|------|------|
| POST | `/api/system/auth/login` | JWT 登录（默认 admin/admin123） |
| POST | `/api/system/users` | 创建用户 |
| GET | `/api/system/users/{id}` | 用户详情 |
| GET | `/api/erp/products` | 商品分页列表 |
| POST | `/api/erp/products` | 创建商品 |
| GET | `/api/erp/products/{id}` | 商品详情 |
| GET | `/api/erp/stocks` | 库存分页列表 |
| GET | `/api/erp/purchases` | 采购单分页 |
| POST | `/api/erp/purchases` | 创建采购草稿 |
| GET | `/api/erp/purchases/{id}` | 采购详情 |
| POST | `/api/erp/purchases/{id}/inbounds` | 采购入库 |
| POST | `/api/oa/approvals` | 创建审批单 |
| POST | `/api/oa/approvals/{id}/submissions` | 提交审批（draft→pending） |
| POST | `/api/finance/receivables` | 创建应收 |
| POST | `/api/finance/receivables/{id}/payments` | 收款 |

## 依赖说明

- MyBatis-Plus 使用官方 `mybatis-plus-bom` + `mybatis-plus-jsqlparser`（分页必需）
- JWT / RabbitMQ 依赖按需引入，避免空依赖
