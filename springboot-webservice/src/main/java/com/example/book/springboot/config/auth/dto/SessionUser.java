package com.example.book.springboot.config.auth.dto;

import com.example.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// Entity는 다른 Entity와 관계를 형성할 수 있으므로
// Entity 대신 별도의 직렬화 Dto 클래스를 생성
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
