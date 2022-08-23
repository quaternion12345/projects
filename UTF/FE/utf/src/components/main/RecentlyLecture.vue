<template>
  <div
    :class="{ recentList: !darkMode, recentListDark: darkMode }"
    v-for="(lecture, index) in this.recentLectures"
    :key="index"
    class="buttons"
    @click="goLecture(lecture.lectureRoomId, lecture.lectureId)"
  >
    <div class="msg">
      과목명 : {{ lecture.title }}<br />
      강의시간 : {{ lecture.startTime }}
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import store from "@/store";

export default {
  name: "RecetlyLecture",
  props: {
    darkMode: Boolean,
  },
  computed: {
    recentLectures() {
      return this.getRecentLectures();
    },
  },
  methods: {
    ...mapActions("StatisticsStore", ["setRecently", "setLectureOne"]),
    ...mapGetters("StatisticsStore", ["getRecentLectures"]),
    goLecture(lectureRoomId, lectureId) {
      this.setLectureOne(lectureId);
      this.$router.push("/lectureRoom/" + lectureRoomId);
    },
  },
  mounted() {
    this.setRecently(store.state.userInfo["userId"]);
  },
};
</script>

<style scoped>
.buttons {
  text-align: left;
}
.msg {
  margin-left: 10px;
}
.recentList {
  background-color: #ffffff;
  margin: 10px;
  border-radius: 10px;
  border: #c1c1c1 solid 1px;
}
.recentListDark {
  background-color: #202026;
  margin: 10px;
  border-radius: 10px;
  border: #3e3e3e solid 1px;
}
</style>
