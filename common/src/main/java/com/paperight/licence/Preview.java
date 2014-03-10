package com.paperight.licence;

import java.io.Serializable;

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

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.paperight.product.Product;
import com.paperight.user.Company;
import com.paperight.user.User;

@Configurable
@Entity
public class Preview implements Serializable {
	
	@PersistenceContext
	transient EntityManager entityManager;

	private static final long serialVersionUID = 5850610792935295379L;
	private Long id;
	private DateTime createdDate;
	private User user;
	private Company company;
	private Product product;
	private PageLayout layout;
	
	public static final EntityManager entityManager() {
		EntityManager em = new Preview().entityManager;
		if (em == null)
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}
	
	@SuppressWarnings("unused")
	@PrePersist
	private void onCreate() {
		this.setCreatedDate(new DateTime());
	}
	
	public static Preview find(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Preview.class, id);
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
			Preview attached = find(this.getId());
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
	public Preview merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Preview merged = this.entityManager.merge(this);
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = true, updatable = false, nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	@JoinColumn(name = "PRODUCT_ID", insertable = true, updatable = false, nullable = false)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public PageLayout getLayout() {
		return layout;
	}

	public void setLayout(PageLayout layout) {
		this.layout = layout;
	}
	

}
