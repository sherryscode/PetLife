<template>
  <div class="social-page">
    <div class="container">
      <h2>💬 养宠社区</h2>
      
      <div class="tabs">
        <el-button :class="{ active: activeTab === 'posts' }" @click="activeTab = 'posts'">帖子</el-button>
        <el-button :class="{ active: activeTab === 'lost' }" @click="activeTab = 'lost'">寻宠启事</el-button>
      </div>

      <div v-if="activeTab === 'posts'" class="posts-section">
        <el-button type="primary" @click="showPostModal = true" class="add-post-btn">发布帖子</el-button>
        
        <div class="posts-list">
          <div class="post-card" v-for="post in posts" :key="post.id" @click="viewPostDetail(post)">
            <h3>{{ post.title }}</h3>
            <p>{{ truncateText(post.content, 15) }}</p>
            <div v-if="post.images" class="post-images-show">
              <img v-for="(img, index) in post.images.split(',')" :key="index" :src="img" class="post-image" />
            </div>
            <div class="post-meta">
              <span>点赞: {{ post.likes }}</span>
              <span>评论: {{ post.comments }}</span>
              <span>收藏: {{ post.favorites }}</span>
            </div>
            <div class="post-actions">
              <el-button @click.stop="likePost(post)" :type="likedPosts.includes(post.id) ? 'primary' : ''">
                {{ likedPosts.includes(post.id) ? '已点赞' : '点赞' }}
              </el-button>
              <el-button @click.stop="toggleComments(post)">评论({{ post.comments }})</el-button>
              <el-button v-if="userStore.userInfo && post.userId === userStore.userInfo.id" type="danger" @click.stop="deletePost(post)">删除</el-button>
            </div>
            
            <div v-if="expandedPosts.includes(post.id)" class="comments-section">
              <div class="comment-input">
                <el-input v-model="commentForms[post.id]" placeholder="发表评论..." @keyup.enter="submitComment(post)" />
                <el-button type="primary" @click="submitComment(post)">发送</el-button>
              </div>
              <div class="comments-list">
                <div class="comment-item" v-for="comment in getComments(post.id)" :key="comment.id">
                  <p><strong>用户{{ comment.userId }}:</strong> {{ comment.content }}</p>
                  <p class="comment-time">{{ comment.createdAt }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'lost'" class="lost-section">
        <el-button type="primary" @click="showLostModal = true" class="add-lost-btn">发布寻宠启事</el-button>
        
        <div class="lost-list">
          <div class="lost-card" v-for="lost in lostPets" :key="lost.id" @click="viewLostDetail(lost)">
            <div class="lost-image">
              <img :src="lost.avatar || 'https://via.placeholder.com/100'" alt="" />
            </div>
            <div class="lost-info">
              <h3>{{ lost.petName }}</h3>
              <p>品种: {{ lost.breed }}</p>
              <p>丢失地点: {{ lost.lostAddress }}</p>
              <p>悬赏: ¥{{ lost.reward }}</p>
            </div>
          </div>
        </div>
      </div>

      <el-dialog v-model="showPostModal" title="发布帖子" width="600px">
        <el-form :model="postForm">
          <el-form-item label="标题">
            <el-input v-model="postForm.title" />
          </el-form-item>
          <el-form-item label="内容">
            <el-input v-model="postForm.content" type="textarea" :rows="5" />
          </el-form-item>
          <el-form-item label="图片">
            <div class="post-images">
              <div v-for="(img, index) in postForm.images" :key="index" class="image-item">
                <img :src="img" class="image-preview" />
                <el-button size="small" type="danger" @click="removePostImage(index)">删除</el-button>
              </div>
              <el-upload
                class="upload-btn"
                action="/api/files/upload"
                :show-file-list="false"
                :on-success="handlePostImageUpload"
                accept="image/*"
              >
                <div class="upload-placeholder">
                  <span>+ 添加图片</span>
                </div>
              </el-upload>
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showPostModal = false">取消</el-button>
          <el-button type="primary" @click="savePost">发布</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showLostModal" title="发布寻宠启事" width="600px">
        <el-form :model="lostForm">
          <el-form-item label="宠物图片">
            <div class="avatar-upload">
              <img v-if="lostForm.avatar" :src="lostForm.avatar" class="avatar-preview" />
              <div v-else class="avatar-placeholder">
                <span>📷</span>
              </div>
              <el-upload
                class="upload-btn"
                action="/api/files/upload"
                :show-file-list="false"
                :on-success="handleLostAvatarUpload"
                accept="image/*"
              >
                <el-button size="small">选择图片</el-button>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item label="宠物名称">
            <el-input v-model="lostForm.petName" />
          </el-form-item>
          <el-form-item label="品种">
            <el-input v-model="lostForm.breed" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="lostForm.gender">
              <el-option :value="1" label="公" />
              <el-option :value="2" label="母" />
              <el-option :value="0" label="未知" />
            </el-select>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input-number v-model="lostForm.age" :min="0" />
          </el-form-item>
          <el-form-item label="丢失地点">
            <el-input v-model="lostForm.lostAddress" />
          </el-form-item>
          <el-form-item label="丢失时间">
            <el-date-picker v-model="lostForm.lostTime" type="date" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="lostForm.contactPhone" />
          </el-form-item>
          <el-form-item label="悬赏金额">
            <el-input-number v-model="lostForm.reward" :min="0" />
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="lostForm.description" type="textarea" :rows="4" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showLostModal = false">取消</el-button>
          <el-button type="primary" @click="saveLost">发布</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showPostDetailModal" title="帖子详情" width="800px">
        <div v-if="currentPost" class="post-detail">
          <h2>{{ currentPost.title }}</h2>
          <p>{{ currentPost.content }}</p>
          <div v-if="currentPost.images" class="post-images-show">
            <img v-for="(img, index) in currentPost.images.split(',')" :key="index" :src="img" class="post-detail-image" />
          </div>
          <div class="post-detail-meta">
            <span>点赞: {{ currentPost.likes }}</span>
            <span>评论: {{ currentPost.comments }}</span>
            <span>收藏: {{ currentPost.favorites }}</span>
            <span>发布时间: {{ currentPost.createdAt }}</span>
          </div>
        </div>
        <template #footer>
          <el-button @click="showPostDetailModal = false">关闭</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showLostDetailModal" title="寻宠启事详情" width="600px">
        <div v-if="currentLost" class="lost-detail">
          <div class="lost-detail-image">
            <img :src="currentLost.avatar || 'https://via.placeholder.com/200'" alt="" />
          </div>
          <div class="lost-detail-info">
            <h3>{{ currentLost.petName }}</h3>
            <p><strong>品种:</strong> {{ currentLost.breed }}</p>
            <p><strong>性别:</strong> {{ currentLost.gender === 1 ? '公' : currentLost.gender === 2 ? '母' : '未知' }}</p>
            <p><strong>年龄:</strong> {{ currentLost.age }}岁</p>
            <p><strong>丢失地点:</strong> {{ currentLost.lostAddress }}</p>
            <p><strong>丢失时间:</strong> {{ currentLost.lostTime }}</p>
            <p><strong>联系电话:</strong> {{ currentLost.contactPhone }}</p>
            <p><strong>悬赏金额:</strong> ¥{{ currentLost.reward }}</p>
            <p><strong>描述:</strong> {{ currentLost.description }}</p>
          </div>
        </div>
        <template #footer>
          <el-button @click="showLostDetailModal = false">关闭</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, reactive } from 'vue'
import axios from '../utils/axios'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const activeTab = ref('posts')
const posts = ref([])
const lostPets = ref([])
const showPostModal = ref(false)
const showLostModal = ref(false)
const showPostDetailModal = ref(false)
const showLostDetailModal = ref(false)
const currentPost = ref(null)
const currentLost = ref(null)
const postForm = ref({ title: '', content: '', images: [] })
const lostForm = ref({
  petName: '',
  breed: '',
  gender: 0,
  age: 0,
  avatar: '',
  lostAddress: '',
  lostTime: '',
  contactPhone: '',
  reward: 0,
  description: ''
})
const expandedPosts = ref([])
const allComments = ref([])
const commentForms = reactive({})
const likedPosts = ref([])

const userStore = useUserStore()

onMounted(() => {
  loadData()
})

watch(activeTab, () => {
  loadData()
})

async function loadData() {
  if (activeTab.value === 'posts') {
    try {
      const response = await axios.get('/social/post/list?page=1&size=20&auditStatus=1')
      if (response.code === 200) {
        posts.value = response.data.records
        await loadLikedStatus()
      }
    } catch (error) {
      console.error(error)
    }
  } else {
    try {
      const response = await axios.get('/social/lost/list?page=1&size=20&auditStatus=1')
      if (response.code === 200) {
        lostPets.value = response.data.records
      }
    } catch (error) {
      console.error(error)
    }
  }
}

async function loadLikedStatus() {
  if (!userStore.userInfo) return
  likedPosts.value = []
  for (const post of posts.value) {
    try {
      const response = await axios.get(`/social/post/${post.id}/isLiked?userId=${userStore.userInfo.id}`)
      if (response.code === 200 && response.data.liked) {
        likedPosts.value.push(post.id)
      }
    } catch (error) {
      console.error(error)
    }
  }
}

async function savePost() {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  const postData = {
    ...postForm.value,
    userId: userStore.userInfo.id,
    images: postForm.value.images.join(',')
  }
  try {
    const response = await axios.post('/social/post', postData)
    if (response.code === 200) {
      ElMessage.success('发布成功，等待审核')
      showPostModal.value = false
      postForm.value = { title: '', content: '', images: [] }
      loadData()
    }
  } catch (error) {
    ElMessage.error('发布失败')
  }
}

function handlePostImageUpload(response) {
  if (response.code === 200) {
    postForm.value.images.push(response.data.url)
    ElMessage.success('图片上传成功')
  }
}

function removePostImage(index) {
  postForm.value.images.splice(index, 1)
}

async function saveLost() {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  if (!lostForm.value.petName || !lostForm.value.lostAddress || !lostForm.value.contactPhone) {
    ElMessage.warning('请填写必填信息')
    return
  }
  try {
    const response = await axios.post('/social/lost', {
      ...lostForm.value,
      userId: userStore.userInfo.id
    })
    if (response.code === 200) {
      ElMessage.success('发布成功，等待审核')
      showLostModal.value = false
      lostForm.value = {
        petName: '',
        breed: '',
        gender: 0,
        age: 0,
        avatar: '',
        lostAddress: '',
        lostTime: '',
        contactPhone: '',
        reward: 0,
        description: ''
      }
      loadData()
    }
  } catch (error) {
    ElMessage.error('发布失败')
    console.error(error)
  }
}

function handleLostAvatarUpload(response) {
  if (response.code === 200) {
    lostForm.value.avatar = response.data.url
    ElMessage.success('图片上传成功')
  }
}

async function likePost(post) {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const response = await axios.put(`/social/post/${post.id}/like?userId=${userStore.userInfo.id}`)
    if (response.code === 200) {
      post.likes = response.data.likes
      const index = likedPosts.value.indexOf(post.id)
      if (index > -1) {
        likedPosts.value.splice(index, 1)
      } else {
        likedPosts.value.push(post.id)
      }
    }
  } catch (error) {
    console.error(error)
  }
}

function viewPostDetail(post) {
  currentPost.value = post
  showPostDetailModal.value = true
}

function viewLostDetail(lost) {
  currentLost.value = lost
  showLostDetailModal.value = true
}

function getComments(postId) {
  return allComments.value.filter(c => c.postId === postId)
}

async function toggleComments(post) {
  const index = expandedPosts.value.indexOf(post.id)
  if (index > -1) {
    expandedPosts.value.splice(index, 1)
  } else {
    expandedPosts.value.push(post.id)
    await loadComments(post.id)
  }
}

async function loadComments(postId) {
  try {
    const response = await axios.get(`/social/comment/post/${postId}`)
    if (response.code === 200) {
      allComments.value = [...allComments.value.filter(c => c.postId !== postId), ...response.data]
      const post = posts.value.find(p => p.id === postId)
      if (post) {
        post.comments = response.data.length
      }
    }
  } catch (error) {
    console.error(error)
  }
}

async function submitComment(post) {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }
  const content = commentForms[post.id]
  if (!content || !content.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  try {
    const response = await axios.post('/social/comment', {
      postId: post.id,
      userId: userStore.userInfo.id,
      content: content.trim()
    })
    
    if (response.code === 200) {
      allComments.value.push(response.data)
      commentForms[post.id] = ''
      post.comments++
      ElMessage.success('评论成功')
    }
  } catch (error) {
    ElMessage.error('评论失败')
    console.error(error)
  }
}

async function deletePost(post) {
  if (!userStore.userInfo || post.userId !== userStore.userInfo.id) {
    ElMessage.warning('无权删除此帖子')
    return
  }
  
  try {
    const response = await axios.delete(`/social/post/${post.id}`)
    if (response.code === 200) {
      posts.value = posts.value.filter(p => p.id !== post.id)
      allComments.value = allComments.value.filter(c => c.postId !== post.id)
      ElMessage.success('删除成功')
    }
  } catch (error) {
    ElMessage.error('删除失败')
    console.error(error)
  }
}

function truncateText(text, maxLength) {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}
</script>

<style scoped>
.social-page {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.tabs {
  margin-bottom: 32px;
}

.tabs .el-button {
  margin-right: 12px;
}

.tabs .el-button.active {
  background: #667eea;
  color: white;
}

.add-post-btn,
.add-lost-btn {
  margin-bottom: 24px;
}

.posts-list,
.lost-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.post-card {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  cursor: pointer;
}

.post-card:hover {
  background: #e9ecef;
}

.post-card h3 {
  margin-bottom: 12px;
}

.post-card p {
  margin-bottom: 16px;
  color: #666;
}

.post-images-show {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

.post-image {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
}

.post-detail-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.post-meta {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.post-detail-meta {
  display: flex;
  gap: 24px;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #ddd;
}

.post-actions {
  display: flex;
  gap: 12px;
}

.post-actions .el-button {
  cursor: pointer;
}

.lost-card {
  display: flex;
  gap: 24px;
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  cursor: pointer;
}

.lost-card:hover {
  background: #e9ecef;
}

.lost-image img {
  width: 100px;
  height: 100px;
  object-fit: cover;
}

.lost-info h3 {
  margin-bottom: 8px;
}

.lost-info p {
  margin-bottom: 4px;
  color: #666;
}

.lost-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.lost-detail-image img {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 20px;
}

.lost-detail-info {
  width: 100%;
}

.lost-detail-info h3 {
  text-align: center;
  margin-bottom: 16px;
}

.lost-detail-info p {
  margin-bottom: 8px;
  color: #666;
}

.comments-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ddd;
}

.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.comment-input .el-input {
  flex: 1;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-item {
  background: #fff;
  padding: 12px;
  border-radius: 8px;
}

.comment-item p {
  margin-bottom: 4px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 50%;
  border: 2px solid #ddd;
}

.avatar-placeholder {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 50%;
  font-size: 40px;
  border: 2px dashed #ddd;
}

.post-images {
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