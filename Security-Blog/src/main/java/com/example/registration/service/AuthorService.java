package com.example.registration.service;

import java.util.List;

import com.example.registration.model.Author;

public interface AuthorService {
	List<Author> getAllAuthor();
	void saveAuthor(Author author);
	Author findAuthor(String email);
	Author findAuthorByAuthorId(Long id);
}
