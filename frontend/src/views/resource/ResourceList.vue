<template>
  <div>
    <el-card>
      <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-weight: bold; font-size: 16px;">资源列表</span>
        <el-button v-if="isLoggedIn" type="primary" size="small" icon="el-icon-upload2" @click="$router.push('/resource/upload')">上传资源</el-button>
      </div>
      <el-form :inline="true" :model="queryForm" size="small" style="margin-bottom: 16px;">
        <el-form-item label="关键词">
          <el-input v-model="queryForm.title" placeholder="搜索资源名称" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryForm.categoryId" placeholder="全部分类" clearable @change="loadData">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="queryForm.fileType" placeholder="全部类型" clearable @change="loadData">
            <el-option label="PDF" value="pdf" />
            <el-option label="PPT" value="pptx" />
            <el-option label="Word" value="docx" />
            <el-option label="视频" value="mp4" />
            <el-option label="图片" value="png" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="resources" v-loading="loading" style="width: 100%">
        <el-table-column prop="title" label="资源名称" show-overflow-tooltip min-width="200" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="fileType" label="类型" width="80">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getTypeTag(scope.row.fileType)">{{ scope.row.fileType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploadUserName" label="上传者" width="100" />
        <el-table-column prop="viewCount" label="浏览" width="70" />
        <el-table-column prop="downloadCount" label="下载" width="70" />
        <el-table-column prop="createTime" label="上传时间" width="160" />
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="$router.push('/resource/detail/' + scope.row.id)">查看</el-button>
            <el-button v-if="isLoggedIn" type="text" @click="handleDownload(scope.row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 16px; text-align: right;"
        @current-change="handlePageChange"
        :current-page="queryForm.page"
        :page-size="queryForm.size"
        :total="total"
        layout="total, prev, pager, next, jumper" />
    </el-card>
  </div>
</template>

<script>
import { getResourcePage, downloadResource, getCategoryList } from '@/api/resource'

export default {
  name: 'ResourceList',
  data() {
    return {
      queryForm: { page: 1, size: 10, title: '', categoryId: null, fileType: '', status: 1 },
      resources: [],
      categories: [],
      total: 0,
      loading: false
    }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn }
  },
  created() {
    if (this.$route.query.title) {
      this.queryForm.title = this.$route.query.title
    }
    if (this.$route.query.categoryId) {
      this.queryForm.categoryId = Number(this.$route.query.categoryId)
    }
    this.loadData()
    this.loadCategories()
  },
  methods: {
    loadData() {
      this.loading = true
      getResourcePage(this.queryForm).then(res => {
        this.resources = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    loadCategories() {
      getCategoryList().then(res => { this.categories = res.data })
    },
    handlePageChange(page) {
      this.queryForm.page = page
      this.loadData()
    },
    resetQuery() {
      this.queryForm = { page: 1, size: 10, title: '', categoryId: null, fileType: '', status: 1 }
      this.loadData()
    },
    handleDownload(row) {
      downloadResource(row.id).then(res => {
        if (res.data && res.data.url) {
          window.open(res.data.url, '_blank')
          this.$message.success('开始下载')
        }
      }).catch(() => {})
    },
    getTypeTag(type) {
      const map = { pdf: '', pptx: 'warning', ppt: 'warning', docx: 'primary', doc: 'primary', mp4: 'danger', png: 'success', jpg: 'success' }
      return map[type] || 'info'
    }
  }
}
</script>
