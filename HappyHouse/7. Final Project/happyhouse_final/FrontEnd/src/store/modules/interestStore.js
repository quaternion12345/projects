import { getInterests, deleteInterest } from "@/api/interest.js";

const interestStore = {
  namespaced: true,
  state: {
    userid: null,
    interests: [],
  },

  getters: {},

  mutations: {
    SET_INTEREST_LIST: (state, interests) => {
      state.interests = interests;
    },
    CLEAR_INTEREST_LIST: (state) => {
      state.interests = [];
    },
  },

  actions: {
    /* eslint-disable */
    getInterestList: ({ commit }, userid) => {
      state.userid = userid;
      getInterests(
        userid,
        ({ data }) => {
          commit("SET_INTEREST_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    deleteInterest: ({ commit }, no) => {
      deleteInterest(
        no,
        ({ data }) => {
          if (data === "success") {
            console.log("관심사 삭제 성공");
            getInterestList(state.userid);
          } else {
            console.log("관심사 삭제 실패");
          }
        },
        (error) => {
          console.log(error);
        }
      );
    },
  },
};

export default interestStore;
