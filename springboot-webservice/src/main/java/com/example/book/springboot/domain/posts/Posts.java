package com.example.book.springboot.domain.posts;

import com.example.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
// Default Constructor를 자동 생성
@NoArgsConstructor
// Table과 링크될 Class임을 나타냄
@Entity
public class Posts extends BaseTimeEntity {
    // PK 필드를 표현
    @Id
    // PK 생성 규칙을 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Table의 Column을 선언, 생략 가능
    // VARCHAR(255)를 VARCHAR(500)으로 변경하기 위해 사용
    @Column(length = 500, nullable = false)
    private String title;

    // VARCHAR(255)를 TEXT로 변경하기 위해 사용
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    // Builder 패턴 적용
    @Builder
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
