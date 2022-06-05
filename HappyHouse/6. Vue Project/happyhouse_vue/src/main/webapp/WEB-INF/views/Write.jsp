<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <c:set var="root" value="${pageContext.request.contextPath}"> </c:set>
<!DOCTYPE html>
<html lang="zxx">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>3조 해피하우스</title>
    <link rel="icon" href="img/favicon.png" />
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <!-- animate CSS -->
    <link rel="stylesheet" href="css/animate.css" />
    <!-- owl carousel CSS -->
    <link rel="stylesheet" href="css/owl.carousel.min.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="css/all.css" />
    <!-- flaticon CSS -->
    <link rel="stylesheet" href="css/flaticon.css" />
    <link rel="stylesheet" href="css/themify-icons.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="css/magnific-popup.css" />
    <!-- swiper CSS -->
    <link rel="stylesheet" href="css/slick.css" />
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style.css" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  </head>

  <body>
    <!--================login_part Area =================-->
    <section class="login_part section_padding">
      <div class="container">
        <div class="row align-items-center">
          <div
            class="col-lg-8 col-md-8 col-sm-8 container justify-content-center"
          >
            <div class="login_part_form">
              <div class="login_part_form_iner">
                <div class="text-center">
                  <h3>
                    	글쓰기
                  </h3>
                </div>

                <form
                  id="writeform"
                  class="row contact_form"
                  action="${root }/notice"
                  method="post"
                  novalidate="novalidate"
                >
                  <input type="hidden" name="action" value="register">
                  <div class="col-md-12 form-group p_star">
                    <label>작성자 이름</label>
                    <input
                      type="text"
                      class="form-control"
                      id="author"
                      name="author"
                     
                      required
                    />
                   
                  </div>
                  
                  <div class="col-md-12 form-group p_star">
                    <label>제목</label>
                      <input
                        type="제목"
                        class="form-control"
                        id="title"
                        name="title"
                        
                        required
                      />
                    
                   
                  </div>
                  <div class="col-md-12 form-group p_star">
                    <label>내용</label>
                      <textarea class="form-control" id="contents" name="contents"></textarea>
                  </div>
                  <div class="col-md-12 form-group">
                    <input type="submit" value="완료"/>

                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!--================login_part end =================-->
<script>
$(function(){

    $('#inputButton').on('click',function(){

      var wobject={};

        var wName=$('#author').val();
        var wTitle=$('#title').val();
        var wContent=$('#contents').val();

        console.log(wName);
        
        if (!wName||!wTitle||!wContent) {
            alert("빠짐없이 입력해주세요");
            return;
        } else
          if (confirm("작성을 완료하시겠습니까?")) {
        	$("#wirteform").attr("action", "${root}/notice").submit();
            opener.location.href = "Notice.jsp?action=list"
            self.close();
          }
    })
})


</script>


  </body>
</html>
