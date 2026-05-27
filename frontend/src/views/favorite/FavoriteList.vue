<template>
  <el-card>
    <div slot="header"><span style="font-weight: bold; font-size: 16px;">我的收藏</span></div>
    <el-table :data="favorites" v-loading="loading" style="width: 100%">
      <el-table-column prop="resourceTitle" label="资源名称" show-overflow-tooltip min-width="300" />
      <el-table-column prop="createTime" label="收藏时间" width="180" />
      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button type="text" @click="$router.push('/resource/detail/' + scope.row.resourceId)">查看</el-button>
          <el-button type="text" style="color: #F56C6C;" @click="handleRemove(scope.row)">取消收藏</el-button>
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
import { getFavoriteList, removeFavorite } from '@/api/interaction'

export default {
  name: 'FavoriteList',
  data() {
    return { favorites: [], page: 1, size: 10, total: 0, loading: false }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      getFavoriteList({ page: this.page, size: this.size }).then(res => {
        this.favorites = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    handlePageChange(page) { this.page = page; this.loadData() },
    handleRemove(row) {
      this.$confirm('确定取消收藏？', '提示', { type: 'warning' }).then(() => {
        removeFavorite(row.resourceId).then(() => {
          this.$message.success('已取消收藏')
          this.loadData()
        }).catch(() => {})
      }).catch(() => {})
    }
  }
}
</script>
