package com.ssafy.utf.db.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class KakaoUserInfo {
    String id;

    String email;

    @JsonProperty("kakao_account")
    private void unpackKakaoAccount(HashMap<String, String> kakaoAccount) {
        email = kakaoAccount.get("email");
    }
}
