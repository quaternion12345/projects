<template>
  <v-bottom-navigation
    :value="value"
    color="primary"
    grow
  >
    <v-btn nuxt to ='/friendpage'>
      <span>친구 목록</span>

      <v-icon>mdi-history</v-icon>
    </v-btn>

    <v-btn nuxt to ='/chat'>
      <span>채팅 목록</span>

      <v-icon>mdi-heart</v-icon>
    </v-btn>

    <v-btn nuxt to ='/mylocation'>
      <span>내 주변</span>

      <v-icon>mdi-map-marker</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
  export default {
    data: () => ({ value: 1 }),
  }
</script>