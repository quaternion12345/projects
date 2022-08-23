<!-- eslint-disable prettier/prettier -->
<template>
  <div @click="goHome()">
    <img src="@/assets/UTFLogo-removebg.png" alt="logo" class="buttons" :style="width"/>
  </div>
</template>

<script>
/* eslint-disable prettier/prettier */
export default {
  name: "LogoComponent",
  props: {
    size: {
      type: Number,
      default: 100,
    },
  },
  computed: {
    width() {
      return {
        "width": this.size+"%"
      };
    },
  },
  methods: {
    goHome() {
      this.$emit("initSelect");
      this.$router.push("/main");
    },
  },
};
</script>

<style></style>
