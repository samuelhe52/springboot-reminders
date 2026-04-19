# 待办事项提醒系统

基于 Spring Boot + Vue 3 的全栈待办事项管理系统，支持用户注册登录、分类管理与任务管理。

---

## 技术栈

| 层次 | 技术 |
| ---- | ---- |
| 后端框架 | Spring Boot 3.3.5（Java 17） |
| 持久层 | MyBatis（注解模式） + HikariCP |
| 数据库 | MySQL 8.4 / H2（本地开发） |
| 会话管理 | HttpSession（服务端 Session） |
| 前端框架 | Vue 3 + Vue Router 4 |
| 前端构建 | Vite 6 |
| UI 组件库 | Element Plus 2 |
| HTTP 客户端 | Axios |
| 容器化 | Docker Compose |

---

## 项目结构

```text
springboot-reminders/
├── backend/                        # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/java26groupwork/reminder/
│       │   ├── ReminderApplication.java
│       │   ├── common/
│       │   │   └── GlobalExceptionHandler.java   # 全局异常处理
│       │   ├── config/
│       │   │   └── WebMvcConfig.java              # 拦截器注册
│       │   ├── controller/
│       │   │   ├── UserController.java            # 用户相关接口
│       │   │   ├── CategoryController.java        # 分类相关接口
│       │   │   └── TodoController.java            # 待办相关接口
│       │   ├── entity/
│       │   │   ├── User.java
│       │   │   ├── TodoCategory.java
│       │   │   └── Todo.java
│       │   ├── interceptor/
│       │   │   └── LoginInterceptor.java          # 登录校验拦截器
│       │   ├── mapper/                            # MyBatis Mapper 接口
│       │   └── service/                          # 业务逻辑层
│       └── resources/
│           ├── application.yml                   # 应用配置
│           ├── application-local.yml             # 本地 H2 配置
│           ├── schema.sql                        # MySQL 建表脚本
│           └── schema-local.sql                  # H2 本地建表脚本
├── frontend/                       # Vue 3 前端
│   ├── index.html
│   ├── vite.config.js              # 含反向代理配置
│   ├── package.json
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── api/                    # Axios 接口封装
│       │   ├── http.js
│       │   ├── user.js
│       │   ├── category.js
│       │   └── todo.js
│       ├── router/index.js         # 路由配置
│       └── views/
│           ├── AuthView.vue        # 注册 / 登录页
│           ├── CategoryView.vue    # 分类管理页
│           └── TodoView.vue        # 待办事项页
├── docker-compose.yml              # MySQL 容器配置
└── Makefile                        # 常用命令快捷方式
```

---

## 数据库结构

数据库名：`reminder_db`，建表脚本位于 [backend/src/main/resources/schema.sql](backend/src/main/resources/schema.sql)。

```text
user           — 用户表（id, username, password, create_time）
todo_category  — 分类表（id, user_id, name, create_time）
todo           — 待办表（id, user_id, category_id, title, notes, status, deadline, create_time, update_time）
```

`todo.status`：`0` = 未完成，`1` = 已完成。

---

## API 接口

后端统一前缀 `http://localhost:8080/api`，前端通过 Vite 代理 `/api` → `localhost:8080`。

### 用户（无需登录）

| 方法 | 路径 | 说明 |
| ---- | ---- | ---- |
| POST | `/api/user/register` | 注册 |
| POST | `/api/user/login` | 登录（写入 Session） |
| GET | `/api/user/logout` | 登出（销毁 Session） |

### 分类（需登录）

| 方法 | 路径 | 说明 |
| ---- | ---- | ---- |
| POST | `/api/category/add` | 新增分类 |
| GET | `/api/category/list` | 获取当前用户的所有分类 |
| POST | `/api/category/update` | 更新分类名 |
| POST | `/api/category/delete` | 删除分类 |

### 待办事项（需登录）

| 方法 | 路径 | 说明 |
| ---- | ---- | ---- |
| POST | `/api/todo/add` | 新增待办 |
| GET | `/api/todo/list` | 获取待办列表，可按 `status`、`categoryId` 过滤 |
| POST | `/api/todo/update` | 更新待办 |
| POST | `/api/todo/delete` | 删除待办 |

> 分类和待办接口均受 `LoginInterceptor` 保护；未登录时返回 HTTP 401。

---

## 环境要求

- Java 17+
- Maven 3.8+
- Node.js 18+ / npm 9+
- Docker & Docker Compose（如需使用 MySQL）

---

## 快速启动

### 方式一：使用 Makefile（推荐）

```bash
# 1. 安装前端依赖
make install

# 2. 启动 MySQL 容器并运行后端（两步合一）
make backend
# 等价于：docker compose up -d mysql && cd backend && mvn spring-boot:run

# 3. 在另一个终端启动前端
make frontend
# 等价于：cd frontend && npm run dev
```

访问 [http://localhost:5173](http://localhost:5173)

---

### 方式二：本地快速启动（不依赖 Docker）

适用于本地联调、接口测试、单人开发。后端使用 `application-local.yml` 中的 H2 内存数据库启动，不需要安装或启动 MySQL。

#### 第一步：启动后端

```bash
cd backend
SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
```

后端启动后监听 `http://localhost:8080`，并自动执行 `schema-local.sql` 初始化表结构。

如需查看 H2 控制台，可访问 `http://localhost:8080/h2-console`，JDBC URL 使用：

```text
jdbc:h2:mem:reminder_db
```

#### 第二步：启动前端

```bash
cd frontend
npm install    # 首次运行时安装依赖
npm run dev
```

前端启动后监听 `http://localhost:5173`，`/api` 请求会被反向代理至 `localhost:8080`。

---

### 方式三：手动启动 MySQL 版本

#### 第一步：启动 MySQL 数据库

**（推荐）Docker 方式：**

```bash
docker compose up -d mysql
```

Docker Compose 会自动：

- 启动 MySQL 8.4，端口 `3306`
- 创建数据库 `reminder_db`，root 密码 `root`
- 自动挂载并执行 `schema.sql` 完成建表

**手动方式（已有本地 MySQL）：**

在 MySQL 中手动执行建表脚本：

```bash
mysql -u root -p < backend/src/main/resources/schema.sql
```

然后修改 [backend/src/main/resources/application.yml](backend/src/main/resources/application.yml) 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/reminder_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root      # 修改为你的用户名
    password: root      # 修改为你的密码
```

#### 第二步：启动后端

```bash
cd backend
mvn spring-boot:run
```

后端启动后监听 `http://localhost:8080`。

#### 第三步：启动前端

```bash
cd frontend
npm install    # 首次运行时安装依赖
npm run dev
```

前端启动后监听 `http://localhost:5173`，`/api` 请求会被反向代理至 `localhost:8080`。

---

## 所有 Makefile 命令

```bash
make help             # 查看所有命令说明
make install          # 安装前端依赖（npm install）
make backend          # 启动 MySQL 容器 + 运行后端
make frontend         # 启动 Vite 开发服务器
make build            # 构建后端 jar + 前端静态资源
make build-backend    # 仅构建后端（mvn clean package）
make build-frontend   # 仅构建前端（npm run build）
make test             # 运行后端单元测试（mvn test）
make format           # 格式化前端代码（Prettier）
make db-reset         # 重建 MySQL 容器（清空数据）
make clean            # 删除 target/ 和 dist/ 构建产物
```

---

## 注意事项

- Docker Compose 的 MySQL 默认账号密码均为 `root`，与 `application.yml` 保持一致，无需额外修改。
- 本地无 Docker 场景可使用 `SPRING_PROFILES_ACTIVE=local` 启动后端，此时使用 H2 内存数据库，重启应用后数据会清空。
- `make db-reset` 会执行 `docker compose down -v`，**会清空所有数据**，仅在需要重置数据库时使用。
- 后端通过 `HttpSession` 管理登录状态，不使用 JWT；关闭浏览器 Tab 后 Session 仍有效，直到调用 `/api/user/logout` 或 Session 过期。
