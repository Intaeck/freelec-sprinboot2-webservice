package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//JUnit에 내장된 실행자 외에 다른 실행자인 SpringRuuner 실행자 사용 (스프링부트 테스트와 JUnit사이에 연결자 역할)
@RunWith(SpringRunner.class)
//@Controller, @ControllerAdvice 등을 사용가능 but @Service, @Component, @Repository등은 사용불가
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    //스프링이 관리하는 bean 주입받음
    @Autowired
    // MockMvc :  웹API를 테스트 시에 사용, 스프링 MVC테스트의 시작점 (HTTP GET, POST 테스트 가능)
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        //MockMvc를 통해 /hello 주소로 HTTP GET 요청
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                //mvc.perform의 결과를 검증 - 응답본문의 내용을 검증
                .andExpect(content().string(hello));
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(get("/hello/dto")
                .param("name", name)
                // param깂은 String만 허용되므로 valueOf 사용
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                          //jsonPath -> JSON 응답값을 필드별로 검증할 수 있는 메소드. $를 기준으로 필드명 명시
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }


}
