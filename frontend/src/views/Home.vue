<template>
  <div class="home-container">
    <!-- 管理员视图 -->
    <template v-if="isAdmin">
      <div class="welcome-banner">
        <h1>管理控制台</h1>
        <p>平台运营数据总览，管理用户、资源与分类</p>
      </div>
      <el-row :gutter="20" class="stat-cards">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #2C3E50;"><i class="el-icon-user"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalUsers || 0 }}</p>
              <p class="stat-label">用户总数</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #3D8B7A;"><i class="el-icon-folder-opened"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalResources || 0 }}</p>
              <p class="stat-label">资源总数</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #C9A96E;"><i class="el-icon-view"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalViews || 0 }}</p>
              <p class="stat-label">浏览总量</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #C0625A;"><i class="el-icon-download"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalDownloads || 0 }}</p>
              <p class="stat-label">下载总量</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="16">
          <el-card>
            <div slot="header"><span style="font-weight: bold;">最新资源</span></div>
            <el-table :data="latestResources" style="width: 100%">
              <el-table-column prop="title" label="资源名称" show-overflow-tooltip />
              <el-table-column prop="categoryName" label="分类" width="100" />
              <el-table-column prop="fileType" label="类型" width="80" />
              <el-table-column prop="uploadUserName" label="上传者" width="100" />
              <el-table-column prop="createTime" label="上传时间" width="160" />
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" @click="$router.push('/resource/detail/' + scope.row.id)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header"><span style="font-weight: bold;">管理操作</span></div>
            <div class="quick-actions">
              <el-button icon="el-icon-user" @click="$router.push('/user/manage')">用户管理</el-button>
              <el-button icon="el-icon-menu" @click="$router.push('/category/manage')">分类管理</el-button>
              <el-button icon="el-icon-s-check" @click="$router.push('/resource/audit')">资源审核</el-button>
              <el-button icon="el-icon-data-analysis" @click="$router.push('/statistics')">数据统计</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 教师视图 -->
    <template v-else-if="isTeacher">
      <div class="welcome-banner welcome-teacher">
        <h1>教师工作台</h1>
        <p>上传教学资源、审核学生提交、解答学生疑问</p>
      </div>
      <el-row :gutter="20" class="stat-cards">
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #3D8B7A;"><i class="el-icon-folder-opened"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalResources || 0 }}</p>
              <p class="stat-label">平台资源</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #C9A96E;"><i class="el-icon-view"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalViews || 0 }}</p>
              <p class="stat-label">总浏览量</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-icon" style="background: #C0625A;"><i class="el-icon-download"></i></div>
            <div class="stat-info">
              <p class="stat-number">{{ stats.totalDownloads || 0 }}</p>
              <p class="stat-label">总下载量</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="16">
          <el-card>
            <div slot="header"><span style="font-weight: bold;">最新资源</span></div>
            <el-table :data="latestResources" style="width: 100%">
              <el-table-column prop="title" label="资源名称" show-overflow-tooltip />
              <el-table-column prop="categoryName" label="分类" width="100" />
              <el-table-column prop="fileType" label="类型" width="80" />
              <el-table-column prop="uploadUserName" label="上传者" width="100" />
              <el-table-column prop="createTime" label="上传时间" width="160" />
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" @click="$router.push('/resource/detail/' + scope.row.id)">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div slot="header"><span style="font-weight: bold;">教学操作</span></div>
            <div class="quick-actions">
              <el-button type="primary" icon="el-icon-upload2" @click="$router.push('/resource/upload')">上传资源</el-button>
              <el-button icon="el-icon-s-check" @click="$router.push('/resource/audit')">资源审核</el-button>
              <el-button icon="el-icon-chat-dot-round" @click="$router.push('/question')">答疑中心</el-button>
              <el-button icon="el-icon-data-analysis" @click="$router.push('/statistics')">数据统计</el-button>
            </div>
          </el-card>
          <el-card style="margin-top: 20px;">
            <div slot="header"><span style="font-weight: bold;">教学提示</span></div>
            <div class="announcement">
              <p>上传的资源需经审核后方可对学生可见。</p>
              <p>请及时关注问答中心的学生提问并给予解答。</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 学生视图（门户官网风格） -->
    <template v-else>
      <!-- Hero 搜索区 -->
      <div class="portal-hero">
        <div class="portal-hero-content">
          <h1 class="portal-title">线上教学资源平台</h1>
          <p class="portal-desc">海量优质教学资源，助力高效学习</p>
          <div class="portal-search">
            <el-input v-model="searchKeyword" placeholder="搜索课件、文档、视频等教学资源..." clearable class="portal-search-input" @keyup.enter.native="handleSearch">
              <el-button slot="append" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </el-input>
          </div>
          <div class="portal-stats-bar">
            <span><strong>{{ portalStats.totalResources || 0 }}</strong> 份资源</span>
            <span class="portal-stats-dot"></span>
            <span><strong>{{ portalStats.totalViews || 0 }}</strong> 次浏览</span>
            <span class="portal-stats-dot"></span>
            <span><strong>{{ portalStats.totalDownloads || 0 }}</strong> 次下载</span>
          </div>
        </div>
      </div>

      <!-- 分类导航 -->
      <div class="portal-section">
        <div class="portal-section-header">
          <h2>资源分类</h2>
          <el-button type="text" @click="$router.push('/resource')">查看全部 <i class="el-icon-arrow-right"></i></el-button>
        </div>
        <el-row :gutter="16">
          <el-col :span="4" v-for="cat in categories" :key="cat.id">
            <div class="cat-card" @click="$router.push('/resource?categoryId=' + cat.id)">
              <div class="cat-icon" :style="{ background: catColors[cat.id % catColors.length] }">
                <i :class="catIcons[cat.name] || 'el-icon-folder'"></i>
              </div>
              <span class="cat-name">{{ cat.name }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 推荐资源 -->
      <div class="portal-section">
        <div class="portal-section-header">
          <h2>推荐资源</h2>
          <el-button type="text" @click="$router.push('/resource')">更多资源 <i class="el-icon-arrow-right"></i></el-button>
        </div>
        <el-row :gutter="20">
          <el-col :span="6" v-for="res in latestResources" :key="res.id">
            <el-card shadow="hover" class="res-card" @click.native="$router.push('/resource/detail/' + res.id)">
              <div class="res-card-type" :style="{ background: typeColors[res.fileType] || '#8B9DAF' }">
                {{ (res.fileType || '').toUpperCase() }}
              </div>
              <h4 class="res-card-title">{{ res.title }}</h4>
              <div class="res-card-meta">
                <span><i class="el-icon-user"></i> {{ res.uploadUserName }}</span>
                <span><i class="el-icon-time"></i> {{ (res.createTime || '').substring(0, 10) }}</span>
              </div>
              <div class="res-card-footer">
                <span><i class="el-icon-view"></i> {{ res.viewCount || 0 }}</span>
                <span><i class="el-icon-download"></i> {{ res.downloadCount || 0 }}</span>
                <el-tag size="mini" v-if="res.categoryName">{{ res.categoryName }}</el-tag>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 功能入口 -->
      <div class="portal-section">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="feature-card" @click="$router.push('/resource')">
              <div class="feature-icon" style="background: #2C3E50;"><i class="el-icon-search"></i></div>
              <div class="feature-text">
                <h3>浏览资源</h3>
                <p>海量教学资源等你发现</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="feature-card" @click="$router.push('/question')">
              <div class="feature-icon" style="background: #3D8B7A;"><i class="el-icon-chat-dot-round"></i></div>
              <div class="feature-text">
                <h3>问答互动</h3>
                <p>有问题？老师来解答</p>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="feature-card" @click="$router.push(isLoggedIn ? '/favorite' : '/login')">
              <div class="feature-icon" style="background: #C9A96E;"><i class="el-icon-star-off"></i></div>
              <div class="feature-text">
                <h3>我的收藏</h3>
                <p>收藏喜欢的学习资源</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </template>
  </div>
</template>

<script>
import { getDashboardStats } from '@/api/statistics'
import { getResourcePage, getCategoryList } from '@/api/resource'

export default {
  name: 'Home',
  data() {
    return {
      stats: {},
      portalStats: {},
      latestResources: [],
      categories: [],
      searchKeyword: '',
      catColors: ['#2C3E50', '#3D8B7A', '#C9A96E', '#C0625A', '#5B7B8A', '#8B6E5A'],
      catIcons: { '课件': 'el-icon-s-management', '视频': 'el-icon-video-camera', '文档': 'el-icon-document', '试题': 'el-icon-edit-outline', '其他': 'el-icon-more' },
      typeColors: { pdf: '#C0625A', ppt: '#C9A96E', doc: '#3D8B7A', mp4: '#2C3E50', zip: '#5B7B8A' }
    }
  },
  computed: {
    isLoggedIn() { return this.$store.getters.isLoggedIn },
    isAdmin() { return this.$store.state.role === 'ADMIN' },
    isTeacher() { return this.$store.state.role === 'TEACHER' }
  },
  created() {
    this.loadLatestResources()
    if (this.isAdmin || this.isTeacher) {
      this.loadStats()
    }
    if (!this.isAdmin && !this.isTeacher) {
      this.loadCategories()
      this.loadPortalStats()
    }
  },
  methods: {
    loadStats() {
      getDashboardStats().then(res => {
        this.stats = res.data
      }).catch(() => {})
    },
    loadPortalStats() {
      getDashboardStats().then(res => {
        this.portalStats = res.data
      }).catch(() => {})
    },
    loadLatestResources() {
      getResourcePage({ page: 1, size: 8, status: 1 }).then(res => {
        this.latestResources = res.data.records
      }).catch(() => {})
    },
    loadCategories() {
      getCategoryList().then(res => {
        this.categories = res.data
      }).catch(() => {})
    },
    handleSearch() {
      this.$router.push({ path: '/resource', query: { title: this.searchKeyword } })
    }
  }
}
</script>

<style scoped>
.welcome-banner {
  background: linear-gradient(135deg, #1a2233 0%, #2C3E50 100%);
  color: #fff;
  padding: 44px 40px;
  border-radius: 12px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
}
.welcome-banner::after {
  content: '';
  position: absolute;
  top: 0; right: 0;
  width: 200px; height: 100%;
  background: linear-gradient(135deg, transparent 40%, rgba(201,169,110,0.15) 100%);
  pointer-events: none;
}
.welcome-banner h1 { font-size: 26px; margin-bottom: 10px; font-weight: 600; letter-spacing: 1px; }
.welcome-banner p { font-size: 15px; opacity: 0.75; letter-spacing: 0.5px; }
.welcome-teacher { background: linear-gradient(135deg, #1a3a2a 0%, #2C5040 100%); }
.welcome-teacher::after { background: linear-gradient(135deg, transparent 40%, rgba(61,139,122,0.15) 100%); }

/* 门户官网风格 */
.portal-hero {
  background: linear-gradient(135deg, #1a2233 0%, #2C3E50 60%, #3D5167 100%);
  border-radius: 16px;
  padding: 60px 40px 50px;
  text-align: center;
  position: relative;
  overflow: hidden;
  margin-bottom: 28px;
}
.portal-hero::before {
  content: '';
  position: absolute;
  top: -50%; left: -50%;
  width: 200%; height: 200%;
  background: radial-gradient(circle at 30% 50%, rgba(201,169,110,0.08) 0%, transparent 50%),
              radial-gradient(circle at 70% 30%, rgba(61,139,122,0.06) 0%, transparent 40%);
  pointer-events: none;
}
.portal-title { font-size: 32px; color: #fff; font-weight: 700; letter-spacing: 3px; margin-bottom: 10px; position: relative; }
.portal-desc { font-size: 16px; color: rgba(255,255,255,0.65); letter-spacing: 1px; margin-bottom: 28px; position: relative; }
.portal-search { max-width: 560px; margin: 0 auto; position: relative; }
.portal-search .el-input__inner { height: 46px; border-radius: 8px; font-size: 15px; border: none; padding-left: 20px; }
.portal-search .el-input-group__append { background: #C9A96E; border: none; color: #fff; border-radius: 0 8px 8px 0; }
.portal-search .el-input-group__append .el-button { color: #fff; }
.portal-stats-bar { margin-top: 24px; color: rgba(255,255,255,0.6); font-size: 14px; position: relative; }
.portal-stats-bar strong { color: #C9A96E; font-size: 18px; }
.portal-stats-dot { display: inline-block; width: 4px; height: 4px; background: rgba(255,255,255,0.3); border-radius: 50%; margin: 0 16px; vertical-align: middle; }

.portal-section { margin-bottom: 28px; }
.portal-section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.portal-section-header h2 { font-size: 20px; color: #1a1a1a; font-weight: 600; position: relative; padding-left: 14px; }
.portal-section-header h2::before { content: ''; position: absolute; left: 0; top: 3px; width: 4px; height: 20px; background: #C9A96E; border-radius: 2px; }

.cat-card { display: flex; flex-direction: column; align-items: center; padding: 20px 10px; background: #fff; border-radius: 12px; cursor: pointer; transition: all 0.25s; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.cat-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.1); }
.cat-icon { width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-bottom: 10px; }
.cat-icon i { font-size: 22px; color: #fff; }
.cat-name { font-size: 13px; color: #333; font-weight: 500; }

.res-card { cursor: pointer; border-radius: 12px; transition: all 0.25s; border: none; }
.res-card:hover { transform: translateY(-4px); box-shadow: 0 12px 32px rgba(0,0,0,0.1); }
.res-card .el-card__body { padding: 20px; }
.res-card-type { display: inline-block; padding: 3px 10px; border-radius: 4px; color: #fff; font-size: 11px; font-weight: 700; letter-spacing: 1px; margin-bottom: 12px; }
.res-card-title { font-size: 15px; color: #1a1a1a; font-weight: 600; margin-bottom: 10px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; line-height: 1.4; }
.res-card-meta { font-size: 12px; color: #999; margin-bottom: 10px; display: flex; gap: 14px; }
.res-card-meta i { margin-right: 3px; }
.res-card-footer { display: flex; align-items: center; gap: 14px; font-size: 12px; color: #aaa; }
.res-card-footer i { margin-right: 2px; }

.feature-card { display: flex; align-items: center; background: #fff; padding: 24px; border-radius: 12px; cursor: pointer; transition: all 0.25s; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.feature-card:hover { transform: translateY(-3px); box-shadow: 0 8px 24px rgba(0,0,0,0.1); }
.feature-icon { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-right: 16px; flex-shrink: 0; }
.feature-icon i { font-size: 24px; color: #fff; }
.feature-text h3 { font-size: 16px; color: #1a1a1a; font-weight: 600; margin-bottom: 4px; }
.feature-text p { font-size: 13px; color: #999; }

.stat-cards { margin-bottom: 0; }
.stat-card .el-card__body {
  display: flex;
  align-items: center;
  padding: 20px;
}
.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}
.stat-icon i { font-size: 26px; color: #fff; }
.stat-number { font-size: 26px; font-weight: 700; color: #1a1a1a; }
.stat-label { font-size: 13px; color: #999; margin-top: 4px; }
.quick-actions { display: flex; flex-direction: column; gap: 12px; }
.quick-actions .el-button { margin-left: 0; }
.announcement { color: #666; line-height: 2; font-size: 14px; }
</style>
