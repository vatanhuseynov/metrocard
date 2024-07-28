package com.ibar.metrocard.config;

import com.ibar.metrocard.auth.filter.AuthorizationFilter;
import com.ibar.metrocard.auth.filter.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthorizationFilter authorizationFilter;
    private final CustomAuthenticationProvider authProvider;


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.authenticationProvider(authProvider)
                .getSharedObject(AuthenticationManagerBuilder.class).build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(STATELESS));
        http.authorizeHttpRequests(authorizehttpRequests-> authorizehttpRequests.requestMatchers("/public/**", "/*/public/**",
                "/error", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll());
        http.authorizeHttpRequests(authorizehttpRequests->authorizehttpRequests.anyRequest().authenticated());
        http.authenticationProvider(authProvider);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }



}
