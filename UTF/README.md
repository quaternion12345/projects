# 팀소개

---

| Name | 고태희 | 노호준 | 양경섭 | 임재훈 |
| --- | --- | --- | --- | --- |
| Profile | <img src="https://user-images.githubusercontent.com/56426044/185310754-8db95229-c9f6-4b75-8bf2-d0f6ecb90d9c.png" height="100"> | <img src="https://user-images.githubusercontent.com/56426044/185322437-81ad90cf-aa7e-4870-81d3-678c1a30e8fa.png" height="100"> | <img src="https://user-images.githubusercontent.com/56426044/185322478-5bddc47c-7de8-4524-9dc7-ee5d065e8734.png" height="100"> | <img src="https://user-images.githubusercontent.com/56426044/185323318-a6461a92-a592-484b-a980-4f49591609e2.png" height="100"> |
| Position | Fullstack / 통계 | Fullstack / 로그인 | Fullstack / openvidu | Fullstack / 채팅 |

# 개요

---

### 프로젝트 진행 기간

2022.7.11(월) ~ 2022.8.19(금)

SSAFY  7기 공통프로젝트 - UTF

### Overview

강사를 위한 강의 피드백 프로젝트

# 기술 스택

---

| Teck | Stack |
| --- | --- |
| Language | Java, Javascript |
| Backend | Spring Boot, JPA |
| Frontend | Vue.js, Bootstrap-vue, Vuex, axios |
| Database | MySql |
| Server | AWS EC2, Nginx , openvidu |
| DevOps | Git, Docker |

### 서비스 아키텍처

![image](https://user-images.githubusercontent.com/56426044/185528366-573a3da5-226b-4a8c-8ec9-dd0cab7b3699.png)

- 백엔드 구조
    
    ```jsx
    📦utf
     ┣ 📂api
     ┃ ┣ 📂controller
     ┃ ┃ ┣ 📜LectureController.java
     ┃ ┃ ┣ 📜LectureRoomController.java
     ┃ ┃ ┣ 📜StatisticsController.java
     ┃ ┃ ┗ 📜UserController.java
     ┃ ┣ 📂request
     ┃ ┃ ┣ 📜LectureRoomRegistReq.java
     ┃ ┃ ┣ 📜RecordingReq.java
     ┃ ┃ ┣ 📜UserJoinReq.java
     ┃ ┃ ┗ 📜UserUpdateReq.java
     ┃ ┣ 📂response
     ┃ ┃ ┗ 📜UserRes.java
     ┃ ┗ 📂service
     ┃ ┃ ┣ 🧾GoogleUserService.java
     ┃ ┃ ┣ 📜GoogleUserServiceImpl.java
     ┃ ┃ ┣ 🧾KakaoUserService.java
     ┃ ┃ ┣ 📜KakaoUserServiceImpl.java
     ┃ ┃ ┣ 🧾LectureRoomService.java
     ┃ ┃ ┣ 📜LectureRoomServiceImpl.java
     ┃ ┃ ┣ 🧾LectureService.java
     ┃ ┃ ┣ 📜LectureServiceImpl.java
     ┃ ┃ ┣ 🧾NaverUserService.java
     ┃ ┃ ┣ 📜NaverUserServiceImpl.java
     ┃ ┃ ┣ 🧾StatisticsService.java
     ┃ ┃ ┣ 📜StatisticsServiceImpl.java
     ┃ ┃ ┣ 🧾UserService.java
     ┃ ┃ ┗ 📜UserServiceImpl.java
     ┣ 📂common
     ┃ ┣ 📂util
     ┃ ┃ ┗ 📜RandomCodeGenerator.java
     ┣ 📂config
     ┃ ┣ 📜SecurityConfig.java
     ┃ ┗ 📜WebMvcConfig.java
     ┣ 📂db
     ┃ ┣ 📂entity
     ┃ ┣ ┣ 📂lecture
     ┃ ┣ ┣ ┣ 📜Lecture.java
     ┃ ┣ ┣ ┗ 📜LectureRoom.java
     ┃ ┣ ┣ 📂statistics
     ┃ ┣ ┣ ┣ 📜Emotion.java
     ┃ ┣ ┣ ┣ 📜Focus.java
     ┃ ┣ ┣ ┣ 📜RecentLecture.java
     ┃ ┣ ┣ ┣ 📜Statistics.java
     ┃ ┣ ┣ ┗ 📜StatisticsConverter.java
     ┃ ┣ ┗ 📂user
     ┃ ┣ ┣ ┣ 📜GoogleTokenInfo.java
     ┃ ┣ ┣ ┣ 📜GoogleUserInfo.java
     ┃ ┣ ┣ ┣ 📜KakaoTokenInfo.java
     ┃ ┣ ┣ ┣ 📜KakaoUserInfo.java
     ┃ ┣ ┣ ┣ 📜NaverTokenInfo.java
     ┃ ┣ ┣ ┣ 📜NaverUserInfo.java
     ┃ ┣ ┣ ┗ 📜User.java
     ┃ ┗ 📂repository
     ┃ ┣ ┣ 🧾LectureRepository.java
     ┃ ┣ ┣ 🧾LectureRoomRepository.java
     ┃ ┣ ┣ 🧾StatisticsRepository.java
     ┗ ┗ ┗ 🧾UserRepository.java
     
    ```
    
- 프론트엔드 구조
    
    ```jsx
    📦src
     ┣ 📂api
     ┃ ┣ 📜index.js
     ┃ ┗ 📜openvidu.js
     ┣ 📂assets
     ┃ ┣ 📂image
     ┃ ┃ ┣ 📂subjectIcon
     ┃ ┃ ┃ ┣ 📜subject0.png
     ┃ ┃ ┃ ┣ 📜subject1.png
     ┃ ┃ ┃ ┣ 📜subject2.png
     ┃ ┃ ┃ ┣ 📜subject3.png
     ┃ ┃ ┃ ┣ 📜subject4.png
     ┃ ┃ ┃ ┣ 📜subject5.png
     ┃ ┃ ┃ ┣ 📜subject6.png
     ┃ ┃ ┃ ┣ 📜subject7.png
     ┃ ┃ ┃ ┣ 📜subject8.png
     ┃ ┃ ┃ ┗ 📜subject9.png
     ┃ ┃ ┣ 📜btnG_완성형.png
     ┃ ┃ ┣ 📜btn_google_signin_dark_normal_web@2x.png
     ┃ ┃ ┣ 📜kakao_login_medium_wide.png
     ┃ ┃ ┗ 📜photo-1456513080510-7bf3a84b82f8.jpg
     ┃ ┣ 📜logo.png
     ┃ ┣ 📜UTFLogo-removebg.png
     ┃ ┗ 📜UTFLogo.png
     ┣ 📂components
     ┃ ┣ 📂chat
     ┃ ┃ ┣ 📜ChatComponents.vue
     ┃ ┃ ┣ 📜ChatForm.vue
     ┃ ┃ ┣ 📜ChatList.vue
     ┃ ┃ ┗ 📜ChatMessage.vue
     ┃ ┣ 📂common
     ┃ ┃ ┣ 📜AudioButton.vue
     ┃ ┃ ┣ 📜CamButton.vue
     ┃ ┃ ┗ 📜Logo.vue
     ┃ ┣ 📂lecture
     ┃ ┃ ┣ 📂student
     ┃ ┃ ┃ ┣ 📜ControlPanel.vue
     ┃ ┃ ┃ ┣ 📜StudentComponents.vue
     ┃ ┃ ┃ ┣ 📜UserInfo.vue
     ┃ ┃ ┃ ┣ 📜UserListComponents.vue
     ┃ ┃ ┃ ┣ 📜VideoComponents.vue
     ┃ ┃ ┃ ┗ 📜WaitingRoom.vue
     ┃ ┃ ┣ 📂teacher
     ┃ ┃ ┃ ┣ 📜ControlPanel.vue
     ┃ ┃ ┃ ┣ 📜FocusChart.vue
     ┃ ┃ ┃ ┣ 📜TeacherComponents.vue
     ┃ ┃ ┃ ┣ 📜UnderstandingChart.vue
     ┃ ┃ ┃ ┣ 📜UserInfo.vue
     ┃ ┃ ┃ ┣ 📜UserListComponents.vue
     ┃ ┃ ┃ ┗ 📜VideoComponents.vue
     ┃ ┃ ┣ 📜MyOvVideo.vue
     ┃ ┃ ┣ 📜MyVideo.vue
     ┃ ┃ ┣ 📜OvVideo.vue
     ┃ ┃ ┗ 📜UserVideo.vue
     ┃ ┣ 📂main
     ┃ ┃ ┣ 📜AddLecture.vue
     ┃ ┃ ┣ 📜LectureItem.vue
     ┃ ┃ ┣ 📜Profile.vue
     ┃ ┃ ┣ 📜ProfileSetting.vue
     ┃ ┃ ┗ 📜RecentlyLecture.vue
     ┃ ┣ 📂statistics
     ┃ ┃ ┣ 📜LectureChat.vue
     ┃ ┃ ┣ 📜LectureChatMessage.vue
     ┃ ┃ ┣ 📜LectureVideo.vue
     ┃ ┃ ┗ 📜RecordChart.vue
     ┃ ┗ 📂user
     ┃ ┃ ┣ 📜JoinFormComp.vue
     ┃ ┃ ┗ 📜LoginComp.vue
     ┣ 📂router
     ┃ ┗ 📜index.js
     ┣ 📂store
     ┃ ┣ 📂modules
     ┃ ┃ ┣ 📜chatStore.js
     ┃ ┃ ┣ 📜focusStore.js
     ┃ ┃ ┣ 📜lectureStore.js
     ┃ ┃ ┗ 📜StatisticsStore.js
     ┃ ┗ 📜index.js
     ┣ 📂views
     ┃ ┣ 📜ExitRoom.vue
     ┃ ┣ 📜JoinFormView.vue
     ┃ ┣ 📜LectureRoom.vue
     ┃ ┣ 📜LectureStatistics.vue
     ┃ ┣ 📜LoginView.vue
     ┃ ┣ 📜main.css
     ┃ ┣ 📜Main.vue
     ┃ ┗ 📜RedirectView.vue
     ┣ 📜App.vue
     ┣ 📜button.css
     ┗ 📜main.js
    ```
    

# 프로젝트 Wiki

---

[와이어프레임](https://www.figma.com/file/hy3n0iMZ772cDmVjjrDelW/Untitled)

[요구사항정의서](https://docs.google.com/spreadsheets/d/1SvJfqvWVth8-1sPwL61sl-OGodbra9xB5C5hfvhxcsk/edit#gid=0)

[ERD](https://www.erdcloud.com/d/gsvoKGuaGr5kK6yFi)

### 코드 컨벤션
---

- 백엔드
    - 파일명: PascalCase (ex: UserRepository)
    - 패키지명: 소문자
    - 클래스: PascalCase (ex : ClassName)
    - 변수: camelCase (ex : getId, userPassword)
    - 메소드 : camelCase (ex : getId, userPassword)
    - 상수: snake_case (ex: FILE_NUMBER)
- 프론트엔드
    - 변수: camelCase
    - 함수: camelCase (ex. const functionName= function () {})
    - 상수: SNAKE_CASE
    - vue 파일명: PascalCase
    - js 파일명: kebab-case
    - template구조
        - router: PascalCase
    - style구조
        - css 클래스: kebab-case
        - css 스타일 가이드: [https://code-study.tistory.com/18](https://code-study.tistory.com/18)
            - 선택자, 속성 전부 알파벳 순서
    - script구조(이하 리스트 순서대로 작성)
        - name
        - components
        - props
        - data : 단일 데이터를 상위에 명시,  form 구조를 하위에 명시
        - methods
        - Life Cycle Hook 순서 (beforeCreate → created → beforeMount → mounted → beforeUpdate → updated → beforeDestroy → destroyed)

### Git 컨벤션

## Branch 컨벤션

master : 기준이 되는 브랜치, 배포하는 브랜치

develop : 개발브랜치로 개발자들이 이 브랜치를 기준으로 각자 작업한 기능들 merge

feature : 단위 기능 개발 브랜치, 기능 개발완료되면 develop 브랜치로 합친다(merge)

release : 배포를 위해 master 브랜치로 보내기전에 QA(품질검사)를 위한 브랜치

hotfix : master 브랜치로 배포 했는데 버그 생겼을 떄 긴급 수정

- Master / develop이 메인 브랜치

<aside>
💡 feature/user

</aside>

## Commit 컨벤션

- **feat:** 새로운 기능 추가
- **fix:** 버그 픽스
- **docs:** 문서 수정
- **style:** 포맷, 세미콜론 수정, Optimize import, Code clean up 등 코드가 아닌 스타일에 관련된 수정
- **refactor:** 코드 리펙토링
- **test:** 테스트 코드 추가
- **chore:** 빌드 관련 업무 수정(안드로이드의 경우 builde.gradle, manifest)
- **design**: UI 디자인 변경 (css 등)

<aside>
💡 feat/frontend : 기능 추가
feat/backend : 기능 추가

</aside>

### Jira

4인팀으로 전원 Fullstack으로 프로젝트를 진행하게되어 기능단위로 Jira를 작성하였습니다

- Epic : 요구사항정의서에 대분류를 기준으로 나누었습니다
- Story : 요구사항정의서의 상세기능을 적절히 쪼개서 작성하였습니다
- Task : 개발외 업무가 필요할때 사용하였습니다

### Notion

공유가 필요한 문서작업에 경우 노션을 통해서 동시 작업하면서 업무 효율을 높일 수 있었고
다양한 문서를 노션에 정리하여 개발에 도움을 주었습니다.

### Scrum

매일 Scrum 회의를 통해서 전날 각자가 진행했던 업무와 막혔던 부분에 대해서 이야기하고
오늘은 어떤 업무를 진행하게 될지 이야기하면서 서로의 진행사항을 파악할수 있었고
이후의 스케줄 관리에 근거가 되어주었습니다

# UI소개

---

### 회원가입, 메인화면
![image](./docs/gif/회원가입,메인화면.gif)

### 강사입장
![image](./docs/gif/강사_입장.gif)

### 강사 화면공유
![image](./docs/gif/강사_화면공유.gif)

### 강사 통신
![image](./docs/gif/강사_채팅,버튼.gif)

### 강사 실시간 통계
![image](./docs/gif/강사_이해도,집중도.gif)

### 강사 통계
![image](./docs/gif/강사_통계.gif)

### 학생 대기화면
![image](./docs/gif/학생_대기화면.gif)

### 학생입장
![image](./docs/gif/학생_입장.gif)

### 학생 화면공유
![image](./docs/gif/학생_화면공유.gif)

### 학생 통신
![image](./docs/gif/학생_채팅,버튼.gif)
