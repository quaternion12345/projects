<!-- eslint-disable prettier/prettier -->
<template>
  <div :class="{ background: !darkMode, backgroundDark: darkMode }">
    <b-container style="padding-top: 40px">
      <b-row style="min-height: 100vh">
        <b-col sm="3" :class="{ left: !darkMode, leftDark: darkMode }">
          <logo class="mb-3" />
          <profile :class="{ displayNone: hideProfile }" @setting="toggle" @logout="logout" v-bind:darkMode="darkMode" />
          <profile-setting :class="{ displayNone: hideProfileSetting }" @cancel="toggle" @toggle="toggle" v-bind:darkMode="darkMode" />
          <h2 class="mt-3">Recently</h2>
          <recetly-lecture v-bind:darkMode="darkMode" />
        </b-col>
        <b-col sm="9" :class="{ right: !darkMode, rightDark: darkMode }">
          <h2>Class</h2>
          <b-row class="cards">
            <b-col sm="3" v-for="(lecture, index) in lectures" :key="index" style="margin-bottom: 20px">
              <lecture-item class="buttons" @emitIndex="saveIndex" :lecture="lecture" :index="index" :darkMode="darkMode"></lecture-item>
              <!-- Modal -->
              <div class="modal fade" id="deleteLectureModal" tabindex="-1" aria-labelledby="deleteLectureModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="deleteLectureModalLabel" style="color: black">강의를 삭제하시겠습니까?</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-danger" data-bs-dismiss="modal" @click="deleteLecture(deleteIndex, deleteLectureRoomId)">삭제</button>
                      <button type="button" class="btn btn-secondaty" data-bs-dismiss="modal">취소</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="w-100" v-if="index % 4 == 0"></div>
            </b-col>
            <b-col sm="3">
              <add-lecture
                style="margin-bottom: 20px"
                @mouseover="mouseOverAdd()"
                @mouseout="mouseOutAdd()"
                @registLecture="registLecture"
                @addBtnFlip="
                  () => {
                    addBtnFliped = !addBtnFliped;
                  }
                "
                :class="{ 'opacity-50': addBtn && !addBtnFliped }"
                v-bind:darkMode="darkMode"
              />
            </b-col>
          </b-row>
          <label for="toggle" class="toggleSwitch" style="position: absolute; left: 85%; bottom: 100px">
            <span class="toggleButton"></span>
          </label>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
/* eslint-disable prettier/prettier */
import Logo from "@/components/common/Logo.vue";
import Profile from "@/components/main/Profile.vue";
import ProfileSetting from "@/components/main/ProfileSetting.vue";
import RecetlyLecture from "@/components/main/RecentlyLecture.vue";
import LectureItem from "@/components/main/LectureItem.vue";
import { registLectureRoomRequest } from "@/api/index.js";
import { deleteLectureRoom } from "@/api/index.js";
import store from "@/store";
import router from "@/router";
import AddLecture from "@/components/main/AddLecture.vue";

export default {
  name: "MainVue",
  components: {
    Logo,
    Profile,
    ProfileSetting,
    RecetlyLecture,
    LectureItem,
    AddLecture,
  },
  data() {
    return {
      lectures: [],
      addBtn: false,
      addBtnFliped: false,
      deleteIndex: -1,
      deleteLectureRoomId: -1,
      hideProfile: false,
      hideProfileSetting: true,
      darkMode: false,
    };
  },
  mounted() {
    console.log("mounted");
    this.lectures = store.state.lectureRoomList;

    const $toggle = document.querySelector(".toggleSwitch");

    $toggle.onclick = () => {
      this.darkMode = !this.darkMode;
      $toggle.classList.toggle("active");
    };
  },
  methods: {
    toggle() {
      this.hideProfile = !this.hideProfile;
      this.hideProfileSetting = !this.hideProfileSetting;
    },
    logout() {
      store.replaceState = {};
      sessionStorage.clear();
      router.push({ path: "/" });
    },
    mouseOverAdd() {
      this.addBtn = true;
    },
    mouseOutAdd() {
      this.addBtn = false;
    },
    async registLecture(lectureName, subject) {
      await registLectureRoomRequest(lectureName, subject);
    },
    saveIndex(index, lectureRoomId) {
      console.log(index);
      console.log(lectureRoomId);
      this.deleteIndex = index;
      this.deleteLectureRoomId = lectureRoomId;
    },
    deleteLecture(index, lectureRoomId) {
      console.log(index);
      console.log(lectureRoomId);
      this.lectures.splice(index, 1);
      deleteLectureRoom(lectureRoomId);
    },
  },
};
</script>

<style>
@import "./main.css";

.background {
  background-color: #f7f7f4;
  color: #000000;
}
.backgroundDark {
  background-color: #17171b;
  color: white;
}
.left {
  background-color: #f7f7f4;
  border-right: #c1c1c1 solid 1px;
}
.leftDark {
  background-color: #17171b;
  border-right: #3e3e3e solid 1px;
}
.right {
  background-color: #f7f7f4;
}
.rightDark {
  background-color: #17171b;
}
.buttons {
  cursor: pointer;
}
.displayNone {
  display: none;
}
.profileBox {
  background-color: #ffffff;
  border-radius: 20px;
  border: solid #c1c1c1 1px;
}
.profileBoxDark {
  background-color: #202026;
  border-radius: 20px;
  border: solid #3e3e3e 1px;
}
.toggleSwitch {
  width: 3rem;
  height: 1.5rem;
  display: block;
  position: relative;
  border-radius: 1rem;
  background-color: #fff;
  box-shadow: 0 0 0.1rem 3px rgba(0 0 0 / 15%);
  cursor: pointer;
}

.toggleSwitch .toggleButton {
  width: 1.3rem;
  height: 1.3rem;
  position: absolute;
  top: 50%;
  left: 0.1rem;
  transform: translateY(-50%);
  border-radius: 50%;
  background: #000000;
}

/* 체크박스가 체크되면 변경 이벤트 */
.toggleSwitch.active {
  background: #000000;
}

.toggleSwitch.active .toggleButton {
  left: calc(100% - 1.4rem);
  background: #fff;
}

.toggleSwitch,
.toggleButton {
  transition: all 0.2s ease-in;
}
</style>
