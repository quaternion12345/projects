<template>  
<v-card
    class="mx-auto"
    max-width="500"
  >
  
  <v-container>
    <v-card
    elevation="1"
    outlined
    shaped
    >
  <v-img :src="require('./1.jpg')"
      height="300px"
    ></v-img>
  <v-card-title>
      <nuxt-link to="./">공학관</nuxt-link>
    </v-card-title>

    </v-card>

     <v-card
    elevation="1"
    outlined
    shaped
    >

  <v-img :src="require('./2.jpg')"
      height="300px"
    ></v-img>
      <v-card-title>
      <nuxt-link to="./">백양관</nuxt-link>
    </v-card-title>
    </v-card>

 <v-card
    elevation="1"
    outlined
    shaped
    >
  
  <v-img :src="require('./3.png')"
      height="300px"
    ></v-img>
  <v-card-title>
      <nuxt-link to="./">학생회관</nuxt-link>
    </v-card-title>
    </v-card>


     <v-card
    elevation="1"
    outlined
    shaped
    >

  <v-img :src="require('./4.jpg')"
      height="300px"
    ></v-img>
       <v-card-title>
      <nuxt-link to="./">신촌역</nuxt-link>
    </v-card-title>
    </v-card>


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
