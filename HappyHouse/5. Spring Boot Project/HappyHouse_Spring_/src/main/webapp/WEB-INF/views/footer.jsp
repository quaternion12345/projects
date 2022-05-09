<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
  
 <c:set var="root" value="${pageContext.request.contextPath}"> </c:set>	
    <!-- client review part here -->
    <section class="client_review">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="client_review_slider owl-carousel">
                        <div class="single_client_review">
                            <div class="client_img">
                                <img src="${root}/resources/img/cccon.png" alt="#">
                            </div>
                            <p>해피하우스에 오신것을 환영합니다.</p>
                            <br />
                            <h2>김태하</h2>
                        </div>
                        <div class="single_client_review">
                            <div class="client_img">
                                <img src="${root}/resources/img/cccon.png" alt="#">
                            </div>
                            <p>해피하우스에 오신것을 환영합니다.</p>
                            <br />
                            <h2>임재훈</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- client review part end -->


    <!-- feature part here -->
    <!-- feature part end -->

    <!--::footer_part start::-->
    <footer class="footer_part">
        <div class="footer_iner">
            <div class="container">
                <div class="row justify-content-between align-items-center">
                    <div class="col-lg-6">
                        <div class="footer_menu">
                            <div class="footer_logo">
                                <a href="index.html"><img src="${root}/resources/img/logo.png" height =100px alt="#"></a>
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
    <script src="${root}/resources/js/jquery-1.12.1.min.js"></script>
    <!-- popper js -->
    <script src="${root}/resources/js/popper.min.js"></script>
    <!-- bootstrap js -->
    <script src="${root}/resources/js/bootstrap.min.js"></script>
    <!-- magnific popup js -->
    <script src="${root}/resources/js/jquery.magnific-popup.js"></script>
    <!-- carousel js -->
    <script src="${root}/resources/js/owl.carousel.min.js"></script>
    <script src="${root}/resources/js/jquery.nice-select.min.js"></script>
    <!-- slick js -->
    <script src="${root}/resources/js/slick.min.js"></script>
    <script src="${root}/resources/js/jquery.counterup.min.js"></script>
    <script src="${root}/resources/js/waypoints.min.js"></script>
    <script src="${root}/resources/js/contact.js"></script>
    <script src="${root}/resources/js/jquery.ajaxchimp.min.js"></script>
    <script src="${root}/resources/js/jquery.form.js"></script>
    <script src="${root}/resources/js/jquery.validate.min.js"></script>
    <script src="${root}/resources/js/mail-script.js"></script>
    <!-- custom js -->
    <script src="${root}/resources/js/custom.js"></script>

