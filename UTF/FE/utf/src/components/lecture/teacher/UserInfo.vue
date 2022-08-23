<template>
  <div class="user-box">
    <div class="user-box-outer">
      <div class="user-box-inner">
        <p :class="[time ? 'absent-user-name' : 'user-name']">{{ username }}</p>
        <p v-if="time" :class="[time ? 'absent-user-time' : 'user-time']">
          {{ "[" + elapsedtime + "]" }}
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserInfo",
  props: ["subscriber", "time"],
  data() {
    return {
      username: this.subscriber.stream.connection.data.slice(15, -2),
      elapsedtime: undefined,
    };
  },
  created() {
    setInterval(this.elapsedTime.bind(this), 1000);
  },
  methods: {
    elapsedTime() {
      var diff = Date.now() - Number(this.time);
      var Min = Math.floor(diff / 1000 / 60);
      var Sec = Math.floor(diff / 1000) % 60;
      this.elapsedtime =
        Min.toString().padStart(2, "0") + ":" + Sec.toString().padStart(2, "0");
    },
  },
};
</script>
<style>
.user-name {
  color: black;
  margin: 3px;
}
.user-time {
  color: black;
  margin: 3px;
}
.absent-user-name {
  color: crimson;
  margin: 3px;
}
.absent-user-time {
  color: crimson;
  margin: 3px;
}
.user-box-inner {
  display: flex;
  font-weight: bold;
  text-align: justify;
  align-items: flex-start;
  vertical-align: top;
  white-space: pre-wrap;
}
.user-box-outer {
  border-radius: 15px 15px 15px 15px;
  background-color: #f3f3f3;
  /* max-width: 180px; */
  color: #414141;
  padding: 0.8rem;
  font-size: 14px;
  width: 100%;
}
.user-box {
  margin-top: 1rem;
}
</style>
