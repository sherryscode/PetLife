<template>
  <div class="dashboard">
    <h2>📊 数据概览</h2>
    
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">💰</div>
        <div class="stat-info">
          <div class="stat-value">¥{{ totalRevenue }}</div>
          <div class="stat-label">今日收入</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📋</div>
        <div class="stat-info">
          <div class="stat-value">{{ orderCount }}</div>
          <div class="stat-label">今日订单</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🛒</div>
        <div class="stat-info">
          <div class="stat-value">{{ goodsCount }}</div>
          <div class="stat-label">商品数量</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⭐</div>
        <div class="stat-info">
          <div class="stat-value">{{ rating }}</div>
          <div class="stat-label">门店评分</div>
        </div>
      </div>
    </div>

    <div class="chart-section">
      <h3>订单统计</h3>
      <div ref="orderTrendChart" class="chart-container"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import axios from '../utils/axios'
import * as echarts from 'echarts'
import { useMerchantStore } from '../stores/merchant'

const totalRevenue = ref(0)
const orderCount = ref(0)
const goodsCount = ref(0)
const rating = ref('0.0')

const orderTrendChart = ref(null)
let chart1 = null

const merchantStore = useMerchantStore()

onMounted(() => {
  loadStats()
  setTimeout(() => {
    loadOrderTrendChart()
  }, 100)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  if (chart1) chart1.dispose()
  window.removeEventListener('resize', handleResize)
})

function handleResize() {
  if (chart1) chart1.resize()
}

async function loadStats() {
  try {
    const merchantId = merchantStore.merchantInfo?.id || 1
    const overviewRes = await axios.get(`/auth/statistics/merchant/overview?merchantId=${merchantId}`)
    if (overviewRes.code === 200) {
      const data = overviewRes.data
      totalRevenue.value = data.todayRevenue || 0
      orderCount.value = data.todayOrderCount || 0
    }
  } catch (error) {
    console.error(error)
  }

  try {
    const response = await axios.get('/mall/goods/list?page=1&size=1')
    if (response.code === 200) {
      goodsCount.value = response.data.total
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadOrderTrendChart() {
  try {
    console.log('Loading merchant order trend chart...')
    const merchantId = merchantStore.merchantInfo?.id || 1
    console.log('Merchant ID:', merchantId)
    const response = await axios.get(`/auth/statistics/merchant/orderTrend?merchantId=${merchantId}&days=7`)
    console.log('Merchant order trend response:', response)
    if (response.code === 200) {
      const data = response.data
      console.log('Merchant order trend data:', data)
      const dates = data.map(item => item.date)
      const counts = data.map(item => item.count)
      const amounts = data.map(item => item.amount)
      
      console.log('Chart container:', orderTrendChart.value)
      if (!orderTrendChart.value) {
        console.error('Chart container is null!')
        return
      }
      chart1 = echarts.init(orderTrendChart.value)
      chart1.setOption({
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

.chart-section {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
}

.chart-section h3 {
  margin-bottom: 16px;
}

.chart-container {
  height: 300px;
  min-height: 300px;
  width: 100%;
}
</style>
