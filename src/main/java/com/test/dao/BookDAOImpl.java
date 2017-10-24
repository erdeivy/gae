package com.test.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.googlecode.objectify.ObjectifyService;
import com.test.data.Book;

public class BookDAOImpl implements IBookDAO {

	private static final Logger LOGGER = Logger.getLogger(BookDAOImpl.class.getName());

	@Override
	public List<Book> list() {
		return ObjectifyService.ofy().load().type(Book.class).list();
	}

	/**
	 * @return list of test beans
	 */
	@Override
	public List<Book> filteredlist(String filter) {
		LOGGER.info("Retrieving list of beans with filter: " + filter);
		List<Long> ids = new ArrayList<Long>();

		Query query = Query.newBuilder().build("tokenized_text" + "=" + filter);
		Index booksDocIndex = getBookDocumentIndex();
		Results<ScoredDocument> books = booksDocIndex.search(query);
		LOGGER.info("Retrieved " + books.getNumberReturned() + " results");
		for (ScoredDocument docs : books) {
			ids.add(Long.valueOf(docs.getId()));
		}

		return new ArrayList<Book>(ObjectifyService.ofy().load().type(Book.class).ids(ids).values());
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
		this.createDocument(book.getId().toString(), book.getName(), book.getAuthor());
	}

	@Override
	public void delete(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("null book object");
		}
		ObjectifyService.ofy().delete().entity(book);
	}

	private void createDocument(String id, String bookName, String authorName) {
		List<String> bookSubstrings = buildAllSubstrings(bookName);
		bookSubstrings.addAll(buildAllSubstrings(authorName));
		String combinedString = combine(bookSubstrings, " ");
		// The input for this looks like "CHR CHRI CHRIS HRI HRIS" etc...
		LOGGER.info("Saving document with id("+id+"), token=("+combinedString+")");
		createBookDocument(id, combinedString);
	}

	private List<String> buildAllSubstrings(String displayName) {
		List<String> substrings = new ArrayList<String>();
		for (String word : displayName.split(" ")) {
			int wordSize = 1;
			while (true) {
				for (int i = 0; i < word.length() - wordSize + 1; i++) {
					substrings.add(word.substring(i, i + wordSize));
				}
				if (wordSize == word.length())
					break;
				wordSize++;
			}
		}
		return substrings;
	}

	private String combine(List<String> strings, String glue) {
		int k = strings.size();
		if (k == 0)
			return null;
		StringBuilder out = new StringBuilder();
		out.append(strings.get(0));
		for (int x = 1; x < k; ++x)
			out.append(glue).append(strings.get(x));
		return out.toString();
	}

	private void createBookDocument(String id, String searchableSubstring) {
		Builder docBuilder = Document.newBuilder().setId(id)
				.addField(Field.newBuilder().setName("tokenized_text").setText(searchableSubstring));

		addDocumentToIndex(docBuilder.build());

	}

	private void addDocumentToIndex(Document document) {

		Index index = getBookDocumentIndex();

		try {
			index.put(document);
		} catch (PutException e) {
			// log.severe("Error putting document in index... trying again.");
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				index.put(document);
			}
		}
	}

	public static Index getBookDocumentIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("BOOK_DOC_INDEX").build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		return index;
	}

}
