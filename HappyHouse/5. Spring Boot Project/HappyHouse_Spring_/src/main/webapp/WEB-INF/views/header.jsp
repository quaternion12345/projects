<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
  
 <c:set var="root" value="${pageContext.request.contextPath}"> </c:set>
   <!DOCTYPE html>
<html>
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>6조 해피하우스</title>
    <link rel="icon" href="${root}/resources/img/favicon.png" />
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${root}/resources/css/bootstrap.min.css" />
    <!-- animate CSS -->
    <link rel="stylesheet" href="${root}/resources/css/animate.css" />
    <!-- owl carousel CSS -->
    <link rel="stylesheet" href="${root}/resources/css/owl.carousel.min.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="${root}/resources/css/all.css" />
    <!-- flaticon CSS -->
    <link rel="stylesheet" href="${root}/resources/css/flaticon.css" />
    <link rel="stylesheet" href="${root}/resources/css/themify-icons.css" />
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="${root}/resources/css/magnific-popup.css" />
    <!-- swiper CSS -->
    <link rel="stylesheet" href="${root}/resources/css/slick.css" />
    <!-- style CSS -->
    <link rel="stylesheet" href="${root}/resources/css/style.css" />
    <link rel="stylesheet" href="${root}/resources/css/mine.css" />
        <script type="text/javascript" src="http://dapi.kakao.com/v2/maps/sdk.js?appkey=066af963f56b72087d9971b98c3e0690&libraries=services"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body> 
    <header class="main_menu home_menu">
        <div class="container">
            <div class="row align-items-center justify-content-center">
                <div class="col-lg-12">
                    <nav class="navbar navbar-expand-lg navbar-light">
                        <a class="navbar-brand" href="${root}/index"> <img src="${root}/resources/img/logo.png" alt="logo"> </a>
                        <button class="navbar-toggler" type="button" data-toggle="collapse"
                            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                            <span class="menu_icon"><i class="fas fa-bars"></i></span>
                        </button>

                        <div class="collapse navbar-collapse main-menu-item" id="navbarSupportedContent">
                            <ul class="navbar-nav">
                                <li class="nav-item">
                                    <a class="nav-link" href="${root}/index">홈</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${root}/about">소개</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${root}/notice?action=list&pg=1&key=&word=">공지사항</a>
                                </li>
                              
                                <li class="nav-item">
                                    <a class="nav-link" href="confirmation.html">실거래가조회</a>
                                </li>
                                                           
                                <li class="nav-item">
                                    <a class="nav-link" href="${root}/contact">관심지역</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${root}/sitemap">사이트맵</a>
                                </li>
                                
 <c:choose>
 			<c:when test="${!empty memberInfo}">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="blog.jsp" id="navbarDropdown_3"
                                        role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        ${memberInfo.name}
                                    </a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdown_2">
                                        <a class="dropdown-item" href="${root}/member/logout" > 
                                            로그아웃
                                        </a>
                                        <a class="dropdown-item" href="${root}/member/${memberInfo.id}">마이페이지</a>
                 
                                    </div>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href=""></a>
                                </li>
             </c:when>
             <c:otherwise>
           					    <li class="nav-item">
                                    <a class="nav-link" href="${root}/member/loginForm">로그인</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="${root}/member/registerForm">회원가입</a>
                                </li>
             
             </c:otherwise>                  
 </c:choose>
                            </ul>
                        </div>
<!--                         <div class="hearer_icon d-flex align-items-center">
                            <a id="search_1" href="javascript:void(0)"><i class="ti-search"></i></a>
                            <a id="logout">
                                <i class="ti-close"></i>
                            </a>
                            <a href="login.jsp">
                                <i class="ti-user"></i>
                            </a>
                        </div> -->
                    </nav>
                </div>
            </div>
        </div>

    </header>
    <!-- Header part end-->