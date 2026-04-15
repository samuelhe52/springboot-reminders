<template>
  <div class="page">
    <header class="toolbar">
      <span class="app-name">待办事项</span>
      <el-button text @click="logout">退出登录</el-button>
    </header>

    <div class="content-layout">
      <aside class="category-sidebar">
        <h3>分类</h3>
        <div class="cat-add-row">
          <el-input
            v-model="categoryName"
            placeholder="分类名称"
          />
          <el-button type="primary" @click="addCategory">添加</el-button>
        </div>

        <el-scrollbar max-height="460px" style="margin-top: 12px">
          <div
            class="category-item"
            :class="{ active: filters.categoryId === null }"
            @click="selectCategory(null)"
          >
            <span>全部</span>
          </div>
          <div
            v-for="item in categories"
            :key="item.id"
            class="category-item"
            :class="{ active: filters.categoryId === item.id }"
            @click="selectCategory(item.id)"
          >
            <span class="name">{{ catLabel(item.name) }}</span>
            <el-space>
              <el-button link size="small" @click.stop="editCategory(item)"
                >重命名</el-button
              >
              <el-button
                link
                size="small"
                type="danger"
                @click.stop="removeCategory(item.id)"
                >删除</el-button
              >
            </el-space>
          </div>
        </el-scrollbar>
      </aside>

      <section class="todo-main">
        <div class="filters">
          <div class="filter-controls">
            <el-select
              v-model="filters.categoryId"
              placeholder="分类"
              clearable
              style="width: 160px"
            >
              <el-option
                v-for="item in categories"
                :key="item.id"
                :label="catLabel(item.name)"
                :value="item.id"
              />
            </el-select>
            <el-select
              v-model="filters.status"
              placeholder="状态"
              clearable
              style="width: 130px"
            >
              <el-option label="未完成" :value="0" />
              <el-option label="已完成" :value="1" />
            </el-select>
            <el-button @click="loadList">搜索</el-button>
            <el-button @click="resetFilters">重置</el-button>
          </div>
          <el-button type="primary" @click="openAddDialog">+ 新建任务</el-button>
        </div>

        <el-table :data="list">
          <el-table-column prop="title" label="标题" />
          <el-table-column label="分类" width="140">
            <template #default="{ row }">{{
              getCategoryName(row.categoryId)
            }}</template>
          </el-table-column>
          <el-table-column label="备注" min-width="160">
            <template #default="{ row }">
              <span
                v-if="row.notes"
                class="notes-cell"
                @click="openNotesDrawer(row)"
              >{{ row.notes }}</span>
              <span v-else class="notes-empty">—</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag
                :type="row.status === 1 ? 'success' : 'info'"
                size="small"
                round
              >{{ row.status === 1 ? "已完成" : "进行中" }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="deadline" label="截止日期" width="180" />
          <el-table-column label="操作" width="240">
            <template #default="{ row }">
              <el-button link @click="toggleStatus(row)">{{ row.status === 1 ? "标记未完成" : "标记已完成" }}</el-button>
              <el-button link @click="openEditDialog(row)">编辑</el-button>
              <el-button link type="danger" @click="remove(row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </section>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑任务' : '新建任务'"
      width="560px"
    >
      <el-form :model="form" label-width="90px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.notes" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" clearable style="width: 100%">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="catLabel(item.name)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" v-if="form.id">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="未完成" :value="0" />
            <el-option label="已完成" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期">
          <el-date-picker
            v-model="form.deadline"
            type="datetime"
            placeholder="选择截止日期"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <el-drawer
      v-model="notesDrawerVisible"
      :title="notesDrawerTitle"
      direction="rtl"
      size="360px"
    >
      <p class="notes-drawer-body">{{ notesDrawerContent }}</p>
    </el-drawer>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox, ElNotification } from "element-plus";

const CATEGORY_NAMES = {
  Default: "默认",
  Work: "工作",
  Personal: "个人",
  Study: "学习",
  Shopping: "购物",
};
const catLabel = (name) => CATEGORY_NAMES[name] ?? name;

const notify = (message, type = "success") =>
  ElNotification({ message, type, position: "bottom-right", duration: 2500 });
import { useRouter } from "vue-router";
import {
  addCategoryApi,
  deleteCategoryApi,
  listCategoryApi,
  updateCategoryApi,
} from "../api/category";
import {
  addTodoApi,
  deleteTodoApi,
  listTodoApi,
  updateTodoApi,
} from "../api/todo";
import { logoutApi } from "../api/user";

const router = useRouter();
const categories = ref([]);
const categoryName = ref("");
const list = ref([]);
const filters = reactive({ status: null, categoryId: null });
const dialogVisible = ref(false);
const notesDrawerVisible = ref(false);
const notesDrawerTitle = ref("");
const notesDrawerContent = ref("");

const openNotesDrawer = (row) => {
  notesDrawerTitle.value = row.title || "备注";
  notesDrawerContent.value = row.notes;
  notesDrawerVisible.value = true;
};

const form = reactive({
  id: null,
  title: "",
  notes: "",
  categoryId: null,
  status: 0,
  deadline: "",
});

const loadCategories = async () => {
  const res = await listCategoryApi();
  categories.value = res.data || [];
};

const loadList = async () => {
  const res = await listTodoApi(filters);
  list.value = res.data || [];
};

const addCategory = async () => {
  if (!categoryName.value) return;
  await addCategoryApi({ name: categoryName.value });
  categoryName.value = "";
  notify("分类已添加");
  await loadCategories();
};

const editCategory = async (row) => {
  const { value } = await ElMessageBox.prompt("输入新分类名称", "重命名", {
    inputValue: row.name,
  });
  if (!value) return;
  await updateCategoryApi({ id: row.id, name: value });
  notify("分类已更新");
  await loadCategories();
  await loadList();
};

const removeCategory = async (id) => {
  await ElMessageBox.confirm("确定要删除该分类吗？", "删除确认", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  });
  await deleteCategoryApi({ id });
  notify("分类已删除");
  if (filters.categoryId === id) {
    filters.categoryId = null;
  }
  await loadCategories();
  await loadList();
};

const selectCategory = async (id) => {
  filters.categoryId = id;
  await loadList();
};

const resetForm = () => {
  form.id = null;
  form.title = "";
  form.notes = "";
  form.categoryId = null;
  form.status = 0;
  form.deadline = "";
};

const openAddDialog = () => {
  resetForm();
  const defaultCat = categories.value.find((c) => c.name === "Default");
  if (defaultCat) form.categoryId = defaultCat.id;
  dialogVisible.value = true;
};

const openEditDialog = (row) => {
  form.id = row.id;
  form.title = row.title;
  form.notes = row.notes;
  form.categoryId = row.categoryId;
  form.status = row.status;
  form.deadline = row.deadline;
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.title) return;

  const payload = {
    id: form.id,
    title: form.title,
    notes: form.notes,
    categoryId: form.categoryId,
    status: form.status,
    deadline: form.deadline || null,
  };

  if (form.id) {
    await updateTodoApi(payload);
    notify("已更新");
  } else {
    await addTodoApi(payload);
    notify("已添加");
  }

  dialogVisible.value = false;
  await loadList();
};

const toggleStatus = async (row) => {
  await updateTodoApi({ ...row, status: row.status === 1 ? 0 : 1 });
  notify("已更新");
  await loadList();
};

const remove = async (id) => {
  await ElMessageBox.confirm("确定要删除该任务吗？", "删除确认", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  });
  await deleteTodoApi({ id });
  notify("已删除");
  await loadList();
};

const resetFilters = async () => {
  filters.status = null;
  filters.categoryId = null;
  await loadList();
};

const getCategoryName = (categoryId) => {
  if (!categoryId) return "未分类";
  const category = categories.value.find((item) => item.id === categoryId);
  return category ? catLabel(category.name) : "未分类";
};

const logout = async () => {
  await logoutApi();
  await router.push("/auth");
};

onMounted(async () => {
  await loadCategories();
  await loadList();
});
</script>

<style scoped>
.page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px 32px;
  min-height: 100vh;
  color: var(--app-text);
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 0;
  margin-bottom: 20px;
  border-bottom: 1px solid var(--app-panel-border);
}

.app-name {
  font-size: 18px;
  font-weight: 700;
}

.content-layout {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 16px;
  align-items: start;
}

.category-sidebar {
  border: 1px solid var(--app-panel-border);
  border-radius: 12px;
  padding: 16px;
  background: var(--app-panel-bg);
  box-shadow: var(--app-shadow);
}

.category-sidebar h3 {
  margin: 0 0 12px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.6px;
  color: var(--app-muted);
}

.cat-add-row {
  display: flex;
  gap: 8px;
  align-items: center;
}

.cat-add-row .el-input {
  flex: 1;
  min-width: 0;
}

.todo-main {
  min-width: 0;
  border: 1px solid var(--app-panel-border);
  border-radius: 12px;
  padding: 16px;
  background: var(--app-panel-bg);
  box-shadow: var(--app-shadow);
}

.filters {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.filter-controls {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.category-item {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  align-items: center;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
}

.category-item + .category-item {
  margin-top: 4px;
}

.category-item:hover {
  background: var(--el-fill-color-light);
}

.category-item.active {
  background: var(--el-color-primary-light-9);
  font-weight: 500;
}

.name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
  min-width: 0;
}

.notes-cell {
  display: block;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
  color: var(--el-color-primary);
}

.notes-cell:hover {
  text-decoration: underline;
}

.notes-empty {
  color: var(--app-muted);
}

.notes-drawer-body {
  margin: 0;
  line-height: 1.7;
  white-space: pre-wrap;
  overflow-wrap: break-word;
  word-break: break-word;
  color: var(--app-text);
}

:global(html.dark) .category-item.active {
  background: rgba(59, 130, 246, 0.2);
}

@media (max-width: 960px) {
  .content-layout {
    grid-template-columns: 1fr;
  }
}
</style>
