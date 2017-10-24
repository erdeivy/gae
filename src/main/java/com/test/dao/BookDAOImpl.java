package com.test.dao;

import java.util.List;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

import com.googlecode.objectify.ObjectifyService;
import com.test.data.Book;

public class BookDAOImpl implements IBookDAO {

	@Override
	public List<Book> list() {
		return ObjectifyService.ofy().load().type(Book.class).list();
	}

	@Override
	public List<Book> filteredlist(String filter) {
//		Query q = new Query("Person").setFilter(new FilterPredicate("lastName", FilterOperator.EQUAL, targetLastName));
//
//		PreparedQuery pq = datastore.prepare(q);
//		Entity result = pq.asSingleEntity();	

		return ObjectifyService.ofy().load().type(Book.class).filter("name >=", filter).filter("name <", filter + "\uFFFD").list();
	}

	@Override
	public Book get(Long id) {
		return ObjectifyService.ofy().load().type(Book.class).id(id).now();
	}

	@Override
	public void save(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("null book object");
		}
		ObjectifyService.ofy().save().entity(book).now();
	}

	@Override
	public void delete(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("null book object");
		}
		ObjectifyService.ofy().delete().entity(book);
	}

}
