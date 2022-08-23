<template>
  <JoinFormComp @getName="join"></JoinFormComp>
</template>

<script>
import JoinFormComp from "@/components/user/JoinFormComp.vue";
import { sendName } from "@/api/index.js";
import { getLectureRoomList } from "@/api/index.js";
import store from "@/store";

export default {
  name: "LoginView",
  components: {
    JoinFormComp,
  },
  methods: {
    async join(name) {
      await sendName(name);
      await getLectureRoomList(store.state.userInfo["userId"]);
    },
  },
};
</script>
