import { apiInstance } from "@/api/index.js";
const api = apiInstance();

const statisticsStore = {
  namespaced: true,
  state: {
    focusRatio: undefined,
  },
  getters: {
    getFocusRatio: (state) => {
      return state.focusRatio;
    },
  },
  mutations: {
    SET_FOCUSING: (state, payload) => {
      state.focusRatio = payload;
    },
  },
  actions: {
    //item : [name, isFocus, lectureRoomId]
    sendMyFocus: (store, item) => {
      api
        .post(`/api/statistics/focus/current/${item[2]}`, {
          name: item[0],
          isFocus: item[1],
        })
        // eslint-disable-next-line
        .then((res) => {})
        .catch((err) => {
          console.log(err);
        });
    },
    setFocusing: (store, lectureRoomId) => {
      api
        .get(`/api/statistics/focus/current/${lectureRoomId}`)
        .then((res) => {
          try {
            store.commit("SET_FOCUSING", res.data);
          } catch (error) {
            //do-nothing
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};

export default statisticsStore;
