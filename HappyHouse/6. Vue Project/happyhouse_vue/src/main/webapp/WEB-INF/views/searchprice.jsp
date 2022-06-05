<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}"> </c:set>
<!doctype html>
<html lang="zxx">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>pillloMart</title>
    <link rel="icon" href="img/favicon.png">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <!-- animate CSS -->
    <link rel="stylesheet" href="css/animate.css">
    <!-- owl carousel CSS -->
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="css/all.css">
    <!-- flaticon CSS -->
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/themify-icons.css">
    <!-- font awesome CSS -->
    <link rel="stylesheet" href="css/magnific-popup.css">
    <!-- swiper CSS -->
    <link rel="stylesheet" href="css/slick.css">
    <!-- style CSS -->
    <link rel="stylesheet" href="css/style.css">

    <link rel="stylesheet" href="css/nice-select.css">

    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=066af963f56b72087d9971b98c3e0690&libraries=services"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">
    
    var AptDong='';
    function PrintAD(A, D, Aname, Dname){
   
    	var aptd = `<tr>
			<td>`+A+`</td>
			<td>`+D+`</td>
			</tr>`;	
			
		AptDong = A+D;
		console.log(AptDong);
		geocoder.addressSearch(AptDong, function(result, status) {

		    // 정상적으로 검색이 완료됐으면 
		     if (status === kakao.maps.services.Status.OK) {

		        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

		        // 결과값으로 받은 위치를 마커로 표시합니다
		        var marker = new kakao.maps.Marker({
		            map: map,
		            position: coords
		        });

		        // 인포윈도우로 장소에 대한 설명을 표시합니다
		        var infowindow = new kakao.maps.InfoWindow({
		            content: '<div style="width:150px;text-align:center;padding:6px 0;">'+Aname+'</div>'
		        });
		        infowindow.open(map, marker);

		        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
		        map.setCenter(coords);
		    } 
		}); 
        $('#Printad').empty().append(aptd);
        
        var ServiceKey = 'zqYP6IXJlrCiPsUMoR5CZ3qhqL8wfdAWRc4Va6opFwc0Hl/FolLU+Y1zKF8gxu19WBMSG/mdAPdweqzTdwsvrw==';
        var pageNo = '1';
        var numOfRows = '5000';
        // server에서 넘어온 data
        $.ajax({
            url: 'http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService',
            type: 'GET',
            data: {
                "ServiceKey": ServiceKey,
                "pageNo": pageNo,
                "numOfRows": numOfRows,
            },
            dataType: 'xml',

            
            success: function (response) {
            	  makeHospitalList(response, Dname);
            },
            error: function (xhr, status, msg) {
              console.log('상태값 : ' + status + ' Http에러메시지 : ' + msg);
            },
          });
    }
    function makeHospitalList(data, Dname){
    	
    	var HospitalList = '';
    	Dname = Dname.substring(1);
    	Dname = "("+Dname;
    	console.log(Dname);
        
        var items = $('item', data)
        $.each(items, function(index, item){
        	console.log("하는중");
        	if ($('addr', item).text().indexOf(Dname)>=0){
        	HospitalList+=`
        		<tr>
        		<td>${$('yadmNm', item).text()}</td>
        		<td>${$('addr', item).text()}</td>
        		<td>${$('ratPsblYn', item).text()}</td>
        		<td>${$('pcrPsblYn', item).text()}</td>
        		<td>${$('telno', item).text()}</td>
        		</tr>
        	`;
        	}
        })
        
      	console.log("다돌았음");
        $('#Hospitalinfo').empty().append(HospitalList);
        
        $('tr:first').css('background', 'darkgray').css('color', 'white');
        $('tr:even').css('background', 'lightgray');

    	 
    }
 
    var Dongname = '';
    var Aptname ='';
    function SearchApt(){
    	Aptname = document.getElementById('AptSearch').value;
        var ServiceKey = 'zqYP6IXJlrCiPsUMoR5CZ3qhqL8wfdAWRc4Va6opFwc0Hl/FolLU+Y1zKF8gxu19WBMSG/mdAPdweqzTdwsvrw==';
        var pageNo = '1';
        var numOfRows = '500';
        var LAWD_CD = '11110';			//지역별 코드
        var DEAL_YMD = '202012';		//매매연월
        // server에서 넘어온 data
        $.ajax({
            url: 'http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev',
            type: 'GET',
            data: {
                "ServiceKey": ServiceKey,
                "pageNo": pageNo,
                "numOfRows": numOfRows,
                "LAWD_CD": LAWD_CD,
                "DEAL_YMD": DEAL_YMD
            },
            dataType: 'xml',
            
            
            
            success: function (response) {
              if (Dongname == '')
              	makeList(Aptname, response, 'A');
              else if (Aptname == '')
            	makeList(Dongname, response, 'D');
              else
            	  makeList(Aptname, response, 'AD');
            },
            error: function (xhr, status, msg) {
              console.log('상태값 : ' + status + ' Http에러메시지 : ' + msg);
            },
          });
    }
    function SearchResult(){
        Dongname = document.getElementById('AptDongSearch').value;
        var ServiceKey = 'zqYP6IXJlrCiPsUMoR5CZ3qhqL8wfdAWRc4Va6opFwc0Hl/FolLU+Y1zKF8gxu19WBMSG/mdAPdweqzTdwsvrw==';
        var pageNo = '1';
        var numOfRows = '500';
        var LAWD_CD = '11110';			//지역별 코드
        var DEAL_YMD = '202012';		//매매연월
        // server에서 넘어온 data
        $.ajax({
            url: 'http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev',
            type: 'GET',
            data: {
                "ServiceKey": ServiceKey,
                "pageNo": pageNo,
                "numOfRows": numOfRows,
                "LAWD_CD": LAWD_CD,
                "DEAL_YMD": DEAL_YMD
            },
            dataType: 'xml',
            
            
            
            success: function (response) {
              if (Aptname == '')
              	makeList(Dongname, response, 'D');
              else if (Dongname == '')
            	  makeList(Aptname, response, 'A');
              else
            	  makeList(Dongname, response, 'AD');
            },
            error: function (xhr, status, msg) {
              console.log('상태값 : ' + status + ' Http에러메시지 : ' + msg);
            },
          });
        
    }
      $(document).ready(function () {
        $(function () {
          // server에서 넘어온 data
                var ServiceKey = 'zqYP6IXJlrCiPsUMoR5CZ3qhqL8wfdAWRc4Va6opFwc0Hl/FolLU+Y1zKF8gxu19WBMSG/mdAPdweqzTdwsvrw==';
                var pageNo = '1';
                var numOfRows = '500';
                var LAWD_CD = '11110';			//지역별 코드
                var DEAL_YMD = '202012';		//매매연월
                // server에서 넘어온 data
                $.ajax({
                    url: 'http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev',
                    type: 'GET',
                    data: {
                        "ServiceKey": ServiceKey,
                        "pageNo": pageNo,
                        "numOfRows": numOfRows,
                        "LAWD_CD": LAWD_CD,
                        "DEAL_YMD": DEAL_YMD
                    },
                    dataType: 'xml',
            
            
            
            success: function (response) {
              makeList(' ', response);
            },
            error: function (xhr, status, msg) {
              console.log('상태값 : ' + status + ' Http에러메시지 : ' + msg);
            },
          });
        });
        
      });
      function makeList(AD, data, S) {
          var AptList = ``;
          var SelectList = ``;
          var countD=1;
          if (AD == ' ' || AD == ''){
          $(data)
            .find('item')
            .each(function () {
              AptList += `<tr>
            	<td><a href="javascript:;"><p onclick='PrintAD("${$(this).find('도로명').text()}", "${$(this).find('도로명건물본번호코드').text()}", "${$(this).find('아파트').text()}", "${$(this).find('법정동').text()}")'>${$(this).find('아파트').text()}</p></a></td>
				<td>${$(this).find('거래금액').text()}</td>
				<td>${$(this).find('년').text()+'.'+$(this).find('월').text()+'.'+$(this).find('일').text()}</td>
				<td>${$(this).find('전용면적').text()}</td>
				<td>${$(this).find('법정동').text()}</td>
				</tr>`;						
					
              $('#Aptinfo').empty().append(AptList);
   

            });
      	}else{
      	     $(data)
             .find('item')
             .each(function () {
            	 
//            	console.log(AD.search($(this).find('법정동').text()))
//            	console.log(AD)
//            	console.log($(this).find('법정동').text())
			if (S == 'D'){
				if ($(this).find('법정동').text().indexOf(AD)>=0){
				AptList += `<tr>
	            	<td><a href="javascript:;"><p onclick='PrintAD("${$(this).find('도로명').text()}", "${$(this).find('도로명건물본번호코드').text()}", "${$(this).find('아파트').text()}", "${$(this).find('법정동').text()}")'>${$(this).find('아파트').text()}</p></a></td>
 				<td>${$(this).find('거래금액').text()}</td>
 				<td>${$(this).find('년').text()+'.'+$(this).find('월').text()+'.'+$(this).find('일').text()}</td>
 				<td>${$(this).find('전용면적').text()}</td>
 				<td>${$(this).find('법정동').text()}</td>
 				</tr>`;				
             }
             
             }else if (S == 'A'){
            	 if ($(this).find('아파트').text().indexOf(AD)>=0){
     				AptList += `<tr>
     	            	<td><a href="javascript:;"><p onclick='PrintAD("${$(this).find('도로명').text()}", "${$(this).find('도로명건물본번호코드').text()}", "${$(this).find('아파트').text()}", "${$(this).find('법정동').text()}")'>${$(this).find('아파트').text()}</p></a></td>
      				<td>${$(this).find('거래금액').text()}</td>
      				<td>${$(this).find('년').text()+'.'+$(this).find('월').text()+'.'+$(this).find('일').text()}</td>
      				<td>${$(this).find('전용면적').text()}</td>
      				<td>${$(this).find('법정동').text()}</td>
      				</tr>`;				
                  }
             }else if (S == 'AD'){
            	 if ($(this).find('아파트').text().indexOf(Aptname)>=0 && $(this).find('법정동').text().indexOf(Dongname)>=0){
      				AptList += `<tr>
      	            	<td><a href="javascript:;"><p onclick='PrintAD("${$(this).find('도로명').text()}", "${$(this).find('도로명건물본번호코드').text()}", "${$(this).find('아파트').text()}", "${$(this).find('법정동').text()}")'>${$(this).find('아파트').text()}</p></a></td>
       				<td>${$(this).find('거래금액').text()}</td>
       				<td>${$(this).find('년').text()+'.'+$(this).find('월').text()+'.'+$(this).find('일').text()}</td>
       				<td>${$(this).find('전용면적').text()}</td>
       				<td>${$(this).find('법정동').text()}</td>
       				</tr>`;				
                   }
             }
 				//console.log(SelectList);
 				//console.log(AptList);
               //alert(AptList);
             });
      	     
            $('#Aptinfo').empty().append(AptList);
      	}
          
          $('tr:first').css('background', 'darkgray').css('color', 'white');
          $('tr:even').css('background', 'lightgray');
        }  
    </script>
</head>

<body>
    <!--::header part start::-->
    <header class="main_menu home_menu">
        <div class="container">
            <div class="row align-items-center justify-content-center">
                <div class="col-lg-12">
                    <nav class="navbar navbar-expand-lg navbar-light">
                        <a class="navbar-brand" href="index.html"> <img src="img/logo.png" alt="logo"> </a>
                        <button class="navbar-toggler" type="button" data-toggle="collapse"
                            data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                            <span class="menu_icon"><i class="fas fa-bars"></i></span>
                        </button>

                        <div class="collapse navbar-collapse main-menu-item" id="navbarSupportedContent">
                            <ul class="navbar-nav">
                                <li class="nav-item">
                                    <a class="nav-link" href="index.html">홈</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="about.html">소개</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="notice.html">공지사항</a>
                                </li>
                              
                                <li class="nav-item">
                                    <a class="nav-link" href="confirmation.html">실거래가조회</a>
                                </li>
                               
                                </li>
                            
                                <li class="nav-item">
                                    <a class="nav-link" href="contact.html">관심지역</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" href="sitemap.html">사이트맵</a>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="blog.html" id="navbarDropdown_3"
                                        role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        MY
                                    </a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdown_2">
                                        <a class="dropdown-item" href="login.html"> 
                                            로그인
                                        </a>
                                        <a class="dropdown-item" href="userInfo.html">회원정보</a>
                                        <a class="dropdown-item" href="join.html">회원가입</a>
                 
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="hearer_icon d-flex align-items-center">
                            <a id="search_1" href="javascript:void(0)"><i class="ti-search"></i></a>
                            <a id="logout">
                                <i class="ti-close"></i>
                            </a>
                            <a href="login.html">
                                <i class="ti-user"></i>
                            </a>
                        </div>
                    </nav>
                </div>
            </div>
        </div>
        <div class="search_input" id="search_input_box">
            <div class="container ">
                <form class="d-flex justify-content-between search-inner">
                    <input type="text" class="form-control" id="search_input" placeholder="Search Here" onkeyup='mainSearch()'>
                    <button type="submit" class="btn"></button>
                    <span class="ti-close" id="close_search" title="Close Search"></span>
                </form>
            </div>
        </div>
    </header>
    <!-- Header part end-->

    <!-- breadcrumb part start-->
    <section class="breadcrumb_part">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb_iner">
                        <h2>주택 실거래가 조회</h2>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- breadcrumb part end-->

  <!--================ confirmation part start =================-->
  
  <section class="confirmation_part section_padding">
  
  <div class="container">
  	<div class="col-md-4">
       <div class="product_sidebar">
           <div class="single_sedebar">
                 <form action="#">
                     <input id = "AptDongSearch" type="text" name="#" placeholder="동 검색" onkeyup='SearchResult()'>
                        <i class="ti-search"></i>
                  </form>
            </div>
         </div>
      </div>

	
  	<div class="col-md-4">
       <div class="product_sidebar">
           <div class="single_sedebar">
                 <form action="#">
                     <input id = "AptSearch" type="text" name="#" placeholder="아파트이름 검색" onkeyup='SearchApt()'>
                        <i class="ti-search"></i>
                  </form>
            </div>
         </div>
      </div>
	</div>	

    <div class="container">
      <div class="row">
        <div class="col-lg-6">
          <div class="order_details_iner" style="width:100%; height:500px; overflow:auto">
            <h3>거래 정보</h3>
            <table width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <th>아파트</th>
                <th>거래금액</th>
                <th>거래일</th>
                <th>면적</th>
                <th>법정동</th>
              </tr>
              <tbody id="Aptinfo"></tbody>
            </table>
          </div>
        </div>
        
        <div class="col-lg-6">
    	<div id="map" style="width:100%;height:550px;"></div>
    	</div>
        </div>
        
     <div class="container">
      <div class="row">
        <div class="col-12">
          <div class="order_details_iner" style="width:100%; height:500px; overflow:auto">
            <h3>호흡기 진료 지정 의료기관</h3>
            <table width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <th>병원이름</th>
                <th>주소</th>
                <th>RAT가능여부</th>
                <th>PCR가능여부</th>
                <th>전화번호</th>
              </tr>
              <tbody id="Hospitalinfo"></tbody>
            </table>
          </div>
        </div>     </div>
        </div>

   
    


<script>
var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };  

// 지도를 생성합니다    
var map = new kakao.maps.Map(mapContainer, mapOption); 

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

// 주소로 좌표를 검색합니다
geocoder.addressSearch('', function(result, status) {

    // 정상적으로 검색이 완료됐으면 
     if (status === kakao.maps.services.Status.OK) {

        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({
            map: map,
            position: coords
        });

        // 인포윈도우로 장소에 대한 설명을 표시합니다
        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:6px 0;">우리회사</div>'
        });
        infowindow.open(map, marker);

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
    } 
});    
</script>
    
  </section>

  <!--================ confirmation part end =================-->

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
    <script src="js/mixitup.min.js"></script>
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
    <script src="js/custom.js"></script>
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
