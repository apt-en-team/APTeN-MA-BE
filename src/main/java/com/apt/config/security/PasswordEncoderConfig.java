package com.apt.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// PasswordEncoder를 별도 Config로 분리하여 순환 참조 방지
// (WebSecurityConfiguration ↔ CustomOAuth2UserService 간 순환 해결)
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 더미 데이터용 BCrypt 해시 출력 (확인 후 삭제)
    @Bean
    public CommandLineRunner printHash(PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("=== BCrypt 해시 출력 ===");
            System.out.println("admin1234  : " + passwordEncoder.encode("admin1234"));
            System.out.println("test1234   : " + passwordEncoder.encode("test1234"));
            System.out.println("=======================");
        };
    }
}