<template>
  <LoginComp></LoginComp>
</template>

<script>
// @ is an alias to /src
import LoginComp from "@/components/user/LoginComp.vue";

export default {
  name: "LoginView",
  components: {
    LoginComp,
  },
};
</script>
