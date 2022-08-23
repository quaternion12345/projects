const lectureStore = {
  namespaced: true,
  state: {
    publisher: null,
  },
  mutations: {
    SET_PUBLISHER: (state, publisher) => {
      state.publisher = publisher;
    },
  },
  actions: {
    setPublisher: ({ commit }, publisher) => {
      commit("SET_PUBLISHER", publisher);
    },
  },
};
export default lectureStore;
