<template>
  <div class="register-page">
    <div class="register-card">
      <div class="register-card-glow"></div>
      <div class="register-card-inner">
        <div class="register-header">
          <h1>创建账号</h1>
          <p>加入「往」智能对话平台</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleRegister">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" size="large" show-password />
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input v-model="form.nickname" placeholder="昵称（可选）" prefix-icon="UserFilled" size="large" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="brand-btn register-btn" size="large" :loading="loading" @click="handleRegister">
              注 册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="register-footer">
          已有账号？<router-link to="/login">返回登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/user'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({ username: '', password: '', confirmPassword: '', nickname: '' })

const validateConfirmPassword = (_rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }]
}

/** 处理注册 */
async function handleRegister() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  loading.value = true
  try {
    await register({
      username: form.username,
      password: form.password,
      nickname: form.nickname
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background:
    radial-gradient(ellipse 80% 60% at 50% -20%, rgba(26, 86, 219, 0.08), transparent),
    radial-gradient(ellipse 60% 50% at 80% 80%, rgba(79, 70, 229, 0.06), transparent),
    #f6f8fc;
  position: relative;
}

.register-card {
  position: relative;
  width: 400px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 4px 24px rgba(15, 23, 42, 0.06), 0 1px 3px rgba(15, 23, 42, 0.04);
  overflow: hidden;
}

.register-card-glow {
  position: absolute;
  top: -60px;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 100px;
  background: radial-gradient(ellipse, rgba(26, 86, 219, 0.10), transparent 70%);
  pointer-events: none;
}

.register-card-inner {
  position: relative;
  padding: 36px 36px 28px;
  z-index: 1;
}

.register-header {
  text-align: center;
  margin-bottom: 28px;
}

.register-header h1 {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 6px;
  letter-spacing: -0.01em;
}

.register-header p {
  font-size: 14px;
  color: var(--text-tertiary);
}

.register-btn {
  width: 100%;
  height: 44px;
  font-size: 15px;
}

.register-footer {
  text-align: center;
  margin-top: 24px;
  color: var(--text-tertiary);
  font-size: 14px;
}

.register-footer a {
  color: var(--primary);
  text-decoration: none;
  font-weight: 500;
  margin-left: 4px;
}

.register-footer a:hover {
  text-decoration: underline;
}

.register-card-inner :deep(.el-form-item) {
  margin-bottom: 18px;
}

.register-card-inner :deep(.el-form-item:last-child) {
  margin-bottom: 0;
}
</style>
