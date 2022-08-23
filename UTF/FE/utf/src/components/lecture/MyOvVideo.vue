<template>
  <video autoplay class="webcam" />
</template>

<script>
import * as faceapi from "face-api.js";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "OvVideo",

  props: {
    streamManager: Object,
    name: String,
  },
  data() {
    return {
      initial_x: 0,
      initial_y: 0,
      isFocus: true,
    };
  },
  computed: {
    lectureRoomId() {
      return this.getLectureRoomId();
    },
  },
  methods: {
    ...mapActions("focusStore", ["sendMyFocus"]),
    ...mapActions("StatisticsStore", ["setEmotion", "setLectureRoomId"]),
    ...mapGetters("StatisticsStore", ["getLectureRoomId"]),
    setId() {
      let link = document.location.href.split("/");
      let code = link[4];
      this.setLectureRoomId(code);
    },
  },
  mounted() {
    this.setId();
    this.streamManager.addVideoElement(this.$el);
    //faceapi 모델 로드
    const webcamElement = document.querySelectorAll(".webcam")[0];
    const modelLoad = async () => {
      await faceapi.loadSsdMobilenetv1Model("/models");
      await faceapi.loadFaceExpressionModel("/models");
      await faceapi.loadFaceLandmarkModel("/models");
    };
    modelLoad();
    //감지한 웹캠에서 face-api이용해 표정 감지 함수
    const getFaceEmotion = async () => {
      const minConfidenceFace = 0.5;

      //이해도
      const expressionResult = await faceapi
        .detectSingleFace(
          webcamElement,
          new faceapi.SsdMobilenetv1Options({ minConfidenceFace })
        )
        .withFaceExpressions();
      const expressionFiltered = Object.fromEntries(
        Object.entries(expressionResult.expressions)
          // eslint-disable-next-line
          .filter(([key, score]) => score > minConfidenceFace) // key : 감정 , score : 점수
          .sort((a, b) => b[1] - a[1]) //score 높은 순으로 정렬
      );
      let emotion = Object.keys(expressionFiltered)[0] || "neutral";
      let score = expressionFiltered[emotion] || 0.9;
      const arr = [this.lectureRoomId, this.name, emotion, score];
      this.setEmotion(arr);
    };
    //집중도
    const getEyeTracking = async () => {
      const eye = await faceapi
        .detectSingleFace(webcamElement)
        .withFaceLandmarks();
      let leftEye = eye.landmarks.getLeftEye();
      let rightEye = eye.landmarks.getRightEye();
      let x = 0;
      let y = 0;
      for (let i = 0; i < 6; i++) {
        x += leftEye[i].x;
        y += rightEye[i].y;
      }
      x = x / 6;
      y = y / 6;
      //처음이면 저장
      if (this.initial_x == 0 && this.initial_y == 0) {
        this.initial_x = x;
        this.initial_y = y;
      }
      if (
        // x,y 중 하나라도 50이상 차이나면 x
        Math.abs(this.initial_x - x) > 50 ||
        Math.abs(this.initial_y - y) > 50
      ) {
        this.isFocus = false;
      } else {
        this.isFocus = true;
      }
      this.sendMyFocus([this.name, this.isFocus, this.lectureRoomId]);
    };
    //시간당(5초마다) 이해도/집중도 얻는 함수
    const getStatisticsPerTime = () => {
      setInterval(async () => {
        await getFaceEmotion();
        await getEyeTracking();
      }, 5000);
    };
    getStatisticsPerTime();
  },
};
</script>
<style scoped>
video {
  width: 100%;
  height: 100%;
}
</style>
