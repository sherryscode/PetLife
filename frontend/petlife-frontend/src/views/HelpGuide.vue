<template>
  <div class="help-page">
    <div class="container">
      <h2>📖 使用指南</h2>
      
      <div class="guide-section">
        <h3>1. 注册与登录</h3>
        <ul>
          <li>点击首页右上角"注册"按钮，填写手机号和密码完成注册</li>
          <li>注册成功后自动登录，也可使用手机号和密码手动登录</li>
          <li>支持手机号验证码登录</li>
        </ul>
      </div>

      <div class="guide-section">
        <h3>2. 宠物档案管理</h3>
        <ul>
          <li>登录后进入"宠物档案"页面</li>
          <li>点击"添加宠物"按钮，填写宠物基本信息</li>
          <li>可以管理宠物的疫苗记录和健康档案</li>
        </ul>
      </div>

      <div class="guide-section">
        <h3>3. 预约门店服务</h3>
        <ul>
          <li>进入"门店服务"页面，浏览或搜索附近门店</li>
          <li>点击"查看服务"查看门店提供的服务项目</li>
          <li>选择服务后点击"预约"，填写预约日期和时间</li>
          <li>提交预约后可在"我的订单"查看预约状态</li>
        </ul>
      </div>

      <div class="guide-section">
        <h3>4. 商城购物</h3>
        <ul>
          <li>进入"商城"页面，浏览各类宠物商品</li>
          <li>点击"加入购物车"将商品添加到购物车</li>
          <li>在购物车页面修改数量或删除商品</li>
          <li>点击"结算"完成订单支付</li>
        </ul>
      </div>

      <div class="guide-section">
        <h3>5. 养宠社区</h3>
        <ul>
          <li>进入"社区"页面浏览其他用户发布的帖子</li>
          <li>点击"发布帖子"分享养宠心得</li>
          <li>可以对帖子进行点赞和评论</li>
          <li>支持发布寻宠启事</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
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

.guide-section {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.guide-section h3 {
  margin-bottom: 16px;
  color: #667eea;
}

.guide-section ul {
  margin: 0;
  padding-left: 24px;
}

.guide-section li {
  margin-bottom: 8px;
  color: #666;
}
</style>