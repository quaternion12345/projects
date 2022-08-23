import { openviduInstance, apiInstance } from "./index.js";

const api = openviduInstance();
const backApi = apiInstance();
const OPENVIDU_SERVER_SECRET = "MY_SECRET";

async function recordingStopApi(recordingId, success, fail) {
  await api
    .post(
      `/openvidu/api/recordings/stop/${recordingId}`,
      {},
      {
        auth: {
          username: "OPENVIDUAPP",
          password: OPENVIDU_SERVER_SECRET,
        },
      }
    )
    .then(success)
    .catch(fail);
}

async function recordingStartApi(sessionId, success, fail) {
  let fileName = Math.random().toString(36).substring(2, 11);
  await api
    .post(
      `/openvidu/api/recordings/start`,
      JSON.stringify({
        session: sessionId,
        name: fileName,
        hasAudio: true,
        hasVideo: true,
        outputMode: "COMPOSED",
        recordingLayout: "CUSTOM",
        resolution: "1280x720",
        frameRate: 25,
        shmSize: 536870912,
        ignoreFailedStreams: false,
      }),
      {
        auth: {
          username: "OPENVIDUAPP",
          password: OPENVIDU_SERVER_SECRET,
        },
      }
    )
    .then(success)
    .catch(fail);
}

async function createSessionApi(sessionId, success, fail) {
  await api
    .post(
      `/openvidu/api/sessions`,
      JSON.stringify({
        customSessionId: sessionId,
      }),
      {
        auth: {
          username: "OPENVIDUAPP",
          password: OPENVIDU_SERVER_SECRET,
        },
      }
    )
    .then(success)
    .catch(fail);
}

async function createTokenApi(sessionId, role, success, fail) {
  await api
    .post(
      `/openvidu/api/sessions/${sessionId}/connection`,
      JSON.stringify({
        role: role,
      }),
      {
        auth: {
          username: "OPENVIDUAPP",
          password: OPENVIDU_SERVER_SECRET,
        },
      }
    )
    .then(success)
    .catch(fail);
}

async function closeSessionApi(sessionId, success, fail) {
  await api
    .delete(`/openvidu/api/sessions/${sessionId}`, {
      auth: {
        username: "OPENVIDUAPP",
        password: OPENVIDU_SERVER_SECRET,
      },
    })
    .then(success)
    .catch(fail);
}

async function registRecordVideoApi(recordReq, success, fail) {
  await backApi
    .post(`/api/lectureRoom/record`, recordReq)
    .then(success)
    .catch(fail);
}

export {
  createTokenApi,
  createSessionApi,
  closeSessionApi,
  recordingStartApi,
  recordingStopApi,
  registRecordVideoApi,
};
