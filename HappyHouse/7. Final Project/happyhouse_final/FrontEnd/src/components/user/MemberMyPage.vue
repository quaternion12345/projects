<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert variant="secondary" show><h3>회원정보</h3></b-alert>
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
                v-model="userInfo.userid"
                required
                placeholder="아이디 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="비밀번호:" label-for="userpwd">
              <b-form-input
                readonly
                type="password"
                id="userpwd"
                v-model="userInfo.userpwd"
                required
                placeholder="비밀번호 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="이름:" label-for="username">
              <b-form-input
                readonly
                id="username"
                v-model="userInfo.username"
                required
                placeholder="이름 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-form-group label="이메일:" label-for="useremail">
              <b-form-input
                readonly
                type="email"
                id="useremail"
                v-model="userInfo.email"
                required
                placeholder="이메일 입력...."
                @keyup.enter="confirm"
              ></b-form-input>
            </b-form-group>
            <b-button
              type="button"
              variant="primary"
              class="m-1"
              @click="movePage"
              >수정</b-button
            >
            <b-button
              type="button"
              variant="danger"
              class="m-1"
              @click="deleteUser(userInfo.userid)"
              >탈퇴</b-button
            >
          </b-form>
        </b-card>
      </b-col>
      <b-col>
        <interest-view></interest-view>
        <frequent-view></frequent-view>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import { mapState, mapActions } from "vuex";
import InterestView from "@/views/InterestView.vue";
import FrequentView from "@/views/FrequentView.vue";
const memberStore = "memberStore";

export default {
  name: "MemberRegister",
  components: {
    InterestView,
    FrequentView,
  },
  data() {
    return {
      user: {
        userid: null,
        userpwd: null,
        username: null,
        useremail: null,
      },
    };
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
  },
  methods: {
    ...mapActions(memberStore, ["deleteUser"]),
    movePage() {
      this.$router.push({ name: "memberModify" });
    },
  },
};
</script>

<style></style>
