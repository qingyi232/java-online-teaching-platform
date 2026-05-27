import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import router from '@/router'

const service = axios.create({
  baseURL: '/api',
  timeout: 30000
})

service.interceptors.request.use(
  config => {
    const token = store.state.token
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    }
    if (res.code === 401) {
      store.commit('LOGOUT')
      router.push('/login')
      Message.error(res.message || '登录已过期')
      return Promise.reject(new Error(res.message))
    }
    Message.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message))
  },
  error => {
    Message.error('网络错误，请稍后重试')
    return Promise.reject(error)
  }
)

export default service
