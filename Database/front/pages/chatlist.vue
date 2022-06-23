<template>
  <v-card
    class="mx-auto"
    max-width="500"
  >
  <v-container>
    <v-header>채팅 목록</v-header>
    <v-btn dark color="blue" nuxt to="/friendSearch" type="submit">친구 검색</v-btn>
  </v-container>

  <v-divider></v-divider>
  
    <v-list subheader>
      <v-subheader>진행 중인 채팅</v-subheader>

      <v-list-item
        v-for="chat in recent"
        :key="chat.title"
      >
        <v-list-item-avatar>
          <v-img
            :alt="`${chat.title} avatar`"
            :src="chat.avatar"
          ></v-img>
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title v-text="chat.title"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
           <v-btn :color="chat.active ? 'blue' : 'grey'" nuxt to="/chatroom" type="submit">채팅 하기</v-btn>
        </v-list-item-icon>
      </v-list-item>
    </v-list>
  <v-container>
    <bottom-nav />
  </v-container>
  </v-card>
</template>

<script>
  import BottomNav from '../components/BottomNav.vue';
  export default {
    components: {
      BottomNav,
    },
    data: () => ({
      recent: [
        {
          active: true,
          avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
          title: 'Jason Oner',
          
        },
        {
          active: true,
          title: '채팅 리스트를 불러옵시다',
        },
        {
          active: false,
          avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
          title: 'Cindy Baker',
        },
        {
          active: false,
          avatar: 'https://cdn.vuetifyjs.com/images/lists/4.jpg',
          title: 'Ali Connors',
        },
      ],
    }),
  }
</script>


<!--<template>
  <div class="chat-wrapper">
    <div
      ref="chat"
      class="chat"
    >
      <Message
        v-for="(message, index) in messages"
        :key="`message-${index}`"
        :message="message"
        :owner="message.id === user.id"
      />
    </div>
    <!-- <div
      v-if="typingUsers.length"
      class="chat__typing"
    >
      <p
        v-for="(typingUser, index) in typingUsers"
        :key="`typingUser-${index}`"
        class="chat__typing-user"
      >
        {{ typingUser.name }} is typing...
      </p>
    </div>
    <div class="chat__form">
      <ChatForm />
    </div>
  </div>
</template>
<script>
import { mapState, mapGetters } from "vuex";
import Message from "@/components/Message";
import ChatForm from "@/components/ChatForm";
export default {
  name: "ChatPage",
  layout: "chat",
  components: {
    Message,
    ChatForm,
  },
  computed: {
    ...mapState(["user", "messages", "users"]),
    // ...mapGetters(["typingUsers"]),
  },
  watch: {
    messages() {
      setTimeout(() => {
        if (this.$refs.chat) {
          this.$refs.chat.scrollTop = this.$refs.chat.scrollHeight;
        }
      }, 0);
    },
  },
  // head() {
  //   return {
  //     title: `Room ${this.user.room}`,
  //   };
  // },
};
</script>
<style scoped>
.chat-wrapper {
  height: 100%;
  position: relative;
  overflow: hidden;
}
.chat__form {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 1rem;
  height: 80px;
}
.chat {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 80px;
  padding: 1rem;
  overflow-y: auto;
  color: #000;
}
.chat__typing {
  position: absolute;
  display: flex;
  bottom: 50px;
}
.chat__typing-user:not(first-child) {
  margin-left: 15px;
}
</style>-->