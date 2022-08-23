<template>
  <div class="lecture-video-container">
    <div class="video">
      <video :src="lecture.videoUrl" style="width: 100%" controls></video>
    </div>
    <div v-for="(item, index) in chartList" :key="index">
      <chart v-if="item.show" :series="item.series" class="mt-1" />
    </div>
    <div class="radioGroup mt-5">
      <div v-for="(item, index) in chartList" :key="index">
        <b-form-radio
          type="radio"
          :id="item.key"
          v-model="selected"
          :value="item.value"
          @change="showChart(index)"
        >
          <p v-if="item.value === 'notFocus'">
            집중 안돼요
            <img src="@/assets/sleep.png" style="width: 30%" alt="x" />
          </p>
          <p v-if="item.value === 'notUnderstand'">
            이해 안돼요
            <img src="@/assets/problem.png" style="width: 30%" alt="x" />
          </p>
          <p v-if="item.value === 'focus'">
            집중하고 있어요
            <img src="@/assets/focus.png" style="width: 30%" alt="o" />
          </p>
          <p v-if="item.value === 'understand'">
            이해잘돼요
            <img src="@/assets/understanding1.png" style="width: 30%" alt="o" />
          </p>
        </b-form-radio>
        <!-- <label :for="item.key" class="text">{{ item.value }}</label> -->
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Chart from "@/components/statistics/RecordChart.vue";

export default {
  name: "LineChart",
  components: {
    Chart,
  },
  props: {
    lecture: Object,
  },
  data() {
    return {
      chartList: [
        {
          value: "notFocus",
          show: false,
          series: [{ data: [] }],
        },
        {
          value: "notUnderstand",
          show: false,
          series: [{ data: [] }],
        },
        {
          value: "understand",
          show: false,
          series: [{ data: [] }],
        },
        {
          value: "focus",
          show: false,
          series: [{ data: [] }],
        },
      ],
      selected: "",
    };
  },
  computed: {
    statistics() {
      return this.getRecordedStatistics();
    },
  },
  watch: {
    statistics() {
      this.setChart();
    },
  },
  methods: {
    ...mapGetters("StatisticsStore", ["getRecordedStatistics"]),

    setChart() {
      console.log(this.statistics);
      this.chartList[0].series[0].data = this.statistics["-2"];
      this.chartList[1].series[0].data = this.statistics["-1"];
      this.chartList[2].series[0].data = this.statistics["1"];
      this.chartList[3].series[0].data = this.statistics["2"];

      //비율로 바꿔주기
      let maxSize = 0; //수업길이
      Object.keys(this.statistics).forEach((key) => {
        maxSize =
          maxSize > this.statistics[key].length
            ? maxSize
            : this.statistics[key].length;
      });
      //수업길이보다 작다면 부족한 시간만큼 0추가해주기
      Object.keys(this.statistics).forEach((key) => {
        if (this.statistics[key].length < maxSize) {
          let diff = maxSize - this.statistics[key].length;
          for (let i = 0; i < diff; i++) {
            this.statistics[key].unshift(0);
          }
        }
      });
      //각 시간에서의 값들을 비율로 바꿔주기
      for (let i = 0; i < maxSize; i++) {
        let focus = 0; // i번째 시간에서 총 학생 수
        let understand = 0;
        focus += this.statistics[-2][i] + this.statistics[2][i];
        understand +=
          this.statistics[-1][i] +
          this.statistics[1][i] +
          this.statistics[0][i];
        let num = Math.max(focus, understand);

        if (num == 0) {
          this.statistics[-2][i] = 0;
          this.statistics[-1][i] = 0;
          this.statistics[0][i] = 0;
          this.statistics[1][i] = 0;
          this.statistics[2][i] = 0;
        } else {
          this.statistics[-2][i] = parseInt(
            (this.statistics[-2][i] * 100) / num
          );
          this.statistics[-1][i] = parseInt(
            (this.statistics[-1][i] * 100) / num
          );
          this.statistics[0][i] = parseInt((this.statistics[0][i] * 100) / num);
          this.statistics[1][i] = parseInt((this.statistics[1][i] * 100) / num);
          this.statistics[2][i] = parseInt((this.statistics[2][i] * 100) / num);
        }
      }
    },
    showChart(index) {
      for (let i = 0; i < this.chartList.length; i++) {
        this.chartList[i].show = false;
      }
      for (let i = 0; i < this.chartList.length; i++) {
        if (i === index) {
          this.chartList[i].show = true;
        }
      }
    },
  },
};
</script>

<style scoped>
.radioGroup {
  display: flex;
  justify-content: space-evenly;
}
.lecture-video-container {
  height: 90vh;
}
.video {
  height: 50%;
  width: 100%;
  background-color: white;
}
</style>
