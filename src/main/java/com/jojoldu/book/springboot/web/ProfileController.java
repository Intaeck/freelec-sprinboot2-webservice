package com.jojoldu.book.springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        //현재 실행중인 ActiveProfile을 모두 가져옴
        //--> 즉, real, oauth, real-db 등이 활성화(active) 되어있다면 3개 모두 담겨있음
        //여기서 real,real1,real2는 모두 배포에 사용될 profile이라서 이중 하나라도 있으면 그값을 반환
        //(여기서는 무중단 배포를 위해 real1, real2만 사용되지만 step2를 사용할 수도 있으니 real을 남겨둠)
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);

        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile);
    }

}
