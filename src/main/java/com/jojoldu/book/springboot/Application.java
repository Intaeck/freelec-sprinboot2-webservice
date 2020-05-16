package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  // JPA Auditing 활성화 (BaseTimeEntity)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run 으로 내장 WAS 실행
        SpringApplication.run(Application.class, args);
    }
}
