const chatStore = {
  namespaced: true,
  state: {
    msgList: [],
  },
  mutations: {
    PUSH_MSG_DATA: (state, payload) => {
      state.msgList.push(payload);
    },
    CLEAR_MSG(state) {
      state.msgList = [];
    },
  },
  actions: {},
};
export default chatStore;
