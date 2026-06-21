<template>
  <div class="chat-page">
    <!-- 左侧会话列表 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="brand">
          <span class="brand-mark">往</span>
          <span class="brand-name">往 · 智能助手</span>
        </div>
        <button class="new-chat-btn" @click="handleNewChat">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M8 3v10M3 8h10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
          新对话
        </button>
      </div>
      <div class="conversation-list">
        <div
          v-for="conv in conversations"
          :key="conv.id"
          class="conv-item"
          :class="{ active: currentConvId === conv.id }"
          @click="selectConversation(conv)"
        >
          <svg class="conv-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
            <path d="M3 2h10a1 1 0 011 1v8a1 1 0 01-1 1H7l-3 2v-2H3a1 1 0 01-1-1V3a1 1 0 011-1z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round"/>
          </svg>
          <span class="conv-title">{{ conv.title }}</span>
          <button class="conv-delete" @click.stop="handleDeleteConv(conv.id)" title="删除会话">
            <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><path d="M3 4h8M5.5 4V3a1 1 0 011-1h1a1 1 0 011 1v1M4 4v7a1 1 0 001 1h4a1 1 0 001-1V4" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>
          </button>
        </div>
        <div v-if="conversations.length === 0" class="conv-empty">
          暂无对话，点击上方「新对话」开始
        </div>
      </div>
      <div class="sidebar-footer">
        <div class="user-info">
          <div class="user-avatar-small">{{ (userStore.nickname || userStore.username).charAt(0) }}</div>
          <span class="user-name">{{ userStore.nickname || userStore.username }}</span>
        </div>
        <button class="logout-btn" @click="handleLogout" title="退出登录">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none"><path d="M6 3H4a1 1 0 00-1 1v8a1 1 0 001 1h2M10 11l3-3-3-3M13 8H7" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </button>
      </div>
    </aside>

    <!-- 右侧聊天区域 -->
    <main class="chat-main">
      <div v-if="!currentConvId && !isNewChat" class="welcome">
        <div class="welcome-brand">
          <span class="welcome-logo">往</span>
        </div>
        <h2>欢迎使用「往」智能助手</h2>
        <p class="welcome-desc">支持文本、图片、音频、视频等多模态交互</p>
        <div class="welcome-hints">
          <div class="hint-item">
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M9 1v16M1 9h16" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
            <span>开始新对话，或从左侧选择已有会话</span>
          </div>
          <div class="hint-item">
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M2 14l4-6 3 4 3-6 4 8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>
            <span>支持拖拽或点击上传图片、音频、视频文件</span>
          </div>
          <div class="hint-item">
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M9 13V5M5 9h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
            <span>AI 基于 LangChain4j + 通义千问 VL 多模态模型</span>
          </div>
        </div>
      </div>

      <div v-else class="chat-area">
        <!-- 消息列表 -->
        <div ref="messageListRef" class="message-list">
          <div v-for="msg in messages" :key="msg.id" class="message-item" :class="msg.role">
            <div class="avatar-col">
              <div v-if="msg.role === 'user'" class="user-avatar-small">{{ (userStore.nickname || userStore.username).charAt(0) }}</div>
              <div v-else class="ai-avatar">往</div>
            </div>
            <div class="message-content">
              <div class="message-sender">{{ msg.role === 'user' ? (userStore.nickname || userStore.username) : '往 AI' }}</div>
              <div class="bubble" :class="msg.role">
                <div v-if="msg.contentType === 'text' || msg.role === 'assistant'" class="text-content" v-text="msg.content"></div>
                <div v-if="msg.contentType === 'image' && msg.fileUrl" class="media-content">
                  <p v-if="msg.content" class="media-caption">{{ msg.content }}</p>
                  <el-image :src="resolveMediaUrl(msg.fileUrl)" fit="contain" style="max-width:320px;max-height:240px;border-radius:8px" :preview-src-list="[resolveMediaUrl(msg.fileUrl)]" />
                </div>
                <div v-if="msg.contentType === 'audio' && msg.fileUrl" class="media-content">
                  <p v-if="msg.content" class="media-caption">{{ msg.content }}</p>
                  <audio :src="resolveMediaUrl(msg.fileUrl)" controls style="width:100%;max-width:300px"></audio>
                </div>
                <div v-if="msg.contentType === 'video' && msg.fileUrl" class="media-content">
                  <p v-if="msg.content" class="media-caption">{{ msg.content }}</p>
                  <video :src="resolveMediaUrl(msg.fileUrl)" controls style="max-width:400px;max-height:250px;border-radius:8px"></video>
                </div>
              </div>
              <div class="msg-time">{{ formatDateTime(msg.createTime) }}</div>
            </div>
          </div>
          <!-- 流式输出中的消息 -->
          <div v-if="streaming" class="message-item assistant">
            <div class="avatar-col">
              <div class="ai-avatar">往</div>
            </div>
            <div class="message-content">
              <div class="message-sender">往 AI</div>
              <div class="bubble assistant">
                <div class="text-content">{{ streamContent }}<span class="cursor-dot">&#9608;</span></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="input-area">
          <div v-if="pendingFile" class="pending-file">
            <span class="pending-file-tag">
              <svg width="14" height="14" viewBox="0 0 14 14" fill="none"><path d="M7 1v12M1 7h12" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
              {{ pendingFile.name }}
              <button class="pending-file-close" @click="pendingFile = null">
                <svg width="12" height="12" viewBox="0 0 12 12" fill="none"><path d="M2 2l8 8M10 2l-8 8" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
              </button>
            </span>
          </div>
          <div class="input-row">
            <el-upload
              :show-file-list="false"
              :before-upload="handleFileSelect"
              accept="image/*,audio/*,video/*"
            >
              <button class="attach-btn" title="上传文件">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M10 3v10M5 8l5-5 5 5M4 14v2a1 1 0 001 1h10a1 1 0 001-1v-2" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
              </button>
            </el-upload>
            <div class="input-field">
              <input
                v-model="inputText"
                placeholder="输入消息，或上传文件进行分析..."
                @keydown.enter.prevent="handleSend"
                :disabled="streaming"
              />
            </div>
            <button
              class="send-btn"
              :class="{ active: canSend }"
              :disabled="!canSend"
              @click="handleSend"
            >
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
                <path d="M3 9l12-6-6 12-2-4-4-2z" stroke="currentColor" stroke-width="1.3" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import { formatDateTime, resolveMediaUrl } from '@/utils/format'
import {
  getConversationList, createConversation, deleteConversation,
  getMessageList, uploadFile
} from '@/api/chat'

const router = useRouter()
const userStore = useUserStore()

const conversations = ref([])
const currentConvId = ref(null)
const isNewChat = ref(false)
const messages = ref([])
const inputText = ref('')
const streaming = ref(false)
const streamContent = ref('')
const pendingFile = ref(null)
const messageListRef = ref()

const canSend = computed(() => {
  return (inputText.value.trim() || pendingFile.value) && !streaming.value
})

/** 加载会话列表 */
async function loadConversations() {
  const res = await getConversationList()
  conversations.value = res.data || []
}

/** 新建对话 */
function handleNewChat() {
  currentConvId.value = null
  isNewChat.value = true
  messages.value = []
  inputText.value = ''
  pendingFile.value = null
}

/** 选择会话 */
async function selectConversation(conv) {
  currentConvId.value = conv.id
  isNewChat.value = false
  const res = await getMessageList(conv.id)
  messages.value = res.data || []
  scrollToBottom()
}

/** 删除会话 */
async function handleDeleteConv(id) {
  await ElMessageBox.confirm('确定删除该会话？', '提示', { type: 'warning' })
  await deleteConversation(id)
  if (currentConvId.value === id) {
    currentConvId.value = null
    messages.value = []
  }
  loadConversations()
}

/** 文件选择 */
function handleFileSelect(file) {
  const isImage = file.type.startsWith('image/')
  const isAudio = file.type.startsWith('audio/')
  const isVideo = file.type.startsWith('video/')
  if (!isImage && !isAudio && !isVideo) {
    ElMessage.warning('仅支持图片、音频、视频文件')
    return false
  }
  if (file.size > 50 * 1024 * 1024) {
    ElMessage.warning('文件大小不能超过50MB')
    return false
  }
  pendingFile.value = { file, name: file.name, contentType: isImage ? 'image' : isAudio ? 'audio' : 'video' }
  return false
}

/** 发送消息(SSE流式) */
async function handleSend() {
  if (!canSend.value) return

  let fileUrl = null
  let contentType = 'text'

  if (pendingFile.value) {
    const uploadRes = await uploadFile(pendingFile.value.file)
    fileUrl = uploadRes.data.url
    contentType = uploadRes.data.contentType
  }

  const content = inputText.value.trim() || (pendingFile.value ? `请分析这个${contentType === 'image' ? '图片' : contentType === 'audio' ? '音频' : '视频'}` : '')
  const convId = currentConvId.value

  // 添加用户消息到界面
  messages.value.push({
    id: Date.now(),
    role: 'user',
    contentType,
    content,
    fileUrl,
    createTime: new Date().toISOString()
  })
  inputText.value = ''
  pendingFile.value = null
  streaming.value = true
  streamContent.value = ''
  scrollToBottom()

  // 构建SSE请求参数
  const params = new URLSearchParams()
  if (convId) params.append('conversationId', convId)
  if (content) params.append('content', content)
  params.append('contentType', contentType)
  if (fileUrl) params.append('fileUrl', fileUrl)

  // 使用 fetch + ReadableStream 接收 SSE 流式响应
  try {
    const response = await fetch(`/api/chat/stream?${params.toString()}`, {
      headers: { Authorization: `Bearer ${userStore.token}` }
    })

    if (!response.ok) {
      throw new Error('请求失败')
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''
    let currentEvent = 'message'

    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })

      const parts = buffer.split('\n')
      buffer = parts.pop() || ''

      for (const line of parts) {
        if (line.startsWith('event:')) {
          currentEvent = line.substring(6).trim()
        } else if (line.startsWith('data:')) {
          const data = line.substring(5).trim()
          if (currentEvent === 'conversation') {
            currentConvId.value = Number(data)
            isNewChat.value = false
          } else if (currentEvent === 'message') {
            streamContent.value += data
            scrollToBottom()
          } else if (currentEvent === 'done') {
            streaming.value = false
            messages.value.push({
              id: Date.now() + 1,
              role: 'assistant',
              contentType: 'text',
              content: streamContent.value,
              createTime: new Date().toISOString()
            })
            streamContent.value = ''
            loadConversations()
            scrollToBottom()
          } else if (currentEvent === 'error') {
            ElMessage.error(data)
            streaming.value = false
          }
        } else if (line === '') {
          currentEvent = 'message'
        }
      }
    }

    if (streaming.value) {
      streaming.value = false
      if (streamContent.value) {
        messages.value.push({
          id: Date.now() + 1,
          role: 'assistant',
          contentType: 'text',
          content: streamContent.value,
          createTime: new Date().toISOString()
        })
        streamContent.value = ''
        loadConversations()
        scrollToBottom()
      }
    }
  } catch (err) {
    ElMessage.error('聊天请求失败')
    streaming.value = false
  }
}

/** 滚动到底部 */
function scrollToBottom() {
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
}

/** 退出登录 */
function handleLogout() {
  userStore.logout()
  router.push('/login')
}

loadConversations()
</script>

<style scoped>
/* ===== Layout ===== */
.chat-page {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: #f6f8fc;
}

/* ===== Sidebar ===== */
.sidebar {
  width: 280px;
  min-height: 0;
  background: #fff;
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.sidebar-header {
  flex-shrink: 0;
  padding: 18px 16px 14px;
  border-bottom: 1px solid var(--border-light);
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
}

.brand-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  letter-spacing: -0.01em;
}

.new-chat-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  width: 100%;
  height: 36px;
  background: var(--brand-gradient);
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.15s;
}

.new-chat-btn:hover { opacity: 0.9; }

.conversation-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 8px;
}

.conv-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.15s;
  margin-bottom: 2px;
}

.conv-item:hover { background: var(--surface-muted); }
.conv-item.active {
  background: rgba(26, 86, 219, 0.06);
}

.conv-item.active .conv-icon { color: var(--primary); }
.conv-item.active .conv-title { color: var(--primary); font-weight: 500; }

.conv-icon {
  flex-shrink: 0;
  color: var(--text-tertiary);
}

.conv-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
  color: var(--text-primary);
}

.conv-delete {
  flex-shrink: 0;
  display: none;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: none;
  border: none;
  border-radius: 6px;
  color: var(--text-tertiary);
  cursor: pointer;
}

.conv-item:hover .conv-delete { display: flex; }
.conv-delete:hover { background: rgba(220, 38, 38, 0.08); color: var(--danger); }

.conv-empty {
  padding: 32px 16px;
  text-align: center;
  color: var(--text-tertiary);
  font-size: 13px;
  line-height: 1.6;
}

.sidebar-footer {
  flex-shrink: 0;
  padding: 12px 16px;
  border-top: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.user-avatar-small {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  background: var(--brand-gradient);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}

.user-name {
  font-size: 13px;
  color: var(--text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  background: none;
  border: none;
  border-radius: 8px;
  color: var(--text-tertiary);
  cursor: pointer;
  transition: background 0.15s;
}

.logout-btn:hover { background: rgba(220, 38, 38, 0.08); color: var(--danger); }

/* ===== Chat Main ===== */
.chat-main {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #fff;
}

/* ===== Welcome ===== */
.welcome {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 40px;
}

.welcome-brand {
  margin-bottom: 24px;
}

.welcome-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 72px;
  height: 72px;
  background: var(--brand-gradient);
  border-radius: 20px;
  color: #fff;
  font-size: 32px;
  font-weight: 700;
  box-shadow: 0 12px 32px rgba(26, 86, 219, 0.25);
}

.welcome h2 {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
  letter-spacing: -0.01em;
}

.welcome-desc {
  color: var(--text-tertiary);
  font-size: 14px;
  margin-bottom: 32px;
}

.welcome-hints {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.hint-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: var(--text-tertiary);
  font-size: 13px;
}

.hint-item svg { color: var(--primary); flex-shrink: 0; }

/* ===== Chat Area ===== */
.chat-area {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* ===== Message List ===== */
.message-list {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 24px 32px;
}

.message-item {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-item.user .message-content {
  align-items: flex-end;
}

.avatar-col {
  flex-shrink: 0;
}

.ai-avatar {
  width: 32px;
  height: 32px;
  border-radius: 10px;
  background: var(--brand-gradient);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  max-width: 72%;
}

.message-sender {
  font-size: 12px;
  color: var(--text-tertiary);
  font-weight: 500;
  padding: 0 4px;
}

.bubble {
  padding: 12px 16px;
  border-radius: 4px 14px 14px 14px;
  background: var(--surface-muted);
  line-height: 1.65;
  font-size: 14px;
}

.message-item.user .bubble {
  background: rgba(26, 86, 219, 0.08);
  border-radius: 14px 4px 14px 14px;
}

.text-content {
  white-space: pre-wrap;
  word-break: break-word;
  color: var(--text-primary);
}

.media-caption {
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 6px;
}

.msg-time {
  font-size: 11px;
  color: var(--text-tertiary);
  padding: 0 4px;
}

.cursor-dot {
  animation: blink 1s step-end infinite;
  color: var(--primary);
  font-size: 12px;
}

@keyframes blink {
  50% { opacity: 0; }
}

/* ===== Input Area ===== */
.input-area {
  flex-shrink: 0;
  padding: 12px 24px 20px;
  border-top: 1px solid var(--border-light);
  background: #fff;
}

.pending-file {
  margin-bottom: 8px;
  padding: 0 4px;
}

.pending-file-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px 4px 10px;
  background: var(--surface-muted);
  border-radius: 8px;
  font-size: 13px;
  color: var(--text-secondary);
}

.pending-file-tag svg { color: var(--primary); }

.pending-file-close {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  background: none;
  border: none;
  border-radius: 4px;
  color: var(--text-tertiary);
  cursor: pointer;
}

.pending-file-close:hover { background: rgba(220, 38, 38, 0.1); color: var(--danger); }

.input-row {
  display: flex;
  gap: 8px;
  align-items: flex-end;
}

.attach-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 38px;
  height: 38px;
  background: none;
  border: 1px solid var(--border);
  border-radius: 10px;
  color: var(--text-tertiary);
  cursor: pointer;
  transition: all 0.15s;
}

.attach-btn:hover {
  border-color: var(--primary);
  color: var(--primary);
  background: rgba(26, 86, 219, 0.04);
}

.input-field {
  flex: 1;
  position: relative;
}

.input-field input {
  width: 100%;
  height: 38px;
  padding: 0 14px;
  border: 1px solid var(--border);
  border-radius: 10px;
  font-size: 14px;
  color: var(--text-primary);
  background: var(--surface-muted);
  outline: none;
  transition: border-color 0.15s, box-shadow 0.15s;
  font-family: inherit;
}

.input-field input:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(26, 86, 219, 0.08);
}

.input-field input::placeholder {
  color: var(--text-tertiary);
}

.send-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 38px;
  height: 38px;
  border: none;
  border-radius: 10px;
  background: var(--surface-muted);
  color: var(--text-tertiary);
  cursor: not-allowed;
  transition: all 0.15s;
}

.send-btn.active {
  background: var(--brand-gradient);
  color: #fff;
  cursor: pointer;
}

.send-btn.active:hover {
  opacity: 0.9;
}
</style>
