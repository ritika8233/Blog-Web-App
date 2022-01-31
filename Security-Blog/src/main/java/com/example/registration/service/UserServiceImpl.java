package com.example.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.registration.dao.UserDao;
import com.example.registration.model.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserDao userDao;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findbyname(String username) {
		return userDao.findUserByUsername(username);
	}

	@Override
	public void saveuser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		this.userDao.save(user);
		
	}
	
}
