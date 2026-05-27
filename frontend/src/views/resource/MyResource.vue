<template>
  <el-card>
    <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
      <span style="font-weight: bold; font-size: 16px;">我的资源</span>
      <el-button type="primary" size="small" icon="el-icon-upload2" @click="$router.push('/resource/upload')">上传资源</el-button>
    </div>
    <el-table :data="resources" v-loading="loading" style="width: 100%">
      <el-table-column prop="title" label="资源名称" show-overflow-tooltip min-width="200" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="fileType" label="类型" width="80" />
      <el-table-column prop="viewCount" label="浏览" width="70" />
      <el-table-column prop="downloadCount" label="下载" width="70" />
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)" size="small">{{ statusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="上传时间" width="160" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="$router.push('/resource/detail/' + scope.row.id)">查看</el-button>
          <el-button type="text" @click="showEditDialog(scope.row)">编辑</el-button>
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
      layout="total, prev, pager, next" />

    <el-dialog :title="'编辑资源'" :visible.sync="editDialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="editRules" label-width="80px">
        <el-form-item label="资源名称" prop="title">
          <el-input v-model="editForm.title" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源分类" prop="categoryId">
          <el-select v-model="editForm.categoryId" placeholder="请选择分类" style="width: 100%;">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="资源描述">
          <el-input v-model="editForm.description" type="textarea" :rows="4" placeholder="请输入资源描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="editLoading" @click="handleEdit">保存</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getResourcePage, deleteResource, updateResource, getCategoryList } from '@/api/resource'

export default {
  name: 'MyResource',
  data() {
    return {
      resources: [], page: 1, size: 10, total: 0, loading: false,
      editDialogVisible: false,
      editLoading: false,
      editForm: { id: null, title: '', categoryId: null, description: '' },
      editRules: {
        title: [{ required: true, message: '请输入资源名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
      },
      categories: []
    }
  },
  created() {
    this.loadData()
    getCategoryList().then(res => { this.categories = res.data }).catch(() => {})
  },
  methods: {
    loadData() {
      this.loading = true
      const userId = this.$store.state.userId
      getResourcePage({ page: this.page, size: this.size, uploadUserId: userId }).then(res => {
        this.resources = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    handlePageChange(page) { this.page = page; this.loadData() },
    handleDelete(row) {
      this.$confirm('确定删除该资源？删除后不可恢复', '警告', { type: 'warning' }).then(() => {
        deleteResource(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(() => {})
      }).catch(() => {})
    },
    showEditDialog(row) {
      this.editForm = { id: row.id, title: row.title, categoryId: row.categoryId, description: row.description || '' }
      this.editDialogVisible = true
    },
    handleEdit() {
      this.$refs.editForm.validate(valid => {
        if (!valid) return
        this.editLoading = true
        updateResource(this.editForm).then(() => {
          this.$message.success('更新成功')
          this.editDialogVisible = false
          this.loadData()
        }).finally(() => { this.editLoading = false })
      })
    },
    statusText(s) { return ['待审核', '已通过', '已拒绝'][s] },
    statusType(s) { return ['warning', 'success', 'danger'][s] }
  }
}
</script>
