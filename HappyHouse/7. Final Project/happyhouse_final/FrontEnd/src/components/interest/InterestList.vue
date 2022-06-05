<template>
  <b-container class="bv-example-row mt-3">
    <div class="mb-2">
      <h4 class="underline-hotpink">관심지역</h4>
      <b-icon
        icon="exclamation-circle-fill"
        variant="secondary"
        v-b-popover.hover="'지도에서 추가할 수 있습니다.'"
      ></b-icon>
      <!-- <b-button
        type="button"
        variant="success"
        class="m-1"
        @click="registInterest"
        >추가</b-button
      > -->
    </div>
    <!-- <b-form-input
      id="interestSearch"
      class="mb-2"
      v-model="word"
      required
      placeholder="관심지역 검색...."
      @keyup.enter="confirm"
    ></b-form-input> -->
    <b-thead head-variant="dark">
      <b-tr>
        <b-th></b-th>
      </b-tr>
    </b-thead>
    <tbody>
      <interest-list-item
        v-for="interest in interests"
        :key="interest.no"
        v-bind="interest"
        @delete="deleteInterest"
      ></interest-list-item></tbody
  ></b-container>
</template>

<script>
import { mapState } from "vuex";
import InterestListItem from "./item/InterestListItem.vue";
import { apiInstance } from "@/api/index.js";
const api = apiInstance();
const memberStore = "memberStore";

export default {
  name: "InteresetList",
  components: {
    InterestListItem,
  },
  data() {
    return {
      interests: [],
    };
  },
  created() {
    this.setInterests();
  },
  computed: {
    ...mapState(memberStore, ["userInfo"]),
  },
  methods: {
    setInterests() {
      api
        .get(`/interest/${this.userInfo.userid}`)
        .then(({ data }) => {
          console.log(data);
          this.interests = data;
        })
        .catch(({ error }) => {
          console.log(error);
        });
    },
    deleteInterest(value) {
      api
        .delete(`/interest/${value}`)
        .then(({ data }) => {
          console.log(data);
          console.log("interest 삭제 성공");
          this.setInterests();
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
