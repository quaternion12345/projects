<template>
  <div v-show="isShow" class="comment">
    <div class="head">{{ comment.userid }} ({{ comment.regtime }})</div>
    <div class="content" v-html="enterToBr(comment.content)"></div>
    <!-- 로그인 기능 구현 후 로그인한 자신의 글에만 보이게 한다. -->
    <div class="cbtn">
      <label @click="modifyCommentView">수정</label> |
      <label @click="deleteComment">삭제</label>
    </div>
  </div>
</template>

<script>
import { apiInstance } from "@/api/index.js";
const http = apiInstance();
export default {
  name: "comment-view",
  data() {
    return {
      isShow: true,
    };
  },
  props: {
    comment: Object,
  },
  methods: {
    modifyCommentView() {
      console.log("mod:", this.comment);
      this.$emit("m-comment", {
        comment: this.comment,
        // no: this.comment.no,
        // articleno: this.comment.articleno,
        // content: this.comment.content,
      });
    },
    deleteComment() {
      if (confirm("정말로 삭제?")) {
        http.delete(`/comment/${this.comment.no}`).then(({ data }) => {
          let msg = "삭제 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "삭제가 완료되었습니다.";
          }
          alert(msg);
          // 도서평(댓글) 얻기.
          this.$store.dispatch(
            "getComments",
            `/comment/${this.comment.articleno}`,
          );
        });
      }
    },
    enterToBr(str) {
      if (str) return str.replace(/(?:\r\n|\r|\n)/g, "<br />");
    },
  },
};
</script>

<style>
.comment {
  text-align: left;
  border-radius: 5px;
  background-color: #d6e7fa;
  padding: 10px 20px;
  margin: 10px;
}
.head {
  font-weight: bold;
  margin-bottom: 5px;
}
.content {
  padding: 5px;
}
.cbtn {
  text-align: right;
  color: steelblue;
  margin: 5px 0px;
}
</style>
