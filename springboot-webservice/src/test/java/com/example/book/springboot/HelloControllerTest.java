package com.example.book.springboot;

import com.example.book.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// 테스트 할 때 JUnit에 내장된 Runner 대신 SpringRunner를 실행
@RunWith(SpringRunner.class)
// Web에 집중하는 Spring Test Annotation
// @Controller, @ControllerAdvice를 사용 가능
// @Service, @Component, @Repository는 사용 불가능
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    // Bean 주입
    @Autowired
    // HTTP GET, POST 등에 대한 웹 API 테스트를 위해 사용
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void helloDto가_리턴된다() throws Exception{
        String name = "hello";
        int amount = 1000;

        // jsonPath를 통해 JSON 응답값을 필드별로 검증
        // $를 기준으로 필드명을 명시
        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
