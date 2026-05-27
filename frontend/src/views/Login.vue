<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">线上教学资源平台</h2>
      <p class="login-subtitle">用户登录</p>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" placeholder="请输入密码" type="password" show-password @keyup.enter.native="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" :loading="loading" @click="handleLogin">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/auth'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: { username: '', password: '' },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (!valid) return
        this.loading = true
        login(this.loginForm).then(res => {
          this.$store.commit('SET_USER', res.data)
          this.$message.success('登录成功')
          this.$router.push('/home')
        }).finally(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #0f1419;
  background-image: radial-gradient(ellipse at 20% 50%, rgba(42,56,74,0.4) 0%, transparent 60%),
                    radial-gradient(ellipse at 80% 20%, rgba(42,56,74,0.3) 0%, transparent 50%);
  position: relative;
}
.login-card {
  width: 420px;
  padding: 48px 40px 40px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.5);
  border-top: 3px solid #C9A96E;
}
.login-title {
  text-align: center;
  font-size: 22px;
  color: #1a1a1a;
  margin-bottom: 6px;
  font-weight: 600;
  letter-spacing: 2px;
}
.login-subtitle {
  text-align: center;
  color: #999;
  margin-bottom: 32px;
  font-size: 13px;
  letter-spacing: 1px;
}
.login-footer {
  text-align: center;
  color: #999;
  font-size: 13px;
  margin-top: 8px;
}
.login-footer a {
  color: #C9A96E;
  text-decoration: none;
  font-weight: 500;
}
.login-footer a:hover {
  color: #b8956a;
}
</style>
