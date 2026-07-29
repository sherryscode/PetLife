<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>🐾 PetLife</h3>
          <p>宠物全生命周期管理平台</p>
          <p>让养宠更简单、更科学</p>
        </div>
        <div class="footer-section">
          <h4>服务模块</h4>
          <ul>
            <li><a href="/pet">宠物档案</a></li>
            <li><a href="/store">门店服务</a></li>
            <li><a href="/mall">商城购物</a></li>
            <li><a href="/social">养宠社区</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>帮助中心</h4>
          <ul>
            <li><router-link to="/help/guide">使用指南</router-link></li>
            <li><router-link to="/help/faq">常见问题</router-link></li>
            <li><router-link to="/help/contact">联系客服</router-link></li>
            <li><router-link to="/help/privacy">隐私政策</router-link></li>
          </ul>
        </div>
      </div>
      <div class="copyright">
        <p>&copy; 2024 PetLife. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: #333;
  color: white;
  padding: 40px 0;
  margin-top: auto;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: flex;
  justify-content: space-around;
  margin-bottom: 30px;
}

.footer-section h3,
.footer-section h4 {
  margin-bottom: 16px;
}

.footer-section ul {
  list-style: none;
}

.footer-section li {
  margin-bottom: 8px;
}

.footer-section a {
  color: #ccc;
  text-decoration: none;
}

.footer-section a:hover {
  color: white;
}

.copyright {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #666;
}
</style>