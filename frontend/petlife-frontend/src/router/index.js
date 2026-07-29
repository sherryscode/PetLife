import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
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
    path: '/pet',
    name: 'Pet',
    component: () => import('../views/Pet.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/health',
    name: 'Health',
    component: () => import('../views/Health.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/store',
    name: 'Store',
    component: () => import('../views/Store.vue')
  },
  {
    path: '/mall',
    name: 'Mall',
    component: () => import('../views/Mall.vue')
  },
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/Cart.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/social',
    name: 'Social',
    component: () => import('../views/Social.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/checkout',
    name: 'Checkout',
    component: () => import('../views/Checkout.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order',
    name: 'Order',
    component: () => import('../views/Order.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/help/guide',
    name: 'HelpGuide',
    component: () => import('../views/HelpGuide.vue')
  },
  {
    path: '/help/faq',
    name: 'HelpFAQ',
    component: () => import('../views/HelpFAQ.vue')
  },
  {
    path: '/help/contact',
    name: 'HelpContact',
    component: () => import('../views/HelpContact.vue')
  },
  {
    path: '/help/privacy',
    name: 'HelpPrivacy',
    component: () => import('../views/HelpPrivacy.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router