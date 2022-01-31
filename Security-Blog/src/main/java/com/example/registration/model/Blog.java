package com.example.registration.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Blog {
	@Column
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String data;
	
	@Column(nullable = false)
	private Date date;
	
	@Column(nullable = false)
	@Lob
	private String myimage;
	
	public String getMyimage() {
		return myimage;
	}

	public void setMyimage(String myimage) {
		this.myimage = myimage;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	private Author author;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

}
