<!-- eslint-disable prettier/prettier -->
<template>
  <div class="flip-card">
    <div class="flip-card-inner" :class="{ flip: isFlip }">
      <div class="buttons" :class="{ 'flip-card-front': !darkMode, 'flip-card-frontDark': darkMode }" @click="flip()">
        <i class="bi bi-plus" style="font-size: 170px"></i>
      </div>
      <div :class="{ 'flip-card-back': !darkMode, 'flip-card-backDark': darkMode }">
        <input type="text" id="lectureName" placeholder="강의명" />
        <select class="form-select" name="subject" id="subject">
          <option value="0">국어</option>
          <option value="1">영어</option>
          <option value="2">수학</option>
          <option value="3">사회</option>
          <option value="4">과학</option>
          <option value="5">음악</option>
          <option value="6">미술</option>
          <option value="7">공학</option>
          <option value="8">컴퓨터</option>
          <option value="9">기타</option>
        </select>
        <div class="btnGroup mt-3">
          <button type="button" class="blueBtn" @click="addLecture()">개설</button>
          <button type="button" class="redBtn" @click="flip()">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/* eslint-disable prettier/prettier */
export default {
  props: {
    darkMode: Boolean,
  },
  data() {
    return {
      isFlip: false,
    };
  },
  methods: {
    addLecture() {
      this.$emit("registLecture", document.querySelector("#lectureName").value, document.querySelector("#subject").value);
      document.querySelector("#subject").value = 0;
      this.flip();
    },
    flip() {
      this.isFlip = !this.isFlip;
      document.querySelector("#lectureName").value = "";
      this.$emit("addBtnFlip");
    },
  },
};
</script>

<style scoped>
/* The flip card container - set the width and height to whatever you want. We have added the border property to demonstrate that the flip itself goes out of the box on hover (remove perspective if you don't want the 3D effect */
.flip-card {
  background-color: transparent;
  width: 100%;
  perspective: 1000px; /* Remove this if you don't want the 3D effect */
}

/* This container is needed to position the front and back side */
.flip-card-inner {
  position: relative;
  width: 100%;
  text-align: center;
  transition: transform 0.8s;
  transform-style: preserve-3d;
}

/* Do an horizontal flip when you move the mouse over the flip box container */
/* .flip-card:hover .flip-card-inner {
  transform: rotateY(180deg);
} */
.flip {
  transform: rotateY(180deg);
}

/* Position the front and back side */
.flip-card-front,
.flip-card-back {
  position: absolute;
  width: 100%;
  height: 237px;
  border: 1px solid #c1c1c1;
  border-radius: 20px;
  -webkit-backface-visibility: hidden; /* Safari */
  backface-visibility: hidden;
}
.flip-card-frontDark,
.flip-card-backDark {
  position: absolute;
  width: 100%;
  height: 237px;
  border: 1px solid #3e3e3e;
  border-radius: 20px;
  -webkit-backface-visibility: hidden; /* Safari */
  backface-visibility: hidden;
}

/* Style the front side (fallback if image is missing) */
.flip-card-front {
  background-color: #ffffff;
}
.flip-card-frontDark {
  background-color: #202026;
}

/* Style the back side */
.flip-card-back {
  background-color: #ffffff;
  transform: rotateY(180deg);
}
.flip-card-backDark {
  background-color: #202026;
  transform: rotateY(180deg);
}
#lectureName {
  border: none;
  border-radius: 10px;
  width: 80%;
  margin: auto;
  margin-top: 50px;
  background-color: #e2ebff;
}
#subject {
  width: 80%;
  margin: auto;
  margin-top: 20px;
}
.btnGroup {
  display: flex;
  justify-content: space-evenly;
}
.btnGroup button {
  width: 80px;
  height: 30px;
}
</style>
