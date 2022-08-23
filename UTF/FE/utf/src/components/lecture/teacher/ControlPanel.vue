<template>
  <div class="control-panel" v-if="publisher">
    <div class="menu-1">
      <audio-button
        pill
        :size="buttonSize"
        :stream-manager="publisher"
        class="m-1"
      />
      <cam-button
        pill
        :size="buttonSize"
        :stream-manager="publisher"
        class="m-1"
      />
      <b-button
        class="screen-share-start m-1"
        pill
        v-if="!isShared"
        @click="screenShare"
      >
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-box-arrow-up"
            viewBox="-2 0 20 20"
          >
            <path
              fill-rule="evenodd"
              d="M3.5 6a.5.5 0 0 0-.5.5v8a.5.5 0 0 0 .5.5h9a.5.5 0 0 0 .5-.5v-8a.5.5 0 0 0-.5-.5h-2a.5.5 0 0 1 0-1h2A1.5 1.5 0 0 1 14 6.5v8a1.5 1.5 0 0 1-1.5 1.5h-9A1.5 1.5 0 0 1 2 14.5v-8A1.5 1.5 0 0 1 3.5 5h2a.5.5 0 0 1 0 1h-2z"
            />
            <path
              fill-rule="evenodd"
              d="M7.646.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 1.707V10.5a.5.5 0 0 1-1 0V1.707L5.354 3.854a.5.5 0 1 1-.708-.708l3-3z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button
        class="screen-share-end m-1"
        v-if="isShared"
        pill
        @click="stopShare"
      >
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-stop"
            viewBox="0 0 16 16"
          >
            <path
              d="M3.5 5A1.5 1.5 0 0 1 5 3.5h6A1.5 1.5 0 0 1 12.5 5v6a1.5 1.5 0 0 1-1.5 1.5H5A1.5 1.5 0 0 1 3.5 11V5zM5 4.5a.5.5 0 0 0-.5.5v6a.5.5 0 0 0 .5.5h6a.5.5 0 0 0 .5-.5V5a.5.5 0 0 0-.5-.5H5z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button
        class="class-start m-1"
        pill
        v-if="!recording"
        @click="recordingStart"
      >
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="red"
            class="bi bi-circle-fill"
            viewBox="0 0 16 16"
          >
            <circle cx="8" cy="8" r="4" />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button
        class="class-end m-1"
        pill
        v-if="recording"
        @click="recordingEnd"
      >
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="red"
            class="bi bi-square-fill"
            viewBox="-8 -8 32 32"
          >
            <path
              d="M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button class="exit m-1" pill @click="leaveSession">
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-box-arrow-right"
            viewBox="-4 -2 20 20"
          >
            <path
              fill-rule="evenodd"
              d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2z"
            />
            <path
              fill-rule="evenodd"
              d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations } from "vuex";
import AudioButton from "@/components/common/AudioButton";
import CamButton from "@/components/common/CamButton";
export default {
  name: "ControlPanel",
  props: { recording: Boolean, publisher: Object },
  components: {
    AudioButton,
    CamButton,
  },
  data() {
    return {
      video: true,
      audio: true,
      timer: undefined,
      isShared: false,
      buttonSize: 45,
    };
  },
  computed: {
    ...mapState("lectureStore", ["publisher"]),
  },
  methods: {
    ...mapMutations("chatStore", {
      pushMessage: "PUSH_MSG_DATA",
    }),
    videoToggle() {
      this.video = !this.video;
      this.publisher.publishVideo(this.video);
    },
    audioToggle() {
      this.audio = !this.audio;
      this.publisher.publishAudio(this.audio);
    },
    screenShare() {
      this.isShared = true;
      this.$emit("screenShare");
    },
    stopShare() {
      this.isShared = false;
      this.$emit("stopShare");
    },

    recordingStart() {
      this.$emit("recordingStart");
    },
    recordingEnd() {
      this.$emit("recordingEnd");
    },
    sendMessageTemplate(data, to, type) {
      this.publisher.session
        .signal({
          data: data, // Any string (optional)
          to: to, // Array of Connection objects (optional. Broadcast to everyone if empty)
          type: type, // The type of message (optional)
        })
        .then(() => {
          this.pushMessage({
            from: {
              name: this.publisher.session.connection.data.slice(15, -2),
              time: new Date().toLocaleTimeString(),
              type: type,
            },
            msg: data,
          });
          console.log("Message successfully sent");
        })
        .catch((error) => {
          console.error(error);
        });
      setTimeout(() => {
        const element = document.getElementById("chat-body");
        element.scrollTop = element.scrollHeight;
      }, 100);
    },

    leaveSession() {
      this.$emit("leaveSession", false);
    },
  },
};
</script>

<style scoped>
.control-panel {
  grid-column: 1/4;
  background: #f0f0ed;
  display: flex;
}
.menu-1 {
  width: 100%;
}

.class-end {
  background: #198754;
}
.missing {
  background: #565e64;
}
.clicked {
  background: black;
}
.comeback {
  background-color: #0d6efd;
}
.screen-share-end {
  background-color: #ff4f5a;
}
.exit {
  background-color: #ff4f5a;
}
</style>
