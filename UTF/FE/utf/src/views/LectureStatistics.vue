<template>
  <div class="background">
    <b-container>
      <logo class="logo" :size="40" @initSelect="initSelect" />
      <b-row>
        <!-- 강의리스트 -->
        <b-col sm="2" class="list">
          <div class="listBackground pt-2">
            <div
              class="lectures"
              :class="{ selectedLecture: lecture.lectureId === this.lectureId }"
              v-for="(lecture, index) in lectureList"
              :key="index"
              @click="lectureOne(lecture.lectureId)"
            >
              {{ lecture.startTime }}
              <br />
              ~
              <br />
              {{ lecture.endTime }}
            </div>
          </div>
        </b-col>
        <!-- 강의 동영상/통계 -->
        <b-col sm="6">
          <div v-if="selected">
            <lecture-video :lecture="lecture" />
          </div>
          <div v-else class="selectMsg">
            <h3>강의를 선택하세요</h3>
          </div>
        </b-col>
        <!-- 채팅목록 -->
        <b-col sm="3">
          <div v-if="selected" class="chat-component">
            <div class="chat-header">채팅내역</div>
            <lecture-chat
              :msgs="JSON.parse(getLecture().chatRecord)"
            ></lecture-chat>
          </div>
        </b-col>
        <!-- 참가자목록 -->
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import LectureVideo from "@/components/statistics/LectureVideo.vue";
import LectureChat from "@/components/statistics/LectureChat.vue";
import Logo from "@/components/common/Logo.vue";

export default {
  components: {
    LectureVideo,
    LectureChat,
    Logo,
  },
  props: {},
  data() {
    return {
      lectureRoomId: 0,
      lectureId: 0,
      selected: false,
      // msgList: [],
    };
  },
  computed: {
    lectureList() {
      return this.getLectures();
    },
    lecture() {
      return this.getLecture();
    },
  },
  watch: {
    lecture() {
      this.lectureId = this.lecture.lectureId;
      this.selected = true;
      this.setRecordStatistics(this.lectureId);
    },
  },
  methods: {
    ...mapActions("StatisticsStore", [
      "setLectures",
      "setLectureOne",
      "setRecordStatistics",
    ]),
    ...mapGetters("StatisticsStore", ["getLectures", "getLecture"]),
    lectureOne(lectureId) {
      this.lectureId = lectureId;
      this.selected = true;
      this.setLectureOne(lectureId);
      // this.msgList = JSON.parse(this.getLecture().chatRecord);
    },
    initSelect() {
      this.selected = false;
    },
  },
  mounted() {
    this.lectureRoomId = this.$route.params.lectureRoomId;
    this.setLectures(this.lectureRoomId);
  },
};
</script>

<style scoped>
.background {
  background-color: #f7f7f4;
  height: 100vh;
  padding-top: 50px;
}
.listBackground {
  overflow: auto;
  background-color: white;
  border-radius: 15px;
  height: 80vh;
}
.listBackground::-webkit-scrollbar {
  width: 10px;
}
.listBackground::-webkit-scrollbar-thumb {
  background-color: #5196fe;
  border-radius: 10px;
}
.listBackground::-webkit-scrollbar-track {
  background: rgba(33, 122, 244, 0.1); /*스크롤바 뒷 배경 색상*/
}
.list {
  border-right: #c1c1c1 solid 1px;
}
.lectures {
  cursor: pointer;
  background-color: #f7f7f4;
  border-radius: 10px;
  margin: 10px;
}
.selectedLecture {
  background-color: #5196fe;
  color: white;
}
.logo {
  width: 20%;
  position: relative;
  right: 5%;
  bottom: 10px;
}
.chat-component {
  height: 80vh;
  background-color: white;
  border-radius: 15px;
  overflow: auto;
}
.chat-component::-webkit-scrollbar {
  width: 10px;
}
.chat-componen::-webkit-scrollbar-thumb {
  background-color: #5196fe;
  border-radius: 10px;
}
.chat-componen::-webkit-scrollbar-track {
  background: rgba(33, 122, 244, 0.1); /*스크롤바 뒷 배경 색상*/
}
.chat-header {
  background: #ffffff;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
  border-radius: 15px 15px 0px 0px;
  padding: 1.8rem;
  font-size: 15px;
  font-weight: 700;
  color: #292929;
}
</style>
