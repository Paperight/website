package com.paperight.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class CompanyCreditRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	transient EntityManager entityManager;

	private Long id;
	private Company company;
	private User user;
	private BigDecimal credits;
	private DateTime createdDate;
	private String note;

	public static final EntityManager entityManager() {
		EntityManager em = new CompanyCreditRecord().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@SuppressWarnings("unused")
	@PrePersist
	private void onCreate() {
		this.setCreatedDate(new DateTime());
	}

	public static CompanyCreditRecord find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(CompanyCreditRecord.class, id);
	}

	public static List<CompanyCreditRecord> findAll() {
		return entityManager().createQuery("SELECT o FROM CompanyCreditRecord o", CompanyCreditRecord.class).getResultList();
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
			CompanyCreditRecord attached = find(this.getId());
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
	public CompanyCreditRecord merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		CompanyCreditRecord merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
	
	@Id
	@Column
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(nullable = false)
	public BigDecimal getCredits() {
		return credits;
	}
	
	public void setCredits(BigDecimal credits) {
		this.credits = credits;
	}
	
	@Column(nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(length=5000)
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public static void sortCompanyCreditRecords(List<CompanyCreditRecord> companyCreditRecords) {
		Collections.sort(companyCreditRecords, new Comparator<CompanyCreditRecord>() {
			@Override
			public int compare(CompanyCreditRecord o1, CompanyCreditRecord o2) {
				return o1.getCreatedDate().compareTo(o2.getCreatedDate()) * -1;
			}
		});
	}
	
}
