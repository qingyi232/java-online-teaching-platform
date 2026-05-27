<template>
  <el-card>
    <div slot="header"><span style="font-weight: bold; font-size: 16px;">资源审核</span></div>
    <el-form :inline="true" size="small" style="margin-bottom: 16px;">
      <el-form-item label="审核状态">
        <el-select v-model="queryStatus" placeholder="全部" clearable @change="loadData">
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已拒绝" :value="2" />
        </el-select>
      </el-form-item>
    </el-form>
    <el-table :data="resources" v-loading="loading" style="width: 100%">
      <el-table-column prop="title" label="资源名称" show-overflow-tooltip min-width="200" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="fileType" label="类型" width="80" />
      <el-table-column prop="uploadUserName" label="上传者" width="100" />
      <el-table-column prop="createTime" label="上传时间" width="160" />
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)" size="small">{{ statusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" @click="$router.push('/resource/detail/' + scope.row.id)">查看</el-button>
          <template v-if="scope.row.status === 0">
            <el-button type="text" style="color: #67C23A;" @click="handleAudit(scope.row.id, 1)">通过</el-button>
            <el-button type="text" style="color: #F56C6C;" @click="handleAudit(scope.row.id, 2)">拒绝</el-button>
          </template>
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
  </el-card>
</template>

<script>
import { getResourcePage, auditResource } from '@/api/resource'

export default {
  name: 'ResourceAudit',
  data() {
    return {
      resources: [],
      page: 1,
      size: 10,
      total: 0,
      queryStatus: 0,
      loading: false
    }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      const params = { page: this.page, size: this.size }
      if (this.queryStatus !== '' && this.queryStatus !== null && this.queryStatus !== undefined) {
        params.status = this.queryStatus
      }
      getResourcePage(params).then(res => {
        this.resources = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    handlePageChange(page) { this.page = page; this.loadData() },
    handleAudit(id, status) {
      const action = status === 1 ? '通过' : '拒绝'
      this.$confirm(`确定${action}该资源？`, '审核确认', { type: 'warning' }).then(() => {
        auditResource(id, { status }).then(() => {
          this.$message.success(`已${action}`)
          this.loadData()
        }).catch(() => {})
      }).catch(() => {})
    },
    statusText(s) { return ['待审核', '已通过', '已拒绝'][s] },
    statusType(s) { return ['warning', 'success', 'danger'][s] }
  }
}
</script>
