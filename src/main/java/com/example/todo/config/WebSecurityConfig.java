package com.example.todo.config;

import com.example.todo.filter.JwtAuthFilter;
import com.example.todo.userapi.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

//@Configuration // 설정 클래스 용도로 사용하도록 스프링에 등록하는 어노테이션
@EnableWebSecurity // 시큐리티 설정 파일로 사용할 클래스 선언.
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;


    @Bean // 외부 라이브러리를 사용하기 위해 스프링에 등록 해주는 어노테이션
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Security 모듈이 기본적으로 제공하는 보안 정책 해제.
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                // 세션 인증을 사용하지 않겠다라는 설정 (생성하지도 말고 사용도 안하겠다)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 어떤 요청에서 인증을 안 할 것인지 설정, 언제 할 것인지 설정
                .authorizeRequests()
                .antMatchers("/", "/api/auth/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/auth/todos").hasRole("ADMIN")
                .anyRequest().authenticated();

        // 토큰 인증 필터 연결
        http.addFilterAfter(
                jwtAuthFilter,
                CorsFilter.class // import 주의 : spring으로 가져올것
        );

        return http.build();

    }

}
