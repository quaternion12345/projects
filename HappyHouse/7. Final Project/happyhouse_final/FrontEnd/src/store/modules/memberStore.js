import jwt_decode from "jwt-decode";
import { login, modifyMember, deleteMember } from "@/api/member.js";
import { findById } from "../../api/member";
import router from "@/router/index.js";

const memberStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
  },
  getters: {
    checkUserInfo: function (state) {
      return state.userInfo;
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_USER_INFO: (state, userInfo) => {
      state.isLogin = true;
      state.userInfo = userInfo;
    },
  },
  actions: {
    async userConfirm({ commit }, user) {
      await login(
        user,
        (response) => {
          if (response.data.message === "success") {
            let token = response.data["access-token"];
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            sessionStorage.setItem("access-token", token);
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
          }
        },
        (error) => {
          console.log(error);
          /* eslint-disable */
        }
      );
    },
    getUserInfo: ({ commit }, token) => {
      let decode_token = jwt_decode(token);
      findById(
        decode_token.userid,
        (response) => {
          if (response.data.message === "success") {
            commit("SET_USER_INFO", response.data.userInfo);
          } else {
            console.log("유저 정보 없음!!");
          }
        },
        (error) => {
          console.log(error);
          /* eslint-disable */
        }
      );
    },
    modifyUser: ({ commit }, user) => {
      modifyMember(
        user,
        (response) => {
          if (response.data.message === "success") {
            console.log(response);
            // commit("SET_USER_INFO", user);
          } else {
            console.log("유저 정보 없음!!");
          }
        },
        (error) => {
          console.log(error);
          /* eslint-disable */
        }
      );
    },
    deleteUser: ({ commit }, userid) => {
      if (confirm("정말 탈퇴하시겠습니까?")) {
        deleteMember(userid);
        commit("SET_USER_INFO", null);
        commit("SET_IS_LOGIN", false);
        sessionStorage.removeItem("access-token");
        alert("탈퇴가 완료되었습니다.");
        router.push({ name: "home" });
      }
    },
  },
};

export default memberStore;
