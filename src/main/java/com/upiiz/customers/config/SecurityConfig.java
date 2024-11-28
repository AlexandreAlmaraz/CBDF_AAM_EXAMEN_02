package com.upiiz.customers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        UserDetails moderator = User.withUsername("moderator")
                .password(passwordEncoder().encode("moderator123"))
                .roles("MODERATOR")
                .build();

        UserDetails editor = User.withUsername("editor")
                .password(passwordEncoder().encode("editor123"))
                .roles("EDITOR")
                .build();

        UserDetails developer = User.withUsername("developer")
                .password(passwordEncoder().encode("dev123"))
                .roles("DEVELOPER")
                .build();

        UserDetails analyst = User.withUsername("analyst")
                .password(passwordEncoder().encode("analyst123"))
                .roles("ANALYST")
                .build();

        return new InMemoryUserDetailsManager(admin, user, moderator, editor, developer, analyst);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "USER", "MODERATOR", "EDITOR", "DEVELOPER", "ANALYST")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        return http.build();
    }
}