<template>
  <div class="users-page">
    <h2>👥 用户管理</h2>
    
    <div class="search-bar">
      <el-input v-model="searchKeyword" placeholder="搜索用户名或手机号" />
      <el-button @click="searchUsers">搜索</el-button>
    </div>

    <el-table :data="users" border style="width: 100%">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="gender" label="性别">
        <template #default="scope">
          {{ scope.row.gender === 1 ? '男' : scope.row.gender === 2 ? '女' : '未知' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          {{ scope.row.status === 1 ? '正常' : '禁用' }}
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="注册时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="toggleStatus(scope.row)">
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'
import { ElMessage } from 'element-plus'

const users = ref([])
const searchKeyword = ref('')

onMounted(() => {
  loadUsers()
})

async function loadUsers() {
  try {
    let url = '/auth/user/list?page=1&size=20'
    if (searchKeyword.value) {
      url += `&keyword=${searchKeyword.value}`
    }
    const response = await axios.get(url)
    if (response.code === 200) {
      users.value = response.data.records
    }
  } catch (error) {
    console.error(error)
  }
}

async function searchUsers() {
  loadUsers()
}

async function toggleStatus(user) {
  try {
    const response = await axios.put(`/auth/user/${user.id}/status`, {
      status: user.status === 1 ? 0 : 1
    })
    if (response.code === 200) {
      ElMessage.success(user.status === 1 ? '已禁用' : '已启用')
      loadUsers()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

async function deleteUser(id) {
  try {
    const response = await axios.delete(`/auth/user/${id}`)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadUsers()
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}
</script>

<style scoped>
.users-page {
  padding: 20px 0;
}

.search-bar {
  margin-bottom: 20px;
}
</style>