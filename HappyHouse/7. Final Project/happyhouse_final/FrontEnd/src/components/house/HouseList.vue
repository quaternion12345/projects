<template>
  <b-container>
    <b-row>
      <b-col>
        <house-search-bar @mark="$emit('mark')"></house-search-bar>
      </b-col>
    </b-row>
    <div v-if="houses && houses.length != 0" class="bv-example-row mt-3">
      <house-list-item
        v-for="(house, index) in houses"
        :key="index"
        :house="house"
      />
    </div>
    <div v-else class="bv-example-row mt-3">
      <b-row>
        <b-col><b-alert show>주택 목록이 없습니다.</b-alert></b-col>
      </b-row>
    </div>
    <b-pagination
      v-model="currentPage"
      :total-rows="totalRows"
      :per-page="perPage"
      align="center"
      @page-click="pageClick"
    ></b-pagination>
  </b-container>
</template>

<script>
import HouseListItem from "@/components/house/HouseListItem.vue";
import HouseSearchBar from "@/components/house/HouseSearchBar.vue";
import { mapState, mapActions } from "vuex";

const houseStore = "houseStore";

export default {
  name: "HouseList",
  components: {
    HouseListItem,
    HouseSearchBar,
  },
  data() {
    return {
      currentPage: 1,
      perPage: 10,
    };
  },
  computed: {
    ...mapState(houseStore, [
      "houses",
      "sido",
      "totalRows",
      "gugunCode",
      "dealYmd",
    ]),
    // houses() {
    //   return this.$store.state.houses;
    // },
  },
  methods: {
    ...mapActions(houseStore, ["getHouseList"]),
    pageClick: function (button, page) {
      this.currentPage = page;
      const gugunCode = this.gugunCode;
      const dealYmd = this.dealYmd;
      var payload = { gugunCode: gugunCode, dealYmd: dealYmd, page: page };
      this.getHouseList(payload);
    },
  },
};
</script>

<style></style>
