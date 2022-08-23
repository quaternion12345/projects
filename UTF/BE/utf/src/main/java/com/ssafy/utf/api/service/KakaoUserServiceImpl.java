package com.ssafy.utf.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.utf.db.entity.user.KakaoTokenInfo;
import com.ssafy.utf.db.entity.user.KakaoUserInfo;
import com.ssafy.utf.db.entity.user.User;
import com.ssafy.utf.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@PropertySource("classpath:social.properties")
public class KakaoUserServiceImpl implements KakaoUserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestApiKey;

    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    //카카오 서버로 인증 코드를 보내서 액세스/리프레쉬 토큰을 받아오는 메서드
    @Override
    public KakaoTokenInfo sendCode(String code) throws Exception {
        System.out.println("sendCode");

        String reqUrl = "https://kauth.kakao.com/oauth/token";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=" + kakaoRestApiKey);
        sb.append("&redirect_uri=" + kakaoRedirectUri);
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper om = new ObjectMapper();

        KakaoTokenInfo kakaoTokenInfo = om.readValue(result, KakaoTokenInfo.class);

        System.out.println(kakaoTokenInfo.toString());

        return kakaoTokenInfo;
    }

    //카카오 서버에 액세스 토큰을 보내서 사용자 정보를 받아오는 메서드
    @Override
    public KakaoUserInfo sendToken(String accessToken) throws Exception {
        System.out.println("sendToken");
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper om = new ObjectMapper();

        KakaoUserInfo kakaoUserInfo = om.readValue(result, KakaoUserInfo.class);

        System.out.println(kakaoUserInfo.toString());

        return kakaoUserInfo;
    }

    //사용자 소셜id가 DB에 존재하는지 조회해서 회원가입 여부 판단. 존재하면 유저 정보, 존재하지 않으면 null 반환
    @Override
    public User socialIdCheck(String socialId) {
        User user = userRepository.findByKakaoSocialId(socialId);
        return user;
    }
}
