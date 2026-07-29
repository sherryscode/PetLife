<template>
  <div class="order-page">
    <div class="container">
      <h2>📋 我的订单</h2>
      
      <div class="tabs">
        <el-button v-for="tab in tabs" :key="tab.value"
          :class="{ active: activeTab === tab.value }"
          @click="activeTab = tab.value">
          {{ tab.label }}
        </el-button>
      </div>

      <div v-if="orders.length === 0" class="empty-orders">
        <div class="empty-icon">📦</div>
        <p>暂无订单</p>
        <el-button type="primary" @click="$router.push('/mall')">去购物</el-button>
      </div>

      <div v-else class="orders-list">
        <div class="order-card" v-for="order in orders" :key="order.id">
          <div class="order-header">
            <div class="order-no">订单编号：{{ order.orderNo }}</div>
            <div class="order-status" :class="getStatusClass(order.orderStatus)">
              {{ getStatusText(order.orderStatus) }}
            </div>
          </div>
          
          <div class="order-items">
            <div class="order-item" v-for="item in getOrderItems(order.id)" :key="item.id">
              <img :src="getGoodsImage(item.goodsId)" class="item-image" />
              <div class="item-info">
                <div class="item-name">{{ item.goodsName }}</div>
                <div class="item-price">¥{{ item.price }} x {{ item.quantity }}</div>
              </div>
              <div class="item-total">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
            </div>
            <div v-if="order.orderType === 'reserve' && getOrderItems(order.id).length === 0" class="reserve-info">
              <span>预约服务订单</span>
            </div>
          </div>
          
          <div class="order-footer">
            <div class="order-total">合计：¥{{ order.totalAmount }}</div>
            <div class="order-actions">
              <el-button v-if="order.orderType === 'mall' && order.payStatus === 0" type="primary" @click="payOrder(order)">去支付</el-button>
              <el-button v-if="order.orderType === 'mall' && order.orderStatus === 2" type="success" @click="confirmOrder(order)">确认收货</el-button>
              <el-button v-if="order.orderStatus === 0" type="danger" @click="cancelOrder(order)">取消订单</el-button>
              <el-button @click="viewOrder(order)">查看详情</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="showDetail" title="订单详情" width="700px">
      <div v-if="currentOrder" class="order-detail">
        <div class="detail-section">
          <h4>基本信息</h4>
          <div class="detail-row">
            <span class="label">订单编号：</span>
            <span>{{ currentOrder.orderNo }}</span>
          </div>
          <div class="detail-row">
            <span class="label">订单类型：</span>
            <span>{{ currentOrder.orderType === 'mall' ? '商城订单' : '预约订单' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">订单状态：</span>
            <span :class="getStatusClass(currentOrder.orderStatus)">{{ getStatusText(currentOrder.orderStatus) }}</span>
          </div>
          <div class="detail-row">
            <span class="label">支付状态：</span>
            <span>{{ currentOrder.payStatus === 0 ? '待支付' : '已支付' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">创建时间：</span>
            <span>{{ formatDate(currentOrder.createdAt) }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4>收货信息</h4>
          <div class="detail-row">
            <span class="label">收货人：</span>
            <span>{{ currentOrder.receiverName || '-' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">联系电话：</span>
            <span>{{ currentOrder.receiverPhone || '-' }}</span>
          </div>
          <div class="detail-row">
            <span class="label">收货地址：</span>
            <span>{{ currentOrder.receiverAddress || '-' }}</span>
          </div>
        </div>

        <div class="detail-section">
          <h4>商品清单</h4>
          <div v-if="currentOrderItems.length > 0">
            <el-table :data="currentOrderItems" border style="width: 100%">
              <el-table-column label="商品图片" width="100">
                <template #default="scope">
                  <img :src="getGoodsImage(scope.row.goodsId)" width="60" height="60" style="border-radius: 8px" />
                </template>
              </el-table-column>
              <el-table-column prop="goodsName" label="商品名称" />
              <el-table-column label="单价" width="100">
                <template #default="scope">
                  ¥{{ scope.row.price }}
                </template>
              </el-table-column>
              <el-table-column prop="quantity" label="数量" width="80" />
              <el-table-column label="小计" width="100">
                <template #default="scope">
                  ¥{{ (scope.row.price * scope.row.quantity).toFixed(2) }}
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="no-items">
            <p>{{ currentOrder.orderType === 'reserve' ? '预约服务订单，无商品明细' : '暂无商品明细' }}</p>
          </div>
        </div>

        <div class="detail-section summary">
          <h4>金额汇总</h4>
          <div class="detail-row">
            <span class="label">商品总价：</span>
            <span>¥{{ currentOrder.totalAmount }}</span>
          </div>
          <div class="detail-row total">
            <span class="label">应付金额：</span>
            <span>¥{{ currentOrder.totalAmount }}</span>
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="showDetail = false">关闭</el-button>
        <el-button v-if="currentOrder?.orderStatus === 0" type="danger" @click="cancelOrder(currentOrder)">取消订单</el-button>
        <el-button v-if="currentOrder?.orderType === 'mall' && currentOrder?.payStatus === 0" type="primary" @click="payOrder(currentOrder)">去支付</el-button>
        <el-button v-if="currentOrder?.orderType === 'mall' && currentOrder?.orderStatus === 2" type="success" @click="confirmOrder(currentOrder)">确认收货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useUserStore } from '../stores/user'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const tabs = [
  { label: '全部', value: -1 },
  { label: '待付款', value: 0 },
  { label: '待发货', value: 1 },
  { label: '待收货', value: 2 },
  { label: '已完成', value: 3 }
]

const activeTab = ref(-1)
const orders = ref([])
const orderItems = ref([])
const goodsMap = ref({})
const userStore = useUserStore()

const showDetail = ref(false)
const currentOrder = ref(null)
const currentOrderItems = ref([])

onMounted(() => {
  loadOrders()
})

watch(activeTab, () => {
  loadOrders()
})

async function loadOrders() {
  if (!userStore.userInfo) return
  
  try {
    const [mallRes, reserveRes] = await Promise.all([
      axios.get(`/mall/order/list?userId=${userStore.userInfo.id}&page=1&size=20`),
      axios.get(`/order/order/list?userId=${userStore.userInfo.id}&page=1&size=20`)
    ])
    
    let allOrders = []
    
    if (mallRes.code === 200) {
      allOrders = [...allOrders, ...mallRes.data.records.map(o => ({ ...o, orderType: 'mall' }))]
    }
    
    if (reserveRes.code === 200) {
      allOrders = [...allOrders, ...reserveRes.data.records.map(o => ({ ...o, orderType: 'reserve' }))]
    }
    
    allOrders.sort((a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0))
    
    if (activeTab.value !== -1) {
      allOrders = allOrders.filter(o => o.orderStatus === activeTab.value)
    }
    
    orders.value = allOrders
    await loadOrderItems()
  } catch (error) {
    console.error(error)
  }
}

async function loadOrderItems() {
  orderItems.value = []
  goodsMap.value = {}
  
  for (const order of orders.value) {
    if (order.orderType === 'mall') {
      try {
        const response = await axios.get(`/mall/order/${order.id}/items`)
        if (response.code === 200) {
          const items = response.data
          orderItems.value = [...orderItems.value, ...items]
          
          for (const item of items) {
            if (!goodsMap.value[item.goodsId]) {
              try {
                const goodsRes = await axios.get(`/mall/goods/${item.goodsId}`)
                if (goodsRes.code === 200) {
                  goodsMap.value[item.goodsId] = goodsRes.data
                }
              } catch (error) {
                console.error(error)
              }
            }
          }
        }
      } catch (error) {
        console.error(error)
      }
    }
  }
}

function getOrderItems(orderId) {
  return orderItems.value.filter(item => item.orderId === orderId)
}

function getGoodsImage(goodsId) {
  if (goodsMap.value[goodsId]) {
    return goodsMap.value[goodsId].images?.split(',')[0] || 'https://via.placeholder.com/80'
  }
  return 'https://via.placeholder.com/80'
}

function getStatusText(status) {
  const statusMap = {
    0: '待付款',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

function getStatusClass(status) {
  const classMap = {
    0: 'status-pending',
    1: 'status-processing',
    2: 'status-shipped',
    3: 'status-completed',
    4: 'status-cancelled'
  }
  return classMap[status] || ''
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

async function payOrder(order) {
  try {
    const response = await axios.put(`/mall/order/${order.id}/pay`)
    if (response.code === 200) {
      ElMessage.success('支付成功！')
      showDetail.value = false
      loadOrders()
    }
  } catch (error) {
    ElMessage.error('支付失败')
    console.error(error)
  }
}

async function confirmOrder(order) {
  try {
    const response = await axios.put(`/mall/order/${order.id}/confirm`)
    if (response.code === 200) {
      ElMessage.success('已确认收货')
      showDetail.value = false
      loadOrders()
    }
  } catch (error) {
    ElMessage.error('确认收货失败')
    console.error(error)
  }
}

async function cancelOrder(order) {
  try {
    const response = await axios.put(`/mall/order/${order.id}/cancel`)
    if (response.code === 200) {
      ElMessage.success('订单已取消')
      showDetail.value = false
      loadOrders()
    }
  } catch (error) {
    ElMessage.error('取消订单失败')
    console.error(error)
  }
}

function viewOrder(order) {
  currentOrder.value = order
  currentOrderItems.value = getOrderItems(order.id)
  showDetail.value = true
}
</script>

<style scoped>
.order-page {
  padding: 40px 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.tabs {
  margin-bottom: 24px;
}

.tabs .el-button {
  margin-right: 12px;
}

.tabs .el-button.active {
  background: #667eea;
  color: white;
}

.empty-orders {
  text-align: center;
  padding: 80px 0;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
}

.empty-orders p {
  font-size: 18px;
  color: #666;
  margin-bottom: 20px;
}

.orders-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.order-card {
  background: #f8f9fa;
  border-radius: 12px;
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.order-no {
  font-size: 14px;
  color: #666;
}

.order-status {
  font-size: 14px;
  font-weight: bold;
}

.status-pending {
  color: #e74c3c;
}

.status-processing {
  color: #f39c12;
}

.status-shipped {
  color: #3498db;
}

.status-completed {
  color: #27ae60;
}

.status-cancelled {
  color: #95a5a6;
}

.order-items {
  padding: 16px 20px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.reserve-info {
  padding: 12px 0;
  color: #666;
}

.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
}

.item-info {
  flex: 1;
}

.item-name {
  font-size: 16px;
  margin-bottom: 8px;
}

.item-price {
  font-size: 14px;
  color: #666;
}

.item-total {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fff;
  border-top: 1px solid #eee;
}

.order-total {
  font-size: 18px;
  font-weight: bold;
  color: #e74c3c;
}

.order-actions {
  display: flex;
  gap: 12px;
}

.order-detail {
  padding: 8px 0;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section h4 {
  font-size: 16px;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.detail-row {
  display: flex;
  padding: 8px 0;
  font-size: 14px;
}

.detail-row .label {
  width: 100px;
  color: #666;
  flex-shrink: 0;
}

.detail-row.total {
  font-size: 18px;
  font-weight: bold;
  color: #e74c3c;
  border-top: 1px solid #eee;
  margin-top: 8px;
  padding-top: 16px;
}

.detail-section.summary {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
}

.no-items {
  text-align: center;
  padding: 24px;
  color: #999;
}
</style>
