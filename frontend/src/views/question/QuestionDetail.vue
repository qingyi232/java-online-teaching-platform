<template>
  <div v-loading="loading">
    <el-card>
      <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
        <div>
          <el-tag :type="question.status === 1 ? 'success' : 'warning'" size="small" style="margin-right: 10px;">{{ question.status === 1 ? '已解决' : '未解决' }}</el-tag>
          <span style="font-weight: bold; font-size: 18px;">{{ question.title }}</span>
        </div>
        <div>
          <el-button v-if="canSolve" type="success" size="small" @click="handleSolve">标记已解决</el-button>
          <el-button size="small" @click="$router.back()">返回</el-button>
        </div>
      </div>
      <div class="question-info">
        <span><i class="el-icon-user"></i> {{ question.userName }}</span>
        <span><i class="el-icon-time"></i> {{ question.createTime }}</span>
        <span v-if="question.resourceTitle"><i class="el-icon-link"></i> 关联资源：{{ question.resourceTitle }}</span>
      </div>
      <div class="question-body">{{ question.content }}</div>
    </el-card>

    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span style="font-weight: bold;">回复 ({{ replies.length }})</span>
      </div>
      <div v-if="isLoggedIn" style="margin-bottom: 20px;">
        <el-input v-model="replyContent" type="textarea" :rows="3" placeholder="写下你的回复..." />
        <el-button type="primary" size="small" style="margin-top: 10px;" @click="submitReply" :disabled="!replyContent.trim()">发表回复</el-button>
      </div>
      <div v-if="replies.length === 0" style="text-align: center; color: #999; padding: 20px;">暂无回复</div>
      <div v-for="reply in replies" :key="reply.id" class="reply-item">
        <div class="reply-header">
          <span class="reply-user">{{ reply.userName }}</span>
          <span class="reply-time">{{ reply.createTime }}</span>
          <el-button v-if="canDeleteReply(reply)" type="text" size="mini" style="color: #F56C6C;" @click="handleDeleteReply(reply.id)">删除</el-button>
        </div>
        <p class="reply-content">{{ reply.content }}</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getQuestionDetail, getReplies, addReply, deleteReply, solveQuestion } from '@/api/interaction'

export default {
  name: 'QuestionDetail',
  data() {
    return { question: {}, replies: [], replyContent: '', loading: false }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn },
    currentUserId() { return this.$store.state.userId },
    currentRole() { return this.$store.state.role },
    canSolve() {
      return this.question.status === 0 && (
        this.currentRole === 'ADMIN' || this.currentRole === 'TEACHER' ||
        String(this.question.userId) === String(this.currentUserId)
      )
    }
  },
  created() { this.loadDetail() },
  methods: {
    loadDetail() {
      this.loading = true
      const id = this.$route.params.id
      getQuestionDetail(id).then(res => { this.question = res.data }).finally(() => { this.loading = false })
      this.loadReplies()
    },
    loadReplies() {
      getReplies(this.$route.params.id).then(res => { this.replies = res.data }).catch(() => {})
    },
    submitReply() {
      addReply({ questionId: this.$route.params.id, content: this.replyContent }).then(() => {
        this.$message.success('回复成功')
        this.replyContent = ''
        this.loadReplies()
      }).catch(() => {})
    },
    handleDeleteReply(id) {
      this.$confirm('确定删除该回复？', '提示', { type: 'warning' }).then(() => {
        deleteReply(id).then(() => {
          this.$message.success('删除成功')
          this.loadReplies()
        })
      }).catch(() => {})
    },
    handleSolve() {
      this.$confirm('确定标记为已解决？', '提示', { type: 'info' }).then(() => {
        solveQuestion(this.question.id).then(() => {
          this.$message.success('已标记为已解决')
          this.loadDetail()
        })
      }).catch(() => {})
    },
    canDeleteReply(reply) {
      return this.currentRole === 'ADMIN' || this.currentRole === 'TEACHER' || String(reply.userId) === String(this.currentUserId)
    }
  }
}
</script>

<style scoped>
.question-info { display: flex; gap: 20px; color: #999; font-size: 14px; margin-bottom: 16px; }
.question-info i { margin-right: 4px; }
.question-body { color: #333; line-height: 1.8; font-size: 15px; padding: 16px; background: #f9f9f9; border-radius: 8px; }
.reply-item { padding: 16px 0; border-bottom: 1px solid #eee; }
.reply-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.reply-user { font-weight: bold; color: #333; }
.reply-time { color: #999; font-size: 12px; }
.reply-content { color: #666; line-height: 1.6; }
</style>
