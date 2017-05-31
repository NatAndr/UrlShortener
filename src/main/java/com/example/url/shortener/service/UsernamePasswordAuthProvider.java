package com.example.url.shortener.service;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by natal on 30-May-17.
 */
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    public UsernamePasswordAuthProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        User principal = (User) authentication.getPrincipal();


        String login = token.getName();
        UserDetails user = userDetailsService.loadUserByUsername(login);
        String password = user.getPassword();
        String tokenPassword = (String) token.getCredentials();
//        if (!passwordEncoder.matches(tokenPassword, password)) {
        if (!tokenPassword.equals(password)) {
            throw new BadCredentialsException("Invalid username/password");
        }
        if (!user.isAccountNonLocked()){
            throw new LockedException("User is locked");
        }
        if (!user.isCredentialsNonExpired()){
            throw new CredentialsExpiredException("User credentials are expired");
        }
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
