<template>
  <header class="header">
    <div class="container">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🐾</span>
        <span class="logo-text">PetLife</span>
      </div>
      <nav class="nav">
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/pet" class="nav-link">宠物档案</router-link>
        <router-link to="/health" class="nav-link">健康管理</router-link>
        <router-link to="/store" class="nav-link">门店服务</router-link>
        <router-link to="/mall" class="nav-link">商城</router-link>
        <router-link to="/social" class="nav-link">社区</router-link>
      </nav>
      <div class="user-menu">
        <template v-if="userStore.userInfo">
          <router-link to="/cart" class="nav-link">🛒</router-link>
          <router-link to="/order" class="nav-link">订单</router-link>
          <router-link to="/profile" class="nav-link">{{ userStore.userInfo.nickname }}</router-link>
          <button @click="logout" class="logout-btn">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">登录</router-link>
          <router-link to="/register" class="nav-link">注册</router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

function logout() {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 16px 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-icon {
  font-size: 28px;
  margin-right: 8px;
}

.logo-text {
  font-size: 24px;
  font-weight: bold;
}

.nav {
  display: flex;
  gap: 32px;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-size: 16px;
  transition: opacity 0.3s;
}

.nav-link:hover {
  opacity: 0.8;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logout-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>