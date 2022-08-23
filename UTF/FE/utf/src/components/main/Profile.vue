<!-- eslint-disable prettier/prettier -->
<template>
  <b-container>
    <div :class="{ profileBox: !darkMode, profileBoxDark: darkMode }">
      <b-row class="row" style="margin-top: 13px; margin-bottom: 10px">
        <b-col sm="10" class="welcomeMsg" style="padding: 0px">
          <div style="margin-left: 7px">{{ userName }} 강사님 환영합니다</div>
        </b-col>
        <b-col sm="2" class="btnGroup" style="padding: 0px">
          <i class="bi bi-gear buttons" id="settingBtn" style="font-size: 20px; margin-right: 6px" @click="updateProfile()"></i>
          <i class="bi bi-box-arrow-right buttons" id="logoutBtn" style="font-size: 20px" @click="logout()"></i>
        </b-col>
      </b-row>
    </div>
  </b-container>
</template>

<script>
/* eslint-disable prettier/prettier */
import store from "@/store";

export default {
  name: "ProfileComponent",
  props: {
    darkMode: Boolean,
  },
  data() {
    return {
      userName: store.state.userInfo["userName"],
    };
  },
  methods: {
    updateProfile() {
      this.$emit("setting");
    },
    logout() {
      let flag = confirm("로그아웃 하시겠습니까??");
      if (flag) {
        this.$emit("logout");
      }
    },
  },
};
</script>

<style scoped>
.row {
  width: 100%;
}
.welcomeMsg {
  display: flex;
  align-items: center;
  justify-content: center;
}
.btnGroup {
  vertical-align: middle;
}
</style>
