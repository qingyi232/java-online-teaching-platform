<template>
  <div class="register-container">
    <div class="register-card">
      <h2 class="register-title">线上教学资源平台</h2>
      <p class="register-subtitle">用户注册</p>
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" prefix-icon="el-icon-lock" placeholder="请输入密码" type="password" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" prefix-icon="el-icon-lock" placeholder="请确认密码" type="password" show-password />
        </el-form-item>
        <el-form-item prop="realName">
          <el-input v-model="registerForm.realName" prefix-icon="el-icon-postcard" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="学生" value="STUDENT" />
            <el-option label="教师" value="TEACHER" />
          </el-select>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="registerForm.email" prefix-icon="el-icon-message" placeholder="请输入邮箱（选填）" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" :loading="loading" @click="handleRegister">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="register-footer">
        已有账号？<router-link to="/login">返回登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { register } from '@/api/auth'

export default {
  name: 'Register',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      registerForm: { username: '', password: '', confirmPassword: '', realName: '', role: 'STUDENT', email: '' },
      registerRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }, { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6个字符', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirmPassword, trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      },
      loading: false
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (!valid) return
        this.loading = true
        const data = { ...this.registerForm }
        delete data.confirmPassword
        register(data).then(() => {
          this.$message.success('注册成功，请登录')
          this.$router.push('/login')
        }).finally(() => {
          this.loading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #0f1419;
  background-image: radial-gradient(ellipse at 20% 50%, rgba(42,56,74,0.4) 0%, transparent 60%),
                    radial-gradient(ellipse at 80% 20%, rgba(42,56,74,0.3) 0%, transparent 50%);
}
.register-card {
  width: 440px;
  padding: 44px 40px 36px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.5);
  border-top: 3px solid #C9A96E;
}
.register-title {
  text-align: center;
  font-size: 22px;
  color: #1a1a1a;
  margin-bottom: 6px;
  font-weight: 600;
  letter-spacing: 2px;
}
.register-subtitle {
  text-align: center;
  color: #999;
  margin-bottom: 28px;
  font-size: 13px;
  letter-spacing: 1px;
}
.register-footer {
  text-align: center;
  color: #999;
  font-size: 13px;
  margin-top: 4px;
}
.register-footer a {
  color: #C9A96E;
  text-decoration: none;
  font-weight: 500;
}
.register-footer a:hover {
  color: #b8956a;
}
</style>
