package com.ssafy.utf.api.service;

import com.ssafy.utf.api.request.UserJoinReq;
import com.ssafy.utf.api.request.UserUpdateReq;
import com.ssafy.utf.db.entity.user.User;

public interface UserService {
    User joinUser(UserJoinReq userJoinReq);

    User updateUser(UserUpdateReq userUpdateReq);

    void deleteUser(long userId);
}
