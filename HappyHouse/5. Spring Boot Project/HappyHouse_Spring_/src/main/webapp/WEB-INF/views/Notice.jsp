<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<%@include file="header.jsp" %>
    

    <!-- breadcrumb part start-->
    <section class="breadcrumb_part">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <div class="breadcrumb_iner">
              <h2>공지사항</h2>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- breadcrumb part end-->

    <!-- feature part here -->
    <section class="feature_part section_padding">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-xl-12">
             <c:if test="${!empty articles}">
              <table class="table">
              <thead>
                <tr>
                  <th scope="col">번호</th>
                  <th scope="col">제목</th>
                  <th scope="col">내용</th>
                  <th scope="col">작성자</th>
                  <th scope="col"></th>
                </tr>
              </thead> 
			<c:forEach var="article" items="${articles}">
	              <tbody id="tableBody"></tbody>
	                    <tr>
	                          <td>${article.no}</td>
	                          <td>${article.title}</td>
                              <td>${article.contents}</td>
                              <td>${article.author}</td>
                              <td>
                              		<a href="${root}/notice?action=modify&no=${article.no}">수정</a>
                           			<a href="${root}/notice?action=delete&no=${article.no}">삭제</a>
                              </td>
                        </tr>
	              </tbody>
			</c:forEach>
           	</table>
		</c:if> 
		<c:if test="${empty articles}">    
			<table class="table table-active text-left">
                    <tr class="table-info">
                        <td colspan="2">작성한 글이 없습니다.</td>
                    </tr>
            </table>
		</c:if>     
          </div>
          <div>
            <nav aria-label="...">
              <ul class="pagination">
                <li class="page-item disabled">
                  <a class="page-link" href="#" tabindex="-1">Previous</a>
                </li>
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item active">
                  <a class="page-link" href="#"
                    >2 <span class="sr-only">(current)</span></a
                  >
                </li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item">
                  <a class="page-link" href="#">Next</a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
        <a href="Write.jsp" id="writeButton" class="genric-btn default2 radius"
          >글쓰기</a
        >
      </div>
    </section>
    <!-- feature part end -->

    <!-- client review part here -->
    <section class="client_review">
      <div class="container">
          <div class="row justify-content-center">
              <div class="col-lg-8">
                  <div class="client_review_slider owl-carousel">
                      <div class="single_client_review">
                          <div class="client_img">
                              <img src="img/cccon.png" alt="#">
                          </div>
                          <p>해피하우스에 오신것을 환영합니다.</p>
                          <br />
                          <h2>홍인호</h2>
                      </div>
                      <div class="single_client_review">
                          <div class="client_img">
                              <img src="img/cccon.png" alt="#">
                          </div>
                          <p>해피하우스에 오신것을 환영합니다.</p>
                          <br />
                          <h2>김유경</h2>
                      </div>
                  </div>
              </div>
          </div>
      </div>
  </section>
    <!-- client review part end -->

    <!--::footer_part start::-->
    <footer class="footer_part">
        <div class="footer_iner">
            <div class="container">
                <div class="row justify-content-between align-items-center">
                    <div class="col-lg-6">
                        <div class="footer_menu">
                            <div class="footer_logo">
                                <a href="index.html"><img src="img/logo.png" height =100px alt="#"></a>
                            </div>
                        </div>
                    </div>
                    <div class="col-8-12">
                            <div class="footer_menu_item">
                                <a href="index.html">홈</a>
                                <a href="about.html">소개</a>
                                <a href="confirmation.html">실거래가조회</a>
                                <a href="notice.html">공지사항</a>
                            </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="copyright_part">
            <div class="container">
                <div class="row ">
                    <div class="col-lg-12">

                    </div>
                </div>
            </div>
        </div>
    </footer>
    <!--::footer_part end::-->

    <!-- jquery plugins here-->
    <script src="js/jquery-1.12.1.min.js"></script>
    <!-- popper js -->
    <script src="js/popper.min.js"></script>
    <!-- bootstrap js -->
    <script src="js/bootstrap.min.js"></script>
    <!-- easing js -->
    <script src="js/jquery.magnific-popup.js"></script>
    <!-- swiper js -->
    <script src="js/swiper.min.js"></script>
    <!-- swiper js -->
    <script src=/js/mixitup.min.js"></script>
    <!-- particles js -->
    <script src="js/owl.carousel.min.js"></script>
    <script src="js/jquery.nice-select.min.js"></script>
    <!-- slick js -->
    <script src="js/slick.min.js"></script>
    <script src="js/jquery.counterup.min.js"></script>
    <script src="js/waypoints.min.js"></script>
    <script src="js/contact.js"></script>
    <script src="js/jquery.ajaxchimp.min.js"></script>
    <script src="js/jquery.form.js"></script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/mail-script.js"></script>
    <!-- custom js -->
    <script src="../js/custom.js"></script>

    <script>
      $(function () {
        let lists;
        
        $("body").on("click", "button[class=deleteButton]", function () {
          var temp=$(this).val();
          console.log(temp);
          $(this).parent().parent().remove();
          lists = JSON.parse(localStorage.getItem("con"));
          console.log(lists);
        lists.splice(temp,1);
        console.log(lists);
        localStorage.setItem("con", JSON.stringify(lists));

        });

        lists = JSON.parse(localStorage.getItem("con"));
        console.log(lists);
        let Scontent;
        if (lists) {
          for (let index = 0; index < lists.length; index++) {
            Scontent += `<tr>
                      <td>${index}</td>
                          <td>${lists[index].title}</td>
                              <td>${lists[index].content}</td>
                              <td>${lists[index].name}</td>
                              <td><button type='button' value=${index} class='deleteButton'>삭제</button></td>
                              </tr>`;
          }

          $("#tableBody").append(Scontent);
        }
      });
    </script>
        <script>
          $(function(){
              $('#logout').on('click',function(){
                  console.log("로그아웃 완료");
                  alert("로그아웃 완료");
  
              })
          })
      </script>
  </body>
</html>
