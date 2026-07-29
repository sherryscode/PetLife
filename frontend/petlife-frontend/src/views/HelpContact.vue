<template>
  <div class="help-page">
    <div class="container">
      <h2>📞 联系客服</h2>
      
      <div class="contact-section">
        <div class="contact-card">
          <div class="contact-icon">📱</div>
          <h3>客服热线</h3>
          <p>400-888-8888</p>
          <p class="contact-time">服务时间：9:00-21:00</p>
        </div>
        
        <div class="contact-card">
          <div class="contact-icon">✉️</div>
          <h3>邮箱地址</h3>
          <p>service@petlife.com</p>
          <p class="contact-time">24小时内回复</p>
        </div>
        
        <div class="contact-card">
          <div class="contact-icon">💬</div>
          <h3>在线客服</h3>
          <p>点击下方按钮进入在线客服</p>
          <el-button type="primary" @click="openOnlineService">在线咨询</el-button>
        </div>
      </div>

      <div class="feedback-section">
        <h3>意见反馈</h3>
        <el-form :model="feedbackForm">
          <el-form-item label="您的姓名">
            <el-input v-model="feedbackForm.name" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="feedbackForm.phone" />
          </el-form-item>
          <el-form-item label="反馈类型">
            <el-select v-model="feedbackForm.type">
              <el-option label="功能建议" value="suggestion" />
              <el-option label="问题反馈" value="problem" />
              <el-option label="投诉" value="complaint" />
              <el-option label="其他" value="other" />
            </el-select>
          </el-form-item>
          <el-form-item label="反馈内容">
            <el-input v-model="feedbackForm.content" type="textarea" :rows="5" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const feedbackForm = ref({
  name: '',
  phone: '',
  type: 'suggestion',
  content: ''
})

function openOnlineService() {
  ElMessage.info('在线客服功能开发中，敬请期待！')
}

function submitFeedback() {
  if (!feedbackForm.value.name || !feedbackForm.value.content) {
    ElMessage.warning('请填写姓名和反馈内容')
    return
  }
  ElMessage.success('反馈提交成功，我们会尽快处理')
  feedbackForm.value = {
    name: '',
    phone: '',
    type: 'suggestion',
    content: ''
  }
}
</script>

<style scoped>
.help-page {
  padding: 40px 0;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.contact-section {
  display: flex;
  gap: 20px;
  margin-bottom: 40px;
}

.contact-card {
  flex: 1;
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  text-align: center;
}

.contact-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.contact-card h3 {
  margin-bottom: 12px;
}

.contact-card p {
  margin-bottom: 4px;
}

.contact-time {
  font-size: 12px;
  color: #999;
}

.feedback-section {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
}

.feedback-section h3 {
  margin-bottom: 20px;
}
</style>