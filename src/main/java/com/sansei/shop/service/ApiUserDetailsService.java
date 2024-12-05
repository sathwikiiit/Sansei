package com.sansei.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sansei.shop.model.ApiUser;
import com.sansei.shop.repository.ApiUserRepository;

@Service
public class ApiUserDetailsService implements UserDetailsService {
    
    @Autowired
    private ApiUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser apiUser = apiUserRepository.findByUsername(username);
        if (apiUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return (UserDetails) apiUser;
    }


}
