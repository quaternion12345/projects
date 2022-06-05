import { apiInstance } from "@/api/index.js";
const http = apiInstance();
const qnaStore = {
  // namespaced: true,
  state: { comments: [] },
  getters: {
    comments(state) {
      return state.comments;
    },
  },
  mutations: {
    setComments(state, payload) {
      state.comments = payload;
    },
  },
  actions: {
    getComments(context, payload) {
      http.get(payload).then(({ data }) => {
        context.commit("setComments", data);
      });
    },
  },
};

export default qnaStore;
