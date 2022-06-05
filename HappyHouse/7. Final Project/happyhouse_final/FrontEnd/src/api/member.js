import { apiInstance } from "./index.js";

const api = apiInstance();

async function login(user, success, fail) {
  await api.post(`/user/login`, JSON.stringify(user)).then(success).catch(fail);
}

async function findById(userid, success, fail) {
  api.defaults.headers["access-token"] = sessionStorage.getItem("access-token");
  await api.get(`/user/info/${userid}`).then(success).catch(fail);
}

function registMember(member, success, fail) {
  api.post(`/user/register`, JSON.stringify(member)).then(success).catch(fail);
}
function modifyMember(member, success, fail) {
  api
    .put(`/user/modify/${member.userid}`, JSON.stringify(member))
    .then(success)
    .catch(fail);
}
function deleteMember(userid, success, fail) {
  api.delete(`/user/delete/${userid}`).then(success).catch(fail);
}

// function logout(success, fail)

export { login, findById, registMember, modifyMember, deleteMember };
