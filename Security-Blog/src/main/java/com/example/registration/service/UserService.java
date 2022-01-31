package com.example.registration.service;

import com.example.registration.model.User;

public interface UserService {
	User findbyname(String username);
	void saveuser(User user);
}
