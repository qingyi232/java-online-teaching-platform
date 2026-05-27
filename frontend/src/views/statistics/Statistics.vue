<template>
  <div>
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #2C3E50;"><i class="el-icon-user"></i></div>
          <div class="stat-info">
            <p class="stat-number">{{ dashboardStats.totalUsers || 0 }}</p>
            <p class="stat-label">用户总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #3D8B7A;"><i class="el-icon-folder-opened"></i></div>
          <div class="stat-info">
            <p class="stat-number">{{ dashboardStats.totalResources || 0 }}</p>
            <p class="stat-label">资源总数</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #C9A96E;"><i class="el-icon-view"></i></div>
          <div class="stat-info">
            <p class="stat-number">{{ dashboardStats.totalViews || 0 }}</p>
            <p class="stat-label">浏览总量</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #C0625A;"><i class="el-icon-download"></i></div>
          <div class="stat-info">
            <p class="stat-number">{{ dashboardStats.totalDownloads || 0 }}</p>
            <p class="stat-label">下载总量</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-weight: bold;">浏览趋势</span>
            <el-radio-group v-model="viewDays" size="mini" @change="loadViewStats">
              <el-radio-button :label="7">近7天</el-radio-button>
              <el-radio-button :label="30">近30天</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="viewChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-weight: bold;">下载趋势</span>
            <el-radio-group v-model="downloadDays" size="mini" @change="loadDownloadStats">
              <el-radio-button :label="7">近7天</el-radio-button>
              <el-radio-button :label="30">近30天</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="downloadChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span style="font-weight: bold;">分类浏览分布</span></div>
          <div ref="categoryChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><span style="font-weight: bold;">下载量TOP10资源</span></div>
          <div ref="topChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardStats, getViewStats, getDownloadStats, getCategoryStats, getTopResources } from '@/api/statistics'

export default {
  name: 'Statistics',
  data() {
    return {
      dashboardStats: {},
      viewDays: 7,
      downloadDays: 7,
      charts: {}
    }
  },
  created() {
    this.loadDashboard()
  },
  mounted() {
    this.loadViewStats()
    this.loadDownloadStats()
    this.loadCategoryStats()
    this.loadTopResources()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    Object.values(this.charts).forEach(c => c && c.dispose())
  },
  methods: {
    loadDashboard() {
      getDashboardStats().then(res => { this.dashboardStats = res.data }).catch(() => {})
    },
    loadViewStats() {
      getViewStats({ days: this.viewDays }).then(res => {
        const data = res.data.viewsByDate || []
        this.renderLineChart('viewChart', data, '浏览量', '#2C3E50')
      }).catch(() => {})
    },
    loadDownloadStats() {
      getDownloadStats({ days: this.downloadDays }).then(res => {
        const data = res.data.downloadsByDate || []
        this.renderLineChart('downloadChart', data, '下载量', '#3D8B7A')
      }).catch(() => {})
    },
    loadCategoryStats() {
      getCategoryStats().then(res => {
        const data = res.data.viewsByCategory || []
        this.renderPieChart('categoryChart', data)
      }).catch(() => {})
    },
    loadTopResources() {
      getTopResources({ limit: 10 }).then(res => {
        const data = res.data.topResources || []
        this.renderBarChart('topChart', data)
      }).catch(() => {})
    },
    renderLineChart(refName, data, name, color) {
      if (this.charts[refName]) this.charts[refName].dispose()
      const chart = echarts.init(this.$refs[refName])
      this.charts[refName] = chart
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.map(d => d.date), axisLabel: { rotate: 30, fontSize: 11 }, axisLine: { lineStyle: { color: '#ddd' } } },
        yAxis: { type: 'value', minInterval: 1, axisLine: { show: false }, splitLine: { lineStyle: { color: '#f0f0f0' } } },
        series: [{ name, type: 'line', data: data.map(d => d.count), smooth: true, areaStyle: { opacity: 0.08 }, itemStyle: { color }, lineStyle: { width: 2.5 }, symbolSize: 6 }],
        grid: { left: 50, right: 20, top: 20, bottom: 50 }
      })
    },
    renderPieChart(refName, data) {
      if (this.charts[refName]) this.charts[refName].dispose()
      const chart = echarts.init(this.$refs[refName])
      this.charts[refName] = chart
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0 },
        color: ['#2C3E50', '#3D8B7A', '#C9A96E', '#C0625A', '#5B7B8A', '#8B6E5A', '#4A6B5D', '#9B8EC4'],
        series: [{
          type: 'pie', radius: ['40%', '70%'],
          data: data.map(d => ({ name: d.category_name || '未分类', value: d.count })),
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.3)' } }
        }]
      })
    },
    renderBarChart(refName, data) {
      if (this.charts[refName]) this.charts[refName].dispose()
      const chart = echarts.init(this.$refs[refName])
      this.charts[refName] = chart
      const sorted = data.sort((a, b) => a.download_count - b.download_count)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'value', minInterval: 1 },
        yAxis: { type: 'category', data: sorted.map(d => d.title || ''), axisLabel: { width: 120, overflow: 'truncate' } },
        series: [{ type: 'bar', data: sorted.map(d => d.download_count), itemStyle: { color: '#2C3E50', borderRadius: [0, 4, 4, 0] } }],
        grid: { left: 140, right: 30, top: 10, bottom: 30 }
      })
    },
    resizeCharts() {
      Object.values(this.charts).forEach(c => c && c.resize())
    }
  }
}
</script>

<style scoped>
.stat-card .el-card__body { display: flex; align-items: center; padding: 20px; }
.stat-icon { width: 52px; height: 52px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 16px; }
.stat-icon i { font-size: 26px; color: #fff; }
.stat-number { font-size: 26px; font-weight: 700; color: #1a1a1a; }
.stat-label { font-size: 13px; color: #999; margin-top: 4px; }
</style>
