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
public class NaverUserInfo {
    String id;

    String email;

    @JsonProperty("response")
    private void unpackResponse(HashMap<String, String> response) {
        id = response.get("id");
        email = response.get("email");
    }
}
