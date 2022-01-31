package com.example.registration.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.registration.model.Blog;

public interface BlogSerivce {
	List<Blog> getAllBlogs();
	void saveBlog(Blog blog);
	Page<Blog> getPage(int pagenumber);
	List<Blog> findBlog(String name);
	Blog findBlogById(Long id);
	void deleteblog(Long id);
}
