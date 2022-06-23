export const state = () => ({
    mainMessage: [],
    hasMoreMessage: true,
});

export const mutations = {
    addMainMessage(state, payload) {
        state.mainMessage.unshift(payload);
    }
};

export const actions = {
    add({ commit }, payload) {
    //서버에 
        commit('addMainMessage', payload);
    },
    loadMessage(state) {
      const fakeMessage = Array(limit).fill().map(v => ({
        id : Math.random().toString(),
        User: {
            id: 1,
            nickname: '제로초',
        },
        content: 'Hello~ ${Math.random()}',
      }));
      state.mainMessage = state.mainMessage.concat(fakeMessage);
      state.hasMoreMessage = fakePosts.length ===limit;
    },
};
