package com.example.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Entity Class로 DB에 접근하게 해주는 DAO(DB Layer Accessor)
// JpaRepository를 상속하면 기본적인 CRUD method 자동 생성
public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
