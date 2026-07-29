<template>
  <div class="register-page">
    <div class="register-bg"></div>
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <div class="logo-wrapper">
            <span class="logo-icon">🐾</span>
          </div>
          <h2 class="register-title">注册 PetLife</h2>
          <p class="register-subtitle">创建您的账户，开启宠物生活之旅</p>
        </div>
        
        <el-form 
          :model="form" 
          :rules="rules" 
          ref="formRef"
          @submit.prevent="handleRegister"
          class="register-form"
        >
          <el-form-item prop="phone">
            <el-input 
              v-model="form.phone" 
              placeholder="请输入手机号"
              prefix-icon="Phone"
              size="large"
              :class="{ 'input-focus': focusedField === 'phone' }"
              @focus="focusedField = 'phone'"
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
          
          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="form.confirmPassword" 
              type="password" 
              placeholder="请确认密码"
              prefix-icon="Lock"
              suffix-icon="View"
              size="large"
              :show-password="showConfirmPassword"
              :class="{ 'input-focus': focusedField === 'confirmPassword' }"
              @focus="focusedField = 'confirmPassword'"
              @blur="focusedField = ''"
              @click:suffix="showConfirmPassword = !showConfirmPassword"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button 
              type="primary" 
              @click="handleRegister" 
              class="register-btn"
              size="large"
              :loading="loading"
            >
              {{ loading ? '注册中...' : '注 册' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="register-link">
          已有账号？<a href="/login">立即登录</a>
        </div>
        
        <div class="register-footer">
          <p>© 2026 PetLife 宠物生活平台</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const formRef = ref(null)
const loading = ref(false)
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const focusedField = ref('')

const form = reactive({
  phone: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 30, message: '密码长度为6-30个字符', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,30}$/, message: '密码需包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const router = useRouter()

async function handleRegister() {
  if (!formRef.value) return
  
  await formRef.value.validate((valid) => {
    if (!valid) return
  })
  
  loading.value = true
  try {
    const response = await axios.post('/auth/user/register', {
      phone: form.phone,
      password: form.password
    })
    if (response.code === 200) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('注册失败，请检查网络')
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
  position: relative;
  overflow: hidden;
}

.register-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-bg::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
}

.register-bg::after {
  content: '';
  position: absolute;
  bottom: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.08) 0%, transparent 50%);
}

.register-container {
  width: 100%;
  max-width: 420px;
  padding: 20px;
  position: relative;
  z-index: 10;
}

.register-card {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  padding: 50px 40px;
  border-radius: 24px;
  box-shadow: 0 25px 80px rgba(102, 126, 234, 0.3);
}

.register-header {
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

.register-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 8px;
}

.register-subtitle {
  font-size: 14px;
  color: #666;
}

.register-form {
  margin-bottom: 24px;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.register-form :deep(.el-input__wrapper) {
  border-radius: 12px;
  padding: 12px 16px;
  border: 2px solid #e8e8e8;
  transition: all 0.3s ease;
  background: #fafafa;
}

.register-form :deep(.el-input__wrapper:hover) {
  border-color: #667eea;
}

.register-form :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
  background: white;
}

.register-form :deep(.el-input__prefix) {
  color: #999;
  margin-right: 8px;
}

.register-form :deep(.el-input__suffix) {
  color: #999;
  cursor: pointer;
}

.register-form :deep(.el-input__suffix:hover) {
  color: #667eea;
}

.register-form :deep(.el-input__inner) {
  font-size: 15px;
  padding: 0;
  height: auto;
  line-height: 1.5;
}

.register-btn {
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

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.register-btn:active {
  transform: translateY(0);
}

.register-link {
  text-align: center;
  margin-bottom: 30px;
  font-size: 14px;
  color: #666;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.register-link a:hover {
  text-decoration: underline;
}

.register-footer {
  text-align: center;
}

.register-footer p {
  font-size: 12px;
  color: #999;
}

@media (max-width: 480px) {
  .register-card {
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
  
  .register-title {
    font-size: 24px;
  }
}
</style>
