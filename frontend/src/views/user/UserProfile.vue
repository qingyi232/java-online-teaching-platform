<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span style="font-weight: bold;">个人信息</span></div>
          <el-form ref="profileForm" :model="profileForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input :value="profileForm.username" disabled />
            </el-form-item>
            <el-form-item label="角色">
              <el-tag :type="roleType">{{ roleText }}</el-tag>
            </el-form-item>
            <el-form-item label="真实姓名">
              <el-input v-model="profileForm.realName" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdateProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><span style="font-weight: bold;">修改密码</span></div>
          <el-form ref="pwdForm" :model="pwdForm" :rules="pwdRules" label-width="80px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdatePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserInfo, updateUser, updatePassword } from '@/api/user'

export default {
  name: 'UserProfile',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.pwdForm.newPassword) { callback(new Error('两次输入的密码不一致')) } else { callback() }
    }
    return {
      profileForm: { username: '', realName: '', email: '', phone: '' },
      pwdForm: { oldPassword: '', newPassword: '', confirmPassword: '' },
      pwdRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '密码至少6个字符', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认新密码', trigger: 'blur' }, { validator: validateConfirm, trigger: 'blur' }]
      }
    }
  },
  computed: {
    roleText() { return { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }[this.$store.state.role] || '' },
    roleType() { return { ADMIN: 'danger', TEACHER: 'warning', STUDENT: '' }[this.$store.state.role] || 'info' }
  },
  created() {
    getUserInfo().then(res => {
      this.profileForm = { username: res.data.username, realName: res.data.realName, email: res.data.email, phone: res.data.phone }
    })
  },
  methods: {
    handleUpdateProfile() {
      updateUser(this.profileForm).then(() => {
        this.$message.success('信息更新成功')
        const updated = {
          token: this.$store.state.token,
          userId: this.$store.state.userId,
          username: this.profileForm.username,
          realName: this.profileForm.realName,
          role: this.$store.state.role,
          avatar: this.$store.state.avatar
        }
        this.$store.commit('SET_USER', updated)
      }).catch(() => {
        this.$message.error('信息更新失败')
      })
    },
    handleUpdatePassword() {
      this.$refs.pwdForm.validate(valid => {
        if (!valid) return
        updatePassword({ oldPassword: this.pwdForm.oldPassword, newPassword: this.pwdForm.newPassword }).then(() => {
          this.$message.success('密码修改成功，请重新登录')
          this.$store.commit('LOGOUT')
          this.$router.push('/login')
        }).catch(() => {
          this.$message.error('密码修改失败，请检查原密码是否正确')
        })
      })
    }
  }
}
</script>
