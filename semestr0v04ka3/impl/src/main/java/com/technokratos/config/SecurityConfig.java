package com.technokratos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/**")
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/auth/signin"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect("/api/v1/main"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/auth/signup", "/auth/signin").anonymous()
                        .requestMatchers("/api/v1/problems/**").permitAll()
                        .requestMatchers("/auth/logout").fullyAuthenticated()
                        .requestMatchers("/problems/**").fullyAuthenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/auth/signin")
                        .loginProcessingUrl("/auth/signin")
                        .defaultSuccessUrl("/problems")
                        .failureUrl("/auth/signin?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/signin")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

}
