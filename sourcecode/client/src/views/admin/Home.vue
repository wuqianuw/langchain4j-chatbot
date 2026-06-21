<template>
  <div class="home-page">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="6" v-for="card in statCards" :key="card.title">
        <div class="stat-card">
          <div class="stat-icon" :style="{ background: card.bg, color: card.color }">
            <el-icon :size="24"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-title">{{ card.title }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" style="margin-top:16px">
      <el-col :xs="24" :lg="14">
        <div class="chart-card">
          <div class="chart-title">
            <span class="chart-dot"></span>
            <h3>近7日消息趋势</h3>
          </div>
          <div ref="trendChartRef" class="chart-container"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="10">
        <div class="chart-card">
          <div class="chart-title">
            <span class="chart-dot"></span>
            <h3>消息模态占比</h3>
          </div>
          <div ref="modalityChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :xs="24" :lg="10">
        <div class="chart-card">
          <div class="chart-title">
            <span class="chart-dot"></span>
            <h3>用户角色分布</h3>
          </div>
          <div ref="roleChartRef" class="chart-container"></div>
        </div>
      </el-col>
      <el-col :xs="24" :lg="14">
        <div class="chart-card">
          <div class="chart-title">
            <span class="chart-dot"></span>
            <h3>活跃用户排行</h3>
          </div>
          <div ref="activeChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, shallowRef } from 'vue'
import * as echarts from 'echarts'
import { getStatOverview } from '@/api/chat'

const trendChartRef = ref()
const modalityChartRef = ref()
const roleChartRef = ref()
const activeChartRef = ref()
const charts = shallowRef([])

const statCards = ref([
  { title: '用户总数', value: 0, icon: 'User', bg: 'rgba(26, 86, 219, 0.08)', color: '#1a56db' },
  { title: '会话总数', value: 0, icon: 'ChatLineSquare', bg: 'rgba(5, 150, 105, 0.08)', color: '#059669' },
  { title: '消息总数', value: 0, icon: 'ChatDotRound', bg: 'rgba(217, 119, 6, 0.08)', color: '#d97706' },
  { title: '今日消息', value: 0, icon: 'TrendCharts', bg: 'rgba(220, 38, 38, 0.08)', color: '#dc2626' }
])

/** 初始化图表 */
function initCharts(data) {
  // 消息趋势折线图
  const trendChart = echarts.init(trendChartRef.value)
  trendChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 50, right: 20, top: 30, bottom: 30 },
    xAxis: { type: 'category', data: data.messageTrend.map(i => i.date), axisLabel: { color: '#888' } },
    yAxis: { type: 'value', axisLabel: { color: '#888' }, splitLine: { lineStyle: { type: 'dashed' } } },
    series: [{
      type: 'line', smooth: true, data: data.messageTrend.map(i => i.count),
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: 'rgba(0,122,255,0.26)' }, { offset: 1, color: 'rgba(0,122,255,0.04)' }
      ])},
      lineStyle: { color: '#007aff', width: 3 },
      itemStyle: { color: '#007aff' }
    }]
  })
  charts.value.push(trendChart)

  // 模态占比饼图
  const modalityChart = echarts.init(modalityChartRef.value)
  modalityChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    color: ['#007aff', '#34c759', '#ff9f0a', '#ff3b30'],
    series: [{
      type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
      data: data.modalityStats,
      label: { formatter: '{b}\n{d}%' },
      emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
    }]
  })
  charts.value.push(modalityChart)

  // 角色分布环图
  const roleChart = echarts.init(roleChartRef.value)
  roleChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    color: ['#007aff', '#34c759'],
    series: [{
      type: 'pie', radius: ['45%', '70%'], center: ['50%', '45%'],
      data: data.roleStats,
      label: { formatter: '{b}: {c}' }
    }]
  })
  charts.value.push(roleChart)

  // 活跃用户柱状图
  const activeChart = echarts.init(activeChartRef.value)
  activeChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 80, right: 20, top: 20, bottom: 30 },
    xAxis: { type: 'value', axisLabel: { color: '#888' } },
    yAxis: { type: 'category', data: data.activeUsers.map(i => i.username).reverse(), axisLabel: { color: '#666' } },
    series: [{
      type: 'bar', data: data.activeUsers.map(i => i.messageCount).reverse(),
      itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
        { offset: 0, color: '#5ac8fa' }, { offset: 1, color: '#007aff' }
      ]), borderRadius: [0, 6, 6, 0] },
      barWidth: 16
    }]
  })
  charts.value.push(activeChart)
}

/** 加载统计数据 */
async function loadStats() {
  const res = await getStatOverview()
  const data = res.data
  statCards.value[0].value = data.userCount
  statCards.value[1].value = data.conversationCount
  statCards.value[2].value = data.messageCount
  statCards.value[3].value = data.todayMessageCount
  initCharts(data)
}

/** 窗口resize时重绘图表 */
function handleResize() {
  charts.value.forEach(c => c.resize())
}

onMounted(() => {
  loadStats()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  charts.value.forEach(c => c.dispose())
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.stat-card {
  padding: 18px 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  background: #fff;
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-sm);
  transition: box-shadow 0.2s, transform 0.2s;
}

.stat-card:hover {
  box-shadow: var(--shadow);
  transform: translateY(-1px);
}

.stat-icon {
  width: 46px;
  height: 46px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-value { font-size: 24px; font-weight: 700; color: var(--text-primary); letter-spacing: -0.02em; }
.stat-title { font-size: 13px; color: var(--text-tertiary); margin-top: 2px; }

.chart-card {
  padding: 20px;
  background: #fff;
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-sm);
}

.chart-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.chart-dot {
  width: 3px;
  height: 16px;
  background: var(--primary);
  border-radius: 2px;
  flex-shrink: 0;
}

.chart-title h3 {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.chart-container {
  height: 280px;
  width: 100%;
}
</style>
