package com.chat.br.security;

import com.chat.br.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional
@Service
public class UserDetailsServiceImplement implements UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userModel = userRepository.findByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException("User Not Found with username:" + username)
        );

        return new User(userModel.getUsername(), userModel.getPassword(), true, true, true,true, userModel.getAuthorities());
    }

}
