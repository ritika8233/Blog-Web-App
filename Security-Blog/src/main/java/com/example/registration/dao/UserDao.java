package com.example.registration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.registration.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	/*@Query("SELECT a from User a where a.username=:username")
	 User getUserByUserName(@Param("username") String username);
	*/
	User findUserByUsername(String username);
}
