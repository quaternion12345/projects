<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="secondary" show><h3>회원정보수정</h3></b-alert>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="6">
        <b-card class="text-center mt-3" style="max-width: 40rem" align="left">
          <h3>기본정보</h3>
          <b-form class="text-left">
            <b-form-group label="아이디:" label-for="userid">
              <b-form-input
                readonly
                id="userid"
                v-model="user.userid"
                required
                placeholder="아이디 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="비밀번호:" label-for="userpwd">
              <b-form-input
                type="password"
                id="userpwd"
                v-model="user.userpwd"
                required
                placeholder="비밀번호 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="이름:" label-for="username">
              <b-form-input
                id="username"
                v-model="user.username"
                required
                placeholder="이름 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="이메일:" label-for="useremail">
              <b-form-input
                type="email"
                id="useremail"
                v-model="user.email"
                required
                placeholder="이메일 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-button
              type="button"
              variant="primary"
              class="m-1"
              @click="modify"
              >완료</b-button
            >
            <b-button
              type="button"
              variant="danger"
              class="m-1"
              @click="cancelModify"
              >취소</b-button
            >
          </b-form>
        </b-card>
      </b-col>
      <b-col>
        <b-card style="max-width: 40rem" align="left" class="m-3 text-center">
          <div class="h4 mb-2">
            관심지역<b-button
              type="button"
              variant="success"
              class="m-1"
              @click="movePage"
              >추가</b-button
            >
          </div>
          <b-form-input
            id="interestSearch"
            class="mb-2"
            v-model="interestSearch"
            required
            placeholder="관심지역 검색...."
            @change="interestSearchResult()"
          ></b-form-input>
          <b-list-group>
            <b-list-group-item
              >Cras justo odio
              <b-icon
                icon="trash-fill"
                aria-hidden="true"
                variant="danger"
              ></b-icon
            ></b-list-group-item>
            <b-list-group-item
              >Dapibus ac facilisis in
              <b-icon
                icon="trash-fill"
                aria-hidden="true"
                variant="danger"
              ></b-icon
            ></b-list-group-item> </b-list-group
        ></b-card>
        <b-card style="max-width: 40rem" align="left" class="m-3">
          <div class="h4 mb-2 text-center">
            자주 가는 장소<b-icon
              icon="exclamation-circle-fill"
              variant="secondary"
              v-b-popover.hover="'지도에서 추가할 수 있습니다.'"
            ></b-icon>
          </div>

          <b-list-group>
            <b-list-group-item
              >Cras justo odio
              <b-icon
                icon="trash-fill"
                aria-hidden="true"
                variant="danger"
              ></b-icon
            ></b-list-group-item>
            <b-list-group-item
              >Dapibus ac facilisis in
              <b-icon
                icon="trash-fill"
                aria-hidden="true"
                variant="danger"
              ></b-icon
            ></b-list-group-item> </b-list-group
        ></b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { apiInstance } from "@/api/index.js";

const api = apiInstance();

const memberStore = "memberStore";

export default {
  name: "MemberRegister",

  data() {
    return {
      user: {
        userid: null,
        userpwd: null,
        username: null,
        email: null,
      },
      interestSearch: null,
    };
  },
  created() {
    this.user.userid = this.userInfo.userid;
    this.user.userpwd = this.userInfo.userpwd;
    this.user.username = this.userInfo.username;
    this.user.email = this.userInfo.email;
    this.user.joindate = this.userInfo.joindate;
    console.log(this.user);
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
  },
  methods: {
    ...mapActions(memberStore, ["getUserInfo"]),
    modify() {
      // this.modifyUser(this.user);
      api
        .put(`/user/modify/${this.user.userid}`, {
          userid: this.user.userid,
          username: this.user.username,
          userpwd: this.user.userpwd,
          email: this.user.email,
          joindate: this.user.joindate,
        })
        .then(({ data }) => {
          let msg = "회원정보 수정 시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "회원정보 수정이 완료되었습니다.";
            let token = sessionStorage.getItem("access-token");
            this.getUserInfo(token);
          }
          alert(msg);
        })
        .catch(({ error }) => {
          console.log(error);
          alert("회원정보 수정 시 문제가 발생했습니다.");
        });
      this.movePage();
    },
    movePage() {
      this.$router.push({ name: "mypage" });
    },
    cancelModify() {
      this.$router.push({ name: "mypage" });
    },
    interestSearchResult() {},
  },
};
</script>

<style></style>
