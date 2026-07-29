<template>
  <div class="orders-page">
    <h2>📋 订单管理</h2>
    
    <div class="order-type-tabs">
      <el-button v-for="type in orderTypes" :key="type.value"
        :class="{ active: orderType === type.value }"
        @click="orderType = type.value">
        {{ type.label }}
      </el-button>
    </div>

    <div class="tabs">
      <el-button v-for="tab in tabs" :key="tab.value"
        :class="{ active: activeTab === tab.value }"
        @click="activeTab = tab.value">
        {{ tab.label }}
      </el-button>
    </div>

    <el-table :data="orders" border style="width: 100%">
      <el-table-column prop="orderNo" label="订单编号" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column prop="orderStatus" label="状态">
        <template #default="scope">
          {{ getStatusText(scope.row.orderStatus) }}
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button v-if="orderType === 'booking' && scope.row.orderStatus === 0" @click="acceptOrder(scope.row)">接单</el-button>
          <el-button v-if="orderType === 'booking' && scope.row.orderStatus === 1" @click="completeOrder(scope.row)">完成</el-button>
          <el-button v-if="orderType === 'mall' && scope.row.orderStatus === 1" type="primary" @click="shipOrder(scope.row)">发货</el-button>
          <el-button @click="viewOrder(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showDetailModal" title="订单详情" width="600px">
      <div v-if="currentOrder" class="order-detail">
        <div class="detail-section">
          <h4>基本信息</h4>
          <div class="detail-row">
            <span class="label">订单编号：</span>
            <span>{{ currentOrder.orderNo }}</span>
          </div>
          <div class="detail-row">
            <span class="label">订单状态：</span>
            <span>{{ getStatusText(currentOrder.orderStatus) }}</span>
          </div>
          <div class="detail-row">
            <span class="label">支付状态：</span>
            <span>{{ currentOrder.payStatus === 0 ? '未支付' : '已支付' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">创建时间：</span>
            <span>{{ currentOrder.createdAt }}</span>
          </div>
        </div>

        <div v-if="orderType === 'booking'" class="detail-section">
          <h4>预约信息</h4>
          <div class="detail-row">
            <span class="label">预约日期：</span>
            <span>{{ currentOrder.reserveDate }}</span>
          </div>
          <div class="detail-row">
            <span class="label">预约时间：</span>
            <span>{{ currentOrder.reserveTime }}</span>
          </div>
          <div class="detail-row">
            <span class="label">宠物描述：</span>
            <span>{{ currentOrder.petDesc || '-' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">备注：</span>
            <span>{{ currentOrder.remark || '-' }}</span>
          </div>
        </div>

        <div v-if="orderType === 'mall'" class="detail-section">
          <h4>收货信息</h4>
          <div class="detail-row">
            <span class="label">收货人：</span>
            <span>{{ currentOrder.receiverName }}</span>
          </div>
          <div class="detail-row">
            <span class="label">联系电话：</span>
            <span>{{ currentOrder.receiverPhone }}</span>
          </div>
          <div class="detail-row">
            <span class="label">收货地址：</span>
            <span>{{ currentOrder.receiverAddress }}</span>
          </div>
          <div class="detail-row">
            <span class="label">备注：</span>
            <span>{{ currentOrder.remark || '-' }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4>金额信息</h4>
          <div class="detail-row">
            <span class="label">订单金额：</span>
            <span class="amount">¥{{ currentOrder.totalAmount }}</span>
          </div>
          <div v-if="orderType === 'booking'" class="detail-row">
            <span class="label">定金金额：</span>
            <span>¥{{ currentOrder.depositAmount || 0 }}</span>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetailModal = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useMerchantStore } from '../stores/merchant'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const orderTypes = [
  { label: '预约订单', value: 'booking' },
  { label: '商城订单', value: 'mall' }
]

const bookingTabs = [
  { label: '全部', value: -1 },
  { label: '待接单', value: 0 },
  { label: '已接单', value: 1 },
  { label: '服务中', value: 2 },
  { label: '已完成', value: 3 }
]

const mallTabs = [
  { label: '全部', value: -1 },
  { label: '待付款', value: 0 },
  { label: '代发货', value: 1 },
  { label: '待收货', value: 2 },
  { label: '已完成', value: 3 }
]

const orderType = ref('booking')
const activeTab = ref(-1)
const orders = ref([])
const merchantStore = useMerchantStore()
const showDetailModal = ref(false)
const currentOrder = ref(null)

const tabs = computed(() => {
  return orderType.value === 'booking' ? bookingTabs : mallTabs
})

onMounted(() => {
  loadOrders()
})

watch([orderType, activeTab], () => {
  loadOrders()
})

async function loadOrders() {
  try {
    let url = ''
    if (orderType.value === 'booking') {
      url = `/order/order/list?merchantId=${merchantStore.merchantInfo?.id}&page=1&size=20`
    } else {
      url = `/mall/order/merchant/list?merchantId=${merchantStore.merchantInfo?.id}&page=1&size=20`
    }
    if (activeTab.value !== -1) {
      url += `&orderStatus=${activeTab.value}`
    }
    const response = await axios.get(url)
    if (response.code === 200) {
      orders.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

function getStatusText(status) {
  if (orderType.value === 'booking') {
    const map = { 0: '待接单', 1: '已接单', 2: '服务中', 3: '已完成', 4: '已取消' }
    return map[status] || '未知'
  } else {
    const map = { 0: '待付款', 1: '代发货', 2: '待收货', 3: '已完成', 4: '已取消' }
    return map[status] || '未知'
  }
}

async function acceptOrder(order) {
  try {
    const response = await axios.put(`/order/order/${order.id}/status`, { orderStatus: 1 })
    if (response.code === 200) {
      ElMessage.success('接单成功')
      loadOrders()
    }
  } catch (error) {
    ElMessage.error('接单失败')
  }
}

async function completeOrder(order) {
  try {
    const response = await axios.put(`/order/order/${order.id}/status`, { orderStatus: 3 })
    if (response.code === 200) {
      ElMessage.success('订单完成')
      loadOrders()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function shipOrder(order) {
  try {
    const response = await axios.put(`/mall/order/${order.id}/ship`)
    if (response.code === 200) {
      ElMessage.success('发货成功')
      loadOrders()
    }
  } catch (error) {
    ElMessage.error('发货失败')
  }
}

function viewOrder(order) {
  currentOrder.value = order
  showDetailModal.value = true
}
</script>

<style scoped>
.orders-page {
  padding: 20px 0;
}

.order-type-tabs {
  margin-bottom: 16px;
}

.order-type-tabs .el-button {
  margin-right: 12px;
}

.order-type-tabs .el-button.active {
  background: #764ba2;
  color: white;
}

.tabs {
  margin-bottom: 20px;
}

.tabs .el-button {
  margin-right: 12px;
}

.tabs .el-button.active {
  background: #667eea;
  color: white;
}
</style>