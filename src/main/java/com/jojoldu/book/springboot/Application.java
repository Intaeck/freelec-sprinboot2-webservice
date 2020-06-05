package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@ComponentScan(basePackages={"com.jojoldu.book.springboot"})
//@EnableJpaAuditing  // JPA Auditing 활성화 (BaseTimeEntity) --> @WebMvcTest를 위해 삭제하고 config/JpaConfig.java로 이동
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run 으로 내장 WAS 실행
        SpringApplication.run(Application.class, args);
    }
}
