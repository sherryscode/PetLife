import axios from 'axios'
import { useAdminStore } from '../stores/admin'
import router from '../router'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(config => {
  const adminStore = useAdminStore()
  if (adminStore.token) {
    config.headers.Authorization = `Bearer ${adminStore.token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

instance.interceptors.response.use(response => {
  return response.data
}, error => {
  if (error.response?.status === 401) {
    const adminStore = useAdminStore()
    adminStore.logout()
    router.push('/login')
  }
  return Promise.reject(error)
})

export default instance