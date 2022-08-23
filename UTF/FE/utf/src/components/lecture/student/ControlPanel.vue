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
      <b-button class="raiseup m-1" pill v-if="!isRaised" @click="raiseUp">
        <b-skeleton-icons>
          <svg
            viewBox="20 0 448 512"
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
          >
            <path
              d="M408.781 128.007C386.356 127.578 368 146.36 368 168.79V256h-8V79.79c0-22.43-18.356-41.212-40.781-40.783C297.488 39.423 280 57.169 280 79v177h-8V40.79C272 18.36 253.644-.422 231.219.007 209.488.423 192 18.169 192 40v216h-8V80.79c0-22.43-18.356-41.212-40.781-40.783C121.488 40.423 104 58.169 104 80v235.992l-31.648-43.519c-12.993-17.866-38.009-21.817-55.877-8.823-17.865 12.994-21.815 38.01-8.822 55.877l125.601 172.705A48 48 0 0 0 172.073 512h197.59c22.274 0 41.622-15.324 46.724-37.006l26.508-112.66a192.011 192.011 0 0 0 5.104-43.975V168c.001-21.831-17.487-39.577-39.218-39.993z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button class="putdown m-1" pill v-if="isRaised" @click="putDown">
        <b-skeleton-icons>
          <svg
            viewBox="20 0 448 512"
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
          >
            <path
              d="M408.781 128.007C386.356 127.578 368 146.36 368 168.79V256h-8V79.79c0-22.43-18.356-41.212-40.781-40.783C297.488 39.423 280 57.169 280 79v177h-8V40.79C272 18.36 253.644-.422 231.219.007 209.488.423 192 18.169 192 40v216h-8V80.79c0-22.43-18.356-41.212-40.781-40.783C121.488 40.423 104 58.169 104 80v235.992l-31.648-43.519c-12.993-17.866-38.009-21.817-55.877-8.823-17.865 12.994-21.815 38.01-8.822 55.877l125.601 172.705A48 48 0 0 0 172.073 512h197.59c22.274 0 41.622-15.324 46.724-37.006l26.508-112.66a192.011 192.011 0 0 0 5.104-43.975V168c.001-21.831-17.487-39.577-39.218-39.993z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button class="missing m-1" pill v-if="!isMissed" @click="missed">
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-clock"
            viewBox="0 0 16 16"
          >
            <path
              d="M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z"
            />
            <path
              d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button class="comeback m-1" pill v-if="isMissed" @click="comeback">
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-clock-fill"
            viewBox="0 0 16 16"
          >
            <path
              d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zM8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z"
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
    <div class="menu-2">
      <b-button
        class="chatting m-1"
        pill
        :class="[!isChatOpened ? '' : 'clicked']"
        @click="chatToggle"
      >
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-chat-left-dots-fill"
            viewBox="0 0 16 16"
          >
            <path
              d="M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H4.414a1 1 0 0 0-.707.293L.854 15.146A.5.5 0 0 1 0 14.793V2zm5 4a1 1 0 1 0-2 0 1 1 0 0 0 2 0zm4 0a1 1 0 1 0-2 0 1 1 0 0 0 2 0zm3 1a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"
            />
          </svg>
        </b-skeleton-icons>
      </b-button>
      <b-button
        class="participant m-1"
        pill
        :class="[isChatOpened ? '' : 'clicked']"
        @click="participantToggle"
      >
        <b-skeleton-icons>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            :width="buttonSize"
            :height="buttonSize"
            fill="currentColor"
            class="bi bi-people-fill"
            viewBox="0 0 16 16"
          >
            <path
              d="M7 14s-1 0-1-1 1-4 5-4 5 3 5 4-1 1-1 1H7zm4-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"
            />
            <path
              fill-rule="evenodd"
              d="M5.216 14A2.238 2.238 0 0 1 5 13c0-1.355.68-2.75 1.936-3.72A6.325 6.325 0 0 0 5 9c-4 0-5 3-5 4s1 1 1 1h4.216z"
            />
            <path d="M4.5 8a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5z" />
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
  props: { recording: Boolean, publisher: Object, maintainer: Object },
  components: {
    AudioButton,
    CamButton,
  },
  data() {
    return {
      video: true,
      audio: true,
      isMissed: false,
      isRaised: false,
      isChatOpened: true,
      timer: undefined,
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

    chatToggle() {
      this.isChatOpened = true;
      this.$emit("reSize", true);
    },
    participantToggle() {
      this.isChatOpened = false;
      this.$emit("reSize", false);
    },
    missed() {
      console.log("자리비움 시작");
      this.isMissed = true;
      const target = [];
      target.push(this.maintainer.stream.connection);
      this.simpleMessageTemplate(Date.now(), target, "missed");
      this.timer = setTimeout(() => {
        const msg =
          this.publisher.session.connection.data.slice(15, -2) +
          "님이 자리를 오래 비웠습니다.";
        this.simpleMessageTemplate(msg, target, "alert");
        console.log("5분 지남");
      }, 5 * 60 * 1000);
    },
    /* Clear timer when user comeback */
    comeback() {
      clearTimeout(this.timer);
      this.isMissed = false;
      const target = [];
      target.push(this.maintainer.stream.connection);
      this.simpleMessageTemplate(undefined, target, "comeback");
      console.log("복귀했음");
    },
    raiseUp() {
      console.log("질문시작");
      this.isRaised = true;
      const target = [];
      target.push(this.maintainer.stream.connection);
      const msg =
        this.publisher.session.connection.data.slice(15, -2) +
        "님이 손을 들었습니다.";
      this.simpleMessageTemplate(msg, target, "alert");
    },
    putDown() {
      this.isRaised = false;
    },
    /* Simple template for alert absent */
    simpleMessageTemplate(data, to, type) {
      this.publisher.session
        .signal({
          data: data, // Any string (optional)
          to: to, // Array of Connection objects (optional. Broadcast to everyone if empty)
          type: type, // The type of message (optional)
        })
        .then(() => {
          console.log("Alert Message successfully sent");
        })
        .catch((error) => {
          console.error(error);
        });
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
.menu-2 {
  position: absolute;
  right: 10px;
}
.class-end {
  background: #198754;
}
.missing {
  background: #565e64;
}
.raiseup {
  background-color: #565e64;
}
.putdown {
  background-color: gold;
}
.clicked {
  background: black;
}
.comeback {
  background-color: #0d6efd;
}
.exit {
  background-color: #ff4f5a;
}
</style>
