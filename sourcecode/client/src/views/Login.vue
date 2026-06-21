<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-card-glow"></div>
      <div class="login-card-inner">
        <div class="login-header">
          <span class="brand-mark-large">往</span>
          <h1>全模态智能助手</h1>
          <p class="subtitle">基于 LangChain4j 的企业级对话系统</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleLogin">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" :prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" size="large" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="brand-btn login-btn" size="large" :loading="loading" @click="handleLogin">
              登 录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="login-footer">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { login } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await login(form)
    userStore.setLogin(res.data)
    ElMessage.success('登录成功')
    router.push(res.data.role === 'ADMIN' ? '/admin' : '/chat')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background:
    radial-gradient(ellipse 80% 60% at 50% -20%, rgba(26, 86, 219, 0.08), transparent),
    radial-gradient(ellipse 60% 50% at 80% 80%, rgba(79, 70, 229, 0.06), transparent),
    #f6f8fc;
  position: relative;
  overflow: hidden;
}

.login-card {
  position: relative;
  width: 400px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 4px 24px rgba(15, 23, 42, 0.06), 0 1px 3px rgba(15, 23, 42, 0.04);
  overflow: hidden;
}

.login-card-glow {
  position: absolute;
  top: -60px;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 100px;
  background: radial-gradient(ellipse, rgba(26, 86, 219, 0.10), transparent 70%);
  pointer-events: none;
}

.login-card-inner {
  position: relative;
  padding: 40px 36px 32px;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.brand-mark-large {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  background: var(--brand-gradient);
  border-radius: 16px;
  color: #fff;
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 16px;
  box-shadow: 0 8px 24px rgba(26, 86, 219, 0.25);
}

.login-header h1 {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 6px;
  letter-spacing: -0.01em;
}

.subtitle {
  font-size: 14px;
  color: var(--text-tertiary);
  font-weight: 400;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: var(--text-tertiary);
  font-size: 14px;
}

.login-footer a {
  color: var(--primary);
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
}

.login-footer a:hover {
  text-decoration: underline;
}

/* Form item spacing */
.login-card-inner :deep(.el-form-item) {
  margin-bottom: 20px;
}

.login-card-inner :deep(.el-form-item:last-child) {
  margin-bottom: 0;
}
</style>
