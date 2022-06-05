"use strict";(self["webpackChunkvue_board_bootstrap"]=self["webpackChunkvue_board_bootstrap"]||[]).push([[805],{68689:function(t,e,n){n.r(e),n.d(e,{default:function(){return S}});var o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("b-container",{staticClass:"bv-example-row mt-3"},[n("b-row",[n("b-col",[n("b-alert",{attrs:{show:""}},[n("h3",[t._v("글보기")])])],1)],1),n("b-row",{staticClass:"mb-1"},[n("b-col",{staticClass:"text-left"},[n("b-button",{attrs:{variant:"outline-primary"},on:{click:t.listArticle}},[t._v("목록")])],1),n("b-col",{staticClass:"text-right"},[n("b-button",{staticClass:"mr-2",attrs:{variant:"outline-info",size:"sm"},on:{click:t.moveModifyArticle}},[t._v("글수정")]),n("b-button",{attrs:{variant:"outline-danger",size:"sm"},on:{click:t.deleteArticle}},[t._v("글삭제")])],1)],1),n("b-row",{staticClass:"mb-1"},[n("b-col",[n("b-card",{staticClass:"mb-2",attrs:{"header-html":"<h3>"+t.article.articleno+".\n          "+t.article.subject+" ["+t.article.hit+"]</h3><div><h6>"+t.article.userid+"</div><div>"+t.article.regtime+"</h6></div>","border-variant":"dark","no-body":""}},[n("b-card-body",{staticClass:"text-left"},[n("div",{domProps:{innerHTML:t._s(t.message)}})])],1)],1)],1)],1),n("comment-write",{attrs:{articleno:""+t.article.articleno}}),t.isModifyShow&&null!=this.modifyComment?n("comment-write",{attrs:{modifyComment:this.modifyComment},on:{"modify-comment-cancel":t.onModifyCommentCancel}}):t._e(),t._l(t.comments,(function(e,o){return n("comment-view",{key:o,attrs:{comment:e},on:{"m-comment":t.onModifyComment}})}))],2)},i=[];n(47941),n(82526),n(57327),n(41539),n(38880),n(54747),n(49337);function c(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function r(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(t);e&&(o=o.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,o)}return n}function m(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?r(Object(n),!0).forEach((function(e){c(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}n(69600),n(74916),n(23123),n(15306);var a=n(75748),s=n(34665),l=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"regist"},[null!=this.modifyComment?n("div",{staticClass:"regist_form"},[n("textarea",{directives:[{name:"model",rawName:"v-model",value:t.modicomment,expression:"modicomment"}],attrs:{id:"modicomment",name:"modicomment",cols:"35",rows:"2"},domProps:{value:t.modicomment},on:{input:function(e){e.target.composing||(t.modicomment=e.target.value)}}}),n("button",{staticClass:"small",on:{click:t.updateCommentCancel}},[t._v("취소")]),n("button",{staticClass:"small",on:{click:t.updateComment}},[t._v("수정")])]):n("div",{staticClass:"regist_form"},[n("textarea",{directives:[{name:"model",rawName:"v-model",value:t.content,expression:"content"}],attrs:{id:"comment",name:"comment",cols:"35",rows:"2"},domProps:{value:t.content},on:{input:function(e){e.target.composing||(t.content=e.target.value)}}}),n("button",{staticClass:"btn btn-primary",staticStyle:{margin:"30px 15px 0px 0px"},on:{click:t.registComment}},[t._v(" 등록 ")])])])},d=[],u={name:"comment-write",data:function(){return{userid:"SSAFY",content:"",modicomment:""}},props:{articleno:{type:String},modifyComment:{type:Object}},created:function(){this.modicomment=this.modifyComment.content},methods:{registComment:function(){var t=this;a.Z.post("/comment/",{userid:"SSAFY",content:this.content,articleno:this.articleno}).then((function(e){var n=e.data,o="등록 처리시 문제가 발생했습니다.";"success"===n&&(o="등록이 완료되었습니다."),alert(o),t.content="",t.$store.dispatch("getComments","/comment/article/".concat(t.articleno))}))},updateComment:function(){var t=this;a.Z.put("/comment/".concat(this.articleno),{no:this.modifyComment.no,content:this.modicomment}).then((function(e){var n=e.data,o="수정 처리시 문제가 발생했습니다.";"success"===n&&(o="수정이 완료되었습니다."),alert(o),t.$store.dispatch("getComments","/comment/article/".concat(t.modifyComment.articleno)),t.$emit("modify-comment-cancel",!1)}))},updateCommentCancel:function(){this.$emit("modify-comment-cancel",!1)}}},f=u,h=n(43736),p=(0,h.Z)(f,l,d,!1,null,"2f4a0daa",null),b=p.exports,v=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{directives:[{name:"show",rawName:"v-show",value:t.isShow,expression:"isShow"}],staticClass:"comment"},[n("div",{staticClass:"head"},[t._v(t._s(t.comment.userid)+" ("+t._s(t.comment.regtime)+")")]),n("div",{staticClass:"content",domProps:{innerHTML:t._s(t.enterToBr(t.comment.content))}}),n("div",{staticClass:"cbtn"},[n("label",{on:{click:t.modifyCommentView}},[t._v("수정")]),t._v(" | "),n("label",{on:{click:t.deleteComment}},[t._v("삭제")])])])},C=[],y={name:"comment-view",data:function(){return{isShow:!0}},props:{comment:Object},beforeMount:function(){this.$store.dispatch("getComments","/comment/article/".concat(this.comment.articleno))},methods:{modifyCommentView:function(){this.$emit("m-comment",{no:this.comment.no,content:this.comment.content,articleno:this.comment.articleno})},deleteComment:function(){var t=this;confirm("정말로 삭제?")&&a.Z["delete"]("/comment/".concat(this.comment.no)).then((function(e){var n=e.data,o="삭제 처리시 문제가 발생했습니다.";"success"===n&&(o="삭제가 완료되었습니다."),alert(o),t.$store.dispatch("getComments","/comment/article/".concat(t.comment.articleno))}))},enterToBr:function(t){if(t)return t.replace(/(?:\r\n|\r|\n)/g,"<br />")}}},w=y,g=(0,h.Z)(w,v,C,!1,null,null,null),_=g.exports,O={name:"BoardDetail",data:function(){return{article:{},isModifyShow:!1,modifyComment:""}},computed:m(m({},(0,s.Se)(["comments"])),{},{message:function(){return this.article.content?this.article.content.split("\n").join("<br>"):""}}),components:{CommentWrite:b,CommentView:_},created:function(){var t=this;a.Z.get("/board/".concat(this.$route.params.articleno)).then((function(e){var n=e.data;t.article=n}))},beforeMount:function(){this.$store.dispatch("getComments","/comment/article/".concat(this.$route.params.articleno))},methods:{listArticle:function(){this.$router.push({name:"boardList"})},moveModifyArticle:function(){this.$router.replace({name:"boardModify",params:{articleno:this.article.articleno}})},deleteArticle:function(){confirm("정말로 삭제?")&&this.$router.replace({name:"boardDelete",params:{articleno:this.article.articleno}})},onModifyComment:function(t){this.modifyComment=t,this.isModifyShow=!0},onModifyCommentCancel:function(t){this.isModifyShow=t}}},j=O,x=(0,h.Z)(j,o,i,!1,null,null,null),S=x.exports}}]);
//# sourceMappingURL=805-legacy.ec056a88.js.map