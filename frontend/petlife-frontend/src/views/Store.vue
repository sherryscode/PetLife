<template>
  <div class="store-page">
    <div class="container">
      <h2>🏪 门店服务</h2>
      
      <div class="search-bar">
        <el-input v-model="searchKeyword" placeholder="搜索门店" />
        <el-button @click="searchStores">搜索</el-button>
      </div>

      <div class="store-grid">
        <div class="store-card" v-for="store in stores" :key="store.id">
          <div class="store-info">
            <h3>{{ store.storeName }}</h3>
            <p>地址：{{ store.storeAddress }}</p>
            <p>电话：{{ store.storePhone }}</p>
            <p>营业时间：{{ store.businessHours }}</p>
            <p>评分：{{ store.rating }} ⭐</p>
          </div>
          <el-button @click="viewServices(store)">查看服务</el-button>
        </div>
      </div>

      <el-dialog v-model="showServicesModal" title="服务项目" width="600px">
        <div v-if="currentStore">
          <h4>{{ currentStore.storeName }} 的服务项目</h4>
          <el-table :data="services" border style="width: 100%">
            <el-table-column prop="serviceName" label="服务名称" />
            <el-table-column prop="price" label="价格" />
            <el-table-column prop="duration" label="时长(分钟)" />
            <el-table-column prop="description" label="描述" />
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="primary" @click="bookService(scope.row)">预约</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <template #footer>
          <el-button @click="showServicesModal = false">关闭</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showBookModal" title="预约服务" width="500px">
        <el-form :model="bookForm">
          <el-form-item label="服务名称">
            <el-input :value="selectedService?.serviceName" disabled />
          </el-form-item>
          <el-form-item label="价格">
            <el-input :value="selectedService?.price" disabled />
          </el-form-item>
          <el-form-item label="预约日期">
            <el-date-picker v-model="bookForm.reserveDate" type="date" :min-date="new Date()" />
          </el-form-item>
          <el-form-item label="预约时间">
            <el-select v-model="bookForm.reserveTime">
              <el-option label="09:00" value="09:00" />
              <el-option label="10:00" value="10:00" />
              <el-option label="11:00" value="11:00" />
              <el-option label="14:00" value="14:00" />
              <el-option label="15:00" value="15:00" />
              <el-option label="16:00" value="16:00" />
              <el-option label="17:00" value="17:00" />
              <el-option label="18:00" value="18:00" />
            </el-select>
          </el-form-item>
          <el-form-item label="宠物描述">
            <el-input v-model="bookForm.petDesc" type="textarea" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="bookForm.remark" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showBookModal = false">取消</el-button>
          <el-button type="primary" @click="submitBooking">提交预约</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const stores = ref([])
const services = ref([])
const searchKeyword = ref('')
const showServicesModal = ref(false)
const showBookModal = ref(false)
const currentStore = ref(null)
const selectedService = ref(null)
const userStore = useUserStore()

const bookForm = ref({
  reserveDate: '',
  reserveTime: '',
  petDesc: '',
  remark: ''
})

onMounted(() => {
  loadStores()
})

async function loadStores() {
  try {
    const response = await axios.get('/order/store/list?page=1&size=20')
    if (response.code === 200) {
      stores.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

async function searchStores() {
  try {
    const response = await axios.get(`/order/store/list?page=1&size=20&storeName=${searchKeyword.value}`)
    if (response.code === 200) {
      stores.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

async function viewServices(store) {
  currentStore.value = store
  try {
    const response = await axios.get(`/order/service/store/${store.id}`)
    if (response.code === 200) {
      services.value = response.data
      showServicesModal.value = true
    }
  } catch (error) {
    console.error(error)
  }
}

function bookService(service) {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  selectedService.value = service
  bookForm.value = {
    reserveDate: '',
    reserveTime: '',
    petDesc: '',
    remark: ''
  }
  showBookModal.value = true
}

async function submitBooking() {
  if (!bookForm.value.reserveDate || !bookForm.value.reserveTime) {
    ElMessage.warning('请填写完整预约信息')
    return
  }
  
  try {
    const response = await axios.post('/order/order', {
      userId: userStore.userInfo.id,
      storeId: currentStore.value.id,
      serviceId: selectedService.value.id,
      reserveDate: bookForm.value.reserveDate,
      reserveTime: bookForm.value.reserveTime,
      totalAmount: selectedService.value.price,
      depositAmount: (selectedService.value.price * 0.2).toFixed(2),
      orderStatus: 0,
      payStatus: 0,
      petDesc: bookForm.value.petDesc,
      remark: bookForm.value.remark
    })
    
    if (response.code === 200) {
      ElMessage.success('预约成功')
      showBookModal.value = false
      showServicesModal.value = false
    }
  } catch (error) {
    ElMessage.error('预约失败')
    console.error(error)
  }
}
</script>

<style scoped>
.store-page {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.search-bar {
  margin-bottom: 32px;
}

.store-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.store-card {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
}

.store-card h3 {
  margin-bottom: 12px;
}

.store-card p {
  margin-bottom: 8px;
  color: #666;
}
</style>