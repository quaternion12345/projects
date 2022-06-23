<template>
  <v-card
    class="mx-auto"
    max-width="500"
  >
<v-list subheader>
      <v-subheader>친구 검색</v-subheader>
      <v-container>
          <v-form ref="form" v-model="valid" @submit.prevent="onSubmitForm">
                <v-text-field
                    v-model="friendSearch"
                    :rules="friendSearchRules"
                    label="친구 검색"
                    type="friendSearch"
                    required
                />
                <v-btn dark color="blue" type="submit">검색</v-btn>
            </v-form>
      </v-container>
      
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
          <v-icon :color="chat.active ? 'deep-purple accent-4' : 'grey'">
            mdi-message-outline
          </v-icon>
        </v-list-item-icon>
      </v-list-item>
    </v-list>

    <v-list subheader>
      <v-subheader>접속중인 친구</v-subheader>
      <v-container>
        접속한 친구 리스트 넣어주세요.
      </v-container>

      <v-list-item
        v-for="chat in previous"
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
      </v-list-item>
    </v-list>

    <v-divider></v-divider>

    <v-list subheader>
      <v-subheader>미접속 친구</v-subheader>
      <v-container>
        미접속 친구 리스트 넣어주세요.
      </v-container>

      <v-list-item
        v-for="chat in previous"
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
    }
  }
</script>
