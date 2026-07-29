<template>
  <div class="orders-page">
    <h2>📋 订单管理</h2>
    
    <div class="search-bar">
      <el-input v-model="searchKeyword" placeholder="搜索订单编号" />
      <el-button @click="searchOrders">搜索</el-button>
    </div>

    <el-table :data="orders" border style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="orderNo" label="订单编号" />
      <el-table-column prop="totalAmount" label="金额" />
      <el-table-column prop="orderStatus" label="状态">
        <template #default="scope">
          {{ getStatusText(scope.row.orderStatus) }}
        </template>
      </el-table-column>
      <el-table-column prop="payStatus" label="支付状态">
        <template #default="scope">
          {{ getPayStatusText(scope.row.payStatus) }}
        </template>
      </el-table-column>
      <el-table-column prop="shipStatus" label="发货状态">
        <template #default="scope">
          {{ getShipStatusText(scope.row.shipStatus) }}
        </template>
      </el-table-column>
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="viewOrder(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="orderDetailVisible" title="订单详情" width="800px">
      <div v-if="currentOrder" class="order-detail">
        <el-form label-width="120px">
          <el-form-item label="订单编号">
            <span>{{ currentOrder.orderNo }}</span>
          </el-form-item>
          <el-form-item label="订单状态">
            <span>{{ getStatusText(currentOrder.orderStatus) }}</span>
          </el-form-item>
          <el-form-item label="支付状态">
            <span>{{ getPayStatusText(currentOrder.payStatus) }}</span>
          </el-form-item>
          <el-form-item label="发货状态">
            <span>{{ getShipStatusText(currentOrder.shipStatus) }}</span>
          </el-form-item>
          <el-form-item label="用户ID">
            <span>{{ currentOrder.userId }}</span>
          </el-form-item>
          <el-form-item label="订单金额">
            <span>{{ currentOrder.totalAmount }}</span>
          </el-form-item>
          <el-form-item label="收货人姓名">
            <span>{{ currentOrder.receiverName }}</span>
          </el-form-item>
          <el-form-item label="收货人电话">
            <span>{{ currentOrder.receiverPhone }}</span>
          </el-form-item>
          <el-form-item label="收货地址">
            <span>{{ currentOrder.receiverAddress }}</span>
          </el-form-item>
          <el-form-item label="订单备注">
            <span>{{ currentOrder.remark || '无' }}</span>
          </el-form-item>
          <el-form-item label="创建时间">
            <span>{{ currentOrder.createdAt }}</span>
          </el-form-item>
          <el-form-item label="更新时间">
            <span>{{ currentOrder.updatedAt }}</span>
          </el-form-item>
        </el-form>

        <div style="margin-top: 20px;">
          <h4>商品明细</h4>
          <el-table :data="orderItems" border style="width: 100%">
            <el-table-column prop="goodsName" label="商品名称" />
            <el-table-column prop="price" label="单价" />
            <el-table-column prop="quantity" label="数量" />
            <el-table-column label="小计">
              <template #default="scope">
                {{ (scope.row.price * scope.row.quantity).toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const orders = ref([])
const searchKeyword = ref('')

const orderDetailVisible = ref(false)
const currentOrder = ref(null)
const orderItems = ref([])

onMounted(() => {
  loadOrders()
})

async function loadOrders() {
  try {
    let url = '/mall/order/list?page=1&size=20'
    if (searchKeyword.value) {
      url += `&orderNo=${searchKeyword.value}`
    }
    const response = await axios.get(url)
    if (response.code === 200) {
      orders.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

async function searchOrders() {
  loadOrders()
}

function getStatusText(status) {
  const map = { 0: '待接单', 1: '已接单', 2: '服务中', 3: '已完成', 4: '已取消' }
  return map[status] || '未知'
}

function getPayStatusText(status) {
  const map = { 0: '未支付', 1: '已支付' }
  return map[status] || '未知'
}

function getShipStatusText(status) {
  const map = { 0: '未发货', 1: '已发货', 2: '已收货' }
  return map[status] || '未知'
}

async function viewOrder(order) {
  try {
    const response = await axios.get(`/mall/order/${order.id}`)
    if (response.code === 200) {
      currentOrder.value = response.data
      
      const itemsResponse = await axios.get(`/mall/order/${order.id}/items`)
      if (itemsResponse.code === 200) {
        orderItems.value = itemsResponse.data
      } else {
        orderItems.value = []
      }
      
      orderDetailVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取订单详情失败')
  }
}
</script>

<style scoped>
.orders-page {
  padding: 20px 0;
}

.search-bar {
  margin-bottom: 20px;
}

.order-detail {
  padding: 10px;
}
</style>