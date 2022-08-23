<!-- eslint-disable prettier/prettier -->
<template>
  <b-container class="mt-5">
    <img src="@/assets/UTFLogo-removebg.png" alt="logo" />
    <b-form inline @submit="submit()" class="mt-5">
      <b-row>
        <b-col sm="4"></b-col>
        <b-col sm="4">
          <div class="inputForm p-2">
            <b-form-input class="mr--sm-0 w-75" v-model="name" placeholder="이름을 입력해주세요" autofocus />
            <input type="submit" class="blueBtn" value="회원가입" />
          </div>
        </b-col>
        <b-col sm="4"></b-col>
      </b-row>
    </b-form>
  </b-container>
</template>

<script>
export default {
  name: "JoinFormComp",
  data() {
    return {
      name: "",
    };
  },
  methods: {
    submit() {
      console.log(this.name);
      this.$emit("getName", this.name);
    },
  },
};
</script>
<style scoped>
input::placeholder {
  text-align: center;
}
.inputForm {
  background-color: #cecece;
  display: flex;
  justify-content: space-evenly;
  border-radius: 10px;
  box-shadow: 2px 1px 1px whitesmoke;
}
</style>
