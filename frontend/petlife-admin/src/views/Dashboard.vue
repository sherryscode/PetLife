<template>
  <div class="dashboard">
    <h2>📊 数据大屏</h2>
    
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-info">
          <div class="stat-value">{{ userCount }}</div>
          <div class="stat-label">用户总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🏪</div>
        <div class="stat-info">
          <div class="stat-value">{{ merchantCount }}</div>
          <div class="stat-label">商户总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📋</div>
        <div class="stat-info">
          <div class="stat-value">{{ orderCount }}</div>
          <div class="stat-label">订单总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">💰</div>
        <div class="stat-info">
          <div class="stat-value">¥{{ totalRevenue }}</div>
          <div class="stat-label">总收入</div>
        </div>
      </div>
    </div>

    <div class="charts-section">
      <div class="chart-card">
        <h3>用户增长趋势</h3>
        <div ref="userGrowthChart" class="chart-container"></div>
      </div>
      <div class="chart-card">
        <h3>订单统计</h3>
        <div ref="orderStatisticsChart" class="chart-container"></div>
      </div>
      <div class="chart-card full-width">
        <h3>订单趋势</h3>
        <div ref="orderTrendChart" class="chart-container"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import axios from '../utils/axios'
import * as echarts from 'echarts'

const userCount = ref(0)
const merchantCount = ref(0)
const orderCount = ref(0)
const totalRevenue = ref(0)

const userGrowthChart = ref(null)
const orderStatisticsChart = ref(null)
const orderTrendChart = ref(null)

let chart1 = null
let chart2 = null
let chart3 = null

onMounted(() => {
  loadStats()
  setTimeout(() => {
    loadCharts()
  }, 100)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  if (chart1) chart1.dispose()
  if (chart2) chart2.dispose()
  if (chart3) chart3.dispose()
  window.removeEventListener('resize', handleResize)
})

function handleResize() {
  if (chart1) chart1.resize()
  if (chart2) chart2.resize()
  if (chart3) chart3.resize()
}

async function loadStats() {
  try {
    const response = await axios.get('/auth/statistics/overview')
    if (response.code === 200) {
      const data = response.data
      userCount.value = data.userCount || 0
      merchantCount.value = data.merchantCount || 0
      orderCount.value = data.totalOrderCount || 0
      totalRevenue.value = data.totalRevenue || 0
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadCharts() {
  await loadUserGrowthChart()
  await loadOrderStatisticsChart()
  await loadOrderTrendChart()
}

async function loadUserGrowthChart() {
  try {
    const response = await axios.get('/auth/statistics/userGrowth?days=7')
    if (response.code === 200) {
      const data = response.data
      const dates = data.map(item => item.date)
      const counts = data.map(item => item.count)
      
      chart1 = echarts.init(userGrowthChart.value)
      chart1.setOption({
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>新增用户: {c}人'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 30
          }
        },
        yAxis: {
          type: 'value',
          name: '人数'
        },
        series: [{
          name: '新增用户',
          type: 'line',
          smooth: true,
          data: counts,
          areaStyle: {
            opacity: 0.3
          },
          itemStyle: {
            color: '#667eea'
          },
          lineStyle: {
            color: '#667eea',
            width: 3
          }
        }]
      })
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadOrderStatisticsChart() {
  try {
    const response = await axios.get('/auth/statistics/orderStatistics')
    if (response.code === 200) {
      const data = response.data
      const orderData = [
        { value: data.pending || 0, name: '待接单', itemStyle: { color: '#f5a623' } },
        { value: data.paid || 0, name: '已支付', itemStyle: { color: '#3498db' } },
        { value: data.shipped || 0, name: '已发货', itemStyle: { color: '#9b59b6' } },
        { value: data.completed || 0, name: '已完成', itemStyle: { color: '#2ecc71' } },
        { value: data.cancelled || 0, name: '已取消', itemStyle: { color: '#e74c3c' } }
      ]
      
      chart2 = echarts.init(orderStatisticsChart.value)
      chart2.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}单 ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '5%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: true,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          data: orderData
        }]
      })
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadOrderTrendChart() {
  try {
    console.log('Loading order trend chart...')
    const response = await axios.get('/auth/statistics/orderTrend?days=7')
    console.log('Order trend response:', response)
    if (response.code === 200) {
      const data = response.data
      console.log('Order trend data:', data)
      const dates = data.map(item => item.date)
      const counts = data.map(item => item.count)
      const amounts = data.map(item => item.amount)
      
      console.log('Chart container:', orderTrendChart.value)
      if (!orderTrendChart.value) {
        console.error('Chart container is null!')
        return
      }
      chart3 = echarts.init(orderTrendChart.value)
      chart3.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          },
          formatter: function(params) {
            let result = params[0].name + '<br/>'
            params.forEach(item => {
              result += `${item.marker} ${item.seriesName}: ${item.seriesName === '订单金额' ? '¥' : ''}${item.value}<br/>`
            })
            return result
          }
        },
        legend: {
          data: ['订单数量', '订单金额'],
          top: '5%'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: dates,
            axisPointer: {
              type: 'shadow'
            },
            axisLabel: {
              rotate: 30
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '订单数',
            axisLabel: {
              formatter: '{value}单'
            }
          },
          {
            type: 'value',
            name: '金额',
            axisLabel: {
              formatter: '¥{value}'
            }
          }
        ],
        series: [
          {
            name: '订单数量',
            type: 'bar',
            data: counts,
            itemStyle: {
              color: '#667eea'
            },
            barWidth: '40%'
          },
          {
            name: '订单金额',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            data: amounts,
            itemStyle: {
              color: '#f5a623'
            },
            lineStyle: {
              color: '#f5a623',
              width: 3
            },
            areaStyle: {
              opacity: 0.2
            }
          }
        ]
      })
    }
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 40px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #667eea;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.charts-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
}

.charts-section .full-width {
  grid-column: span 2;
}

.chart-card {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
}

.chart-card h3 {
  margin-bottom: 16px;
}

.chart-container {
  height: 300px;
  min-height: 300px;
  width: 100%;
}
</style>
