package com.jojoldu.book.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//lombok -> 선언된 모든 필드의 getter 메소드 생성
@Getter
//lombok -> 선언된 모든 final필드가 포함된 생성자를 생성해줌 but final이 없는 필드는 생성자에 미포함
@RequiredArgsConstructor
public class HelloResponseDto {

    private final String name;
    private final int amount;
}
