package com.example.book.springboot.config.auth;

import com.example.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
// Spring Security 설정을 활성화
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                // h2-console을 사용하기 위해 비활성화
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    // URL별 권한 관리 설정의 시작점
                    .authorizeRequests()
                    // 권한 관리 대상을 지정
                    .antMatchers("/", "/css/**","/images/**","/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // 지정하지 않은 다른 요청
                    .anyRequest().authenticated()
                .and()
                    // 로그아웃 기능에 대한 설정의 시작점
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    // OAuth2 로그인 기능에 대한 설정의 시작점
                    .oauth2Login()
                    // OAut2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                    .userInfoEndpoint()
                    // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
                    // 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
                    .userService(customOAuth2UserService);
    }
}
