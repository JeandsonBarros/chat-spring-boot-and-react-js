package com.chat.br.security;

import com.chat.br.models.UserModel;
import com.chat.br.repository.UserRepository;
import com.chat.br.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    TokenService tokenService;
    UserRepository userRepository;

    public TokenAuthenticationFilter(UserRepository userRepository, TokenService tokenService){
        this.tokenService =  tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenFromHeader = getTokenFromHeader(request);
        boolean tokenValid = tokenService.isTokenValid(tokenFromHeader);

        if(tokenValid) {
            this.authenticate(tokenFromHeader);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(String tokenFromHeader) {
        UUID id = UUID.fromString(tokenService.getTokenId(tokenFromHeader));
        Optional<UserModel> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()) {

            UserModel user = optionalUser.get();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return "invalid";
        }

        return token.substring(7, token.length());
    }

}