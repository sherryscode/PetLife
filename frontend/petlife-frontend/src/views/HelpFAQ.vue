<template>
  <div class="help-page">
    <div class="container">
      <h2>❓ 常见问题</h2>
      
      <div class="faq-list">
        <div class="faq-item" v-for="(faq, index) in faqs" :key="index">
          <div class="faq-question" @click="toggleFaq(index)">
            <span class="faq-icon">{{ expandedIndex === index ? '−' : '+' }}</span>
            <span>{{ faq.question }}</span>
          </div>
          <div class="faq-answer" v-show="expandedIndex === index">
            {{ faq.answer }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const expandedIndex = ref(-1)

const faqs = [
  {
    question: '如何修改我的个人信息？',
    answer: '登录后进入"个人中心"页面，点击"编辑资料"即可修改昵称、头像等个人信息。'
  },
  {
    question: '预约服务后可以取消吗？',
    answer: '可以的。在"我的订单"页面找到对应的预约订单，点击"取消预约"即可。提前24小时取消不收取手续费。'
  },
  {
    question: '商城商品如何退换货？',
    answer: '收到商品后7天内，在"我的订单"页面申请退换货。请保持商品完好并提供相关凭证，审核通过后即可退换。'
  },
  {
    question: '如何联系客服？',
    answer: '可以在"帮助中心"的"联系客服"页面找到客服联系方式，也可以通过App内在线客服进行咨询。'
  },
  {
    question: '宠物档案中的疫苗记录如何添加？',
    answer: '进入"宠物档案"页面，选择对应的宠物，点击"添加疫苗记录"，填写疫苗名称、接种日期等信息即可。'
  },
  {
    question: '社区帖子为什么看不到？',
    answer: '新发布的帖子需要经过审核才能显示。审核时间一般在24小时内，请耐心等待。'
  },
  {
    question: '如何找回密码？',
    answer: '在登录页面点击"忘记密码"，输入注册手机号，通过短信验证码重置密码。'
  },
  {
    question: '门店服务价格是否包含所有费用？',
    answer: '门店服务价格为基础服务费用，如需要额外服务可能会产生附加费用，具体以门店实际收费为准。'
  }
]

function toggleFaq(index) {
  expandedIndex.value = expandedIndex.value === index ? -1 : index
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

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.faq-item {
  background: #f8f9fa;
  border-radius: 12px;
  overflow: hidden;
}

.faq-question {
  display: flex;
  align-items: center;
  padding: 20px;
  cursor: pointer;
  font-weight: 500;
}

.faq-icon {
  width: 24px;
  height: 24px;
  background: #667eea;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 18px;
}

.faq-answer {
  padding: 0 20px 20px 56px;
  color: #666;
  line-height: 1.6;
}
</style>