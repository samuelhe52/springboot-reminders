<template>
  <div class="page">
    <div class="toolbar">
      <h2>Category Management</h2>
      <el-space>
        <el-button @click="$router.push('/todo')">Todo Page</el-button>
        <el-button @click="logout">Logout</el-button>
      </el-space>
    </div>

    <el-space>
      <el-input v-model="name" placeholder="Category name" style="width: 220px;" />
      <el-button type="primary" @click="addCategory">Add</el-button>
      <el-button @click="loadList">Refresh</el-button>
    </el-space>

    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="Name" />
      <el-table-column label="Actions" width="200">
        <template #default="{ row }">
          <el-button link @click="editName(row)">Rename</el-button>
          <el-button link type="danger" @click="remove(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {addCategoryApi, deleteCategoryApi, listCategoryApi, updateCategoryApi} from '../api/category'
import {logoutApi} from '../api/user'
import {useRouter} from 'vue-router'

const router = useRouter()
const name = ref('')
const list = ref([])

const loadList = async () => {
  const res = await listCategoryApi()
  list.value = res.data || []
}

const addCategory = async () => {
  if (!name.value) return
  await addCategoryApi({name: name.value})
  name.value = ''
  await loadList()
}

const editName = async (row) => {
  const {value} = await ElMessageBox.prompt('New category name', 'Rename', {inputValue: row.name})
  await updateCategoryApi({id: row.id, name: value})
  ElMessage.success('Updated')
  await loadList()
}

const remove = async (id) => {
  await deleteCategoryApi({id})
  ElMessage.success('Deleted')
  await loadList()
}

const logout = async () => {
  await logoutApi()
  await router.push('/auth')
}

onMounted(loadList)
</script>

<style scoped>
.page {
  max-width: 900px;
  margin: 40px auto;
  padding: 0 16px;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
</style>
