package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.LoginUser;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.service.PostsService;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    // @RequiredArgsConstructor가 final로 선언된 PostsService 클래스 생성자를 생성함
    private final PostsService postsService;
    private final HttpSession httpSession;

    //HandlerMethodArgumnetResolver (LoginUserArgumentResolver) 클래스 사용 전
//    @GetMapping("/")
//    public String index(Model model) {  //서버 템플릿 엔진에서 사용할 수 있는 객체를 저장 가능
//        // postsService.findAllDesc()로 가져온 결과를 posts로 index.mustache에 전달
//        model.addAttribute("posts", postsService.findAllDesc());
//
//        // CustomOAuth2UserService에서 로그인 성공 시 세션에 SessionUser를 저장하도록 구성
//        // 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있음
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//
//        // 세션에 저장된 값이 있을때만 model에 userName으로 등록함. 세션에 저장된값이 없으면 model은 null이니 로그인 버튼이 보이게됨
//        if (user != null) {
//            model.addAttribute("userName", user.getEmail());
//        }
//        return "index";
//    }

    //HandlerMethodArgumnetResolver (LoginUserArgumentResolver) 클래스 사용 후
    @GetMapping("/")
    //기존에 (User) httpSession.getAttribute("user")로 가져오던 세션정보값을 개선하여, 어느 컨트롤러든지 @LoginUser만 사용하면
    //세션정보를 가져올 수 있음
    public String index(Model model, @LoginUser SessionUser user) {

        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    //게시글 수정 - index.mustache 에서 호출
    @GetMapping("/posts/update/{id}")
    //@PathVariable은 URL경로로 넘어온 변수인 {id}값을 받아줌
    public String postUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }
}
