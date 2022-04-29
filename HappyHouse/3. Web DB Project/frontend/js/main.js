var searchObject = {};
window.onload = () => {
  var login = localStorage.getItem("login");
  let nav = document.querySelector(".navbar-menu")
  if(!login){
    let scr = `<span><a href="login.html">로그인</a></span>`
    nav.innerHTML = scr + nav.innerHTML;
  } else {
    var scr = `<span><a href="" onclick="logout()">로그아웃</a></span>`
    let info = `<span><a href="userinfo.html">회원정보</a></span>`
    nav.innerHTML = scr + nav.innerHTML + info;
  }
  $('#city li > a').on('click', function() {
    $('#dd1').text($(this).text());
    searchObject.city = $(this).attr('value');
  })
  $('#gu li > a').on('click', function() {
    $('#dd2').text($(this).text());
    searchObject.gu = $(this).attr('value');
  })
  $('#dong li > a').on('click', function() {
    $('#dd3').text($(this).text());
    searchObject.dong = $(this).attr('value');
  })
  $('#kind li > a').on('click', function() {
    $('#dd4').text($(this).text());
    searchObject.kind = $(this).attr('value');
  })
}


function logout() {
  // 로그아웃
  console.log('logout');
  localStorage.removeItem("login");
  if(!localStorage.getItem("login")){
    alert("성공적으로 로그아웃 되었습니다.")
    location.reload();
  }
}

function regist() {
  let id = document.getElementById("id").value;
  let password = document.getElementById("password").value;
  let name = document.getElementById("name").value;
  let email = document.getElementById("email").value;
  let phone = document.getElementById("phone").value;

  // 입력 내용 출력
  console.log('ID: ' + id
          +'\nPWD: ' + password
          +'\nNAME: ' + name
          +'\nADDR: ' + email
          +'\nPHONE: ' + phone);


  if (!id || !password || !name || !email || !phone) {
    alert("빈칸이 없도록 입력해주세요.");
    return;
  } else {
    const user = {
      id: id,
      password: password,
      name: name,
      email: email,
      phone: phone,
    };

    localStorage.setItem("user", JSON.stringify(user));
    alert("사용자 등록 성공!");
    window.location.replace("login.html");
  }
}

function login() {
  // 로그인
  console.log('login');
  // 문서에서 id로 input data 가져오기
  let id = document.getElementById("id").value;
  let password = document.getElementById("password").value;

  // 입력 id password 출력
  console.log('ID: ' + id + '\nPWD: ' + password)

  // 로컬스토리지에 "user" 키로 저장된 item 가져와서 json 객체로 만들기
  const user = JSON.parse(localStorage.getItem("user"));

  // 입력값 검증
  if (id == user.id && password == user.password) {
    alert("로그인 성공 !");
    // 로그인 성공하면 index 페이지로 이동.
    localStorage.setItem("login","true");
    location.replace("index.html");
  } else {
    alert("로그인 실패 !");
  }
}

function search() {
  console.log(searchObject);
  if(searchObject.city && searchObject.gu && searchObject.dong && searchObject.kind){
    setTimeout(() =>{
      location.replace("searchDong.html");
    }, 1000)
  } else{
    alert("항목을 모두 입력해주세요!")
  }
}

function findpwd(){                            
  var USER = JSON.parse(localStorage.getItem("user"));
  let ID = document.getElementById('id').value;
  let NAME = document.getElementById('name').value;
  let PHONE = document.getElementById('phone').value;
  if(!ID || !NAME || !PHONE){
    alert('항목을 모두 입력해주세요!');
    return;
  }                        
  else{
    if (   ID == USER.id
      && NAME == USER.name
      &&PHONE == USER.phone){ // 비밀 번호 찾기 성공
        let tmp = Math.random().toString(36).slice(2);
        USER.password = tmp;
        localStorage.setItem("user", JSON.stringify(USER));
        // 메일로 임시비밀번호 전송은 생략
        alert("임시 비밀번호 전송 완료!");
        window.location.replace("login.html");
    }
    else{
      alert("입력한 정보와 일치하는 정보가 없습니다.")
      return;
    }

  }
}