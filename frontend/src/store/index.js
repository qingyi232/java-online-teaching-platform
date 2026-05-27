import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    username: localStorage.getItem('username') || '',
    realName: localStorage.getItem('realName') || '',
    role: localStorage.getItem('role') || '',
    avatar: localStorage.getItem('avatar') || ''
  },
  mutations: {
    SET_USER(state, user) {
      state.token = user.token
      state.userId = user.userId
      state.username = user.username
      state.realName = user.realName
      state.role = user.role
      state.avatar = user.avatar || ''
      localStorage.setItem('token', user.token)
      localStorage.setItem('userId', user.userId)
      localStorage.setItem('username', user.username)
      localStorage.setItem('realName', user.realName)
      localStorage.setItem('role', user.role)
      localStorage.setItem('avatar', user.avatar || '')
    },
    LOGOUT(state) {
      state.token = ''
      state.userId = ''
      state.username = ''
      state.realName = ''
      state.role = ''
      state.avatar = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('realName')
      localStorage.removeItem('role')
      localStorage.removeItem('avatar')
    }
  },
  getters: {
    isLoggedIn: state => !!state.token,
    isAdmin: state => state.role === 'ADMIN',
    isTeacher: state => state.role === 'TEACHER',
    isStudent: state => state.role === 'STUDENT'
  }
})
