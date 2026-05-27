import request from '@/utils/request'

export function getResourcePage(params) {
  return request({ url: '/resource/list', method: 'get', params })
}

export function getResourceDetail(id) {
  return request({ url: `/resource/detail/${id}`, method: 'get' })
}

export function uploadResource(data) {
  return request({
    url: '/resource/upload',
    method: 'post',
    data,
    headers: { 'Content-Type': 'multipart/form-data' },
    timeout: 300000
  })
}

export function updateResource(data) {
  return request({ url: '/resource/update', method: 'put', data })
}

export function deleteResource(id) {
  return request({ url: `/resource/${id}`, method: 'delete' })
}

export function auditResource(id, data) {
  return request({ url: `/resource/audit/${id}`, method: 'put', data })
}

export function downloadResource(id) {
  return request({ url: `/resource/download/${id}`, method: 'get' })
}

export function recordView(id) {
  return request({ url: `/resource/view/${id}`, method: 'post' })
}

export function getCategoryList() {
  return request({ url: '/resource/category/list', method: 'get' })
}

export function addCategory(data) {
  return request({ url: '/resource/category', method: 'post', data })
}

export function updateCategory(data) {
  return request({ url: '/resource/category', method: 'put', data })
}

export function deleteCategory(id) {
  return request({ url: `/resource/category/${id}`, method: 'delete' })
}
