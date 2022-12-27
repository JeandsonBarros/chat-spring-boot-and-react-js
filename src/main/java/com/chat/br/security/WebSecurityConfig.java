package com.chat.br.security;

import com.chat.br.repository.UserRepository;
import com.chat.br.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
//@ComponentScan("com.chat.br.security")
public class WebSecurityConfig{

    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.httpBasic().and()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/forgot-password/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/data").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/auth/update").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/auth/delete").hasAnyAuthority("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/auth/all-users").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/auth/admin/delete/{email}").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/chat/**").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(userRepository, tokenService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

       return http.build();
    }

     @Bean
     public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger.json/**",
                "/swagger-ui/**",
                "/api/**"
                ,"/ws-message/**"
                ,"/topic/message/**"
                //,"/sendMessage/**"
        );
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name()
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
        return source;
    }
}
