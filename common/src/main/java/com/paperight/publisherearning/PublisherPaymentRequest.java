package com.paperight.publisherearning;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

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

@Entity
@Configurable
public class PublisherPaymentRequest implements Serializable {

	@PersistenceContext
	transient EntityManager entityManager;
	
	private static final long serialVersionUID = 7645672506372962173L;	
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private PublisherPaymentRequestStatus status = PublisherPaymentRequestStatus.PENDING;
	private Set<PublisherEarning> publisherEarnings;
	private Company company;
	private User user;
	
	public static final EntityManager entityManager() {
		EntityManager em = new PublisherPaymentRequest().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	private void onCreate() {
		this.setCreatedDate(new DateTime());
		this.setUpdatedDate(this.getCreatedDate());
	}
	
	@SuppressWarnings("unused")
	@PreUpdate
	private void onUpdate() {
		this.setUpdatedDate(new DateTime());
	}
	
	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM PublisherPaymentRequest o", Long.class).getSingleResult();
	}

	public static List<PublisherPaymentRequest> findAll() {
		return entityManager().createQuery("SELECT o FROM PublisherPaymentRequest o ORDER BY o.createdDate DESC", PublisherPaymentRequest.class).getResultList();
	}

	public static PublisherPaymentRequest find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PublisherPaymentRequest.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public static List<PublisherPaymentRequest> findByStatus(PublisherPaymentRequestStatus status) {
		if (status == null) {
			return findAll();
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherPaymentRequest.class);
		criteria.add( Restrictions.eq("status", status) );
		criteria.addOrder( Order.desc("updatedDate") );
		return (List<PublisherPaymentRequest>) criteria.getResultList();
	}
	
	public static List<PublisherPaymentRequest> findByCompanyId(Long companyId) {
		return findByCompanyIdAndStatus(companyId, null, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public static List<PublisherPaymentRequest> findByCompanyIdAndStatus(Long companyId, PublisherPaymentRequestStatus status) {
		return findByCompanyIdAndStatus(companyId, status, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public static List<PublisherPaymentRequest> findByCompanyIdAndStatus(Long companyId, PublisherPaymentRequestStatus status, DateTime fromDate, DateTime toDate) {
		if (companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherPaymentRequest.class);
		criteria.add( Restrictions.eq("company.id", companyId) );
		if (status != null) {
			criteria.add( Restrictions.eq("status", status) );
		}
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("updatedDate", fromDate, toDate));
		} else if (fromDate != null) {
			criteria.add(Restrictions.gt("updatedDate", fromDate));
		} else if (toDate != null) {
			criteria.add(Restrictions.lt("updatedDate", toDate));
		}
		criteria.addOrder( Order.desc("updatedDate") );
		return (List<PublisherPaymentRequest>) criteria.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public static PublisherPaymentRequest findByCompanyId(Long id, Long companyId) {
		if (companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherPaymentRequest.class);
		criteria.add( Restrictions.eq("id", id) );
		criteria.add( Restrictions.eq("company.id", companyId) );
		return (PublisherPaymentRequest) criteria.getSingleResultOrNull();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			PublisherPaymentRequest attached = find(this.getId());
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public PublisherPaymentRequest merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PublisherPaymentRequest merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable=false, updatable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}

	private void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(nullable=false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getUpdatedDate() {
		return updatedDate;
	}

	private void setUpdatedDate(DateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	public Set<PublisherEarning> getPublisherEarnings() {
		return publisherEarnings;
	}

	public void setPublisherEarnings(Set<PublisherEarning> publisherEarnings) {
		this.publisherEarnings = publisherEarnings;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public PublisherPaymentRequestStatus getStatus() {
		return status;
	}

	public void setStatus(PublisherPaymentRequestStatus status) {
		this.status = status;
	}
	
	@Transient
	public BigDecimal getAmount() {
		BigDecimal result = new BigDecimal(0);
		for (PublisherEarning publisherEarning : getPublisherEarnings()) {
			result = result.add(publisherEarning.getAmount());
		}
		return result;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID", insertable = true, updatable = false, nullable = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = true, updatable = false, nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Transient
	public BigDecimal getRandsAmount() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarning publisherEarning : publisherEarnings) {
			result = result.add(publisherEarning.getPublisherRandAmount());
		}
		return result.setScale(2, RoundingMode.HALF_DOWN);
	}
	
	@Transient
	public BigDecimal getDollarsAmount() {
		BigDecimal result = BigDecimal.valueOf(0);
		for (PublisherEarning publisherEarning : publisherEarnings) {
			result = result.add(publisherEarning.getPublisherDollarAmount());
		}
		return result.setScale(2, RoundingMode.HALF_DOWN);
	}

}
