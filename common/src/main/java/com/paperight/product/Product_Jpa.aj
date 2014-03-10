package com.paperight.product;

import java.util.List;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.MatchMode;
import org.criteria4jpa.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Configurable;
import org.apache.commons.lang3.StringUtils;

privileged aspect Product_Jpa {
	
	declare @type: Product: @Configurable;
	declare @type: Product: @Entity;
	declare @type: Product: @Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "identifier", "identifierType" }) });
	
	@PersistenceContext
	transient EntityManager Product.entityManager;
	
	public static final EntityManager Product.entityManager() {
		EntityManager em = new Product().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@PrePersist
	private void Product.onCreate() {
		this.setCreatedDate(new DateTime());
		this.setUpdatedDate(this.getCreatedDate());
	}
	
	@PreUpdate
	private void Product.onUpdate() {
		this.setUpdatedDate(new DateTime());
	}

	public static long Product.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Product o", Long.class).getSingleResult();
	}

	public static List<Product> Product.findAll() {
		return entityManager().createQuery("SELECT o FROM Product o", Product.class).getResultList();
	}

	public static Product Product.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Product.class, id);
	}

	public static List<Product> Product.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Product o", Product.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void Product.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void Product.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Product attached = Product.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void Product.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void Product.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Product Product.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Product merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	public static Product Product.findByIdentifierAndIdentifierType(String identifier, String identifierType) {
		if (StringUtils.isBlank(identifier) || StringUtils.isBlank(identifierType) ) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Product.class);
		criteria.add( Restrictions.eq("identifier", identifier) );
		criteria.add( Restrictions.eq("identifierType", identifierType) );
		return (Product) criteria.getSingleResultOrNull();
	}
	
//	@SuppressWarnings("unchecked")
//	public static List<Product> Product.search(Map<String, Object> searchMap) {
//		if (searchMap == null) {
//			return null;
//		}
//		if (searchMap.isEmpty()) {
//			return null;
//		}
//		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Product.class);
//		for( String key: searchMap.keySet() ){
//			criteria.add( Restrictions.eq(key, searchMap.get(key)) );
//		}
//		return (List<Product>) criteria.getResultList();
//	}
//	
	@SuppressWarnings("unchecked")
	public static List<Product> Product.search(String identifier, String title, int pageNumber, int resultSize) {
		if (StringUtils.isBlank(identifier) && StringUtils.isBlank(title) ) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Product.class);
		if (!StringUtils.isBlank(identifier)) {
			criteria.add( Restrictions.eq("identifier", identifier) );
		}
		if (!StringUtils.isBlank(title)) {
			criteria.add( Restrictions.like("title", title, MatchMode.ANYWHERE) );
		}		
		return (List<Product>) criteria.setFirstResult((pageNumber - 1) * resultSize).setMaxResults(resultSize).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Product> Product.findByCompanyId(Long companyId) {
		if (companyId == null) {
			return entityManager().createQuery("SELECT o FROM Product o WHERE o.ownerCompany is null", Product.class).getResultList();
		} else {
			Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Product.class);
			criteria.add( Restrictions.eq("ownerCompany.id", companyId) );
			return (List<Product>) criteria.getResultList();
		}
	}
	
}
