import { getFrequents, deleteFrequent } from "@/api/frequent.js";

const FrequentStore = {
  namespaced: true,
  state: {
    userid: null,
    frequents: [],
  },

  getters: {},

  mutations: {
    SET_FREQUENT_LIST: (state, frequents) => {
      state.frequents = frequents;
    },
    CLEAR_FREQUENT_LIST: (state) => {
      state.frequents = [];
    },
  },

  actions: {
    /* eslint-disable */
    getFrequentList: ({ commit }, userid) => {
      state.userid = userid;
      getFrequents(
        userid,
        ({ data }) => {
          commit("SET_FREQUENT_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    deleteFrequent: ({ commit }, no) => {
      deleteFrequent(
        no,
        ({ data }) => {
          if (data === "success") {
            console.log("관심사 삭제 성공");
            getFrequentList(state.userid);
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

export default FrequentStore;
