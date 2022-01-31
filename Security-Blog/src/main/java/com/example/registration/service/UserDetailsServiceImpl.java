package com.example.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.registration.dao.UserDao;
import com.example.registration.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
    private UserDao userDao;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(username);
         
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new MyUserDetails(user);
    }
}
