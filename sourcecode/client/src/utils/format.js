import dayjs from 'dayjs'

/**
 * 格式化日期 yyyy-MM-dd
 * @param {string|Date} date 日期
 * @returns {string}
 */
export function formatDate(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD')
}

/**
 * 格式化日期时间 yyyy-MM-dd HH:mm:ss
 * @param {string|Date} date 日期时间
 * @returns {string}
 */
export function formatDateTime(date) {
  if (!date) return ''
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

/**
 * 解析媒体文件访问地址（兼容历史数据中的 /uploads7 路径）
 * @param {string} url 文件URL
 * @returns {string}
 */
export function resolveMediaUrl(url) {
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://')) return url
  if (url.startsWith('/api/')) return url
  if (url.startsWith('/uploads7/')) return `/api${url}`
  return url
}
