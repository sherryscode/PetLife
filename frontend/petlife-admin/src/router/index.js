import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: '/dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
      { path: '/users', name: 'Users', component: () => import('../views/Users.vue') },
      { path: '/merchants', name: 'Merchants', component: () => import('../views/Merchants.vue') },
      { path: '/audit', name: 'Audit', component: () => import('../views/Audit.vue') },
      { path: '/orders', name: 'Orders', component: () => import('../views/Orders.vue') },
      { path: '/logs', name: 'Logs', component: () => import('../views/Logs.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('admin_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router