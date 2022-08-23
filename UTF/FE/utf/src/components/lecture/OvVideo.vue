<template>
  <video autoplay class="webcam" />
</template>

<script>
export default {
  name: "OvVideo",

  props: {
    streamManager: Object,
  },
  mounted() {
    this.streamManager.addVideoElement(this.$el);
  },
};
</script>
<style scoped>
video {
  height: 100%;
}
</style>
