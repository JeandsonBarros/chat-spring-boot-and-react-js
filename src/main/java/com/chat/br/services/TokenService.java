package com.chat.br.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.chat.br.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public static final int TOKEN_EXPIRACAO = 600_000;

    @Value("${jwt.secret}")
    private String secretToken;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(Authentication authentication) {

        User usuario = (User) authentication.getPrincipal();
        var userModel = userRepository.findByEmail(usuario.getUsername());

        String token = JWT.create()
                .withSubject(userModel.get().getUserId().toString())
                //.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO))
                .sign(Algorithm.HMAC512(secretToken));
        return "Bearer "+token;
    }

    public boolean isTokenValid(String token) {
        try {

            String usuario = JWT.require(Algorithm.HMAC512(secretToken)).build().verify(token)
                    .getSubject();

            if (usuario.isEmpty())
                return false;

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public String getTokenId(String token) {

        return JWT.require(Algorithm.HMAC512(secretToken)).build().verify(token)
                .getSubject();
    }

}