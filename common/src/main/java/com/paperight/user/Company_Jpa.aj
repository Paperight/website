package com.paperight.user;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.MatchMode;
import org.criteria4jpa.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Company_Jpa {
	
	declare @type: Company: @Configurable;
	declare @type: Company: @Entity;
	
	@PersistenceContext
	transient EntityManager Company.entityManager;
	
	public static final EntityManager Company.entityManager() {
		EntityManager em = new Company().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	
	@PrePersist
	private void Company.onCreate() {
		this.setCreatedDate(new DateTime());
		this.setUpdatedDate(this.getCreatedDate());
	}
	
	@PreUpdate
	private void Company.onUpdate() {
		this.setUpdatedDate(new DateTime());
	}
	
	public static long Company.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Company o", Long.class).getSingleResult();
	}

	public static List<Company> Company.findAll() {
		return entityManager().createQuery("SELECT o FROM Company o", Company.class).getResultList();
	}

	public static List<Company> Company.findByParentCompany(long companyId) {
		return entityManager()
			.createQuery("SELECT o FROM Company o WHERE o.parentCompany.id = :companyId", Company.class)
			.setParameter("companyId", companyId)
			.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Company> Company.search(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Company.class);
		if (!StringUtils.isBlank(name)) {
			criteria.add( Restrictions.like("name", name, MatchMode.ANYWHERE) );
		}		
		return (List<Company>) criteria.getResultList();
	}

	public static Company Company.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Company.class, id);
	}

	public static List<Company> Company.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Company o", Company.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}
	
	public static List<Company> Company.findAllActiveByRole(Role role) {
		return entityManager()
				.createQuery("SELECT o FROM Company o WHERE o.id IN (SELECT DISTINCT(u.company) FROM User u WHERE :role in elements(u.roles) AND u.deleted = :userDeleted AND u.closed = :userClosed AND u.enabled = :userEnabled) AND o.closed = :companyClosed AND o.deleted = :companyDeleted AND o.disabled = :companyDisabled", Company.class)
				.setParameter("role", role.name())
				.setParameter("userDeleted", false)
				.setParameter("userClosed", false)
				.setParameter("userEnabled", true)
				.setParameter("companyClosed", false)
				.setParameter("companyDeleted", false)
				.setParameter("companyDisabled", false)
				.getResultList();		
	}
	//JOIN u.roles r 

	@Transactional
	public void Company.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void Company.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Company attached = Company.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void Company.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void Company.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Company Company.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Company merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

}
