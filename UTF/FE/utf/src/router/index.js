import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView.vue";
import RedirectView from "@/views/RedirectView.vue";
import JoinFormView from "@/views/JoinFormView.vue";

const routes = [
  {
    path: "/",
    name: "login",
    component: LoginView,
  },
  {
    path: "/joinForm",
    name: "joinForm",
    component: JoinFormView,
  },
  {
    path: "/redirect",
    name: "redirect",
    component: RedirectView,
  },
  {
    path: "/lecture/:lectureRoomCode",
    name: "lecture",
    component: () => import("@/views/LectureRoom.vue"),
    props: true,
  },
  {
    path: "/main",
    name: "main",
    component: () => import("@/views/Main.vue"),
  },
  {
    path: "/exit",
    name: "exit",
    component: () => import("@/views/ExitRoom.vue"),
  },
  {
    path: "/lectureRoom/:lectureRoomId",
    name: "lectureRoom",
    component: () => import("@/views/LectureStatistics.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
