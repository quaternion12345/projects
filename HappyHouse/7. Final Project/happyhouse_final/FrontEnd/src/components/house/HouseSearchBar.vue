<template>
  <b-row class="mt-4 mb-4 text-center">
    <!-- <b-col class="sm-3">
      <b-form-input
        v-model.trim="dongCode"
        placeholder="동코드 입력...(예 : 11110)"
        @keypress.enter="sendKeyword"
      ></b-form-input>
    </b-col>
    <b-col class="sm-3" align="left">
      <b-button variant="outline-primary" @click="sendKeyword">검색</b-button>
    </b-col> -->
    <b-col>
      <b-form-select
        v-model="sidoCode"
        :options="sidos"
        @change="gugunList"
      ></b-form-select>
    </b-col>
    <b-col>
      <b-form-select
        v-model="gugunCode"
        :options="guguns"
        @change="searchApt"
      ></b-form-select>
    </b-col>
    <b-col>
      <b-form-input
        v-model="dealYearMonth"
        placeholder="거래년월(YYYYMM)"
        @keyup.enter="searchApt"
      ></b-form-input>
    </b-col>
    <b-col cols="1" class="mt-2"
      ><b-icon
        icon="bookmark-star"
        variant="warning"
        font-scale="1.5"
        @click="registInterest"
      ></b-icon
    ></b-col>
  </b-row>
</template>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
import { apiInstance } from "@/api/index.js";
const api = apiInstance();
/*
  namespaced: true를 사용했기 때문에 선언해줍니다.
  index.js 에서 modules 객체의 '키' 이름입니다.

  modules: {
    키: 값
    memberStore: memberStore,
    houseStore: houseStore
  }  
*/
const houseStore = "houseStore";
const memberStore = "memberStore";

export default {
  name: "HouseSearchBar",
  data() {
    return {
      sidoCode: null,
      gugunCode: null,
      dealYearMonth: null,
    };
  },
  computed: {
    ...mapState(houseStore, ["sidos", "guguns", "houses"]),
    ...mapState(memberStore, ["userInfo"]),
    sido: function () {
      const selected = this.sidos.find((sido) => sido.value === this.sidoCode);
      return selected.text;
    },
    gugun: function () {
      const selected2 = this.guguns.find(
        /* eslint-disable */
        (gugun) => gugun.value === this.gugunCode
      );
      return selected2.text;
    },
  },
  created() {
    // this.$store.dispatch("getSido");
    // this.sidoList();
    this.CLEAR_SIDO_LIST();
    this.getSido();
  },
  methods: {
    ...mapActions(houseStore, ["getSido", "getGugun", "getHouseList"]),
    ...mapMutations(houseStore, [
      "CLEAR_SIDO_LIST",
      "CLEAR_GUGUN_LIST",
      "CLEAR_APT_LIST",
    ]),
    // sidoList() {
    //   this.getSido();
    // },
    gugunList() {
      console.log(this.sidoCode);
      this.CLEAR_GUGUN_LIST();
      this.gugunCode = null;
      if (this.sidoCode) this.getGugun(this.sidoCode);
    },
    searchApt() {
      this.CLEAR_APT_LIST();
      if (this.dealYearMonth === null || this.dealYearMonth.length < 1) {
        this.dealYearMonth = "202110";
      }
      var payload = {
        gugunCode: this.gugunCode,
        dealYmd: this.dealYearMonth,
        page: 1,
      };
      if (this.gugunCode) this.getHouseList(payload);
    },
    registInterest() {
      api
        .post(`/interest`, {
          dongCode: null,
          dongName: null,
          gugunName: this.gugun,
          member_id: this.userInfo.userid,
          sidoName: this.sido,
        })
        .then(({ data }) => {
          console.log(data);
        })
        .catch(({ error }) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
