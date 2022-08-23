package com.ssafy.utf.api.controller;

import com.ssafy.utf.api.request.UserJoinReq;
import com.ssafy.utf.api.request.UserUpdateReq;
import com.ssafy.utf.api.service.*;
import com.ssafy.utf.db.entity.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    KakaoUserService kakaoUserService;

    @Autowired
    NaverUserService naverUserService;

    @Autowired
    GoogleUserService googleUserService;

    @Autowired
    UserService userService;

    @PostMapping("/socialLogin")
    public ResponseEntity<Object> socialLogin(@RequestBody HashMap<String, Object> payload) {
        HashMap<String, Object> data = (HashMap<String, Object>) payload.get("data");
        int socialLoginType = (int) data.get("socialLoginType");
        String code = (String) data.get("code");
        String state = (String) data.get("state");

        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;
        try {
            User user = null;
            String socialId = null;
            String email = null;
            if (socialLoginType == 1) {
                KakaoTokenInfo kakaoTokenInfo = kakaoUserService.sendCode(code);
                KakaoUserInfo kakaoUserInfo = kakaoUserService.sendToken(kakaoTokenInfo.getAccessToken());
                socialId = kakaoUserInfo.getId();
                email = kakaoUserInfo.getEmail();
                user = kakaoUserService.socialIdCheck(socialId);
            } else if (socialLoginType == 2) {
                NaverTokenInfo naverTokenInfo = naverUserService.sendCode(code, state);
                NaverUserInfo naverUserInfo = naverUserService.sendToken(naverTokenInfo.getAccessToken());
                socialId = naverUserInfo.getId();
                email = naverUserInfo.getEmail();
                user = naverUserService.socialIdCheck(socialId);
            } else if (socialLoginType == 3) {
                GoogleTokenInfo googleTokenInfo = googleUserService.sendCode(code);
                GoogleUserInfo googleUserInfo = googleUserService.sendToken(googleTokenInfo.getAccessToken());
                socialId = googleUserInfo.getId();
                email = googleUserInfo.getEmail();
                user = googleUserService.socialIdCheck(socialId);
            }

            //가입 안했으면
            if (user == null) {
                System.out.println("가입된 회원이 존재하지 않습니다.");
                result.put("userExist", false);
                result.put("socialLoginType", socialLoginType);
                result.put("socialId", socialId);
                result.put("email", email);
            }
            //가입 했으면
            else {
                System.out.println("가입된 회원이 존재합니다.");
                result.put("userExist", true);
                result.put("userId", user.getUserId());
                result.put("userName", user.getUserName());
                result.put("email", user.getEmail());
            }
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }

        return ResponseEntity.status(status).body(result);
    }

    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody UserJoinReq userJoinReq) {
        System.out.println(userJoinReq);

        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;
        try {
            User user = userService.joinUser(userJoinReq);
            result.put("userExist", true);
            result.put("userId", user.getUserId());
            result.put("userName", user.getUserName());
            result.put("email", user.getEmail());
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }

        return ResponseEntity.status(status).body(result);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") long userId, @RequestBody UserUpdateReq userUpdateReq) {
        userUpdateReq.setUserId(userId);

        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;
        try {
            User user = userService.updateUser(userUpdateReq);
            result.put("userId", user.getUserId());
            result.put("userName", user.getUserName());
            result.put("email", user.getEmail());
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        return ResponseEntity.status(status).body(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") long userId) {
        System.out.println(userId);
        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;
        try {
            userService.deleteUser(userId);
            status = HttpStatus.OK;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        return ResponseEntity.status(status).body(result);
    }

}
