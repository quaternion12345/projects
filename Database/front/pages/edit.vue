<template>
  <div>
    <v-container>
      <v-card
        class="mx-auto"
        max-width="500"      
        >
        <v-container>
          <v-subheader>내 정보 수정</v-subheader>
          <v-form ref="form" v-model="valid" @submit.prevent="onSubmitForm">
            <v-text-field
              v-model="status_message"
              label="상태 메시지"
              type="status_message"
              :rules="status_messageRules"
              required
            />
            <v-select
              v-model="area"
              label="지역"
              :items="area"
              item-text="name"
              item-value="value"
              type="area"
              :rules="areaRules"
              required
            />
          </v-form>
        </v-container>
        <v-container>
            <v-btn color="blue" type="submit" nuxt to = "/friendpage">변경하기</v-btn>
            <v-btn color="blue" nuxt to="/friendpage">취소</v-btn>
        </v-container>
        <v-container>
            <v-btn color="blue" type="submit" nuxt to = "/login">로그아웃</v-btn>
            <v-btn color="blue" type="submit" nuxt to="/login">회원탈퇴</v-btn>
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
        status_message: '',
        terms: false,
        area:[
          {name : '학관', value: '학관'},
          {name : '백양관', value: '백악관'},
          {name : '공학관', value: '공학관'},
          {name : '신촌역', value: '신촌역'}
        ],
      };
    },
    methods: {
      onSubmitForm() {
        if (this.$refs.form.validate()) {
          this.$store.dispatch('users/signUp', {
            nickname: this.nickname,
            email: this.email,
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
        title: '내 정보 수정',
      };
    },
  };
</script>

<style>
</style>