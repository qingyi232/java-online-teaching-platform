<template>
  <el-card>
    <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
      <span style="font-weight: bold; font-size: 16px;">分类管理</span>
      <el-button type="primary" size="small" icon="el-icon-plus" @click="showDialog('add')">新增分类</el-button>
    </div>
    <el-table :data="categories" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="sort" label="排序" width="100" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button type="text" @click="showDialog('edit', scope.row)">编辑</el-button>
          <el-button type="text" style="color: #F56C6C;" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogType === 'add' ? '新增分类' : '编辑分类'" :visible.sync="dialogVisible" width="400px">
      <el-form ref="catForm" :model="catForm" :rules="catRules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="catForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="catForm.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getCategoryList, addCategory, updateCategory, deleteCategory } from '@/api/resource'

export default {
  name: 'CategoryManage',
  data() {
    return {
      categories: [],
      loading: false,
      dialogVisible: false,
      dialogType: 'add',
      catForm: { name: '', sort: 0 },
      catRules: { name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }] }
    }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      getCategoryList().then(res => { this.categories = res.data }).finally(() => { this.loading = false })
    },
    showDialog(type, row) {
      this.dialogType = type
      this.catForm = type === 'edit' ? { ...row } : { name: '', sort: 0 }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.catForm.validate(valid => {
        if (!valid) return
        const fn = this.dialogType === 'add' ? addCategory : updateCategory
        fn(this.catForm).then(() => {
          this.$message.success(this.dialogType === 'add' ? '添加成功' : '更新成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(() => {})
      })
    },
    handleDelete(row) {
      this.$confirm(`确定删除分类 "${row.name}"？`, '警告', { type: 'warning' }).then(() => {
        deleteCategory(row.id).then(() => { this.$message.success('删除成功'); this.loadData() }).catch(() => {})
      }).catch(() => {})
    }
  }
}
</script>
