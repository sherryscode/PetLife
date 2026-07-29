<template>
  <div class="store-page">
    <h2>🏠 门店信息</h2>
    
    <el-form :model="storeForm" class="store-form">
      <el-form-item label="门店名称">
        <el-input v-model="storeForm.storeName" />
      </el-form-item>
      <el-form-item label="门店地址">
        <el-input v-model="storeForm.storeAddress" />
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="storeForm.storePhone" />
      </el-form-item>
      <el-form-item label="营业时间">
        <el-input v-model="storeForm.businessHours" />
      </el-form-item>
      <el-form-item label="经营范围">
        <el-input v-model="storeForm.businessScope" type="textarea" :rows="5" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveStore">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMerchantStore } from '../stores/merchant'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const storeForm = ref({
  storeName: '',
  storeAddress: '',
  storePhone: '',
  businessHours: '',
  businessScope: ''
})

const merchantStore = useMerchantStore()

onMounted(() => {
  loadStore()
})

let currentStoreId = null

async function loadStore() {
  try {
    const response = await axios.get(`/order/store/merchant/${merchantStore.merchantInfo?.id}`)
    if (response.code === 200 && response.data.length > 0) {
      const store = response.data[0]
      currentStoreId = store.id
      storeForm.value = {
          storeName: store.storeName || '',
          storeAddress: store.storeAddress || '',
          storePhone: store.storePhone || '',
          businessHours: store.businessHours || '',
          businessScope: store.businessScope || ''
        }
    }
  } catch (error) {
    console.error(error)
  }
}

async function saveStore() {
  try {
    let response
    if (currentStoreId) {
      response = await axios.put(`/order/store/${currentStoreId}`, {
        ...storeForm.value,
        merchantId: merchantStore.merchantInfo?.id
      })
    } else {
      response = await axios.post('/order/store', {
        ...storeForm.value,
        merchantId: merchantStore.merchantInfo?.id,
        status: 1
      })
    }
    if (response.code === 200) {
      ElMessage.success('保存成功')
      if (!currentStoreId) {
        currentStoreId = response.data.id
      }
    }
  } catch (error) {
    ElMessage.error('保存失败')
  }
}
</script>

<style scoped>
.store-page {
  padding: 20px 0;
}

.store-form {
  max-width: 600px;
}
</style>