<template>
  <div class="student-components">
    <div class="student-lecture" v-if="maintainer && isWait">
      <video-components
        :maintainer="maintainer"
        :subscribers="subscribers"
        :lastPage="lastPage"
        :screen="screen"
        :lectureRoomId="lectureRoomId"
      />
      <chat-components
        v-if="publisher && isChat"
        :publisher="publisher"
        :subscribers="subscribers"
      ></chat-components>
      <user-list-components
        :subscribers="subscribers"
        v-else
      ></user-list-components>
      <control-panel
        :maintainer="maintainer"
        :publisher="publisher"
        :recording="recording"
        @reSize="reSize"
        @leaveSession="leaveSession"
      ></control-panel>
    </div>
    <waiting-room
      :stream-manager="publisher"
      @joinLecture="joinLecture"
      @waitStop="disconect"
      v-else
    />
  </div>
</template>

<script>
import WaitingRoom from "@/components/lecture/student/WaitingRoom.vue";
import ChatComponents from "@/components/chat/ChatComponents.vue";
import ControlPanel from "@/components/lecture/student/ControlPanel.vue";
import UserListComponents from "@/components/lecture/student/UserListComponents.vue";
import VideoComponents from "@/components/lecture/student/VideoComponents.vue";
import { OpenVidu } from "openvidu-browser";
import { createTokenApi, createSessionApi } from "@/api/openvidu.js";
import { mapMutations } from "vuex";
export default {
  name: "StudentComponents",
  components: {
    WaitingRoom,
    VideoComponents,
    ChatComponents,
    ControlPanel,
    UserListComponents,
  },
  props: {
    lectureRoomId: Number,
  },
  data() {
    return {
      OVCamera: undefined,
      sessionCamera: undefined,
      mySessionId: this.$route.params.lectureRoomCode,

      publisher: undefined,
      subscribers: [],
      maintainer: undefined,
      screen: undefined,

      lastPage: 1,
      isWait: false,
      isChat: true,
    };
  },
  methods: {
    ...mapMutations("chatStore", {
      clearMessage: "CLEAR_MSG",
    }),
    createPublisher() {
      this.OVCamera = new OpenVidu();
      this.sessionCamera = this.OVCamera.initSession();

      this.sessionCamera.on("streamCreated", ({ stream }) => {
        const subscriber = this.sessionCamera.subscribe(stream);

        if (stream.typeOfVideo == "SCREEN") {
          this.screen = subscriber;
        } else if (
          JSON.parse(subscriber.stream.connection.data).role == "teacher"
        ) {
          this.sessionCamera.publish(this.publisher);
          this.subscribers.push(this.publisher);
          this.maintainer = subscriber;
        } else {
          this.subscribers.push(subscriber);
          this.lastPage = parseInt(this.subscribers.length / 4);
          if (this.subscribers.length % 4 > 0) this.lastPage++;
        }
      });

      this.sessionCamera.on("streamDestroyed", ({ stream }) => {
        if (stream.typeOfVideo == "SCREEN") {
          this.screen = undefined;
        } else if (stream.streamId == this.maintainer.stream.streamId) {
          this.leaveSession();
        }

        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      this.sessionCamera.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      this.publisher = this.OVCamera.initPublisher(undefined, {
        audioSource: undefined, // The source of audio. If undefined default microphone
        videoSource: undefined, // The source of video. If undefined default webcam
        publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
        publishVideo: true, // Whether you want to start publishing with your video enabled or not
        resolution: "640x480", // The resolution of your video
        frameRate: 30, // The frame rate of your video
        insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
        mirror: false, // Whether to mirror your local video or not
      });
    },

    joinLecture(name) {
      this.joinSession(name);
      this.isWait = true;
    },

    joinSession(name) {
      if (this.sessionCamera.connection) {
        this.sessionCamera.connection.data = JSON.stringify({
          clientData: name,
        });
        return;
      }

      this.getToken(this.mySessionId).then((token) => {
        this.sessionCamera
          .connect(token, { clientData: name })
          .catch((error) => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    disconect() {},

    reSize(chat) {
      this.isChat = chat;
    },

    leaveSession() {
      if (this.sessionCamera) this.sessionCamera.disconnect();

      this.sessionCamera = undefined;
      this.sessionScreen = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OVCamera = undefined;
      this.speaker = undefined;

      window.removeEventListener("beforeunload", this.leaveSession);
      this.clearMessage();
      this.$router.push("/exit");
    },

    getToken(mySessionId) {
      return this.createSession(mySessionId).then((sessionId) =>
        this.createToken(sessionId)
      );
    },

    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        createSessionApi(
          sessionId,
          ({ data }) => resolve(data.id),
          (error) => {
            if (error.response.status === 409) {
              resolve(sessionId);
            } else {
              reject(error.response);
            }
          }
        );
      });
    },

    createToken(sessionId) {
      return new Promise((resolve, reject) => {
        createTokenApi(
          sessionId,
          "PUBLISHER",
          ({ data }) => resolve(data.token),
          (error) => reject(error.response)
        );
      });
    },
  },
  mounted() {
    this.createPublisher();
  },
};
</script>

<style scoped>
.student-components {
  background: #c1c1c1;
  height: 100vh;
}

.student-lecture {
  display: grid;
  grid-template-columns: 4fr 1fr;
  grid-template-rows: calc(100% - 70px) 70px;
  background: #c1c1c1;
  height: 100vh;
}

.cancel {
  background-color: #ff4f5a;
  color: #ffffff;
  margin-top: 16rem;
}

.wait-modal {
  height: 350px;
  text-align: center;
}
</style>
