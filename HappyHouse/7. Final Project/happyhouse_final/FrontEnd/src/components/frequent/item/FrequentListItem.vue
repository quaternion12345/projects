<template>
  <b-tr>
    <b-th class="text-left">
      {{ address }}
    </b-th>
    <b-td style="vertical-align: right"
      ><b-icon
        icon="trash-fill"
        aria-hidden="true"
        variant="danger"
        @click="deleteFrequent(no)"
      ></b-icon
    ></b-td>
  </b-tr>
</template>

<script>
export default {
  name: "FrequentListItem",
  props: {
    no: Number,
    member_id: String,
    address: String,
    gugunName: String,
    lat: String,
    lng: String,
  },
  methods: {
    deleteFrequent(no) {
      console.log(no);
      this.$emit("delete", this.no);
    },
  },
};
</script>

<style></style>
