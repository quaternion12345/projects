import { sidoList, gugunList, houseList } from "@/api/house.js";

const houseStore = {
  namespaced: true,
  state: {
    sidos: [{ value: null, text: "선택하세요" }],
    guguns: [{ value: null, text: "선택하세요" }],
    houses: [],
    house: null,
    dealYmd: null,
    isOk: false,
    sido: null,
    totalRows: null,
    gugunCode: null,
  },

  getters: {},

  mutations: {
    SET_SIDO_LIST: (state, sidos) => {
      sidos.forEach((sido) => {
        state.sidos.push({ value: sido.sidoCode, text: sido.sidoName });
      });
    },
    SET_GUGUN_LIST: (state, guguns) => {
      guguns.forEach((gugun) => {
        state.guguns.push({ value: gugun.gugunCode, text: gugun.gugunName });
      });
      console.log(state.guguns);
    },
    CLEAR_SIDO_LIST: (state) => {
      state.sidos = [{ value: null, text: "선택하세요" }];
    },
    CLEAR_GUGUN_LIST: (state) => {
      state.guguns = [{ value: null, text: "선택하세요" }];
    },
    CLEAR_APT_LIST: (state) => {
      state.houses = [];
    },
    SET_HOUSE_LIST: (state, houses) => {
      // console.log(houses);
      state.houses = houses;
      state.isOk = !state.isOk;
    },
    SET_DETAIL_HOUSE: (state, house) => {
      state.house = house;
    },
    SET_TOTAL_COUNT: (state, totalRows) => {
      state.totalRows = totalRows;
    },
    SET_GUGUN_CODE: (state, gugunCode) => {
      state.gugunCode = gugunCode;
    },
    SET_DEAL_YMD: (state, dealYmd) => {
      state.dealYmd = dealYmd;
    },
  },

  actions: {
    /* eslint-disable */
    getSido: ({ commit }) => {
      sidoList(
        ({ data }) => {
          // console.log(data);
          commit("SET_SIDO_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    getGugun: ({ commit }, sidoCode) => {
      const params = {
        sido: sidoCode,
      };
      gugunList(
        params,
        ({ data }) => {
          // console.log(commit, response);
          commit("SET_GUGUN_LIST", data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    getHouseList: ({ commit }, payload) => {
      // vue cli enviroment variables 검색
      //.env.local file 생성.
      // 반드시 VUE_APP으로 시작해야 한다.
      const SERVICE_KEY = process.env.VUE_APP_APT_DEAL_API_KEY;
      //   const SERVICE_KEY =
      //     "9Xo0vlglWcOBGUDxH8PPbuKnlBwbWU6aO7%2Bk3FV4baF9GXok1yxIEF%2BIwr2%2B%2F%2F4oVLT8bekKU%2Bk9ztkJO0wsBw%3D%3D";

      console.log(
        `getHouseList Params : ${payload.gugunCode} ${payload.dealYmd} ${payload.page}`
      );
      const params = {
        serviceKey: decodeURIComponent(SERVICE_KEY),
        pageNo: payload.page,
        LAWD_CD: payload.gugunCode,
        DEAL_YMD: payload.dealYmd,
        numOfRows: 10,
      };

      houseList(
        params,
        (response) => {
          console.log("dealYMD : " + payload.dealYmd);
          console.log("totalCount : " + response.data.response.body.totalCount);
          console.log(response.data.response.body.items.item);
          commit("SET_GUGUN_CODE", payload.gugunCode);
          commit("SET_TOTAL_COUNT", response.data.response.body.totalCount);
          commit("SET_HOUSE_LIST", response.data.response.body.items.item);
          commit("SET_DEAL_YMD", payload.dealYmd);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    detailHouse: ({ commit }, house) => {
      // 나중에 house.일련번호를 이용하여 API 호출
      commit("SET_DETAIL_HOUSE", house);
    },
  },
};

export default houseStore;
