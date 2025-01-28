package com.study.userity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.study.userity.repository.UserRepository;
import com.study.userity.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;
    
}
