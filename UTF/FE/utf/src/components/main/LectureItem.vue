<!-- eslint-disable prettier/prettier -->
<template>
  <div :class="{ card: !darkMode, cardDark: darkMode }">
    <div style="display: flex; justify-content: center;">
      <img :src="getImgSrc()" alt="logo" style="width: 60%; margin: 15px" @click="startLecture(lecture.lectureRoomId)" />
      <!-- Button trigger modal -->
      <i class="bi bi-x" data-bs-toggle="modal" data-bs-target="#deleteLectureModal" style="vertical-align: top; margin: 5px; font-size: 30px" @click="emitIndex(index, lecture.lectureRoomId)"></i>
      <!-- <button type="button" class="btn-close" data-bs-toggle="modal" data-bs-target="#exampleModal" aria-label="Close" style="vertical-align: top; margin: 5px" @click="emitIndex(index)"></button> -->
    </div>
    <div>
      <h5>{{ lecture["title"] }}</h5>
    </div>
    <div>
      <button type="button" class="blueBtn" style="margin: 7px; width: 40%" @click="copyCode">URL 복사</button>
      <button type="button" class="yellowBtn" style="margin: 7px; width: 40%" @click="goLecture(this.lecture.lectureRoomId)">통계 보기</button>
    </div>
  </div>
</template>

<script>
/* eslint-disable prettier/prettier */
export default {
  props: {
    lecture: Object,
    index: Number,
    darkMode: Boolean,
    lectureRoom: Object,
  },
  methods: {
    emitIndex(index, lectureRoomId) {
      console.log("index : " + index);
      console.log("lectureRoomId : " + lectureRoomId);
      this.$emit("emitIndex", index, lectureRoomId);
    },
    copyCode() {
      var text = process.env.VUE_APP_LECTURE_ROOM_URL + this.lecture["lectureRoomCode"];
      console.log(text);
      navigator.clipboard
        .writeText(text)
        .then(() => {
          console.log("Text copied to clipboard");
          alert("복사되었습니다");
        })
        .catch((err) => {
          console.error("Error in copying text: ", err);
        });
    },
    getImgSrc() {
      return require("../../assets/image/subjectIcon/subject" + this.lecture["subject"] + ".png");
    },
    goLecture(lectureRoomId) {
      this.$router.push(`/lectureRoom/${lectureRoomId}`);
    },
    startLecture(lectureRoomId) {
      this.$router.push({
        name: "lecture",
        params: {
          lectureRoomCode: this.lecture.lectureRoomCode,
          lectureRoomId: lectureRoomId,
        },
      });
    },
  },
};
</script>

<style scoped>
.card {
  width: 100%;
  height: 100%;
  background-color: #ffffff;
  border: 1px solid #c1c1c1;
  border-radius: 20px;
}
.cardDark {
  width: 100%;
  height: 100%;
  background-color: #202026;
  border: 1px solid #3e3e3e;
  border-radius: 20px;
}
</style>
