import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'resource',
        name: 'ResourceList',
        component: () => import('@/views/resource/ResourceList.vue'),
        meta: { title: '资源列表' }
      },
      {
        path: 'resource/detail/:id',
        name: 'ResourceDetail',
        component: () => import('@/views/resource/ResourceDetail.vue'),
        meta: { title: '资源详情' }
      },
      {
        path: 'resource/upload',
        name: 'ResourceUpload',
        component: () => import('@/views/resource/ResourceUpload.vue'),
        meta: { title: '上传资源', requireAuth: true }
      },
      {
        path: 'resource/audit',
        name: 'ResourceAudit',
        component: () => import('@/views/resource/ResourceAudit.vue'),
        meta: { title: '资源审核', requireAuth: true, roles: ['ADMIN', 'TEACHER'] }
      },
      {
        path: 'resource/my',
        name: 'MyResource',
        component: () => import('@/views/resource/MyResource.vue'),
        meta: { title: '我的资源', requireAuth: true }
      },
      {
        path: 'question',
        name: 'QuestionList',
        component: () => import('@/views/question/QuestionList.vue'),
        meta: { title: '问答中心' }
      },
      {
        path: 'question/detail/:id',
        name: 'QuestionDetail',
        component: () => import('@/views/question/QuestionDetail.vue'),
        meta: { title: '问题详情' }
      },
      {
        path: 'favorite',
        name: 'FavoriteList',
        component: () => import('@/views/favorite/FavoriteList.vue'),
        meta: { title: '我的收藏', requireAuth: true }
      },
      {
        path: 'user/manage',
        name: 'UserManage',
        component: () => import('@/views/user/UserManage.vue'),
        meta: { title: '用户管理', requireAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'user/profile',
        name: 'UserProfile',
        component: () => import('@/views/user/UserProfile.vue'),
        meta: { title: '个人中心', requireAuth: true }
      },
      {
        path: 'category/manage',
        name: 'CategoryManage',
        component: () => import('@/views/category/CategoryManage.vue'),
        meta: { title: '分类管理', requireAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/Statistics.vue'),
        meta: { title: '数据统计', requireAuth: true, roles: ['ADMIN', 'TEACHER'] }
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth && !store.getters.isLoggedIn) {
    next('/login')
    return
  }
  if (to.meta.roles && !to.meta.roles.includes(store.state.role)) {
    next('/home')
    return
  }
  next()
})

export default router
