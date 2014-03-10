package com.paperight.product;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.FetchType;

import org.apache.commons.lang3.StringUtils;
import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.criteria4jpa.order.Order;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ImportExecution_Jpa {
	
	declare @type: ImportExecution: @Configurable;
	declare @type: ImportExecution: @Entity;
	
	declare @method: public Long ImportExecution.getId() : @Id;
	declare @method: public Long ImportExecution.getId() : @GeneratedValue;
	
	declare @method: public DateTime ImportExecution.getCreatedDate() : @Column(nullable=false, updatable=false);
	declare @method: public DateTime ImportExecution.getCreatedDate() : @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime");
	
	declare @method: public ImportStatus ImportExecution.getStatus() : @Column(nullable = false);
	declare @method: public ImportStatus ImportExecution.getStatus() : @Enumerated(EnumType.STRING);
	
	declare @method: public String ImportExecution.getFilename() : @Column(nullable=false);
	
	declare @method: public public List<ImportError> ImportExecution.getErrors() : @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "importExecution");
			
	@PersistenceContext
	transient EntityManager ImportExecution.entityManager;
	
	public static final EntityManager ImportExecution.entityManager() {
		EntityManager em = new ImportExecution().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@PrePersist
	private void ImportExecution.onCreate() {
		this.setCreatedDate(new DateTime());
	}

	public static long ImportExecution.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM ImportExecution o", Long.class).getSingleResult();
	}

	public static List<ImportExecution> ImportExecution.findAll() {
		return entityManager().createQuery("SELECT o FROM ImportExecution o ORDER BY createdDate DESC", ImportExecution.class).getResultList();
	}

	public static ImportExecution ImportExecution.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(ImportExecution.class, id);
	}

	public static List<ImportExecution> ImportExecution.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ImportExecution o", ImportExecution.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void ImportExecution.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void ImportExecution.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			ImportExecution attached = ImportExecution.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void ImportExecution.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void ImportExecution.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public ImportExecution ImportExecution.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ImportExecution merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	public static ImportExecution ImportExecution.findByFilename(String filename) {
		if (StringUtils.isBlank(filename)) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), ImportExecution.class);
		criteria.add( Restrictions.eq("filename", filename) );
		return (ImportExecution) criteria.getSingleResultOrNull();
	}
	
	@SuppressWarnings("unchecked")
	public static List<ImportExecution> ImportExecution.findByStatus(ImportStatus status) {
		if (status == null ) {
			return findAll();
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), ImportExecution.class);
		criteria.add( Restrictions.eq("status", status) );	
		criteria.addOrder(Order.desc("createdDate"));
		return (List<ImportExecution>) criteria.getResultList();
	}
	
}
