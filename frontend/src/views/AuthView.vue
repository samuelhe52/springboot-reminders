<template>
  <div class="page">
    <el-card class="auth-card">
      <h2 class="title">Reminder System</h2>
      <p class="subtitle">Track tasks with less friction.</p>
      <p v-if="registerNotice" class="register-notice">{{ registerNotice }}</p>
      <el-form :model="form" label-width="90px">
        <el-form-item label="Username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">Login</el-button>
          <el-button @click="register">Register</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { loginApi, registerApi } from "../api/user";

const router = useRouter();
const form = reactive({ username: "", password: "" });
const registerNotice = ref("");

const register = async () => {
  await registerApi(form);
  registerNotice.value =
    "Registration successful. Please log in with your registered credentials.";
  ElMessage.success("Registered successfully");
};

const login = async () => {
  await loginApi(form);
  registerNotice.value = "";
  ElMessage.success("Logged in");
  await router.push("/todo");
};
</script>

<style scoped>
:global(body) {
  margin: 0;
  font-family:
    "Inter",
    "Segoe UI",
    "SF Pro Text",
    "Helvetica Neue",
    Arial,
    sans-serif;
}

.page {
  --bg-start: #eef4ff;
  --bg-end: #f9fbff;
  --card-bg: rgba(255, 255, 255, 0.92);
  --text-primary: #0f172a;
  --text-muted: #64748b;
  --notice-bg: #ecfdf3;
  --notice-border: #86efac;
  --notice-text: #166534;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background:
    radial-gradient(circle at 0% 0%, #dbeafe 0%, transparent 38%),
    linear-gradient(160deg, var(--bg-start), var(--bg-end));
}

.auth-card {
  width: 460px;
  border-radius: 16px;
  border: 1px solid rgba(148, 163, 184, 0.25);
  background-color: var(--card-bg);
  color: var(--text-primary);
  box-shadow:
    0 20px 38px rgba(15, 23, 42, 0.08),
    0 6px 12px rgba(15, 23, 42, 0.08);
}

.auth-card :deep(.el-card__body) {
  padding: 28px 28px 20px;
}

.title {
  margin: 0;
  letter-spacing: 0.2px;
}

.subtitle {
  margin: 8px 0 18px;
  color: var(--text-muted);
}

.register-notice {
  margin: 0 0 16px;
  padding: 10px 12px;
  border: 1px solid var(--notice-border);
  border-radius: 10px;
  background: var(--notice-bg);
  color: var(--notice-text);
  font-size: 14px;
}

.auth-card :deep(.el-form-item__label) {
  color: var(--text-primary);
}

@media (max-width: 560px) {
  .auth-card {
    width: min(460px, calc(100vw - 32px));
  }
}

@media (prefers-color-scheme: dark) {
  .page {
    --bg-start: #0b1220;
    --bg-end: #111827;
    --card-bg: rgba(17, 24, 39, 0.86);
    --text-primary: #f1f5f9;
    --text-muted: #94a3b8;
    --notice-bg: rgba(20, 83, 45, 0.35);
    --notice-border: rgba(74, 222, 128, 0.7);
    --notice-text: #bbf7d0;
  }

  .auth-card {
    border-color: rgba(71, 85, 105, 0.42);
    box-shadow:
      0 20px 38px rgba(2, 6, 23, 0.55),
      0 6px 12px rgba(2, 6, 23, 0.5);
  }

  .auth-card :deep(.el-input__wrapper) {
    background-color: rgba(30, 41, 59, 0.9);
    box-shadow: 0 0 0 1px rgba(100, 116, 139, 0.55) inset;
  }

  .auth-card :deep(.el-input__inner) {
    color: #f1f5f9;
  }
}
</style>
