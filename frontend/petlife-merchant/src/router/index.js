import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: '/dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
      { path: '/goods', name: 'Goods', component: () => import('../views/Goods.vue') },
      { path: '/orders', name: 'Orders', component: () => import('../views/Orders.vue') },
      { path: '/services', name: 'Services', component: () => import('../views/Services.vue') },
      { path: '/store', name: 'Store', component: () => import('../views/Store.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('merchant_token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router