package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor  // final이 선언된 모든 필드를 인자값으로 하는 생성자를 생성 (lombok)
@EnableWebSecurity  // Sprinig Security 설정들을 활성화 해줌
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // h2-console 화면을 사용하기 위해 해당 옵션들(csrf, headers, frameOptions)을 disable
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    // URL별 권한관리를 설정하는 옵션의 시작점 - 선언해야 antMatchers옵션 사용 가능
                    .authorizeRequests()
                    // antMatchers -> 권한 관리 대상을 지정하는 옵션으로 URL, HTTP 메소드별로 관리 가능
                    // permitAll()옵션을 통해 전체 열람권한 부여
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                    // USER권한을 가진 사람만 가능
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // 나머지 URL은 모두 인증된 사용자들에게만 허용
                    .anyRequest().authenticated()
                .and()
                    // 로그아웃 기능에 대한 여러 설정의 진입점
                    .logout()
                        // 로그아웃 성공 시 "/" 주소로 이동
                        .logoutSuccessUrl("/")
                .and()
                    // OAut2 로그인 기능에 대한 여러 설정의 진입점
                    .oauth2Login()
                        // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                        .userInfoEndpoint()
                            // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
                            .userService(customOAuth2UserService);
                            // 리소스 서버(소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시 가능
    }


}
