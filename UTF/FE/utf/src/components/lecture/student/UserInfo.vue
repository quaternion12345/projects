<template>
  <div class="user-box">
    <div class="user-box-outer">
      <div class="user-box-inner">
        <p class="user-name">{{ username }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserInfo",
  props: ["subscriber"],
  data() {
    return {
      username: this.subscriber.stream.connection.data.slice(15, -2),
    };
  },
  methods: {},
};
</script>
<style>
.user-icon {
  color: black;
  margin: 3px;
}
.user-name {
  color: black;
  margin: 3px;
}
.user-time {
  color: black;
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
/* .chat__yourmessage__user {
  text-align: left;
  font-size: 14px;
  font-weight: 700;
  color: #292929;
  margin-top: 0;
  margin-block-end: 0rem;
} */
</style>
