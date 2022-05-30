package com.cts.pod4.authservice.service;

import com.cts.pod4.authservice.model.JwtUserDetails;
import com.cts.pod4.authservice.model.User;
import com.cts.pod4.authservice.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private PasswordEncoder bcryptEncoder;w

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User not found with username: " + username);

        org.springframework.security.core.userdetails.User u = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

        return new JwtUserDetails(user);
    }
}
