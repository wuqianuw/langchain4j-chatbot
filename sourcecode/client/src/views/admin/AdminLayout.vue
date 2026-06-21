<template>
  <el-container class="admin-layout">
    <el-aside width="200px" class="admin-aside">
      <div class="admin-logo">
        <span class="brand-mark">往</span>
        <span>管理后台</span>
      </div>
      <el-menu :default-active="activeMenu" router>
        <el-menu-item index="/admin">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/conversations">
          <el-icon><ChatLineSquare /></el-icon>
          <span>会话管理</span>
        </el-menu-item>
        <el-menu-item index="/chat">
          <el-icon><ChatDotRound /></el-icon>
          <span>聊天界面</span>
        </el-menu-item>
      </el-menu>
      <div class="admin-aside-footer">
        <div class="admin-user">
          <div class="admin-avatar">{{ (userStore.nickname || userStore.username).charAt(0) }}</div>
          <span>{{ userStore.nickname || userStore.username }}</span>
        </div>
        <el-button text size="small" class="logout-text-btn" @click="handleLogout">退出</el-button>
      </div>
    </el-aside>
    <el-container>
      <el-header class="admin-header">
        <h2>仪表盘</h2>
        <span class="header-date">{{ currentDate }}</span>
      </el-header>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const currentDate = ref(dayjs().format('YYYY年M月D日'))

const activeMenu = computed(() => route.path)

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  background: #f6f8fc;
}

.admin-aside {
  background: #fff;
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
}

.admin-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 16px;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid var(--border-light);
}

.admin-aside .el-menu {
  border-right: none;
  flex: 1;
  background: transparent;
}

.admin-aside :deep(.el-menu-item) {
  margin: 4px 10px;
  border-radius: 10px;
  color: var(--text-secondary);
  height: 40px;
  line-height: 40px;
  font-size: 14px;
}

.admin-aside :deep(.el-menu-item.is-active) {
  background: rgba(26, 86, 219, 0.06);
  color: var(--primary);
  font-weight: 500;
}

.admin-aside :deep(.el-menu-item:hover) {
  background: rgba(26, 86, 219, 0.04);
  color: var(--primary);
}

.admin-aside-footer {
  padding: 12px 16px;
  border-top: 1px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.admin-user {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
  color: var(--text-secondary);
}

.admin-avatar {
  width: 26px;
  height: 26px;
  border-radius: 7px;
  background: var(--brand-gradient);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 600;
  flex-shrink: 0;
}

.logout-text-btn {
  color: var(--text-tertiary !important);
  font-size: 13px;
}
.logout-text-btn:hover {
  color: var(--danger) !important;
}

.admin-header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--border-light);
  padding: 0 28px;
  height: 56px;
}

.admin-header h2 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.header-date {
  color: var(--text-tertiary);
  font-size: 13px;
}

.admin-main {
  background: transparent;
  padding: 20px;
}
</style>
