<template>
  <div class="pet-page">
    <div class="container">
      <h2>🐾 宠物档案管理</h2>
      <el-button type="primary" @click="showAddModal = true" class="add-btn">添加宠物</el-button>
      
      <div class="pet-cards">
        <div class="pet-card" v-for="pet in pets" :key="pet.id">
          <div class="pet-avatar">
            <img :src="pet.avatar || 'https://via.placeholder.com/100'" alt="" />
          </div>
          <div class="pet-info">
            <h3>{{ pet.petName }}</h3>
            <p>品种：{{ pet.breed }}</p>
            <p>年龄：{{ pet.age }}个月</p>
            <p>性别：{{ pet.gender === 1 ? '公' : pet.gender === 2 ? '母' : '未知' }}</p>
            <p>体重：{{ pet.weight }}kg</p>
          </div>
          <div class="pet-actions">
            <el-button @click="viewDetail(pet)">详情</el-button>
            <el-button @click="editPet(pet)">编辑</el-button>
            <el-button type="danger" @click="deletePet(pet.id)">删除</el-button>
          </div>
        </div>
      </div>

      <el-dialog v-model="showAddModal" :title="isEditing ? '编辑宠物' : '添加宠物'" width="500px">
        <el-form :model="petForm">
          <el-form-item label="宠物图片">
            <div class="avatar-upload">
              <img v-if="petForm.avatar" :src="petForm.avatar" class="avatar-preview" />
              <div v-else class="avatar-placeholder">
                <span>📷</span>
              </div>
              <el-upload
                class="upload-btn"
                action="/api/files/upload"
                :show-file-list="false"
                :on-success="handleAvatarUpload"
                accept="image/*"
              >
                <el-button size="small">选择图片</el-button>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item label="宠物名称">
            <el-input v-model="petForm.petName" />
          </el-form-item>
          <el-form-item label="品种">
            <el-input v-model="petForm.breed" />
          </el-form-item>
          <el-form-item label="年龄(月)">
            <el-input v-model.number="petForm.age" />
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="petForm.gender">
              <el-radio :value="1">公</el-radio>
              <el-radio :value="2">母</el-radio>
              <el-radio :value="0">未知</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="体重(kg)">
            <el-input v-model.number="petForm.weight" />
          </el-form-item>
          <el-form-item label="领养日期">
            <el-date-picker v-model="petForm.adoptDate" type="date" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showAddModal = false">取消</el-button>
          <el-button type="primary" @click="savePet">确定</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showDetailModal" title="宠物详情" width="800px">
        <div v-if="currentPet" class="detail-content">
          <div class="detail-header">
            <img :src="currentPet.avatar || 'https://via.placeholder.com/120'" class="detail-avatar" />
            <div class="detail-basic">
              <h3>{{ currentPet.petName }}</h3>
              <p>品种：{{ currentPet.breed }}</p>
              <p>年龄：{{ currentPet.age }}个月</p>
              <p>性别：{{ currentPet.gender === 1 ? '公' : currentPet.gender === 2 ? '母' : '未知' }}</p>
              <p>体重：{{ currentPet.weight }}kg</p>
              <p>领养日期：{{ currentPet.adoptDate || '-' }}</p>
            </div>
          </div>

          <div class="health-tabs">
            <el-button v-for="tab in healthTabs" :key="tab.value"
              :class="{ active: activeHealthTab === tab.value }"
              @click="activeHealthTab = tab.value">
              {{ tab.label }}
            </el-button>
          </div>

          <div v-if="activeHealthTab === 'vaccine'" class="health-section">
            <el-button type="primary" @click="showVaccineModal = true" class="add-vaccine-btn">添加疫苗记录</el-button>
            <el-table :data="vaccines" border style="width: 100%" class="health-table">
              <el-table-column prop="vaccineName" label="疫苗名称" />
              <el-table-column prop="vaccineTime" label="接种时间" />
              <el-table-column prop="validUntil" label="有效期至">
                <template #default="scope">
                  <span :class="{ expired: isExpired(scope.row.validUntil) }">{{ scope.row.validUntil }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="remark" label="备注" />
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button type="danger" @click="deleteVaccine(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div v-if="expiringVaccines.length > 0" class="expiring-warning">
              <span class="warning-icon">⚠️</span>
              <span>以下疫苗即将到期：{{ expiringVaccines.map(v => v.vaccineName).join('、') }}</span>
            </div>
          </div>

          <div v-if="activeHealthTab === 'checkup'" class="health-section">
            <el-button type="primary" @click="showCheckupModal = true" class="add-checkup-btn">添加体检记录</el-button>
            <el-table :data="checkups" border style="width: 100%" class="health-table">
              <el-table-column prop="hospitalName" label="医院名称" />
              <el-table-column prop="checkupType" label="体检类型" />
              <el-table-column prop="checkupTime" label="体检时间" />
              <el-table-column prop="cost" label="费用" />
              <el-table-column prop="result" label="体检结果" />
              <el-table-column prop="remark" label="备注" />
              <el-table-column label="操作">
                <template #default="scope">
                  <el-button type="danger" @click="deleteCheckup(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div v-if="activeHealthTab === 'case'" class="health-section">
            <el-button type="primary" @click="showCaseModal = true" class="add-case-btn">添加病历记录</el-button>
            <el-table :data="cases" border style="width: 100%" class="health-table">
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
          </div>

          <div v-if="activeHealthTab === 'report'" class="health-section">
            <div class="health-report">
              <h4>📊 宠物健康报告</h4>
              <div class="report-section">
                <h5>基本信息</h5>
                <p>宠物名称：{{ currentPet.petName }}</p>
                <p>品种：{{ currentPet.breed }}</p>
                <p>年龄：{{ currentPet.age }}个月</p>
                <p>当前体重：{{ currentPet.weight }}kg</p>
              </div>
              <div class="report-section">
                <h5>疫苗接种情况</h5>
                <p>已接种疫苗：{{ vaccines.length }} 次</p>
                <p>即将到期：{{ expiringVaccines.length }} 种</p>
                <p v-if="expiringVaccines.length > 0">到期疫苗：{{ expiringVaccines.map(v => v.vaccineName).join('、') }}</p>
              </div>
              <div class="report-section">
                <h5>体检记录</h5>
                <p>体检次数：{{ checkups.length }} 次</p>
                <p>累计费用：¥{{ totalCheckupCost }}</p>
              </div>
              <div class="report-section">
                <h5>就医记录</h5>
                <p>就诊次数：{{ cases.length }} 次</p>
                <p>累计费用：¥{{ totalCaseCost }}</p>
              </div>
              <div class="report-section">
                <h5>健康建议</h5>
                <ul>
                  <li v-if="expiringVaccines.length > 0">建议及时为宠物接种到期疫苗</li>
                  <li>定期为宠物进行体检</li>
                  <li>保持宠物饮食均衡</li>
                  <li>每天保证足够的运动量</li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <template #footer>
          <el-button @click="showDetailModal = false">关闭</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showVaccineModal" title="添加疫苗记录" width="500px">
        <el-form :model="vaccineForm">
          <el-form-item label="疫苗名称">
            <el-input v-model="vaccineForm.vaccineName" />
          </el-form-item>
          <el-form-item label="接种时间">
            <el-date-picker v-model="vaccineForm.vaccineTime" type="datetime" />
          </el-form-item>
          <el-form-item label="有效期至">
            <el-date-picker v-model="vaccineForm.validUntil" type="datetime" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="vaccineForm.remark" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showVaccineModal = false">取消</el-button>
          <el-button type="primary" @click="saveVaccine">确定</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showCaseModal" title="添加病历记录" width="500px">
        <el-form :model="caseForm">
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
          <el-button @click="showCaseModal = false">取消</el-button>
          <el-button type="primary" @click="saveCase">确定</el-button>
        </template>
      </el-dialog>

      <el-dialog v-model="showCheckupModal" title="添加体检记录" width="500px">
        <el-form :model="checkupForm">
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
          <el-button @click="showCheckupModal = false">取消</el-button>
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

const pets = ref([])
const showAddModal = ref(false)
const isEditing = ref(false)
const showDetailModal = ref(false)
const showVaccineModal = ref(false)
const showCaseModal = ref(false)
const showCheckupModal = ref(false)
const currentPet = ref(null)
const activeHealthTab = ref('vaccine')

const petForm = ref({
  petName: '',
  breed: '',
  age: 0,
  gender: 0,
  weight: 0,
  adoptDate: '',
  avatar: ''
})

const vaccineForm = ref({
  vaccineName: '',
  vaccineTime: '',
  validUntil: '',
  remark: ''
})

const caseForm = ref({
  hospitalName: '',
  disease: '',
  medicine: '',
  diagnosis: '',
  visitTime: '',
  cost: 0
})

const checkupForm = ref({
  hospitalName: '',
  checkupType: '',
  checkupTime: '',
  cost: 0,
  result: '',
  remark: ''
})

const vaccines = ref([])
const cases = ref([])
const checkups = ref([])

const userStore = useUserStore()

const healthTabs = [
  { label: '疫苗记录', value: 'vaccine' },
  { label: '体检记录', value: 'checkup' },
  { label: '病历记录', value: 'case' },
  { label: '健康报告', value: 'report' }
]

const expiringVaccines = computed(() => {
  const now = new Date()
  const oneMonthLater = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000)
  return vaccines.value.filter(v => {
    const validUntil = new Date(v.validUntil)
    return validUntil > now && validUntil <= oneMonthLater
  })
})

const totalCaseCost = computed(() => {
  return cases.value.reduce((sum, c) => sum + (c.cost || 0), 0).toFixed(2)
})

const totalCheckupCost = computed(() => {
  return checkups.value.reduce((sum, c) => sum + (c.cost || 0), 0).toFixed(2)
})

onMounted(() => {
  loadPets()
})

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

async function savePet() {
  petForm.value.userId = userStore.userInfo.id
  try {
    let response
    if (isEditing.value) {
      response = await axios.put(`/health/pet/${petForm.value.id}`, petForm.value)
      if (response.code === 200) {
        ElMessage.success('修改成功')
      }
    } else {
      response = await axios.post('/health/pet', petForm.value)
      if (response.code === 200) {
        ElMessage.success('添加成功')
      }
    }
    showAddModal.value = false
    petForm.value = { petName: '', breed: '', age: 0, gender: 0, weight: 0, adoptDate: '', avatar: '' }
    isEditing.value = false
    loadPets()
  } catch (error) {
    ElMessage.error(isEditing.value ? '修改失败' : '添加失败')
  }
}

function handleAvatarUpload(response) {
  if (response.code === 200) {
    petForm.value.avatar = response.data.url
    ElMessage.success('图片上传成功')
  }
}

function editPet(pet) {
  petForm.value = { ...pet }
  isEditing.value = true
  showAddModal.value = true
}

async function deletePet(id) {
  try {
    const response = await axios.delete(`/health/pet/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadPets()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

async function viewDetail(pet) {
  currentPet.value = pet
  activeHealthTab.value = 'vaccine'
  await loadVaccines(pet.id)
  await loadCheckups(pet.id)
  await loadCases(pet.id)
  showDetailModal.value = true
}

async function loadVaccines(petId) {
  try {
    const response = await axios.get(`/health/vaccine/pet/${petId}`)
    if (response.code === 200) {
      vaccines.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadCheckups(petId) {
  try {
    const response = await axios.get(`/health/checkup/pet/${petId}`)
    if (response.code === 200) {
      checkups.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

async function loadCases(petId) {
  try {
    const response = await axios.get(`/health/case/pet/${petId}`)
    if (response.code === 200) {
      cases.value = response.data
    }
  } catch (error) {
    console.error(error)
  }
}

async function saveVaccine() {
  vaccineForm.value.petId = currentPet.value.id
  try {
    const response = await axios.post('/health/vaccine', vaccineForm.value)
    if (response.code === 200) {
      ElMessage.success('添加成功')
      showVaccineModal.value = false
      vaccineForm.value = { vaccineName: '', vaccineTime: '', validUntil: '', remark: '' }
      await loadVaccines(currentPet.value.id)
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

async function deleteVaccine(id) {
  try {
    const response = await axios.delete(`/health/vaccine/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadVaccines(currentPet.value.id)
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

async function saveCase() {
  caseForm.value.petId = currentPet.value.id
  try {
    const response = await axios.post('/health/case', caseForm.value)
    if (response.code === 200) {
      ElMessage.success('添加成功')
      showCaseModal.value = false
      caseForm.value = { hospitalName: '', disease: '', medicine: '', diagnosis: '', visitTime: '', cost: 0 }
      await loadCases(currentPet.value.id)
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
      await loadCases(currentPet.value.id)
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

async function saveCheckup() {
  checkupForm.value.petId = currentPet.value.id
  try {
    const response = await axios.post('/health/checkup', checkupForm.value)
    if (response.code === 200) {
      ElMessage.success('添加成功')
      showCheckupModal.value = false
      checkupForm.value = { hospitalName: '', checkupType: '', checkupTime: '', cost: 0, result: '', remark: '' }
      await loadCheckups(currentPet.value.id)
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
      await loadCheckups(currentPet.value.id)
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function isExpired(dateStr) {
  if (!dateStr) return false
  return new Date(dateStr) < new Date()
}
</script>

<style scoped>
.pet-page {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 50%;
  border: 2px solid #ddd;
}

.avatar-placeholder {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 50%;
  font-size: 40px;
  border: 2px dashed #ddd;
}

.upload-btn {
  margin-top: 8px;
}

.add-btn {
  margin-bottom: 20px;
}

.pet-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.pet-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.pet-avatar img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
  margin-bottom: 16px;
}

.pet-info {
  text-align: center;
  margin-bottom: 16px;
}

.pet-info h3 {
  margin-bottom: 8px;
}

.pet-info p {
  margin-bottom: 4px;
  color: #666;
}

.pet-actions {
  display: flex;
  gap: 12px;
}

.detail-content {
  padding: 20px 0;
}

.detail-header {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #eee;
}

.detail-avatar {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 50%;
}

.detail-basic h3 {
  margin-bottom: 12px;
}

.detail-basic p {
  margin-bottom: 8px;
  color: #666;
}

.health-tabs {
  margin-bottom: 20px;
}

.health-tabs .el-button {
  margin-right: 12px;
}

.health-tabs .el-button.active {
  background: #667eea;
  color: white;
}

.health-section {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
}

.add-vaccine-btn,
.add-checkup-btn,
.add-case-btn {
  margin-bottom: 20px;
}

.health-table {
  margin-top: 20px;
}

.expired {
  color: #e74c3c;
  font-weight: bold;
}

.expiring-warning {
  margin-top: 16px;
  padding: 12px;
  background: #fff3cd;
  border-radius: 8px;
  color: #856404;
  display: flex;
  align-items: center;
  gap: 8px;
}

.warning-icon {
  font-size: 18px;
}

.health-report {
  padding: 20px;
}

.health-report h4 {
  margin-bottom: 24px;
  color: #667eea;
}

.report-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.report-section h5 {
  margin-bottom: 12px;
  color: #333;
}

.report-section p {
  margin-bottom: 4px;
  color: #666;
}

.report-section ul {
  margin: 0;
  padding-left: 20px;
}

.report-section li {
  margin-bottom: 8px;
  color: #666;
}
</style>