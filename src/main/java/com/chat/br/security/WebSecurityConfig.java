package com.chat.br.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class WebSecurityConfig{

    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/forgot-password/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/user/data").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/user/update").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/user/delete").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/user/all-users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/user/admin/delete/{email}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/message/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

       return http.build();
    }

  /*  @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails user = User.builder()
                .username("jeu")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    } */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
