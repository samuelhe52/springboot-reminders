<template>
  <div class="page">
    <div class="auth-card">
      <h1 class="title">待办事项</h1>
      <p class="subtitle">整理任务，掌握进度。</p>
      <el-form :model="form" label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
          <el-button @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { ElNotification } from "element-plus";
import { loginApi, registerApi } from "../api/user";

const notify = (message, type = "success") =>
  ElNotification({ message, type, position: "bottom-right", duration: 2500 });

const router = useRouter();
const form = reactive({ username: "", password: "" });

const register = async () => {
  await registerApi(form);
  notify("注册成功，请登录");
};

const login = async () => {
  await loginApi(form);
  notify("登录成功");
  await router.push("/todo");
};
</script>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.auth-card {
  width: 420px;
  max-width: calc(100vw - 48px);
  background: var(--app-panel-bg);
  border: 1px solid var(--app-panel-border);
  border-radius: 12px;
  padding: 36px 32px 28px;
  box-shadow: var(--app-shadow);
  color: var(--app-text);
}

.title {
  margin: 0 0 6px;
  font-size: 22px;
  font-weight: 700;
}

.subtitle {
  margin: 0 0 24px;
  color: var(--app-muted);
  font-size: 14px;
}
</style>
