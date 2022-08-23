<template>
  <div class="lecture-room">
    <teacher-components :lectureRoomId="lectureRoomId" v-if="userInfo" />
    <student-components v-else />
  </div>
</template>

<script>
import TeacherComponents from "@/components/lecture/teacher/TeacherComponents.vue";
import StudentComponents from "@/components/lecture/student/StudentComponents.vue";
import store from "@/store/index.js";

export default {
  name: "LectureRoom",
  props: {
    lectureRoomId: Number,
  },
  components: {
    TeacherComponents,
    StudentComponents,
  },
  data() {
    return {
      userInfo: undefined,
    };
  },
  mounted() {
    this.userInfo = store.state.userInfo;
    console.log(this.lectureRoomId);
  },
};
</script>
