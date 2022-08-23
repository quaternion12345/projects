<template>
  <div class="chat-body">
    <lecture-chat-message v-for="(msg, index) in msgs" :key="index" :msg="msg">
    </lecture-chat-message>
  </div>
</template>

<script>
import LectureChatMessage from "@/components/statistics/LectureChatMessage";
export default {
  name: "LectureChat",
  props: ["msgs"],
  components: {
    LectureChatMessage,
  },
};
</script>

<style>
.chat-component {
  height: 80vh;
  background-color: white;
  border-radius: 15px;
}
.chat-body {
  padding: 1rem;
  overflow: scroll;
  scroll-behavior: smooth;
  background: #ffffff;
  /* box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05); */
  height: 100%;
  max-height: 100%;
  border-radius: 0px 0px 15px 15px;
  border-top: 3px solid #f3f3f3;
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
