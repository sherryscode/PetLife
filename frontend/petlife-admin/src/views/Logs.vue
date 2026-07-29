<template>
  <div class="logs-page">
    <h2>📝 操作日志</h2>
    
    <div class="search-bar">
      <el-input v-model="searchKeyword" placeholder="搜索操作描述" style="width: 200px; margin-right: 10px;" />
      <el-select v-model="searchModule" placeholder="选择模块" style="width: 150px; margin-right: 10px;">
        <el-option label="全部" value="" />
        <el-option label="Admin" value="Admin" />
        <el-option label="Menu" value="Menu" />
        <el-option label="User" value="User" />
        <el-option label="Merchant" value="Merchant" />
        <el-option label="Role" value="Role" />
        <el-option label="FileUpload" value="FileUpload" />
      </el-select>
      <el-select v-model="searchOperation" placeholder="选择操作" style="width: 120px; margin-right: 10px;">
        <el-option label="全部" value="" />
        <el-option label="新增" value="新增" />
        <el-option label="修改" value="修改" />
        <el-option label="删除" value="删除" />
        <el-option label="查询" value="查询" />
      </el-select>
      <el-button @click="searchLogs">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
    </div>

    <el-table :data="logs" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="userName" label="用户名" width="120" />
      <el-table-column prop="module" label="模块" width="120">
        <template #default="scope">
          <el-tag size="small">{{ scope.row.module || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operation" label="操作" width="80">
        <template #default="scope">
          <el-tag :type="getOperationType(scope.row.operation)" size="small">{{ scope.row.operation }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" width="250" show-overflow-tooltip>
        <template #default="scope">
          {{ scope.row.description || scope.row.url || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="ip" label="IP" width="130" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
            {{ scope.row.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="executionTime" label="耗时(ms)" width="100" />
      <el-table-column prop="createdAt" label="操作时间" width="180" />
      <el-table-column label="详情" width="80">
        <template #default="scope">
          <el-button size="small" @click="viewLogDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next, jumper"
        @current-change="handlePageChange"
      />
    </div>

    <el-dialog v-model="logDetailVisible" title="日志详情" width="700px">
      <div v-if="currentLog" class="log-detail">
        <el-form label-width="120px">
          <el-form-item label="日志ID">
            <span>{{ currentLog.id }}</span>
          </el-form-item>
          <el-form-item label="用户ID">
            <span>{{ currentLog.userId || '-' }}</span>
          </el-form-item>
          <el-form-item label="用户名">
            <span>{{ currentLog.userName || '-' }}</span>
          </el-form-item>
          <el-form-item label="模块">
            <el-tag size="small">{{ currentLog.module || '-' }}</el-tag>
          </el-form-item>
          <el-form-item label="操作">
            <el-tag :type="getOperationType(currentLog.operation)" size="small">{{ currentLog.operation || '-' }}</el-tag>
          </el-form-item>
          <el-form-item label="请求URL">
            <span>{{ currentLog.url || '-' }}</span>
          </el-form-item>
          <el-form-item label="请求方法">
            <el-tag size="small">{{ currentLog.method || '-' }}</el-tag>
          </el-form-item>
          <el-form-item label="请求参数">
            <pre class="code-block">{{ formatJson(currentLog.params) }}</pre>
          </el-form-item>
          <el-form-item label="响应结果">
            <pre class="code-block">{{ formatJson(currentLog.result) }}</pre>
          </el-form-item>
          <el-form-item label="客户端IP">
            <span>{{ currentLog.ip || '-' }}</span>
          </el-form-item>
          <el-form-item label="User-Agent">
            <span class="break-all">{{ currentLog.userAgent || '-' }}</span>
          </el-form-item>
          <el-form-item label="执行耗时">
            <span>{{ currentLog.executionTime }} ms</span>
          </el-form-item>
          <el-form-item label="操作状态">
            <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'" size="small">
              {{ currentLog.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </el-form-item>
          <el-form-item label="错误信息">
            <span v-if="currentLog.errorMsg" class="error-msg">{{ currentLog.errorMsg }}</span>
            <span v-else>-</span>
          </el-form-item>
          <el-form-item label="操作时间">
            <span>{{ currentLog.createdAt }}</span>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const logs = ref([])
const searchKeyword = ref('')
const searchModule = ref('')
const searchOperation = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const logDetailVisible = ref(false)
const currentLog = ref(null)

onMounted(() => {
  loadLogs()
})

async function loadLogs() {
  try {
    let url = `/auth/log/list?page=${currentPage.value}&size=${pageSize.value}`
    if (searchKeyword.value) {
      url += `&keyword=${encodeURIComponent(searchKeyword.value)}`
    }
    if (searchModule.value) {
      url += `&module=${searchModule.value}`
    }
    if (searchOperation.value) {
      url += `&operation=${searchOperation.value}`
    }
    const response = await axios.get(url)
    if (response.code === 200) {
      logs.value = response.data.records
      total.value = response.data.total
    }
  } catch (error) {
    console.error(error)
  }
}

async function searchLogs() {
  currentPage.value = 1
  loadLogs()
}

function resetSearch() {
  searchKeyword.value = ''
  searchModule.value = ''
  searchOperation.value = ''
  currentPage.value = 1
  loadLogs()
}

function handlePageChange(page) {
  currentPage.value = page
  loadLogs()
}

function getOperationType(operation) {
  const map = {
    '新增': 'success',
    '修改': 'warning',
    '删除': 'danger',
    '查询': 'info'
  }
  return map[operation] || 'info'
}

function formatJson(str) {
  if (!str) return '-'
  try {
    const obj = JSON.parse(str)
    return JSON.stringify(obj, null, 2)
  } catch {
    return str
  }
}

async function viewLogDetail(log) {
  try {
    const response = await axios.get(`/auth/log/${log.id}`)
    if (response.code === 200) {
      currentLog.value = response.data
      logDetailVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}
</script>

<style scoped>
.logs-page {
  padding: 20px 0;
}

.search-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.log-detail {
  padding: 10px;
}

.code-block {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 8px;
  font-size: 13px;
  max-height: 200px;
  overflow-y: auto;
  white-space: pre-wrap;
  word-break: break-all;
}

.error-msg {
  color: #f56c6c;
}

.break-all {
  word-break: break-all;
}
</style>
