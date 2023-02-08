package com.example.book.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // @WebMvcTest는 @Configuration을 스캔하지 않음
@EnableJpaAuditing
public class JpaConfig {
}
