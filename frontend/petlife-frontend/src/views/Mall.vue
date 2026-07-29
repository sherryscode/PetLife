<template>
  <div class="mall-page">
    <div class="container">
      <h2>🛒 宠物商城</h2>
      
      <div class="category-tabs">
        <el-button v-for="cat in categories" :key="cat.value" 
          :class="{ active: currentCategory === cat.value }"
          @click="currentCategory = cat.value">
          {{ cat.label }}
        </el-button>
      </div>

      <div class="goods-grid">
        <div class="goods-card" v-for="goods in goodsList" :key="goods.id">
          <div class="goods-image">
            <img :src="goods.images ? goods.images.split(',')[0] : 'https://via.placeholder.com/200'" alt="" />
          </div>
          <h3>{{ goods.goodsName }}</h3>
          <p>¥{{ goods.price }}</p>
          <el-button @click="addToCart(goods)">加入购物车</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const categories = [
  { label: '全部', value: 0 },
  { label: '粮食', value: 1 },
  { label: '玩具', value: 2 },
  { label: '洗护', value: 3 },
  { label: '药品', value: 4 }
]

const currentCategory = ref(0)
const goodsList = ref([])
const userStore = useUserStore()

onMounted(() => {
  loadGoods()
})

watch(currentCategory, () => {
  loadGoods()
})

async function loadGoods() {
  try {
    let url = '/mall/goods/list?page=1&size=20&auditStatus=1'
    if (currentCategory.value > 0) {
      url += `&category=${currentCategory.value}`
    }
    const response = await axios.get(url)
    if (response.code === 200) {
      goodsList.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

async function addToCart(goods) {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const response = await axios.post('/mall/cart', {
      userId: userStore.userInfo.id,
      goodsId: goods.id,
      quantity: 1
    })
    if (response.code === 200) {
      ElMessage.success('添加成功')
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}
</script>

<style scoped>
.mall-page {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.category-tabs {
  margin-bottom: 32px;
}

.category-tabs .el-button {
  margin-right: 12px;
}

.category-tabs .el-button.active {
  background: #667eea;
  color: white;
}

.goods-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.goods-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
}

.goods-image img {
  width: 150px;
  height: 150px;
  object-fit: cover;
  margin-bottom: 12px;
}

.goods-card h3 {
  font-size: 16px;
  margin-bottom: 8px;
}

.goods-card p {
  font-size: 20px;
  color: #e74c3c;
  margin-bottom: 12px;
}
</style>