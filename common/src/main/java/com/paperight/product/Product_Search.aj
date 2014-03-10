package com.paperight.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause;
import org.hibernate.search.SearchException;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;

import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Disjunction;
import org.criteria4jpa.criterion.MatchMode;
import org.criteria4jpa.criterion.Restrictions;
import org.criteria4jpa.order.Order;

import com.paperight.search.ProductSearch;
import com.paperight.search.SearchResult;

privileged aspect Product_Search {
	
	declare @type: Product: @Indexed;
	
	declare @method :public Long Product.getId() : @DocumentId;
	declare @method :public String Product.getTitle() : @Field(index=Index.YES, analyze=Analyze.NO, store=Store.NO); 
	declare @method :public String Product.getTokenTitle() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getTokenTitle() : @Transient;
	declare @method :public String Product.getSubTitle() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getAlternativeTitle() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getIdentifier() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getPrimaryCreators() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getSecondaryCreators() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getSubjectArea() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public String Product.getPublisher() : @Field(index=Index.YES, analyze=Analyze.NO, store=Store.NO);
	declare @method :public String Product.getTags() : @Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO);
	declare @method :public boolean Product.isDisabled() : @Field(index=Index.YES, analyze=Analyze.NO, store=Store.NO);
	declare @method :public boolean Product.isPublisherInactive() : @Field(index=Index.YES, analyze=Analyze.NO, store=Store.NO);
	//declare @method :public String Product.getTags() : @Analyzer(definition = "customanalyzer");
	
	
	public String Product.getTokenTitle() {
		return getTitle();
	}
	

	@SuppressWarnings("unchecked")
	public static List<Product> Product.search(String searchTerms) throws Exception {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(Product.entityManager());

		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity( Product.class ).get();
		org.apache.lucene.search.Query query = qb
		  .keyword()
		  .onFields("tokenTitle", "subTitle", "alternativeTitle", "identifier", "primaryCreators", "secondaryCreators", "subjectArea", "tags")
		  .matching(searchTerms)
		  .createQuery();

		TermQuery disabledClause = new TermQuery(new Term("disabled", "false"));
		TermQuery publisherInactiveClause = new TermQuery(new Term("publisherInactive", "false"));
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(query, BooleanClause.Occur.MUST);
		booleanQuery.add(disabledClause, BooleanClause.Occur.MUST);
		booleanQuery.add(publisherInactiveClause, BooleanClause.Occur.MUST);
		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(booleanQuery, Product.class);

		// execute search
		return (List<Product>) persistenceQuery.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static SearchResult<Product> Product.search(String searchTerms, int pageNumber, int resultSize) throws InvalidSearchException, Exception {
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(Product.entityManager());

		
		// create native Lucene query using the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommended though
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity( Product.class ).get();
		org.apache.lucene.search.Query query;
		Sort sort = null;
		if (!StringUtils.isBlank(searchTerms)) {
			try {
				query = qb
						.bool().should(qb
				  .keyword()
				  .onFields("tokenTitle", "subTitle", "alternativeTitle", "identifier", "primaryCreators", "secondaryCreators", "subjectArea", "tags")
				  .matching(searchTerms)
				  .createQuery())
				  .should(qb
				  .keyword()
				  .wildcard()
				  .onField("tokenTitle").andField("subTitle").andField("alternativeTitle").andField("identifier").andField("primaryCreators").andField("secondaryCreators").andField("subjectArea").andField("tags")
				  .matching("*" + StringUtils.lowerCase(searchTerms) + "*")
				  .createQuery())
//				  .should(qb
//				  .keyword()
//				  .onFields("disabled", "publisherInactive")
//				  .matching(false)
//				  .createQuery())
				  .createQuery();
			} catch (SearchException searchException) {
				if (StringUtils.startsWithIgnoreCase(searchException.getMessage(), "Try to search with an empty string: title")) {
					throw new InvalidSearchException();
				} else {
					throw searchException;
				}
			}
		} else {
			query = new MatchAllDocsQuery();
			sort = new Sort(new SortField("title", SortField.STRING));			
		}
		TermQuery disabledClause = new TermQuery(new Term("disabled", "false"));
		TermQuery publisherInactiveClause = new TermQuery(new Term("publisherInactive", "false"));
		BooleanQuery booleanQuery = new BooleanQuery();
		booleanQuery.add(query, BooleanClause.Occur.MUST);
		booleanQuery.add(disabledClause, BooleanClause.Occur.MUST);
		booleanQuery.add(publisherInactiveClause, BooleanClause.Occur.MUST);
		
		// wrap Lucene query in a javax.persistence.Query
		FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(booleanQuery, Product.class);
		
//		FacetingRequest publisherFacetingRequest = qb.facet()
//			    .name( "publisherFaceting" )
//			    .onField( "publisher")
//			    .discrete()
//			    .orderedBy( FacetSortOrder.COUNT_DESC )
//			    .includeZeroCounts( false )
//			    .maxFacetCount( 1 )
//			    .createFacetingRequest();
//		
//		FacetManager facetManager = fullTextQuery.getFacetManager();
//		facetManager.enableFaceting( publisherFacetingRequest );

		
		List<Product> products = fullTextQuery.setSort(sort)
				.setFirstResult( (pageNumber - 1) * resultSize )
				.setMaxResults( resultSize )
				.getResultList();
		
		int totalResults = fullTextQuery.getResultSize();
		
//		List<Facet> facets = facetManager.getFacets( "publisherFaceting" );
		
		SearchResult<Product> searchResult = new SearchResult<Product>();
		searchResult.setItems(products);
		searchResult.setPageNumber(pageNumber);
		searchResult.setResultSize(resultSize);
		searchResult.setTotalResults(totalResults);
		return searchResult;
	}
	
	@SuppressWarnings("unchecked")
	public static SearchResult<Product> Product.search(ProductSearch productSearch, int pageNumber, int resultSize) throws Exception {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Product.class);
		
		List<String> titles = splitQuery(productSearch.getTitle());
		if (!titles.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String title : titles) {
				disjunction.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}
		
		List<String> subTitles = splitQuery(productSearch.getSubTitle());
		if (!subTitles.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String subTitle : subTitles) {
				disjunction.add(Restrictions.ilike("subTitle", subTitle, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}
		
		List<String> alternativeTitles = splitQuery(productSearch.getAlternativeTitle());
		if (!subTitles.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String alternativeTitle : alternativeTitles) {
				disjunction.add(Restrictions.ilike("alternativeTitle", alternativeTitle, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}
		
		List<String> publishers = splitQuery(productSearch.getPublisher());
		if (!publishers.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String publisher : publishers) {
				disjunction.add(Restrictions.ilike("publisher", publisher, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}
		
		List<String> creators = splitQuery(productSearch.getCreator());
		if (!creators.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String creator : creators) {
				disjunction.add(Restrictions.ilike("primaryCreators", creator, MatchMode.ANYWHERE));
				disjunction.add(Restrictions.ilike("secondaryCreators", creator, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}
		
		List<String> subjectAreas = splitQuery(productSearch.getSubjectArea());
		if (!subjectAreas.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String subjectArea : subjectAreas) {
				disjunction.add(Restrictions.ilike("subjectArea", subjectArea, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}

		List<String> tags = splitQuery(productSearch.getTags());
		if (!tags.isEmpty()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String tag : tags) {
				disjunction.add(Restrictions.ilike("tags", tag, MatchMode.ANYWHERE));
			}
			criteria.add(disjunction);
		}
		
		criteria.add(Restrictions.eq("disabled", false));
		criteria.add(Restrictions.eq("publisherInactive", false));
		
		criteria.addOrder(Order.asc("title"));
		
		int totalResults = criteria.getResultList().size();
		
		List<Product> products = criteria.setFirstResult( (pageNumber - 1) * resultSize )
				.setMaxResults(resultSize)
				.getResultList();

		SearchResult<Product> searchResult = new SearchResult<Product>();
		searchResult.setItems(products);
		searchResult.setPageNumber(pageNumber);
		searchResult.setResultSize(resultSize);
		searchResult.setTotalResults(totalResults);
		return searchResult;
	}
	
	public static List<String> splitQuery(String commaSeparatedString) {
		List<String> list = new ArrayList<String>();
		if (!StringUtils.isBlank(commaSeparatedString)) {
			String strings[] = commaSeparatedString.split(",");
			for (String string : strings) {
				list.add(StringUtils.trim(string));
			}
		}
		return list;
	}

	
}
