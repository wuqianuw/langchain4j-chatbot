import request from '@/utils/request'

/** 登录 */
export function login(data) {
  return request.post('/auth/login', data)
}

/** 注册 */
export function register(data) {
  return request.post('/auth/register', data)
}

/** 获取当前用户信息 */
export function getUserInfo() {
  return request.get('/user/info')
}

/** 用户列表(管理员) */
export function getUserList(params) {
  return request.get('/user/list', { params })
}

/** 新增用户 */
export function addUser(data) {
  return request.post('/user', data)
}

/** 更新用户 */
export function updateUser(data) {
  return request.put('/user', data)
}

/** 删除用户 */
export function deleteUser(id) {
  return request.delete(`/user/${id}`)
}
