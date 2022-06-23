package com.checkers.web.service;

import com.checkers.web.domain.CustomUserDetails;
import com.checkers.web.domain.User;
import com.checkers.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
        //("authService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user==null) {
            throw new UsernameNotFoundException("UserType not found");
        }
        return new CustomUserDetails(user);
    }

    public void register(String username, String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPassword = bCryptPasswordEncoder.encode(password);
        userRepository.save(new User(username, hashPassword, "USER"));
    }
}
