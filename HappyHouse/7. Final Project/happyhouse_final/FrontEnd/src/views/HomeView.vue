<template>
  <b-container fluid class="bv-example-row mt-3 text-center">
    <!-- <h3 class="underline-steelblue"><b-icon icon="house"></b-icon> SSAFY</h3> -->
    <b-row align-v="center">
      <b-col>
        <div class="banner_text">
          <div class="banner_text_iner">
            <h1>행복한 우리집</h1>

            <p>실거래가 조회와 관심정보를 <br />한눈에 파악하세요</p>
            <a href="#" @click="moveAbout">자세히 보기</a>
          </div>
        </div>
      </b-col>
      <b-col>
        <section class="banner_part">
          <div class="container">
            <div class="row align-items-center">
              <div class="col-md-5"></div>
            </div>
          </div>
          <div class="banner_img">
            <img
              src="@/assets/img/banner.png"
              style="width: 80%"
              alt="#"
              class="img-fluid"
            />
            <img
              src="@/assets/img/banner_pattern.png"
              alt="#"
              class="pattern_img img-fluid"
            />
          </div>
        </section>
        <!-- <template #header>SSAFY Home</template>

          <template #lead>
            슬기로운 싸피 생활 (:7기편) <br />
            행운을 부르는 러~~~~키 Seven!!!!!
          </template>

          <hr class="my-4" />

          <p>Vue + Bootstrap활용.</p>
          <p>Bootstrap-vue는 버전 <b>4.6.1</b>을 권장합니다.</p>
          <p><b>BoardList.vue</b>를 바꿔가면서 테스트하세요.</p>
          <p>Bootstrap의 <b>table</b> 사용법을 익히게됩니다.</p> -->
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "HomeView",
  props: {
    msg: String,
  },
  methods: {
    moveAbout() {
      this.$router.push("/about");
    },
  },
};
</script>

<style scoped>
.underline-steelblue {
  display: inline-block;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0) 70%,
    rgba(72, 190, 233, 0.3) 30%
  );
}
</style>
