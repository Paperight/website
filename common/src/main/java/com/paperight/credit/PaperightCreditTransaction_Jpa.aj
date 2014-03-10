package com.paperight.credit;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
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

import com.paperight.user.Company;
import com.paperight.user.User;

privileged aspect PaperightCreditTransaction_Jpa {
	
	declare @type: PaperightCreditTransaction: @Configurable;
	declare @type: PaperightCreditTransaction: @Entity;
	
	declare @method: public Long PaperightCreditTransaction.getId() : @Id;
	declare @method: public Long PaperightCreditTransaction.getId() : @GeneratedValue;
	
	declare @method: public DateTime PaperightCreditTransaction.getCreatedDate() : @Column(nullable=false, updatable=false);
	declare @method: public DateTime PaperightCreditTransaction.getCreatedDate() : @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime");
	
	declare @method: public DateTime PaperightCreditTransaction.getUpdatedDate() : @Column(nullable=false);
	declare @method: public DateTime PaperightCreditTransaction.getUpdatedDate() : @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime");
	
	declare @method: public String PaperightCreditTransaction.getTransactionReference() : @Column(nullable=false);
	
	declare @method: public PaperightCreditTransactionStatus PaperightCreditTransaction.getStatus() : @Column(nullable = false);
	declare @method: public PaperightCreditTransactionStatus PaperightCreditTransaction.getStatus() : @Enumerated(EnumType.STRING);
	
	declare @method: public BigDecimal PaperightCreditTransaction.getNumberOfCredits() : @Column(nullable=false);
	
	declare @method: public BigDecimal PaperightCreditTransaction.getAmount() : @Column(nullable=false);
	
	declare @method: public String PaperightCreditTransaction.getCurrency() : @Column(nullable=false);
	
	declare @method: public Company PaperightCreditTransaction.getCompany() : @ManyToOne(fetch = FetchType.LAZY);
	declare @method: public Company PaperightCreditTransaction.getCompany() : @JoinColumn(name = "COMPANY_ID", insertable = true, updatable = false, nullable = false);	
		
	declare @method: public User PaperightCreditTransaction.getUser() : @ManyToOne(fetch = FetchType.LAZY);
	declare @method: public User PaperightCreditTransaction.getUser() : @JoinColumn(name = "USER_ID", insertable = true, updatable = false, nullable = false);
	
	declare @method: public PaymentMethod PaperightCreditTransaction.getPaymentMethod() : @Column(nullable = false);
	declare @method: public PaymentMethod PaperightCreditTransaction.getPaymentMethod() : @Enumerated(EnumType.STRING);
		
	@PersistenceContext
	transient EntityManager PaperightCreditTransaction.entityManager;
	
	public static final EntityManager PaperightCreditTransaction.entityManager() {
		EntityManager em = new PaperightCreditTransaction().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@PrePersist
	private void PaperightCreditTransaction.onCreate() {
		this.setCreatedDate(new DateTime());
		this.setUpdatedDate(this.getCreatedDate());
		if (StringUtils.isBlank(this.getTransactionReference())) {
			this.setTransactionReference(getCompany().getId() + DateTime.now().toString("yyyyMMdd"));
		}
	}
	
	@PreUpdate
	private void PaperightCreditTransaction.onUpdate() {
		this.setUpdatedDate(new DateTime());
	}

	public static long PaperightCreditTransaction.count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM PaperightCreditTransaction o", Long.class).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public static List<PaperightCreditTransaction> PaperightCreditTransaction.findAll() {
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PaperightCreditTransaction.class);
		criteria.addOrder(Order.desc("createdDate"));
		return (List<PaperightCreditTransaction>) criteria.getResultList();
	}

	public static PaperightCreditTransaction PaperightCreditTransaction.find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PaperightCreditTransaction.class, id);
	}

	public static List<PaperightCreditTransaction> PaperightCreditTransaction.findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM PaperightCreditTransaction o", PaperightCreditTransaction.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void PaperightCreditTransaction.persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void PaperightCreditTransaction.remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			PaperightCreditTransaction attached = PaperightCreditTransaction.find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void PaperightCreditTransaction.flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void PaperightCreditTransaction.clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public PaperightCreditTransaction PaperightCreditTransaction.merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PaperightCreditTransaction merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@SuppressWarnings("unchecked")
	public static List<PaperightCreditTransaction> PaperightCreditTransaction.search(Long id, PaperightCreditTransactionStatus status) {
		if (id == null && status == null ) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PaperightCreditTransaction.class);
		if (id != null) {
			criteria.add( Restrictions.eq("id", id) );
		}
		if (status != null) {
			criteria.add( Restrictions.eq("status", status) );
		}			
		return (List<PaperightCreditTransaction>) criteria.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<PaperightCreditTransaction> PaperightCreditTransaction.findByCompanyId(Long companyId) {
		if (companyId == null ) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PaperightCreditTransaction.class);
		if (companyId != null) {
			criteria.add( Restrictions.eq("company.id", companyId) );
			criteria.addOrder(Order.desc("createdDate"));
		}			
		return (List<PaperightCreditTransaction>) criteria.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static List<PaperightCreditTransaction> PaperightCreditTransaction.findByStatus(PaperightCreditTransactionStatus status) {
		if (status == null ) {
			return findAll();
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PaperightCreditTransaction.class);
		criteria.add( Restrictions.eq("status", status) );	
		criteria.addOrder(Order.desc("createdDate"));
		return (List<PaperightCreditTransaction>) criteria.getResultList();
	}
	
	public static PaperightCreditTransaction PaperightCreditTransaction.findByTransactionReference(String transactionReference) {
		if (StringUtils.isBlank(transactionReference)) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PaperightCreditTransaction.class);
		criteria.add( Restrictions.eq("transactionReference", transactionReference) );			
		return (PaperightCreditTransaction) criteria.getSingleResultOrNull();
	}
	
}
