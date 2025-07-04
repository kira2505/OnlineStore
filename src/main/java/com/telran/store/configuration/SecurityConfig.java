package com.telran.store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(requests ->
                        requests
                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/users").authenticated()
                                .requestMatchers(HttpMethod.DELETE,"/users/**").authenticated()
                                .requestMatchers("/products/**").authenticated()
                                .requestMatchers("/orders/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/categories/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/categories/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/categories/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/products/**").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/products/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/products/**").authenticated()
                                .requestMatchers("/favorites/**").authenticated()
                                .requestMatchers("/carts/**").authenticated()
                                .requestMatchers("/payments/**").authenticated()
                                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
