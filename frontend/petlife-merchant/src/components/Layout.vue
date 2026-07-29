<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="logo">
        <span>🐾 PetLife</span>
        <span class="logo-sub">商户后台</span>
      </div>
      <nav class="menu">
        <router-link to="/dashboard" class="menu-item">📊 数据概览</router-link>
        <router-link to="/goods" class="menu-item">🛒 商品管理</router-link>
        <router-link to="/orders" class="menu-item">📋 订单管理</router-link>
        <router-link to="/services" class="menu-item">🏪 服务管理</router-link>
        <router-link to="/store" class="menu-item">🏠 门店信息</router-link>
      </nav>
      <button @click="logout" class="logout-btn">退出登录</button>
    </aside>
    <main class="main-content">
      <header class="header">
        <span class="welcome">欢迎, {{ merchantStore.merchantInfo?.storeName }}</span>
      </header>
      <div class="content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { useMerchantStore } from '../stores/merchant'
import { useRouter } from 'vue-router'

const merchantStore = useMerchantStore()
const router = useRouter()

function logout() {
  merchantStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  flex-direction: column;
}

.logo {
  padding: 24px;
  text-align: center;
}

.logo span:first-child {
  font-size: 24px;
  font-weight: bold;
  display: block;
}

.logo-sub {
  font-size: 14px;
  opacity: 0.8;
}

.menu {
  flex: 1;
  padding: 16px;
}

.menu-item {
  display: block;
  padding: 16px;
  color: white;
  text-decoration: none;
  margin-bottom: 8px;
  border-radius: 8px;
  transition: background 0.3s;
}

.menu-item:hover,
.menu-item.router-link-active {
  background: rgba(255, 255, 255, 0.2);
}

.logout-btn {
  margin: 16px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  border-radius: 8px;
  cursor: pointer;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  padding: 16px 24px;
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

.welcome {
  font-size: 16px;
  color: #333;
}

.content {
  flex: 1;
  padding: 24px;
}
</style>