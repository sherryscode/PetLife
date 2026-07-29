<template>
  <div class="home">
    <section class="hero">
      <div class="container">
        <div class="hero-content">
          <h1>🐾 PetLife</h1>
          <p>宠物全生命周期管理平台</p>
          <p>记录爱宠成长，守护健康生活</p>
          <div class="hero-buttons">
            <button @click="$router.push('/pet')" class="btn-primary">开始记录</button>
            <button @click="$router.push('/store')" class="btn-secondary">预约服务</button>
          </div>
        </div>
      </div>
    </section>

    <section class="features">
      <div class="container">
        <h2>核心功能</h2>
        <div class="feature-grid">
          <div class="feature-card">
            <div class="feature-icon">🐕</div>
            <h3>宠物档案</h3>
            <p>记录宠物基本信息、疫苗接种、体检记录</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🏥</div>
            <h3>健康管理</h3>
            <p>疫苗到期提醒、病历管理、健康报告生成</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🛒</div>
            <h3>商城购物</h3>
            <p>宠物用品选购、购物车、订单管理</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🏪</div>
            <h3>门店服务</h3>
            <p>洗护、寄养、遛狗、医疗服务预约</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📢</div>
            <h3>寻宠启事</h3>
            <p>发布走失信息、同城寻宠互助</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💬</div>
            <h3>养宠社区</h3>
            <p>分享养宠经验、交流心得、结识宠友</p>
          </div>
        </div>
      </div>
    </section>

    <section class="stats">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">10万+</div>
            <div class="stat-label">养宠用户</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">500+</div>
            <div class="stat-label">合作门店</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">100万+</div>
            <div class="stat-label">服务订单</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">99.8%</div>
            <div class="stat-label">用户好评</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.hero {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 100px 0;
  text-align: center;
}

.hero-content h1 {
  font-size: 64px;
  margin-bottom: 16px;
}

.hero-content p {
  font-size: 24px;
  margin-bottom: 12px;
  opacity: 0.9;
}

.hero-buttons {
  margin-top: 32px;
}

.btn-primary,
.btn-secondary {
  padding: 16px 32px;
  border-radius: 30px;
  font-size: 18px;
  cursor: pointer;
  margin: 0 12px;
}

.btn-primary {
  background: white;
  color: #667eea;
  border: none;
}

.btn-secondary {
  background: transparent;
  color: white;
  border: 2px solid white;
}

.features {
  padding: 80px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.features h2 {
  text-align: center;
  font-size: 36px;
  margin-bottom: 48px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
}

.feature-card {
  background: #f8f9fa;
  padding: 32px;
  border-radius: 16px;
  text-align: center;
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 12px;
}

.feature-card p {
  color: #666;
}

.stats {
  background: #667eea;
  color: white;
  padding: 60px 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 48px;
  font-weight: bold;
}

.stat-label {
  font-size: 16px;
  opacity: 0.8;
  margin-top: 8px;
}
</style>