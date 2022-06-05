<template>
  <div class="regist">
    <div v-if="this.modifyComment != null" class="regist_form">
      <textarea
        id="modicomment"
        name="modicomment"
        v-model="modicomment.comment.content"
        cols="35"
        rows="2"
      ></textarea>
      <!-- <textarea id="comment" name="comment" v-text="modifyComment.comment" ref="comment" cols="35" rows="2">
      </textarea> -->
      <button class="small" @click="updateCommentCancel">취소</button>
      <button class="small" @click="updateComment">수정</button>
    </div>
    <div v-else class="regist_form">
      <textarea
        id="comment"
        name="comment"
        v-model="content"
        cols="35"
        rows="2"
      ></textarea>
      <button
        class="small"
        style="margin: 30px 20px 0px 0px"
        @click="registComment"
      >
        등록
      </button>
    </div>
  </div>
</template>

<script>
import { apiInstance } from "@/api/index.js";
// import { mapGetters, mapActions } from "vuex";
const http = apiInstance();
export default {
  name: "comment-write",
  data() {
    return {
      // 차후 작성자 이름은 로그인 구현후 로그인한 사용자로 바꾼다.
      user_name: "",
      content: "",
      modicomment: this.modifyComment, //props 는 직접 변경 X
    };
  },
  // computed: {
  //   // ...mapGetters(["comments"]),
  //   // ...mapActions(["getComments"]),
  // },
  props: {
    articleno: { type: Number },
    modifyComment: { type: Object },
  },
  methods: {
    registComment() {
      http
        .post("/comment/", {
          userid: "SSAFY",
          content: this.content,
          articleno: this.articleno,
        })
        .then(({ data }) => {
          let msg = "등록 처리시 문제가 발생했습니다.";
          if (data === "success") {
            msg = "등록이 완료되었습니다.";
          }
          alert(msg);

          // 작성글 지우기
          this.content = "";

          // 도서평(댓글) 얻기.
          this.$store.dispatch("getComments", `/comment/${this.articleno}`);
        });
    },
    updateComment() {
      console.log("modifyComment: ", this.modifyComment);
      console.log("modicomment: ", this.modicomment.comment);
      http
        .put(`/comment/${this.modicomment.comment.no}`, {
          // no: this.modifyComment.no,
          //  comment: this.modifyComment.comment, //에러나요 ~~
          //부모뷰에서 자식뷰로 전달한 데이터는 수정하지말고 따로 값을 변수화하여 사용
          no: this.modicomment.comment.no,
          articleno: this.modicomment.comment.articleno,
          userid: this.modicomment.comment.userid,
          content: this.modicomment.comment.content,
          // regtime: this.modicomment.comment.regtime,
        })
        .then(({ data }) => {
          let msg = "수정 처리시 문제가 발생했습니다.";
          console.log("data: ", data);
          if (data === "success") {
            msg = "수정이 완료되었습니다.";
          }
          alert(msg);

          // 도서평(댓글) 얻기.
          console.log("ano:", this.modicomment.comment.articleno);
          this.$store.dispatch(
            "getComments",
            `/comment/${this.modicomment.comment.articleno}`,
          );

          this.$emit("modify-comment-cancel", false);
        });
    },
    updateCommentCancel() {
      this.$emit("modify-comment-cancel", false);
    },
  },
};
</script>

<style scoped>
.regist {
  padding: 10px;
}
.regist_form {
  text-align: left;
  border-radius: 5px;
  background-color: #d6e7fa;
  padding: 20px;
}

textarea {
  width: 90%;
  padding: 10px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  color: #787878;
  font-size: large;
}

button {
  float: right;
}

button.small {
  width: 45px;
  margin: 35px 0px 0px 5px;
  font-size: small;
  font-weight: bold;
}
</style>
