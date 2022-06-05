<template>
  <b-tr>
    <b-th class="text-left">
      {{ sidoName + " " + gugunName }}
    </b-th>
    <b-td>
      <b-icon
        icon="trash-fill"
        aria-hidden="true"
        variant="danger"
        @click="deleteInterest(no)"
      ></b-icon>
    </b-td>
  </b-tr>
</template>

<script>
export default {
  name: "InterestListItem",
  props: {
    no: Number,
    member_id: String,
    sidoName: String,
    gugunName: String,
    dongName: String,
    dongCode: String,
  },
  methods: {
    deleteInterest(no) {
      this.$emit("delete", no);
    },
  },
};
</script>

<style></style>
