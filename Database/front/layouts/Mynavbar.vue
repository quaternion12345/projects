<template>
  <nav class="nav">
    <div class="logo">
      <a href="./main" class="logo text-lg">
        연세톡
      </a>
      <span class="subheader">연세대학교 데이터베이스 6조</span>
    </div>
    <ul>
      <li class="btn btn1 mx-5 px-5">
      <nuxt-link to="./login">logout</nuxt-link>
      </li>
      <li class="btn btn2">
      <nuxt-link to="./signup">회원가입</nuxt-link>
      </li>
      
      </ul>
  </nav>
</template>

<script>
export default {}
</script>

<style scoped>
nav {
  box-shadow: rgb(100 100 111 / 20%) 0px 7px 29px 0px;
  background-color: #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 70px;
  font-weight: ligher;
  letter-spacing: 0.5px;
}
.logo {
  box-sizing: content-box;
  padding: 0 12px;
  height: 100%;
  flex: 1 0 auto;
  display: flex;
  align-items: center;
  background-color: transparent;
}
.text-lg {
  font-size: 24px;
}
.subheader {
  color: #ccc;
}
a {
  color: var(--theme-link-color);
  text-decoration: none;
  cursor: pointer;
}

.btn {
  position: relative;
  display: inline-block;
  padding: .8em;
  color: var(--theme-button-color);
  border: 1px solid transparent;
  border-radius: 3px;
  background-color: transparent;
  outline: none;
  font-family: inherit;
  font-size: 13px;
  font-weight: normal;
  line-height: 1.15384615;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
  user-select: none;
  margin: 2px;
}
.btn:hover {
  opacity: 0.7;
}

.btn1 {
  background-color: #fcc;
}
.btn2 {
  background-color: #ffc;
}
.btn3 {
  background-color: #cff;
}
</style>