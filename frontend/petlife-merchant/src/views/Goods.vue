<template>
  <div class="goods-page">
    <h2>🛒 商品管理</h2>
    <el-button type="primary" @click="showAddModal = true" class="add-btn">添加商品</el-button>
    
    <el-table :data="goodsList" border style="width: 100%">
      <el-table-column prop="goodsName" label="商品名称" />
      <el-table-column prop="category" label="分类">
        <template #default="scope">
          {{ getCategoryText(scope.row.category) }}
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="stock" label="库存" />
      <el-table-column prop="sales" label="销量" />
      <el-table-column prop="auditStatus" label="审核状态">
        <template #default="scope">
          {{ getAuditStatusText(scope.row.auditStatus) }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="editGoods(scope.row)">编辑</el-button>
          <el-button type="danger" @click="deleteGoods(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showAddModal" :title="isEditing ? '编辑商品' : '添加商品'" width="600px">
      <el-form :model="goodsForm">
        <el-form-item label="商品图片">
          <div class="goods-images">
            <div v-for="(img, index) in goodsForm.images" :key="index" class="image-item">
              <img :src="img" class="image-preview" />
              <el-button size="small" type="danger" @click="removeGoodsImage(index)">删除</el-button>
            </div>
            <el-upload
              class="upload-btn"
              action="/api/files/upload"
              :show-file-list="false"
              :on-success="handleGoodsImageUpload"
              accept="image/*"
            >
              <div class="upload-placeholder">
                <span>+ 添加图片</span>
              </div>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="goodsForm.goodsName" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="goodsForm.category">
            <el-option :value="1" label="粮食" />
            <el-option :value="2" label="玩具" />
            <el-option :value="3" label="洗护" />
            <el-option :value="4" label="药品" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model.number="goodsForm.price" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model.number="goodsForm.stock" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="goodsForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="saveGoods">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMerchantStore } from '../stores/merchant'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const goodsList = ref([])
const showAddModal = ref(false)
const isEditing = ref(false)
const goodsForm = ref({
  goodsName: '',
  category: 1,
  price: 0,
  stock: 0,
  description: '',
  images: []
})

const merchantStore = useMerchantStore()

onMounted(() => {
  loadGoods()
})

async function loadGoods() {
  try {
    const response = await axios.get(`/mall/goods/list?merchantId=${merchantStore.merchantInfo?.id}&page=1&size=20`)
    if (response.code === 200) {
      goodsList.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

function getCategoryText(category) {
  const map = { 1: '粮食', 2: '玩具', 3: '洗护', 4: '药品' }
  return map[category] || '未知'
}

function getAuditStatusText(status) {
  const map = { 0: '待审核', 1: '通过', 2: '驳回' }
  return map[status] || '未知'
}

async function saveGoods() {
  const goodsData = {
    ...goodsForm.value,
    merchantId: merchantStore.merchantInfo?.id,
    images: goodsForm.value.images.join(',')
  }
  try {
    let response
    if (isEditing.value) {
      response = await axios.put(`/mall/goods/${goodsForm.value.id}`, goodsData)
      if (response.code === 200) {
        ElMessage.success('修改成功')
      }
    } else {
      response = await axios.post('/mall/goods', goodsData)
      if (response.code === 200) {
        ElMessage.success('添加成功，等待审核')
      }
    }
    showAddModal.value = false
    goodsForm.value = { goodsName: '', category: 1, price: 0, stock: 0, description: '', images: [] }
    isEditing.value = false
    loadGoods()
  } catch (error) {
    ElMessage.error(isEditing.value ? '修改失败' : '添加失败')
  }
}

function handleGoodsImageUpload(response) {
  if (response.code === 200) {
    goodsForm.value.images.push(response.data.url)
    ElMessage.success('图片上传成功')
  }
}

function removeGoodsImage(index) {
  goodsForm.value.images.splice(index, 1)
}

function editGoods(goods) {
  goodsForm.value = {
    ...goods,
    images: goods.images ? goods.images.split(',') : []
  }
  isEditing.value = true
  showAddModal.value = true
}

async function deleteGoods(id) {
  try {
    const response = await axios.delete(`/mall/goods/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadGoods()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.goods-page {
  padding: 20px 0;
}

.add-btn {
  margin-bottom: 20px;
}

.goods-images {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.image-item {
  position: relative;
  width: 100px;
  height: 100px;
}

.image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.image-item .el-button {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  padding: 4px;
}

.upload-placeholder {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border: 2px dashed #ddd;
  border-radius: 8px;
  cursor: pointer;
}

.upload-placeholder span {
  font-size: 14px;
  color: #999;
}
</style>