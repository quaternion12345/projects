<template>
  <div id="video-components">
    <user-video
      :stream-manager="publisher"
      :class="{ highlight: speaker == 'me' }"
    />
    <user-video
      v-for="sub in listData"
      :key="sub.stream.streamId"
      :stream-manager="sub"
      :class="{ highlight: sub.stream.streamId == speaker }"
    />

    <b-button
      variant="outline-secondary"
      :disabled="page == 1"
      class="left-button"
      @click="page--"
      >&lt;</b-button
    >
    <b-button
      variant="outline-secondary"
      :disabled="!(lastPage > page)"
      class="right-button"
      @click="page++"
      >&gt;</b-button
    >
    <p class="page">{{ page }}/{{ lastPage }}</p>
  </div>
</template>

<script>
import UserVideo from "@/components/lecture/UserVideo";

export default {
  name: "VideoComponents",

  components: {
    UserVideo,
  },
  props: {
    publisher: Object,
    subscribers: Object,
    speaker: String,
    lastPage: Number,
  },
  data() {
    return {
      page: 1,
    };
  },
  computed: {
    listData: function () {
      return this.subscribers.slice((this.page - 1) * 15, this.page * 15);
    },
  },
};
</script>

<style scoped>
#video-components {
  display: grid;
  grid-template-columns: 30px repeat(4, calc(25% - 15px)) 30px;
  grid-template-rows: repeat(4, calc(25% - 9px)) 20px;
  row-gap: 4px;
  overflow: hidden;
  padding-left: 3px;
}

.page {
  grid-column: 1/6;
}

.left-button {
  grid-row: 1/5;
  height: 50px;
  width: 30px;
  margin: auto;
}

.right-button {
  grid-row: 1/5;
  grid-column: 6/7;
  height: 50px;
  width: 30px;
  margin: auto;
}

.highlight {
  border: 1px;
  border-style: solid;
  border-color: blue;
}
</style>
