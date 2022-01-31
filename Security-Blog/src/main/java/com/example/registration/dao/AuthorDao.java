package com.example.registration.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.registration.model.Author;

public interface AuthorDao extends JpaRepository<Author, Long>{

}
