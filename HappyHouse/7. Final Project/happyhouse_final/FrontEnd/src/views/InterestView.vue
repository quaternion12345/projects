<template>
  <b-container class="bv-example-row mt-3 text-center">
    <interest-list></interest-list>
  </b-container>
</template>

<script>
import InterestList from "@/components/interest/InterestList.vue";
export default {
  name: "InterestView",
  components: {
    InterestList,
  },
};
</script>

<style></style>
