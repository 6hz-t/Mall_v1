import { Notification } from 'element-ui'

// 初始化方法
export function initWebSocket() {
  const ws = 'ws://localhost:8012/websocket/1'
  // 实例化socket
  this.socket = new WebSocket(ws)
  // 监听socket连接
  this.socket.onopen = this.onopen
  // 监听socket错误信息
  this.socket.onerror = this.onerror
  // 监听socket消息
  this.socket.onmessage = this.onMessage
  // 监听socket断开连接的消息
  this.socket.close = this.close
}

// 连接成功方法
export function onopen() {
  console.log('socket连接成功')
}
// 连接错误
export function onerror() {
  console.log('连接错误')
}
// 接受信息接口
export function onMessage(message) {
  console.log('收到消息:' + message)
  const data = message.data
  Notification.success({
    title: '消息通知',
    type: 'success',
    message: data,
    duration: 10000,
    position: 'top-right',
    dangerouslyUseHTMLString: true,
    onClick: () => {
      const notificationContent = document.querySelector('.el-notification__content')
      const link = notificationContent.querySelector('a')
      if (link) {
        link.addEventListener('click', (e) => {
          e.preventDefault()
          const href = link.getAttribute('href')
          window.location.href = href
        })
      }
    }
  })
}

export default { initWebSocket, onopen, onerror, onMessage }
