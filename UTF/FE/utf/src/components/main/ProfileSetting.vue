<!-- eslint-disable prettier/prettier -->
<template>
  <div class="container">
    <div :class="{ profileBox: !darkMode, profileBoxDark: darkMode }">
      Edit profile<br />
      <label for="profileInputName">name</label><br />
      <input id="profileInputName" v-model="userName" /><br />
      <label for="profileInputEmail">email</label><br />
      <input id="profileInputEmail" v-model="email" /><br />
      <div class="btnGroup mt-3">
        <button type="button" class="blueBtn" @click="updateUser()">수정</button>
        <button type="button" class="redBtn" @click="cancel()">취소</button>
      </div>
      <b-row>
        <b-col sm="1"></b-col>
        <b-col sm="2">
          <!-- <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteUserModal" style="margin-right: 50px">탈퇴</button> -->
          <p @click="deleteUser()">탈퇴</p>
        </b-col>
      </b-row>
    </div>
  </div>
</template>

<script>
/* eslint-disable prettier/prettier */
import store from "@/store";
import { updateUserRequest } from "@/api/index.js";
import { deleteUserRequest } from "@/api/index.js";
export default {
  props: {
    darkMode: Boolean,
  },
  data() {
    return {
      userName: store.state.userInfo["userName"],
      email: store.state.userInfo["email"],
    };
  },
  methods: {
    cancel() {
      this.userName = store.state.userInfo["userName"];
      this.email = store.state.userInfo["email"];
      this.$emit("cancel");
    },
    deleteUser() {
      let flag = confirm("정말로 탈퇴하시겠습니까??");
      if (flag) {
        deleteUserRequest(store.state.userInfo["userId"]);
        store.replaceState = {};
        sessionStorage.clear();
      }
    },
    async updateUser() {
      await updateUserRequest(store.state.userInfo["userId"], this.userName, this.email);
      // this.$emit("toggle");
      window.location.reload();
    },
  },
};
</script>

<style scoped>
#profileInputName,
#profileInputEmail {
  width: 80%;
  border: none;
  border-radius: 10px;
  margin: auto;
  background-color: #e2ebff;
}
p {
  cursor: pointer;
  font-size: 12px;
  color: red;
}
.btnGroup {
  display: flex;
  justify-content: flex-end;
}
.btnGroup button {
  margin-right: 10px;
}
label{
  position: relative;
  right: 32%;
}
</style>
