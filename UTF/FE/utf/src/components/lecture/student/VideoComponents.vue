<template>
  <div id="video-components">
    <div class="student-videos">
      <b-button v-if="page != 1" variant="outline-light" @click="page--"
        >&lt;</b-button
      >
      <my-video
        class="mx-1"
        :stream-manager="subscribers[0]"
        :lectureRoomId="lectureRoomId"
      />
      <user-video
        class="mx-1"
        v-for="sub in listData"
        :key="sub.stream.streamId"
        :stream-manager="sub"
      />

      <b-button v-if="lastPage > page" variant="outline-light" @click="page++"
        >&gt;</b-button
      >
    </div>
    <div class="teacher-video">
      <user-video class="teacher" :stream-manager="screen" v-if="screen" />
      <user-video class="teacher" :stream-manager="maintainer" v-else />
    </div>
  </div>
</template>

<script>
import UserVideo from "@/components/lecture/UserVideo";
import MyVideo from "@/components/lecture/MyVideo.vue";

export default {
  name: "VideoComponents",

  components: {
    UserVideo,
    MyVideo,
  },
  props: {
    subscribers: Object,
    maintainer: Object,
    screen: Object,
    lastPage: Number,
    lectureRoomId: Number,
  },
  data() {
    return {
      page: 1,
    };
  },
  computed: {
    listData: function () {
      let obj = this.subscribers.slice(1);
      return obj.slice((this.page - 1) * 3, this.page * 3);
      // return this.subscribers.slice((this.page - 1) * 4, this.page * 4);
    },
  },
};
</script>

<style scoped>
.highlight {
  border: 2px;
  border-style: solid;
  border-color: blue;
}

.user-video {
  height: 100%;
}

.student-videos {
  height: 180px;
  padding-top: 8px;
  padding-bottom: 4px;
}

.teacher-video {
  height: calc(100% - 180px);
  padding-top: 4px;
  padding-bottom: 8px;
}

button {
  height: 100%;
  position: relative;
  bottom: 78px;
}
</style>
