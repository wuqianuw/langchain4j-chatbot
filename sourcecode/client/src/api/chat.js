import request from '@/utils/request'

/** 获取会话列表 */
export function getConversationList() {
  return request.get('/conversation/list')
}

/** 创建会话 */
export function createConversation(title) {
  return request.post('/conversation', null, { params: { title } })
}

/** 重命名会话 */
export function renameConversation(id, title) {
  return request.put(`/conversation/${id}`, null, { params: { title } })
}

/** 删除会话 */
export function deleteConversation(id) {
  return request.delete(`/conversation/${id}`)
}

/** 管理员会话列表 */
export function getAdminConversationList(params) {
  return request.get('/conversation/admin/list', { params })
}

/** 获取消息历史 */
export function getMessageList(conversationId) {
  return request.get(`/message/list/${conversationId}`)
}

/** 上传文件 */
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/** 获取统计数据 */
export function getStatOverview() {
  return request.get('/stat/overview')
}
