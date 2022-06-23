module.exports = {
    head: {
        title: 'NodeBird',
    }, 
    modules: [
        '@nuxtjs/axios'
    ],
    buildModules: [
        '@nuxtjs/vuetify',
    ],
    plugins: [
        { src: '~/plugins/socket.client.js' }
    ],
    vuetify: {

    },
    // serverMiddleware: [
    //     ~api/index.js
    // ],
    axios: {
        baseURL: 'https://localhost:3000'
    }

};