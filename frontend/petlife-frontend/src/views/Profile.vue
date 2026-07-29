<template>
  <div class="profile-page">
    <div class="container">
      <h2>👤 个人中心</h2>
      
      <div class="profile-card">
        <div class="avatar-section">
          <div class="avatar">
            <img :src="userStore.userInfo?.avatar || 'https://via.placeholder.com/100'" alt="" />
          </div>
          <h3>{{ userStore.userInfo?.nickname }}</h3>
          <p>{{ userStore.userInfo?.phone }}</p>
        </div>
        
        <div class="info-section">
          <el-form :model="userForm">
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickname" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userForm.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
                <el-radio :value="0">未知</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="userForm.address" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateProfile">保存</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="stats-section">
        <div class="stat-item">
          <div class="stat-value">{{ petCount }}</div>
          <div class="stat-label">我的宠物</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ orderCount }}</div>
          <div class="stat-label">我的订单</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ postCount }}</div>
          <div class="stat-label">我的帖子</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useUserStore } from '../stores/user'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const userForm = ref({
  nickname: '',
  gender: 0,
  address: ''
})

const petCount = ref(0)
const orderCount = ref(0)
const postCount = ref(0)

onMounted(() => {
  initForm()
  loadStats()
})

watch(() => userStore.userInfo, () => {
  initForm()
}, { deep: true })

function initForm() {
  if (userStore.userInfo) {
    userForm.value = {
      nickname: userStore.userInfo.nickname,
      gender: userStore.userInfo.gender || 0,
      address: userStore.userInfo.address || ''
    }
  }
}

async function loadStats() {
  if (!userStore.userInfo) return
  
  try {
    const [petRes, reserveOrderRes, mallOrderRes, postRes] = await Promise.all([
      axios.get(`/health/pet/user/${userStore.userInfo.id}`),
      axios.get(`/order/order/list?userId=${userStore.userInfo.id}&page=1&size=1`),
      axios.get(`/mall/order/list?userId=${userStore.userInfo.id}&page=1&size=1`),
      axios.get(`/social/post/list?userId=${userStore.userInfo.id}&page=1&size=1&auditStatus=1`)
    ])
    
    petCount.value = petRes.code === 200 ? petRes.data.length : 0
    const reserveTotal = reserveOrderRes.code === 200 ? reserveOrderRes.data.total : 0
    const mallTotal = mallOrderRes.code === 200 ? mallOrderRes.data.total : 0
    orderCount.value = reserveTotal + mallTotal
    postCount.value = postRes.code === 200 ? postRes.data.total : 0
  } catch (error) {
    console.error(error)
  }
}

async function updateProfile() {
  try {
    const response = await axios.put(`/auth/user/${userStore.userInfo.id}`, userForm.value)
    if (response.code === 200) {
      userStore.setUserInfo(response.data)
      ElMessage.success('更新成功')
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}
</script>

<style scoped>
.profile-page {
  padding: 40px 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.profile-card {
  background: #f8f9fa;
  padding: 40px;
  border-radius: 16px;
  margin-bottom: 32px;
}

.avatar-section {
  text-align: center;
  margin-bottom: 32px;
}

.avatar img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
}

.stats-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.stat-item {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  text-align: center;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #667eea;
}

.stat-label {
  font-size: 16px;
  color: #666;
  margin-top: 8px;
}
</style>