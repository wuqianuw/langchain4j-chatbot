import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 用户状态管理
 */
export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const role = ref(localStorage.getItem('role') || '')
  const avatar = ref(localStorage.getItem('avatar') || '')

  const isAdmin = computed(() => role.value === 'ADMIN')
  const isLoggedIn = computed(() => !!token.value)

  /**
   * 设置登录信息
   */
  function setLogin(data) {
    token.value = data.token
    userId.value = data.userId
    username.value = data.username
    nickname.value = data.nickname
    role.value = data.role
    avatar.value = data.avatar || ''
    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', data.userId)
    localStorage.setItem('username', data.username)
    localStorage.setItem('nickname', data.nickname || '')
    localStorage.setItem('role', data.role)
    localStorage.setItem('avatar', data.avatar || '')
  }

  /**
   * 退出登录
   */
  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    nickname.value = ''
    role.value = ''
    avatar.value = ''
    localStorage.clear()
  }

  return { token, userId, username, nickname, role, avatar, isAdmin, isLoggedIn, setLogin, logout }
})
