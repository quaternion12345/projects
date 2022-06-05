<template>
  <b-container fluid>
    <b-row>
      <b-col>
        <div>
          <b-nav vertical>
            <house-view />
          </b-nav>
        </div>
      </b-col>
      <b-col>
        <div
          id="map"
          style="
            width: 100%;
            height: 100%;
            position: relative;
            overflow: hidden;
          "
        ></div>
      </b-col>
    </b-row>
  </b-container>
</template>
<script>
import { mapState } from "vuex";
import HouseView from "./HouseView.vue";

const houseStore = "houseStore";

export default {
  name: "mapView",
  components: { HouseView },
  data() {
    return {
      markers: [],
      markerPositions: [],
      infowindow: null,
      placeOverlay: null,
      mapContainer: null,
      word: null,
      map: null,
    };
  },
  computed: {
    ...mapState(houseStore, ["houses", "isOk", "sido"]),
  },
  watch: {
    isOk: function () {
      this.displayMarker();
    },
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      console.log("start");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=52865164964cb05c81471dcd1ecaddd8&libraries=services";
      document.head.appendChild(script);
    }
  },
  methods: {
    initMap() {
      const container = document.getElementById("map");
      container.style.width = `60vw`;
      container.style.height = `100vh`;
      const options = {
        center: new kakao.maps.LatLng(37.566932, 126.978663),
        level: 5,
      };

      //지도 객체를 등록합니다.
      //지도 객체는 반응형 관리 대상이 아니므로 initMap에서 선언합니다.
      this.map = new kakao.maps.Map(container, options);
      this.map.relayout();
    },

    displayMarker() {
      console.log("displayMarker......................");
      // let i = 0;
      // console.log(this.houses);
      if (this.markers.length > 0) {
        this.markers.forEach((marker) => marker.setMap(null));
      }
      const sidoName = this.sido === null ? "" : this.sido;
      const geocoder = new kakao.maps.services.Geocoder();
      this.markers = [];
      var positions = [];
      this.houses.forEach((item) => {
        /* eslint-disable */
        let address =
          sidoName +
          " " +
          item.도로명 +
          " " +
          Number(item.도로명건물본번호코드).toString();
        console.log(address);
        geocoder.addressSearch(address, (result, status) => {
          if (status == kakao.maps.services.Status.OK) {
            let coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            positions.push(coords);
            let marker = new kakao.maps.Marker({
              map: this.map,
              position: coords,
              title: item.아파트,
              clickable: true,
            });
            let infowindow = new kakao.maps.InfoWindow({
              content: '<div style="padding:5px;">' + item.아파트 + "</div>",
              position: coords,
              removable: false,
            });

            kakao.maps.event.addListener(marker, "mouseover", () => {
              infowindow.open(this.map, marker);
            });
            kakao.maps.event.addListener(marker, "mouseout", () => {
              infowindow.close(this.map, marker);
            });
            this.markers.push(marker);
            // i = i + 1;
            // if ((i = 1)) {
            //   this.map.panTo(coords);
            // }
            const bounds = positions.reduce(
              (bounds, position) => bounds.extend(position),
              new kakao.maps.LatLngBounds()
            );
            this.map.setBounds(bounds);
          }
        });
      });

      // /* eslint-disable */
      // const positions = this.markerPositions.map(
      //   (position) => new kakao.maps.LatLng(...position)
      // );

      // if (positions.length > 0) {
      //   this.markers = positions.map(
      //     (position) =>
      //       new kakao.maps.Marker({
      //         map: this.map,
      //         position,
      //       })
      //   );

      //   const bounds = positions.reduce(
      //     (bounds, latlng) => bounds.extend(latlng),
      //     new kakao.maps.LatLngBounds()
      //   );

      //   this.map.setBounds(bounds);
      // }
    },

    showDetail() {
      this.clicked = true;
      console.log(this.clicked);
    },
  },
};
</script>
<style scoped>
.underline-orange {
  display: inline-block;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0) 70%,
    rgba(231, 149, 27, 0.3) 30%
  );
}
</style>
