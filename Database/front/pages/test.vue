<!--<template>
  <div class='row'>
    <h1>API test</h1>
      <button class="btn btn-primary" @click="api">API</button>
  </div>
</template>

<script>
export default {
    layout: 'main',
    data() {
        return {
            apiCheck: ''
        }
    },
    methods: {
        async api() {
            console.log('api');
            try {
                let rs = await this.$axios.post('/api');
                console.log('Response : ', rs);
                this.apiCheck = rs;
            } catch(err) {
                console.log('Error: ', err);
                this.apiCheck = err;
            }
        }
    },
}
</script>

<style scoped>

</style>-->