<template>
  <div class="waiting-room" v-if="streamManager">
    <b-container fluid style="height: 100px"
      ><logo class="h-100" :size="9"
    /></b-container>
    <b-container class="waiting-room-container bg-light">
      <div class="position-relative">
        <ov-video :stream-manager="streamManager" class="webcam" />
        <div class="buttons w-100">
          <audio-button
            pill
            :size="45"
            :stream-manager="streamManager"
            class="m-1"
          />
          <cam-button
            pill
            :size="45"
            :stream-manager="streamManager"
            class="m-1"
          />
        </div>
      </div>
      <div class="input-group mt-2 w-25 mx-auto">
        <b-form-input
          v-model="myName"
          placeholder="Enter your name"
          :disabled="isWait"
        />
        <div class="input-group-append mx-2">
          <b-button
            class="wait-btn"
            :disabled="myName"
            variant="danger"
            v-if="isWait"
            @click="waitStop"
            >대기중..</b-button
          >
          <b-button
            class="ok-btn"
            :disabled="myName"
            variant="primary"
            @click="waitStart"
            v-else
            >입장</b-button
          >
        </div>
      </div>
    </b-container>

    <div>
      <apexchart
        ref="chart"
        width="150"
        height="150"
        type="pie"
        :series="series"
        :options="chartOptions"
      />
    </div>
  </div>
</template>

<script>
import Logo from "@/components/common/Logo.vue";
import OvVideo from "@/components/lecture/OvVideo.vue";
import CamButton from "@/components/common/CamButton.vue";
import AudioButton from "@/components/common/AudioButton.vue";

export default {
  name: "WaitingRoom",
  components: {
    AudioButton,
    CamButton,
    Logo,
    OvVideo,
  },
  props: { streamManager: Object },
  data() {
    return {
      myName: "",
      isWait: false,
      x: 0,
      y: 0,
      isFocus: true,
      series: [0, 0],
      chartOptions: {
        chart: {
          type: "pie",
        },
        labels: ["집중o", "집중x"],
        legend: {
          show: false,
        },
        responsive: [
          {
            breakpoint: 480,
          },
        ],
      },
    };
  },
  methods: {
    waitStart() {
      this.$emit("joinLecture", this.myName);
      this.isWait = true;
    },
    waitStop() {
      this.$emit("waitStop");
      this.isWait = false;
    },
  },
};
</script>

<style scoped>
.waiting-room {
  height: 100vh;
}

.waiting-room-container {
  height: 600px;
  border-radius: 25px;
}

.webcam {
  border-radius: 15px;
}

.buttons {
  position: absolute;
  bottom: 1vh;
}

.ok-btn {
  background-color: #2b80ff;
  width: 90px;
  color: #ffffff;
}

.wait-btn {
  background-color: #ff4f5a;
  width: 90px;
  color: #ffffff;
}

.position-relative {
  min-height: 480px;
}
</style>
