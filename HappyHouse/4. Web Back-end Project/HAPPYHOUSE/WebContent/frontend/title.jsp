<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:choose>
		<c:when test="${empty id}">
			<span><a href="login.jsp">로그인</a></span>
			<span><a href="register.jsp">회원가입</a></span>
		</c:when>
		<c:otherwise>
			<span><a href="logout.do">로그아웃</a></span>
			<span><a href="memberUpdateForm.do">회원정보</a></span>
		</c:otherwise>
	</c:choose>
</body>
</html>