package com.example.registration.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.registration.dao.BlogDao;
import com.example.registration.model.Author;
import com.example.registration.model.Blog;

@Service
public class BlogServiceImpl implements BlogSerivce{

	@Autowired
	private BlogDao blogDao; 
	
	@Override
	public List<Blog> getAllBlogs() {
		return blogDao.findAll();
	}

	@Override
	public Page<Blog> getPage(int pagenumber) {
		Pageable pageable = PageRequest.of(pagenumber-1, 4);
		return blogDao.findAll(pageable);
	}
	
	@Override
	public void saveBlog(Blog blog) {
		this.blogDao.save(blog);
	}

	@Override
	public List<Blog> findBlog(String name) {
		List<Blog> allBlogs = getAllBlogs();
		List<Blog> b1 = new ArrayList<>();
		for(Blog blog: allBlogs) {
			Author auth = blog.getAuthor();
			System.out.println("Hiiii   " + auth.getName());
			if(auth.getName().equals(name)) {
				b1.add(blog);
			}
		}
		return b1;
	}

	@Override
	public Blog findBlogById(Long id) {
		return blogDao.getById(id);
	}

	@Override
	public void deleteblog(Long id) {
		blogDao.delete(blogDao.getById(id));
		
	}

}
