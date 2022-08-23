/* eslint-disable prettier/prettier */
import axios from "axios";
import store from "@/store/index.js";
import router from "@/router/index.js";

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL;

async function loginRequest(socialLoginType, code, state) {
    await axios
        .post(API_BASE_URL + `/api/user/socialLogin`, {
            data: {
                socialLoginType: socialLoginType,
                code: code,
                state: state,
            },
        })
        .then((res) => {
            console.log(res);
            if (!res.data["userExist"]) {
                store.dispatch("saveTempUserInfo", res.data);
                router.push({ path: "/joinForm" });
            } else {
                store.dispatch("saveUserInfo", res.data);
            }
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/" });
        });
}

async function sendName(name) {
    await axios
        .post(API_BASE_URL + `/api/user/join`, {
            userName: name,
            socialLoginType: store.state.tempUserInfo["socialLoginType"],
            socialId: store.state.tempUserInfo["socialId"],
            email: store.state.tempUserInfo["email"],
        })
        .then((res) => {
            console.log(res);
            store.dispatch("saveUserInfo", res.data);
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/" });
        });
}

function deleteUserRequest(userId) {
    axios
        .delete(API_BASE_URL + `/api/user/${userId}`)
        .then((res) => {
            console.log(res);
            router.push({ path: "/" });
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/" });
        });
}
async function updateUserRequest(userId, userName, email) {
    await axios
        .put(API_BASE_URL + `/api/user/${userId}`, {
            userName: userName,
            email: email,
        })
        .then((res) => {
            console.log(res);
            store.dispatch("saveUserInfo", res.data);
            router.push({ path: "/main" });
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/" });
        });
}
async function registLectureRoomRequest(lectureName, subject) {
    await axios
        .post(API_BASE_URL + `/api/lectureRoom`, {
            userId: store.state.userInfo["userId"],
            title: lectureName,
            subject: subject,
        })
        .then((res) => {
            console.log(res);
            store.dispatch('addLectureRoom', res.data);
            router.push({ path: "/main" });
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/main" });
        });
}
function getLectureRoomList(userId) {
    axios
        .get(API_BASE_URL + `/api/lectureRoom/${userId}`)
        .then((res) => {
            console.log(res);
            store.dispatch("saveLectureRoomList", res.data["lectureRoomList"]);
            router.push({ path: "/main" });
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/main" });
        });
}
function deleteLectureRoom(lectureRoomId) {
    axios
        .delete(API_BASE_URL + `/api/lectureRoom/${lectureRoomId}`)
        .then((res) => {
            console.log(res);
            router.push({ path: "/main" });
        })
        .catch((err) => {
            console.log(err);
            router.push({ path: "/main" });
        });
}

// axios 객체 생성
function apiInstance() {
    const API_INSTANCE_BASE_URL = process.env.VUE_APP_API_BASE_URL;
    const instance = axios.create({
        baseURL: API_INSTANCE_BASE_URL,
        headers: {
            "Content-type": "application/json",
        },
    });
    return instance;
}

function openviduInstance() {
    const OPENVIDU_BASE_URL = process.env.VUE_APP_OPENVIDU_BASE_URL;
    const instance = axios.create({
        baseURL: OPENVIDU_BASE_URL,
        headers: {
            "Content-type": "application/json",
        },
    });
    return instance;
}

export {
    loginRequest,
    sendName,
    deleteUserRequest,
    updateUserRequest,
    registLectureRoomRequest,
    getLectureRoomList,
    deleteLectureRoom,
    apiInstance,
    openviduInstance,
};
