<template>
  <div class="page">
    <div class="toolbar">
      <h2>分类管理</h2>
      <el-space>
        <el-button @click="$router.push('/todo')">待办事项</el-button>
        <el-button @click="logout">退出登录</el-button>
      </el-space>
    </div>

    <el-space>
      <el-input
        v-model="name"
        placeholder="分类名称"
        style="width: 220px"
      />
      <el-button type="primary" @click="addCategory">添加</el-button>
      <el-button @click="loadList">刷新</el-button>
    </el-space>

    <el-table :data="list" style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称">
        <template #default="{ row }">{{ catLabel(row.name) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link @click="editName(row)">重命名</el-button>
          <el-button link type="danger" @click="remove(row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
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
import {
  addCategoryApi,
  deleteCategoryApi,
  listCategoryApi,
  updateCategoryApi,
} from "../api/category";
import { logoutApi } from "../api/user";
import { useRouter } from "vue-router";

const router = useRouter();
const name = ref("");
const list = ref([]);

const loadList = async () => {
  const res = await listCategoryApi();
  list.value = res.data || [];
};

const addCategory = async () => {
  if (!name.value) return;
  await addCategoryApi({ name: name.value });
  name.value = "";
  await loadList();
};

const editName = async (row) => {
  const { value } = await ElMessageBox.prompt("输入新分类名称", "重命名", {
    inputValue: row.name,
  });
  await updateCategoryApi({ id: row.id, name: value });
  notify("已更新");
  await loadList();
};

const remove = async (id) => {
  await ElMessageBox.confirm("确定要删除该分类吗？", "删除确认", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  });
  await deleteCategoryApi({ id });
  notify("已删除");
  await loadList();
};

const logout = async () => {
  await logoutApi();
  await router.push("/auth");
};

onMounted(loadList);
</script>

<style scoped>
.page {
  max-width: 900px;
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

.toolbar h2 {
  margin: 0;
  font-size: 18px;
}
</style>
