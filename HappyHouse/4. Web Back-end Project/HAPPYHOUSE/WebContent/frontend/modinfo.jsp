<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <link href="css/main.css" rel="stylesheet" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
    </head>
    <body>
        <!-- Navigation-->
        <nav class="navbar navbar-light bg-light static-top">
            <div class="container">
                <a class="navbar-brand" href="index.jsp">해피하우스</a>
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
        <!-- ê°ì´ë° ìë ¥ -->
        <main class="container">
            <div class="login-page">
            	<h1 style='text-align: center;'>회원정보 수정</h1>
                <div class="form">
                  <form name="register-form" class="register-form" method="post" action="updateMember.do">
                    <label for="id">ID</label>
                    <input id="id" name="id" type="text" value="${member.id}" readonly/>

                    <label for="password">PWD</label>
                    <input
                    id="password"
                    name="password"
                    type="text"
                    value="${member.password}"
                    />

                    <label for="name">NAME</label>
                    <input id="name" name="name" type="text" value="${member.name}"/>

                    <label for="email">ADDR</label>
                    <input
                    id="email"
                    name="email"
                    type="text"
                    value="${member.email}"
                    />

                    <label for="phone">PHONE</label>
                    <input id="phone" name="phone" type="tel" value="${member.phone}"/>
                    <script>                            
                        function modify(){
                            alert("회원정보 수정 완료!");
                            window.location.replace("userinfo.jsp");
                        }
                    </script>
                    <input type="submit" value="완료" style='width: 34%; margin-left: 60px; margin-right:5px; background: gray; color: #ffffff;'/>
                    <button onclick="location.href='memberUpdateForm.do'" type="button">돌아가기</button>

                  </form>
                </div>
              </div>
        </main>
        <!-- Footer-->
        <footer class="footer bg-light">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6 h-100 text-center text-lg-start my-auto">
                        <ul class="list-inline mb-2">
                            <li class="list-inline-item"><a href="#!">About</a></li>
                            <li class="list-inline-item">â</li>
                            <li class="list-inline-item"><a href="#!">Contact</a></li>
                            <li class="list-inline-item">â</li>
                            <li class="list-inline-item"><a href="#!">Terms of Use</a></li>
                            <li class="list-inline-item">â</li>
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
        <!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *-->
        <!-- * *                               SB Forms JS                               * *-->
        <!-- * * Activate your form at https://startbootstrap.com/solution/contact-forms * *-->
        <!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *-->
        <script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
    </body>
</html>
