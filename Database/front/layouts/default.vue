<template>
  <v-app>
    <Mynavbar>
    </Mynavbar>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>   

<script>
import Mynavbar from "./Mynavbar" 
  export default {
    name: 'default',
    components : {Mynavbar},
    data(){
      return {

      }
    } 
  }
</script>