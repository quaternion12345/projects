<template>
  <b-container class="bv-example-row mt-3">
    <b-row>
      <b-col>
        <b-alert show><h3>질문보기</h3></b-alert>
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col class="text-left">
        <b-button variant="outline-primary" @click="listArticle">목록</b-button>
      </b-col>
      <b-col class="text-right">
        <b-button
          variant="outline-info"
          size="sm"
          @click="moveModifyArticle"
          class="mr-2"
          >질문수정</b-button
        >
        <b-button variant="outline-danger" size="sm" @click="deleteArticle"
          >질문삭제</b-button
        >
      </b-col>
    </b-row>
    <b-row class="mb-1">
      <b-col>
        <b-card
          :header-html="`<h3>${article.articleno}.
          ${article.subject} [${article.hit}]</h3><div><h6>${article.userid}</div><div>${article.regtime}</h6></div>`"
          class="mb-2"
          border-variant="dark"
          no-body
        >
          <b-card-body class="text-left">
            <div v-html="message"></div>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>
    <comment-write :articleno="this.articleno" />
    <comment-write
      v-if="isModifyShow && this.modifyComment != null"
      :modifyComment="this.modifyComment"
      @modify-comment-cancel="onModifyCommentCancel"
    />
    <comment-view
      v-for="(comment, index) in comments"
      :key="index"
      :comment="comment"
      @m-comment="onModifyComment"
    />
  </b-container>
</template>

<script>
// import moment from "moment";
import { mapGetters } from "vuex";
import { getArticle, deleteArticle } from "@/api/qna";
import CommentWrite from "@/components/qna/comment/CommentWrite.vue";
import CommentView from "@/components/qna/comment/CommentView.vue";

export default {
  name: "QnaDetail",
  data() {
    return {
      article: {},
      articleno: "",
      isModifyShow: false,
      modifyComment: Object,
    };
  },
  computed: {
    ...mapGetters(["comments"]),
    // ...mapActions(["getComments"]),
    message() {
      if (this.article.content)
        return this.article.content.split("\n").join("<br>");
      return "";
    },
  },
  components: {
    CommentWrite,
    CommentView,
  },
  created() {
    getArticle(
      (this.articleno = this.$route.params.articleno),
      (response) => {
        this.article = response.data;
        console.log("article::: ", this.article);
      },
      (error) => {
        console.log("삭제시 에러발생!!", error);
      },
      this.$store.dispatch("getComments", `/comment/${this.articleno}`),
      console.log(this.articleno),
    );
  },
  methods: {
    listArticle() {
      this.$router.push({ name: "qnaList" });
    },
    moveModifyArticle() {
      this.$router.replace({
        name: "qnaModify",
        params: { articleno: this.article.articleno },
      });
      //   this.$router.push({ path: `/board/modify/${this.article.articleno}` });
    },
    deleteArticle() {
      if (confirm("정말로 삭제?")) {
        deleteArticle(this.article.articleno, () => {
          this.$router.push({ name: "qnaList" });
        });
      }
    },
    onModifyComment(comment) {
      this.modifyComment = comment;
      console.log("onModifyComment: ", this.modifyComment);
      this.isModifyShow = true;
    },
    onModifyCommentCancel(isShow) {
      this.isModifyShow = isShow;
    },
  },
  // filters: {
  //   dateFormat(regtime) {
  //     return moment(new Date(regtime)).format("YY.MM.DD hh:mm:ss");
  //   },
  // },
};
</script>

<style></style>
