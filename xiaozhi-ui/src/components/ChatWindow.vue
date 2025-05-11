<template>
  <div class="app-layout">

    <!-- 左侧边栏区域 -->
    <div class="sidebar">

      <!-- Logo 区域 -->
      <div class="logo-section">
        <img src="@/assets/logo.png" alt="小智" width="160" height="160" />
        <span class="logo-text">小智（医疗版）</span>
      </div>

      <!-- 新会话按钮 -->
      <el-button class="new-chat-button" @click="newChat">
        <i class="fa-solid fa-plus"></i>
        &nbsp;新会话
      </el-button>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 聊天窗口容器 -->
      <div class="chat-container">

        <!-- 消息列表区域 -->
        <div class="message-list" ref="messaggListRef">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="
              message.isUser ? 'message user-message' : 'message bot-message'
            "
          >
            <!-- 会话图标 -->
            <i
              :class="
                message.isUser
                  ? 'fa-solid fa-user message-icon'
                  : 'fa-solid fa-robot message-icon'
              "
            ></i>
            <!-- 会话内容 -->
            <span>
              <span v-html="message.content"></span>
              <!-- loading -->
              <span
                class="loading-dots"
                v-if="message.isThinking || message.isTyping"
              >
                <span class="dot"></span>
                <span class="dot"></span>
              </span>
            </span>
          </div>
        </div>

        <!-- 输入框和发送按钮区域 按下回车键(enter)/按按钮 都会触发sendMessage事件-->
        <div class="input-container">
          <el-input
            v-model="inputMessage"
            placeholder="请输入消息"
            @keyup.enter="sendMessage" 
          ></el-input>
          <el-button @click="sendMessage" :disabled="isSending" type="primary"
            >发送</el-button
          >
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>

import { onMounted, ref, watch } from 'vue'
import axios from 'axios'
import { v4 as uuidv4 } from 'uuid'

const messaggListRef = ref()
const isSending = ref(false)
const uuid = ref()
const inputMessage = ref('')
const messages = ref([]) // 数组

// 组件挂载后执行
onMounted(() => {
  //1、生成uuid，作为messageId
  initUUID()
  //2、深度监听messages：每当数组发生变化，将聊天窗口自动滚动到底部。
  watch(messages, () => scrollToBottom(), { deep: true })
  //3、sayhello
  hello()
})

 
// 把数组自动滚动到底部
const scrollToBottom = () => {
  if (messaggListRef.value) {
    // scrollTop：元素内容垂直滚动的像素数。
    // scrollHeight:元素内容的总高度。
    // 将滚动条位置设置为最大值，使聊天窗口自动滚动到底部。
    messaggListRef.value.scrollTop = messaggListRef.value.scrollHeight
  }
}

const hello = () => {
  sendRequest('你好')
}

const sendMessage = () => {
  if (inputMessage.value.trim()) {
    // 把输入的信息放入 messages 数组
    sendRequest(inputMessage.value.trim())
    inputMessage.value = ''
  }
}

// 发送请求！！
const sendRequest = (message) => {
  isSending.value = true
  const userMsg = {
    isUser: true,
    content: message,
    isTyping: false,
    isThinking: false,
  }
  //第一条默认发送的用户消息”你好“不放入会话列表
  if(messages.value.length > 0){
    messages.value.push(userMsg)
  }


  // 用于在用户发送消息后，添加 机器人 的占位消息和加载动画。
  const botMsg = {
    isUser: false,
    content: '', // 增量填充
    isTyping: true, // 显示加载动画
    isThinking: false,
  }
  messages.value.push(botMsg)

  // 最新信息
  const lastMsg = messages.value[messages.value.length - 1]
 
  // 立即滚动到底部（显示加载动画）
  scrollToBottom()

  axios
    .post(
      //1、访问地址
      '/api/xiaozhi/chat',
      //2、发送的信息
      { memoryId: uuid.value, message },
      //3、配置对象，允许你定义各种请求设置。
      {
        // 下载进度监听函数，处理服务器逐步返回的文本内容，每次接收到新数据时都会触发此回调
        responseType: 'stream', // 必须为合法值 "text"
        onDownloadProgress: (e) => {
          // 获取当前已接收到的完整文本（累积内容），流式的，一个字一个字增加的！
          const fullText = e.event.target.responseText // 累积的完整文本
          // 提取新增的部分：从上次内容长度之后截取
          let newText = fullText.substring(lastMsg.content.length)
          // 将新内容追加到当前消息中，实现“增量更新”
          lastMsg.content += newText
          // 页面自动滚动到底部，显示最新回复内容
          scrollToBottom() 
        },
      }
    )
    // 成功
    .then(() => {
      // 流结束后隐藏加载动画
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
    // 失败
    .catch((error) => {
      console.error('流式错误:', error)
      // 修改最后一条消息内容为提示语句
      messages.value.at(-1).content = '请求失败，请重试'
      // 停止打字动画
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
}

// 初始化 UUID
const initUUID = () => {
  let storedUUID = localStorage.getItem('user_uuid')
  if (!storedUUID) {
    storedUUID = uuidToNumber(uuidv4())
    localStorage.setItem('user_uuid', storedUUID)
  }
  uuid.value = storedUUID
}

const uuidToNumber = (uuid) => {
  let number = 0
  for (let i = 0; i < uuid.length && i < 6; i++) {
    const hexValue = uuid[i]
    number = number * 16 + (parseInt(hexValue, 16) || 0)
  }
  return number % 1000000
}

// 转换特殊字符
const convertStreamOutput = (output) => {
  return output
    .replace(/\n/g, '<br>')
    .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
    .replace(/&/g, '&amp;') // 新增转义，避免 HTML 注入
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
}

const newChat = () => {
  // 这里添加新会话的逻辑
  console.log('开始新会话')
  localStorage.removeItem('user_uuid')
  // 重新加载当前页面
  window.location.reload()
}
</script>


<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  background-color: #f4f4f9;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  margin-top: 10px;
}

.new-chat-button {
  width: 100%;
  margin-top: 20px;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background-color: #fff;
  margin-bottom: 10px;
  display: flex;
  flex-direction: column;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  display: flex;
  /* align-items: center; */
}

.user-message {
  max-width: 70%;
  background-color: #e1f5fe;
  align-self: flex-end;
  flex-direction: row-reverse;
}

.bot-message {
  max-width: 100%;
  background-color: #f1f8e9;
  align-self: flex-start;
}

.message-icon {
  margin: 0 10px;
  font-size: 1.2em;
}

.loading-dots {
  padding-left: 5px;
}

.dot {
  display: inline-block;
  margin-left: 5px;
  width: 8px;
  height: 8px;
  background-color: #000000;
  border-radius: 50%;
  animation: pulse 1.2s infinite ease-in-out both;
}

.dot:nth-child(2) {
  animation-delay: -0.6s;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(0.6);
    opacity: 0.4;
  }

  50% {
    transform: scale(1);
    opacity: 1;
  }
}
.input-container {
  display: flex;
}

.input-container .el-input {
  flex: 1;
  margin-right: 10px;
}

/* 媒体查询，当设备宽度小于等于 768px 时应用以下样式 */
@media (max-width: 768px) {
  .main-content {
    padding: 10px 0 10px 0;
  }
  .app-layout {
    flex-direction: column;
  }

  .sidebar {
    /* display: none; */
    width: 100%;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
  }

  .logo-section {
    flex-direction: row;
    align-items: center;
  }

  .logo-text {
    font-size: 20px;
  }

  .logo-section img {
    width: 40px;
    height: 40px;
  }

  .new-chat-button {
    margin-right: 30px;
    width: auto;
    margin-top: 5px;
  }
}

/* 媒体查询，当设备宽度大于 768px 时应用原来的样式 */
@media (min-width: 769px) {
  .main-content {
    padding: 0 0 10px 10px;
  }

  .app-layout {
    display: flex;
    height: 100vh;
  }

  .sidebar {
    width: 200px;
    background-color: #f4f4f9;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .logo-section {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .logo-text {
    font-size: 18px;
    font-weight: bold;
    margin-top: 10px;
  }

  .new-chat-button {
    width: 100%;
    margin-top: 20px;
  }
}
</style>
