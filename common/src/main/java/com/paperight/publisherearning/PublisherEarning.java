package com.paperight.publisherearning;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.currency.Currency;
import com.paperight.currency.CurrencyService;
import com.paperight.licence.Licence;
import com.paperight.user.Company;

@Entity
@Configurable
public class PublisherEarning implements Serializable {
	
	@PersistenceContext
	transient EntityManager entityManager;
	
	private static final long serialVersionUID = -4384794857266770342L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private BigDecimal amount;
	private Licence licence;
	private Company company;
	private PublisherEarningStatus status = PublisherEarningStatus.PENDING;
	private Integer publisherEarningPercent;
	private BigDecimal amountInCurrency;
	private String currencyCode;
	private Currency currency;
	private InvoiceState invoiceState = InvoiceState.NEW;

	public enum InvoiceState {
		NEW("New"),
		DOWNLOADED("Downloaded");
		
		private String displayName;
		
		private InvoiceState(String displayName) {
			setDisplayName(displayName);
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}
	
	@Transient
	@Autowired
	private CurrencyService currencyService;
	
	public static final EntityManager entityManager() {
		EntityManager em = new PublisherEarning().entityManager;
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
		return entityManager().createQuery("SELECT COUNT(o) FROM PublisherEarning o", Long.class).getSingleResult();
	}

	public static List<PublisherEarning> findAll() {
		return entityManager().createQuery("SELECT o FROM PublisherEarning o", PublisherEarning.class).getResultList();
	}

	public static PublisherEarning find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PublisherEarning.class, id);
	}
	
	public static PublisherEarning findByLicenceId(Long licenceId) {
		if (licenceId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherEarning.class);
		criteria.add( Restrictions.eq("licence.id", licenceId) );
		return (PublisherEarning) criteria.getSingleResultOrNull();
	}
	
	public static long countByProductIdAndCompanyId(Long productId, Long companyId) {
		return entityManager().createQuery("SELECT COUNT(o) FROM PublisherEarning o WHERE o.licence.product.id = :productId AND o.company.id = :companyId", Long.class).setParameter("productId", productId).setParameter("companyId", companyId).getSingleResult();
	}
	
	public static List<PublisherEarning> findByCompanyId(Long companyId,  int pageNumber, int resultSize) {
		return findByCompanyId(companyId, null, null, pageNumber, resultSize);
	}
	
	public static List<PublisherEarning> findByCompanyIdAndProductId(Long companyId, Long productId, int pageNumber, int resultSize) {
		return findByCompanyIdAndProductId(companyId, productId, null, null, null, pageNumber, resultSize);
	}
	
	public static List<PublisherEarning> findByCompanyId(Long companyId) {
		return findByCompanyId(companyId, null, null, -1, -1);
	}
	
	public static List<PublisherEarning> findByCompanyId(Long companyId, DateTime fromDate, DateTime toDate) {
		return findByCompanyIdAndProductId(companyId, null, null, fromDate, toDate, -1, -1);
	}
	
	public static List<PublisherEarning> findByCompanyId(Long companyId, DateTime fromDate, DateTime toDate, int pageNumber, int resultSize) {
		return findByCompanyIdAndProductId(companyId, null, null, fromDate, toDate, pageNumber, resultSize);
	}
	
	public static List<PublisherEarning> findByCompanyIdAndStatus(Long companyId, PublisherEarningStatus status, DateTime fromDate, DateTime toDate) {
		return findByCompanyIdAndProductId(companyId, null, status, fromDate, toDate, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	public static List<PublisherEarning> findByCompanyIdAndProductId(Long companyId, Long productId, PublisherEarningStatus status, DateTime fromDate, DateTime toDate, int pageNumber, int resultSize) {
		if (companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherEarning.class);
		criteria.add( Restrictions.eq("company.id", companyId) );
		if (productId != null) {
			criteria.add( Restrictions.eq("licence.product.id", productId) );
		}
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
			return (List<PublisherEarning>) criteria.setFirstResult((pageNumber - 1) * resultSize).setMaxResults(resultSize).getResultList();
		} else {
			return (List<PublisherEarning>) criteria.getResultList();
		}
	}
	
	public static PublisherEarning findByIdAndCompanyId(Long publisherEarningId, Long companyId) {
		if (publisherEarningId == null || companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherEarning.class);
		criteria.add( Restrictions.eq("id", publisherEarningId) );
		criteria.add( Restrictions.eq("company.id", companyId) );
		return (PublisherEarning) criteria.getSingleResultOrNull();
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
			PublisherEarning attached = find(this.getId());
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
	public PublisherEarning merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PublisherEarning merged = this.entityManager.merge(this);
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LICENCE_ID", insertable = true, updatable = false, nullable = false)
	public Licence getLicence() {
		return licence;
	}

	public void setLicence(Licence licence) {
		this.licence = licence;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public PublisherEarningStatus getStatus() {
		return status;
	}

	public void setStatus(PublisherEarningStatus status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID", insertable = true, updatable = false, nullable = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getPublisherEarningPercent() {
		return publisherEarningPercent;
	}

	public void setPublisherEarningPercent(Integer publisherEarningPercent) {
		this.publisherEarningPercent = publisherEarningPercent;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		currency = currencyService.findByCode(currencyCode);
		this.currencyCode = currencyCode;
	}

	@Transient
	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getAmountInCurrency() {
		return amountInCurrency;
	}

	public void setAmountInCurrency(BigDecimal amountInCurrency) {
		this.amountInCurrency = amountInCurrency;
	}
	
	@Transient
	public Integer getCommissionPercent() {
		return 100 - getPublisherEarningPercent();
	}
	
	@Transient
	public BigDecimal getPublisherDollarAmount() {
		return currencyService.percentageValue(getLicence().getDollarsInvoiceValue(), getPublisherEarningPercent()).setScale(2, RoundingMode.HALF_DOWN);
	}
	
	@Transient
	public BigDecimal getCommissionDollarAmount() {
		return getLicence().getDollarsInvoiceValue().subtract(getPublisherDollarAmount());
	}
	
	@Transient
	public BigDecimal getPublisherRandAmount() {
		return currencyService.percentageValue(getLicence().getRandsInvoiceValue(), getPublisherEarningPercent()).setScale(2, RoundingMode.HALF_DOWN);
	}
	
	@Transient
	public BigDecimal getCommissionRandAmount() {
		return getLicence().getRandsInvoiceValue().subtract(getPublisherRandAmount());
	}
	
	@Enumerated(EnumType.STRING)
	public InvoiceState getInvoiceState() {
		return invoiceState;
	}

	public void setInvoiceState(InvoiceState invoiceState) {
		this.invoiceState = invoiceState;
	}

}
