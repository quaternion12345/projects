<template>
  <b-container class="bv-example-row mt-3">
    <div class="mb-2">
      <h4 class="underline-hotpink">자주 가는 장소</h4>
      <b-icon
        icon="exclamation-circle-fill"
        variant="secondary"
        v-b-popover.hover="'지도에서 추가할 수 있습니다.'"
      ></b-icon>
    </div>
    <b-thead head-variant="dark">
      <b-tr>
        <b-th></b-th>
      </b-tr>
    </b-thead>
    <tbody>
      <frequent-list-item
        v-for="frequent in frequents"
        :key="frequent.no"
        v-bind="frequent"
        @delete="deleteFrequent"
      ></frequent-list-item></tbody
  ></b-container>
</template>

<script>
import FrequentListItem from "./item/FrequentListItem.vue";
import { mapState } from "vuex";
import { apiInstance } from "@/api/index.js";
const api = apiInstance();
const memberStore = "memberStore";

export default {
  name: "FrequentList",
  components: {
    FrequentListItem,
  },
  data() {
    return {
      frequents: [],
    };
  },
  created() {
    this.setFrequent();
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
  },
  methods: {
    setFrequent() {
      console.log("setfrequent().............");
      console.log(this.userInfo.userid);
      api
        .get(`/frequent/${this.userInfo.userid}`)
        .then(({ data }) => {
          console.log(data);
          this.frequents = data;
        })
        .catch(({ error }) => {
          console.log(error);
        });
    },
    deleteFrequent(value) {
      api
        .delete(`/frequent/${value}`)
        .then(({ data }) => {
          console.log(data);
          console.log("frequent 삭제 성공");
          this.setFrequent();
        })
        .catch(({ error }) => {
          console.log(error);
        });
    },
  },
};
</script>

<style scoped>
.underline-hotpink {
  display: inline-block;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0) 70%,
    rgba(231, 27, 139, 0.3) 30%
  );
}
</style>
