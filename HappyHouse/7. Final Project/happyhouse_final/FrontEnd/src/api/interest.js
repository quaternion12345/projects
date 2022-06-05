import { apiInstance } from "./index.js";

const api = apiInstance();

async function getInterests(userid, success, fail) {
  await api.get(`/interest/${userid}`).then(success).catch(fail);
}

async function deleteInterest(no, success, fail) {
  await api.delete(`/interest/${no}`).then(success).catch(fail);
}

export { getInterests, deleteInterest };
