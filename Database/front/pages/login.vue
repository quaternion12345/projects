<template>
  <div>
    <v-container>
      <v-card 
        class="mx-auto"
        max-width="500"
      >
    <!-- <v-main class="white"> -->
        <v-container>
          <!-- <v-row align="center" justify-center> -->
          <!-- <v-row>
            <v-col cols="12" sm="8"> -->
              <v-form ref="form" v-model="valid" @submit.prevent="onSubmitForm">
                <v-text-field
                    v-model="email"
                    :rules="emailRules"
                    label="이메일"
                    type="email"
                    required
                />
                <v-text-field
                    v-model="password"
                    :rules="passwordRules"
                    label="비밀번호"
                    type="password"
                    required
                />
                <v-btn color="green" type="submit" :disabled="!valid">로그인</v-btn>
                <v-btn nuxt to="/signup">회원가입</v-btn>
              </v-form>
          <!-- </v-row> -->
            <!-- </v-col>
          </v-row> -->
        </v-container>
    <!-- </v-main> -->
      </v-card>
    </v-container>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        valid: false,
        email: '',
        password: '',
        emailRules: [
          v => !!v || '이메일은 필수입니다.',
          v => /.+@.+/.test(v) || '이메일이 유효하지 않습니다.',
        ],
        passwordRules: [
          v => !!v || '비밀번호는 필수입니다.',
        ],
      };
    },
    computed: {
      me() {
        return this.$store.state.users.me;
      },
    },
    methods: {
      onSubmitForm() {
        if (this.$refs.form.validate()) {
          this.$store.dispatch('users/login', {
            email: this.email,
            nickname: '제로초',
          }),
          this.$router.push({
            path: '/main',
          })              
        }
      },
      onLogOut() {
        this.$store.dispatch('users/logOut')
      }
    }
  }
</script>

<style>
</style>