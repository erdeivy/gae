package com.test.data;

import java.util.Comparator;

public class BookComparator implements Comparator<Book> {

	@Override
	public int compare(Book o1, Book o2) {
		return o1 != null && o2 != null && o1.getAuthor() != null && o2.getAuthor() != null
				? o1.getAuthor().compareTo(o2.getAuthor())
				: null;
	}

}
