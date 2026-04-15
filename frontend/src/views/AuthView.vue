<template>
  <div class="page">
    <el-card class="auth-card">
      <h2>Reminder System</h2>
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
import {reactive} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {loginApi, registerApi} from '../api/user'

const router = useRouter()
const form = reactive({username: '', password: ''})

const register = async () => {
  await registerApi(form)
  ElMessage.success('Registered')
}

const login = async () => {
  await loginApi(form)
  ElMessage.success('Logged in')
  await router.push('/todo')
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: #f5f7fa;
}

.auth-card {
  width: 460px;
}
</style>
