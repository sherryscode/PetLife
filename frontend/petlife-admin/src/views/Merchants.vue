<template>
  <div class="merchants-page">
    <h2>🏪 商户管理</h2>
    
    <div class="search-bar">
      <el-input v-model="searchKeyword" placeholder="搜索商户名称" />
      <el-button @click="searchMerchants">搜索</el-button>
    </div>

    <el-table :data="merchants" border style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="storeName" label="门店名称" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="storeAddress" label="地址" />
      <el-table-column prop="auditStatus" label="审核状态">
        <template #default="scope">
          <span :class="getAuditClass(scope.row.auditStatus)">
            {{ getAuditText(scope.row.auditStatus) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          {{ scope.row.status === 1 ? '正常' : '禁用' }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button v-if="scope.row.auditStatus === 0" @click="auditMerchant(scope.row, 1)">通过</el-button>
          <el-button v-if="scope.row.auditStatus === 0" type="danger" @click="auditMerchant(scope.row, 2)">驳回</el-button>
          <el-button @click="toggleStatus(scope.row)">
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const merchants = ref([])
const searchKeyword = ref('')

onMounted(() => {
  loadMerchants()
})

async function loadMerchants() {
  try {
    let url = '/auth/merchant/list?page=1&size=20'
    if (searchKeyword.value) {
      url += `&storeName=${searchKeyword.value}`
    }
    const response = await axios.get(url)
    if (response.code === 200) {
      merchants.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

async function searchMerchants() {
  loadMerchants()
}

function getAuditText(status) {
  const map = { 0: '待审核', 1: '通过', 2: '驳回' }
  return map[status] || '未知'
}

function getAuditClass(status) {
  const map = {
    0: 'audit-pending',
    1: 'audit-pass',
    2: 'audit-reject'
  }
  return map[status] || ''
}

async function auditMerchant(merchant, status) {
  try {
    const response = await axios.put(`/auth/merchant/${merchant.id}/audit`, { auditStatus: status })
    if (response.code === 200) {
      ElMessage.success(status === 1 ? '审核通过' : '已驳回')
      loadMerchants()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function toggleStatus(merchant) {
  try {
    const response = await axios.put(`/auth/merchant/${merchant.id}/status`, {
      status: merchant.status === 1 ? 0 : 1
    })
    if (response.code === 200) {
      ElMessage.success(merchant.status === 1 ? '已禁用' : '已启用')
      loadMerchants()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}
</script>

<style scoped>
.merchants-page {
  padding: 20px 0;
}

.search-bar {
  margin-bottom: 20px;
}

.audit-pending {
  color: #f39c12;
}

.audit-pass {
  color: #27ae60;
}

.audit-reject {
  color: #e74c3c;
}
</style>