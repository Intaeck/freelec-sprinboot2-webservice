package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.service.PostsService;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//lombok -> 선언된 모든 final필드가 포함된 생성자를 생성해줌 but final이 없는 필드는 생성자에 미포함
@RequiredArgsConstructor
//JSON을 반환하는 컨트롤러 - 예전에 ResponseBody를 각 메소드마다 선언했던 것을 한번에 사용하게 해줌
@RestController
public class PostsApiController {

    //@RequiredArgsConstructor 가 PostsService 생성자를 자동으로 생성함
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    // URL의 /api/v1/posts/{id} 중에서 @PathVariable로 {id}값을 가져올 수 있음
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);
        return id;
    }

}
