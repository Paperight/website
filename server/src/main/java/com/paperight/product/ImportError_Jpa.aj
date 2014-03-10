package com.paperight.product;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ImportError_Jpa {
	
	declare @type: ImportError: @Configurable;
	declare @type: ImportError: @Entity;
	
	declare @method: public Long ImportError.getId() : @Id;
	declare @method: public Long ImportError.getId() : @GeneratedValue;
	
	declare @method: public DateTime ImportError.getCreatedDate() : @Column(nullable=false, updatable=false);
	declare @method: public DateTime ImportError.getCreatedDate() : @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime");
	
	declare @method: public ImportExecution ImportError.getImportExecution() : @ManyToOne(fetch = FetchType.LAZY);
	declare @method: public ImportExecution ImportError.getImportExecution() : @JoinColumn(name = "IMPORT_EXECUTION_ID", insertable = true, updatable = false, nullable = false);
	
	declare @method: public String ImportError.getError() : @Column(length = 5000, nullable = true);
	
	declare @method: public String ImportError.getLineContent() : @Column(length = 5000, nullable = true);
		
	@PersistenceContext
	transient EntityManager ImportError.entityManager;
	
	public static final EntityManager ImportError.entityManager() {
		EntityManager em = new ImportError().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@PrePersist
	private void ImportError.onCreate() {
		this.setCreatedDate(new DateTime());
	}
	
	public static long ImportError.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM ImportError o", Long.class).getSingleResult();
	}

	public static List<ImportError> ImportError.findAll() {
		return entityManager().createQuery("SELECT o FROM ImportError o", ImportError.class).getResultList();
	}

	public static ImportError ImportError.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(ImportError.class, id);
	}

	public static List<ImportError> ImportError.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ImportError o", ImportError.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void ImportError.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void ImportError.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			ImportError attached = ImportError.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void ImportError.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void ImportError.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public ImportError ImportError.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ImportError merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
}
