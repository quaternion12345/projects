<template>
  <div class="user-list-components">
    <div class="statistics-info">
      <understanding-chart :lectureRoomId="lectureRoomId" />
      <focus-chart :lectureRoomId="lectureRoomId" />
    </div>
    <div class="userlist-info">
      <p class="userlist-header">
        {{ "참가인원 " + subscribers.length + "명" }}
      </p>
      <user-info
        v-for="subscriber in orderedUsers"
        :key="subscriber.stream.connection.data"
        :subscriber="subscriber"
        :time="missedStudent.get(subscriber.stream.connection.connectionId)"
      >
      </user-info>
    </div>
  </div>
</template>

<script>
import UserInfo from "@/components/lecture/teacher/UserInfo";
import UnderstandingChart from "@/components/lecture/teacher/UnderstandingChart.vue";
import FocusChart from "@/components/lecture/teacher/FocusChart.vue";
export default {
  props: {
    subscribers: Array,
    publisher: Object,
    lectureRoomId: Number,
  },
  components: {
    UserInfo,
    UnderstandingChart,
    FocusChart,
  },
  data() {
    return {
      missedStudent: new Map(),
    };
  },
  mounted() {
    this.updateUser();
  },
  computed: {
    orderedUsers: function () {
      console.log("sub: ", this.subscribers);
      return this.subscribers
        .slice()
        .sort((a, b) =>
          a.stream.connection.data > b.stream.connection.data ? 1 : -1
        );
    },
  },
  methods: {
    /* Save map for missed students */
    updateUser() {
      this.publisher.session.on("signal:missed", (event) => {
        var conId = event.from.connectionId;
        var time = event.data;
        this.missedStudent.set(conId, time);
        console.log("Id, time: ", conId, time);
      });
      this.publisher.session.on("signal:comeback", (event) => {
        var conId = event.from.connectionId;
        this.missedStudent.delete(conId);
        console.log("Id: ", conId);
      });
    },
  },
};
</script>

<style scoped>
.user-list-components {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  max-height: 100%;
  border-radius: 12px 12px 12px 12px;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
  margin: 5px 5px 5px 5px;
  overflow-x: hidden;
}
.statistics-info {
  display: flex;
  background: #ffffff;
  color: #292929;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
  border-radius: 12px 12px 12px 12px;
  padding: 1rem;
  font-size: 16px;
  font-weight: 700;
  height: 25%;
}
.userlist-info {
  background: #ffffff;
  color: #292929;
  text-align: left;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
  border-radius: 12px 12px 12px 12px;
  padding: 1rem;
  font-size: 16px;
  font-weight: 700;
  min-height: 75%;
  margin: 5px 0px 0px 0px;
}
.userlist-header {
  text-align: left;
  color: blue;
  background: #ffffff;
  margin: 0px;
  padding: 0.5rem;
}
.user-list-components::-webkit-scrollbar {
  width: 6px;
}
.user-list-components::-webkit-scrollbar-track {
  background-color: transparent;
}
.user-list-components::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background-color: gray;
}
.user-list-components::-webkit-scrollbar-button {
  width: 0;
  height: 0;
}
</style>
