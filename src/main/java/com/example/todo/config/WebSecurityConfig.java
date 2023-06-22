package com.example.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 설정 클래스 용도로 사용하도록 스프링에 등록하는 어노테이션
public class WebSecurityConfig {

    @Bean // 외부 라이브러리를 사용하기 위해 스프링에 등록 해주는 어노테이션
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
