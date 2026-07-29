<template>
  <div class="checkout-page">
    <div class="container">
      <h2>📋 确认订单</h2>
      
      <div class="order-info">
        <div class="section">
          <h3>收货信息</h3>
          <el-form :model="addressForm">
            <el-form-item label="收货人">
              <el-input v-model="addressForm.name" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="addressForm.phone" />
            </el-form-item>
            <el-form-item label="收货地址">
              <el-input v-model="addressForm.address" type="textarea" />
            </el-form-item>
          </el-form>
        </div>

        <div class="section">
          <h3>商品清单</h3>
          <el-table :data="orderItems" border style="width: 100%">
            <el-table-column label="商品图片" width="100">
              <template #default="scope">
                <img :src="scope.row.goods?.images ? scope.row.goods.images.split(',')[0] : 'https://via.placeholder.com/80'" width="60" height="60" />
              </template>
            </el-table-column>
            <el-table-column label="商品名称">
              <template #default="scope">
                {{ scope.row.goods?.goodsName || '' }}
              </template>
            </el-table-column>
            <el-table-column label="单价">
              <template #default="scope">
                ¥{{ scope.row.goods?.price || 0 }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" />
            <el-table-column label="小计">
              <template #default="scope">
                ¥{{ (scope.row.goods?.price || 0) * scope.row.quantity }}
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="section summary">
          <h3>订单汇总</h3>
          <div class="summary-item">
            <span>商品数量：</span>
            <span>{{ itemCount }} 件</span>
          </div>
          <div class="summary-item">
            <span>商品总价：</span>
            <span>¥{{ totalAmount }}</span>
          </div>
          <div class="summary-item">
            <span>运费：</span>
            <span>¥{{ shippingFee }}</span>
          </div>
          <div class="summary-item total">
            <span>应付金额：</span>
            <span>¥{{ finalAmount }}</span>
          </div>
        </div>

        <div class="checkout-actions">
          <el-button @click="$router.push('/cart')">返回购物车</el-button>
          <el-button type="primary" size="large" :loading="isSubmitting" @click="submitOrder">提交订单</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from '../utils/axios'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const orderItems = ref([])
const isSubmitting = ref(false)

const addressForm = ref({
  name: '',
  phone: '',
  address: ''
})

const shippingFee = computed(() => {
  const total = parseFloat(totalAmount.value)
  return total >= 99 ? '0.00' : '10.00'
})

const totalAmount = computed(() => {
  return orderItems.value.reduce((sum, item) => {
    return sum + (item.goods?.price || 0) * item.quantity
  }, 0).toFixed(2)
})

const finalAmount = computed(() => {
  return (parseFloat(totalAmount.value) + parseFloat(shippingFee.value)).toFixed(2)
})

const itemCount = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + item.quantity, 0)
})

onMounted(() => {
  loadCartItems()
})

async function loadCartItems() {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    const response = await axios.get(`/mall/cart/user/${userStore.userInfo.id}`)
    if (response.code === 200) {
      const items = response.data.filter(item => item.selected)
      if (items.length === 0) {
        ElMessage.warning('请选择要结算的商品')
        return
      }
      orderItems.value = items
      await loadGoodsInfo()
      
      if (userStore.userInfo.nickname) {
        addressForm.value.name = userStore.userInfo.nickname
      }
      if (userStore.userInfo.phone) {
        addressForm.value.phone = userStore.userInfo.phone
      }
      if (userStore.userInfo.address) {
        addressForm.value.address = userStore.userInfo.address
      }
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadGoodsInfo() {
  for (const item of orderItems.value) {
    try {
      const response = await axios.get(`/mall/goods/${item.goodsId}`)
      if (response.code === 200) {
        item.goods = response.data
      }
    } catch (error) {
      console.error(error)
    }
  }
}

async function submitOrder() {
  if (!addressForm.value.name || !addressForm.value.phone || !addressForm.value.address) {
    ElMessage.warning('请填写完整收货信息')
    return
  }
  
  if (orderItems.value.length === 0) {
    ElMessage.warning('没有选中的商品')
    return
  }
  
  isSubmitting.value = true
  
  try {
    const order = {
      userId: userStore.userInfo.id,
      totalAmount: finalAmount.value,
      payStatus: 0,
      orderStatus: 0,
      shipStatus: 0,
      receiverName: addressForm.value.name,
      receiverPhone: addressForm.value.phone,
      receiverAddress: addressForm.value.address
    }
    
    const response = await axios.post('/mall/order', order)
    
    if (response.code === 200) {
      for (const item of orderItems.value) {
        try {
          await axios.post('/mall/order/item', {
            orderId: response.data.id,
            goodsId: item.goodsId,
            goodsName: item.goods?.goodsName || '',
            price: item.goods?.price || 0,
            quantity: item.quantity
          })
        } catch (error) {
          console.error(error)
        }
      }
      
      for (const item of orderItems.value) {
        try {
          await axios.delete(`/mall/cart?userId=${userStore.userInfo.id}&goodsId=${item.goodsId}`)
        } catch (error) {
          console.error(error)
        }
      }
      
      ElMessage.success('订单提交成功！')
      setTimeout(() => {
        window.location.href = '/order'
      }, 1500)
    }
  } catch (error) {
    ElMessage.error('订单提交失败')
    console.error(error)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
.checkout-page {
  padding: 40px 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.order-info {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
}

.section {
  margin-bottom: 32px;
}

.section h3 {
  margin-bottom: 16px;
  font-size: 18px;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 1px solid #ddd;
}

.summary {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  font-size: 16px;
}

.summary-item.total {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
  border-top: 1px solid #ddd;
  margin-top: 8px;
  padding-top: 16px;
}

.checkout-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
}
</style>