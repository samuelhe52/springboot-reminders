<template>
  <div class="page">
    <div class="toolbar">
      <h2>Todo List</h2>
      <el-space>
        <el-button @click="$router.push('/category')">Category Page</el-button>
        <el-button @click="logout">Logout</el-button>
      </el-space>
    </div>

    <el-space wrap>
      <el-select v-model="filters.categoryId" placeholder="Category" clearable style="width: 180px;">
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="filters.status" placeholder="Status" clearable style="width: 140px;">
        <el-option label="Unfinished" :value="0" />
        <el-option label="Finished" :value="1" />
      </el-select>
      <el-button @click="loadList">Search</el-button>
      <el-button @click="resetFilters">Reset</el-button>
      <el-button type="primary" @click="openAddDialog">Add Todo</el-button>
    </el-space>

    <el-table :data="list" style="margin-top: 16px;">
      <el-table-column prop="title" label="Title" />
      <el-table-column label="Category" width="140">
        <template #default="{ row }">{{ getCategoryName(row.categoryId) }}</template>
      </el-table-column>
      <el-table-column prop="content" label="Content" />
      <el-table-column prop="status" label="Status" width="120">
        <template #default="{ row }">{{ row.status === 1 ? 'Done' : 'Todo' }}</template>
      </el-table-column>
      <el-table-column prop="deadline" label="Deadline" width="180" />
      <el-table-column label="Actions" width="280">
        <template #default="{ row }">
          <el-button link @click="toggleStatus(row)">{{ row.status === 1 ? 'Mark Todo' : 'Mark Done' }}</el-button>
          <el-button link @click="openEditDialog(row)">Edit</el-button>
          <el-button link type="danger" @click="remove(row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? 'Edit Todo' : 'Add Todo'" width="560px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="Title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="Content">
          <el-input v-model="form.content" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="Category">
          <el-select v-model="form.categoryId" clearable style="width: 100%;">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="Status" v-if="form.id">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="Unfinished" :value="0" />
            <el-option label="Finished" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="Deadline">
          <el-date-picker
            v-model="form.deadline"
            type="datetime"
            placeholder="Select deadline"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
import {listCategoryApi} from '../api/category'
import {addTodoApi, deleteTodoApi, listTodoApi, updateTodoApi} from '../api/todo'
import {logoutApi} from '../api/user'

const router = useRouter()
const categories = ref([])
const list = ref([])
const filters = reactive({status: null, categoryId: null})
const dialogVisible = ref(false)
const form = reactive({
  id: null,
  title: '',
  content: '',
  categoryId: null,
  status: 0,
  deadline: ''
})

const loadCategories = async () => {
  const res = await listCategoryApi()
  categories.value = res.data || []
}

const loadList = async () => {
  const res = await listTodoApi(filters)
  list.value = res.data || []
}

const resetForm = () => {
  form.id = null
  form.title = ''
  form.content = ''
  form.categoryId = null
  form.status = 0
  form.deadline = ''
}

const openAddDialog = () => {
  resetForm()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  form.id = row.id
  form.title = row.title
  form.content = row.content
  form.categoryId = row.categoryId
  form.status = row.status
  form.deadline = row.deadline
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!form.title) return

  const payload = {
    id: form.id,
    title: form.title,
    content: form.content,
    categoryId: form.categoryId,
    status: form.status,
    deadline: form.deadline || null
  }

  if (form.id) {
    await updateTodoApi(payload)
    ElMessage.success('Updated')
  } else {
    await addTodoApi(payload)
    ElMessage.success('Added')
  }

  dialogVisible.value = false
  await loadList()
}

const toggleStatus = async (row) => {
  await updateTodoApi({...row, status: row.status === 1 ? 0 : 1})
  ElMessage.success('Updated')
  await loadList()
}

const remove = async (id) => {
  await deleteTodoApi({id})
  ElMessage.success('Deleted')
  await loadList()
}

const resetFilters = async () => {
  filters.status = null
  filters.categoryId = null
  await loadList()
}

const getCategoryName = (categoryId) => {
  if (!categoryId) return 'Uncategorized'
  const category = categories.value.find((item) => item.id === categoryId)
  return category ? category.name : 'Uncategorized'
}

const logout = async () => {
  await logoutApi()
  await router.push('/auth')
}

onMounted(async () => {
  await loadCategories()
  await loadList()
})
</script>

<style scoped>
.page {
  max-width: 1100px;
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
