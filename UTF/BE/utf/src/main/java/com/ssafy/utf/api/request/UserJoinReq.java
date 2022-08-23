package com.ssafy.utf.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserJoinReq {
    String userName;

    int socialLoginType;

    String socialId;

    String email;
}
