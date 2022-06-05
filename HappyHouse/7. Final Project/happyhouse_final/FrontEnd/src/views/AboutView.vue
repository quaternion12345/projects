<template>
  <b-container class="bv-example-row mt-3 text-center">
    <b-row class="breadcrumb_part">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <div class="breadcrumb_iner">
              <h2>About HappyHouse</h2>
            </div>
          </div>
        </div>
      </div>
    </b-row>
    <!-- breadcrumb part end-->

    <!-- product list part start-->
    <b-row class="about_us padding_top">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-lg-8">
            <div class="about_us_content">
              <h5>비전</h5>
              <h3>정확하고 가독성있는 실거래가 정보 제공</h3>
              <img
                src="@/assets/img/about_us_video.png"
                alt="#"
                class="img-fluid"
              />
              <a
                class="about_video_icon popup-youtube"
                href="https://www.youtube.com/watch?v=DWHB6nTyKDI"
              ></a>
            </div>
          </div>
        </div>
      </div>
    </b-row>
    <!-- product list part end-->

    <!-- feature part here -->
    <b-row class="feature_part section_padding">
      <div class="container">
        <div class="row justify-content-between">
          <div class="col-lg-6">
            <div class="feature_part_tittle">
              <h3>행복한 집에서 살면 인생이 바뀐다</h3>
            </div>
          </div>
          <div class="col-lg-5">
            <div class="feature_part_content">
              <p>
                정확하고 가독성 있는 실거래가 정보는 물론이고 상권정보와
                교통정보를 제공합니다. 관심지역 설정까지@
              </p>
            </div>
          </div>
        </div>
      </div>
    </b-row>
  </b-container>
</template>
<script>
export default {
  name: "AboutView",
};
</script>
<style scoped>
.underline-hotpink {
  display: inline-block;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0) 70%,
    rgba(231, 27, 139, 0.3) 30%
  );
}
</style>
