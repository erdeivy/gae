package com.test.dao;

import java.util.List;

import com.test.data.Book;

public interface IBookDAO {
	public List<Book> list();
	
	public List<Book> filteredlist(String filter);

	public Book get(Long id);

	public void save(Book book);

	public void delete(Book book);
}
