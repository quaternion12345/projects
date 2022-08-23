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
import VueApexCharts from "vue3-apexcharts";
import { mapActions, mapGetters } from "vuex";

export default {
  components: {
    apexchart: VueApexCharts,
  },
  props: {
    lectureRoomId: Number,
  },
  data() {
    return {
      series: [0, 0],
      chartOptions: {
        chart: {
          type: "pie",
        },
        labels: ["집중 잘하고 있어요", "집중이 필요해요"],
        legend: {
          show: false,
        },
        responsive: [
          {
            breakpoint: 480,
          },
        ],
        title: {
          text: "집중도",
          align: "center",
        },
        colors: ["#2B80FF", "#FF4F5A"],
      },
    };
  },
  computed: {
    isRecording() {
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
    ...mapActions("focusStore", ["setFocusing"]),
    ...mapGetters("focusStore", ["getFocusRatio"]),
    ...mapGetters("StatisticsStore", [
      "getCurrentUnderstanding",
      "getIsRecording",
    ]),
    setChart() {
      var interval = setInterval(() => {
        if (this.isRecording) {
          this.setFocusing(this.lectureRoomId);
          let obj = this.getFocusRatio();
          let newChart = [obj[2], obj[-2]];
          this.series = newChart;
        } else {
          clearInterval(interval);
        }
      }, 5000);
    },
  },
};
</script>

<style></style>
