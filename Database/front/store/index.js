// export const state = () => ({
//     hello: 'vuex',
// });

// export const mutations = {
//     bye(state) {
//         state.name = 'goodbye';
//     }
// };

export const state = () => ({});

export const mutations = {};

export const actions = {
  nuxtServerInit({ commit, dispatch, state }, { req }) {
    return dispatch('users/loadUser');
  },
};

// export const state = () => ({
//     user: {},
//     messages: [],
//     users: []
//    })
    
//    export const mutations = {
//     SOCKET_newMessage(state, msg) {
//         state.messages = [...state.messages, msg];
//       },
//       SOCKET_updateUsers(state, users) {
//         state.users = users;
//       },
      
//     setUser(state, user) {
//       state.user = user;
//     },
//     newMessage(state, msg) {
//       state.messages = [...state.messages, msg];
//     },
//     updateUsers(state, users) {
//       state.users = users;
//     },
//     clearData(state) {
//       state.user = {};
//       state.messages = [];
//       state.users = [];
//     },
// }