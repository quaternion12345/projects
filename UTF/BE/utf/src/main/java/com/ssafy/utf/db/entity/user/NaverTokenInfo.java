package com.ssafy.utf.db.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverTokenInfo {
    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("expires_in")
    int expiresIn;
}
