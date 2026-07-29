<template>
  <div class="audit-page">
    <h2>✅ 内容审核</h2>
    
    <div class="tabs">
      <el-button v-for="tab in tabs" :key="tab.value"
        :class="{ active: activeTab === tab.value }"
        @click="activeTab = tab.value">
        {{ tab.label }}
      </el-button>
    </div>

    <el-table v-if="activeTab === 'posts'" :data="posts" border style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="title" label="标题" />
      <el-table-column label="内容" width="200">
          <template #default="scope">
            {{ truncateText(scope.row.content, 15) }}
          </template>
        </el-table-column>
      <el-table-column prop="createdAt" label="发布时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="viewPostDetail(scope.row)">查看</el-button>
          <el-button @click="auditPost(scope.row, 1)">通过</el-button>
          <el-button type="danger" @click="auditPost(scope.row, 2)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-table v-if="activeTab === 'goods'" :data="goods" border style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="goodsName" label="商品名称" />
      <el-table-column prop="price" label="价格" />
      <el-table-column prop="createdAt" label="提交时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="viewGoodsDetail(scope.row)">查看</el-button>
          <el-button @click="auditGoods(scope.row, 1)">通过</el-button>
          <el-button type="danger" @click="auditGoods(scope.row, 2)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-table v-if="activeTab === 'lost'" :data="lostPets" border style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="petName" label="宠物名称" />
      <el-table-column prop="breed" label="品种" />
      <el-table-column prop="lostAddress" label="丢失地点" />
      <el-table-column prop="createdAt" label="发布时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="viewLostDetail(scope.row)">查看</el-button>
          <el-button @click="auditLost(scope.row, 1)">通过</el-button>
          <el-button type="danger" @click="auditLost(scope.row, 2)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="postDetailVisible" title="帖子详情" width="600px">
      <div v-if="currentPost" class="detail-content">
        <el-form label-width="100px">
          <el-form-item label="标题">
            <span>{{ currentPost.title }}</span>
          </el-form-item>
          <el-form-item label="内容">
            <p class="content-text">{{ currentPost.content }}</p>
          </el-form-item>
          <el-form-item label="用户ID">
            <span>{{ currentPost.userId }}</span>
          </el-form-item>
          <el-form-item label="点赞数">
            <span>{{ currentPost.likes }}</span>
          </el-form-item>
          <el-form-item label="评论数">
            <span>{{ currentPost.comments }}</span>
          </el-form-item>
          <el-form-item label="收藏数">
            <span>{{ currentPost.favorites }}</span>
          </el-form-item>
          <el-form-item label="审核状态">
            <span>{{ getAuditStatusText(currentPost.auditStatus) }}</span>
          </el-form-item>
          <el-form-item label="发布时间">
            <span>{{ currentPost.createdAt }}</span>
          </el-form-item>
          <el-form-item label="图片">
            <div v-if="currentPost.images" class="image-preview">
              <img v-for="(img, index) in parseImages(currentPost.images)" :key="index" :src="img" style="max-width: 200px; max-height: 200px; margin-right: 10px;" />
            </div>
            <span v-else>暂无图片</span>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>

    <el-dialog v-model="goodsDetailVisible" title="商品详情" width="600px">
      <div v-if="currentGoods" class="detail-content">
        <el-form label-width="100px">
          <el-form-item label="商品名称">
            <span>{{ currentGoods.goodsName }}</span>
          </el-form-item>
          <el-form-item label="商户ID">
            <span>{{ currentGoods.merchantId }}</span>
          </el-form-item>
          <el-form-item label="分类">
            <span>{{ currentGoods.category }}</span>
          </el-form-item>
          <el-form-item label="价格">
            <span>{{ currentGoods.price }}</span>
          </el-form-item>
          <el-form-item label="原价">
            <span>{{ currentGoods.originalPrice }}</span>
          </el-form-item>
          <el-form-item label="库存">
            <span>{{ currentGoods.stock }}</span>
          </el-form-item>
          <el-form-item label="销量">
            <span>{{ currentGoods.sales }}</span>
          </el-form-item>
          <el-form-item label="是否热销">
            <span>{{ currentGoods.isHot === 1 ? '是' : '否' }}</span>
          </el-form-item>
          <el-form-item label="审核状态">
            <span>{{ getAuditStatusText(currentGoods.auditStatus) }}</span>
          </el-form-item>
          <el-form-item label="状态">
            <span>{{ currentGoods.status === 1 ? '上架' : '下架' }}</span>
          </el-form-item>
          <el-form-item label="描述">
            <p class="content-text">{{ currentGoods.description }}</p>
          </el-form-item>
          <el-form-item label="图片">
            <div v-if="currentGoods.images" class="image-preview">
              <img v-for="(img, index) in parseImages(currentGoods.images)" :key="index" :src="img" style="max-width: 200px; max-height: 200px; margin-right: 10px;" />
            </div>
            <span v-else>暂无图片</span>
          </el-form-item>
          <el-form-item label="提交时间">
            <span>{{ currentGoods.createdAt }}</span>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>

    <el-dialog v-model="lostDetailVisible" title="寻宠启事详情" width="600px">
      <div v-if="currentLost" class="detail-content">
        <el-form label-width="100px">
          <el-form-item label="宠物名称">
            <span>{{ currentLost.petName }}</span>
          </el-form-item>
          <el-form-item label="品种">
            <span>{{ currentLost.breed }}</span>
          </el-form-item>
          <el-form-item label="性别">
            <span>{{ currentLost.gender === 1 ? '公' : '母' }}</span>
          </el-form-item>
          <el-form-item label="年龄">
            <span>{{ currentLost.age }}</span>
          </el-form-item>
          <el-form-item label="丢失地点">
            <span>{{ currentLost.lostAddress }}</span>
          </el-form-item>
          <el-form-item label="丢失时间">
            <span>{{ currentLost.lostTime }}</span>
          </el-form-item>
          <el-form-item label="联系电话">
            <span>{{ currentLost.contactPhone }}</span>
          </el-form-item>
          <el-form-item label="悬赏金额">
            <span>{{ currentLost.reward }}</span>
          </el-form-item>
          <el-form-item label="是否置顶">
            <span>{{ currentLost.isTop === 1 ? '是' : '否' }}</span>
          </el-form-item>
          <el-form-item label="审核状态">
            <span>{{ getAuditStatusText(currentLost.auditStatus) }}</span>
          </el-form-item>
          <el-form-item label="状态">
            <span>{{ currentLost.status === 1 ? '正常' : '已结束' }}</span>
          </el-form-item>
          <el-form-item label="描述">
            <p class="content-text">{{ currentLost.description }}</p>
          </el-form-item>
          <el-form-item label="图片">
            <div v-if="currentLost.avatar" class="image-preview">
              <img :src="currentLost.avatar" style="max-width: 200px; max-height: 200px;" />
            </div>
            <span v-else>暂无图片</span>
          </el-form-item>
          <el-form-item label="发布时间">
            <span>{{ currentLost.createdAt }}</span>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const tabs = [
  { label: '帖子审核', value: 'posts' },
  { label: '商品审核', value: 'goods' },
  { label: '寻宠审核', value: 'lost' }
]

const activeTab = ref('posts')
const posts = ref([])
const goods = ref([])
const lostPets = ref([])

const postDetailVisible = ref(false)
const goodsDetailVisible = ref(false)
const lostDetailVisible = ref(false)

const currentPost = ref(null)
const currentGoods = ref(null)
const currentLost = ref(null)

onMounted(() => {
  loadData()
})

watch(activeTab, () => {
  loadData()
})

async function loadData() {
  if (activeTab.value === 'posts') {
    try {
      const response = await axios.get('/social/post/list?page=1&size=20&auditStatus=0')
      if (response.code === 200) {
        posts.value = response.data.records
      }
    } catch (error) {
      console.error(error)
    }
  } else if (activeTab.value === 'goods') {
    try {
      const response = await axios.get('/mall/goods/list?page=1&size=20&auditStatus=0')
      if (response.code === 200) {
        goods.value = response.data.records
      }
    } catch (error) {
      console.error(error)
    }
  } else {
    try {
      const response = await axios.get('/social/lost/list?page=1&size=20&auditStatus=0')
      if (response.code === 200) {
        lostPets.value = response.data.records
      }
    } catch (error) {
      console.error(error)
    }
  }
}

async function auditPost(post, status) {
  try {
    const response = await axios.put(`/social/post/${post.id}/audit`, { auditStatus: status })
    if (response.code === 200) {
      ElMessage.success(status === 1 ? '审核通过' : '已驳回')
      loadData()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function auditGoods(good, status) {
  try {
    const response = await axios.put(`/mall/goods/${good.id}/audit`, { auditStatus: status })
    if (response.code === 200) {
      ElMessage.success(status === 1 ? '审核通过' : '已驳回')
      loadData()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function auditLost(lost, status) {
  try {
    const response = await axios.put(`/social/lost/${lost.id}/audit`, { auditStatus: status })
    if (response.code === 200) {
      ElMessage.success(status === 1 ? '审核通过' : '已驳回')
      loadData()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function viewPostDetail(post) {
  try {
    const response = await axios.get(`/social/post/${post.id}`)
    if (response.code === 200) {
      currentPost.value = response.data
      postDetailVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

async function viewGoodsDetail(goods) {
  try {
    const response = await axios.get(`/mall/goods/${goods.id}`)
    if (response.code === 200) {
      currentGoods.value = response.data
      goodsDetailVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

async function viewLostDetail(lost) {
  try {
    const response = await axios.get(`/social/lost/${lost.id}`)
    if (response.code === 200) {
      currentLost.value = response.data
      lostDetailVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

function getAuditStatusText(status) {
  const map = { 0: '待审核', 1: '已通过', 2: '已驳回' }
  return map[status] || '未知'
}

function parseImages(images) {
  if (!images) return []
  try {
    return JSON.parse(images)
  } catch {
    return [images]
  }
}

function truncateText(text, maxLength) {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}
</script>

<style scoped>
.audit-page {
  padding: 20px 0;
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

.detail-content {
  padding: 10px;
}

.content-text {
  white-space: pre-wrap;
  word-break: break-all;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>