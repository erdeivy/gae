package com.test.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.test.dao.BookDAOImpl;
import com.test.dao.IBookDAO;
import com.test.data.Book;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("/book")
@Produces("application/json;charset=utf-8")
@Api(value = "Books", description = "Books API")
public class BookResource {
	private IBookDAO bookDAO;

	public BookResource() {
		this.bookDAO = new BookDAOImpl();
	}
	
	
	public BookResource(IBookDAO bookDAO) {
		this.bookDAO = bookDAO;
	}

	@GET
	@ApiOperation("Get all matching books")
	public Response list(@QueryParam("filter") String filter) {
		return Response.ok(filter == null ? this.bookDAO.list() : this.bookDAO.filteredlist(filter)).build();
	}

	@GET
	@Path("/{id}")
	@ApiOperation("Get one book")
	public Response get(@PathParam("id") Long id) {
		Book book = this.bookDAO.get(id);
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(book).build();
	}
	
	@POST
	@Consumes("application/json;charset=utf-8")
	@ApiOperation("Save a new book")
	public Response save(Book book) {
		this.bookDAO.save(book);
		return Response.ok().build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation("Delete an existing book")
	public Response delete(@PathParam("id") Long id) {
		Book book = this.bookDAO.get(id);
		if (book == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		this.bookDAO.delete(book);
		return Response.ok().build();
	}
}
