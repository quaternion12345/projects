<template>
  <apexchart
    ref="chart"
    width="150"
    height="150"
    type="pie"
    :series="series"
    :options="chartOptions"
  />
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import VueApexCharts from "vue3-apexcharts";

export default {
  components: {
    apexchart: VueApexCharts,
  },
  props: {
    lectureRoomId: Number,
  },
  data() {
    return {
      series: [0, 0, 0],
      chartOptions: {
        chart: {
          type: "pie",
        },
        labels: ["이해돼요", "평범해요", "이해안돼요"],
        legend: {
          show: false,
        },
        responsive: [
          {
            breakpoint: 480,
          },
        ],
        title: {
          text: "이해도",
          align: "center",
        },
        colors: ["#2B80FF", "#FFD543", "#FF4F5A"],
      },
    };
  },
  computed: {
    isRecording() {
      console.log(this.getIsRecording);
      return this.getIsRecording();
    },
  },
  watch: {
    isRecording() {
      if (this.isRecording === true) {
        this.setChart();
      }
    },
  },
  methods: {
    ...mapActions("StatisticsStore", ["setUnderstanding"]),
    ...mapGetters("StatisticsStore", [
      "getCurrentUnderstanding",
      "getIsRecording",
    ]),
    setChart() {
      var interval = setInterval(() => {
        if (this.isRecording) {
          this.setUnderstanding(this.lectureRoomId);
          let obj = this.getCurrentUnderstanding();
          let newChart = [obj[1], obj[0], obj[-1]];
          this.series = newChart;
        } else {
          clearInterval(interval);
        }
      }, 5000);
    },
  },
};
</script>

<style scoped>
.vue-apexcharts {
  position: relative;
  right: 5%;
}
</style>
