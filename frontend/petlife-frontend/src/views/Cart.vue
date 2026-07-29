<template>
  <div class="cart-page">
    <div class="container">
      <h2>🛒 购物车</h2>
      
      <div v-if="cartItems.length === 0" class="empty-cart">
        <div class="empty-icon">🛒</div>
        <p>购物车是空的</p>
        <el-button type="primary" @click="$router.push('/mall')">去购物</el-button>
      </div>

      <div v-else>
        <el-table :data="cartItems" border style="width: 100%">
          <el-table-column prop="selected" label="选择" width="80">
            <template #default="scope">
              <el-checkbox v-model="scope.row.selected" @change="updateSelected(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column prop="goodsName" label="商品名称" width="200" />
          <el-table-column label="商品图片" width="100">
            <template #default="scope">
              <img :src="scope.row.goods?.images ? scope.row.goods.images.split(',')[0] : 'https://via.placeholder.com/80'" width="60" height="60" />
            </template>
          </el-table-column>
          <el-table-column prop="price" label="单价" />
          <el-table-column label="数量" width="150">
            <template #default="scope">
              <el-input-number 
                :model-value="scope.row.quantity" 
                @update:model-value="updateQuantity(scope.row, $event)"
                :min="1" 
                :max="99"
              />
            </template>
          </el-table-column>
          <el-table-column label="小计">
            <template #default="scope">
              ¥{{ (scope.row.goods?.price || 0) * scope.row.quantity }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="danger" @click="removeItem(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="cart-footer">
          <div class="select-all">
            <el-checkbox :model-value="isAllSelected" @change="toggleSelectAll">全选</el-checkbox>
          </div>
          <div class="total-info">
            <span>已选 {{ selectedCount }} 件商品</span>
            <span class="total-price">合计：¥{{ totalPrice }}</span>
          </div>
          <el-button type="primary" size="large" @click="checkout">结算</el-button>
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

const cartItems = ref([])
const userStore = useUserStore()

onMounted(() => {
  loadCart()
})

async function loadCart() {
  if (!userStore.userInfo) return
  
  try {
    const response = await axios.get(`/mall/cart/user/${userStore.userInfo.id}`)
    if (response.code === 200) {
      cartItems.value = response.data.map(item => ({
        ...item,
        selected: item.selected || true
      }))
      await loadGoodsInfo()
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadGoodsInfo() {
  const goodsIds = [...new Set(cartItems.value.map(item => item.goodsId))]
  for (const item of cartItems.value) {
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

const isAllSelected = computed(() => {
  return cartItems.value.length > 0 && cartItems.value.every(item => item.selected)
})

const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.selected).reduce((sum, item) => sum + item.quantity, 0)
})

const totalPrice = computed(() => {
  return cartItems.value.filter(item => item.selected).reduce((sum, item) => {
    return sum + (item.goods?.price || 0) * item.quantity
  }, 0).toFixed(2)
})

async function updateQuantity(item, quantity) {
  item.quantity = quantity
  try {
    await axios.put('/mall/cart', {
      userId: userStore.userInfo.id,
      goodsId: item.goodsId,
      quantity: quantity
    })
  } catch (error) {
    console.error(error)
  }
}

async function updateSelected(item) {
  try {
    await axios.put('/mall/cart/select', {
      userId: userStore.userInfo.id,
      goodsId: item.goodsId,
      selected: item.selected ? 1 : 0
    })
  } catch (error) {
    console.error(error)
  }
}

function toggleSelectAll(value) {
  cartItems.value.forEach(item => {
    item.selected = value
    updateSelected(item)
  })
}

async function removeItem(item) {
  try {
    const response = await axios.delete(`/mall/cart?userId=${userStore.userInfo.id}&goodsId=${item.goodsId}`)
    if (response.code === 200) {
      cartItems.value = cartItems.value.filter(i => i.id !== item.id)
      ElMessage.success('删除成功')
    }
  } catch (error) {
    ElMessage.error('删除失败')
    console.error(error)
  }
}

function checkout() {
  const selectedItems = cartItems.value.filter(item => item.selected)
  if (selectedItems.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  window.location.href = '/checkout'
}
</script>

<style scoped>
.cart-page {
  padding: 40px 0;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.empty-cart {
  text-align: center;
  padding: 80px 0;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
}

.empty-cart p {
  font-size: 18px;
  color: #666;
  margin-bottom: 20px;
}

.cart-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
}

.select-all {
  font-size: 16px;
}

.total-info {
  display: flex;
  gap: 24px;
  font-size: 16px;
}

.total-price {
  font-size: 24px;
  color: #e74c3c;
  font-weight: bold;
}
</style>