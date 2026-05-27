<template>
  <el-card>
    <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
      <span style="font-weight: bold; font-size: 16px;">问答中心</span>
      <el-button v-if="isLoggedIn" type="primary" size="small" icon="el-icon-plus" @click="showAddDialog = true">提问</el-button>
    </div>
    <el-form :inline="true" size="small" style="margin-bottom: 16px;">
      <el-form-item>
        <el-input v-model="queryTitle" placeholder="搜索问题" clearable @clear="loadData" />
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryStatus" placeholder="全部状态" clearable @change="loadData">
          <el-option label="未解决" :value="0" />
          <el-option label="已解决" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
      </el-form-item>
    </el-form>
    <div v-loading="loading">
      <div v-if="questions.length === 0" style="text-align: center; color: #999; padding: 40px;">暂无问题</div>
      <div v-for="q in questions" :key="q.id" class="question-item" @click="$router.push('/question/detail/' + q.id)">
        <div class="question-header">
          <el-tag :type="q.status === 1 ? 'success' : 'warning'" size="mini">{{ q.status === 1 ? '已解决' : '未解决' }}</el-tag>
          <span class="question-title">{{ q.title }}</span>
        </div>
        <p class="question-content">{{ q.content }}</p>
        <div class="question-meta">
          <span><i class="el-icon-user"></i> {{ q.userName }}</span>
          <span><i class="el-icon-chat-dot-round"></i> {{ q.replyCount || 0 }} 回复</span>
          <span v-if="q.resourceTitle"><i class="el-icon-link"></i> {{ q.resourceTitle }}</span>
          <span><i class="el-icon-time"></i> {{ q.createTime }}</span>
        </div>
      </div>
    </div>
    <el-pagination
      style="margin-top: 16px; text-align: right;"
      @current-change="handlePageChange"
      :current-page="page"
      :page-size="size"
      :total="total"
      layout="total, prev, pager, next" />

    <el-dialog title="提问" :visible.sync="showAddDialog" width="600px">
      <el-form ref="questionForm" :model="questionForm" :rules="questionRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="questionForm.title" placeholder="请输入问题标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="questionForm.content" type="textarea" :rows="5" placeholder="请详细描述你的问题" />
        </el-form-item>
        <el-form-item label="关联资源">
          <el-select v-model="questionForm.resourceId" placeholder="选择关联资源（选填）" clearable filterable style="width: 100%;">
            <el-option v-for="r in resourceOptions" :key="r.id" :label="r.title" :value="r.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitQuestion">提交</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getQuestionPage, addQuestion } from '@/api/interaction'
import { getResourcePage } from '@/api/resource'

export default {
  name: 'QuestionList',
  data() {
    return {
      questions: [],
      page: 1,
      size: 10,
      total: 0,
      queryTitle: '',
      queryStatus: null,
      loading: false,
      showAddDialog: false,
      questionForm: { title: '', content: '', resourceId: null },
      questionRules: {
        title: [{ required: true, message: '请输入问题标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入问题内容', trigger: 'blur' }]
      },
      resourceOptions: []
    }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn }
  },
  created() {
    this.loadData()
    if (this.isLoggedIn) {
      getResourcePage({ page: 1, size: 100, status: 1 }).then(res => { this.resourceOptions = res.data.records })
    }
  },
  methods: {
    loadData() {
      this.loading = true
      getQuestionPage({ page: this.page, size: this.size, title: this.queryTitle, status: this.queryStatus }).then(res => {
        this.questions = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    handlePageChange(page) { this.page = page; this.loadData() },
    submitQuestion() {
      this.$refs.questionForm.validate(valid => {
        if (!valid) return
        addQuestion(this.questionForm).then(() => {
          this.$message.success('提问成功')
          this.showAddDialog = false
          this.questionForm = { title: '', content: '', resourceId: null }
          this.loadData()
        }).catch(() => {})
      })
    }
  }
}
</script>

<style scoped>
.question-item { padding: 16px; border: 1px solid #ebeef5; border-radius: 8px; margin-bottom: 12px; cursor: pointer; transition: box-shadow 0.2s; }
.question-item:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.1); }
.question-header { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; }
.question-title { font-size: 16px; font-weight: bold; color: #333; }
.question-content { color: #666; font-size: 14px; line-height: 1.6; margin-bottom: 10px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.question-meta { display: flex; gap: 20px; color: #999; font-size: 13px; }
.question-meta i { margin-right: 4px; }
</style>
