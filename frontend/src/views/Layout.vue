<template>
  <div style="height: 100vh;">
  <!-- 管理员/教师：侧边栏布局 -->
  <el-container style="height: 100%" v-if="isAdmin || isTeacher">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="background-color: #1a2233; transition: width 0.3s;">
      <div class="logo" :class="{ 'logo-collapse': isCollapse }">
        <i class="el-icon-reading" style="font-size: 24px; color: #C9A96E"></i>
        <span v-if="!isCollapse" class="logo-text">教学资源平台</span>
      </div>
      <el-menu :default-active="$route.path" :collapse="isCollapse" background-color="#1a2233" text-color="#8899aa" active-text-color="#C9A96E" router unique-opened>
        <el-menu-item index="/home">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </el-menu-item>
        <el-submenu index="resource-menu">
          <template slot="title">
            <i class="el-icon-folder-opened"></i>
            <span>资源管理</span>
          </template>
          <el-menu-item index="/resource">资源列表</el-menu-item>
          <el-menu-item index="/resource/upload">上传资源</el-menu-item>
          <el-menu-item index="/resource/my">我的资源</el-menu-item>
          <el-menu-item index="/resource/audit">资源审核</el-menu-item>
        </el-submenu>
        <el-menu-item index="/question">
          <i class="el-icon-chat-dot-round"></i>
          <span slot="title">问答中心</span>
        </el-menu-item>
        <el-menu-item index="/favorite">
          <i class="el-icon-star-off"></i>
          <span slot="title">我的收藏</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <i class="el-icon-data-analysis"></i>
          <span slot="title">数据统计</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin" index="/user/manage">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>
        <el-menu-item v-if="isAdmin" index="/category/manage">
          <i class="el-icon-menu"></i>
          <span slot="title">分类管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: #fff; border-bottom: 1px solid #ebeef5; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; box-shadow: 0 1px 4px rgba(0,0,0,0.04);">
        <div style="display: flex; align-items: center;">
          <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'" style="font-size: 20px; cursor: pointer; color: #666;" @click="isCollapse = !isCollapse"></i>
          <el-breadcrumb separator="/" style="margin-left: 20px;">
            <el-breadcrumb-item>{{ $route.meta.title || '首页' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div style="display: flex; align-items: center;">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link" style="cursor: pointer; color: #666; display: flex; align-items: center;">
              <el-avatar :size="32" icon="el-icon-user-solid" style="margin-right: 8px;" />
              {{ realName || username }}
              <span style="margin-left: 4px; color: #999; font-size: 12px;">({{ roleLabel }})</span>
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main style="background: #f5f6fa; padding: 24px;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>

  <!-- 学生/游客：顶部导航栏全宽布局 -->
  <div class="student-layout" v-else>
    <header class="top-navbar">
      <div class="top-navbar-inner">
        <div class="top-navbar-left" @click="$router.push('/home')">
          <i class="el-icon-reading" style="font-size: 22px; color: #C9A96E; margin-right: 8px;"></i>
          <span class="top-navbar-brand">教学资源平台</span>
        </div>
        <nav class="top-navbar-nav">
          <a :class="{ active: $route.path === '/home' }" @click="$router.push('/home')">首页</a>
          <a :class="{ active: $route.path.startsWith('/resource') }" @click="$router.push('/resource')">资源中心</a>
          <a :class="{ active: $route.path.startsWith('/question') }" @click="$router.push('/question')">问答互动</a>
          <a v-if="isLoggedIn" :class="{ active: $route.path === '/favorite' }" @click="$router.push('/favorite')">我的收藏</a>
          <a v-if="isLoggedIn" :class="{ active: $route.path === '/resource/my' }" @click="$router.push('/resource/my')">我的资源</a>
        </nav>
        <div class="top-navbar-right">
          <template v-if="isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link" style="cursor: pointer; color: #ccc; display: flex; align-items: center;">
                <el-avatar :size="30" icon="el-icon-user-solid" style="margin-right: 8px; background: #3D5167;" />
                <span style="color: #e8e8e8;">{{ realName || username }}</span>
                <i class="el-icon-arrow-down el-icon--right" style="color: #999;"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button size="small" @click="$router.push('/login')" style="border-color: #C9A96E; color: #C9A96E; background: transparent;">登录</el-button>
            <el-button size="small" type="primary" @click="$router.push('/register')" style="background: #C9A96E; border-color: #C9A96E; margin-left: 8px;">注册</el-button>
          </template>
        </div>
      </div>
    </header>
    <main class="student-main">
      <router-view />
    </main>
  </div>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user'

export default {
  name: 'Layout',
  data() {
    return {
      isCollapse: false
    }
  },
  created() {
    if (this.isLoggedIn) {
      getUserInfo().then(res => {
        const u = res.data
        const updated = {
          token: this.$store.state.token,
          userId: this.$store.state.userId,
          username: u.username,
          realName: u.realName,
          role: u.role,
          avatar: u.avatar
        }
        this.$store.commit('SET_USER', updated)
      }).catch(() => {})
    }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn },
    isAdmin() { return this.$store.getters.isAdmin },
    isTeacher() { return this.$store.getters.isTeacher },
    username() { return this.$store.state.username },
    realName() { return this.$store.state.realName },
    roleLabel() {
      const map = { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }
      return map[this.$store.state.role] || ''
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/user/profile')
      } else if (command === 'logout') {
        this.$store.commit('LOGOUT')
        this.$message.success('已退出登录')
        this.$router.push('/login')
      }
    }
  }
}
</script>

<style scoped>
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid rgba(201,169,110,0.2);
}
.logo-collapse {
  gap: 0;
}
.logo-text {
  color: #e8e8e8;
  font-size: 15px;
  font-weight: 600;
  white-space: nowrap;
  letter-spacing: 2px;
}
.el-menu {
  border-right: none;
}

/* 学生端顶部导航栏 */
.student-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}
.top-navbar {
  background: #1a2233;
  height: 60px;
  flex-shrink: 0;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
  position: sticky;
  top: 0;
  z-index: 100;
}
.top-navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}
.top-navbar-left {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.top-navbar-brand {
  color: #e8e8e8;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 2px;
}
.top-navbar-nav {
  display: flex;
  gap: 8px;
}
.top-navbar-nav a {
  color: #8899aa;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
  font-weight: 500;
}
.top-navbar-nav a:hover {
  color: #e8e8e8;
  background: rgba(255,255,255,0.06);
}
.top-navbar-nav a.active {
  color: #C9A96E;
  background: rgba(201,169,110,0.1);
}
.top-navbar-right {
  display: flex;
  align-items: center;
}
.student-main {
  flex: 1;
  overflow-y: auto;
  background: #f5f6fa;
  padding: 24px;
}
</style>
