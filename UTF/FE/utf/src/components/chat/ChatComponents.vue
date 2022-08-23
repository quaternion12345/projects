<template>
  <div class="chat">
    <chat-list :msgs="msgList"></chat-list>
    <chat-form @submitMessage="sendMessage"></chat-form>
  </div>
</template>

<script>
// docker run -p 4443:4443 --rm -e OPENVIDU_SECRET=MY_SECRET openvidu/openvidu-server-kms:2.22.0
import { mapState, mapMutations } from "vuex";
import ChatList from "@/components/chat/ChatList";
import ChatForm from "@/components/chat/ChatForm";
export default {
  name: "ChatComponents",
  data() {
    return {
      username: this.getUserName(this.publisher.session.connection.data),
      timer: undefined,
    };
  },
  components: {
    ChatList,
    ChatForm,
  },
  props: {
    publisher: Object,
    subscribers: Array,
  },
  computed: {
    ...mapState("chatStore", ["msgList"]),
  },
  created() {},
  mounted() {
    this.receiveMessage();
  },
  methods: {
    ...mapMutations("chatStore", {
      pushMessage: "PUSH_MSG_DATA",
    }),
    /* Send a general message */
    sendMessage(msg) {
      this.sendMessageTemplate(msg, this.subscribers, "general");
    },
    /* Receive a message from current session */
    receiveMessage() {
      this.publisher.session.on("signal:general", (event) => {
        if (
          this.publisher.stream.connection.connectionId !=
          event.from.connectionId
        ) {
          this.pushMessage({
            from: {
              name: this.getUserName(event.from.data), //username,
              time: this.getCurrentTime(),
              type: "general",
            },
            msg: event.data,
          }); // save to vuex
          setTimeout(() => {
            const element = document.getElementById("chat-body");
            element.scrollTop = element.scrollHeight;
          }, 100);
        }
      });
      this.publisher.session.on("signal:alert", (event) => {
        if (
          this.publisher.stream.connection.connectionId !=
          event.from.connectionId
        ) {
          this.pushMessage({
            from: {
              name: this.getUserName(event.from.data), //username,
              time: this.getCurrentTime(),
              type: "alert",
            },
            msg: event.data,
          }); // save to vuex
          setTimeout(() => {
            const element = document.getElementById("chat-body");
            element.scrollTop = element.scrollHeight;
          }, 100);
        }
      });
    },
    /* A template for sending a message
     * Input: data(JSON: message), to(Array: target), type(String: message tag)
     */
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
              name: this.username,
              time: this.getCurrentTime(),
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
    /* Get a current time */
    getCurrentTime() {
      return new Date().toLocaleTimeString();
    },
    /* Get a user name */
    getUserName(name) {
      var idx = name.slice(15).indexOf('"');
      return name.slice(15, 15 + idx);
    },
  },
};
</script>

<style scoped>
.chat {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
  background: #f7f7f4;
  margin: 5px 5px 5px 5px;
  overflow-x: hidden;
  border-radius: 12px 12px 12px 12px;
}
</style>
