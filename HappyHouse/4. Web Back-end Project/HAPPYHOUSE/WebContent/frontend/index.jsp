<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>해피하우스</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Bootstrap icons-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" type="text/css" />
        <!-- Google fonts-->
        <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="css/styles.css" rel="stylesheet" />
        <link rel="stylesheet" href="css/main.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
    </head>
    <body>
        <!-- Navigation-->
        <nav class="navbar navbar-light bg-light static-top">
            <div class="container">
                <a class="navbar-brand" href="#!">해피하우스</a>
                <div class="navbar-menu">
                    <jsp:include page="title.jsp"/>
                </div>
            </div>
            
        </nav>
        <nav class="navbar bg-secondary static-top">
            <div class="container justify-content-end">
                <ul class="nav">
                    <li class="nav-item">
                        <a class="nav-link" href="#">메뉴 1</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">메뉴 2</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">메뉴 3</a>
                    </li>
                </ul>
            </div>
        </nav>
        <!-- Masthead-->
        <header class="masthead">
            <div class="container position-relative">
                <div class="row justify-content-center">
                    <div class="col-xl-6">
                        <div class="text-center text-white">
                            <!-- Page heading-->
                            <h1 class="mb-5">HappyHouse</h1>
                            <form class="form-subscribe" id="contactForm" data-sb-form-api-token="API_TOKEN">
                                <!-- Email address input-->
                                <div class="row">
                                    <div class="dropdown">
                                        <button type="button" class="btn btn-dark dropdown-toggle" data-bs-toggle="dropdown" id="dd1">
                                            도/광역시
                                        </button>
                                        <ul class="dropdown-menu" id="city">
                                            <li><a class="dropdown-item" href="#" value="서울특별시">서울특별시</a></li>
                                            <li><a class="dropdown-item" href="#" value="경기도">경기도</a></li>
                                            <li><a class="dropdown-item" href="#" value="인천광역시">인천광역시</a></li>
                                        </ul>
                                        <button type="button" class="btn btn-dark dropdown-toggle" data-bs-toggle="dropdown" id="dd2">
                                            시/구/군
                                        </button>
                                        <ul class="dropdown-menu" id="gu">
                                            <li><a class="dropdown-item" href="#" value="마포구">마포구</a></li>
                                            <li><a class="dropdown-item" href="#" value="서대문구">서대문구</a></li>
                                            <li><a class="dropdown-item" href="#" value="용산구">용산구</a></li>
                                        </ul>
                                        <button type="button" class="btn btn-dark dropdown-toggle" data-bs-toggle="dropdown" id="dd3">
                                            동
                                        </button>
                                        <ul class="dropdown-menu" id="dong">
                                            <li><a class="dropdown-item" href="#" value="망원동">망원동</a></li>
                                            <li><a class="dropdown-item" href="#" value="서강동">서강동</a></li>
                                            <li><a class="dropdown-item" href="#" value="합정동">합정동</a></li>
                                        </ul>
                                        <button type="button" class="btn btn-dark dropdown-toggle" data-bs-toggle="dropdown" id="dd4">
                                            종류
                                        </button>
                                        <ul class="dropdown-menu" id="kind">
                                            <li><a class="dropdown-item" href="#" value="아파트">아파트</a></li>
                                            <li><a class="dropdown-item" href="#" value="연립 다세대">연립 다세대</a></li>
                                        </ul>
                                        <button class="btn btn-primary" id="submitButton" type="button" onclick="search()">검색</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <!-- Footer-->
        <footer class="footer bg-light">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6 h-100 text-center text-lg-start my-auto">
                        <ul class="list-inline mb-2">
                            <li class="list-inline-item"><a href="#!">About</a></li>
                            <li class="list-inline-item">⋅</li>
                            <li class="list-inline-item"><a href="#!">Contact</a></li>
                            <li class="list-inline-item">⋅</li>
                            <li class="list-inline-item"><a href="#!">Terms of Use</a></li>
                            <li class="list-inline-item">⋅</li>
                            <li class="list-inline-item"><a href="#!">Privacy Policy</a></li>
                        </ul>
                        <p class="text-muted small mb-4 mb-lg-0">&copy; Your Website 2021. All Rights Reserved.</p>
                    </div>
                </div>
            </div>
        </footer>
        <!-- Bootstrap core JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Core theme JS-->
        <script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
    </body>
</html>
