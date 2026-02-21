package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                    .mvcMatchers(AUTH_WHITELIST).permitAll()
                    // Открытые GET-эндпоинты
                    .mvcMatchers(HttpMethod.GET, "/ads", "/ads/{id}", "/ads/{id}/comments").permitAll()
                    // Всё остальное требует аутентификации
                    .anyRequest().authenticated()
            )
            .cors(withDefaults())
            .httpBasic(withDefaults());
        return http.build();
    }
}