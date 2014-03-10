package com.paperight.user;

import java.io.Serializable;
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

import org.criteria4jpa.Criteria;
import org.criteria4jpa.CriteriaUtils;
import org.criteria4jpa.criterion.Restrictions;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class PublisherPaymentDetails implements Serializable {
	
	@PersistenceContext
	transient EntityManager entityManager;

	private static final long serialVersionUID = -5741469483185995309L;
	private Long id;
	private DateTime createdDate;
	private DateTime updatedDate;
	private Company company;
	private String paypalAddress;
	private String bankName;
	private String bankAccountNumber;
	private String bankBranchCode;
	private String bankBranchName;
	private String bankAccountHolder;
	private PaymentMethod paymentMethod = PaymentMethod.BANK_ACCOUNT;
	
	public enum PaymentMethod {
		PAYPAL("Paypal"), 
		BANK_ACCOUNT("Bank Account");
		
		private String displayName;
		
		private PaymentMethod(String displayName) {
			setDisplayName(displayName);
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public static final EntityManager entityManager() {
		EntityManager em = new PublisherPaymentDetails().entityManager;
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
		return entityManager().createQuery("SELECT COUNT(o) FROM PublisherPaymentDetails o", Long.class).getSingleResult();
	}

	public static List<PublisherPaymentDetails> findAll() {
		return entityManager().createQuery("SELECT o FROM PublisherPaymentDetails o", PublisherPaymentDetails.class).getResultList();
	}

	public static PublisherPaymentDetails find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(PublisherPaymentDetails.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public static PublisherPaymentDetails findByCompanyId(Long companyId) {
		if (companyId == null) {
			return null;
		}
		Criteria criteria = CriteriaUtils.createCriteria(entityManager(), PublisherPaymentDetails.class);
		criteria.add( Restrictions.eq("company.id", companyId) );
		return (PublisherPaymentDetails) criteria.getSingleResultOrNull();
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
			PublisherPaymentDetails attached = find(this.getId());
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
	public PublisherPaymentDetails merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		PublisherPaymentDetails merged = this.entityManager.merge(this);
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

	@SuppressWarnings("unused")
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
	@JoinColumn(name = "COMPANY_ID", insertable = true, updatable = false, nullable = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPaypalAddress() {
		return paypalAddress;
	}

	public void setPaypalAddress(String paypalAddress) {
		this.paypalAddress = paypalAddress;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankBranchCode() {
		return bankBranchCode;
	}

	public void setBankBranchCode(String bankBranchCode) {
		this.bankBranchCode = bankBranchCode;
	}

	public String getBankAccountHolder() {
		return bankAccountHolder;
	}

	public void setBankAccountHolder(String bankAccountHolder) {
		this.bankAccountHolder = bankAccountHolder;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
}
