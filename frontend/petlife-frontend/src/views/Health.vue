<template>
  <div class="health-page">
    <div class="container">
      <h2>🏥 健康管理</h2>

      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon">💉</div>
          <div class="stat-info">
            <p class="stat-value">{{ expiringVaccines.length }}</p>
            <p class="stat-label">疫苗即将到期</p>
          </div>
        </div>
        <div class="stat-card warning">
          <div class="stat-icon">⚠️</div>
          <div class="stat-info">
            <p class="stat-value">{{ expiredVaccines.length }}</p>
            <p class="stat-label">疫苗已过期</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">📝</div>
          <div class="stat-info">
            <p class="stat-value">{{ totalCases }}</p>
            <p class="stat-label">病历记录</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">💰</div>
          <div class="stat-info">
            <p class="stat-value">¥{{ totalCost }}</p>
            <p class="stat-label">累计医疗费用</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🩺</div>
          <div class="stat-info">
            <p class="stat-value">{{ totalCheckups }}</p>
            <p class="stat-label">体检记录</p>
          </div>
        </div>
      </div>

      <div class="tabs">
        <el-button v-for="tab in tabs" :key="tab.value"
          :class="{ active: activeTab === tab.value }"
          @click="activeTab = tab.value">
          {{ tab.label }}
        </el-button>
      </div>

      <div v-if="activeTab === 'reminder'" class="tab-content">
        <div v-if="expiringVaccines.length > 0 || expiredVaccines.length > 0">
          <div v-if="expiredVaccines.length > 0" class="reminder-section expired-section">
            <h4>⚠️ 已过期疫苗</h4>
            <el-table :data="expiredVaccines" border style="width: 100%">
              <el-table-column prop="petName" label="宠物名称" />
              <el-table-column prop="vaccineName" label="疫苗名称" />
              <el-table-column prop="vaccineTime" label="接种时间" />
              <el-table-column prop="validUntil" label="有效期至" />
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>

          <div v-if="expiringVaccines.length > 0" class="reminder-section expiring-section">
            <h4>⏰ 即将到期疫苗（30天内）</h4>
            <el-table :data="expiringVaccines" border style="width: 100%">
              <el-table-column prop="petName" label="宠物名称" />
              <el-table-column prop="vaccineName" label="疫苗名称" />
              <el-table-column prop="vaccineTime" label="接种时间" />
              <el-table-column prop="validUntil" label="有效期至">
                <template #default="scope">
                  <span>{{ scope.row.validUntil }} ({{ getDaysRemaining(scope.row.validUntil) }}天后到期)</span>
                </template>
              </el-table-column>
              <el-table-column prop="remark" label="备注" />
            </el-table>
          </div>
        </div>
        <div v-else class="empty-state">
          <span class="empty-icon">✅</span>
          <p>所有疫苗均在有效期内，暂无提醒</p>
        </div>
      </div>

      <div v-if="activeTab === 'checkup'" class="tab-content">
        <el-button type="primary" @click="showAddCheckupModal = true" class="add-btn">添加体检记录</el-button>
        
        <el-table :data="allCheckups" border style="width: 100%">
          <el-table-column prop="petName" label="宠物名称" />
          <el-table-column prop="hospitalName" label="医院名称" />
          <el-table-column prop="checkupType" label="体检类型" />
          <el-table-column prop="checkupTime" label="体检时间" />
          <el-table-column prop="cost" label="费用" />
          <el-table-column prop="result" label="体检结果" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="danger" @click="deleteCheckup(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="allCheckups.length === 0" class="empty-state">
          <span class="empty-icon">🩺</span>
          <p>暂无体检记录</p>
        </div>
      </div>

      <div v-if="activeTab === 'cases'" class="tab-content">
        <el-button type="primary" @click="showAddCaseModal = true" class="add-btn">添加病历记录</el-button>
        
        <el-table :data="allCases" border style="width: 100%">
          <el-table-column prop="petName" label="宠物名称" />
          <el-table-column prop="hospitalName" label="医院名称" />
          <el-table-column prop="disease" label="病症" />
          <el-table-column prop="medicine" label="用药" />
          <el-table-column prop="diagnosis" label="诊断结果" />
          <el-table-column prop="visitTime" label="就诊时间" />
          <el-table-column prop="cost" label="费用" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="danger" @click="deleteCase(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="allCases.length === 0" class="empty-state">
          <span class="empty-icon">📋</span>
          <p>暂无病历记录</p>
        </div>
      </div>

      <div v-if="activeTab === 'report'" class="tab-content">
        <div class="report-container">
          <h3>📊 宠物健康报告</h3>
          
          <div class="report-header">
            <div class="report-date">生成日期：{{ currentDate }}</div>
            <div class="report-period">统计周期：全部时间</div>
          </div>

          <div class="report-section">
            <h4>🐾 宠物概览</h4>
            <div class="pet-summary">
              <div v-for="pet in pets" :key="pet.id" class="pet-summary-card">
                <img :src="pet.avatar || 'https://via.placeholder.com/80'" class="pet-thumb" />
                <div class="pet-summary-info">
                  <p class="pet-name">{{ pet.petName }}</p>
                  <p>{{ pet.breed }} · {{ pet.age }}个月 · {{ pet.gender === 1 ? '公' : pet.gender === 2 ? '母' : '未知' }}</p>
                  <p>疫苗记录：{{ getPetVaccines(pet.id).length }}条 | 体检记录：{{ getPetCheckups(pet.id).length }}条 | 病历记录：{{ getPetCases(pet.id).length }}条</p>
                </div>
              </div>
            </div>
          </div>

          <div class="report-section">
            <h4>💉 疫苗接种统计</h4>
            <div class="vaccine-stats">
              <div class="vaccine-stat-item">
                <span class="stat-num">{{ allVaccines.length }}</span>
                <span class="stat-text">总接种次数</span>
              </div>
              <div class="vaccine-stat-item warning">
                <span class="stat-num">{{ expiredVaccines.length }}</span>
                <span class="stat-text">已过期</span>
              </div>
              <div class="vaccine-stat-item warning">
                <span class="stat-num">{{ expiringVaccines.length }}</span>
                <span class="stat-text">即将到期</span>
              </div>
              <div class="vaccine-stat-item success">
                <span class="stat-num">{{ validVaccines.length }}</span>
                <span class="stat-text">有效期内</span>
              </div>
            </div>
          </div>

          <div class="report-section">
            <h4>🩺 体检统计</h4>
            <div class="checkup-stats">
              <div class="checkup-stat-item">
                <span class="stat-num">{{ allCheckups.length }}</span>
                <span class="stat-text">体检次数</span>
              </div>
            </div>
          </div>

          <div class="report-section">
            <h4>🏥 医疗费用统计</h4>
            <div class="cost-summary">
              <div class="cost-item">
                <span class="cost-label">病历次数</span>
                <span class="cost-value">{{ totalCases }}次</span>
              </div>
              <div class="cost-item">
                <span class="cost-label">体检次数</span>
                <span class="cost-value">{{ totalCheckups }}次</span>
              </div>
              <div class="cost-item">
                <span class="cost-label">累计医疗费用</span>
                <span class="cost-value total">¥{{ totalCost }}</span>
              </div>
              <div class="cost-item">
                <span class="cost-label">平均每次费用</span>
                <span class="cost-value">¥{{ avgCost }}</span>
              </div>
            </div>
          </div>

          <div class="report-section">
            <h4>📌 健康建议</h4>
            <div class="health-tips">
              <div v-if="expiredVaccines.length > 0" class="tip warning">
                <span class="tip-icon">⚠️</span>
                <span>您有 {{ expiredVaccines.length }} 种疫苗已过期，请尽快为宠物接种</span>
              </div>
              <div v-if="expiringVaccines.length > 0" class="tip warning">
                <span class="tip-icon">⏰</span>
                <span>您有 {{ expiringVaccines.length }} 种疫苗即将到期，请提前安排接种</span>
              </div>
              <div class="tip">
                <span class="tip-icon">🩺</span>
                <span>建议定期为宠物进行体检，保持健康状态</span>
              </div>
              <div class="tip">
                <span class="tip-icon">🍎</span>
                <span>注意宠物饮食均衡，避免暴饮暴食</span>
              </div>
              <div class="tip">
                <span class="tip-icon">🏃</span>
                <span>每天保证宠物足够的运动量，增强体质</span>
              </div>
              <div class="tip">
                <span class="tip-icon">💧</span>
                <span>确保宠物随时有干净的饮用水</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-dialog v-model="showAddCaseModal" title="添加病历记录" width="500px">
        <el-form :model="caseForm">
          <el-form-item label="宠物">
            <el-select v-model="caseForm.petId">
              <el-option v-for="pet in pets" :key="pet.id" :label="pet.petName" :value="pet.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="医院名称">
            <el-input v-model="caseForm.hospitalName" />
          </el-form-item>
          <el-form-item label="病症">
            <el-input v-model="caseForm.disease" />
          </el-form-item>
          <el-form-item label="用药">
            <el-input v-model="caseForm.medicine" />
          </el-form-item>
          <el-form-item label="诊断结果">
            <el-input v-model="caseForm.diagnosis" type="textarea" />
          </el-form-item>
          <el-form-item label="就诊时间">
            <el-date-picker v-model="caseForm.visitTime" type="datetime" />
          </el-form-item>
          <el-form-item label="费用">
            <el-input v-model.number="caseForm.cost" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showAddCaseModal = false">取消</el-button>
          <el-button type="primary" @click="saveCase">确定</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showAddCheckupModal" title="添加体检记录" width="500px">
        <el-form :model="checkupForm">
          <el-form-item label="宠物">
            <el-select v-model="checkupForm.petId">
              <el-option v-for="pet in pets" :key="pet.id" :label="pet.petName" :value="pet.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="医院名称">
            <el-input v-model="checkupForm.hospitalName" />
          </el-form-item>
          <el-form-item label="体检类型">
            <el-input v-model="checkupForm.checkupType" />
          </el-form-item>
          <el-form-item label="体检时间">
            <el-date-picker v-model="checkupForm.checkupTime" type="datetime" />
          </el-form-item>
          <el-form-item label="费用">
            <el-input v-model.number="checkupForm.cost" />
          </el-form-item>
          <el-form-item label="体检结果">
            <el-input v-model="checkupForm.result" type="textarea" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="checkupForm.remark" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showAddCheckupModal = false">取消</el-button>
          <el-button type="primary" @click="saveCheckup">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '../stores/user'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const activeTab = ref('reminder')
const showAddCaseModal = ref(false)
const showAddCheckupModal = ref(false)

const pets = ref([])
const allVaccines = ref([])
const allCases = ref([])
const allCheckups = ref([])

const caseForm = ref({
  petId: '',
  hospitalName: '',
  disease: '',
  medicine: '',
  diagnosis: '',
  visitTime: '',
  cost: 0
})

const checkupForm = ref({
  petId: '',
  hospitalName: '',
  checkupType: '',
  checkupTime: '',
  cost: 0,
  result: '',
  remark: ''
})

const userStore = useUserStore()

const tabs = [
  { label: '疫苗到期提醒', value: 'reminder' },
  { label: '体检记录', value: 'checkup' },
  { label: '病历管理', value: 'cases' },
  { label: '健康报告', value: 'report' }
]

const currentDate = new Date().toLocaleDateString('zh-CN')

const expiredVaccines = computed(() => {
  const now = new Date()
  return allVaccines.value.filter(v => {
    const validUntil = new Date(v.validUntil)
    return validUntil < now
  })
})

const expiringVaccines = computed(() => {
  const now = new Date()
  const oneMonthLater = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000)
  return allVaccines.value.filter(v => {
    const validUntil = new Date(v.validUntil)
    return validUntil > now && validUntil <= oneMonthLater
  })
})

const validVaccines = computed(() => {
  const now = new Date()
  return allVaccines.value.filter(v => {
    const validUntil = new Date(v.validUntil)
    return validUntil > now
  })
})

const totalCases = computed(() => allCases.value.length)

const totalCheckups = computed(() => allCheckups.value.length)

const totalCost = computed(() => {
  const caseCost = allCases.value.reduce((sum, c) => sum + (c.cost || 0), 0)
  const checkupCost = allCheckups.value.reduce((sum, c) => sum + (c.cost || 0), 0)
  return (caseCost + checkupCost).toFixed(2)
})

const avgCost = computed(() => {
  const total = totalCases.value + totalCheckups.value
  if (total === 0) return '0.00'
  return (totalCost.value / total).toFixed(2)
})

onMounted(() => {
  loadData()
})

async function loadData() {
  await loadPets()
  await loadVaccines()
  await loadCheckups()
  await loadCases()
}

async function loadPets() {
  try {
    const response = await axios.get(`/health/pet/user/${userStore.userInfo.id}`)
    if (response.code === 200) {
      pets.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadVaccines() {
  const vaccineList = []
  for (const pet of pets.value) {
    try {
      const response = await axios.get(`/health/vaccine/pet/${pet.id}`)
      if (response.code === 200) {
        response.data.forEach(v => {
          vaccineList.push({ ...v, petName: pet.petName })
        })
      }
    } catch (error) {
      console.error(error)
    }
  }
  allVaccines.value = vaccineList
}

async function loadCheckups() {
  const checkupList = []
  for (const pet of pets.value) {
    try {
      const response = await axios.get(`/health/checkup/pet/${pet.id}`)
      if (response.code === 200) {
        response.data.forEach(c => {
          checkupList.push({ ...c, petName: pet.petName })
        })
      }
    } catch (error) {
      console.error(error)
    }
  }
  allCheckups.value = checkupList
}

async function loadCases() {
  const caseList = []
  for (const pet of pets.value) {
    try {
      const response = await axios.get(`/health/case/pet/${pet.id}`)
      if (response.code === 200) {
        response.data.forEach(c => {
          caseList.push({ ...c, petName: pet.petName })
        })
      }
    } catch (error) {
      console.error(error)
    }
  }
  allCases.value = caseList
}

function getPetVaccines(petId) {
  return allVaccines.value.filter(v => v.petId === petId)
}

function getPetCases(petId) {
  return allCases.value.filter(c => c.petId === petId)
}

function getPetCheckups(petId) {
  return allCheckups.value.filter(c => c.petId === petId)
}

function getDaysRemaining(dateStr) {
  const now = new Date()
  const validUntil = new Date(dateStr)
  const diff = validUntil - now
  return Math.ceil(diff / (1000 * 60 * 60 * 24))
}

async function saveCase() {
  try {
    const response = await axios.post('/health/case', caseForm.value)
    if (response.code === 200) {
      ElMessage.success('添加成功')
      showAddCaseModal.value = false
      caseForm.value = { petId: '', hospitalName: '', disease: '', medicine: '', diagnosis: '', visitTime: '', cost: 0 }
      await loadCases()
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

async function deleteCase(id) {
  try {
    const response = await axios.delete(`/health/case/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadCases()
      await loadVaccines()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

async function saveCheckup() {
  try {
    const response = await axios.post('/health/checkup', checkupForm.value)
    if (response.code === 200) {
      ElMessage.success('添加成功')
      showAddCheckupModal.value = false
      checkupForm.value = { petId: '', hospitalName: '', checkupType: '', checkupTime: '', cost: 0, result: '', remark: '' }
      await loadCheckups()
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

async function deleteCheckup(id) {
  try {
    const response = await axios.delete(`/health/checkup/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadCheckups()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.health-page {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.stat-card.warning {
  background: #fff3cd;
}

.stat-icon {
  font-size: 32px;
}

.stat-info .stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stat-info .stat-label {
  font-size: 14px;
  color: #666;
}

.tabs {
  margin-bottom: 20px;
}

.tabs .el-button {
  margin-right: 12px;
}

.tabs .el-button.active {
  background: #667eea;
  color: white;
}

.tab-content {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
}

.add-btn {
  margin-bottom: 20px;
}

.reminder-section {
  margin-bottom: 24px;
}

.reminder-section h4 {
  margin-bottom: 16px;
  color: #333;
}

.expired-section {
  background: #ffebee;
  padding: 20px;
  border-radius: 8px;
}

.expiring-section {
  background: #fff3e0;
  padding: 20px;
  border-radius: 8px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  font-size: 48px;
  display: block;
  margin-bottom: 16px;
}

.report-container {
  background: #fff;
  padding: 30px;
  border-radius: 12px;
}

.report-container h3 {
  margin-bottom: 24px;
  color: #667eea;
}

.report-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
  color: #666;
  font-size: 14px;
}

.report-section {
  margin-bottom: 24px;
}

.report-section h4 {
  margin-bottom: 16px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.pet-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.pet-summary-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.pet-thumb {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 50%;
}

.pet-summary-info .pet-name {
  font-weight: bold;
  margin-bottom: 4px;
}

.pet-summary-info p {
  margin-bottom: 4px;
  color: #666;
  font-size: 14px;
}

.vaccine-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.vaccine-stat-item {
  text-align: center;
  padding: 20px;
  background: #e3f2fd;
  border-radius: 8px;
}

.vaccine-stat-item.warning {
  background: #fff3cd;
}

.vaccine-stat-item.success {
  background: #d4edda;
}

.vaccine-stat-item .stat-num {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.vaccine-stat-item .stat-text {
  font-size: 14px;
  color: #666;
}

.checkup-stats {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 16px;
}

.checkup-stat-item {
  text-align: center;
  padding: 20px;
  background: #e8f5e9;
  border-radius: 8px;
}

.checkup-stat-item .stat-num {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.checkup-stat-item .stat-text {
  font-size: 14px;
  color: #666;
}

.cost-summary {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.cost-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.cost-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.cost-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.cost-value.total {
  color: #e74c3c;
}

.health-tips {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tip {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.tip.warning {
  background: #fff3cd;
}

.tip-icon {
  font-size: 20px;
  flex-shrink: 0;
}

.tip span:last-child {
  color: #666;
  font-size: 14px;
}
</style>