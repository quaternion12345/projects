<template>
  <div class="teacher-components">
    <video-components
      :publisher="publisher"
      :subscribers="subscribers"
      :lastPage="lastPage"
      :speaker="speaker"
    />
    <chat-components
      v-if="publisher"
      :publisher="publisher"
      :subscribers="subscribers"
    ></chat-components>
    <user-list-components
      v-if="publisher"
      :subscribers="subscribers"
      :publisher="publisher"
      :lectureRoomId="lectureRoomId"
    ></user-list-components>
    <control-panel
      :publisher="publisher"
      :recording="recording"
      @screenShare="screenShare"
      @recordingStart="recordingStart"
      @recordingEnd="recordingEnd"
      @stopShare="stopShare"
      @leaveSession="leaveSession"
      ref="control"
    ></control-panel>
  </div>
</template>
<script>
import ChatComponents from "@/components/chat/ChatComponents.vue";
import ControlPanel from "@/components/lecture/teacher/ControlPanel.vue";
import UserListComponents from "@/components/lecture/teacher/UserListComponents.vue";
import VideoComponents from "@/components/lecture/teacher/VideoComponents.vue";
import { OpenVidu } from "openvidu-browser";
import {
  createTokenApi,
  createSessionApi,
  closeSessionApi,
  recordingStartApi,
  recordingStopApi,
  registRecordVideoApi,
} from "@/api/openvidu.js";
import store from "@/store";
import { mapState, mapMutations, mapActions, mapGetters } from "vuex";
export default {
  name: "TeacherComponents",
  components: {
    ChatComponents,
    ControlPanel,
    UserListComponents,
    VideoComponents,
  },
  props: {
    lectureRoomId: Number,
  },
  data() {
    return {
      OVCamera: undefined,
      OVScreen: undefined,
      publisher: undefined,
      publisherScreen: undefined,
      recording: false,
      recordingId: undefined,
      sessionCamera: undefined,
      sessionScreen: undefined,
      subscribers: [],
      speaker: undefined,

      lastPage: 1,
      mySessionId: this.$route.params.lectureRoomCode,
      myUserName: store.state.userInfo["userName"],
    };
  },
  computed: {
    ...mapState("chatStore", ["msgList"]),
  },
  methods: {
    ...mapMutations("chatStore", {
      clearMessage: "CLEAR_MSG",
    }),
    ...mapActions("StatisticsStore", ["setIsRecording", "endLecture"]),
    ...mapGetters("StatisticsStore", ["getRecentLectureId"]),
    joinSession() {
      this.OVCamera = new OpenVidu();
      this.OVScreen = new OpenVidu();
      this.sessionCamera = this.OVCamera.initSession();
      this.sessionScreen = this.OVScreen.initSession();

      this.sessionCamera.on("streamCreated", ({ stream }) => {
        const subscriber = this.sessionCamera.subscribe(stream);

        if (stream.typeOfVideo == "SCREEN") return;

        subscriber.on("publisherStartSpeaking", ({ streamId }) => {
          this.speaker = streamId;
        });
        subscriber.on("publisherStopSpeaking", () => {
          this.speaker = null;
        });

        this.subscribers.push(subscriber);

        this.lastPage = parseInt(this.subscribers.length / 15);
        if (this.subscribers.length % 15 > 0) this.lastPage++;
      });

      this.sessionCamera.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });
      this.sessionCamera.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      this.getToken(this.mySessionId).then((token) => {
        this.sessionCamera
          .connect(token, { clientData: this.myUserName, role: "teacher" })
          .then(() => {
            // --- Get your own camera stream with the desired properties ---
            let publisher = this.OVCamera.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: false, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });

            publisher.on("publisherStartSpeaking", () => {
              this.speaker = "me";
            });
            publisher.on("publisherStopSpeaking", () => {
              this.speaker = null;
            });

            this.publisher = publisher;
            this.sessionCamera.publish(this.publisher);
          })
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

    screenShare() {
      this.getToken(this.mySessionId).then((token) => {
        this.sessionScreen
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            this.publisherScreen = this.OVScreen.initPublisher(undefined, {
              videoSource: "screen",
              publishAudio: false,
            });

            this.publisherScreen.once("accessAllowed", () => {
              this.publisherScreen.stream
                .getMediaStream()
                .getVideoTracks()[0]
                .addEventListener("ended", () => {
                  this.sessionScreen.unpublish(this.publisherScreen);
                  this.$refs.control.isShared = false;
                });

              this.sessionScreen.publish(this.publisherScreen);
            });
            this.publisherScreen.on("videoElementCreated", function (event) {
              event.element["muted"] = true;
            });
            this.publisherScreen.once("accessDenied", () => {
              console.warn("ScreenShare: Access Denied");
              this.$refs.control.isShared = false;
            });
          })
          .catch((error) => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });
    },

    stopShare() {
      this.sessionScreen.unpublish(this.publisherScreen);
    },

    recordingStart() {
      this.recording = true;
      this.setIsRecording(true);
      recordingStartApi(
        this.mySessionId,
        ({ data }) => {
          this.RECORDING_ID = data.id;
          console.log("레코딩 id", this.RECORDING_ID);
        },
        (error) => console.log(error)
      );
    },

    //registRecordVideoApi
    recordingEnd() {
      let lectureId = 0;
      this.recording = false;
      this.setIsRecording(false);
      console.log("msglst:", this.msgList);
      recordingStopApi(
        this.RECORDING_ID,
        ({ data }) => {
          console.log(data);
          registRecordVideoApi(
            {
              //RoomId를 몰라용
              lectureRoomId: this.lectureRoomId,
              id: data.id,
              name: data.name,
              sessionId: data.sessionId,
              createdAt: data.createdAt,
              duration: data.duration * 1000,
              url: data.url,
              chatRecord: JSON.stringify(this.msgList),
              // 채팅로그 가져옵시다
            },
            (res) => {
              console.log(res);
              lectureId = res.data.lectureId;
              this.endLecture([this.lectureRoomId, lectureId]);
            },
            () => {}
          );
        },
        (error) => console.log(error)
      );
    },

    leaveSession() {
      if (this.sessionCamera) this.sessionCamera.disconnect();
      if (this.sessionScreen) this.sessionScreen.disconnect();

      this.sessionCamera = undefined;
      this.sessionScreen = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OVCamera = undefined;
      this.OVScreen = undefined;
      this.speaker = undefined;

      closeSessionApi(
        this.mySessionId,
        () => {},
        () => {}
      );

      window.removeEventListener("beforeunload", this.leaveSession);
      this.clearMessage();
      this.$router.push("/main");
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
          "MODERATOR",
          ({ data }) => resolve(data.token),
          (error) => reject(error.response)
        );
      });
    },
  },

  mounted() {
    this.joinSession();
  },
};
</script>
<style>
.teacher-components {
  display: grid;
  grid-template-columns: 3fr 1fr 1fr;
  grid-template-rows: 1fr 70px;
  background: #c1c1c1;
  height: 100vh;
}
</style>
