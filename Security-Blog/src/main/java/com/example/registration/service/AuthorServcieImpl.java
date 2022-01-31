package com.example.registration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.registration.dao.AuthorDao;
import com.example.registration.model.Author;

@Service
public class AuthorServcieImpl implements AuthorService{

	@Autowired
	private AuthorDao authorDao; 
	
	@Override
	public List<Author> getAllAuthor() {
		return authorDao.findAll();
	}

	@Override
	public void saveAuthor(Author author) {
		this.authorDao.save(author);
	}

	@Override
	public Author findAuthor(String email) {
		List<Author> author = getAllAuthor();
		
		Author a1 = null;
		for(Author auth: author) {
			if(auth.getEmail().equals(email)) {
				a1 = auth;
				break;
			}
		}
		return a1;
	}

	@Override
	public Author findAuthorByAuthorId(Long id) {
		return authorDao.getById(id);
	}

}
