package com.example.url.shortener.service;

import com.example.url.shortener.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by natal on 30-May-17.
 */
//@Transactional(readOnly = true)
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        accountService.addAccount("1");
        Account account = this.accountService.getAccountByLogin(id)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username/password"));

        Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(account.getId(), account.getPassword(), true, true, true, true, authorities);
    }
}
