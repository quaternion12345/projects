<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

    <!--::header part start::-->

	<%@include file="header.jsp" %>
    <!-- banner part start-->
    <section class="banner_part">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-5">
                    <div class="banner_text">
                        <div class="banner_text_iner">
                            <h1>행복한
                                우리집</h1>
                            <p>실거래가 조회와 관심정보 환경정보를 한눈에 파악하세요</p>
                            <p>실거래가 조회와 관심정보 환경정보를 한눈에 파악하세요</p>
                            <p>실거래가 조회와 관심정보 환경정보를 한눈에 파악하세요</p>
                            <p>실거래가 조회와 관심정보 환경정보를 한눈에 파악하세요</p>
                            <a href="${pageContext.request.contextPath}/about" class="btn_1">자세히 보기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="banner_img">
            <img src="img/banner.png" style="width:80%"alt="#" class="img-fluid">
            <img src="img/banner_pattern.png " alt="#" class="pattern_img img-fluid">
        </div>
    </section>
    <!-- banner part start-->



    <!-- trending item start-->
    <section class="trending_items">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section_tittle text-center">
                        <h2>인기매물</h2>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4 col-sm-6">
                    <div class="single_product_item">
                        <div class="single_product_item_thumb">
                            <img src="img/apt/무악동현대.jpg" alt="#" class="img-fluid" width=400px>
                        </div>
                        <h3> <a href="single-product.html">무악동현대</a> </h3>
                        <p>84,960</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="single_product_item">
                        <img src="img/apt/경희궁의아침.jpg" alt="#" class="img-fluid" width=400px>
                        <h3> <a href="single-product.html">경희궁의아침</a> </h3>
                        <p>160,000</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="single_product_item">
                        <img src="img/apt/광화문풍림스페이스본.jpg"  alt="#" class="img-fluid" width=400px>
                        <h3> <a href="single-product.html">광화문풍림스페이스본</a> </h3>
                        <p>125,000</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="single_product_item">
                        <img src="img/apt/교북동경희궁자이(4단지).jpg" alt="#" class="img-fluid" width=400px>
                        <h3> <a href="single-product.html">교북동경희궁자이(4단지)</a> </h3>
                        <p>98,000</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="single_product_item">
                        <img src="img/apt/동성아파트.jpg" alt="#" class="img-fluid" width=400px>
                        <h3> <a href="single-product.html">동성아파트</a> </h3>
                        <p>25,000</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="single_product_item">
                        <img src="img/apt/무악동인왕산아이파크.jpg" alt="#" class="img-fluid" width=400px>
                        <h3> <a href="single-product.html">무악동인왕산아이파크</a> </h3>
                        <p>130,000</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- trending item end-->
    
    	<%@include file="footer.jsp" %>
