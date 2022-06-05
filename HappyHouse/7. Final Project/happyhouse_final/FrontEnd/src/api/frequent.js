import { apiInstance } from "./index.js";

const api = apiInstance();

async function getFrequents(userid, success, fail) {
  await api.get(`/frequent/${userid}`).then(success).catch(fail);
}

async function deleteFrequent(no, success, fail) {
  await api.delete(`/frequent/${no}`).then(success).catch(fail);
}

export { getFrequents, deleteFrequent };
