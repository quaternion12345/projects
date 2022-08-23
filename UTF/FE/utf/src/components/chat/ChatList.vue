<template>
  <div class="chat-body" id="chat-body">
    <chat-message v-for="(msg, index) in msgs" :key="index" :msg="msg">
    </chat-message>
  </div>
</template>

<script>
import ChatMessage from "@/components/chat/ChatMessage";
export default {
  name: "ChatList",
  props: ["msgs"],
  components: {
    ChatMessage,
  },
};
</script>

<style>
.chat-body {
  padding: 1rem;
  overflow: scroll;
  scroll-behavior: smooth;
  background: #ffffff;
  /* box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05); */
  height: 100%;
  max-height: 100%;
  border-radius: 12px 12px 0px 0px;
}
/* .chat-body::-webkit-scrollbar {
  display: none;
} */
.chat-body::-webkit-scrollbar {
  width: 6px;
}
.chat-body::-webkit-scrollbar-track {
  background-color: transparent;
}
.chat-body::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background-color: gray;
}
.chat-body::-webkit-scrollbar-button {
  width: 0;
  height: 0;
}
</style>
