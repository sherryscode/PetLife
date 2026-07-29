<template>
  <div class="login-page">
    <div class="login-bg"></div>
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-wrapper">
            <span class="logo-icon">🐾</span>
          </div>
          <h2 class="login-title">管理员登录</h2>
          <p class="login-subtitle">欢迎回来，请登录您的账户</p>
        </div>
        
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="formRef"
          @submit.prevent="handleLogin"
          class="login-form"
        >
          <el-form-item prop="username">
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名"
              prefix-icon="User"
              size="large"
              :class="{ 'input-focus': focusedField === 'username' }"
              @focus="focusedField = 'username'"
              @blur="focusedField = ''"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码"
              prefix-icon="Lock"
              suffix-icon="View"
              size="large"
              :show-password="showPassword"
              :class="{ 'input-focus': focusedField === 'password' }"
              @focus="focusedField = 'password'"
              @blur="focusedField = ''"
              @click:suffix="showPassword = !showPassword"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button 
              type="primary" 
              @click="handleLogin" 
              class="login-btn"
              size="large"
              :loading="loading"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="login-footer">
          <p>© 2026 PetLife 宠物生活管理系统</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAdminStore } from '../stores/admin'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const formRef = ref(null)
const loading = ref(false)
const showPassword = ref(false)
const focusedField = ref('')

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 30, message: '密码长度为6-30个字符', trigger: 'blur' }
  ]
}

const router = useRouter()
const adminStore = useAdminStore()

async function handleLogin() {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (!valid) return
  })
  
  loading.value = true
  try {
    const response = await axios.post('/auth/admin/login', form)
    if (response.code === 200) {
      adminStore.setToken(response.data.token)
      adminStore.setAdminInfo(response.data.admin)
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查网络')
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
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
}

.login-bg::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 30% 20%, rgba(102, 126, 234, 0.15) 0%, transparent 50%);
}

.login-bg::after {
  content: '';
  position: absolute;
  bottom: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 70% 80%, rgba(118, 75, 162, 0.1) 0%, transparent 50%);
}

.login-container {
  width: 100%;
  max-width: 420px;
  padding: 20px;
  position: relative;
  z-index: 10;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  padding: 50px 40px;
  border-radius: 24px;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.35);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-wrapper {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.logo-icon {
  font-size: 40px;
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
}

.login-form {
  margin-bottom: 30px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  padding: 12px 16px;
  border: 2px solid #e8e8e8;
  transition: all 0.3s ease;
  background: #fafafa;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: #667eea;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
  background: white;
}

.login-form :deep(.el-input__prefix) {
  color: #999;
  margin-right: 8px;
}

.login-form :deep(.el-input__suffix) {
  color: #999;
  cursor: pointer;
}

.login-form :deep(.el-input__suffix:hover) {
  color: #667eea;
}

.login-form :deep(.el-input__inner) {
  font-size: 15px;
  padding: 0;
  height: auto;
  line-height: 1.5;
}

.login-btn {
  width: 100%;
  height: 52px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.login-btn:active {
  transform: translateY(0);
}

.login-footer {
  text-align: center;
}

.login-footer p {
  font-size: 12px;
  color: #999;
}

@media (max-width: 480px) {
  .login-card {
    padding: 40px 24px;
    border-radius: 16px;
  }
  
  .logo-wrapper {
    width: 60px;
    height: 60px;
    margin-bottom: 16px;
  }
  
  .logo-icon {
    font-size: 30px;
  }
  
  .login-title {
    font-size: 24px;
  }
}
</style>
