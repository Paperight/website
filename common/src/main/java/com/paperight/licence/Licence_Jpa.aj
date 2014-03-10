package com.paperight.licence;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;

import org.apache.commons.lang3.StringUtils;
import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.criteria4jpa.order.Order;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Configurable;

privileged aspect Licence_Jpa {
	
	declare @type: Licence: @Configurable;
	declare @type: Licence: @Entity;
	
	@PersistenceContext
	transient EntityManager Licence.entityManager;
	
	public static final EntityManager Licence.entityManager() {
		EntityManager em = new Licence().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@PrePersist
	private void Licence.onCreate() {
		this.setCreatedDate(new DateTime());
	}

	public static long Licence.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Licence o", Long.class).getSingleResult();
	}

	public static List<Licence> Licence.findAll() {
		return entityManager().createQuery("SELECT o FROM Licence o", Licence.class).getResultList();
	}

	public static Licence Licence.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Licence.class, id);
	}

	public static List<Licence> Licence.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Licence o", Licence.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void Licence.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void Licence.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Licence attached = Licence.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void Licence.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void Licence.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Licence Licence.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Licence merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Licence> Licence.findByProductId(Long productId) {
		if (productId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Licence.class);
		criteria.add( Restrictions.eq("product.id", productId) );
		return (List<Licence>) criteria.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Licence> Licence.findByProductIdAndCompanyId(Long productId, Long companyId, int pageNumber, int resultSize) {
		if (productId == null || companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Licence.class);
		criteria.add( Restrictions.eq("product.id", productId) );
		criteria.add( Restrictions.eq("company.id", companyId) );
		criteria.addOrder(Order.desc("createdDate"));
		return (List<Licence>) criteria.setFirstResult((pageNumber - 1) * resultSize).setMaxResults(resultSize).getResultList();
	}
	
	public static long Licence.countByProductIdAndCompanyId(Long productId, Long companyId) {
		return entityManager().createQuery("SELECT COUNT(o) FROM Licence o WHERE o.product.id = :productId AND o.company.id = :companyId", Long.class).setParameter("productId", productId).setParameter("companyId", companyId).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Licence> Licence.findByCustomerDetails(Long companyId, String customerFirstName, String customerLastName, String customerPhoneNumber) {
		if (companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Licence.class);
		criteria.add( Restrictions.eq("company.id", companyId) );
		if (!StringUtils.isBlank(customerFirstName)) {
			criteria.add( Restrictions.like("customerName", "%" + customerFirstName + "%") );
		}
		if (!StringUtils.isBlank(customerLastName)) {
			criteria.add( Restrictions.like("customerLastName", "%" + customerLastName + "%") );
		}
		if (!StringUtils.isBlank(customerPhoneNumber)) {
			criteria.add( Restrictions.like("customerPhoneNumber", "%" + customerPhoneNumber + "%") );
		}
		criteria.addOrder(Order.desc("createdDate"));
		return (List<Licence>) criteria.getResultList();
	}
		
	public static Licence Licence.findByIdAndCompanyId(Long licenceId, Long companyId) {
		if (licenceId == null || companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Licence.class);
		criteria.add( Restrictions.eq("id", licenceId) );
		criteria.add( Restrictions.eq("company.id", companyId) );
		return (Licence) criteria.getSingleResultOrNull();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Licence> Licence.findByDateRange(DateTime fromDate, DateTime toDate) {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Licence.class);
		criteria.add(Restrictions.between("createdDate", fromDate, toDate));
		return (List<Licence>) criteria.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Licence> Licence.findByCompanyId(Long companyId, int pageNumber, int resultSize) {
		return findByCompanyIdAndStatus(companyId, null, null, null, pageNumber, resultSize);
	}
	
	public static List<Licence> Licence.findByCompanyId(Long companyId, DateTime fromDate, DateTime toDate) {
		return findByCompanyIdAndStatus(companyId, null, fromDate, toDate, -1, -1);
	}
		
	public static List<Licence> Licence.findByCompanyId(Long companyId, DateTime fromDate, DateTime toDate, int pageNumber, int resultSize) {
		return findByCompanyIdAndStatus(companyId, null, fromDate, toDate, pageNumber, resultSize);
	}
	
	public static List<Licence> Licence.findByCompanyIdAndStatus(Long companyId, LicenceStatus status, DateTime fromDate, DateTime toDate) {
		return findByCompanyIdAndStatus(companyId, status, fromDate, toDate, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Licence> Licence.findByCompanyIdAndStatus(Long companyId, LicenceStatus status, DateTime fromDate, DateTime toDate, int pageNumber, int resultSize) {
		if (companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), Licence.class);
		criteria.add( Restrictions.eq("company.id", companyId) );
		if (status != null) {
			criteria.add( Restrictions.eq("status", status) );
		}
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("createdDate", fromDate, toDate));
		} else if (fromDate != null) {
			criteria.add(Restrictions.gt("createdDate", fromDate));
		} else if (toDate != null) {
			criteria.add(Restrictions.lt("createdDate", toDate));
		}
		criteria.addOrder(Order.desc("createdDate"));
		if (pageNumber > -1 && resultSize > -1) {
			return (List<Licence>) criteria.setFirstResult((pageNumber - 1) * resultSize).setMaxResults(resultSize).getResultList();
		} else {
			return (List<Licence>) criteria.getResultList();
		}
	}
	
}
