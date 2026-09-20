# 材陆建材管理系统

进销存 + 微OA + 财务。模块化单体 + DDD + 前后端分离。

```text
sys-coydemo-cailu-1/
├── backend/     Spring Boot 3 · MyBatis Plus · MySQL · Redis · MQ
├── frontend/    Vue3 · TypeScript · Vite · Element Plus
├── mobile/      UniApp
└── scripts/     本机辅助脚本（如 start-mysql.ps1）
```

| 层 | 启动 |
|----|------|
| MySQL（本机） | `powershell -File scripts/start-mysql.ps1`（root/root，库 `cailu_bom`） |
| Redis（本机） | Windows 服务 `Redis`（6379） |
| 后端 | `cd backend && mvn -pl cailu-bom-bootstrap -am spring-boot:run` |
| 前端 | `cd frontend && npm install && npm run dev` |

Docker Desktop 已安装；首次启用 **WSL2** 后需**重启电脑**，再执行：

```bash
cd backend/docker
docker compose up -d mysql redis rabbitmq
```

Web 开发默认代理：`/api` → `http://127.0.0.1:8080`。
