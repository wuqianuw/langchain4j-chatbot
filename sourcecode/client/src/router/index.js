import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { public: true }
  },
  {
    path: '/chat',
    name: 'Chat',
    component: () => import('@/views/ChatView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    component: () => import('@/views/admin/AdminLayout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        name: 'AdminHome',
        component: () => import('@/views/admin/Home.vue')
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('@/views/admin/UserManage.vue')
      },
      {
        path: 'conversations',
        name: 'ConversationManage',
        component: () => import('@/views/admin/ConversationManage.vue')
      }
    ]
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/** 路由守卫 - 鉴权与角色控制 */
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.public) {
    if (userStore.isLoggedIn) {
      next(userStore.isAdmin ? '/admin' : '/chat')
    } else {
      next()
    }
    return
  }

  if (!userStore.isLoggedIn) {
    next('/login')
    return
  }

  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    next('/chat')
    return
  }

  next()
})

export default router
