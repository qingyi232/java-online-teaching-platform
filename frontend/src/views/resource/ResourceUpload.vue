<template>
  <el-card>
    <div slot="header"><span style="font-weight: bold; font-size: 16px;">上传资源</span></div>
    <el-form ref="uploadForm" :model="form" :rules="rules" label-width="100px" style="max-width: 600px;">
      <el-form-item label="资源名称" prop="title">
        <el-input v-model="form.title" placeholder="请输入资源名称" />
      </el-form-item>
      <el-form-item label="资源分类" prop="categoryId">
        <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%;">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="资源描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入资源描述" />
      </el-form-item>
      <el-form-item label="选择文件" prop="file">
        <el-upload
          ref="upload"
          action=""
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :limit="1"
          :file-list="fileList"
          drag>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">支持PDF、PPT、Word、视频、图片等格式，单文件不超过500MB</div>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="uploading" @click="submitUpload">提交上传</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { uploadResource, getCategoryList } from '@/api/resource'

export default {
  name: 'ResourceUpload',
  data() {
    return {
      form: { title: '', categoryId: null, description: '' },
      rules: {
        title: [{ required: true, message: '请输入资源名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择资源分类', trigger: 'change' }]
      },
      categories: [],
      fileList: [],
      selectedFile: null,
      uploading: false
    }
  },
  created() {
    getCategoryList().then(res => { this.categories = res.data })
  },
  methods: {
    handleFileChange(file) {
      this.selectedFile = file.raw
    },
    handleFileRemove() {
      this.selectedFile = null
    },
    submitUpload() {
      this.$refs.uploadForm.validate(valid => {
        if (!valid) return
        if (!this.selectedFile) {
          this.$message.warning('请选择要上传的文件')
          return
        }
        this.uploading = true
        const formData = new FormData()
        formData.append('file', this.selectedFile)
        formData.append('title', this.form.title)
        formData.append('categoryId', this.form.categoryId)
        if (this.form.description) {
          formData.append('description', this.form.description)
        }
        uploadResource(formData).then(() => {
          this.$message.success('上传成功，等待审核')
          this.$router.push('/resource/my')
        }).finally(() => { this.uploading = false })
      })
    }
  }
}
</script>
