package com.paperight.search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class SearchIndexUtils {
	
	@PersistenceContext
	transient EntityManager entityManager;
	
	private SearchIndexUtils() {
		super();
	}
	
	public static final EntityManager entityManager() {
		EntityManager em = new SearchIndexUtils().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	public static void rebuildSearchIndex() throws InterruptedException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager());
		fullTextEntityManager.createIndexer().startAndWait();
	}

}
