package com.example.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.registration.dao.UserDao;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserDao.class)
public class DynamicBlog1Application {

	public static void main(String[] args) {
		SpringApplication.run(DynamicBlog1Application.class, args);
	}

}
