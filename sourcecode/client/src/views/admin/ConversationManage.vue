<template>
  <div class="conv-manage">
    <div class="page-header">
      <h3>会话管理</h3>
    </div>
    <div class="table-wrap">
      <el-table :data="tableData" stripe style="width:100%" v-loading="loading" size="small">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="userId" label="用户ID" width="70" />
        <el-table-column prop="title" label="会话标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" min-width="150">
          <template #default="{ row }">{{ formatDateTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="150">
          <template #default="{ row }">{{ formatDateTime(row.updateTime) }}</template>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { formatDateTime } from '@/utils/format'
import { getAdminConversationList } from '@/api/chat'

const loading = ref(false)
const tableData = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

/** 加载会话列表 */
async function loadData() {
  loading.value = true
  try {
    const res = await getAdminConversationList({ page: page.value, size: size.value })
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.page-header {
  margin-bottom: 12px;
}

.page-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.table-wrap {
  background: #fff;
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-sm);
  padding: 16px;
}
</style>
