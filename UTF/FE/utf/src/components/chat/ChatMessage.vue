<template>
  <div class="message-box">
    <div v-if="msg.from.type === 'general'" class="chat-message-box-outer">
      <div class="chat-message-box-inner">
        <p class="chat-message-sender">{{ msg.from.name + ": " }}</p>
        <p class="chat-message-content">{{ msg.msg }}</p>
      </div>
      <p class="chat-message-time">{{ msg.from.time }}</p>
    </div>
    <div v-if="msg.from.type === 'alert'" class="alert-message-box-outer">
      <div class="chat-message-box-inner">
        <p class="alert-message-content">{{ msg.msg }}</p>
      </div>
      <p class="chat-message-time">{{ msg.from.time }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChatMessage",
  props: ["msg"],
  data() {
    return {
      isSame: false,
    };
  },
  methods: {},
};
</script>
<style>
.chat-message-sender {
  font-weight: bold;
  text-align: justify;
  vertical-align: top;
  min-width: 50px;
  margin-bottom: 0.5rem;
}
.chat-message-content {
  font-weight: lighter;
  text-align: justify;
  margin-bottom: 0.5rem;
  line-break: anywhere;
}
.chat-message-time {
  flex-direction: column;
  text-align: left;
  margin: 0;
  font-size: 12px;
  color: #9c9c9c;
}
.chat-message-box-inner {
  display: flex;
  align-items: flex-start;
  white-space: pre-wrap;
}
.chat-message-box-outer {
  border-radius: 15px 15px 15px 15px;
  background-color: #f3f3f3;
  /* max-width: 180px; */
  color: #414141;
  padding: 0.8rem;
  font-size: 14px;
  width: 100%;
}
.alert-message-content {
  font-weight: bold;
  text-align: justify;
  margin-bottom: 0.5rem;
  line-break: anywhere;
}
.alert-message-box-outer {
  border-radius: 15px 15px 15px 15px;
  background-color: rgba(0, 0, 0, 0.75);
  /* max-width: 180px; */
  color: white;
  padding: 0.8rem;
  font-size: 14px;
  width: 100%;
}
.message-box {
  margin-top: 1rem;
}
</style>
