package com.example.book.springboot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// SpringBoot의 Autoconfiguration, ComponentScan을 자동으로 수행하도록 설정
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        // 내장 WAS를 이용하여 Tomcat의 설치없이 Jar파일로 실행
        SpringApplication.run(Application.class, args);
    }
}
