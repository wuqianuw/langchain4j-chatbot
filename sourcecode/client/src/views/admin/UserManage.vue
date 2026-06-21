<template>
  <div class="user-manage">
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="searchKey" placeholder="搜索用户名/昵称" clearable style="width:220px" @clear="loadData" @keyup.enter="loadData">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <button class="toolbar-btn" @click="loadData">搜索</button>
      </div>
      <button class="toolbar-btn primary" @click="openDialog()">
        <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><path d="M7 1v12M1 7h12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
        新增用户
      </button>
    </div>

    <div class="table-wrap">
      <el-table :data="tableData" stripe style="width:100%" v-loading="loading" size="small">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" min-width="100" />
        <el-table-column prop="nickname" label="昵称" min-width="100" />
        <el-table-column prop="role" label="角色" width="80">
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'danger' : ''" size="small">{{ row.role === 'ADMIN' ? '管理员' : '用户' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="70">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="150">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <button class="table-action-btn" @click="openDialog(row)">编辑</button>
            <button class="table-action-btn danger" @click="handleDelete(row.id)">删除</button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top:12px;justify-content:flex-end"
        background
        small
        @change="loadData"
      />
    </div>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="editForm.id ? '编辑用户' : '新增用户'" width="420px">
      <el-form :model="editForm" label-width="70px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" :disabled="!!editForm.id" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="editForm.password" type="password" :placeholder="editForm.id ? '留空则不修改' : '默认123456'" show-password />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editForm.role" style="width:100%">
            <el-option label="普通用户" value="USER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="editForm.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <button class="dialog-btn" @click="dialogVisible = false">取消</button>
        <button class="dialog-btn primary" @click="handleSave">保存</button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const searchKey = ref('')
const dialogVisible = ref(false)
const editForm = reactive({ id: null, username: '', password: '', nickname: '', role: 'USER', status: 1 })

/** 加载用户列表 */
async function loadData() {
  loading.value = true
  try {
    const res = await getUserList({ page: page.value, size: size.value, username: searchKey.value })
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

/** 打开编辑对话框 */
function openDialog(row) {
  if (row) {
    Object.assign(editForm, { id: row.id, username: row.username, password: '', nickname: row.nickname, role: row.role, status: row.status })
  } else {
    Object.assign(editForm, { id: null, username: '', password: '', nickname: '', role: 'USER', status: 1 })
  }
  dialogVisible.value = true
}

/** 保存用户 */
async function handleSave() {
  if (editForm.id) {
    await updateUser(editForm)
    ElMessage.success('更新成功')
  } else {
    await addUser(editForm)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

/** 删除用户 */
async function handleDelete(id) {
  await ElMessageBox.confirm('确定删除该用户？', '提示', { type: 'warning' })
  await deleteUser(id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  flex-wrap: wrap;
  gap: 8px;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.toolbar-btn {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  height: 34px;
  padding: 0 14px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: #fff;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.15s;
  white-space: nowrap;
}

.toolbar-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
}

.toolbar-btn.primary {
  background: var(--brand-gradient);
  border-color: transparent;
  color: #fff;
}

.toolbar-btn.primary:hover {
  opacity: 0.9;
}

.table-wrap {
  background: #fff;
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-sm);
  padding: 16px;
}

.table-action-btn {
  background: none;
  border: none;
  color: var(--primary);
  font-size: 13px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.15s;
}

.table-action-btn:hover {
  background: rgba(26, 86, 219, 0.06);
}

.table-action-btn.danger {
  color: var(--danger);
}

.table-action-btn.danger:hover {
  background: rgba(220, 38, 38, 0.06);
}

.dialog-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 34px;
  padding: 0 18px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: #fff;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  margin-left: 8px;
  transition: all 0.15s;
}

.dialog-btn.primary {
  background: var(--brand-gradient);
  border-color: transparent;
  color: #fff;
}

.dialog-btn.primary:hover { opacity: 0.9; }
.dialog-btn:hover:not(.primary) { border-color: var(--primary); color: var(--primary); }

@media (max-width: 768px) {
  .toolbar :deep(.el-input) {
    width: 100% !important;
  }
}
</style>
