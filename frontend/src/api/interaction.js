import request from '@/utils/request'

export function getComments(resourceId) {
  return request({ url: `/comment/list/${resourceId}`, method: 'get' })
}

export function addComment(data) {
  return request({ url: '/comment', method: 'post', data })
}

export function deleteComment(id) {
  return request({ url: `/comment/${id}`, method: 'delete' })
}

export function addFavorite(resourceId) {
  return request({ url: `/favorite/${resourceId}`, method: 'post' })
}

export function removeFavorite(resourceId) {
  return request({ url: `/favorite/${resourceId}`, method: 'delete' })
}

export function checkFavorite(resourceId) {
  return request({ url: `/favorite/check/${resourceId}`, method: 'get' })
}

export function getFavoriteList(params) {
  return request({ url: '/favorite/list', method: 'get', params })
}

export function getQuestionPage(params) {
  return request({ url: '/question/list', method: 'get', params })
}

export function getQuestionDetail(id) {
  return request({ url: `/question/detail/${id}`, method: 'get' })
}

export function addQuestion(data) {
  return request({ url: '/question', method: 'post', data })
}

export function updateQuestion(data) {
  return request({ url: '/question', method: 'put', data })
}

export function deleteQuestion(id) {
  return request({ url: `/question/${id}`, method: 'delete' })
}

export function solveQuestion(id) {
  return request({ url: `/question/solve/${id}`, method: 'put' })
}

export function getReplies(questionId) {
  return request({ url: `/question/reply/list/${questionId}`, method: 'get' })
}

export function addReply(data) {
  return request({ url: '/question/reply', method: 'post', data })
}

export function deleteReply(id) {
  return request({ url: `/question/reply/${id}`, method: 'delete' })
}
