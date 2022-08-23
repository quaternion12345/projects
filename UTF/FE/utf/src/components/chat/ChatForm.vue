<template>
  <div class="chat-form">
    <input
      class="form-input"
      type="text"
      placeholder="메세지를 입력하세요."
      v-model.trim="msg"
      spellcheck="false"
      @keyup.enter="submitMessage"
    />
    <div @click="submitMessage" class="form-submit">
      <!-- svg를 이용한 전송버튼 -->
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="30"
        height="30"
        viewBox="0 0 68 68"
        fill="#CCCCCC"
      >
        <path
          fill-rule="evenodd"
          clip-rule="evenodd"
          d="M48.0833 19.799C48.619 20.3347 48.806 21.127 48.5665 21.8457L35.8385 60.0294C35.5946 60.7614 34.9513 61.2877 34.1855 61.382C33.4198 61.4763 32.6681 61.1217 32.2539 60.4707L22.593 45.2893L7.41158 35.6285C6.76065 35.2142 6.40604 34.4625 6.50031 33.6968C6.59458 32.931 7.12092 32.2878 7.85287 32.0438L46.0366 19.3159C46.7553 19.0763 47.5476 19.2633 48.0833 19.799ZM26.5903 44.1204L33.3726 54.7782L42.0926 28.6181L26.5903 44.1204ZM39.2642 25.7897L23.7619 41.292L13.1041 34.5097L39.2642 25.7897Z"
          fill=""
        />
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChatForm",
  data() {
    return {
      msg: "",
    };
  },
  methods: {
    submitMessage() {
      if (this.msg) {
        this.$emit("submitMessage", this.msg);
        this.msg = "";
      }
      return;
    },
  },
};
</script>

<style scoped>
.chat-form {
  display: flex;
  justify-content: space-between;
  padding: 1.4rem;
  background: #ffffff;
  border-radius: 12px 12px 12px 12px;
  box-shadow: 0px -5px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid #999999;
  height: 100px;
}
.form-input {
  border: none;
  padding: 0.5rem;
  font-size: 16px;
  width: calc(100% - 60px);
}
.form-input:focus {
  outline: none;
}
.form-submit {
  display: flex;
  align-items: center;
  cursor: pointer;
}
svg {
  transition: 0.3s;
}
svg:hover {
  fill: #999999;
}
</style>
