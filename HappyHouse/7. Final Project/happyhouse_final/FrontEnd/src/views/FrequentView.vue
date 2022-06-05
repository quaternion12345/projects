<template>
  <b-container class="bv-example-row mt-3 text-center">
    <frequent-list></frequent-list>
  </b-container>
</template>

<script>
import FrequentList from "@/components/frequent/FrequentList.vue";
export default {
  name: "FrequentView",
  components: {
    FrequentList,
  },
};
</script>

<style></style>
