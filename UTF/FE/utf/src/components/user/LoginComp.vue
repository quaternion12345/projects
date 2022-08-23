<!-- eslint-disable prettier/prettier -->
<template>
  <b-container>
    <b-row>
      <b-col sm="4" class="align-self-center img-container">
        <img src="@/assets/UTFLogo-removebg.png" alt="로고" style="margin-bottom: 50px" /><br />
        <img id="kakaoLoginBtn" class="loginBtn mb-2" src="@/assets/image/kakao_login_medium_wide.png" alt="카카오 로그인 버튼" /><br />
        <img id="naverLoginBtn" class="loginBtn mb-2" src="@/assets/image/btnG_완성형.png" alt="네이버 로그인 버튼" /><br />
        <img id="googleLoginBtn" class="loginBtn" src="@/assets/image/btn_google_signin_dark_normal_web@2x.png" alt="구글 로그인 버튼" />
      </b-col>
      <b-col sm="8" class="loginBg">
      </b-col>
    </b-row>
  </b-container>
</template>
<script>
const KAKAO_REST_API_KEY = process.env.VUE_APP_KAKAO_REST_API_KEY;
const KAKAO_REDIRECT_URI = process.env.VUE_APP_KAKAO_REDIRECT_URI;
const getKakao = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}&response_type=code`;

const NAVER_CLIENT_ID = process.env.VUE_APP_NAVER_CLIENT_ID;
const NAVER_CLIENT_SECRET = process.env.VUE_APP_NAVER_CLIENT_SECRET;
const NAVER_CALLBACK_URL = process.env.VUE_APP_NAVER_CALLBACK_URL;
const getNaver = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${NAVER_CLIENT_ID}&state=${NAVER_CLIENT_SECRET}&redirect_uri=${NAVER_CALLBACK_URL}`;

const GOOGLE_CLIENT_ID = process.env.VUE_APP_GOOGLE_CLIENT_ID;
const GOOGLE_REDIRECT_URI = process.env.VUE_APP_GOOGLE_REDIRECT_URI;
const getGoogle = `https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A//www.googleapis.com/auth/drive.metadata.readonly&access_type=offline&include_granted_scopes=true&response_type=code&state=state_parameter_passthrough_value&redirect_uri=${GOOGLE_REDIRECT_URI}&client_id=${GOOGLE_CLIENT_ID}`;

export default {
  name: "LoginComp",
  mounted() {
    const kakaoLoginBtn = document.querySelector("#kakaoLoginBtn");
    function kakaoLogin() {
      window.location.assign(getKakao);
    }
    kakaoLoginBtn.onclick = kakaoLogin;

    const naverLoginBtn = document.querySelector("#naverLoginBtn");
    function naverLogin() {
      window.location.assign(getNaver);
    }
    naverLoginBtn.onclick = naverLogin;

    const googleLoginBtn = document.querySelector("#googleLoginBtn");
    function googleLogin() {
      window.location.assign(getGoogle);
    }
    googleLoginBtn.onclick = googleLogin;
  },
};
</script>

<style scoped>
.loginBtn {
  width: 80%;
  height: 60px;
  cursor: pointer;
}
.loginBg {
  height: 100vh;
  background-image: url("@/assets/image/photo-1456513080510-7bf3a84b82f8.jpg");
  background-repeat: no-repeat;
  background-size: cover;
}
</style>
