<template>
  <div>
    <v-container>
      <v-card
        class="mx-auto"
        max-width="500"      
        >
        <v-container>
          <v-subheader>회원가입</v-subheader>
          <v-form ref="form" v-model="valid" @submit.prevent="onSubmitForm">
            <v-text-field
              v-model="id"
              label="아이디"
              type="id"
              :rules="idRules"
              required
            />
            <v-text-field
              v-model="password"
              label="비밀번호"
              type="password"
              :rules="passwordRules"
              required
            />
            <v-text-field
              v-model="passwordCheck"
              label="비밀번호확인"
              type="password"
              :rules="passwordCheckRules"
              required
            />
            <v-text-field
              v-model="nickname"
              label="닉네임"
              type="nickname"
              :rules="nicknameRules"
              required
            />
            <v-select
              v-model="membership"
              label="회원구분"
              :items="membership"
              item-text="name"
              item-value="value"
              type="membership"
              :rules="membershipRules"
              required
            />
            <v-checkbox
              v-model="terms"
              required
              :rules="[v => !!v || '약관에 동의해야 합니다.']"
              label="약관에 동의합니다."
            />
            <v-btn nuxt to="/login" color="blue">이전</v-btn>
            <v-btn color="blue" type="submit">가입하기</v-btn>
          </v-form>
        </v-container>
      </v-card>
    </v-container>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        valid: false,
        id: '',
        password: '',
        passwordCheck: '',
        nickname: '',
        membership: '',
        terms: false,
        membership:[
          {name : '일반', value: '일반'},
          {name : '학생', value: '학생'},
          {name : '강사', value: '강사'},
          {name : '기업', value: '기업'}
        ],
        idRules: [
          v => !!v || '아이디는 필수입니다.',
          // v => /.+@.+/.test(v) || '아이디가 유효하지 않습니다.',
        ],
        nicknameRules: [
          v => !!v || '닉네임은 필수입니다.',
        ],
        passwordRules: [
          v => !!v || '비밀번호는 필수입니다.',
        ],
        passwordCheckRules: [
          v => !!v || '비밀번호 확인은 필수입니다.',
          v => v === this.password || '비밀번호가 일치하지 않습니다.',
        ],
        membershipRulse: [
          v => !!v || '회원구분은 필수입니다.',
        ],
      };
    },
    computed: {
      me() {
        return this.$store.state.users.me;
      }
    },
    methods: {
      onSubmitForm() {
        if (this.$refs.form.validate()) {
          this.$store.dispatch('users/signUp', {
            nickname: this.nickname,
            id: this.id,
            password: this.password,
            membership:this.membership,
          })
            .then(() => {
              this.$router.push({
                path: '/login',
              });
            })
            .catch(()=> {
               alert('폼이 유효하지 않습니다.'); 
            });
        }
      }
    },
    head() {
      return {
        title: '회원가입',
      };
    },
  };
</script>

<style>
</style>