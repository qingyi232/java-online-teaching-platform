<template>
  <div v-loading="loading">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-weight: bold; font-size: 18px;">{{ resource.title }}</span>
            <el-button size="small" @click="$router.back()">返回</el-button>
          </div>
          <el-descriptions :column="2" border size="medium">
            <el-descriptions-item label="资源分类">{{ resource.categoryName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="文件类型">
              <el-tag size="small">{{ resource.fileType }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="上传者">{{ resource.uploadUserName }}</el-descriptions-item>
            <el-descriptions-item label="上传时间">{{ resource.createTime }}</el-descriptions-item>
            <el-descriptions-item label="文件大小">{{ formatSize(resource.fileSize) }}</el-descriptions-item>
            <el-descriptions-item label="原始文件名">{{ resource.fileName }}</el-descriptions-item>
            <el-descriptions-item label="浏览次数">{{ resource.viewCount }}</el-descriptions-item>
            <el-descriptions-item label="下载次数">{{ resource.downloadCount }}</el-descriptions-item>
          </el-descriptions>
          <div style="margin-top: 20px;">
            <h4>资源描述</h4>
            <p style="color: #666; line-height: 1.8; margin-top: 8px;">{{ resource.description || '暂无描述' }}</p>
          </div>
          <div style="margin-top: 20px;" v-if="isLoggedIn">
            <el-button type="primary" icon="el-icon-download" @click="handleDownload">下载资源</el-button>
            <el-button :type="isFavorited ? 'warning' : 'default'" :icon="isFavorited ? 'el-icon-star-on' : 'el-icon-star-off'" @click="toggleFavorite">
              {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
        </el-card>
        <el-card style="margin-top: 20px;">
          <div slot="header"><span style="font-weight: bold;">评论区</span></div>
          <div v-if="isLoggedIn" style="margin-bottom: 20px;">
            <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..." />
            <el-button type="primary" size="small" style="margin-top: 10px;" @click="submitComment" :disabled="!commentContent.trim()">发表评论</el-button>
          </div>
          <div v-if="comments.length === 0" style="text-align: center; color: #999; padding: 20px;">暂无评论</div>
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-header">
              <span class="comment-user">{{ comment.userName }}</span>
              <span class="comment-time">{{ comment.createTime }}</span>
              <el-button v-if="canDeleteComment(comment)" type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteComment(comment.id)">删除</el-button>
            </div>
            <p class="comment-content">{{ comment.content }}</p>
            <div v-if="comment.children && comment.children.length > 0" class="comment-replies">
              <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                <div class="comment-header">
                  <span class="comment-user">{{ reply.userName }}</span>
                  <span class="comment-time">{{ reply.createTime }}</span>
                </div>
                <p class="comment-content">{{ reply.content }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header"><span style="font-weight: bold;">相关资源</span></div>
          <div v-if="relatedResources.length === 0" style="text-align: center; color: #999; padding: 20px;">暂无相关资源</div>
          <div v-for="item in relatedResources" :key="item.id" class="related-item" @click="goToDetail(item.id)">
            <div class="related-title">{{ item.title }}</div>
            <div class="related-meta">
              <el-tag size="mini">{{ item.fileType }}</el-tag>
              <span>{{ item.uploadUserName }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getResourceDetail, getResourcePage, downloadResource } from '@/api/resource'
import { getComments, addComment, deleteComment, checkFavorite, addFavorite, removeFavorite } from '@/api/interaction'
import { recordView } from '@/api/resource'

export default {
  name: 'ResourceDetail',
  data() {
    return {
      resource: {},
      comments: [],
      relatedResources: [],
      commentContent: '',
      isFavorited: false,
      loading: false
    }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn },
    currentUserId() { return this.$store.state.userId },
    currentRole() { return this.$store.state.role }
  },
  created() {
    this.loadDetail()
  },
  watch: {
    '$route.params.id'() { this.loadDetail() }
  },
  methods: {
    loadDetail() {
      const id = this.$route.params.id
      this.loading = true
      getResourceDetail(id).then(res => {
        this.resource = res.data
        this.loadComments()
        this.loadRelated()
        if (this.isLoggedIn) {
          this.checkFavoriteStatus()
          recordView(id).catch(() => {})
        }
      }).finally(() => { this.loading = false })
    },
    loadComments() {
      getComments(this.$route.params.id).then(res => { this.comments = res.data }).catch(() => {})
    },
    loadRelated() {
      getResourcePage({ page: 1, size: 5, categoryId: this.resource.categoryId, status: 1 }).then(res => {
        this.relatedResources = res.data.records.filter(r => r.id !== this.resource.id).slice(0, 4)
      }).catch(() => {})
    },
    checkFavoriteStatus() {
      checkFavorite(this.$route.params.id).then(res => { this.isFavorited = res.data.favorited }).catch(() => {})
    },
    submitComment() {
      addComment({ resourceId: this.$route.params.id, content: this.commentContent, parentId: 0 }).then(() => {
        this.$message.success('评论成功')
        this.commentContent = ''
        this.loadComments()
      }).catch(() => {})
    },
    handleDeleteComment(id) {
      this.$confirm('确定删除该评论？', '提示', { type: 'warning' }).then(() => {
        deleteComment(id).then(() => {
          this.$message.success('删除成功')
          this.loadComments()
        })
      }).catch(() => {})
    },
    canDeleteComment(comment) {
      return this.currentRole === 'ADMIN' || String(comment.userId) === String(this.currentUserId)
    },
    handleDownload() {
      downloadResource(this.resource.id).then(res => {
        if (res.data && res.data.url) {
          window.open(res.data.url, '_blank')
        }
      }).catch(() => {})
    },
    toggleFavorite() {
      if (this.isFavorited) {
        removeFavorite(this.$route.params.id).then(() => {
          this.isFavorited = false
          this.$message.success('已取消收藏')
        }).catch(() => {})
      } else {
        addFavorite(this.$route.params.id).then(() => {
          this.isFavorited = true
          this.$message.success('收藏成功')
        }).catch(() => {})
      }
    },
    goToDetail(id) {
      this.$router.push('/resource/detail/' + id)
    },
    formatSize(bytes) {
      if (!bytes) return '-'
      if (bytes < 1024) return bytes + ' B'
      if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
      return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
    }
  }
}
</script>

<style scoped>
.comment-item { padding: 16px 0; border-bottom: 1px solid #eee; }
.comment-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.comment-user { font-weight: bold; color: #333; }
.comment-time { color: #999; font-size: 12px; }
.comment-content { color: #666; line-height: 1.6; }
.comment-replies { margin-left: 24px; margin-top: 12px; padding-left: 16px; border-left: 2px solid #e6e6e6; }
.reply-item { padding: 8px 0; }
.related-item { padding: 12px 0; border-bottom: 1px solid #f0f0f0; cursor: pointer; }
.related-item:hover { background: #f5f7fa; }
.related-title { font-size: 14px; color: #333; margin-bottom: 6px; }
.related-meta { display: flex; align-items: center; gap: 8px; font-size: 12px; color: #999; }
</style>
