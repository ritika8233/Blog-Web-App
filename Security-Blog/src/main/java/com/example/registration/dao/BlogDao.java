package com.example.registration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.registration.model.Blog;

@Repository
public interface BlogDao extends JpaRepository<Blog, Long>{

}
