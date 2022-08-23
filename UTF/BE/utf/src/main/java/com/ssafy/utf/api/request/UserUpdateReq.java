package com.ssafy.utf.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateReq {
    long userId;

    String userName;

    String email;
}
