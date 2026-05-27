import request from '@/utils/request'

export function getDashboardStats() {
  return request({ url: '/statistics/dashboard', method: 'get' })
}

export function getViewStats(params) {
  return request({ url: '/statistics/views', method: 'get', params })
}

export function getDownloadStats(params) {
  return request({ url: '/statistics/downloads', method: 'get', params })
}

export function getCategoryStats() {
  return request({ url: '/statistics/category', method: 'get' })
}

export function getTopResources(params) {
  return request({ url: '/statistics/top', method: 'get', params })
}
