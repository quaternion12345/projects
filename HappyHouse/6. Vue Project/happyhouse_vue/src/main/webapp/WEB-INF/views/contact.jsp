<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

	
<%@include file="header.jsp" %>

    <!-- breadcrumb part start-->
    <section class="breadcrumb_part">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <div class="breadcrumb_iner">
              <h2>관심지역 상권정보 및 환경정보(교통)</h2>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- breadcrumb part end-->
    <div class="container text-center" style="margin-top: 10px;">
     
      <h1><a href="airInfo.html">관심지역 대기정보 보러가기</a></h1>
    
    </div>
    <!-- ================ contact section start ================= -->
    <section class="section_padding">
     
      <div class="container årow justify-content-center">
        
        <div class="col-xl-12">
          <table class="table">
            <h3>관심지역 리스트</h3>
            <p>리스트를 클릭하면 해당 지역으로 이동합니다.</p>
            <thead>
              <tr>
                <th scope="col">시</th>
                <th scope="col">군/구</th>
                <th scope="col">읍/면/동</th>
                <th scope="col">아파트이름</th>
              </tr>
              <tr onclick="a($(this))">
                <td scope="si">서울</td>
                <td scope="gungu">송파구</td>
                <td scope="dong">가락동</td>
                <td scope="APT">헬리오시티 아파트</td>
              </tr>
              <tr onclick="a($(this))">
                <td scope="si">서울</td>
                <td scope="gungu">송파구</td>
                <td scope="dong">장지동</td>
                <td scope="APT">파크하비오 아파트</td>
              </tr>
              <script>
                function a(aaaaa) {
                  var SelectList = ``;
                  $(aaaaa)
                    .find("td")
                    .each(function () {
                      SelectList = SelectList + $(this).text() + " ";
                    });
                  console.log(SelectList);

                  // 장소 검색 객체를 생성합니다
                  var ps = new kakao.maps.services.Places();

                  // 키워드로 장소를 검색합니다
                  ps.keywordSearch(SelectList, placesSearchCB);

                  // 키워드 검색 완료 시 호출되는 콜백함수 입니다
                  function placesSearchCB(data, status, pagination) {
                    if (status === kakao.maps.services.Status.OK) {
                      // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                      // LatLngBounds 객체에 좌표를 추가합니다
                      var bounds = new kakao.maps.LatLngBounds();
                      
                      for (var i = 0; i < data.length; i++) {
                        displayMarker(data[i]);
                        bounds.extend(
                          new kakao.maps.LatLng(data[i].y, data[i].x)
                        );
                      }

                      // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                      map.setBounds(bounds);
                    }
                  }
                  // 지도에 마커를 표시하는 함수입니다
function displayMarker(place) {
    
    // 마커를 생성하고 지도에 표시합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(place.y, place.x) 
    });

    // 마커에 클릭이벤트를 등록합니다
    kakao.maps.event.addListener(marker, 'click', function() {
        // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
        infowindow.open(map, marker);
    });
}

                }
              </script>
            </thead>
            <tbody id="tableBody"></tbody>
          </table>
        </div>
      </div>
      <div class="container">
        <div class="map_wrap">
          <div
            id="map"
            style="
              width: 100%;
              height: 100%;
              position: relative;
              overflow: hidden;
            "
          ></div>
          <ul id="category">
            <li id="BK9" data-order="0">
              <span class="category_bg bank"></span>
              은행
            </li>
            <li id="MT1" data-order="1">
              <span class="category_bg mart"></span>
              마트
            </li>
            <li id="PM9" data-order="2">
              <span class="category_bg pharmacy"></span>
              약국
            </li>
            <li id="OL7" data-order="3">
              <span class="category_bg oil"></span>
              주유소
            </li>
            <li id="CE7" data-order="4">
              <span class="category_bg cafe"></span>
              카페
            </li>
            <li id="CS2" data-order="5">
              <span class="category_bg store"></span>
              편의점
            </li>
          </ul>
        </div>
        <script
          type="text/javascript"
          src="//dapi.kakao.com/v2/maps/sdk.js?appkey=3f3b1d83ead55bfa5bc25c76e3210799&libraries=services"
        ></script>
        <script>
          // 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
          var placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 }),
            contentNode = document.createElement("div"), // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다
            markers = [], // 마커를 담을 배열입니다
            currCategory = ""; // 현재 선택된 카테고리를 가지고 있을 변수입니다

          var mapContainer = document.getElementById("map"), // 지도를 표시할 div
            mapOption = {
              center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
              level: 5, // 지도의 확대 레벨
            };

          // 지도를 생성합니다
          var map = new kakao.maps.Map(mapContainer, mapOption);
          map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);

          var geocoder = new kakao.maps.services.Geocoder();

          // 장소 검색 객체를 생성합니다
          var ps = new kakao.maps.services.Places(map);

          // 지도에 idle 이벤트를 등록합니다
          kakao.maps.event.addListener(map, "idle", searchPlaces);

          // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
          contentNode.className = "placeinfo_wrap";

          // 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
          // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
          addEventHandle(contentNode, "mousedown", kakao.maps.event.preventMap);
          addEventHandle(
            contentNode,
            "touchstart",
            kakao.maps.event.preventMap
          );

          // 커스텀 오버레이 컨텐츠를 설정합니다
          placeOverlay.setContent(contentNode);

          // 각 카테고리에 클릭 이벤트를 등록합니다
          addCategoryClickEvent();

          // 엘리먼트에 이벤트 핸들러를 등록하는 함수입니다
          function addEventHandle(target, type, callback) {
            if (target.addEventListener) {
              target.addEventListener(type, callback);
            } else {
              target.attachEvent("on" + type, callback);
            }
          }

          // 카테고리 검색을 요청하는 함수입니다
          function searchPlaces() {
            if (!currCategory) {
              return;
            }

            // 커스텀 오버레이를 숨깁니다
            placeOverlay.setMap(null);

            // 지도에 표시되고 있는 마커를 제거합니다
            removeMarker();

            ps.categorySearch(currCategory, placesSearchCB, {
              useMapBounds: true,
            });
          }

          // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
          function placesSearchCB(data, status, pagination) {
            if (status === kakao.maps.services.Status.OK) {
              // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
              displayPlaces(data);
            } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
              // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
            } else if (status === kakao.maps.services.Status.ERROR) {
              // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
            }
          }

          // 지도에 마커를 표출하는 함수입니다
          function displayPlaces(places) {
            // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
            // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
            var order = document
              .getElementById(currCategory)
              .getAttribute("data-order");

            for (var i = 0; i < places.length; i++) {
              // 마커를 생성하고 지도에 표시합니다
              var marker = addMarker(
                new kakao.maps.LatLng(places[i].y, places[i].x),
                order
              );

              // 마커와 검색결과 항목을 클릭 했을 때
              // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
              (function (marker, place) {
                kakao.maps.event.addListener(marker, "click", function () {
                  displayPlaceInfo(place);
                });
              })(marker, places[i]);
            }
          }

          // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
          function addMarker(position, order) {
            var imageSrc =
                "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
              imageSize = new kakao.maps.Size(27, 28), // 마커 이미지의 크기
              imgOptions = {
                spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
                spriteOrigin: new kakao.maps.Point(46, order * 36), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                offset: new kakao.maps.Point(11, 28), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
              },
              markerImage = new kakao.maps.MarkerImage(
                imageSrc,
                imageSize,
                imgOptions
              ),
              marker = new kakao.maps.Marker({
                position: position, // 마커의 위치
                image: markerImage,
              });

            marker.setMap(map); // 지도 위에 마커를 표출합니다
            markers.push(marker); // 배열에 생성된 마커를 추가합니다

            return marker;
          }

          // 지도 위에 표시되고 있는 마커를 모두 제거합니다
          function removeMarker() {
            for (var i = 0; i < markers.length; i++) {
              markers[i].setMap(null);
            }
            markers = [];
          }

          // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
          function displayPlaceInfo(place) {
            var content =
              '<div class="placeinfo">' +
              '   <a class="title" href="' +
              place.place_url +
              '" target="_blank" title="' +
              place.place_name +
              '">' +
              place.place_name +
              "</a>";

            if (place.road_address_name) {
              content +=
                '    <span title="' +
                place.road_address_name +
                '">' +
                place.road_address_name +
                "</span>" +
                '  <span class="jibun" title="' +
                place.address_name +
                '">(지번 : ' +
                place.address_name +
                ")</span>";
            } else {
              content +=
                '    <span title="' +
                place.address_name +
                '">' +
                place.address_name +
                "</span>";
            }

            content +=
              '    <span class="tel">' +
              place.phone +
              "</span>" +
              "</div>" +
              '<div class="after"></div>';

            contentNode.innerHTML = content;
            placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
            placeOverlay.setMap(map);
          }

          // 각 카테고리에 클릭 이벤트를 등록합니다
          function addCategoryClickEvent() {
            var category = document.getElementById("category"),
              children = category.children;

            for (var i = 0; i < children.length; i++) {
              children[i].onclick = onClickCategory;
            }
          }

          // 카테고리를 클릭했을 때 호출되는 함수입니다
          function onClickCategory() {
            var id = this.id,
              className = this.className;

            placeOverlay.setMap(null);

            if (className === "on") {
              currCategory = "";
              changeCategoryClass();
              removeMarker();
            } else {
              currCategory = id;
              changeCategoryClass(this);
              searchPlaces();
            }
          }

          // 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
          function changeCategoryClass(el) {
            var category = document.getElementById("category"),
              children = category.children,
              i;

            for (i = 0; i < children.length; i++) {
              children[i].className = "";
            }

            if (el) {
              el.className = "on";
            }
          }
        </script>
      </div>
      <!-- <div class="container">

      <div class="row">
        <div class="col-12">
          <h2 class="contact-title">Get in Touch</h2>
        </div>
        <div class="col-lg-8">
          <form class="form-contact contact_form" action="contact_process.php" method="post" id="contactForm"
            novalidate="novalidate">
            <div class="row">
              <div class="col-12">
                <div class="form-group">

                  <textarea class="form-control w-100" name="message" id="message" cols="30" rows="9"
                    onfocus="this.placeholder = ''" onblur="this.placeholder = 'Enter Message'"
                    placeholder='Enter Message'></textarea>
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <input class="form-control" name="name" id="name" type="text" onfocus="this.placeholder = ''"
                    onblur="this.placeholder = 'Enter your name'" placeholder='Enter your name'>
                </div>
              </div>
              <div class="col-sm-6">
                <div class="form-group">
                  <input class="form-control" name="email" id="email" type="email" onfocus="this.placeholder = ''"
                    onblur="this.placeholder = 'Enter email address'" placeholder='Enter email address'>
                </div>
              </div>
              <div class="col-12">
                <div class="form-group">
                  <input class="form-control" name="subject" id="subject" type="text" onfocus="this.placeholder = ''"
                    onblur="this.placeholder = 'Enter Subject'" placeholder='Enter Subject'>
                </div>
              </div>
            </div>
            <div class="form-group mt-3">
              <a href="#" class="btn_3 button-contactForm">Send Message</a>
            </div>
          </form>
        </div>
        <div class="col-lg-4">
          <div class="media contact-info">
            <span class="contact-info__icon"><i class="ti-home"></i></span>
            <div class="media-body">
              <h3>Buttonwood, California.</h3>
              <p>Rosemead, CA 91770</p>
            </div>
          </div>
          <div class="media contact-info">
            <span class="contact-info__icon"><i class="ti-tablet"></i></span>
            <div class="media-body">
              <h3>00 (440) 9865 562</h3>
              <p>Mon to Fri 9am to 6pm</p>
            </div>
          </div>
          <div class="media contact-info">
            <span class="contact-info__icon"><i class="ti-email"></i></span>
            <div class="media-body">
              <h3>support@colorlib.com</h3>
              <p>Send us your query anytime!</p>
            </div>
          </div>
        </div>
      </div>
    </div> -->
    </section>
    <!-- ================ contact section end ================= -->
<%@include file="footer.jsp" %>

  </body>
</html>
