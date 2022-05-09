<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

<%@include file="header.jsp" %>

<section class="breadcrumb_part">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <div class="breadcrumb_iner">
              <h2>회원가입</h2>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- breadcrumb part end-->

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
                    환영합니다! <br />
                    회원가입 화면입니다.
                  </h3>
                </div>

                <form
                  class="row contact_form"
                  action="member"
                  method="post"
                  id="joinForm"
                >
                <input type="hidden" name="act" value="register" />
                  <div class="col-md-12 form-group p_star">
                    <label for="id">아이디</label>
                    <input
                      type="text"
                      class="form-control"
                      id="id"
                      name="id"
                      placeholder="소문자와 숫자를 조합하여 3자 이상의 아이디를 입력해주세요"
                      required
                    />
                    <div class="check_font" id="id_check"></div>
                  </div>
                  <div class="col-md-12 form-group p_star">
                    <label for="password">비밀번호</label>
                    <input
                      type="password"
                      class="form-control"
                      id="password"
                      name="password"
                      placeholder="PASSWORD"
                      required
                    />
                    <div class="check_font" id="pw_check"></div>
                  </div>
                  <div class="col-md-12 form-group p_star">
                    <label for="user_pw2">비밀번호 확인</label>
                    <input
                      type="password"
                      class="form-control"
                      id="user_pw2"
                      name="user_pw2"
                      placeholder="Confirm Password"
                      required
                    />
                    <div class="check_font" id="pw2_check"></div>
                  </div>
                  <div class="col-md-12 form-group p_star">
                    <label for="user_name">이름</label>
                    <input
                      type="text"
                      class="form-control"
                      id="name"
                      name="name"
                      placeholder="Name"
                      required
                    />
                    <div class="check_font" id="name_check"></div>
                  </div>
                                    <div class="col-md-12 form-group p_star">
                    <label for="user_birth">이메일</label>
                    <input
                      type="email"
                      class="form-control"
                      id="email"
                      name="email"
                      placeholder="ex)aaa@aaaa.com"
                      required
                    />
                    <div class="check_font" id="birth_check"></div>
                  </div>
                  <div class="col-md-12 form-group p_star">
                    <label for="address">주소</label>
                    <input
                      type="text"
                      class="form-control"
                      id="address"
                      name="address"
                      placeholder="ex) 00시 00구 00동"
                      required
                    />
                    <div class="check_font" id="birth_check"></div>
                  </div>
                  <div class="col-md-12 form-group p_star">
                    <label for="phone">휴대폰번호</label>
                    <input
                      type="text"
                      class="form-control"
                      id="phone"
                      name="phone"
                      placeholder="-빼고 숫자만 입력 ex)000000000"
                      required
                    />
                    <div class="check_font" id="phone_check"></div>
                  </div>
                  <div class="col-md-12 form-group">
                    <button type="button"  class="btn_3"  id="joinBtn">
                      회원가입
                    </button>

                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!--================login_part end =================-->
<%@include file="footer.jsp" %>

        <script type="text/javascript">
        $(document).ready(function () {
            $("#joinBtn").click(function () {
                if (!$("#id").val()) {
                    alert("아이디 입력!!!");
                    return;
                } else if (!$("#password").val()) {
                    alert("비밀번호 입력!!!");
                    return; 
                } else if (!$("#user_pw2").val()) {
                    alert("비밀번호2 입력!!!");
                    return; 
                }else if (!$("#name").val()) {
                        alert("이름 입력!!!");
                        return;
                }else if (!$("#email").val()) {
                    alert("이메일 입력!!!");
                    return;
         	   } else if (!$("#address").val()) {
                   alert("주소  입력!!!");
                   return;
        	   } else if (!$("#phone").val()) {
                   alert("휴대폰번호 입력!!!");
                   return;
        	   }  else {
                    $("#joinForm").attr("action", "${pageContext.request.contextPath}/member").submit();
                }
            });
        });
    </script>
    <script>
      $(function () {
        //모든 공백 체크 정규식
        var empJ = /\s/g;
        //아이디 정규식
        var idJ = /^[a-z0-9]{4,12}$/;
        // 비밀번호 정규식
        var pwJ = /^[A-Za-z0-9]{4,12}$/;
        // 이름 정규식
        var nameJ = /^[가-힣]{2,6}$/;
        // 이메일 검사 정규식
        var mailJ =
          /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        // 휴대폰 번호 정규식
        var phoneJ = /^01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})$/;

        //아이디 중복성검사
        $("#id").blur(function () {
          if (idJ.test($(this).val())) {
            console.log(idJ.test($(this).val()));
            $("#id_check").text("");
          } else {
            $("#id_check").text("소문자와 숫자만 가능합니다. 아이디를 확인해주세요");
            $("#id_check").css("color", "red");
          }
        });

        $("#user_name").blur(function () {
          if (nameJ.test($(this).val())) {
            console.log(nameJ.test($(this).val()));
            $("#name_check").text("");
          } else {
            $("#name_check").text("이름을 확인해주세요");
            $("#name_check").css("color", "red");
          }
        });

        $("#phone").blur(function () {
          if (phoneJ.test($(this).val())) {
            console.log(nameJ.test($(this).val()));
            $("#phone_check").text("");
          } else {
            $("#phone_check").text("휴대폰번호를 확인해주세요");
            $("#phone_check").css("color", "red");
          }
        });
        //비밀번호 유효성 검사
        $("#password").blur(function () {
          if (pwJ.test($("#user_pw").val())) {
            console.log(true);
            $("pw_check").text("");
          } else {
            console.log(false);
            $("#pw_check").text(
              "공백 없이 숫자 or 문자로만 4-12자리 입력해주세요"
            );
            $("#pw_check").css("color", "red");
          }
        });
        //비밀번호2 유효성검사
        $("#user_pw2").blur(function () {
          if ($('#password').val()!=$(this).val()) {
            $("#pw2_check").text(
              "비밀번호가 일치하지 않습니다."
            );
            $("#pw2_check").css("color", "red");
          } else {
            $("pw2_check").text("");
           
          }
        });
        //비밀번호2 유효성 검사

      });
    </script>

  </body>
</html>
