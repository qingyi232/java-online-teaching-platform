<template>
  <el-card>
    <div slot="header"><span style="font-weight: bold; font-size: 16px;">用户管理</span></div>
    <el-form :inline="true" size="small" style="margin-bottom: 16px;">
      <el-form-item>
        <el-input v-model="queryUsername" placeholder="搜索用户名/姓名" clearable @clear="loadData" />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryRole" placeholder="全部角色" clearable @change="loadData">
          <el-option label="管理员" value="ADMIN" />
          <el-option label="教师" value="TEACHER" />
          <el-option label="学生" value="STUDENT" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="users" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="真实姓名" width="120" />
      <el-table-column label="角色" width="100">
        <template slot-scope="scope">
          <el-tag :type="roleType(scope.row.role)" size="small">{{ roleText(scope.row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" show-overflow-tooltip />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          <el-switch :value="scope.row.status === 1" @change="handleStatusChange(scope.row)" active-color="#13ce66" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="160" />
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="handleResetPwd(scope.row)">重置密码</el-button>
          <el-button type="text" style="color: #F56C6C;" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 16px; text-align: right;"
      @current-change="handlePageChange"
      :current-page="page"
      :page-size="size"
      :total="total"
      layout="total, prev, pager, next, jumper" />
  </el-card>
</template>

<script>
import { getUserPage, updateStatus, deleteUser, resetPassword } from '@/api/user'

export default {
  name: 'UserManage',
  data() {
    return { users: [], page: 1, size: 10, total: 0, queryUsername: '', queryRole: '', loading: false }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      getUserPage({ page: this.page, size: this.size, username: this.queryUsername, role: this.queryRole }).then(res => {
        this.users = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    handlePageChange(page) { this.page = page; this.loadData() },
    handleStatusChange(row) {
      const newStatus = row.status === 1 ? 0 : 1
      updateStatus(row.id, { status: newStatus }).then(() => {
        this.$message.success('状态更新成功')
        this.loadData()
      }).catch(() => {})
    },
    handleResetPwd(row) {
      this.$confirm(`确定重置用户 "${row.username}" 的密码为123456？`, '提示', { type: 'warning' }).then(() => {
        resetPassword(row.id).then(() => { this.$message.success('密码已重置为123456') }).catch(() => {})
      }).catch(() => {})
    },
    handleDelete(row) {
      this.$confirm(`确定删除用户 "${row.username}"？`, '警告', { type: 'warning' }).then(() => {
        deleteUser(row.id).then(() => { this.$message.success('删除成功'); this.loadData() }).catch(() => {})
      }).catch(() => {})
    },
    roleText(r) { return { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }[r] || r },
    roleType(r) { return { ADMIN: 'danger', TEACHER: 'warning', STUDENT: '' }[r] || 'info' }
  }
}
</script>
