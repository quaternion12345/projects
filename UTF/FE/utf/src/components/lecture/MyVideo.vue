<template>
  <div class="user-video" v-if="streamManager">
    <p class="client-name">{{ clientData }}</p>
    <my-ov-video :stream-manager="streamManager" :name="this.clientData" />
  </div>
</template>

<script>
import MyOvVideo from "./MyOvVideo.vue";

export default {
  name: "UserVideo",

  components: {
    MyOvVideo,
  },

  props: {
    streamManager: Object,
  },

  computed: {
    clientData() {
      const { clientData } = this.getConnectionData();
      return clientData;
    },
  },

  methods: {
    getConnectionData() {
      const { connection } = this.streamManager.stream;
      return JSON.parse(connection.data);
    },
  },
};
</script>

<style>
.user-video {
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  display: inline-block;
  width: auto;
}
.client-name {
  position: absolute;
  color: white;
  z-index: 1000;
  left: 12px;
  margin: 0;
}
</style>
