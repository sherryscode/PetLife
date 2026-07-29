import axios from 'axios'
import { useMerchantStore } from '../stores/merchant'
import router from '../router'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(config => {
  const merchantStore = useMerchantStore()
  if (merchantStore.token) {
    config.headers.Authorization = `Bearer ${merchantStore.token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

instance.interceptors.response.use(response => {
  return response.data
}, error => {
  if (error.response?.status === 401) {
    const merchantStore = useMerchantStore()
    merchantStore.logout()
    router.push('/login')
  }
  return Promise.reject(error)
})

export default instance