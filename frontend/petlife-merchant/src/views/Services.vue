<template>
  <div class="services-page">
    <h2>🏪 服务管理</h2>
    <el-button type="primary" @click="showAddModal = true" class="add-btn">添加服务</el-button>
    
    <el-table :data="services" border style="width: 100%">
      <el-table-column prop="serviceName" label="服务名称" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="duration" label="时长(分钟)" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="editService(scope.row)">编辑</el-button>
          <el-button type="danger" @click="deleteService(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showAddModal" :title="isEditing ? '编辑服务' : '添加服务'" width="500px">
      <el-form :model="serviceForm">
        <el-form-item label="服务名称">
          <el-input v-model="serviceForm.serviceName" />
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select v-model="serviceForm.serviceType">
            <el-option :value="1" label="美容洗护" />
            <el-option :value="2" label="医疗保健" />
            <el-option :value="3" label="寄养服务" />
            <el-option :value="4" label="宠物训练" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model.number="serviceForm.price" />
        </el-form-item>
        <el-form-item label="时长(分钟)">
          <el-input v-model.number="serviceForm.duration" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="serviceForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveService">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMerchantStore } from '../stores/merchant'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const services = ref([])
const showAddModal = ref(false)
const isEditing = ref(false)
const serviceForm = ref({
  serviceName: '',
  serviceType: 1,
  price: 0,
  duration: 0,
  description: ''
})

const merchantStore = useMerchantStore()
let currentStoreId = null

onMounted(async () => {
  await loadStoreId()
  loadServices()
})

async function loadStoreId() {
  try {
    const response = await axios.get(`/order/store/merchant/${merchantStore.merchantInfo?.id}`)
    if (response.code === 200 && response.data.length > 0) {
      currentStoreId = response.data[0].id
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadServices() {
  if (!currentStoreId) return
  try {
    const response = await axios.get(`/order/service/store/${currentStoreId}`)
    if (response.code === 200) {
      services.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

async function saveService() {
  if (!currentStoreId) {
    ElMessage.error('请先创建门店信息')
    return
  }
  serviceForm.value.storeId = currentStoreId
  try {
    let response
    if (isEditing.value) {
      response = await axios.put(`/order/service/${serviceForm.value.id}`, serviceForm.value)
      if (response.code === 200) {
        ElMessage.success('修改成功')
      }
    } else {
      response = await axios.post('/order/service', serviceForm.value)
      if (response.code === 200) {
        ElMessage.success('添加成功')
      }
    }
    showAddModal.value = false
    serviceForm.value = { serviceName: '', serviceType: 1, price: 0, duration: 0, description: '' }
    isEditing.value = false
    loadServices()
  } catch (error) {
    ElMessage.error(isEditing.value ? '修改失败' : '添加失败')
  }
}

function editService(service) {
  serviceForm.value = { ...service }
  isEditing.value = true
  showAddModal.value = true
}

async function deleteService(id) {
  try {
    const response = await axios.delete(`/order/service/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadServices()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.services-page {
  padding: 20px 0;
}

.add-btn {
  margin-bottom: 20px;
}
</style>